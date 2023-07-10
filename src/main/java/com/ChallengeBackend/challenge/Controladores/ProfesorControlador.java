package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Dtos.ProfesorDto;
import com.ChallengeBackend.challenge.Entidades.Enums.EstadoAcademico;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import com.ChallengeBackend.challenge.Repositorios.PersonaRepositorio;
import com.ChallengeBackend.challenge.Repositorios.ProfesorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProfesorControlador {
    @Autowired
    ProfesorRepositorio profesorRepositorio;
    @Autowired
    PersonaRepositorio personaRepositorio;
   @Autowired
    private PasswordEncoder passwordEncoder;
  @GetMapping("/api/profesores/actual")
  public ProfesorDto traerTodos(Authentication autenticacion) {
      String email = autenticacion.getName();

      if (email != null) {
          Profesor profesor = profesorRepositorio.findByEmail(email);
          if (profesor != null) {
              return new ProfesorDto (profesor);
          }
      }

      throw new RuntimeException("No se encontró ningún alumno correspondiente");
  }

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

        profesorRepositorio.save(new Profesor(profesor.getNombre(), profesor.getApellido(), profesor.getEmail(), passwordEncoder.encode(profesor.getContrasena())));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/api/modificarProfesor/{id}")
    public ResponseEntity<Object> modificarPropiedadProfesor(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        String email = authentication.getName();

        Profesor profesorExistente = profesorRepositorio.findByEmail(email);
        if (profesorExistente != null) {
            List<String> propiedadesModificadas = new ArrayList<>();

            for (Map.Entry<String, Object> entry : cambios.entrySet()) {
                String propiedad = entry.getKey();
                Object valor = entry.getValue();

                switch (propiedad) {
                    case "nombre":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            profesorExistente.setNombre((String) valor);
                            propiedadesModificadas.add("Nombre");
                        }
                        break;
                    case "apellido":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            profesorExistente.setApellido((String) valor);
                            propiedadesModificadas.add("Apellido");
                        }
                        break;
                    case "email":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            String nuevoEmail = (String) valor;

                            if (personaRepositorio.findByEmail(nuevoEmail) != null) {
                                return new ResponseEntity<>("El correo electrónico ya está registrado", HttpStatus.BAD_REQUEST);
                            }

                            profesorExistente.setEmail(nuevoEmail);
                            propiedadesModificadas.add("Email");
                        }
                        break;
                    case "contrasena":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            if (((String) valor).length() < 8) {
                                return new ResponseEntity<>("La contraseña debe tener al menos 8 caracteres", HttpStatus.BAD_REQUEST);
                            }
                            profesorExistente.setContrasena((String) valor);
                            propiedadesModificadas.add("Contraseña");
                        }
                        break;
                    default:
                        return new ResponseEntity<>("Propiedad no válida: " + propiedad, HttpStatus.BAD_REQUEST);
                }
            }

            if (!propiedadesModificadas.isEmpty()) {
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("Modificado correctamente: ");
                for (int i = 0; i < propiedadesModificadas.size(); i++) {
                    mensaje.append(propiedadesModificadas.get(i));
                    if (i < propiedadesModificadas.size() - 1) {
                        mensaje.append(", ");
                    }
                }
                profesorRepositorio.save(profesorExistente);
                return new ResponseEntity<>(mensaje.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se encontraron propiedades para modificar", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        }
    }

}
