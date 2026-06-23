package com.euPassei.demo.DTO;

import com.euPassei.demo.Entity.Provas;
import lombok.Getter;

@Getter
public class ProvasDTO {

    private Long id;
    private String titulo;
    private double nota;

    private double necessario; // quantos pontos ainda faltam
    private String resultado; // se passou ou não

    public ProvasDTO(Provas provas) {
        this.id = provas.getId();
        this.titulo = provas.getTitulo();
        this.nota = provas.getNota();
    }

}
