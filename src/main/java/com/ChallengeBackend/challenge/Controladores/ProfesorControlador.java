package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import com.ChallengeBackend.challenge.Repositorios.PersonaRepositorio;
import com.ChallengeBackend.challenge.Repositorios.ProfesorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProfesorControlador {
    @Autowired
    ProfesorRepositorio profesorRepositorio;
    @Autowired
    PersonaRepositorio personaRepositorio;
  /*  @Autowired
    private PasswordEncoder passwordEncoder;*/

    @PostMapping("/api/profesores")
    public ResponseEntity<Object> registro(@RequestBody Profesor profesor) {
        StringBuilder errorMessage = new StringBuilder();

        if (profesor.getNombre().isBlank()) {
            errorMessage.append("Nombre es requerido\n");
        }

        if (profesor.getApellido().isBlank()) {
            errorMessage.append("Apellido es requerido\n");
        }

        if (profesor.getEmail().isBlank()) {
            errorMessage.append("Email es requerido\n");
        } else if (profesor.getEmail().contains("@gmail")) {
            errorMessage.append("No puedes registrarte como alumno\n");
        } else if (profesor.getEmail().contains("@admin")) {
            errorMessage.append("No puedes registrarte como administrador\n");
        }
        Persona profesorExistente = personaRepositorio.findByEmail(profesor.getEmail());
        if(profesorExistente != null) {
            errorMessage.append("El correo electrónico ya está registrado\n");
        }
        Persona nuevoProfesor = personaRepositorio.findByContrasena(profesor.getContrasena());
            if(nuevoProfesor != null){
                errorMessage.append("La contraseña ya existe\n");
        }

        if (profesor.getContrasena().isBlank()) {
            errorMessage.append("Contraseña es requerida\n");
        } else if (profesor.getContrasena().length() < 8) {
            errorMessage.append("La contraseña debe tener al menos 8 caracteres\n");
        }

        if (profesor.getNombre().matches(".*\\d.*")) {
            errorMessage.append("El nombre no puede contener números\n");
        }

        if (profesor.getApellido().matches(".*\\d.*")) {
            errorMessage.append("El apellido no puede contener números\n");
        }

        if (errorMessage.length() > 0) {
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        profesorRepositorio.save(new Profesor(profesor.getNombre(), profesor.getApellido(), profesor.getEmail(), profesor.getContrasena()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
