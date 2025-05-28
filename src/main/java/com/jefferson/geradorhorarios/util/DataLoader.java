package com.jefferson.geradorhorarios.util;

import com.jefferson.geradorhorarios.model.*;
import com.jefferson.geradorhorarios.model.enums.TipoDisponibilidade;
import com.jefferson.geradorhorarios.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;

@Component
@Profile("test")
public class DataLoader implements CommandLineRunner {

    private final ProfessorService professorService;
    private final DisciplinaService disciplinaService;
    private final TurmaService turmaService;
    private final DisponibilidadeProfessorService disponibilidadeProfessorService;
    private final AulaService aulaService;

    @Autowired
    public DataLoader(ProfessorService professorService,
                      DisciplinaService disciplinaService,
                      TurmaService turmaService,
                      DisponibilidadeProfessorService disponibilidadeProfessorService,
                      AulaService aulaService) {
        this.professorService = professorService;
        this.disciplinaService = disciplinaService;
        this.turmaService = turmaService;
        this.disponibilidadeProfessorService = disponibilidadeProfessorService;
        this.aulaService = aulaService;
    }

    @Override
    @Transactional // Adicione esta anotação
    public void run(String... args) throws Exception {
        // Este método será executado ao iniciar a aplicação, apenas no perfil "test"

        // 1. Criar e salvar Professores
        Professor ana = new Professor(null, "Prof. Ana Silva", "ana.silva@example.com", new HashSet<>(), new HashSet<>());
        Professor bruno = new Professor(null, "Prof. Bruno Costa", "bruno.costa@example.com", new HashSet<>(), new HashSet<>());
        Professor carla = new Professor(null, "Prof. Carla Dias", "carla.dias@example.com", new HashSet<>(), new HashSet<>());
        Professor daniel = new Professor(null, "Prof. Daniel Souza", "daniel.souza@example.com", new HashSet<>(), new HashSet<>());

        ana = professorService.salvarProfessor(ana);
        bruno = professorService.salvarProfessor(bruno);
        carla = professorService.salvarProfessor(carla);
        daniel = professorService.salvarProfessor(daniel);

        System.out.println("Professores salvos: " + ana.getNome() + ", " + bruno.getNome()); // Debugging

        // 2. Criar e salvar Disciplinas
        Disciplina matematica = new Disciplina(null, "Matemática", 4, new HashSet<>());
        Disciplina portugues = new Disciplina(null, "Português", 4, new HashSet<>());
        Disciplina historia = new Disciplina(null, "História", 2, new HashSet<>());
        Disciplina geografia = new Disciplina(null, "Geografia", 2, new HashSet<>());
        Disciplina ciencias = new Disciplina(null, "Ciências", 3, new HashSet<>());

        matematica = disciplinaService.salvarDisciplina(matematica);
        portugues = disciplinaService.salvarDisciplina(portugues);
        historia = disciplinaService.salvarDisciplina(historia);
        geografia = disciplinaService.salvarDisciplina(geografia);
        ciencias = disciplinaService.salvarDisciplina(ciencias);

        System.out.println("Disciplinas salvas: " + matematica.getNome() + ", " + portugues.getNome()); // Debugging

        // 3. Criar e salvar Turmas
        Turma turma1A = new Turma(null, "1º Ano A");
        Turma turma1B = new Turma(null, "1º Ano B");
        Turma turma2A = new Turma(null, "2º Ano A");

        turma1A = turmaService.salvarTurma(turma1A);
        turma1B = turmaService.salvarTurma(turma1B);
        turma2A = turmaService.salvarTurma(turma2A);

        System.out.println("Turmas salvos: " + turma1A.getNome() + ", " + turma1B.getNome()); // Debugging

        // 4. Ligar Professores a Disciplinas (Many-to-Many)
        // Agora que o DataLoader é @Transactional, os objetos ana, bruno, etc.
        // que foram retornados pelo professorService.salvarProfessor() já estão "gerenciados"
        // e suas coleções LAZY podem ser acessadas dentro desta transação.
        ana.addDisciplina(matematica);
        ana.addDisciplina(ciencias);
        professorService.salvarProfessor(ana); // Salve para persistir as mudanças no relacionamento

        bruno.addDisciplina(portugues);
        bruno.addDisciplina(historia);
        professorService.salvarProfessor(bruno);

        carla.addDisciplina(geografia);
        professorService.salvarProfessor(carla);

        daniel.addDisciplina(matematica);
        professorService.salvarProfessor(daniel);

        System.out.println("Relacionamentos Professor-Disciplina criados."); // Debugging

        // 5. Inserir Disponibilidades de Professores (One-to-Many)
        // Como ana, bruno, carla são objetos gerenciados, pode adicionar diretamente
        // às suas coleções e salvar o professor novamente.
        DisponibilidadeProfessor dispAna = new DisponibilidadeProfessor(null, ana, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(12, 0), TipoDisponibilidade.FOLGA, "Consulta médica");
        DisponibilidadeProfessor dispBruno = new DisponibilidadeProfessor(null, bruno, DayOfWeek.TUESDAY,
                LocalTime.of(14, 0), LocalTime.of(18, 0), TipoDisponibilidade.AC, "Reunião administrativa");
        DisponibilidadeProfessor dispCarla = new DisponibilidadeProfessor(null, carla, DayOfWeek.WEDNESDAY,
                LocalTime.of(8, 0), LocalTime.of(12, 0), TipoDisponibilidade.PREFERENCIA, "Horário preferencial");

        // Use o método addDisponibilidade da entidade Professor para garantir a consistência bidirecional
        ana.addDisponibilidade(dispAna);
        professorService.salvarProfessor(ana);

        bruno.addDisponibilidade(dispBruno);
        professorService.salvarProfessor(bruno);

        carla.addDisponibilidade(dispCarla);
        professorService.salvarProfessor(carla);

        System.out.println("Disponibilidades salvas."); // Debugging


        // 6. Inserir Aulas (exemplo de aulas já agendadas)
        Aula aula1 = new Aula(null, matematica, ana, turma1A, DayOfWeek.TUESDAY,
                LocalTime.of(8, 0), LocalTime.of(9, 0));
        Aula aula2 = new Aula(null, portugues, bruno, turma1B, DayOfWeek.WEDNESDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        aulaService.salvarAula(aula1);
        aulaService.salvarAula(aula2);

        System.out.println("Aulas de exemplo salvas."); // Debugging
        System.out.println("Dados de exemplo populados com sucesso!");
    }
}