package com.ChallengeBackend.challenge.Configuraciones;

import com.ChallengeBackend.challenge.Entidades.Subclases.Administrador;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import com.ChallengeBackend.challenge.Repositorios.PersonaRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;

@Configuration
public class Autenticacion extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            Persona persona = personaRepositorio.findByEmail(email);

                    if(persona!=null){
                        Profesor profesor = persona instanceof Profesor ? ((Profesor) persona) : null;
                        Alumno alumno = persona instanceof Alumno ? ((Alumno) persona) : null;
                        Administrador administrador = persona instanceof Administrador ? ((Administrador) persona) : null;

                        if (profesor!=null){
                            if(profesor.getEmail().contains("@profesor.com"))
                            return new User(profesor.getEmail(), profesor.getContrasena(),
                                    AuthorityUtils.createAuthorityList("PROFESOR"));
                        }
                        if(alumno!=null){
                            if(alumno.getEmail().contains("@gmail.com"))
                            return new User(alumno.getEmail(),alumno.getContrasena(),
                                    AuthorityUtils.createAuthorityList("ALUMNO"));
                        }
                        if(administrador!=null){
                            if(administrador.getEmail().equals("admin@admin.com"))
                            return new User(administrador.getEmail(), administrador.getContrasena(),
                                    AuthorityUtils.createAuthorityList("ADMIN"));
                        }
                    }else {
                        throw new UsernameNotFoundException("Persona inexistente" + email);
                    }

                    return null;
                }
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}



