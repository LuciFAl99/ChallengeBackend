package com.ChallengeBackend.challenge.Repositorios;

import com.ChallengeBackend.challenge.Entidades.Subclases.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AdministradorRepositorio extends JpaRepository<Administrador, Long> {
}
