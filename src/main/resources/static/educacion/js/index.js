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
      estadoAcademico: ""
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
          let errorMessage = error.response.data;// Supongamos que el mensaje de error se encuentra en la propiedad 'message'

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
      axios.post('/api/alumnos', "nombre=" + this.nombre + "&apellido=" + this.apellido + "&email=" + this.postEmail + "&contrasena=" + this.postContrasena + "&estadoAcademico" + this.estadoAcademico, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(() => {
          axios.post('/api/login', "email=" + this.postEmail + "&contrasena=" + this.contrasena, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
            .then(() => {
              if (this.postEmail.includes("@profesor")) {
                window.location.href = "/profesor.html";
              } else if (this.postEmail.includes("@gmail")) {
                window.location.href = "/alumno.html";
              } else {
                // Redirección predeterminada en caso de otro tipo de email
                window.location.href = "/index.html";
              }
            });
        })
        .catch(error => {
          let errorMessage = error.response.data;
          errorMessage = errorMessage.replace(/\n/g, '<br>');

          Swal.fire({
            icon: 'error',
            title: 'Error',
            html: errorMessage,
            timer: 6000,
          });
        });
    }

  },
})

app.mount('#app')