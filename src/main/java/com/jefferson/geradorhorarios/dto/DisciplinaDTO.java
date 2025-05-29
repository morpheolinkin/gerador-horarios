package com.jefferson.geradorhorarios.dto;

import com.jefferson.geradorhorarios.model.Disciplina;

// DTO para Disciplina (sem a lista de professores para evitar loops)
public record DisciplinaDTO(
        Long id,
        String nome,
        Integer cargaHorariaSemanal
) {
    public static DisciplinaDTO fromEntity(Disciplina disciplina) {
        return new DisciplinaDTO(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getCargaHorariaSemanal()
        );
    }
}
