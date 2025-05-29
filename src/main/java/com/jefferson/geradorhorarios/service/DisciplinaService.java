package com.jefferson.geradorhorarios.service;

import com.jefferson.geradorhorarios.exception.ResourceNotFoundException;
import com.jefferson.geradorhorarios.model.Disciplina;
import com.jefferson.geradorhorarios.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {
    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * Salva uma nova disciplina ou atualiza uma disciplina existente.
     *
     * @param disciplina O objeto Disciplina a ser salvo.
     * @return A disciplina salva/atualizada.
     */
    public Disciplina salvarDisciplina(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    /**
     * Busca uma disciplina pelo ID. Lança ResourceNotFoundException se não encontrado.
     *
     * @param id O ID da disciplina.
     * @return A disciplina encontrada.
     */
    public Disciplina buscarDisciplinaPorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com ID: " + id));
    }

    /**
     * Lista todas as disciplinas.
     *
     * @return Uma lista de todas as disciplinas.
     */
    public List<Disciplina> listarTodasDisciplinas() {
        return disciplinaRepository.findAll();
    }

    /**
     * Deleta uma disciplina pelo ID. Lança ResourceNotFoundException se a disciplina não existir.
     *
     * @param id O ID da disciplina a ser deletada.
     */
    public void deletarDisciplina(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Disciplina não encontrada com ID: " + id);
        }
        disciplinaRepository.deleteById(id);
    }

    /**
     * Atualiza uma disciplina existente.
     *
     * @param id         O ID da disciplina a ser atualizada.
     * @param disciplina O objeto Disciplina com os novos dados.
     * @return A disciplina atualizada.
     */
    public Disciplina atualizarDisciplina(Long id, Disciplina disciplina) {
        if (!disciplinaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Disciplina não encontrada com ID: " + id);
        }
        disciplina.setId(id); // Certifica-se de que o ID está definido para a atualização
        return disciplinaRepository.save(disciplina);
    }
}
