package com.jefferson.geradorhorarios.dto;

import com.jefferson.geradorhorarios.model.Disciplina;
import com.jefferson.geradorhorarios.model.Professor;

import java.util.List;
import java.util.stream.Collectors;

// Um Record para representar os dados de um Professor na API
// Não inclui as coleções completas para evitar loops ou carregamento desnecessário
public record ProfessorDTO(
        Long id,
        String nome,
        String email,
        // Podemos incluir IDs de disciplinas lecionadas se quisermos apenas as referências
        List<Long> disciplinasLecionadasIds
) {
    // Construtor "canônico" é gerado automaticamente
    // Você pode adicionar um método de fábrica ou um construtor extra para mapeamento
    public static ProfessorDTO fromEntity(Professor professor) {
        List<Long> disciplinaIds = null;
        // Verifica se a coleção está inicializada e não é nula antes de tentar acessá-la
        if (professor.getDisciplinasLecionadas() != null && !professor.getDisciplinasLecionadas().isEmpty()) {
            disciplinaIds = professor.getDisciplinasLecionadas().stream()
                    .map(Disciplina::getId)
                    .collect(Collectors.toList());
        }
        return new ProfessorDTO(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                disciplinaIds
        );
    }
}
