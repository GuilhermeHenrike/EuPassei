package com.euPassei.demo.DTO.CaixaProvas;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CaixaProvasEditDTO {

    private String titulo;
    private Double mediaMin;
    private Integer quantidade;
    private Boolean temRecuperacao;
    private Boolean temProvaFinal;
    private Double mediaMinDireitoFinal;
    private Double mediaMinFinal;
}
