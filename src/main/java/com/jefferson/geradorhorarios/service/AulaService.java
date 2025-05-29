package com.jefferson.geradorhorarios.service;

import com.jefferson.geradorhorarios.exception.ResourceNotFoundException;
import com.jefferson.geradorhorarios.model.*;
import com.jefferson.geradorhorarios.model.enums.TipoDisponibilidade;
import com.jefferson.geradorhorarios.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AulaService {

    // Constantes para os horários e duração das aulas (podem ser configuráveis)
    private static final LocalTime HORA_INICIO_DIA = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIM_DIA = LocalTime.of(18, 0);
    private static final int DURACAO_AULA_MINUTOS = 60; // Aulas de 1 hora
    private static final List<DayOfWeek> DIAS_SEMANA_UTEIS = Arrays.asList(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    );
    private final AulaRepository aulaRepository;
    private final ProfessorService professorService;
    private final DisciplinaService disciplinaService;
    private final TurmaService turmaService;

    @Autowired
    public AulaService(AulaRepository aulaRepository,
                       ProfessorService professorService,
                       DisciplinaService disciplinaService,
                       TurmaService turmaService) {
        this.aulaRepository = aulaRepository;
        this.professorService = professorService;
        this.disciplinaService = disciplinaService;
        this.turmaService = turmaService;
    }

    /**
     * Salva uma nova aula ou atualiza uma aula existente.
     *
     * @param aula O objeto Aula a ser salvo.
     * @return A aula salva/atualizada.
     */
    @Transactional
    public Aula salvarAula(Aula aula) {
        // Futuramente, aqui poderíamos adicionar validações de negócio
        // antes de salvar a aula, como verificar choques de horário,
        // disponibilidade do professor, etc.
        return aulaRepository.save(aula);
    }

    /**
     * Busca uma aula pelo ID. Lança ResourceNotFoundException se não encontrada.
     *
     * @param id O ID da aula.
     * @return A aula encontrada.
     * @throws ResourceNotFoundException Se a aula não for encontrada.
     */
    @Transactional(readOnly = true)
    public Aula buscarAulaPorId(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com ID: " + id));
    }

    /**
     * Lista todas as aulas com detalhes de disciplina, professor e turma.
     *
     * @return Uma lista de todas as aulas.
     */
    @Transactional(readOnly = true)
    public List<Aula> listarTodasAulas() {
        // Garante que os relacionamentos LAZY sejam carregados para o DTO
        return aulaRepository.findAllWithDetails();
    }

    /**
     * Deleta uma aula pelo ID. Lança ResourceNotFoundException se a aula não existir.
     *
     * @param id O ID da aula a ser deletada.
     * @throws ResourceNotFoundException Se a aula não for encontrada.
     */
    @Transactional
    public void deletarAula(Long id) {
        if (!aulaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aula não encontrada com ID: " + id);
        }
        aulaRepository.deleteById(id);
    }

    /**
     * Busca todas as aulas de um professor específico.
     *
     * @param professorId O ID do professor.
     * @return Uma lista de aulas do professor.
     */
    @Transactional(readOnly = true)
    public List<Aula> buscarAulasPorProfessor(Long professorId) {
        // Considerar carregar a disciplina e a turma também, se forem necessárias no DTO
        return aulaRepository.findByProfessorId(professorId);
    }

    /**
     * Busca todas as aulas de uma turma específica.
     *
     * @param turmaId O ID da turma.
     * @return Uma lista de aulas da turma.
     */
    @Transactional(readOnly = true)
    public List<Aula> buscarAulasPorTurma(Long turmaId) {
        // Considerar carregar a disciplina e o professor também, se forem necessárias no DTO
        return aulaRepository.findByTurmaId(turmaId);
    }

    /**
     * Busca aulas por dia da semana e que ocorrem em um determinado intervalo de tempo.
     *
     * @param diaSemana     O dia da semana.
     * @param horarioInicio O horário de início do intervalo.
     * @param horarioFim    O horário de fim do intervalo.
     * @return Uma lista de aulas que se encaixam nos critérios.
     */
    @Transactional(readOnly = true)
    public List<Aula> buscarAulasPorDiaEHorario(DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        return aulaRepository.findByDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
                diaSemana, horarioFim, horarioInicio);
    }

    /**
     * Busca aulas por professor, dia da semana e que ocorrem em um determinado intervalo de tempo.
     *
     * @param professorId   O ID do professor.
     * @param diaSemana     O dia da semana.
     * @param horarioInicio O horário de início da aula a ser verificada.
     * @param horarioFim    O horário de fim da aula a ser verificada.
     * @return Uma lista de aulas existentes para o professor naquele dia e horário.
     */
    @Transactional(readOnly = true)
    public List<Aula> buscarAulasExistentesDoProfessor(Long professorId, DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        return aulaRepository.findByProfessorIdAndDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
                professorId, diaSemana, horarioFim, horarioInicio);
    }

    /**
     * Busca aulas por turma, dia da semana e que ocorrem em um determinado intervalo de tempo.
     *
     * @param turmaId       O ID da turma.
     * @param diaSemana     O dia da semana.
     * @param horarioInicio O horário de início da aula a ser verificada.
     * @param horarioFim    O horário de fim da aula a ser verificada.
     * @return Uma lista de aulas existentes para a turma naquele dia e horário.
     */
    @Transactional(readOnly = true)
    public List<Aula> buscarAulasExistentesDaTurma(Long turmaId, DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim) {
        return aulaRepository.findByTurmaIdAndDiaSemanaAndHorarioInicioLessThanEqualAndHorarioFimGreaterThanEqual(
                turmaId, diaSemana, horarioFim, horarioInicio);
    }

    /**
     * Gera um novo conjunto de horários de aula, limpando os existentes e alocando novamente.
     * Implementa uma heurística de agendamento.
     *
     * @return Uma lista das aulas geradas.
     */
    @Transactional
    public List<Aula> gerarHorarios() {
        // Limpa todas as aulas existentes para gerar um novo horário do zero
        aulaRepository.deleteAll();
        List<Aula> horariosGerados = new ArrayList<>();

        List<Professor> professores = professorService.listarTodosProfessores();
        List<Disciplina> disciplinas = disciplinaService.listarTodasDisciplinas();
        List<Turma> turmas = turmaService.listarTodasTurmas();

        // Mapeia disciplinas para seus professores aptos
        Map<Disciplina, List<Professor>> professoresPorDisciplina = new HashMap<>();
        for (Disciplina disc : disciplinas) {
            // Garante que a coleção de professores esteja inicializada dentro da transação
            // Ao chamar .stream(), a coleção LAZY será carregada.
            List<Professor> aptos = disc.getProfessores().stream().collect(Collectors.toList());
            professoresPorDisciplina.put(disc, aptos);
        }

        // Representação dos horários ocupados para cada professor e turma
        // Map<Entidade, Map<Dia da Semana, List<LocalTime>>>
        // Armazena o horário de início de cada slot ocupado
        Map<Professor, Map<DayOfWeek, List<LocalTime>>> professorHorariosOcupados = new HashMap<>();
        Map<Turma, Map<DayOfWeek, List<LocalTime>>> turmaHorariosOcupados = new HashMap<>();

        // Inicializa os mapas de ocupação e adiciona folgas e ACs dos professores
        for (Professor p : professores) {
            professorHorariosOcupados.put(p, new HashMap<>());
            for (DisponibilidadeProfessor disp : p.getDisponibilidades()) {
                if (disp.getTipo() == TipoDisponibilidade.FOLGA || disp.getTipo() == TipoDisponibilidade.AC || disp.getTipo() == TipoDisponibilidade.INDISPONIBILIDADE) {
                    // Marca todos os slots dentro do intervalo de indisponibilidade
                    LocalTime currentSlot = disp.getHorarioInicio();
                    while (currentSlot.isBefore(disp.getHorarioFim())) {
                        professorHorariosOcupados.get(p).computeIfAbsent(disp.getDiaSemana(), k -> new ArrayList<>())
                                .add(currentSlot);
                        currentSlot = currentSlot.plusMinutes(DURACAO_AULA_MINUTOS);
                    }
                }
            }
        }
        for (Turma t : turmas) {
            turmaHorariosOcupados.put(t, new HashMap<>());
        }

        // Para cada turma, vamos tentar agendar as disciplinas
        for (Turma turma : turmas) {
            for (Disciplina disciplina : disciplinas) {
                // Filtra apenas os professores que lecionam esta disciplina
                List<Professor> professoresAptos = professoresPorDisciplina.get(disciplina);
                if (professoresAptos == null || professoresAptos.isEmpty()) {
                    System.out.println("Nenhum professor apto encontrado para a disciplina: " + disciplina.getNome());
                    continue;
                }

                // Agendar aulas para esta disciplina nesta turma, de acordo com a carga horária semanal
                for (int i = 0; i < disciplina.getCargaHorariaSemanal(); i++) {
                    boolean aulaAgendada = false;
                    // Tenta encontrar um professor disponível e um horário disponível
                    for (DayOfWeek dia : DIAS_SEMANA_UTEIS) {
                        for (LocalTime horario = HORA_INICIO_DIA; horario.isBefore(HORA_FIM_DIA); horario = horario.plusMinutes(DURACAO_AULA_MINUTOS)) {
                            LocalTime horarioFimAula = horario.plusMinutes(DURACAO_AULA_MINUTOS);

                            // Verifica se o slot já está ocupado pela turma
                            if (!isTurmaLivre(turma, dia, horario, horarioFimAula, turmaHorariosOcupados)) {
                                continue;
                            }

                            // Tenta com cada professor apto
                            for (Professor professor : professoresAptos) {
                                // Verifica se o slot já está ocupado pelo professor (aula ou folga/AC)
                                if (!isProfessorLivre(professor, dia, horario, horarioFimAula, professorHorariosOcupados)) {
                                    continue;
                                }

                                // Se chegou aqui, o slot está livre para professor e turma
                                Aula novaAula = new Aula(null, disciplina, professor, turma, dia, horario, horarioFimAula);

                                // Adiciona a aula gerada
                                horariosGerados.add(novaAula);
                                aulaRepository.save(novaAula); // Persiste a aula no banco

                                // Marca o professor e a turma como ocupados
                                professorHorariosOcupados.get(professor).computeIfAbsent(dia, k -> new ArrayList<>()).add(horario);
                                turmaHorariosOcupados.get(turma).computeIfAbsent(dia, k -> new ArrayList<>()).add(horario);

                                aulaAgendada = true;
                                break; // Aula agendada, sai do loop de professores
                            }
                            if (aulaAgendada) break; // Sai do loop de horários
                        }
                        if (aulaAgendada) break; // Sai do loop de dias
                    }

                    if (!aulaAgendada) {
                        System.out.println("WARN: Não foi possível agendar todas as aulas para " + disciplina.getNome() + " na Turma " + turma.getNome() + ". Faltam " + (disciplina.getCargaHorariaSemanal() - i) + " aulas.");
                        // Aqui você poderia implementar um mecanismo de backtracking mais robusto
                        // ou lançar uma exceção se a geração de horários for estritamente necessária.
                    }
                }
            }
        }
        return horariosGerados;
    }

    /**
     * Verifica se um professor está livre em um determinado slot de tempo.
     * Considera folgas/indisponibilidades e outras aulas agendadas.
     *
     * @param professor                 O professor a verificar.
     * @param dia                       O dia da semana.
     * @param inicio                    O horário de início do slot.
     * @param fim                       O horário de fim do slot.
     * @param professorHorariosOcupados Mapa de horários ocupados dos professores.
     * @return true se o professor estiver livre, false caso contrário.
     */
    private boolean isProfessorLivre(Professor professor, DayOfWeek dia, LocalTime inicio, LocalTime fim,
                                     Map<Professor, Map<DayOfWeek, List<LocalTime>>> professorHorariosOcupados) {
        // Verifica se o slot já está ocupado por uma aula ou indisponibilidade
        List<LocalTime> horariosOcupadosNoDia = professorHorariosOcupados.get(professor).getOrDefault(dia, new ArrayList<>());
        for (LocalTime horarioOcupado : horariosOcupadosNoDia) {
            LocalTime fimHorarioOcupado = horarioOcupado.plusMinutes(DURACAO_AULA_MINUTOS); // Assumindo duração padrão da aula

            // Verifica sobreposição: [inicio, fim] se sobrepõe a [horarioOcupado, fimHorarioOcupado]
            // se inicio < fimHorarioOcupado E fim > horarioOcupado
            if (inicio.isBefore(fimHorarioOcupado) && fim.isAfter(horarioOcupado)) {
                return false; // Há sobreposição de horários
            }
        }
        return true;
    }

    /**
     * Verifica se uma turma está livre em um determinado slot de tempo.
     *
     * @param turma                 A turma a verificar.
     * @param dia                   O dia da semana.
     * @param inicio                O horário de início do slot.
     * @param fim                   O horário de fim do slot.
     * @param turmaHorariosOcupados Mapa de horários ocupados das turmas.
     * @return true se a turma estiver livre, false caso contrário.
     */
    private boolean isTurmaLivre(Turma turma, DayOfWeek dia, LocalTime inicio, LocalTime fim,
                                 Map<Turma, Map<DayOfWeek, List<LocalTime>>> turmaHorariosOcupados) {
        // Verifica se a turma já tem uma aula agendada para aquele horário
        List<LocalTime> horariosOcupadosNoDia = turmaHorariosOcupados.get(turma).getOrDefault(dia, new ArrayList<>());
        for (LocalTime horarioOcupado : horariosOcupadosNoDia) {
            LocalTime fimHorarioOcupado = horarioOcupado.plusMinutes(DURACAO_AULA_MINUTOS);

            // Verifica sobreposição
            if (inicio.isBefore(fimHorarioOcupado) && fim.isAfter(horarioOcupado)) {
                return false; // Há sobreposição de horários
            }
        }
        return true;
    }
}
