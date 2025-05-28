package com.jefferson.geradorhorarios.service;

import com.jefferson.geradorhorarios.exception.ResourceNotFoundException;
import com.jefferson.geradorhorarios.model.Aula;
import com.jefferson.geradorhorarios.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service // Indica que esta classe é um componente de serviço do Spring
public class AulaService {

    private final AulaRepository aulaRepository; // Injeção de dependência do repositório

    @Autowired // Anotação para injeção de dependência
    public AulaService(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    /**
     * Salva uma nova aula ou atualiza uma aula existente.
     *
     * @param aula O objeto Aula a ser salvo.
     * @return A aula salva/atualizada.
     */
    public Aula salvarAula(Aula aula) {
        // Aqui, futuramente, poderíamos adicionar validações de negócio
        // antes de salvar a aula, como verificar choques de horário,
        // disponibilidade do professor, etc.
        return aulaRepository.save(aula);
    }

    /**
     * Busca uma aula pelo 'ID'. Lança ResourceNotFoundException se não encontrada.
     *
     * @param id O 'ID' da aula.
     * @return A aula encontrada.
     * @throws ResourceNotFoundException Se a aula não for encontrada.
     */
    public Aula buscarAulaPorId(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com ID: " + id));
    }

    /**
     * Lista todas as aulas.
     *
     * @return Uma lista de todas as aulas.
     */
    public List<Aula> listarTodasAulas() {
        return aulaRepository.findAll();
    }

    /**
     * Deleta uma aula pelo 'ID'. Lança ResourceNotFoundException se a aula não existir.
     *
     * @param id O 'ID' da aula a ser deletada.
     * @throws ResourceNotFoundException Se a aula não for encontrada.
     */
    public void deletarAula(Long id) {
        if (!aulaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aula não encontrada com ID: " + id);
        }
        aulaRepository.deleteById(id);
    }

    /**
     * Busca todas as aulas de um professor específico.
     * Para isso, precisamos adicionar um método no AulaRepository.
     *
     * @param professorId O ID do professor.
     * @return Uma lista de aulas do professor.
     */
    public List<Aula> buscarAulasPorProfessor(Long professorId) {
        return aulaRepository.findByProfessorId(professorId);
    }

    /**
     * Busca todas as aulas de uma turma específica.
     * Para isso, precisamos adicionar um método no AulaRepository.
     *
     * @param turmaId O 'ID' da turma.
     * @return Uma lista de aulas da turma.
     */
    public List<Aula> buscarAulasPorTurma(Long turmaId) {
        return aulaRepository.findByTurmaId(turmaId);
    }

    /**
     * Busca aulas por dia da semana e que ocorrem em um determinado intervalo de tempo.
     * Útil para verificar choques de horário ou visualizar horários de um período.
     * Para isso, precisamos adicionar um método no AulaRepository.
     *
     * @param diaSemana     O dia da semana.
     * @param horarioInicio O horário de início do intervalo.
     * @param horarioFim    O horário de fim do intervalo.
     * @return Uma lista de aulas encaixadas nos critérios.
     */
    public List<Aula> buscarAulasPorDiaEHorario(DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        return aulaRepository.findByDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
                diaSemana, horarioFim, horarioInicio);
        // O método findByDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual
        // verifica se a aula começa antes ou no horário final do intervalo E termina depois ou no horário inicial do intervalo.
        // Isso captura aulas que se sobrepõem ao intervalo.
    }

    /**
     * Busca aulas por professor, dia da semana, que ocorrem num determinado intervalo de tempo.
     * Muito útil para o algoritmo de geração de horários e verificação de choques.
     *
     * @param professorId   O ID do professor.
     * @param diaSemana     O dia da semana.
     * @param horarioInicio O horário de início da aula a ser verificada.
     * @param horarioFim    O horário de fim da aula a ser verificada.
     * @return Uma lista de aulas existentes para o professor naquele dia e horário.
     */
    public List<Aula> buscarAulasExistentesDoProfessor(Long professorId, DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        return aulaRepository.findByProfessorIdAndDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
                professorId, diaSemana, horarioFim, horarioInicio);
    }

    /**
     * Busca aulas por turma, dia da semana, que ocorrem num determinado intervalo de tempo.
     * Muito útil para o algoritmo de geração de horários e verificação de choques.
     *
     * @param turmaId       O 'ID' da turma.
     * @param diaSemana     O dia da semana.
     * @param horarioInicio O horário de início da aula a ser verificada.
     * @param horarioFim    O horário de fim da aula a ser verificada.
     * @return Uma lista de aulas existentes para a turma naquele dia e horário.
     */
    public List<Aula> buscarAulasExistentesDaTurma(Long turmaId, DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        return aulaRepository.findByTurmaIdAndDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
                turmaId, diaSemana, horarioFim, horarioInicio);
    }

    // Métodos para o algoritmo de geração de horários serão adicionados aqui futuramente.
    // Por exemplo:
    // public List<Aula> gerarHorarios() { ... }
    // public boolean verificarChoque(Aula novaAula) { ... }
}
