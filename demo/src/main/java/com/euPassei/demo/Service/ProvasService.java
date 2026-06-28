package com.euPassei.demo.Service;

import com.euPassei.demo.DTO.Provas.ProvasEditDTO;
import com.euPassei.demo.Entity.CaixaProvas;
import com.euPassei.demo.Entity.Provas;
import com.euPassei.demo.Repository.CaixaProvasRepository;
import com.euPassei.demo.Repository.ProvasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProvasService {

    @Autowired
    private ProvasRepository provasRepository;

    @Autowired
    private CaixaProvasRepository caixaProvasRepository;

    @Autowired
    private CaixaProvasService caixaProvasService;

    @Transactional
    public Provas salvar(Provas prova, Long caixaId) {
        CaixaProvas caixaDona = caixaProvasRepository.findById(caixaId).orElse(null);

        if (caixaDona == null) {
            throw new IllegalArgumentException("Matéria não encontrada");
        }

        // seta a caixa da prova como a caixa dona que veio do id
        prova.setCaixaProvas(caixaDona);
        // salva a prova no banco pra ela ter atributos
        Provas provasSalvo = provasRepository.save(prova);
        // adiciona a prova salva no banco na lista de provas da caixa dona
        caixaDona.getListaProvas().add(provasSalvo);
        // adiciona as regras nas provas da caixa
        caixaProvasService.calcularMedia(caixaDona);
        // salva na caixa (tipo um commit contendo as as regras de cima)
        caixaProvasRepository.save(caixaDona);
        return provasSalvo;
    }

    @Transactional
    public void mudarProva(ProvasEditDTO prova, Long caixaId) {

        // caso mande a prova nula
        if (prova == null || prova.getId() == null) {
            throw new IllegalArgumentException("Id da prova inexistente");
        }

        Optional<Provas> provaOPT = provasRepository.findById(prova.getId());
        // a prova ja vem com a caixa dela dentro por causa do ManyToOne la

        if (provaOPT.isEmpty()) {
            throw new IllegalArgumentException("Prova inexistente");
        }

        // capturando a prova e pegando a caixa da prova
        Provas provaAntiga = provaOPT.get();
        CaixaProvas caixa = provaAntiga.getCaixaProvas();

        // checando se a caixa ta nula ou se o id da caixa é diferente do id passado
        if (caixa == null || !caixa.getId().equals(caixaId)) {
            throw new IllegalArgumentException("Caixa de provas inexistente");
        }

        if (prova.getTitulo() != null) {
            provaAntiga.setTitulo(prova.getTitulo());
        }

        if (prova.getNota() != null) {
            provaAntiga.setNota(prova.getNota());
        }

        provasRepository.save(provaAntiga);
        caixaProvasService.calcularMedia(caixa);
        caixaProvasRepository.save(caixa);
    }

    @Transactional
    public void deletar(Long provaId, Long caixaId) {

        if (provaId == null) {
            throw new IllegalArgumentException("Id da prova inexistente");
        }

        Optional<Provas> provaDeletarOPT = provasRepository.findById(provaId);

        if (provaDeletarOPT.isEmpty()) {
            throw new IllegalArgumentException("Dados incorretos");
        }

        Provas provaDeletar = provaDeletarOPT.get();
        CaixaProvas caixa = provaDeletar.getCaixaProvas();

        // checa se a caixa é nula ou se o caixaId passado bate com o id da caixa (ai de cima)
        // se não da esse erro
        if (caixa == null || !caixa.getId().equals(caixaId)) {
            throw new IllegalArgumentException("Dados incorretos");
        }

        caixa.getListaProvas().remove(provaDeletar);
        caixaProvasService.calcularMedia(caixa);
        caixaProvasRepository.save(caixa);
        provasRepository.delete(provaDeletar);
    }


}
