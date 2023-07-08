package com.ChallengeBackend.challenge.Repositorios;

import com.ChallengeBackend.challenge.Entidades.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CursoRepositorio extends JpaRepository<Curso, Long> {
    Curso findByNombreCurso(String nombre);
}
