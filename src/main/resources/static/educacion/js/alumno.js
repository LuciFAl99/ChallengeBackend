const { createApp } = Vue

const app = createApp({
    data() {
        return {
            cursos: [],
            alumno:[],
            busqueda:''
        }
    },
    created() {
            this.traerCursos()
            this.traerAlumnoActual();
            this.filtrarCursos();


    },
    methods: {
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/educacion/index.html")
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
        traerAlumnoActual() {
            axios.get('/api/alumnos/actual')
                .then(response => {
                    this.alumno = response.data;
                    console.log(this.alumno);

                })
                .catch(error => {
                    console.log(error);
                })

        },
        inscribirse(cursoId) {
            Swal.fire({
              icon: 'warning',
              title: 'Confirmación',
              text: '¿Estás seguro de inscribirte a este curso?',
              showCancelButton: true,
              confirmButtonText: 'Sí',
              cancelButtonText: 'No'
            }).then(response => {
              if (response.isConfirmed) {
                axios.post(`/api/alumnos/inscribirse?cursoId=${cursoId}`)
                  .then(response => {
                    const message = response.data;
                    Swal.fire({
                      icon: 'success',
                      title: 'Alumno inscrito correctamente',
                      text: message,
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
          },       
          filtrarCursos() {
            if (this.busqueda === '') {
              this.cursosFiltrados = this.cursos;
            } else {
              const letra = this.busqueda.toLowerCase();
              this.cursosFiltrados = this.cursos.filter(curso => {
                const nombreCurso = curso.nombreCurso.toLowerCase();
                return nombreCurso.includes(letra);
              });
            }
          },   
          ocultarContraseña(contraseña) {
            const caracteresOcultos = '*'.repeat(contraseña.length);
            return caracteresOcultos;
          },
          editarPropiedadAlumno(propiedad) {
            const alumno = this.alumno;
      
            const valorActual = alumno[propiedad];
      
            const nuevoValor = prompt(`Editar ${propiedad}:`, valorActual);
      
            if (nuevoValor !== null) {
              Swal.fire({
                icon: 'question',
                title: 'Confirmación',
                text: `¿Estás seguro de editar la propiedad ${propiedad}?`,
                showCancelButton: true,
                confirmButtonText: 'Sí',
                cancelButtonText: 'No',
              }).then(result => {
                if (result.isConfirmed) {
                  const valorEnviado = nuevoValor.trim();
      
                  axios.patch(`/api/modificar/${alumno.id}`, { [propiedad]: valorEnviado })
                    .then(response => {
                      console.log(response.data);
                      alumno[propiedad] = valorEnviado;
      
                      Swal.fire({
                        icon: 'success',
                        title: 'Modificado correctamente',
                        showConfirmButton: false,
                        timer: 1500,
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
          },
        
               
       
    },

})

app.mount('#app')