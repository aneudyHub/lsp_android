package com.system.lsp.modelo;


import com.system.lsp.utilidades.UTiempo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aneudy on 8/7/2017.
 */

public class Cliente {
    private int Id;
    private String Nombre;
    private int Sexo;
    private int TipoDocumento;
    private String Telefono;
    private String Celular;
    private String Documento;
    private String FotoLocal;
    private String FotoWeb;
    private int Edad;
    private String Direccion;
    private Double Lat;
    private Double Lng;
    private String EstadoCivil;
    private int Cobrador;
    private String Updated_at;
    private int Modificado;


    public Cliente(){

    }


    public Cliente(int id, String nombre, int sexo, int tipoDocumento, String documento,String telefono,String celular, String fotoLocal, String fotoWeb, int edad, String direccion, Double lat, Double lng, String estadoCivil, int cobrador, String updated_at, int modificado) {
        Id = id;
        Nombre = nombre;
        Sexo = sexo;
        TipoDocumento = tipoDocumento;
        Documento = documento;
        Telefono = telefono;
        Celular = celular;
        FotoLocal = fotoLocal;
        FotoWeb = fotoWeb;
        Edad = edad;
        Direccion = direccion;
        Lat = lat;
        Lng = lng;
        EstadoCivil = estadoCivil;
        Cobrador = cobrador;
        Updated_at = updated_at;
        Modificado = modificado;

    }

    public Cliente(int id, String nombre, String documento,String telefono,String celular, String fotoLocal, String fotoWeb,  String direccion, Double lat, Double lng, String updated_at, int modificado) {
        Id = id;
        Nombre = nombre;
        Documento = documento;
        Telefono = telefono;
        Celular = celular;
        FotoLocal = fotoLocal;
        FotoWeb = fotoWeb;
        Direccion = direccion;
        Lat = lat;
        Lng = lng;
        Updated_at = updated_at;
        Modificado = modificado;

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getSexo() {
        return Sexo;
    }

    public void setSexo(int sexo) {
        Sexo = sexo;
    }

    public int getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getTelefono() {return Telefono;}

    public void setTelefono(String telefono) {Telefono = telefono; }

    public String getCelular() {return Celular;}

    public void setCelular(String celular) {Celular = celular;}

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String documento) {
        Documento = documento;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int edad) {
        Edad = edad;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public String getEstadoCivil() {
        return EstadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        EstadoCivil = estadoCivil;
    }

    public int getCobrador() {
        return Cobrador;
    }

    public void setCobrador(int cobrador) {
        Cobrador = cobrador;
    }

    public String getUpdated_at() {
        return Updated_at;
    }

    public void setUpdated_at(String version) {
        Updated_at = version;
    }

    public int getModificado() {
        return Modificado;
    }

    public void setModificado(int modificado) {
        Modificado = modificado;
    }

    public String getFotoLocal() {
        return FotoLocal;
    }

    public void setFotoLocal(String fotoLocal) {
        FotoLocal = fotoLocal;
    }

    public String getFotoWeb() {
        return FotoWeb;
    }

    public void setFotoWeb(String fotoWeb) {
        FotoWeb = fotoWeb;
    }


    public void aplicarSanidad(){
        this.setUpdated_at(this.getUpdated_at() == null? UTiempo.obtenerTiempo():this.getUpdated_at());
        this.setModificado(0);
    }


    public Boolean compararCon(Cliente cliente){
        return Id==cliente.Id &&
               Nombre.equals(cliente.getNombre()) &&
               Sexo==cliente.getSexo() &&
               TipoDocumento == cliente.getTipoDocumento() &&
               Documento.equals(cliente.getDocumento()) &&
                Telefono.equals(cliente.getTelefono()) &&
                Celular.equals(cliente.getCelular()) &&
               FotoLocal.equals(cliente.getFotoLocal()) &&
               FotoWeb.equals(cliente.getFotoWeb()) &&
               Edad==cliente.getEdad() &&
               Direccion.equals(cliente.getDireccion()) &&
               Lat==cliente.getLat() &&
               Lng==cliente.getLng() &&
               EstadoCivil.equals(cliente.getEstadoCivil()) &&
               Cobrador==cliente.getCobrador();
    }

    public int esMasReciente(Cliente match){
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date fechaA = formato.parse(this.getUpdated_at());
            Date fechaB = formato.parse(match.getUpdated_at());

            return fechaA.compareTo(fechaB);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
