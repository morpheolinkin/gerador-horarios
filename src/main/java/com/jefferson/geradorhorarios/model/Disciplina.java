package com.jefferson.geradorhorarios.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disciplina implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Nome da disciplina deve ser único
    private String nome;

    @Column(nullable = false)
    private Integer cargaHorariaSemanal; // Número de aulas por semana

    // Relacionamento Many-to-Many com Professor (lado inverso do Professor)
    // mappedBy indica que o relacionamento é gerenciado pelo campo "disciplinasLecionadas" na classe Professor
    // Isso evita a criação de duas tabelas de junção e define quem é o "dono" do relacionamento
    @ManyToMany(mappedBy = "disciplinasLecionadas", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude // IMPORTANTE: Exclui este campo de equals/hashCode
    @ToString.Exclude // IMPORTANTE: Exclui este campo de toString
    @JsonIgnore
    private Set<Professor> professores = new HashSet<>();
}