package com.euPassei.demo.DTO;

import com.euPassei.demo.Entity.Provas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProvasDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotNull(message = "A nota é obrigatória!")
    private double nota;

    private String tipo = "NORMAL";

    public ProvasDTO(Provas provas) {
        this.id = provas.getId();
        this.titulo = provas.getTitulo();
        this.nota = provas.getNota();
        this.tipo = provas.getTipo();
    }

}
