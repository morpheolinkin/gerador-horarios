package com.jefferson.geradorhorarios.service;

import com.jefferson.geradorhorarios.exception.ResourceNotFoundException;
import com.jefferson.geradorhorarios.model.Turma;
import com.jefferson.geradorhorarios.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {
    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    /**
     * Salva uma nova turma ou atualiza uma turma existente.
     *
     * @param turma O objeto Turma a ser salvo.
     * @return A turma salva/atualizada.
     */
    public Turma salvarTurma(Turma turma) {
        return turmaRepository.save(turma);
    }

    /**
     * Busca uma turma pelo ID. Lança ResourceNotFoundException se não encontrado.
     *
     * @param id O ID da turma.
     * @return A turma encontrada.
     */

    public Turma buscarTurmaPorId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + id));
    }

    /**
     * Lista todas as turmas.
     *
     * @return Uma lista de todas as turmas.
     */
    public List<Turma> listarTodasTurmas() {
        return turmaRepository.findAll();
    }

    /**
     * Deleta uma turma pelo ID. Lança ResourceNotFoundException se a turma não existir.
     *
     * @param id O ID da turma a ser deletada.
     */
    public void deletarTurma(Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Turma não encontrada com ID: " + id);
        }
        turmaRepository.deleteById(id);
    }

    /**
     * Atualiza uma turma existente.
     *
     * @param turma O objeto Turma a ser atualizado.
     * @return A turma atualizada.
     */
    public Turma update(Turma turma) {
        if (!turmaRepository.existsById(turma.getId())) {
            throw new ResourceNotFoundException("Turma não encontrada com ID: " + turma.getId());
        }
        return turmaRepository.save(turma);
    }
}
