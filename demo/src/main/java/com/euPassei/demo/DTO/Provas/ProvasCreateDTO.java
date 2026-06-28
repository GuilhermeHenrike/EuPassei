package com.euPassei.demo.DTO.Provas;

import com.euPassei.demo.Entity.Provas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProvasDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotNull(message = "A nota é obrigatória!")
    private Double nota;

    private String tipo = "NORMAL";

    public ProvasDTO(Provas provas) {
        this.id = provas.getId();
        this.titulo = provas.getTitulo();
        this.nota = provas.getNota();
        this.tipo = provas.getTipo();
    }

}
