package com.jefferson.geradorhorarios.model;

import jakarta.persistence.*;
import lombok.Data; // Anotação do Lombok para gerar getters, setters, toString, equals e hashCode
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity // Indica que esta classe é uma entidade JPA e será mapeada para uma tabela no DB
@Data // Anotação Lombok para gerar getters, setters, toString, equals e hashCode
@NoArgsConstructor // Anotação Lombok para gerar um construtor sem argumentos
@AllArgsConstructor // Anotação Lombok para gerar um construtor com todos os argumentos
public class Professor {

    @Id // Indica que este campo é a chave primária da entidade
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a estratégia de geração de valor para a chave primária (auto-incremento)
    private Long id;

    @Column(nullable = false) // Indica que a coluna não pode ser nula no banco de dados
    private String nome;

    @Column(nullable = false, unique = true) // Não nulo e deve ser único
    private String email;

    // Relacionamento One-to-Many com DisponibilidadeProfessor
    // mappedBy indica o nome do campo na classe DisponibilidadeProfessor que possui a chave estrangeira
    // CascadeType.ALL significa que operações (persist, remove) em Professor afetam suas Disponibilidades
    // orphanRemoval = true garante que disponibilidades órfãs (que não estão mais ligadas a um professor) sejam removidas
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DisponibilidadeProfessor> disponibilidades = new HashSet<>();

    // Relacionamento Many-to-Many com Disciplina
    // @ManyToMany: Indica um relacionamento muitos-para-muitos
    // @JoinTable: Define a tabela de junção para o relacionamento
    // name: Nome da tabela de junção
    // joinColumns: Colunas que referenciam esta entidade (Professor) na tabela de junção
    // inverseJoinColumns: Colunas que referenciam a outra entidade (Disciplina) na tabela de junção
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "professor_disciplina", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "professor_id"), // Coluna que referencia Professor
            inverseJoinColumns = @JoinColumn(name = "disciplina_id") // Coluna que referencia Disciplina
    )
    private Set<Disciplina> disciplinasLecionadas = new HashSet<>();

    // Métodos auxiliares para adicionar/remover disponibilidades e disciplinas
    public void addDisponibilidade(DisponibilidadeProfessor disponibilidade) {
        disponibilidades.add(disponibilidade);
        disponibilidade.setProfessor(this);
    }

    public void removeDisponibilidade(DisponibilidadeProfessor disponibilidade) {
        disponibilidades.remove(disponibilidade);
        disponibilidade.setProfessor(null);
    }

    public void addDisciplina(Disciplina disciplina) {
        disciplinasLecionadas.add(disciplina);
        // O relacionamento Many-to-Many é gerenciado de um lado, geralmente do lado que tem o @JoinTable.
        // No entanto, para garantir a consistência bidirecional, podemos adicionar a disciplina ao professor e vice-versa.
        // disciplina.getProfessores().add(this); // Isso seria necessário se tivéssemos um Set<Professor> em Disciplina e quiséssemos gerenciá-lo.
        // Mas por simplicidade e para evitar loop infinito, muitas vezes é gerenciado apenas de um lado principal.
    }

    public void removeDisciplina(Disciplina disciplina) {
        disciplinasLecionadas.remove(disciplina);
        // disciplina.getProfessores().remove(this); // Similarmente, se fosse gerenciado nos dois lados
    }
}
