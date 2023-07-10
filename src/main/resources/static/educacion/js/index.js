const { createApp } = Vue

const app = createApp({
  data() {
    return {
      email: "",
      contrasena: "",
      nombre: "",
      apellido: "",
      postEmail: "",
      postContrasena: "",
      estadoAcademico: "",
      emailProfesor:"",
      nombreProfesor:"",
      apellidoProfesor:"",
      contrasenaProfesor:"",
      
    }
  },

  methods: {
    login() {
      axios.post('/api/login',  "email=" + this.email + "&contrasena=" + this.contrasena )
        .then(() => {
          if (this.email === "admin@admin.com") {
            window.location.href = "/educacion/admin.html";
          } else if (this.email.includes("@profesor")) {
            window.location.href = "/educacion/profesor.html";
          } else if (this.email.includes("@gmail.com")) {
            window.location.href = "/educacion/alumno.html"
          }
        })
        .catch(error => {
          let errorMessage = error.response.data;

          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: errorMessage,
          });
        });
    },
    logout() {
      axios.post('/api/logout')
        .then(() => window.location.href = "/educacion/index.html")
    },

    register() {
      const alumnoData = {
        nombre: this.nombre,
        apellido: this.apellido,
        email: this.postEmail,
        contrasena: this.postContrasena,
        estadoAcademico: this.estadoAcademico

      }
      axios.post('/api/alumnos', alumnoData)
        .then(response => {
          // Mostrar mensaje de éxito con SweetAlert
          Swal.fire({
            icon: 'success',
            title: 'Registrado correctamente',
            showConfirmButton: false,
            timer: 1500
          });
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
    registerProfesor() {
      const profesorData = {
        nombre: this.nombreProfesor,
        apellido: this.apellidoProfesor,
        email: this.emailProfesor,
        contrasena: this.contraseñaProfesor,

      }
      axios.post('/api/profesores', profesorData)
        .then(response => {
          Swal.fire({
            icon: 'success',
            title: 'Profesor creado correctamente',
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
    },

  },
})

app.mount('#app')