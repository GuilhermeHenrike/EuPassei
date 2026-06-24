package com.euPassei.demo.DTO;

import com.euPassei.demo.Entity.CaixaProvas;
import com.euPassei.demo.Entity.Provas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String situacao;
    private Double pontosNecessarios;

    private List<ProvasDTO> listaProvas;

    public CaixaProvasResponseDTO(CaixaProvas caixa) {
        this.id = caixa.getId();
        this.titulo = caixa.getTitulo();
        this.mediaMin = caixa.getMediaMin();
        this.quantidade = caixa.getQuantidade();
        this.temRecuperacao = caixa.isTemRecuperacao();
        this.temProvaFinal = caixa.isTemProvaFinal();
        this.mediaMinDireitoFinal = caixa.getMediaMinDireitoFinal();
        this.mediaMinFinal = caixa.getMediaMinFinal();

        if (caixa.getListaProvas() != null) {
            this.listaProvas = new ArrayList<>();

            // pegando todas as provas de caixa.getListaProvas lá do Entity
            for (Provas provas : caixa.getListaProvas()) {
                // adiciona uma nova prova em DTO (a prova da vez do for) dentro de listaProvas daqui
                this.listaProvas.add(new ProvasDTO(provas));
            }
        }
    }
}
