package com.jefferson.geradorhorarios.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<DisponibilidadeProfessor> disponibilidades = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "professor_disciplina",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Disciplina> disciplinasLecionadas = new HashSet<>();

    public void addDisponibilidade(DisponibilidadeProfessor disponibilidade) {
        this.disponibilidades.add(disponibilidade);
        disponibilidade.setProfessor(this);
    }

    public void removeDisponibilidade(DisponibilidadeProfessor disponibilidade) {
        this.disponibilidades.remove(disponibilidade);
        disponibilidade.setProfessor(null);
    }

    public void addDisciplina(Disciplina disciplina) {
        this.disciplinasLecionadas.add(disciplina);
        // RE-ADICIONADA: Mantenha esta linha para consistência bidirecional
        disciplina.getProfessores().add(this);
    }

    public void removeDisciplina(Disciplina disciplina) {
        this.disciplinasLecionadas.remove(disciplina);
        // RE-ADICIONADA: Mantenha esta linha para consistência bidirecional
        disciplina.getProfessores().remove(this);
    }
}
