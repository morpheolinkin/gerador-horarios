package com.jefferson.geradorhorarios.model;

import com.jefferson.geradorhorarios.model.enums.TipoDisponibilidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadeProfessor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Many-to-One com Professor
    @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY para carregar o professor apenas quando necessário
    @JoinColumn(name = "professor_id", nullable = false) // Coluna da chave estrangeira no DB
    private Professor professor;

    @Enumerated(EnumType.STRING) // Armazena o enum como String no DB (ex: "MONDAY", "TUESDAY")
    @Column(nullable = false)
    private DayOfWeek diaSemana; // Enum DayOfWeek (SEGUNDA, TERCA, etc.)

    @Column(nullable = false)
    private LocalTime horarioInicio; // Horário de início da disponibilidade/restrição

    @Column(nullable = false)
    private LocalTime horarioFim; // Horário de fim da disponibilidade/restrição

    @Enumerated(EnumType.STRING) // Armazena o enum como String
    @Column(nullable = false)
    private TipoDisponibilidade tipo; // Enum para o tipo de disponibilidade (FOLGA, AC, PREFERENCIA, INDISPONIBILIDADE)

    private String observacao; // Campo opcional para detalhes adicionais
}
