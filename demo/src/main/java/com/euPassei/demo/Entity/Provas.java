package com.euPassei.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "provas")
public class Provas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Double nota;
    private String tipo = "NORMAL";

    @ManyToOne
    @JoinColumn(name = "caixa_id")
    @JsonBackReference // pra evitar o loop infinito
    private CaixaProvas caixaProvas;
}
