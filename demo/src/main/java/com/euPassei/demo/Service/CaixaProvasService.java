package com.euPassei.demo.Service;

import com.euPassei.demo.DTO.UsersDTO;
import com.euPassei.demo.Entity.CaixaProvas;
import com.euPassei.demo.Entity.Provas;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Repository.CaixaProvasRepository;
import com.euPassei.demo.Repository.ProvasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaixaProvasService {

    @Autowired
    private CaixaProvasRepository caixaProvasRepository;

    public CaixaProvas calcularMedia(CaixaProvas caixa) {

        // quando não se tem nada pra calcular ainda
        if (caixa.getListaProvas() == null || caixa.getListaProvas().isEmpty()) {
            caixa.setMediaAtual(0.0);
            caixa.setSituacao("Sem Notas");
            caixa.setPontosNecessarios(0.0);
            return caixa;
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
        double media = notas / qntProvasNormais;
        caixa.setMediaAtual(media);

        if (caixa.isTemProvaFinal()) {
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

    return caixa;
    }

    public CaixaProvas criarCaixaProvas(CaixaProvas caixaProvas, Long userId) {

        if (userId == null) {
            throw new RuntimeException("Você precisa estar logado!");
        }

        // cria um usuario vazio
        Users userAtual = new Users();

        // coloca o id que veio nele
        userAtual.setId(userId);

        // coloca o usuario como dono da tarefa
        caixaProvas.setUser(userAtual);
        calcularMedia(caixaProvas);
        return caixaProvasRepository.save(caixaProvas);
    }
}
