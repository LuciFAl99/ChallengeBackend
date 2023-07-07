package com.ChallengeBackend.challenge.Repositorios;

import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PersonaRepositorio extends JpaRepository<Persona, Long> {
    Persona findByEmail(String email);
}
