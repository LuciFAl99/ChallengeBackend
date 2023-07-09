package com.ChallengeBackend.challenge.Entidades.Subclases;

import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;

import javax.persistence.Entity;

@Entity
public class Administrador extends Persona {
    public Administrador(){}
    public Administrador(String nombre, String apellido, String email, String contrasena) {
        super(nombre, apellido, email, contrasena);
    }


}
