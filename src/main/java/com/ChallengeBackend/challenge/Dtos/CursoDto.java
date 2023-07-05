package com.ChallengeBackend.challenge.Dtos;

import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Enums.Horario;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CursoDto{
    private long Id;
    private String nombreCurso;
    private String descripcion;
    private Horario horario;
    private boolean presencial;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupos;
    private List<String> materias;
    private List<AlumnoDto> alumnos;

    public CursoDto() {
    }
    public CursoDto(Curso curso){
        this.nombreCurso = curso.getNombreCurso();
        this.descripcion = curso.getDescripcion();
        this.horario = curso.getHorario();
        this.presencial = curso.isPresencial();
        this.fechaInicio = curso.getFechaInicio();
        this.fechaFin = curso.getFechaFin();
        this.cupos = curso.getCupos();
        this.materias = curso.getMaterias();
        this.alumnos = curso.getAlumnos()
                .stream()
                .map(alumno -> new AlumnoDto(alumno))
                .collect(Collectors.toList());
    }

    public long getId() {
        return Id;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Horario getHorario() {
        return horario;
    }

    public boolean isPresencial() {
        return presencial;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getCupos() {
        return cupos;
    }

    public List<String> getMaterias() {
        return materias;
    }
}
