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
import com.ChallengeBackend.challenge.Repositorios.PersonaRepositorio;
import com.ChallengeBackend.challenge.Repositorios.ProfesorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

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
    PersonaRepositorio personaRepositorio;
  /*  @Autowired
    private PasswordEncoder passwordEncoder;*/


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
    public ResponseEntity<Object> crearCursos(@RequestBody Curso curso) {
        StringBuilder errorMessage = new StringBuilder();

        if (curso.getNombreCurso().isEmpty()) {
            errorMessage.append("Nombre es requerido\n");
        }

        if (curso.getDescripcion().isEmpty()) {
            errorMessage.append("Descripción es requerida\n");
        }

        if (curso.getHorario() == null) {
            errorMessage.append("Horario es requerido\n");
        }

        if (!Boolean.valueOf(curso.isPresencial()).equals(curso.isPresencial())) {
            errorMessage.append("Presencial es requerido\n");
        }

        if (curso.getFechaInicio() == null) {
            errorMessage.append("Fecha de inicio es requerida\n");
        }

        if (curso.getFechaFin() == null) {
            errorMessage.append("Fecha de fin es requerida\n");
        }

        if (curso.getCupos() == 0 || curso.getCupos() < 0) {
            errorMessage.append("Número de cupos es requerido\n");
        }

        if (curso.getImagen().isEmpty()) {
            errorMessage.append("Imagen es requerida\n");
        }
        if (curso.getCategoria().isBlank()) {
            errorMessage.append("Categoría es requerido\n");
        }

        if (curso.getMaterias().isEmpty()) {
            errorMessage.append("Materias es requerida\n");
        }

        if (errorMessage.length() > 0) {
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.FORBIDDEN);
        }

        Curso cursoExistente = cursoRepositorio.findByNombreCurso(curso.getNombreCurso());
        if (cursoExistente != null) {
            return new ResponseEntity<>("El curso ya existe", HttpStatus.BAD_REQUEST);
        }

        curso.setProfesor(null);
        cursoRepositorio.save(curso);

        return new ResponseEntity<>("Curso creado correctamente", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/profesores/{id}")
    @Transactional
    public ResponseEntity<Object> eliminarProfesor(@PathVariable Long id) {
        Optional<Profesor> optionalProfesorAEliminar = profesorRepositorio.findById(id);

        if (optionalProfesorAEliminar.isPresent()) {
            Profesor profesorAEliminar = optionalProfesorAEliminar.get();

            profesorAEliminar.getCursos().forEach(curso -> curso.setProfesor(null));

            profesorRepositorio.delete(profesorAEliminar);
            return new ResponseEntity<>("Profesor eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
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
    @DeleteMapping("/api/cursos/{id}/materias/{materia}")
    public ResponseEntity<Object> eliminarMateriaDelCurso(@PathVariable Long id, @PathVariable String materia) {
        Optional<Curso> optionalCurso = cursoRepositorio.findById(id);
        if (optionalCurso.isPresent()) {
            Curso curso = optionalCurso.get();
            List<String> materias = curso.getMaterias();
            if (materias.contains(materia)) {
                materias.remove(materia);
                cursoRepositorio.save(curso);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/api/cursos/{id}/eliminar-alumno/{alumnoId}")
    public ResponseEntity<Object> eliminarAlumnoCurso(@PathVariable Long id, @PathVariable Long alumnoId) {
        Optional<Curso> optionalCurso = cursoRepositorio.findById(id);
        if (optionalCurso.isPresent()) {
            Curso cursoExistente = optionalCurso.get();

            Optional<Alumno> optionalAlumno = alumnoRepositorio.findById(alumnoId);
            if (optionalAlumno.isPresent()) {
                Alumno alumnoExistente = optionalAlumno.get();

                cursoExistente.getAlumnos().remove(alumnoExistente);
                alumnoExistente.getCursos().remove(cursoExistente);

                cursoRepositorio.save(cursoExistente);
                alumnoRepositorio.save(alumnoExistente);

                return new ResponseEntity<>("Alumno eliminado del curso exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
            }
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
                            //profesorExistente.setContrasena(passwordEncoder.encode((String) valor));
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

    @PatchMapping("/api/cursos/{id}")
    public ResponseEntity<Object> modificarPropiedadCurso(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        Optional<Curso> optionalCurso = cursoRepositorio.findById(id);
        if (optionalCurso.isPresent()) {
            Curso cursoExistente = optionalCurso.get();
            List<String> propiedadesModificadas = new ArrayList<>();

            cambios.forEach((propiedad, valor) -> {
                switch (propiedad) {
                    case "nombreCurso":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            cursoExistente.setNombreCurso((String) valor);
                            propiedadesModificadas.add("Nombre del curso");
                        }
                        break;
                    case "descripcion":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            cursoExistente.setDescripcion((String) valor);
                            propiedadesModificadas.add("Descripción");
                        }
                        break;
                    case "horario":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            try {
                                Horario nuevoHorario = Horario.valueOf((String) valor);
                                cursoExistente.setHorario(nuevoHorario);
                                propiedadesModificadas.add("Horario");
                            } catch (IllegalArgumentException e) {
                                new ResponseEntity<>("El valor para 'horario' no es válido", HttpStatus.BAD_REQUEST);
                            }
                        }
                        break;
                    case "presencial":
                        if (valor instanceof Boolean) {
                            cursoExistente.setPresencial((Boolean) valor);
                            propiedadesModificadas.add("Presencial");
                        }
                        break;
                    case "fechaInicio":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            try {
                                LocalDate fechaInicio = LocalDate.parse((String) valor);
                                cursoExistente.setFechaInicio(fechaInicio);
                                propiedadesModificadas.add("Fecha de inicio");
                            } catch (DateTimeParseException e) {
                                new ResponseEntity<>("El formato de fecha para 'fechaInicio' es inválido", HttpStatus.BAD_REQUEST);
                            }
                        }
                        break;
                    case "fechaFin":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            try {
                                LocalDate fechaFin = LocalDate.parse((String) valor);
                                cursoExistente.setFechaFin(fechaFin);
                                propiedadesModificadas.add("Fecha de fin");
                            } catch (DateTimeParseException e) {
                                new ResponseEntity<>("El formato de fecha para 'fechaFin' es inválido", HttpStatus.BAD_REQUEST);
                            }
                        }
                        break;
                    case "cupos":
                        if (valor instanceof Integer) {
                            cursoExistente.setCupos((int) valor);
                            propiedadesModificadas.add("Cupos");
                        }
                        break;
                    case "imagen":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            cursoExistente.setImagen((String) valor);
                            propiedadesModificadas.add("Imagen");
                        }
                        break;
                    case "categoria":
                        if (valor instanceof String && !((String) valor).isBlank()) {
                            cursoExistente.setCategoria((String) valor);
                            propiedadesModificadas.add("Categoria");
                        }
                        break;
                    case "materias":
                        if (valor instanceof List<?>) {
                            List<String> nuevasMaterias = (List<String>) valor;
                            for (String materia : nuevasMaterias) {
                                if (!cursoExistente.getMaterias().contains(materia)) {
                                    cursoExistente.getMaterias().add(materia);
                                    propiedadesModificadas.add("Materia agregada: " + materia);
                                }
                            }
                        }
                        break;
                    case "materiasEliminar":
                        if (valor instanceof List<?>) {
                            List<String> materiasEliminar = (List<String>) valor;
                            List<String> materiasExistentes = cursoExistente.getMaterias();
                            for (String materia : materiasEliminar) {
                                if (materiasExistentes.contains(materia)) {
                                    materiasExistentes.remove(materia);
                                    propiedadesModificadas.add("Materia eliminada: " + materia);
                                } else {
                                    materiasExistentes.add(materia);
                                }
                            }
                        }
                        break;
                    default:
                        ResponseEntity.badRequest().body("Propiedad no válida: " + propiedad);
                }
            });

            if (propiedadesModificadas.isEmpty()) {
                return new ResponseEntity<>("No se encontraron propiedades para modificar", HttpStatus.BAD_REQUEST);
            }

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Modificado correctamente: ");
            mensaje.append(String.join(", ", propiedadesModificadas));
            cursoRepositorio.save(cursoExistente);
            return new ResponseEntity<>(mensaje.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/profesores/{profesorId}/inscribir")
    public ResponseEntity<Object> inscribirProfesorACurso(@PathVariable Long profesorId, @RequestParam Long cursoId) {
        Optional<Profesor> optionalProfesor = profesorRepositorio.findById(profesorId);
        if (!optionalProfesor.isPresent()) {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        }

        Optional<Curso> optionalCurso = cursoRepositorio.findById(cursoId);
        if (!optionalCurso.isPresent()) {
            return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
        }

        Profesor profesor = optionalProfesor.get();
        Curso curso = optionalCurso.get();

        if (curso.getProfesor() != null && curso.getProfesor().equals(profesor)) {
            return new ResponseEntity<>("El profesor ya está inscrito en este curso", HttpStatus.BAD_REQUEST);
        }

        if (curso.getProfesor() != null) {
            return new ResponseEntity<>("El curso ya tiene asignado un profesor", HttpStatus.BAD_REQUEST);
        }

        curso.setProfesor(profesor);
        cursoRepositorio.save(curso);

        return new ResponseEntity<>("Profesor inscrito exitosamente en el curso", HttpStatus.OK);
    }

    @PatchMapping("/api/cursos/{id}/cambiar-profesor")
    public ResponseEntity<Object> cambiarProfesorCurso(@PathVariable Long id, @RequestParam Long nuevoProfesorId) {
        Optional<Curso> optionalCurso = cursoRepositorio.findById(id);
        if (optionalCurso.isPresent()) {
            Curso cursoExistente = optionalCurso.get();

            Optional<Profesor> optionalNuevoProfesor = profesorRepositorio.findById(nuevoProfesorId);
            if (optionalNuevoProfesor.isPresent()) {
                Profesor nuevoProfesor = optionalNuevoProfesor.get();

                cursoExistente.setProfesor(nuevoProfesor);
                cursoRepositorio.save(cursoExistente);

                return new ResponseEntity<>("Profesor del curso cambiado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nuevo profesor no encontrado", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/api/alumnos/inscribir")
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
            return new ResponseEntity<>("El alumno ya está inscrpito en este curso", HttpStatus.BAD_REQUEST);
        }

        alumno.inscribirCurso(curso);
        curso.setCupos(curso.getCupos() - 1);

        alumnoRepositorio.save(alumno);
        cursoRepositorio.save(curso);

        return new ResponseEntity<>("Alumno inscrito exitosamente en el curso", HttpStatus.OK);
    }
    @GetMapping("/api/current")
    public ResponseEntity<Object> getCurrent(Authentication authentication){
        return new ResponseEntity<>(authentication.getAuthorities(), HttpStatus.OK);
    }

}


