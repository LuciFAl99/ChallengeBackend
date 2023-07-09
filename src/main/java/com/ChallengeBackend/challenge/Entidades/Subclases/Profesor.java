package com.ChallengeBackend.challenge.Entidades.Subclases;

import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Enums.Horario;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Profesor extends Persona {
    @OneToMany(mappedBy="profesor", fetch= FetchType.EAGER)
    private Set<Curso> cursos = new HashSet<>();

    public Profesor(){}
    public Profesor(String nombre, String apellido, String email, String contrasena) {
        super(nombre, apellido, email, contrasena);
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
