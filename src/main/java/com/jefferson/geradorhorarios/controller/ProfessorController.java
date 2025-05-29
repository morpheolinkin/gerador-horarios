package com.jefferson.geradorhorarios.controller;

import com.jefferson.geradorhorarios.dto.ProfessorDTO;
import com.jefferson.geradorhorarios.model.Professor;
import com.jefferson.geradorhorarios.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> criarProfessor(@RequestBody Professor professor) {
        Professor novoProfessor = professorService.salvarProfessor(professor);
        return new ResponseEntity<>(ProfessorDTO.fromEntity(novoProfessor), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> listarTodosProfessores() {
        List<Professor> professores = professorService.listarTodosProfessores();
        // Mapeia a lista de entidades para uma lista de DTOs
        List<ProfessorDTO> professoresDTO = professores.stream()
                .map(ProfessorDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(professoresDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarProfessorPorId(@PathVariable Long id) {
        Professor professor = professorService.buscarProfessorPorId(id);
        return new ResponseEntity<>(ProfessorDTO.fromEntity(professor), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> atualizarProfessor(@PathVariable Long id, @RequestBody Professor professor) {
        if (!id.equals(professor.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Professor professorAtualizado = professorService.salvarProfessor(professor);
        return new ResponseEntity<>(ProfessorDTO.fromEntity(professorAtualizado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable Long id) {
        professorService.deletarProfessor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
