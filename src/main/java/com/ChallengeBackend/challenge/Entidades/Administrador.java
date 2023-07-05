package com.ChallengeBackend.challenge.Entidades;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Administrador extends Persona{
    @OneToMany(mappedBy="administrador", fetch= FetchType.EAGER)
    private Set<Curso> cursos = new HashSet<>();
    @OneToMany(mappedBy="administrador", fetch= FetchType.EAGER)
    private Set<Alumno> alumnos = new HashSet<>();
    @OneToMany(mappedBy="administrador", fetch= FetchType.EAGER)
    private Set<Profesor> profesores = new HashSet<>();

    public Administrador(String nombre, String apellido, String email, String contrasena, String imagen) {
        super(nombre, apellido, email, contrasena, imagen);
    }

    public Set<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

    public Set<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public Set<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(Set<Profesor> profesores) {
        this.profesores = profesores;
    }
    public void agregarCurso(Curso curso){
        curso.setAdministrador(this);
        cursos.add(curso);
    }
    public void agregarProfesor(Profesor profesor){
        profesor.setAdministrador(this);
        profesores.add(profesor);
    }
    public void agregarAlumno(Alumno alumno){
        alumno.setAdministrador(this);
        alumnos.add(alumno);
    }
}
