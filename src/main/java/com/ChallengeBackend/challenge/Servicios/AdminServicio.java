package com.ChallengeBackend.challenge.Servicios;

import com.ChallengeBackend.challenge.Dtos.AlumnoDto;
import com.ChallengeBackend.challenge.Dtos.CursoDto;
import com.ChallengeBackend.challenge.Dtos.ProfesorDto;
import com.ChallengeBackend.challenge.Entidades.Curso;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface AdminServicio {
    List<AlumnoDto> traerAlumnos();

    List<ProfesorDto> traerProfesores();

    List<CursoDto> traerCursos();
    ResponseEntity<Object> crearCurso(Curso curso);
    ResponseEntity<Object> eliminarProfesor(Long id);
    ResponseEntity<Object> eliminarAlumno(Long id);
    ResponseEntity<Object> eliminarCurso(Long id);
    ResponseEntity<Object> eliminarMateriaDelCurso(Long id, String materia);
    ResponseEntity<Object> eliminarAlumnoCurso(Long id, Long alumnoId);
    ResponseEntity<Object> modificarPropiedadProfesor(Long id, Map<String, Object> cambios);
    ResponseEntity<Object> modificarPropiedadAlumno(Long id, Map<String, Object> cambios);
    ResponseEntity<Object> modificarPropiedadCurso(Long id, Map<String, Object> cambios);
    ResponseEntity<Object> inscribirProfesorACurso(Long profesorId, Long cursoId);
    ResponseEntity<Object> cambiarProfesorCurso(Long id, Long nuevoProfesorId);
    ResponseEntity<Object> inscribirAlumnoACurso(Long alumnoId, Long cursoId);

}

