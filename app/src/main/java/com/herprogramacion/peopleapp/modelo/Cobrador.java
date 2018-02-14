package com.herprogramacion.peopleapp.modelo;

/**
 * Created by aneudy on 8/7/2017.
 */

public class Cobrador {
    private int Id;
    private String UserName;
    private String Nombre;
    private String Zona;
    private String Token;
    private String Email;

    public Cobrador() {
    }

    public Cobrador(int id, String userName, String nombre, String zona, String token, String email) {
        Id = id;
        UserName = userName;
        Nombre = nombre;
        Zona = zona;
        Token = token;
        Email = email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getZona() {
        return Zona;
    }

    public void setZona(String zona) {
        Zona = zona;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
