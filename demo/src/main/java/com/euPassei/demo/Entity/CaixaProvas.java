package com.euPassei.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "caixaProvas")
public class CaixaProvas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Double mediaMin;
    private Integer quantidade;
    private Boolean temRecuperacao;
    private Boolean temProvaFinal;
    private Double mediaMinDireitoFinal;
    private Double mediaMinFinal;
    private Double mediaAtual;
    private String situacao;
    private Double pontosNecessarios;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @OneToMany(mappedBy = "caixaProvas", cascade = CascadeType.ALL)
    private List<Provas> listaProvas;

    // para fazer os botões sumirem quando atinge o limite
    @JsonIgnore
    public boolean isPodeAdicionarMaisProvasNormais() {
        if (this.listaProvas == null) {
            return true; // se tiver nula (vazia) é pq pode adicionar
        }

        // conta quantas provas de tipo "NORMAL" tem na caixa
        long totalProvasNormais = this.listaProvas.stream()
                .filter(provas -> "NORMAL".equals(provas.getTipo())).count();

        // enquanto for menor que quantidade ele continua
        return totalProvasNormais < this.quantidade;
    }
}
