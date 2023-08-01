package com.ChallengeBackend.challenge.Servicios;

import com.ChallengeBackend.challenge.Dtos.AlumnoDto;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AlumnoServicio {
    AlumnoDto traerTodos(Authentication authentication);
    ResponseEntity<Object> registro(Alumno alumno);
    ResponseEntity<Object> inscribirAlumnoACurso(Authentication authentication,Long cursoId);
    ResponseEntity<Object> modificarPropiedad(Authentication authentication, Long id, Map<String, Object> cambios);
}
