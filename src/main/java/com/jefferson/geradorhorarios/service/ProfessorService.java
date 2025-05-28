package com.jefferson.geradorhorarios.service;

import com.jefferson.geradorhorarios.exception.ResourceNotFoundException;
import com.jefferson.geradorhorarios.model.Professor;
import com.jefferson.geradorhorarios.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    /**
     * Salva um novo professor ou atualiza um professor existente.
     * Pode incluir validações de negócio, por exemplo, se o email já existe.
     *
     * @param professor O objeto Professor a ser salvo.
     * @return O professor salvo/atualizado.
     */
    public Professor salvarProfessor(Professor professor) {
        // Exemplo de validação de regra de negócio: verificar se o email já está em uso
        // if (professorRepository.findByEmail(professor.getEmail()).isPresent() && professor.getId() == null) {
        //    throw new BusinessRuleException("Email já cadastrado para outro professor.");
        // }
        // Para isso, você precisaria adicionar `Optional<Professor> findByEmail(String email);` na ProfessorRepository

        return professorRepository.save(professor);
    }

    /**
     * Busca um professor pelo ID. Lança ResourceNotFoundException se não encontrado.
     *
     * @param id O ID do professor.
     * @return O professor encontrado.
     * @throws ResourceNotFoundException Se o professor não for encontrado.
     */
    public Professor buscarProfessorPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + id));
    }

    /**
     * Lista todos os professores.
     *
     * @return Uma lista de todos os professores.
     */
    public List<Professor> listarTodosProfessores() {
        return professorRepository.findAll();
    }

    /**
     * Deleta um professor pelo ID. Lança ResourceNotFoundException se o professor não existir.
     *
     * @param id O ID do professor a ser deletado.
     * @throws ResourceNotFoundException Se o professor não for encontrado.
     */
    public void deletarProfessor(Long id) {
        if (!professorRepository.existsById(id)) { // Verifica se o professor existe antes de tentar deletar
            throw new ResourceNotFoundException("Professor não encontrado com ID: " + id);
        }
        professorRepository.deleteById(id);
    }
}
