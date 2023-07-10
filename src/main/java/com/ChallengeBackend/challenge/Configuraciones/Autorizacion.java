package com.ChallengeBackend.challenge.Configuraciones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class Autorizacion {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/educacion/index.html", "/educacion/css/**", "/educacion/js/**", "/educacion/img/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login", "/api/logout").permitAll()
                .antMatchers(HttpMethod.POST, "/api/alumnos").permitAll()
                .antMatchers(HttpMethod.POST, "/api/profesores").permitAll()
                .antMatchers("/api/alumnos/actual").hasAuthority("ALUMNO")
                .antMatchers("/api/alumnos/inscribirse").hasAuthority("ALUMNO")
                .antMatchers("/api/profesores/actual").hasAuthority("PROFESOR")
                .antMatchers("/api/profesores", "/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("api/cursos/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/cursos").permitAll()
                .antMatchers("/educacion/alumno.html", "/educacion/fichaAlumno.html").hasAnyAuthority("ALUMNO", "ADMIN")
                .antMatchers("/educacion/profesor.html", "/educacion/fichaProfesor.html", "/api/modificarProfesor/{id}").hasAnyAuthority("PROFESOR", "ADMIN")
                .antMatchers("/educacion/admin.html").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/cursos", "/api/cursos/{id}/materias", "/api/profesores/{profesorId}/inscribir").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/cursos/{id}", "/api/cursos/{id}/cambiar-profesor").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/profesores/{id}").hasAnyAuthority("PROFESOR", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/alumnos/{id}", "/api/modificar/{id}").hasAnyAuthority("ALUMNO", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/profesores/{id}", "/api/alumnos/{id}", "/api/cursos/{id}", "/api/cursos/{id}/materias/{materia}", "/api/cursos/{id}/eliminar-alumno/{alumnoId}").hasAuthority("ADMIN")
                .antMatchers("/api/alumnos").hasAnyAuthority("ADMIN", "PROFESOR")
                .antMatchers(HttpMethod.POST, "/api/alumnos/inscribir").hasAnyAuthority( "ADMIN")

                .anyRequest().denyAll();


        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("contrasena")

                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }
}
