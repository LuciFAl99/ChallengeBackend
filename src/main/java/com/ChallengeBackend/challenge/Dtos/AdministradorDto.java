package com.ChallengeBackend.challenge.Dtos;


import com.ChallengeBackend.challenge.Entidades.Subclases.Administrador;

public class AdministradorDto {
    private Long id;
    private String nombre, apellido, email, contrasena;

    public AdministradorDto(){}
    public AdministradorDto(Administrador administrador){
        this.id = administrador.getId();
        this.nombre = administrador.getNombre();
        this.apellido = administrador.getApellido();
        this.email = administrador.getEmail();
        this.contrasena = administrador.getContrasena();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }
}