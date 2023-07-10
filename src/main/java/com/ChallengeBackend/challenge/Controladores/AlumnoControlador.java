package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Dtos.AlumnoDto;
import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Enums.EstadoAcademico;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import com.ChallengeBackend.challenge.Repositorios.AlumnoRepositorio;
import com.ChallengeBackend.challenge.Repositorios.CursoRepositorio;
import com.ChallengeBackend.challenge.Repositorios.PersonaRepositorio;
import com.ChallengeBackend.challenge.Repositorios.ProfesorRepositorio;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class AlumnoControlador {
    @Autowired
    CursoRepositorio cursoRepositorio;
    @Autowired
    AlumnoRepositorio alumnoRepositorio;
    @Autowired
    PersonaRepositorio personaRepositorio;
    @Autowired
    ProfesorRepositorio profesorRepositorio;
   @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/api/alumnos/actual")
    public AlumnoDto traerTodos(Authentication authentication) {
        String email = authentication.getName();

        if (email != null) {
            Alumno alumno = alumnoRepositorio.findByEmail(email);
            if (alumno != null) {
                return new AlumnoDto (alumno);
            }
        }

        throw new RuntimeException("No se encontró ningún alumno correspondiente");
    }


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

        Persona alumnoExistente = personaRepositorio.findByEmail(alumno.getEmail());
        if (alumnoExistente != null) {
            errorMessage.append("El correo electrónico ya está registrado\n");
        }
        Persona nuevoAlumno = personaRepositorio.findByContrasena(alumno.getContrasena());
        if(nuevoAlumno != null){
            errorMessage.append("La contraseña ya existe\n");
        }

        if (errorMessage.length() > 0) {
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        alumnoRepositorio.save(new Alumno(alumno.getNombre(), alumno.getApellido(), alumno.getEmail(), passwordEncoder.encode(alumno.getContrasena()), alumno.getEstadoAcademico()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/alumnos/inscribirse")
    public ResponseEntity<Object> inscribirAlumnoACurso(Authentication authentication, @RequestParam Long cursoId) {
        String email = authentication.getName();

        Alumno alumno = alumnoRepositorio.findByEmail(email);
        if (alumno == null) {
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
        }

        Optional<Curso> optionalCurso = cursoRepositorio.findById(cursoId);
        if (!optionalCurso.isPresent()) {
            return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
        }

        Curso curso = optionalCurso.get();

        if (curso.getCupos() <= 0) {
            return new ResponseEntity<>("No hay cupos disponibles en el curso", HttpStatus.BAD_REQUEST);
        }

        if (alumno.getCursos().contains(curso)) {
            return new ResponseEntity<>("El alumno ya está inscripto en este curso", HttpStatus.BAD_REQUEST);
        }

        alumno.inscribirCurso(curso);
        curso.setCupos(curso.getCupos() - 1);

        alumnoRepositorio.save(alumno);
        cursoRepositorio.save(curso);

        return new ResponseEntity<>("Alumno inscrito exitosamente en el curso", HttpStatus.OK);
    }
    @PatchMapping("/api/modificar/{id}")
    public ResponseEntity<Object> modificarPropieda(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        String email = authentication.getName(); // Obtener el email del alumno autenticado

        Alumno alumnoExistente = alumnoRepositorio.findByEmail(email);
        if (alumnoExistente != null) {
            List<String> propiedadesModificadas = new ArrayList<>();

            for (Map.Entry<String, Object> entry : cambios.entrySet()) {
                String propiedad = entry.getKey();
                Object valor = entry.getValue();

                switch (propiedad) {
                    case "nombre":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            alumnoExistente.setNombre((String) valor);
                            propiedadesModificadas.add("Nombre");
                        }
                        break;
                    case "apellido":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            alumnoExistente.setApellido((String) valor);
                            propiedadesModificadas.add("Apellido");
                        }
                        break;
                    case "email":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            String nuevoEmail = (String) valor;

                            if (personaRepositorio.findByEmail(nuevoEmail) != null) {
                                return new ResponseEntity<>("El correo electrónico ya está registrado", HttpStatus.BAD_REQUEST);
                            }

                            alumnoExistente.setEmail(nuevoEmail);
                            propiedadesModificadas.add("Email");
                        }
                        break;
                    case "contrasena":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            if (((String) valor).length() < 8) {
                                return new ResponseEntity<>("La contraseña debe tener al menos 8 caracteres", HttpStatus.BAD_REQUEST);
                            }
                            alumnoExistente.setContrasena((String) valor);
                            propiedadesModificadas.add("Contraseña");
                        }
                        break;
                    case "estadoAcademico":
                        if (valor instanceof String) {
                            String nuevoEstadoAcademicoStr = (String) valor;
                            EstadoAcademico nuevoEstadoAcademico = null;

                            if (nuevoEstadoAcademicoStr.equalsIgnoreCase("Graduado")) {
                                nuevoEstadoAcademico = EstadoAcademico.GRADUADO;
                            } else if (nuevoEstadoAcademicoStr.equalsIgnoreCase("En Pausa")) {
                                nuevoEstadoAcademico = EstadoAcademico.EN_PAUSA;
                            } else if (nuevoEstadoAcademicoStr.equalsIgnoreCase("Activo")) {
                                nuevoEstadoAcademico = EstadoAcademico.ACTIVO;
                            }

                            if (nuevoEstadoAcademico != null) {
                                alumnoExistente.setEstadoAcademico(nuevoEstadoAcademico);
                                propiedadesModificadas.add("Estado Académico");
                            }
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
                alumnoRepositorio.save(alumnoExistente);
                return new ResponseEntity<>(mensaje.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se encontraron propiedades para modificar", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
        }
    }




}
