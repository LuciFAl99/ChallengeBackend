package com.ChallengeBackend.challenge.Dtos;

import com.ChallengeBackend.challenge.Entidades.Enums.Horario;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;

import java.util.List;
import java.util.stream.Collectors;

public class ProfesorDto{
    private Long id;
    private String nombre, apellido, email, contrasena;
    private Horario turnoClases;
    private List<CursoDto> cursos;
    public ProfesorDto(Profesor profesor){
        this.id = profesor.getId();
        this.nombre = profesor.getNombre();
        this.apellido = profesor.getApellido();
        this.email = profesor.getEmail();
        this.contrasena = profesor.getContrasena();
        this.turnoClases = profesor.getTurnoClases();
        this.cursos =  profesor.getCursos()
                .stream()
                .map(curso -> new CursoDto(curso)).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Horario getTurnoClases() {
        return turnoClases;
    }

    public void setTurnoClases(Horario turnoClases) {
        this.turnoClases = turnoClases;
    }

    public List<CursoDto> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoDto> cursos) {
        this.cursos = cursos;
    }
}
