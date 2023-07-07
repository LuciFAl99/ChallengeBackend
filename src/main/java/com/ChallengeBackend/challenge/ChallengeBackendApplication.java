package com.ChallengeBackend.challenge;

import com.ChallengeBackend.challenge.Entidades.Curso;
import com.ChallengeBackend.challenge.Entidades.Enums.EstadoAcademico;
import com.ChallengeBackend.challenge.Entidades.Enums.Horario;
import com.ChallengeBackend.challenge.Entidades.Subclases.Administrador;
import com.ChallengeBackend.challenge.Entidades.Subclases.Alumno;
import com.ChallengeBackend.challenge.Entidades.Subclases.Profesor;
import com.ChallengeBackend.challenge.Entidades.Superclase.Persona;
import com.ChallengeBackend.challenge.Repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;



@SpringBootApplication
public class ChallengeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeBackendApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData(PersonaRepositorio personaRepositorio, AlumnoRepositorio alumnoRepositorio, ProfesorRepositorio profesorRepositorio, CursoRepositorio cursoRepositorio, AdministradorRepositorio administradorRepositorio) {
		return (args) -> {
			String descripcion1 = "Este curso te introducirá al lenguaje de programación Python, uno de los más populares y versátiles en la actualidad. Aprenderás los fundamentos de la programación, como estructuras de control, funciones y manipulación de datos. También explorarás conceptos avanzados, como la programación orientada a objetos";
			String descripcion2 = "Si estás interesado en crear sitios web visualmente atractivos y responsivos, este curso es para ti. Aprenderás HTML y CSS para la estructura y el diseño de páginas web, respectivamente. También explorarás JavaScript para agregar interactividad y dinamismo a tus sitios. Además, aprenderás sobre frameworks CSS populares, como Bootstrap, que te ayudarán a agilizar el desarrollo. Al final del curso, estarás preparado para crear interfaces web atractivas y funcionales.";
			String descripcion3 = "En este curso, te sumergirás en el emocionante mundo del desarrollo de aplicaciones móviles. Explorarás los aspectos fundamentales de la creación de aplicaciones móviles tanto para Android como para iOS. Aprenderás los lenguajes y frameworks necesarios para desarrollar aplicaciones nativas, así como las mejores prácticas de diseño de interfaces móviles. También adquirirás conocimientos sobre el acceso a datos y servicios, lo que te permitirá crear aplicaciones móviles completas y funcionales.";
			String descripcion4 = "Las bases de datos son elementos fundamentales en muchas aplicaciones y sistemas de información. En este curso, aprenderás los conceptos básicos de las bases de datos, incluyendo el diseño, la estructura y la normalización. Explorarás el lenguaje SQL para realizar consultas y actualizaciones en bases de datos relacionales. También obtendrás conocimientos sobre la administración de bases de datos y la optimización del rendimiento. Este curso te proporcionará una base sólida en el manejo de datos y te preparará para trabajar con bases de datos en aplicaciones del mundo real.";
			String descripcion5 = "Si deseas convertirte en un desarrollador web versátil y capaz de manejar tanto el front-end como el back-end, este curso es ideal para ti. Aprenderás las habilidades necesarias para crear aplicaciones web completas desde cero. En el lado del front-end, adquirirás conocimientos en HTML, CSS y JavaScript para construir interfaces atractivas y dinámicas. En el lado del back-end, explorarás lenguajes y frameworks populares como Node.js, Python Django o Ruby on Rails para crear la lógica y la funcionalidad detrás de las aplicaciones. Además, aprenderás sobre bases de datos, APIs y seguridad, obteniendo una visión completa del desarrollo de aplicaciones web.";

			Curso curso1 = new Curso("Programación en Python", descripcion1, Horario.MANANA, false, LocalDate.of(2023,07,5), LocalDate.of(2023, 11, 5), 20, "kjhu" ,new ArrayList<>(Arrays.asList("Introducción a Python", "Estructuras de control (condicionales y bucles)", "Funciones y modularidad", "Manipulación de datos (listas, diccionarios, tuplas)", "Programación orientada a objetos en Python", "Manipulación de archivos y excepciones", "Introducción a la programación web con Python (opcional)")));
			Curso curso2 = new Curso("Curso de Desarrollo Web Front-End", descripcion2, Horario.NOCHE, false, LocalDate.of(2023, 5, 12), LocalDate.of(2023, 9, 12), 25, "kjj",new ArrayList<>(Arrays.asList("HTML y CSS", "Javascript básico", "Diseño responsive y frameworks CSS ", "Manipulación del DOM", "Manipulación de eventos y manejo de formularios", "Introducción a AJAX y consumo de APIs", "Optimización y rendimiento web")));
			Curso curso3 = new Curso("Curso de Desarrollo de Aplicaciones Móviles", descripcion3, Horario.MANANA, true, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 9, 1), 30, "jkghku", new ArrayList<>(Arrays.asList("Introducción al desarrollo móvil y arquitectura de aplicaciones", "Desarrollo nativo (Android con Java/Kotlin o iOS con Swift)", "Fundamentos de diseño de interfaces para aplicaciones móviles", "Acceso a datos y almacenamiento local", "Integración de servicios y notificaciones", "Publicación de aplicaciones en tiendas (Google Play Store, App Store)")));
			Curso curso4 = new Curso("Curso de Bases de Datos", descripcion4, Horario.TARDE, true, LocalDate.of(2023, 8, 5), LocalDate.of(2024, 1, 5), 40, "jjhjkh",new ArrayList<>(Arrays.asList("Fundamentos de bases de datos (modelos, estructuras, relaciones)", "Lenguaje SQL (consultas, actualizaciones, creación de tablas y vistas)", "Diseño de bases de datos y normalización", "Administración de bases de datos (respaldo, recuperación, optimización)", "Introducción a bases de datos NoSQL (por ejemplo, MongoDB)", "Integración de bases de datos en aplicaciones")));
			Curso curso5 = new Curso("Curso de Desarrollo de Aplicaciones Web Full-Stack", descripcion5, Horario.TARDE, false, LocalDate.of(2023, 6, 8), LocalDate.of(2023, 12, 8), 15,"ioio" ,new ArrayList<>(Arrays.asList("Desarrollo de aplicaciones web front-end (HTML, CSS, JavaScript)", "Desarrollo de aplicaciones web back-end (con Java)", "Bases de datos y persistencia de datos", "APIs y servicios web", "Seguridad y autenticación en aplicaciones web", "Despliegue y administración de aplicaciones web")));

