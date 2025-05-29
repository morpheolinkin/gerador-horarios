package com.jefferson.geradorhorarios.dto;

import com.jefferson.geradorhorarios.model.Turma;

// DTO para Turma
public record TurmaDTO(
        Long id,
        String nome
) {
    public static TurmaDTO fromEntity(Turma turma) {
        return new TurmaDTO(
                turma.getId(),
                turma.getNome()
        );
    }
}
