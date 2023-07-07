package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Repositorios.AlumnoRepositorio;
import com.ChallengeBackend.challenge.Repositorios.CursoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AlumnoControlador {
    @Autowired
    CursoRepositorio cursoRepositorio;
    @Autowired
    AlumnoRepositorio alumnoRepositorio;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/alumnos")
    public ResponseEntity<Object> registro(@RequestBody Alumno alumno) {

        StringBuilder errorMessage = new StringBuilder();
        if (alumno.getNombre().isBlank()) {
            errorMessage.append("Nombre es requerido");
        } else if (alumno.getApellido().isBlank()) {
            errorMessage.append("Apellido es requerido");
        } else if (alumno.getEmail().isBlank()) {
            errorMessage.append("Email es requerido");
        } else if (alumno.getContrasena().isBlank()) {
            errorMessage.append("Contraseña es requerida");
        } else if (alumno.getContrasena().length() < 8) {
            errorMessage.append("La contraseña debe tener al menos 8 caracteres");
        } else if (alumno.getEstadoAcademico() == null) {
            errorMessage.append("Estado académico es requerido");
        }
        if (errorMessage.length() > 0) {
            String errorString = errorMessage.toString();
            return new ResponseEntity<>(errorString, HttpStatus.FORBIDDEN);
        }
        alumnoRepositorio.save(new Alumno(alumno.getNombre(), alumno.getApellido(), alumno.getEmail(), passwordEncoder.encode(alumno.getContrasena()), alumno.getEstadoAcademico()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
