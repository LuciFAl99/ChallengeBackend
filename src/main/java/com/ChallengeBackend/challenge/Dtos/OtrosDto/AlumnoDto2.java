package com.ChallengeBackend.challenge.Dtos.OtrosDto;

import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;

public class AlumnoDto2 {
    private String nombre;
    private String apellido;

    public AlumnoDto2() {
    }
    public AlumnoDto2(Alumno alumno){
        if(alumno != null) {
            this.nombre = alumno.getNombre();
            this.apellido = alumno.getApellido();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
