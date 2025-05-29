package com.jefferson.geradorhorarios.controller;

import com.jefferson.geradorhorarios.dto.AulaDTO;
import com.jefferson.geradorhorarios.model.Aula;
import com.jefferson.geradorhorarios.model.Disciplina;
import com.jefferson.geradorhorarios.model.Professor;
import com.jefferson.geradorhorarios.model.Turma;
import com.jefferson.geradorhorarios.service.AulaService;
import com.jefferson.geradorhorarios.service.DisciplinaService;
import com.jefferson.geradorhorarios.service.ProfessorService;
import com.jefferson.geradorhorarios.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    private final AulaService aulaService;
    private final DisciplinaService disciplinaService;
    private final ProfessorService professorService;
    private final TurmaService turmaService;

    @Autowired
    public AulaController(AulaService aulaService,
                          DisciplinaService disciplinaService,
                          ProfessorService professorService,
                          TurmaService turmaService) {
        this.aulaService = aulaService;
        this.disciplinaService = disciplinaService;
        this.professorService = professorService;
        this.turmaService = turmaService;
    }

    /**
     * Endpoint para criar uma nova aula.
     * Recebe um AulaDTO para simplificar a entrada, mas cria a entidade Aula.
     * Método HTTP: POST
     * URL: /api/aulas
     * Corpo da requisição: JSON do AulaDTO (com IDs das entidades relacionadas).
     * Exemplo:
     * {
     * "disciplina": {"id": 1},
     * "professor": {"id": 1},
     * "turma": {"id": 1},
     * "diaSemana": "MONDAY",
     * "horarioInicio": "08:00:00",
     * "horarioFim": "09:00:00"
     * }
     *
     * @param aulaDTO O objeto AulaDTO a ser criado.
     * @return ResponseEntity com o aulaDTO criado e status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<AulaDTO> criarAula(@RequestBody AulaDTO aulaDTO) {
        // Busque as entidades completas a partir dos IDs do DTO
        // É importante que os DTOs de entrada tenham apenas o ID se for para buscar a entidade
        Disciplina disciplina = disciplinaService.buscarDisciplinaPorId(aulaDTO.disciplina().id());
        Professor professor = professorService.buscarProfessorPorId(aulaDTO.professor().id());
        Turma turma = turmaService.buscarTurmaPorId(aulaDTO.turma().id());

        // Crie a entidade Aula
        Aula aula = new Aula(
                null, disciplina, professor, turma,
                aulaDTO.diaSemana(), aulaDTO.horarioInicio(), aulaDTO.horarioFim()
        );

        Aula novaAula = aulaService.salvarAula(aula);
        return new ResponseEntity<>(AulaDTO.fromEntity(novaAula), HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todas as aulas.
     * Retorna uma lista de AulaDTOs.
     * Método HTTP: GET
     * URL: /api/aulas
     *
     * @return ResponseEntity com uma lista de AulaDTOs e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<AulaDTO>> listarTodasAulas() {
        // Use o método do serviço que carrega os detalhes (JOIN FETCH)
        List<Aula> aulas = aulaService.listarTodasAulas();
        List<AulaDTO> aulasDTO = aulas.stream()
                .map(AulaDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(aulasDTO, HttpStatus.OK);
    }

    /**
     * Endpoint para buscar uma aula pelo ID.
     *
     * @param id O ID da aula.
     * @return ResponseEntity com o AulaDTO encontrada e status HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<AulaDTO> buscarAulaPorId(@PathVariable Long id) {
        Aula aula = aulaService.buscarAulaPorId(id);
        return new ResponseEntity<>(AulaDTO.fromEntity(aula), HttpStatus.OK);
    }

    /**
     * Endpoint para atualizar uma aula existente.
     *
     * @param id      O ID da aula a ser atualizada.
     * @param aulaDTO Os dados atualizados da aula em formato DTO.
     * @return ResponseEntity com o AulaDTO atualizada e status HTTP 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<AulaDTO> atualizarAula(@PathVariable Long id, @RequestBody AulaDTO aulaDTO) {
        // Para atualizar, primeiro busca a aula existente
        Aula aulaExistente = aulaService.buscarAulaPorId(id); // Lança 404 se não encontrar

        // Atualize os campos da aula existente com base no DTO
        aulaExistente.setDisciplina(disciplinaService.buscarDisciplinaPorId(aulaDTO.disciplina().id()));
        aulaExistente.setProfessor(professorService.buscarProfessorPorId(aulaDTO.professor().id()));
        aulaExistente.setTurma(turmaService.buscarTurmaPorId(aulaDTO.turma().id()));
        aulaExistente.setDiaSemana(aulaDTO.diaSemana());
        aulaExistente.setHorarioInicio(aulaDTO.horarioInicio());
        aulaExistente.setHorarioFim(aulaDTO.horarioFim());

        Aula aulaAtualizada = aulaService.salvarAula(aulaExistente);
        return new ResponseEntity<>(AulaDTO.fromEntity(aulaAtualizada), HttpStatus.OK);
    }


    /**
     * Endpoint para deletar uma aula.
     *
     * @param id O ID da aula a ser deletada.
     * @return ResponseEntity com status HTTP 204 (No Content) se deletada com sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAula(@PathVariable Long id) {
        aulaService.deletarAula(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint para listar aulas por professor.
     *
     * @param professorId O ID do professor.
     * @return ResponseEntity com uma lista de AulaDTOs do professor.
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<AulaDTO>> buscarAulasPorProfessor(@PathVariable Long professorId) {
        List<Aula> aulas = aulaService.buscarAulasPorProfessor(professorId);
        List<AulaDTO> aulasDTO = aulas.stream()
                .map(AulaDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(aulasDTO, HttpStatus.OK);
    }

    /**
     * Endpoint para listar aulas por turma.
     *
     * @param turmaId O ID da turma.
     * @return ResponseEntity com uma lista de AulaDTOs da turma.
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<AulaDTO>> buscarAulasPorTurma(@PathVariable Long turmaId) {
        List<Aula> aulas = aulaService.buscarAulasPorTurma(turmaId);
        List<AulaDTO> aulasDTO = aulas.stream()
                .map(AulaDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(aulasDTO, HttpStatus.OK);
    }

    /**
     * Endpoint para buscar aulas por dia da semana e que se sobrepõem a um intervalo de tempo.
     *
     * @param diaSemana     O dia da semana (ex: MONDAY, TUESDAY).
     * @param horarioInicio O horário de início do intervalo (HH:mm:ss).
     * @param horarioFim    O horário de fim do intervalo (HH:mm:ss).
     * @return ResponseEntity com uma lista de AulaDTOs.
     */
    @GetMapping("/search")
    public ResponseEntity<List<AulaDTO>> buscarAulasPorDiaEHorario(
            @RequestParam DayOfWeek diaSemana,
            @RequestParam LocalTime horarioInicio,
            @RequestParam LocalTime horarioFim) {
        List<Aula> aulas = aulaService.buscarAulasPorDiaEHorario(diaSemana, horarioInicio, horarioFim);
        List<AulaDTO> aulasDTO = aulas.stream()
                .map(AulaDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(aulasDTO, HttpStatus.OK);
    }

    /**
     * Endpoint para gerar um novo conjunto de horários de aula.
     * Este método limpa os horários existentes e tenta criar novos com base nas regras.
     * Método HTTP: POST
     * URL: /api/aulas/gerar
     *
     * @return ResponseEntity com a lista de AulaDTOs geradas e status HTTP 200 (OK).
     */
    @PostMapping("/gerar")
    public ResponseEntity<List<AulaDTO>> gerarHorarios() {
        List<Aula> aulasGeradas = aulaService.gerarHorarios();
        List<AulaDTO> aulasDTO = aulasGeradas.stream()
                .map(AulaDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(aulasDTO, HttpStatus.OK);
    }
}
