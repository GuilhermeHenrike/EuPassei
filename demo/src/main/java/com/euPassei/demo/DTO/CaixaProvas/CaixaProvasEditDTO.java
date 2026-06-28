package com.euPassei.demo.DTO.CaixaProvas;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CaixaProvasEditDTO {

    @NotNull(message = "O Id é obrigatório")
    private Long id;

    private String titulo;
    private Double mediaMin;
    private Integer quantidade;
    private Boolean temRecuperacao;
    private Boolean temProvaFinal;
    private Double mediaMinDireitoFinal;
    private Double mediaMinFinal;
}
