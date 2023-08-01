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
import com.ChallengeBackend.challenge.Servicios.AdminServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
public class AdministradorControlador {
    @Autowired
    AdminServicio adminServicio;

    @GetMapping("/api/alumnos")
    public List<AlumnoDto> traerAlumnos() {
        return adminServicio.traerAlumnos();
    }

    @GetMapping("/api/profesores")
    public List<ProfesorDto> traerProfesores() {
        return adminServicio.traerProfesores();
    }

    @GetMapping("/api/cursos")
    public List<CursoDto> traerCursos() {
        return adminServicio.traerCursos();
    }

    @PostMapping("/api/cursos")
    public ResponseEntity<Object> crearCursos(@RequestBody Curso curso) {
      return adminServicio.crearCurso(curso);
    }

    @DeleteMapping("/api/profesores/{id}")
    @Transactional
    public ResponseEntity<Object> eliminarProfesor(@PathVariable Long id) {
        return adminServicio.eliminarProfesor(id);
    }


    @DeleteMapping("/api/alumnos/{id}")
    public ResponseEntity<Object> eliminarAlumno(@PathVariable Long id) {
        return adminServicio.eliminarAlumno(id);
    }

    @DeleteMapping("/api/cursos/{id}")
    public ResponseEntity<Object> eliminarCurso(@PathVariable Long id) {
       return adminServicio.eliminarCurso(id);
    }
    @DeleteMapping("/api/cursos/{id}/materias/{materia}")
    public ResponseEntity<Object> eliminarMateriaDelCurso(@PathVariable Long id, @PathVariable String materia) {
        return adminServicio.eliminarMateriaDelCurso(id, materia);
    }
    @DeleteMapping("/api/cursos/{id}/eliminar-alumno/{alumnoId}")
    public ResponseEntity<Object> eliminarAlumnoCurso(@PathVariable Long id, @PathVariable Long alumnoId) {
        return adminServicio.eliminarAlumnoCurso(id, alumnoId);
    }


    @PatchMapping("/api/profesores/{id}")
    public ResponseEntity<Object> modificarPropiedadProfesor(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return adminServicio.modificarPropiedadProfesor(id, cambios);
    }

    @PatchMapping("/api/alumnos/{id}")
    public ResponseEntity<Object> modificarPropiedadAlumno(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return adminServicio.modificarPropiedadAlumno(id, cambios);
    }

    @PatchMapping("/api/cursos/{id}")
    public ResponseEntity<Object> modificarPropiedadCurso(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
       return adminServicio.modificarPropiedadCurso(id, cambios);
    }

    @PostMapping("/api/profesores/{profesorId}/inscribir")
    public ResponseEntity<Object> inscribirProfesorACurso(@PathVariable Long profesorId, @RequestParam Long cursoId) {
       return adminServicio.inscribirProfesorACurso(profesorId, cursoId);
    }

    @PatchMapping("/api/cursos/{id}/cambiar-profesor")
    public ResponseEntity<Object> cambiarProfesorCurso(@PathVariable Long id, @RequestParam Long nuevoProfesorId) {
       return adminServicio.cambiarProfesorCurso(id, nuevoProfesorId);
    }
    @PostMapping("/api/alumnos/inscribir")
    public ResponseEntity<Object> inscribirAlumnoACurso(@RequestParam Long alumnoId, @RequestParam Long cursoId) {
        return adminServicio.inscribirAlumnoACurso(alumnoId, cursoId);
    }

}


