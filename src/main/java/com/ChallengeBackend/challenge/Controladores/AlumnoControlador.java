package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Repositorios.AlumnoRepositorio;
import com.ChallengeBackend.challenge.Repositorios.CursoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
            errorMessage.append("Nombre es requerido\n");
        }

        if (alumno.getApellido().isBlank()) {
            errorMessage.append("Apellido es requerido\n");
        }

        if (alumno.getEmail().isBlank()) {
            errorMessage.append("Email es requerido\n");
        } else if (alumno.getEmail().contains("@profesor")) {
            errorMessage.append("No puedes registrarte como profesor\n");
        } else if (alumno.getEmail().contains("@admin")) {
            errorMessage.append("No puedes registrarte como administrador\n");
        }

        if (alumno.getContrasena().isBlank()) {
            errorMessage.append("Contraseña es requerida\n");
        } else if (alumno.getContrasena().length() < 8) {
            errorMessage.append("La contraseña debe tener al menos 8 caracteres\n");
        }

        if (alumno.getEstadoAcademico() == null) {
            errorMessage.append("Estado académico es requerido\n");
        }

        if (alumno.getNombre().matches(".*\\d.*")) {
            errorMessage.append("El nombre no puede contener números\n");
        }

        if (alumno.getApellido().matches(".*\\d.*")) {
            errorMessage.append("El apellido no puede contener números\n");
        }

        if (errorMessage.length() > 0) {
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        alumnoRepositorio.save(new Alumno(alumno.getNombre(), alumno.getApellido(), alumno.getEmail(), passwordEncoder.encode(alumno.getContrasena()), alumno.getEstadoAcademico()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/alumnos/inscribirse")
    public ResponseEntity<Object> inscribirAlumnoACurso(@RequestParam Long alumnoId, @RequestParam Long cursoId) {
        Optional<Alumno> optionalAlumno = alumnoRepositorio.findById(alumnoId);
        if (!optionalAlumno.isPresent()) {
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
        }

        Optional<Curso> optionalCurso = cursoRepositorio.findById(cursoId);
        if (!optionalCurso.isPresent()) {
            return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
        }

        Alumno alumno = optionalAlumno.get();
        Curso curso = optionalCurso.get();

        if (curso.getCupos() <= 0) {
            return new ResponseEntity<>("No hay cupos disponibles en el curso", HttpStatus.BAD_REQUEST);
        }

        if (curso.getCupos() <= 0) {
            return new ResponseEntity<>("No hay cupos disponibles en el curso", HttpStatus.BAD_REQUEST);
        }

        if (alumno.getCursos().contains(curso)) {
            return new ResponseEntity<>("El alumno ya está inscrito en este curso", HttpStatus.BAD_REQUEST);
        }

        alumno.inscribirCurso(curso);
        curso.setCupos(curso.getCupos() - 1);

        alumnoRepositorio.save(alumno);
        cursoRepositorio.save(curso);

        return new ResponseEntity<>("Alumno inscrito exitosamente en el curso", HttpStatus.OK);
    }


}
