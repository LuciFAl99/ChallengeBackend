package com.ChallengeBackend.challenge.Entidades.Subclases;

import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;

import javax.persistence.Entity;

@Entity
public class Administrador extends Persona {

    public Administrador(String nombre, String apellido, String email, String contrasena, String imagen) {
        super(nombre, apellido, email, contrasena, imagen);
    }

}