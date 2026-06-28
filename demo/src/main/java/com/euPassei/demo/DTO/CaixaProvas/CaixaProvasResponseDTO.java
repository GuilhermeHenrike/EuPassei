package com.euPassei.demo.DTO.CaixaProvas;

import com.euPassei.demo.DTO.Provas.ProvasCreateDTO;
import com.euPassei.demo.Entity.CaixaProvas;
import com.euPassei.demo.Entity.Provas;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CaixaProvasResponseDTO {

    private Long id;
    private String titulo;
    private double mediaMin;
    private int quantidade;
    private boolean temRecuperacao;
    private boolean temProvaFinal;
    private Double mediaMinDireitoFinal;
    private Double mediaMinFinal;
    private Double mediaAtual;
    private String situacao;
    private Double pontosNecessarios;

    private List<ProvasCreateDTO> listaProvas;

    public CaixaProvasResponseDTO(CaixaProvas caixa) {
        this.id = caixa.getId();
        this.titulo = caixa.getTitulo();
        this.mediaMin = caixa.getMediaMin();
        this.quantidade = caixa.getQuantidade();
        this.temRecuperacao = caixa.getTemRecuperacao();
        this.temProvaFinal = caixa.getTemProvaFinal();
        this.mediaMinDireitoFinal = caixa.getMediaMinDireitoFinal();
        this.mediaMinFinal = caixa.getMediaMinFinal();
        this.situacao = caixa.getSituacao();
        this.pontosNecessarios = caixa.getPontosNecessarios();
        this.mediaAtual = caixa.getMediaAtual();


        if (caixa.getListaProvas() != null) {
            this.listaProvas = new ArrayList<>();

            // pegando todas as provas de caixa.getListaProvas lá do Entity
            for (Provas provas : caixa.getListaProvas()) {
                // adiciona uma nova prova em DTO (a prova da vez do for) dentro de listaProvas daqui
                this.listaProvas.add(new ProvasCreateDTO(provas));
            }
        }
    }
}
