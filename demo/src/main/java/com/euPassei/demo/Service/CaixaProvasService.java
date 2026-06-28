package com.euPassei.demo.Service;

import com.euPassei.demo.DTO.CaixaProvas.CaixaProvasEditDTO;
import com.euPassei.demo.Entity.CaixaProvas;
import com.euPassei.demo.Entity.Provas;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Repository.CaixaProvasRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CaixaProvasService {

    @Autowired
    private CaixaProvasRepository caixaProvasRepository;

    public void calcularMedia(CaixaProvas caixa) {

        // quando não se tem nada pra calcular ainda
        if (caixa.getListaProvas() == null || caixa.getListaProvas().isEmpty()) {
            caixa.setMediaAtual(0.0);
            caixa.setSituacao("Sem Notas");
            caixa.setPontosNecessarios(0.0);
            return;
        }

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
        double media = notas / qntProvasNormais;
        caixa.setMediaAtual(media);

        // só vai se não for nulo e for verdadeiro
        if (caixa.getTemProvaFinal() != null && caixa.getTemProvaFinal()) {
            if (notaFinal != null) {
                double mediaFinal = (caixa.getMediaAtual() + notaFinal) / 2;
                caixa.setMediaAtual(mediaFinal);

                // FEZ A PROVA FINAL
                if (mediaFinal >= caixa.getMediaMinFinal()) {
                    caixa.setSituacao("Aprovado");
                    caixa.setPontosNecessarios(0.0);
                } else {
                    caixa.setSituacao("Reprovado");
                    caixa.setPontosNecessarios(0.0);
                }

                // NAO FEZ AINDA
            } else {
                if (caixa.getMediaAtual() >= caixa.getMediaMin()) {
                    caixa.setSituacao("Aprovado");
                    caixa.setPontosNecessarios(0.0);
                } else if (caixa.getMediaAtual() >= caixa.getMediaMinDireitoFinal()) {
                    caixa.setSituacao("Prova Final");

                    // nota necessaria pra quem ficou na final
                    double notaNecessaria = (2 * caixa.getMediaMinFinal()) - caixa.getMediaAtual();
                    caixa.setPontosNecessarios(notaNecessaria);

                } else {
                    caixa.setSituacao("Reprovado");
                    caixa.setPontosNecessarios(0.0);
                }
            }

            // NAO TEM FINAL
        } else {
            if (caixa.getMediaAtual() >= caixa.getMediaMin()) {
                caixa.setSituacao("Aprovado");
                caixa.setPontosNecessarios(0.0);
            } else {
                caixa.setSituacao("Reprovado");
                caixa.setPontosNecessarios(0.0);
            }
        }
    }

    @Transactional
    public CaixaProvas criarCaixaProvas(CaixaProvas caixaProvas, Long userId) {

        if (userId == null) {
            throw new RuntimeException("Você precisa estar logado!");
        }

        // cria um usuario vazio
        Users userAtual = new Users();

        // coloca o id que veio nele
        userAtual.setId(userId);

        // coloca o usuario como dono da caixa
        caixaProvas.setUser(userAtual);
        calcularMedia(caixaProvas);
        return caixaProvasRepository.save(caixaProvas);
    }

    @Transactional(readOnly = true) // pro metodo conseguir retornar as entidades da lista da caixa
    public List<Provas> mostrarProvasDaCaixa(Long caixaId, Long userId) {
        Optional<CaixaProvas> caixaProvasOPT =  caixaProvasRepository.findByIdAndUserId(caixaId, userId);

        if (caixaProvasOPT.isPresent()) {
            CaixaProvas caixaProvasAchada = caixaProvasOPT.get();

            return caixaProvasAchada.getListaProvas();
        }

        throw new IllegalArgumentException("Caixa de provas não encontrada ou você não tem permissão!");
    }

    @Transactional
    public CaixaProvas editarCaixa(Long userId, Long caixaId, CaixaProvasEditDTO caixaProvas) {
        // ai ele ta pegando o id da caixa e do user e ta procurando qual caixa do user tem id = caixaId
        // ai se ele acha o metodo ja sabe que essa é a caixa que o usuario ta mudando
        Optional<CaixaProvas> caixaOPT = caixaProvasRepository.findByIdAndUserId(caixaId, userId);

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
}
