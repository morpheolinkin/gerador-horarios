package com.jefferson.geradorhorarios.repository;

import com.jefferson.geradorhorarios.model.DisponibilidadeProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadeProfessorRepository extends JpaRepository<DisponibilidadeProfessor, Long> {
}
