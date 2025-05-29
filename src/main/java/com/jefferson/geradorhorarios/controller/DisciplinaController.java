package com.jefferson.geradorhorarios.controller;

import com.jefferson.geradorhorarios.dto.DisciplinaDTO;
import com.jefferson.geradorhorarios.model.Disciplina;
import com.jefferson.geradorhorarios.service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    @Autowired
    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    public ResponseEntity<DisciplinaDTO> criarDisciplina(@RequestBody Disciplina disciplina) {
        Disciplina novaDisciplina = disciplinaService.salvarDisciplina(disciplina);
        return new ResponseEntity<>(DisciplinaDTO.fromEntity(novaDisciplina), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaDTO>> listarTodasDisciplinas() {
        List<Disciplina> disciplinas = disciplinaService.listarTodasDisciplinas();
        List<DisciplinaDTO> disciplinasDTO = disciplinas.stream()
                .map(DisciplinaDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(disciplinasDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> buscarDisciplinaPorId(@PathVariable Long id) {
        Disciplina disciplina = disciplinaService.buscarDisciplinaPorId(id);
        return new ResponseEntity<>(DisciplinaDTO.fromEntity(disciplina), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> atualizarDisciplina(@PathVariable Long id, @RequestBody Disciplina disciplina) {
        if (!id.equals(disciplina.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Disciplina disciplinaAtualizada = disciplinaService.salvarDisciplina(disciplina);
        return new ResponseEntity<>(DisciplinaDTO.fromEntity(disciplinaAtualizada), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDisciplina(@PathVariable Long id) {
        disciplinaService.deletarDisciplina(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
