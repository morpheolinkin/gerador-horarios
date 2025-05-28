package com.jefferson.geradorhorarios.service;

import com.jefferson.geradorhorarios.exception.ResourceNotFoundException;
import com.jefferson.geradorhorarios.model.DisponibilidadeProfessor;
import com.jefferson.geradorhorarios.repository.DisponibilidadeProfessorRepository;
import org.springframework.stereotype.Service;

@Service
public class DisponibilidadeProfessorService {
    private final DisponibilidadeProfessorRepository disponibilidadeProfessorRepository;

    public DisponibilidadeProfessorService(DisponibilidadeProfessorRepository disponibilidadeProfessorRepository) {
        this.disponibilidadeProfessorRepository = disponibilidadeProfessorRepository;
    }

    /**
     * Salva uma nova disponibilidade de professor ou atualiza uma existente.
     *
     * @param disponibilidade O objeto DisponibilidadeProfessor a ser salvo.
     * @return A disponibilidade salva/atualizada.
     */
    public DisponibilidadeProfessor salvarDisponibilidade(DisponibilidadeProfessor disponibilidade) {
        // Aqui, você pode adicionar validações de negócio, se necessário
        return disponibilidadeProfessorRepository.save(disponibilidade);
    }

    /**
     * Busca uma disponibilidade de professor pelo ID. Lança ResourceNotFoundException se não encontrado.
     *
     * @param id O ID da disponibilidade.
     * @return A disponibilidade encontrada.
     */
    public DisponibilidadeProfessor buscarDisponibilidadePorId(Long id) {
        return disponibilidadeProfessorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disponibilidade não encontrada com ID: " + id));
    }

    /**
     * Lista todas as disponibilidades de professores.
     *
     * @return Uma lista de todas as disponibilidades.
     */
    public Iterable<DisponibilidadeProfessor> listarTodasDisponibilidades() {
        return disponibilidadeProfessorRepository.findAll();
    }

    /**
     * Deleta uma disponibilidade de professor pelo ID. Lança ResourceNotFoundException se a disponibilidade não existir.
     *
     * @param id O ID da disponibilidade a ser deletada.
     */
    public void deletarDisponibilidade(Long id) {
        if (!disponibilidadeProfessorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Disponibilidade não encontrada com ID: " + id);
        }
        disponibilidadeProfessorRepository.deleteById(id);
    }

    /**
     * Atualiza uma disponibilidade de professor existente.
     *
     * @param disponibilidade O objeto DisponibilidadeProfessor a ser atualizado.
     * @return A disponibilidade atualizada.
     */
    public DisponibilidadeProfessor atualizarDisponibilidade(DisponibilidadeProfessor disponibilidade) {
        if (!disponibilidadeProfessorRepository.existsById(disponibilidade.getId())) {
            throw new ResourceNotFoundException("Disponibilidade não encontrada com ID: " + disponibilidade.getId());
        }
        return disponibilidadeProfessorRepository.save(disponibilidade);
    }

}
