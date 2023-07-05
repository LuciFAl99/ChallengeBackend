package com.ChallengeBackend.challenge.Dtos;

import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Horario;
import com.ChallengeBackend.challenge.Entidades.Persona;
import com.ChallengeBackend.challenge.Entidades.Profesor;

import java.util.List;
import java.util.stream.Collectors;

public class ProfesorDto extends Persona {
    private Horario turnoClases;
    private List<CursoDto> cursos;
    public ProfesorDto(Profesor profesor){
        super(profesor.getNombre(), profesor.getApellido(), profesor.getEmail(), profesor.getContrasena(), profesor.getImagen());
        this.turnoClases = profesor.getTurnoClases();
        this.cursos =  profesor.getCursos()
                .stream()
                .map(curso -> new CursoDto(curso)).collect(Collectors.toList());
    }

    public Horario getTurnoClases() {
        return turnoClases;
    }

    public List<CursoDto> getCursos() {
        return cursos;
    }
}
