package com.ChallengeBackend.challenge.Repositorios;

import com.ChallengeBackend.challenge.Dtos.AlumnoDto;
import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AlumnoRepositorio extends JpaRepository<Alumno, Long> {
    Alumno findByEmail(String email);


}
