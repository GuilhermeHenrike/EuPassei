package com.euPassei.demo.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "O titulo da prova é obrigatório!")
    private String titulo;

    @NotNull(message = "A nota é obrigatória!")
    private double nota;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
