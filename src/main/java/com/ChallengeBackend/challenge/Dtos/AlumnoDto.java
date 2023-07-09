package com.ChallengeBackend.challenge.Dtos;

import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Enums.EstadoAcademico;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;

import java.util.List;
import java.util.stream.Collectors;

public class AlumnoDto{
    private Long id;
    private String nombre, apellido, email, contrasena;
    private List<CursoDto> cursos;
    private EstadoAcademico estadoAcademico;

    public AlumnoDto(){}
    public AlumnoDto(Alumno alumno){
        this.id = alumno.getId();
        this.nombre = alumno.getNombre();
        this.apellido = alumno.getApellido();
        this.email = alumno.getEmail();
        this.contrasena = alumno.getContrasena();        this.estadoAcademico = alumno.getEstadoAcademico();
        this.cursos =  alumno.getCursos()
                .stream()
                .map(curso -> new CursoDto(curso)).collect(Collectors.toList());

    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public List<CursoDto> getCursos() {
        return cursos;
    }

    public EstadoAcademico getEstadoAcademico() {
        return estadoAcademico;
    }
}
