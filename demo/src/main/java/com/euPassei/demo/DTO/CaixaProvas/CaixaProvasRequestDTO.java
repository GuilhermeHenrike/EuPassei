package com.euPassei.demo.DTO.CaixaProvas;

import com.euPassei.demo.Entity.CaixaProvas;
import jakarta.validation.constraints.*;
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

    public CaixaProvas toEntity() {
        CaixaProvas caixaProvas = new CaixaProvas();
        caixaProvas.setTitulo(this.titulo);
        caixaProvas.setMediaMin(this.mediaMin);
        caixaProvas.setQuantidade(this.quantidade);
        caixaProvas.setTemRecuperacao(this.temRecuperacao);
        caixaProvas.setTemProvaFinal(this.temProvaFinal);
        caixaProvas.setMediaMinDireitoFinal(this.mediaMinDireitoFinal);
        caixaProvas.setMediaMinFinal(this.mediaMinFinal);
        return caixaProvas;
    }

}
