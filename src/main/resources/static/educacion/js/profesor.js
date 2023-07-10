const { createApp } = Vue

const app = createApp({
    data() {
        return {
            profesor:[],
        }
    },
    created() {
            this.traerProfesorActual();


    },
    methods: {
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/educacion/index.html")
        },
        traerProfesorActual() {
            axios.get('/api/profesores/actual')
                .then(response => {
                    this.profesor = response.data;
                    console.log(this.profesor);

                })
                .catch(error => {
                    console.log(error);
                })

        },
        ocultarContraseña(contraseña) {
            const caracteresOcultos = '*'.repeat(contraseña.length);
            return caracteresOcultos;
          },
          editarPropiedadProfesor(propiedad) {
            const profesor = this.profesor;
      
            const valorActual = profesor[propiedad];
      
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
      
                  axios.patch(`/api/modificarProfesor/${profesor.id}`, { [propiedad]: valorEnviado })
                    .then(response => {
                      console.log(response.data);
                      profesor[propiedad] = valorEnviado;
      
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