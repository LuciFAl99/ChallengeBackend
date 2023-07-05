package com.ChallengeBackend.challenge.Entidades;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long Id;
    private String nombreCurso;
    private String descripcion;
    private Horario horario;
    private boolean presencial;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupos;
    private List<String> materias;
    @ManyToOne(fetch = FetchType.EAGER)
    private Profesor profesor;
    @ManyToOne(fetch = FetchType.EAGER)
    private Administrador administrador;
    @ManyToMany(mappedBy = "cursos", fetch = FetchType.EAGER)
    private Set <Alumno> alumnos = new HashSet<>();

    public Curso() {
    }

    public Curso(String nombreCurso, String descripcion, Horario horario, boolean presencial, LocalDate fechaInicio, LocalDate fechaFin, int cupos, List<String> materias) {
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.horario = horario;
        this.presencial = presencial;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupos = cupos;
        this.materias = materias;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public boolean isPresencial() {
        return presencial;
    }

    public void setPresencial(boolean presencial) {
        this.presencial = presencial;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public List<String> getMaterias() {
        return materias;
    }

    public void setMaterias(List<String> materias) {
        this.materias = materias;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Set<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Alumno> alumnos) {
        this.alumnos = alumnos;
    }
}
