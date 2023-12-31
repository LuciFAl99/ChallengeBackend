package com.ChallengeBackend.challenge.Entidades;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Profesor extends Persona{
    private Horario turnoClases;
    @ManyToOne(fetch = FetchType.EAGER)
    private Administrador administrador;
    @OneToMany(mappedBy="profesor", fetch= FetchType.EAGER)
    private Set<Curso> cursos = new HashSet<>();


    public Profesor(String nombre, String apellido, String email, String contrasena, String imagen, Horario turnoClases, Set<Curso> cursos) {
        super(nombre, apellido, email, contrasena, imagen);
        this.turnoClases = turnoClases;
        this.cursos = cursos;
    }

    public Horario getTurnoClases() {
        return turnoClases;
    }

    public void setTurnoClases(Horario turnoClases) {
        this.turnoClases = turnoClases;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Set<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }
    public void dictarCurso(Curso curso){
        curso.setProfesor(this);
        cursos.add(curso);
    }
}
