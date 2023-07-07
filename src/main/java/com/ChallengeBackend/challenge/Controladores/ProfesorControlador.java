package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.ChallengeBackend.challenge.Repositorios.ProfesorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfesorControlador {
    @Autowired
    ProfesorRepositorio profesorRepositorio;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/profesores")
    public ResponseEntity<Object> crearProfesor(@RequestBody Profesor profesor){

        StringBuilder errorMessage = new StringBuilder();
        if (profesor.getNombre().isBlank()){
            errorMessage.append("Nombre es requerido");
        } else if (profesor.getApellido().isBlank()) {
            errorMessage.append("Apellido es requerido");
        } else if (profesor.getEmail().isBlank()) {
            errorMessage.append("Email es requerido");
        } else if (profesor.getContrasena().isBlank()){
            errorMessage.append("Contraseña es requerida");
        } else if (profesor.getContrasena().length() < 8){
            errorMessage.append("La contraseña debe tener al menos 8 caracteres");
        } else if (profesor.getTurnoClases() == null){
            errorMessage.append("Turno de clases es requerido");
        }
        if (errorMessage.length() > 0) {
            String errorString = errorMessage.toString();
            return new ResponseEntity<>(errorString, HttpStatus.FORBIDDEN);
        }

        profesorRepositorio.save(new Profesor(profesor.getNombre(), profesor.getApellido(), profesor.getEmail(), passwordEncoder.encode(profesor.getContrasena()), profesor.getTurnoClases()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
