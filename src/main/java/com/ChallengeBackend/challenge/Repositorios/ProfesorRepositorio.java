package com.ChallengeBackend.challenge.Repositorios;

import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProfesorRepositorio extends JpaRepository<Profesor, Long> {
    Profesor findByEmail(String email);
}
