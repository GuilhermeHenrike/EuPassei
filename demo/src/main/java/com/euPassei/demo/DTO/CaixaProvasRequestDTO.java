package com.euPassei.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaixaProvasRequestDTO {

    @NotBlank(message = "O título é obrigatório!")
    private String titulo;

    @NotNull(message = "A média mínima é obrigatória!")
    private Double mediaMin;

    @NotNull(message = "A quantidade de provas é obrigatória!")
    private Integer quantidade;

    private boolean temRecuperacao;
    private boolean temProvaFinal;
    private Double mediaMinDireitoFinal;
    private Double mediaMinFinal;


}
