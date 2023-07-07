package com.ChallengeBackend.challenge.Controladores;

import com.ChallengeBackend.challenge.Dtos.AlumnoDto;
import com.ChallengeBackend.challenge.Dtos.CursoDto;
import com.ChallengeBackend.challenge.Dtos.ProfesorDto;
import com.ChallengeBackend.challenge.Entidades.Curso;
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
    public ResponseEntity<Object>crearProfesor(@RequestBody Curso curso, @RequestParam Long profesorId){
        StringBuilder errorMessage = new StringBuilder();
        if (curso.getNombreCurso().isEmpty()){
            errorMessage.append("Nombre es requerido");
        } else if (curso.getDescripcion().isEmpty()) {
            errorMessage.append("Descripción es requerido");
        } else if (curso.getHorario() == null) {
            errorMessage.append("Horario es requerido");
        } else if (!curso.isPresencial()){
            errorMessage.append("Presencial es requerido");
        } else if (curso.getFechaInicio() == null){
            errorMessage.append("Fecha de inicio es requerido");
        } else if (curso.getFechaFin()==null){
            errorMessage.append("Fecha de fin es requerido");
        } else if (curso.getCupos() == 0 || curso.getCupos() <0){
            errorMessage.append("Número de cupos es requerido");
        }else if (curso.getImagen().isEmpty()){
            errorMessage.append("Imagen es requerido");
        }else if (curso.getMaterias().isEmpty()){
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








/*    @PatchMapping("/api/cursos/{id}")
    public ResponseEntity<Object> modificarPropiedadCurso(@PathVariable Long id, @RequestParam("propiedad") String propiedad, @RequestParam("valor") String valor) {
        // Acceder al curso existente utilizando el ID
        Curso cursoExistente = cursoRepositorio.findById(id);

        // Verificar si el curso existe
        if (cursoExistente == null) {
            return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
        }

        // Modificar la propiedad especificada
        switch (propiedad) {
            case "nombreCurso":
                cursoExistente.set(valor);
                break;
            case "descripcion":
                cursoExistente.setDescripcion(valor);
                break;
            case "horario":
                cursoExistente.setHorario(valor);
                break;
            case "presencial":
                // Aquí debes asegurarte de manejar el valor booleano correctamente, según tus necesidades
                cursoExistente.setPresencial(Boolean.parseBoolean(valor));
                break;
            case "fechaInicio":
                // Aquí debes manejar la conversión de cadena a fecha, según tu implementación
                LocalDate fechaInicio = LocalDate.parse(valor);
                cursoExistente.setFechaInicio(fechaInicio);
                break;
            // Agrega casos para las demás propiedades
            // ...
            default:
                return new ResponseEntity<>("Propiedad no válida", HttpStatus.BAD_REQUEST);
        }

        // Guardar los cambios realizados en el curso
        cursoRepositorio.save(cursoExistente);

        return new ResponseEntity<>(HttpStatus.OK);
    }*/

}


