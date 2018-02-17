package com.system.lsp.modelo;

/**
 * Created by Suarez on 10/02/2018.
 */

public class HistorialPagos {

    private int id;
    private String fecha;
    private int usuario;
    private String nombreCobrador;
    private String nombreCliente;
    private double monto;
    private int prestamoID;
    private String cadena;


    public HistorialPagos(int id, String fecha, int usuario, String nombreCobrador, String nombreCliente, double monto, int prestamoID, String cadena) {
        this.id = id;
        this.fecha = fecha;
        this.usuario = usuario;
        this.nombreCobrador = nombreCobrador;
        this.nombreCliente = nombreCliente;
        this.monto = monto;
        this.prestamoID = prestamoID;
        this.cadena = cadena;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getFecha() {return fecha;}

    public void setFecha(String fecha) {this.fecha = fecha;}

    public int getUsuario() {return usuario;}

    public void setUsuario(int usuario) {this.usuario = usuario;}

    public String getNombreCobrador() {return nombreCobrador;}

    public void setNombreCobrador(String nombreCobrador) {this.nombreCobrador = nombreCobrador;}

    public String getNombreCliente() {return nombreCliente;}

    public void setNombreCliente(String nombreCliente) {this.nombreCliente = nombreCliente;}

    public double getMonto() {return monto;}

    public void setMonto(double monto) {this.monto = monto;}

    public int getPrestamoID() {return prestamoID;}

    public void setPrestamoID(int prestamoID) {this.prestamoID = prestamoID;}

    public String getCadena() {return cadena;}

    public void setCadena(String cadena) {this.cadena = cadena;}
}
