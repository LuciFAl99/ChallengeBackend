package com.ChallengeBackend.challenge.Dtos.OtrosDto;

import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;

public class ProfesorDto2 {
    private Long id;
    private String nombre;
    private String apellido;

    public ProfesorDto2() {
    }

    public ProfesorDto2(Profesor profesor){
        this.id = profesor.getId();
        this.nombre = profesor.getNombre();
        this.apellido = profesor.getApellido();
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
