package com.ChallengeBackend.challenge.Entidades.Subclases;


import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Enums.EstadoAcademico;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Alumno extends Persona {
    @ManyToMany
    @JoinTable(
            name = "alumno_curso",
            joinColumns = @JoinColumn(name = "alumno_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursos = new HashSet<>();
    private EstadoAcademico estadoAcademico;

    public Alumno(){}

    public Alumno(String nombre, String apellido, String email, String contrasena, EstadoAcademico estadoAcademico) {
        super(nombre, apellido, email, contrasena);
        this.estadoAcademico = estadoAcademico;
    }

    public Set<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

    public EstadoAcademico getEstadoAcademico() {
        return estadoAcademico;
    }

    public void setEstadoAcademico(EstadoAcademico estadoAcademico) {
        this.estadoAcademico = estadoAcademico;
    }

    public void inscribirCurso(Curso curso){
        cursos.add(curso);
        curso.getAlumnos().add(this);
        estadoAcademico = EstadoAcademico.ACTIVO;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                super.toString()+
                "estadoAcademico=" + estadoAcademico +
                '}';
    }
}
