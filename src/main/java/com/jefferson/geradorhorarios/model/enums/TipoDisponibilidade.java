package com.jefferson.geradorhorarios.model.enums;

public enum TipoDisponibilidade {
    FOLGA, // O professor não está disponível de forma alguma
    AC, // Atividade Complementar (pode ser usado para bloquear horário para AC)
    PREFERENCIA, // Horário que o professor prefere dar aula
    INDISPONIBILIDADE // Horário em que o professor está indisponível por outro motivo
}
