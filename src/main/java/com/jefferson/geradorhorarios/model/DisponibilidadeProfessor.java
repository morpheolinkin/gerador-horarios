package com.jefferson.geradorhorarios.model;

import com.jefferson.geradorhorarios.model.enums.TipoDisponibilidade;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.DayOfWeek; // Enum do Java para dias da semana
import java.time.LocalTime; // Para representar horários

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadeProfessor {

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
