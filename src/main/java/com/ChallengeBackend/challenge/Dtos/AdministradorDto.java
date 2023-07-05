package com.ChallengeBackend.challenge.Dtos;

import com.ChallengeBackend.challenge.Entidades.Administrador;
import com.ChallengeBackend.challenge.Entidades.Persona;

import java.util.List;
import java.util.stream.Collectors;

public class AdministradorDto extends Persona {
    private List<CursoDto> cursos;
    private List<AlumnoDto> alumnos;
    private List<ProfesorDto>profesores;
    public AdministradorDto(Administrador administrador){
        super(administrador.getNombre(), administrador.getApellido(), administrador.getEmail(), administrador.getContrasena(), administrador.getImagen());
        this.cursos =  administrador.getCursos()
                .stream()
                .map(curso -> new CursoDto(curso)).collect(Collectors.toList());
        this.alumnos = administrador.getAlumnos()
                .stream()
                .map(alumno -> new AlumnoDto(alumno)).collect(Collectors.toList());
        this.profesores = administrador.getProfesores()
                .stream()
                .map(profesor -> new ProfesorDto(profesor)).collect(Collectors.toList());
    }

    public List<CursoDto> getCursos() {
        return cursos;
    }

    public List<AlumnoDto> getAlumnos() {
        return alumnos;
    }

    public List<ProfesorDto> getProfesores() {
        return profesores;
    }
}
