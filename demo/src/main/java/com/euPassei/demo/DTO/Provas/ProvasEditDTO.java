package com.euPassei.demo.DTO.Provas;

import com.euPassei.demo.Entity.Provas;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(value = 0, message = "A nota minima é 0.0")
    @Max(value = 10, message = "A nota maxima é 10.0")
    private Double nota;

    public ProvasEditDTO(Provas provas) {
        this.id = provas.getId();
        this.titulo = provas.getTitulo();
        this.nota = provas.getNota();
    }

    public Provas toEntity() {
        Provas provas = new Provas();
        provas.setId(this.id);
        provas.setTitulo(this.titulo);
        provas.setNota(this.nota);
        return provas;
    }

}
