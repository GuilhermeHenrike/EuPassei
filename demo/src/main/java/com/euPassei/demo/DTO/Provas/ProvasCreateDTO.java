package com.euPassei.demo.DTO.Provas;

import com.euPassei.demo.Entity.Provas;
import com.euPassei.demo.Entity.Users;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProvasCreateDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotNull(message = "A nota é obrigatória!")
    @Min(value = 0, message = "A nota minima é 0.0")
    @Max(value = 10, message = "A nota maxima é 10.0")
    private double nota;

    private String tipo = "NORMAL";

    public ProvasCreateDTO(Provas provas) {
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
