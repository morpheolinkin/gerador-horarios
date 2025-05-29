package com.jefferson.geradorhorarios.controller;

import com.jefferson.geradorhorarios.dto.TurmaDTO;
import com.jefferson.geradorhorarios.model.Turma;
import com.jefferson.geradorhorarios.service.TurmaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/turmas")
public class TurmaController {
    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    /**
     * Endpoint para criar uma nova turma.
     * Método HTTP: POST
     * URL: /api/turmas
     * Corpo da requisição: JSON do objeto Turma (sem o ID).
     * Exemplo: {"nome": "3º Ano C"}
     *
     * @param turma O objeto Turma a ser criado.
     * @return ResponseEntity com a turma criada e status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<TurmaDTO> criarTurma(@RequestBody Turma turma) {
        Turma novaTurma = turmaService.salvarTurma(turma);
        return new ResponseEntity<>(TurmaDTO.fromEntity(novaTurma), HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todas as turmas.
     * Método HTTP: GET
     * URL: /api/turmas
     *
     * @return ResponseEntity com uma lista de turmas e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<TurmaDTO>> listarTodasTurmas() {
        List<Turma> turmas = turmaService.listarTodasTurmas();
        List<TurmaDTO> turmasDTO = turmas.stream()
                .map(TurmaDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(turmasDTO, HttpStatus.OK);
    }

    /**
     * Endpoint para buscar uma turma pelo ID.
     * Método HTTP: GET
     * URL: /api/turmas/{id}
     * Exemplo: /api/turmas/1
     *
     * @param id O ID da turma.
     * @return ResponseEntity com a turma encontrada e status HTTP 200 (OK),
     * ou status HTTP 404 (Not Found) se não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TurmaDTO> buscarTurmaPorId(@PathVariable Long id) {
        Turma turma = turmaService.buscarTurmaPorId(id);
        return new ResponseEntity<>(TurmaDTO.fromEntity(turma), HttpStatus.OK);
    }

    /**
     * Endpoint para atualizar uma turma existente.
     * Método HTTP: PUT
     * URL: /api/turmas/{id}
     * Corpo da requisição: JSON do objeto Turma com os dados atualizados.
     * Exemplo: /api/turmas/1 com {"id": 1, "nome": "1º Ano A - Atualizado"}
     *
     * @param id    O ID da turma a ser atualizada.
     * @param turma Os dados atualizados da turma.
     * @return ResponseEntity com a turma atualizada e status HTTP 200 (OK).
     * Ou status HTTP 400 (Bad Request) se os IDs não corresponderem, ou 404 (Not Found) se a turma não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TurmaDTO> atualizarTurma(@PathVariable Long id, @RequestBody Turma turma) {
        if (!id.equals(turma.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Turma turmaAtualizada = turmaService.salvarTurma(turma);
        return new ResponseEntity<>(TurmaDTO.fromEntity(turmaAtualizada), HttpStatus.OK);
    }

    /**
     * Endpoint para deletar uma turma.
     * Método HTTP: DELETE
     * URL: /api/turmas/{id}
     * Exemplo: /api/turmas/1
     *
     * @param id O ID da turma a ser deletada.
     * @return ResponseEntity com status HTTP 204 (No Content) se deletada com sucesso,
     * ou status HTTP 404 (Not Found) se não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTurma(@PathVariable Long id) {
        turmaService.deletarTurma(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
