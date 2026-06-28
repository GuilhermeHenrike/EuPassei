package com.euPassei.demo.DTO.Provas;

import com.euPassei.demo.Entity.Provas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProvasEditDTO {

    @NotNull(message = "O Id é obrigatório para edição")
    private Long id;
    private String titulo;
    private Double nota;
    private String tipo = "NORMAL";

    public ProvasEditDTO(Provas provas) {
        this.id = provas.getId();
        this.titulo = provas.getTitulo();
        this.nota = provas.getNota();
        this.tipo = provas.getTipo();
    }

    public Provas toEntity() {
        Provas provas = new Provas();
        provas.setId(this.id);
        provas.setTitulo(this.titulo);
        provas.setNota(this.nota);
        provas.setTipo(this.tipo);
        return provas;
    }

}
