const { createApp } = Vue

const app = createApp({
  data() {
    return {
      alumnos: [],
      profesores: [],
      cursos: [],
      alumnos2:[],
      nombreCurso: '',
      descripcion: '',
      horario: '',
      presencial: false,
      fechaInicio: '',
      fechaFin: '',
      cupos: 0,
      imagen: '',
      categoria: '',
      materias: [],
      nombre: '',
      apellido: '',
      email: '',
      contraseña: '',
      estadoAcademico: '',
      nombreProfesor: '',
      apellidoProfesor: '',
      emailProfesor: '',
      contraseñaProfesor: '',
      profesorId: '',
      cursoId: '',
      mensaje: '',
      alumnoId: '',
      busqueda: '',
      letraBuscada:'',
      busquedaProfesor:'',
      busquedaCurso:'',
      cursoId: '',
      nuevoProfesorId: ''
    }
  },
  created() {
    this.traerAlumnos(),
      this.traerProfesores(),
      this.traerCursos()

  },
  methods: {
    traerAlumnos() {
      axios.get('/api/alumnos')
        .then(response => {
          this.alumnos = response.data;
          console.log(this.alumnos);
        })
        .catch(error => {
          console.error(error);
        });
    },   
    traerProfesores() {
      axios.get('/api/profesores')
        .then(response => {
          this.profesores = response.data;
          console.log(this.profesores);
        })
        .catch(error => {
          console.error(error);
        });
    },
    traerCursos() {
      axios.get('/api/cursos')
        .then(response => {
          this.cursos = response.data;
          console.log(this.cursos);

        })
        .catch(error => {
          console.log(error);
        })

    },
    logout() {
      axios.post('/api/logout')
        .then(() => {
          window.location.href = "/educacion/index.html";
        })
        .catch(error => {
          console.error(error);
        });
    },
    crearCurso() {
      const materias = this.materias.split(",").map(materia => materia.trim());
      const cursoData = {
        nombreCurso: this.nombreCurso,
        descripcion: this.descripcion,
        horario: this.horario,
        presencial: this.presencial,
        fechaInicio: this.fechaInicio,
        fechaFin: this.fechaFin,
        cupos: this.cupos,
        imagen: this.imagen,
        categoria: this.categoria,
        materias: materias
      };
      axios.post('/api/cursos', cursoData)
        .then(response => {
          // Mostrar mensaje de éxito con SweetAlert
          Swal.fire({
            icon: 'success',
            title: 'Curso creado correctamente',
            showConfirmButton: false,
            timer: 1500
          });

          // Reiniciar el formulario
          this.nuevoCurso = {
            nombreCurso: '',
            descripcion: '',
            horario: '',
            presencial: false,
            fechaInicio: '',
            fechaFin: '',
            cupos: 0,
            imagen: '',
            categoria: '',
            materias: [],
            busqueda: '',
            alumnosFiltrados:[]
          };
          this.traerCursos();
        })
        .catch(error => {
          let errorMessage = error.response.data;// Supongamos que el mensaje de error se encuentra en la propiedad 'message'

          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage.replace(/\n/g, '<br>'),
          });

        });
    },
    crearAlumno() {
      const alumnoData = {
        nombre: this.nombre,
        apellido: this.apellido,
        email: this.email,
        contrasena: this.contraseña,
        estadoAcademico: this.estadoAcademico

      }
      axios.post('/api/alumnos', alumnoData)
        .then(response => {
          // Mostrar mensaje de éxito con SweetAlert
          Swal.fire({
            icon: 'success',
            title: 'Alumno creado correctamente',
            showConfirmButton: false,
            timer: 1500
          });

          // Reiniciar el formulario
          this.nuevoCurso = {
            nombre: '',
            apellido: '',
            email: '',
            contrasena: '',
            estadoAcademico: '',

          };
          this.traerAlumnos();
        })
        .catch(error => {
          let errorMessage = error.response.data;// Supongamos que el mensaje de error se encuentra en la propiedad 'message'

          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage.replace(/\n/g, '<br>'),
          });
        });
    },
    crearProfesor() {
      const profesorData = {
        nombre: this.nombreProfesor,
        apellido: this.apellidoProfesor,
        email: this.emailProfesor,
        contrasena: this.contraseñaProfesor,

      }
      axios.post('/api/profesores', profesorData)
        .then(response => {
          // Mostrar mensaje de éxito con SweetAlert
          Swal.fire({
            icon: 'success',
            title: 'Profesor creado correctamente',
            showConfirmButton: false,
            timer: 1500
          });

          // Reiniciar el formulario
          this.nuevoCurso = {
            nombre: '',
            apellido: '',
            email: '',
            contrasena: '',

          };
          this.traerProfesores();
        })
        .catch(error => {
          let errorMessage = error.response.data;// Supongamos que el mensaje de error se encuentra en la propiedad 'message'

          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage.replace(/\n/g, '<br>'),
          });
        });
    },
    eliminarProfesor(profesorId) {
      Swal.fire({
        icon: 'question',
        title: 'Confirmación',
        text: '¿Estás seguro de eliminar al profesor?',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'Cancelar',
      }).then((result) => {
        if (result.isConfirmed) {
          axios.delete(`/api/profesores/${profesorId}`)
            .then(response => {
              console.log(response.data);
              this.profesores = this.profesores.filter(profesor => profesor.id !== profesorId);
            })
          Swal.fire({
            icon: 'success',
            title: 'Profesor eliminado correctamente',
            showConfirmButton: false,
            timer: 1500
          })
            .catch(error => {
              let errorMessage = error.response.data;
              Swal.fire({
                icon: 'error',
                title: 'Error',
                html: errorMessage.replace(/\n/g, '<br>'),
              });
            });
        }
      });
    },
    eliminarAlumno(alumnoId) {
      Swal.fire({
        icon: 'question',
        title: 'Confirmación',
        text: '¿Estás seguro de eliminar al alumno?',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'Cancelar',
      }).then((result) => {
        if (result.isConfirmed) {
          axios.delete(`/api/alumnos/${alumnoId}`)
            .then(response => {
              console.log(response.data);
              this.alumnos = this.alumnos.filter(alumno => alumno.id !== alumnoId);
            })
          Swal.fire({
            icon: 'success',
            title: 'Alumno eliminado correctamente',
            showConfirmButton: false,
            timer: 1500
          })
            .catch(error => {
              let errorMessage = error.response.data;
              Swal.fire({
                icon: 'error',
                title: 'Error',
                html: errorMessage.replace(/\n/g, '<br>'),
              });
            });
        }
      });
    },
    eliminarCurso(cursoId) {
      Swal.fire({
        icon: 'question',
        title: 'Confirmación',
        text: '¿Estás seguro de eliminar el curso?',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'Cancelar',
      }).then((result) => {
        if (result.isConfirmed) {
          axios.delete(`/api/cursos/${cursoId}`)
            .then(response => {
              console.log(response.data);
              this.cursos = this.cursos.filter(curso => curso.id !== cursoId);
            })
          Swal.fire({
            icon: 'success',
            title: 'Curso eliminado correctamente',
            showConfirmButton: false,
            timer: 1500
          })
            .catch(error => {
              let errorMessage = error.response.data;
              Swal.fire({
                icon: 'error',
                title: 'Error',
                html: errorMessage.replace(/\n/g, '<br>'),
              });
            });
        }
      });
    },
    editarPropiedadCurso(cursoId, propiedad) {
      const curso = this.cursos.find(curso => curso.id === cursoId);

      if (curso) {
        const valorActual = curso[propiedad];

        let nuevoValor;
        if (propiedad === 'materias') {
          nuevoValor = prompt(`Editar ${propiedad} (separadas por comas):`, valorActual.join(','));
        } else {
          nuevoValor = prompt(`Editar ${propiedad}:`, valorActual);
        }

        if (nuevoValor !== null) {
          Swal.fire({
            icon: 'question',
            title: 'Confirmación',
            text: `¿Estás seguro de editar la propiedad ${propiedad}?`,
            showCancelButton: true,
            confirmButtonText: 'Sí',
            cancelButtonText: 'No'
          }).then(result => {
            if (result.isConfirmed) {
              let valorEnviado;
              if (propiedad === 'materias') {
                valorEnviado = nuevoValor.split(',').map(materia => materia.trim());
              } else {
                valorEnviado = nuevoValor;
              }

              axios.patch(`/api/cursos/${cursoId}`, { [propiedad]: valorEnviado })
                .then(response => {
                  console.log(response.data);
                  curso[propiedad] = valorEnviado;

                  // Mostrar mensaje de éxito
                  Swal.fire({
                    icon: 'success',
                    title: 'Modificado correctamente',
                    showConfirmButton: false,
                    timer: 1500
                  });
                })
                .catch(error => {
                  let errorMessage = error.response.data;
                  Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    html: errorMessage.replace(/\n/g, '<br>'),
                  });
                });
            }
          });
        }
      }
    },
    editarPropiedadAlumno(alumnoId, propiedad) {
      const alumno = this.alumnos.find(alumno => alumno.id === alumnoId);

      if (alumno) {
        const valorActual = alumno[propiedad];

        const nuevoValor = prompt(`Editar ${propiedad}:`, valorActual);

        if (nuevoValor !== null) {
          Swal.fire({
            icon: 'question',
            title: 'Confirmación',
            text: `¿Estás seguro de editar la propiedad ${propiedad}?`,
            showCancelButton: true,
            confirmButtonText: 'Sí',
            cancelButtonText: 'No'
          }).then(result => {
            if (result.isConfirmed) {
              const valorEnviado = nuevoValor.trim();

              axios.patch(`/api/alumnos/${alumnoId}`, { [propiedad]: valorEnviado })
                .then(response => {
                  console.log(response.data);
                  alumno[propiedad] = valorEnviado;

                  Swal.fire({
                    icon: 'success',
                    title: 'Modificado correctamente',
                    showConfirmButton: false,
                    timer: 1500
                  });
                })
                .catch(error => {
                  let errorMessage = error.response.data;
                  Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    html: errorMessage.replace(/\n/g, '<br>'),
                  });
                });
            }
          });
        }
      }
    },
    editarPropiedadProfesor(profesorId, propiedad) {
      const profesor = this.profesores.find(profesor => profesor.id === profesorId);

      if (profesor) {
        const valorActual = profesor[propiedad];

        const nuevoValor = prompt(`Editar ${propiedad}:`, valorActual);

        if (nuevoValor !== null) {
          Swal.fire({
            icon: 'question',
            title: 'Confirmación',
            text: `¿Estás seguro de editar la propiedad ${propiedad}?`,
            showCancelButton: true,
            confirmButtonText: 'Sí',
            cancelButtonText: 'No'
          }).then(result => {
            if (result.isConfirmed) {
              const valorEnviado = nuevoValor.trim();

              axios.patch(`/api/profesores/${profesorId}`, { [propiedad]: valorEnviado })
                .then(response => {
                  console.log(response.data);
                  profesor[propiedad] = valorEnviado;

                  Swal.fire({
                    icon: 'success',
                    title: 'Modificado correctamente',
                    showConfirmButton: false,
                    timer: 1500
                  });
                })
                .catch(error => {
                  let errorMessage = error.response.data;
                  Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    html: errorMessage.replace(/\n/g, '<br>'),
                  });
                });
            }
          });
        }
      }
    },
    editarMateria(cursoId, materia) {
      const curso = this.cursos.find(curso => curso.id === cursoId);

      if (curso) {
        const index = curso.materias.indexOf(materia);

        if (index !== -1) {
          const valorActual = curso.materias[index];

          const nuevoValor = prompt(`Editar materia:`, valorActual);

          if (nuevoValor !== null) {
            Swal.fire({
              icon: 'question',
              title: 'Confirmación',
              text: `¿Estás seguro de editar la materia?`,
              showCancelButton: true,
              confirmButtonText: 'Sí',
              cancelButtonText: 'No'
            }).then(result => {
              if (result.isConfirmed) {
                curso.materias[index] = nuevoValor;

                axios.patch(`/api/cursos/${cursoId}`, { materias: curso.materias })
                  .then(response => {
                    console.log(response.data);

                    // Mostrar mensaje de éxito
                    Swal.fire({
                      icon: 'success',
                      title: 'Modificado correctamente',
                      showConfirmButton: false,
                      timer: 1500
                    });
                  })
                  .catch(error => {
                    let errorMessage = error.response.data;
                    Swal.fire({
                      icon: 'error',
                      title: 'Error',
                      html: errorMessage.replace(/\n/g, '<br>'),
                    });
                  });
              }
            });
          }
        }
      }
    },
    eliminarMateria(cursoId, materia) {
      Swal.fire({
        icon: 'warning',
        title: 'Confirmación',
        text: '¿Estás seguro de eliminar la materia?',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'No'
      }).then(result => {
        if (result.isConfirmed) {
          axios.delete(`/api/cursos/${cursoId}/materias/${materia}`)
            .then(response => {
              console.log(response.data);

              // Mostrar mensaje de éxito
              Swal.fire({
                icon: 'success',
                title: 'Materia eliminada correctamente',
                showConfirmButton: false,
                timer: 1500
              });

              this.traerCursos();
            })
            .catch(error => {
              console.error(error);

              Swal.fire({
                icon: 'error',
                title: 'Error al eliminar la materia',
                showConfirmButton: false,
                timer: 1500
              });
            });
        }
      });
    },
    inscribirProfesor() {
      axios.post(`/api/profesores/${this.profesorId}/inscribir?cursoId=${this.cursoId}`)
        .then(response => {
          this.mensaje = response.data;
          Swal.fire({
            icon: 'success',
            title: 'Profesor asignado correctamente',
            showConfirmButton: false,
            timer: 1500
          });
          this.traerProfesores();
        })
        .catch(error => {
          let errorMessage = error.response.data;
          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage.replace(/\n/g, '<br>'),
          });
        });
    },
    inscribirAlumno() {
      axios.post(`/api/alumnos/inscribir?alumnoId=${this.alumnoId}&cursoId=${this.cursoId}`)
        .then(response => {
          this.mensaje = response.data;
          Swal.fire({
            icon: 'success',
            title: 'Alumno inscripto correctamente',
            showConfirmButton: false,
            timer: 1500
          });
          this.traerAlumnos();
          this.traerCursos();
        })
        .catch(error => {
          let errorMessage = error.response.data;
          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage.replace(/\n/g, '<br>'),
          });
        });
    },
    filtrarAlumnos() {
      const textoBusqueda = this.busqueda.toLowerCase().trim();
      if (textoBusqueda === '') {
        return this.alumnos;
      } else {
        return this.alumnos.filter(alumno => {
          return (
            alumno.nombre.toLowerCase().includes(textoBusqueda) ||
            alumno.apellido.toLowerCase().includes(textoBusqueda) ||
            alumno.email.toLowerCase().includes(textoBusqueda)
          );
        });
      }
    },
    filtrarProfesores() {
      const textoBusqueda = this.busquedaProfesor.toLowerCase().trim();
      if (textoBusqueda === '') {
        return this.profesores;
      } else {
        return this.profesores.filter(profesor => {
          return (
            profesor.nombre.toLowerCase().includes(textoBusqueda) ||
            profesor.apellido.toLowerCase().includes(textoBusqueda) ||
            profesor.email.toLowerCase().includes(textoBusqueda)
          );
        });
      }
    },
    filtrarCursos() {
      const textoBusqueda = this.busquedaCurso.toLowerCase().trim();
      if (textoBusqueda === '') {
        return this.cursos;
      } else {
        return this.cursos.filter(curso => {
          return (
            curso.nombreCurso.toLowerCase().includes(textoBusqueda) ||
            curso.descripcion.toLowerCase().includes(textoBusqueda) ||
            curso.horario.toLowerCase().includes(textoBusqueda) ||
            curso.categoria.toLowerCase().includes(textoBusqueda)
          );
        });
      }
    },
    cambiarProfesor() {
      const formData = new FormData();
      formData.append('nuevoProfesorId', this.nuevoProfesorId);

      axios.patch(`/api/cursos/${this.cursoId}/cambiar-profesor`, formData)
        .then(response => {
          Swal.fire({
            icon: 'success',
            title: 'Profesor cambiado correctamente',
            showConfirmButton: false,
            timer: 1500
          });
          this.traerProfesores()
        })
        .catch(error => {
          let errorMessage = error.response.data;
          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage.replace(/\n/g, '<br>'),
          });
        });
    },
    eliminarAlumno(cursoId, alumnoId) {
      axios.delete(`/api/cursos/${cursoId}/eliminar-alumno/${alumnoId}`)
      Swal.fire({
        icon: 'warning',
        title: 'Confirmación',
        text: '¿Estás seguro de eliminar al alumno del curso?',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'No'
      }).then(response => {
          Swal.fire({
            icon: 'success',
            title: 'Alumno eliminado del curso correctamente',
            showConfirmButton: false,
            timer: 1500
          });
          this.traerAlumnos();
        })
        .catch(error => {
          let errorMessage = error.response.data;
          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage.replace(/\n/g, '<br>'),
          });
        });
    }    
  
  }
  
  
})

app.mount('#app')