			cursoRepositorio.save(curso1);
			cursoRepositorio.save(curso2);
			cursoRepositorio.save(curso3);
			cursoRepositorio.save(curso4);
			cursoRepositorio.save(curso5);

			Alumno alumno1 = new Alumno("Lucila", "Alochis", "lucila@gmail.com", passwordEncoder.encode("Lucila123"), EstadoAcademico.GRADUADO);
			alumnoRepositorio.save(alumno1);

			Profesor profesor1 = new Profesor("Jorge", "Diaz", "Jorge@profesor.com", passwordEncoder.encode("Jorge123"), Horario.NOCHE);
			Profesor profesor2 = new Profesor("Carlos", "Lopez", "carlos@profesor.com", passwordEncoder.encode("Carlos123"), Horario.TARDE);

			profesorRepositorio.save(profesor1);
			profesorRepositorio.save(profesor2);

			profesor1.dictarCurso(curso1);
			profesor1.dictarCurso(curso2);
			profesor1.dictarCurso(curso3);
			profesor2.dictarCurso(curso4);
			profesor2.dictarCurso(curso5);

			profesorRepositorio.save(profesor1);
			profesorRepositorio.save(profesor2);

			Administrador administrador1 = new Administrador("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("admin"));
			administradorRepositorio.save(administrador1);

			Persona persona = personaRepositorio.findById(1L).orElse(null);
			Profesor profesor = persona instanceof Profesor ? ((Profesor)persona) : null;
			Alumno alumno = persona instanceof Alumno ? ((Alumno) persona) : null;
			Administrador administrador = persona instanceof  Administrador ? ((Administrador) persona) : null;

			System.out.println(profesor);
			System.out.println(alumno);
			System.out.println(administrador);

			alumno1.inscribirCurso(curso1);
			alumno1.inscribirCurso(curso2);

			cursoRepositorio.save(curso1);
			cursoRepositorio.save(curso2);
			cursoRepositorio.save(curso3);
			cursoRepositorio.save(curso4);
			cursoRepositorio.save(curso5);

			alumnoRepositorio.save(alumno1);
		};
	}
}
