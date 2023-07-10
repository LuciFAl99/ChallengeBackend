package com.ChallengeBackend.challenge.Entidades;

import com.ChallengeBackend.challenge.Entidades.Enums.Horario;
import com.ChallengeBackend.challenge.Entidades.Subclases.Administrador;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Column(length = 4000)
    private String descripcion;
    private Horario horario;
    private boolean presencial;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupos;
    private String imagen;
    private String categoria;
    @ElementCollection
    private List<String> materias = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    private Profesor profesor;
    @ManyToMany(mappedBy = "cursos", fetch = FetchType.EAGER)
    private Set <Alumno> alumnos = new HashSet<>();

    public Curso() {
    }

    public Curso(String nombreCurso, String descripcion, Horario horario, boolean presencial, LocalDate fechaInicio, LocalDate fechaFin, int cupos, String imagen, String categoria, List<String> materias) {
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.horario = horario;
        this.presencial = presencial;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupos = cupos;
        this.imagen = imagen;
        this.categoria = categoria;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public Set<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Alumno> alumnos) {
        this.alumnos = alumnos;
    }
}
