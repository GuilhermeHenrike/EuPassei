package com.euPassei.demo.Service;

import com.euPassei.demo.DTO.CaixaProvas.CaixaProvasEditDTO;
import com.euPassei.demo.Entity.CaixaProvas;
import com.euPassei.demo.Entity.Provas;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Repository.CaixaProvasRepository;
import com.euPassei.demo.Repository.UsersRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CaixaProvasService {

    @Autowired
    private CaixaProvasRepository caixaProvasRepository;

    @Autowired
    private UsersRepository usersRepository;

    public void calcularMedia(CaixaProvas caixa) {

        // quando não se tem nada pra calcular ainda
        if (caixa.getListaProvas() == null || caixa.getListaProvas().isEmpty()) {
            caixa.setMediaAtual(0.0);
            caixa.setSituacao("Sem Notas");
            caixa.setPontosNecessarios(0.0);
            return;
        }

        // para não deixar o limite exceder
        long qntProvas = caixa.getListaProvas()
                .stream().filter(provas -> provas.getTipo().equals("NORMAL"))
                .count();

        if (qntProvas > caixa.getQuantidade()) {
            throw new IllegalArgumentException("Limite de provas excedido!");
        }

        // nota de tipos diferentes de prova
        double notas = 0;
        Double notaFinal = null;

        for (Provas provas : caixa.getListaProvas()) {
            if (provas.getTipo().equals("NORMAL")) {
                notas += provas.getNota();
            }
            else if (provas.getTipo().equals("FINAL")) {
                notaFinal = provas.getNota();
            }
        }

        Integer qntProvasNormais = caixa.getQuantidade();
        if (qntProvasNormais <= 0) {
            qntProvasNormais = 1;
        }

        // Calcula a média normal baseada na quantidade de provas
        double media = notas / qntProvasNormais;
        caixa.setMediaAtual(media);

        boolean temProvaFinalAtiva = Boolean.TRUE.equals(caixa.getTemProvaFinal());

        // 1. Aprovado Direto pela média normal
        if (media >= caixa.getMediaMin()) {
            caixa.setSituacao("Aprovado");
            caixa.setPontosNecessarios(0.0);
        }
        // 2. JÁ FEZ A PROVA FINAL
        else if (temProvaFinalAtiva && notaFinal != null) {

            if (caixa.getMediaMinFinal() != null && notaFinal < caixa.getMediaMinFinal()) {
                caixa.setSituacao("Reprovado");
            } else {
                double mediaFinal = (caixa.getMediaAtual() + notaFinal) / 2.0;
                caixa.setMediaAtual(mediaFinal);

                double notaCorteFinal = (caixa.getMediaMinFinal() != null) ? caixa.getMediaMinFinal() : caixa.getMediaMin();

                if (mediaFinal >= notaCorteFinal) {
                    caixa.setSituacao("Aprovado");
                } else {
                    caixa.setSituacao("Reprovado");
                }
            }
            caixa.setPontosNecessarios(0.0);
        }
        // 3. AINDA NÃO FEZ A PROVA FINAL, MAS TEM DIREITO A ELA
        else if (temProvaFinalAtiva &&
                (caixa.getMediaMinDireitoFinal() == null || media >= caixa.getMediaMinDireitoFinal())) {

            caixa.setSituacao("Prova Final");

            // Define qual é a nota de corte alvo (se não tiver a da final específica, usa a média mínima geral)
            double corteAlvo = (caixa.getMediaMinFinal() != null) ? caixa.getMediaMinFinal() : caixa.getMediaMin();

            // 1. Calcula quanto ele precisa tirar pela fórmula da média final usando o corte correto
            double notaNecessariaMedia = (2 * corteAlvo) - media;

            // 2. Pega a nota mínima absoluta exigida só para fazer/passar na final (se houver)
            double notaMinimaFinalExigida = (caixa.getMediaMinFinal() != null) ? caixa.getMediaMinFinal() : 0.0;

            // 3. O aluno precisará tirar o MAIOR valor entre o que a média exige e a nota mínima obrigatória
            double notaNecessariaFinal = Math.max(notaNecessariaMedia, notaMinimaFinalExigida);

            caixa.setPontosNecessarios(Math.max(0, notaNecessariaFinal));
        }
        // 4. REPROVADO
        else {
            caixa.setSituacao("Reprovado");
            caixa.setPontosNecessarios(0.0);
        }
    }

    @Transactional
    public void criarCaixaProvas(CaixaProvas caixaProvas, Users userLogado) {

        if (userLogado == null) {
            throw new RuntimeException("Você precisa estar logado!");
        }

        Users userAtual = usersRepository.findById(userLogado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        caixaProvas.setUser(userAtual);
        calcularMedia(caixaProvas);
        caixaProvasRepository.save(caixaProvas);
    }

    @Transactional
    public CaixaProvas editarCaixa(Long caixaId, CaixaProvasEditDTO caixaProvas, Users userLogado) {

        if (userLogado == null) {
            throw new IllegalArgumentException("Você precisa estar logado!");
        }

        // ai ele ta pegando o id da caixa e do user e ta procurando qual caixa do user tem id = caixaId
        // ai se ele acha o metodo ja sabe que essa é a caixa que o usuario ta mudando
        Optional<CaixaProvas> caixaOPT = caixaProvasRepository.findByIdAndUserId(caixaId, userLogado.getId());

        if (caixaOPT.isEmpty()) {
            throw new IllegalArgumentException("Caixa de provas não encontradas ou sem permissão!");
        }

        CaixaProvas caixaAtual = caixaOPT.get();

        // Esse if serve para evitar que a pessoa não consiga desmarcar o temProvaFinal
        // antes de apagar a prova final que já existia, se não fizesse isso ia dar ao calcular média

        // Pega a caixa que o usuário mandou e vê se se ele desmarcou o temProvaFinal.
        // Se ele já tiver prova bate aí e o método bloqueia a ação.
        // stream é como um For que passa por todas as provas e o
        // anyMatch procura se alguma tem o tipo FINAL. Se ele achar bate no if de baixo.
        if (caixaProvas.getTemProvaFinal() != null && !caixaProvas.getTemProvaFinal()) {
            boolean provaFinalSalva = caixaAtual.getListaProvas().stream()
                    .anyMatch(prova -> prova.getTipo().equals("FINAL"));

            if (provaFinalSalva) {
                throw new IllegalArgumentException("Não é possivel desativar a opção, apague a prova final existente!");
            }
        }

        if (caixaProvas.getTitulo() != null) {
            caixaAtual.setTitulo(caixaProvas.getTitulo());
        }

        if (caixaProvas.getMediaMin() != null) {
            caixaAtual.setMediaMin(caixaProvas.getMediaMin());
        }

        if (caixaProvas.getQuantidade() != null) {
            caixaAtual.setQuantidade(caixaProvas.getQuantidade());
        }

        if (caixaProvas.getTemRecuperacao() != null) {
            caixaAtual.setTemRecuperacao(caixaProvas.getTemRecuperacao());
        }

        if (caixaProvas.getTemProvaFinal() != null) {
            caixaAtual.setTemProvaFinal(caixaProvas.getTemProvaFinal());
        }

        if (caixaProvas.getMediaMinDireitoFinal() != null) {
            caixaAtual.setMediaMinDireitoFinal(caixaProvas.getMediaMinDireitoFinal());
        }

        if (caixaProvas.getMediaMinFinal() != null) {
            caixaAtual.setMediaMinFinal(caixaProvas.getMediaMinFinal());
        }

        calcularMedia(caixaAtual);
        return caixaProvasRepository.save(caixaAtual);
    }

    @Transactional
    public void excluirCaixaProvas(Long caixaId, Users userLogado) {

        if (userLogado == null) {
            throw new IllegalArgumentException("Você precisa estar logado");
        }

        // checa se acha uma caixa pelo id da caixa e do usuario, se não joga erro
        CaixaProvas caixaAtual = caixaProvasRepository.findByIdAndUserId(caixaId, userLogado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Caixa não encontrada ou não existe ou você não tem permissão!"));

        caixaProvasRepository.delete(caixaAtual);
    }

    // lista todas as caixas do usuario pelo id dele
    @Transactional(readOnly = true)
    public List<CaixaProvas> listarTodasCaixasDoUsuario(Users userLogado) {

        if (userLogado == null) {
            throw new IllegalArgumentException("Você precisa estar logado!");
        }

        // Busca todas as caixas onde o ID do usuário seja igual ao logado
        return caixaProvasRepository.findByUserId(userLogado.getId());
    }
}