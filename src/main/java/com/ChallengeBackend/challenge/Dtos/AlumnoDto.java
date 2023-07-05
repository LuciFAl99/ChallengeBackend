package com.ChallengeBackend.challenge.Dtos;

import com.ChallengeBackend.challenge.Entidades.Alumno;
import com.ChallengeBackend.challenge.Entidades.EstadoAcademico;
import com.ChallengeBackend.challenge.Entidades.Persona;

import java.util.List;
import java.util.stream.Collectors;

public class AlumnoDto extends Persona {
    private List<CursoDto> cursos;
    private EstadoAcademico estadoAcademico;

    public AlumnoDto(Alumno alumno){
        super(alumno.getNombre(), alumno.getApellido(), alumno.getEmail(), alumno.getContrasena(), alumno.getImagen());
        this.estadoAcademico = alumno.getEstadoAcademico();
        this.cursos =  alumno.getCursos()
                .stream()
                .map(curso -> new CursoDto(curso)).collect(Collectors.toList());

    }

    public List<CursoDto> getCursos() {
        return cursos;
    }

    public EstadoAcademico getEstadoAcademico() {
        return estadoAcademico;
    }
}
