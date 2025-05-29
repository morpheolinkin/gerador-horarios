package com.jefferson.geradorhorarios.repository;

import com.jefferson.geradorhorarios.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
    // Métodos de busca personalizados:

    /**
     * Encontra todas as aulas associadas a um determinado professor.
     *
     * @param professorId O 'ID' do professor.
     * @return Uma lista de aulas do professor.
     */
    List<Aula> findByProfessorId(Long professorId);

    // Método para carregar todas as aulas com seus relacionamentos de forma EAGER
    @Query("SELECT a FROM Aula a JOIN FETCH a.disciplina JOIN FETCH a.professor JOIN FETCH a.turma")
    List<Aula> findAllWithDetails();

    /**
     * Encontra todas as aulas associadas a uma determinada turma.
     *
     * @param turmaId O 'ID' da turma.
     * @return Uma lista de aulas da turma.
     */
    List<Aula> findByTurmaId(Long turmaId);

    /**
     * Encontra aulas para um dado dia da semana sobreposta a um intervalo de tempo.
     * Isso significa que a aula começa antes ou igual ao fim do intervalo E termina depois ou igual ao início do intervalo.
     *
     * @param diaSemana              O dia da semana.
     * @param horarioFimIntervalo    O horário de fim do intervalo de busca.
     * @param horarioInicioIntervalo O horário de início do intervalo de busca.
     * @return Uma lista de aulas sobrepostas ao intervalo.
     */
    List<Aula> findByDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
            DayOfWeek diaSemana, LocalTime horarioFimIntervalo, LocalTime horarioInicioIntervalo);

    /**
     * Encontra aulas de um professor específico para um dado dia da semana sobreposta a um intervalo de tempo.
     *
     * @param professorId            O 'ID' do professor.
     * @param diaSemana              O dia da semana.
     * @param horarioFimIntervalo    O horário de fim do intervalo de busca.
     * @param horarioInicioIntervalo O horário de início do intervalo de busca.
     * @return Uma lista de aulas do professor sobreposto ao intervalo.
     */
    List<Aula> findByProfessorIdAndDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
            Long professorId, DayOfWeek diaSemana, LocalTime horarioFimIntervalo, LocalTime horarioInicioIntervalo);

    /**
     * Encontra aulas de uma turma específica para um dado dia da semana sobreposta a um intervalo de tempo.
     *
     * @param turmaId                O 'ID' da turma.
     * @param diaSemana              O dia da semana.
     * @param horarioFimIntervalo    O horário de fim do intervalo de busca.
     * @param horarioInicioIntervalo O horário de início do intervalo de busca.
     * @return Uma lista de aulas da turma sobreposta ao intervalo.
     */
    List<Aula> findByTurmaIdAndDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
            Long turmaId, DayOfWeek diaSemana, LocalTime horarioFimIntervalo, LocalTime horarioInicioIntervalo);
}
