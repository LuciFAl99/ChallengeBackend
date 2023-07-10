package com.ChallengeBackend.challenge.Dtos.OtrosDto;

import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;

public class AlumnoDto2 {
    private Long id;
    private String nombre;
    private String apellido;

    public AlumnoDto2() {
    }

    public AlumnoDto2(Alumno alumno){
        this.id = alumno.getId();
        this.nombre = alumno.getNombre();
        this.apellido = alumno.getApellido();
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
}
