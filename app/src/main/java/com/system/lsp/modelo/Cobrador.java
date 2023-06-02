package com.system.lsp.modelo;

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

    //DATOS COPAÑIA
    private String nombreC;
    private String direccionC;
    private String telefonoC;
    private String rncC;
    private String foot_facturaC;


    public Cobrador() {
    }

    public Cobrador(int id, String userName, String nombre, String zona, String token,
                    String email, String nombreC,String direccionC,String telefonoC, String rncC,
                    String foot_facturaC) {
        Id = id;
        UserName = userName;
        Nombre = nombre;
        Zona = zona;
        Token = token;
        Email = email;
        this.nombreC = nombreC;
        this.direccionC = direccionC;
        this.telefonoC = telefonoC;
        this.rncC = rncC;
        this.foot_facturaC = foot_facturaC;

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

    public String getNombreC() {
        return nombreC;
    }

    public void setNombreC(String nombreC) {
        this.nombreC = nombreC;
    }

    public String getDireccionC() {
        return direccionC;
    }

    public void setDireccionC(String direccionC) {
        this.direccionC = direccionC;
    }

    public String getTelefonoC() {
        return telefonoC;
    }

    public void setTelefonoC(String telefonoC) {
        this.telefonoC = telefonoC;
    }

    public String getRncC() {
        return rncC;
    }

    public void setRncC(String rncC) {
        this.rncC = rncC;
    }

    public String getFoot_facturaC() {
        return foot_facturaC;
    }

    public void setFoot_facturaC(String foot_facturaC) {
        this.foot_facturaC = foot_facturaC;
    }
}
