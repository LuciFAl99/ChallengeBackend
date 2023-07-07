package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Dtos.AlumnoDto;
import com.ChallengeBackend.challenge.Dtos.CursoDto;
import com.ChallengeBackend.challenge.Dtos.ProfesorDto;
import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Enums.EstadoAcademico;
import com.ChallengeBackend.challenge.Entidades.Enums.Horario;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.ChallengeBackend.challenge.Repositorios.AlumnoRepositorio;
import com.ChallengeBackend.challenge.Repositorios.CursoRepositorio;
import com.ChallengeBackend.challenge.Repositorios.ProfesorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class AdministradorControlador {
    @Autowired
    AlumnoRepositorio alumnoRepositorio;
    @Autowired
    ProfesorRepositorio profesorRepositorio;
    @Autowired
    CursoRepositorio cursoRepositorio;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/api/alumnos")
    public List<AlumnoDto> traerAlumnos() {
        return alumnoRepositorio.findAll().stream().map(alumno -> new AlumnoDto(alumno)).collect(toList());
    }

    @GetMapping("/api/profesores")
    public List<ProfesorDto> traerProfesores() {
        return profesorRepositorio.findAll().stream().map(profesor -> new ProfesorDto(profesor)).collect(toList());
    }

    @GetMapping("/api/cursos")
    public List<CursoDto> traerCursos() {
        return cursoRepositorio.findAll().stream().map(curso -> new CursoDto(curso)).collect(toList());
    }

    @PostMapping("/api/cursos")
    public ResponseEntity<Object> crearProfesor(@RequestBody Curso curso, @RequestParam Long profesorId) {
        StringBuilder errorMessage = new StringBuilder();
        if (curso.getNombreCurso().isEmpty()) {
            errorMessage.append("Nombre es requerido");
        } else if (curso.getDescripcion().isEmpty()) {
            errorMessage.append("Descripción es requerido");
        } else if (curso.getHorario() == null) {
            errorMessage.append("Horario es requerido");
        } else if (!curso.isPresencial()) {
            errorMessage.append("Presencial es requerido");
        } else if (curso.getFechaInicio() == null) {
            errorMessage.append("Fecha de inicio es requerido");
        } else if (curso.getFechaFin() == null) {
            errorMessage.append("Fecha de fin es requerido");
        } else if (curso.getCupos() == 0 || curso.getCupos() < 0) {
            errorMessage.append("Número de cupos es requerido");
        } else if (curso.getImagen().isEmpty()) {
            errorMessage.append("Imagen es requerido");
        } else if (curso.getMaterias().isEmpty()) {
            errorMessage.append("Materias es requerido");
        }
        if (errorMessage.length() > 0) {
            String errorString = errorMessage.toString();
            return new ResponseEntity<>(errorString, HttpStatus.FORBIDDEN);
        }
        Optional<Profesor> optionalProfesor = profesorRepositorio.findById(profesorId);
        if (optionalProfesor.isPresent()) {
            Profesor profesor = optionalProfesor.get();
            curso.setProfesor(profesor);
            cursoRepositorio.save(curso);
        } else {
            return new ResponseEntity<>("Profesor no existe", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @DeleteMapping("/api/profesores/{id}")
    @Transactional
    public ResponseEntity<Object> eliminarProfesor(@PathVariable Long id, @RequestParam Long otroProfesorId) {
        Optional<Profesor> optionalProfesorAEliminar = profesorRepositorio.findById(id);
        Optional<Profesor> optionalOtroProfesor = profesorRepositorio.findById(otroProfesorId);

        if (optionalProfesorAEliminar.isPresent() && optionalOtroProfesor.isPresent()) {
            Profesor profesorAEliminar = optionalProfesorAEliminar.get();
            Profesor otroProfesor = optionalOtroProfesor.get();

            profesorAEliminar.getCursos().forEach(curso -> curso.setProfesor(otroProfesor));

            profesorRepositorio.delete(profesorAEliminar);
            return new ResponseEntity<>("Profesor eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Profesor o otro profesor no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/alumnos/{id}")
    public ResponseEntity<Object> eliminarAlumno(@PathVariable Long id) {
        Optional<Alumno> optionalAlumno = alumnoRepositorio.findById(id);
        if (optionalAlumno.isPresent()) {
            Alumno alumno = optionalAlumno.get();
            alumnoRepositorio.delete(alumno);
            return new ResponseEntity<>("Alumno eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/cursos/{id}")
    public ResponseEntity<Object> eliminarCurso(@PathVariable Long id) {
        Optional<Curso> optionalCurso = cursoRepositorio.findById(id);
        if (optionalCurso.isPresent()) {
            Curso curso = optionalCurso.get();

            for (Alumno alumno : curso.getAlumnos()) {
                alumno.getCursos().remove(curso);
            }
            curso.getAlumnos().clear();

            curso.getMaterias().clear();

            cursoRepositorio.delete(curso);

            return new ResponseEntity<>("Curso eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/api/profesores/{id}")
    public ResponseEntity<Object> modificarPropiedadProfesor(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        Optional<Profesor> optionalProfesor = profesorRepositorio.findById(id);
        if (optionalProfesor.isPresent()) {
            Profesor profesorExistente = optionalProfesor.get();
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
                            profesorExistente.setEmail((String) valor);
                            propiedadesModificadas.add("Email");
                        }
                        break;
                    case "contrasena":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            if (((String) valor).length() < 8) {
                                return new ResponseEntity<>("La contraseña debe tener al menos 8 caracteres", HttpStatus.BAD_REQUEST);
                            }
                            profesorExistente.setContrasena(passwordEncoder.encode((String) valor));
                            propiedadesModificadas.add("Contraseña");
                        }
                        break;
                    case "turnoClases":
                        if (valor instanceof String) {
                            Horario nuevoTurno = Horario.valueOf((String) valor);
                            profesorExistente.setTurnoClases(nuevoTurno);
                            propiedadesModificadas.add("Turno de clases");
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
                return new ResponseEntity<>(mensaje.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se encontraron propiedades para modificar", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        }
    }
    @PatchMapping("/api/alumnos/{id}")
    public ResponseEntity<Object> modificarPropiedadAlumno(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        Optional<Alumno> optionalAlumno = alumnoRepositorio.findById(id);
        if (optionalAlumno.isPresent()) {
            Alumno alumnoExistente = optionalAlumno.get();
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
                            alumnoExistente.setEmail((String) valor);
                            propiedadesModificadas.add("Email");
                        }
                        break;
                    case "contrasena":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            if (((String) valor).length() < 8) {
                                return new ResponseEntity<>("La contraseña debe tener al menos 8 caracteres", HttpStatus.BAD_REQUEST);
                            }
                            alumnoExistente.setContrasena(passwordEncoder.encode((String) valor));
                            propiedadesModificadas.add("Contraseña");
                        }
                        break;
                    case "turnoClases":
                        if (valor instanceof String) {
                            EstadoAcademico nuevoEstadoAcademico = EstadoAcademico.valueOf((String) valor);
                            alumnoExistente.setEstadoAcademico(nuevoEstadoAcademico);
                            propiedadesModificadas.add("Estado Académico");
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
                return new ResponseEntity<>(mensaje.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se encontraron propiedades para modificar", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        }
    }

}


