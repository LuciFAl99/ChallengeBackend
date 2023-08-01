package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Dtos.AlumnoDto;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Servicios.AlumnoServicio;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AlumnoControlador {
    @Autowired
    AlumnoServicio alumnoServicio;

    @GetMapping("/api/alumnos/actual")
    public AlumnoDto traerTodos(Authentication authentication) {
       return alumnoServicio.traerTodos(authentication);
    }

    @PostMapping("/api/alumnos")
    public ResponseEntity<Object> registro(@RequestBody Alumno alumno) {
        return alumnoServicio.registro(alumno);
    }

    @PostMapping("/api/alumnos/inscribirse")
    public ResponseEntity<Object> inscribirAlumnoACurso(Authentication authentication, @RequestParam Long cursoId) {
       return alumnoServicio.inscribirAlumnoACurso(authentication,cursoId);
    }
    @PatchMapping("/api/modificar/{id}")
    public ResponseEntity<Object> modificarPropiedad(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, Object> cambios) {
       return alumnoServicio.modificarPropiedad(authentication, id, cambios);
    }

}
