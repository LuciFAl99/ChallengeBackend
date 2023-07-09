package com.ChallengeBackend.challenge.Dtos;

import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Enums.Horario;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CursoDto{
    private long id;
    private String nombreCurso;
    private String descripcion;
    private Horario horario;
    private boolean presencial;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupos;
    private String imagen;
    private String categoria;
    private List<String> materias;

    public CursoDto() {
    }
    public CursoDto(Curso curso){
        this.id = curso.getId();
        this.nombreCurso = curso.getNombreCurso();
        this.descripcion = curso.getDescripcion();
        this.horario = curso.getHorario();
        this.presencial = curso.isPresencial();
        this.fechaInicio = curso.getFechaInicio();
        this.fechaFin = curso.getFechaFin();
        this.cupos = curso.getCupos();
        this.imagen = curso.getImagen();
        this.categoria = curso.getCategoria();
        this.materias = curso.getMaterias();
    }

    public long getId() {
        return id;
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

    public String getImagen() {
        return imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public List<String> getMaterias() {
        return materias;
    }

}
