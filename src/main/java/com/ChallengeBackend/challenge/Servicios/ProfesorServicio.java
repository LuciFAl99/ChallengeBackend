package com.ChallengeBackend.challenge.Servicios;

import com.ChallengeBackend.challenge.Dtos.ProfesorDto;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface ProfesorServicio {
    ProfesorDto traerTodos(Authentication autenticacion);
    ResponseEntity<Object> registro(Profesor profesor);
    ResponseEntity<Object> modificarPropiedadProfesor(Authentication authentication, Long id,  Map<String, Object> cambios);

}
