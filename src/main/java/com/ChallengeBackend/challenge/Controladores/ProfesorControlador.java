package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Dtos.ProfesorDto;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.ChallengeBackend.challenge.Servicios.ProfesorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ProfesorControlador {
    @Autowired
    ProfesorServicio profesorServicio;
  @GetMapping("/api/profesores/actual")
  public ProfesorDto traerTodos(Authentication autenticacion) {
      return profesorServicio.traerTodos(autenticacion);
  }
    @PostMapping("/api/profesores")
    public ResponseEntity<Object> registro(@RequestBody Profesor profesor) {
        return profesorServicio.registro(profesor);
    }
    @PatchMapping("/api/modificarProfesor/{id}")
    public ResponseEntity<Object> modificarPropiedadProfesor(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return profesorServicio.modificarPropiedadProfesor(authentication, id, cambios);
    }

}
