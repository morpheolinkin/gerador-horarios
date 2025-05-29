package com.jefferson.geradorhorarios.dto;

import com.jefferson.geradorhorarios.model.Aula;

import java.time.DayOfWeek;
import java.time.LocalTime;

// DTO para Aula
public record AulaDTO(
        Long id,
        // Podemos incluir DTOs aninhados para mais detalhes
        DisciplinaDTO disciplina,
        ProfessorDTO professor, // Usamos ProfessorDTO para evitar loop e LazyInitializationException
        TurmaDTO turma,
        DayOfWeek diaSemana,
        LocalTime horarioInicio,
        LocalTime horarioFim
) {
    public static AulaDTO fromEntity(Aula aula) {
        // Para evitar LazyInitializationException ao acessar disciplinasLecionadas do Professor dentro do ProfessorDTO
        // e problemas com @JsonIgnore no ProfessorDTO se ele tentasse serializar Disciplinas
        // Usaremos uma versão simplificada do ProfessorDTO ou carregaremos EAGERLY para o DTO.
        // Já que estamos indo para DTOs, vamos garantir que só o necessário é carregado.
        // O ProfessorDTO.fromEntity que criamos para ProfessorDTO já lida com a lazy init de disciplinasLecionadas
        // (ele só pega IDs).

        return new AulaDTO(
                aula.getId(),
                // Mapeia entidades para seus respectivos DTOs
                DisciplinaDTO.fromEntity(aula.getDisciplina()),
                ProfessorDTO.fromEntity(aula.getProfessor()), // Mapeia o Professor para ProfessorDTO
                TurmaDTO.fromEntity(aula.getTurma()),
                aula.getDiaSemana(),
                aula.getHorarioInicio(),
                aula.getHorarioFim()
        );
    }
}
