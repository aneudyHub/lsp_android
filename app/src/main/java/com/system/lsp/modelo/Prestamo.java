package com.system.lsp.modelo;

import com.system.lsp.utilidades.UTiempo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aneudy on 8/7/2017.
 */

public class Prestamo {
    private int Id;
    private int Clientes_Id;
    private String nombreCliente;
    private String documentoCliente;
    private double balance;
    private double interes;
    private double mora;
    private double Capital;
    private float PorcientoInteres;
    private float PorcientoMora;
    private String Plazo;
    private int Cuotas;
    private String FechaInicio;
    private String FechaSaldo;
    private int Activo;
    private int Saldado;
    private String Updated_at;
    private int Modificado;
    private List<PrestamoDetalle> Detalle;

    public Prestamo() {
    }

    public Prestamo(int id, int clientes_Id, double capital, float porcientoInteres, float porcientoMora, String plazo, int cuotas, String fechaInicio, String fechaSaldo, int activo, int saldado, String updated_at, int modificado) {
        Id = id;
        Clientes_Id = clientes_Id;
        Capital = capital;
        PorcientoInteres = porcientoInteres;
        PorcientoMora = porcientoMora;
        Plazo = plazo;
        Cuotas = cuotas;
        FechaInicio = fechaInicio;
        FechaSaldo = fechaSaldo;
        Activo = activo;
        Saldado = saldado;
        Updated_at = updated_at;
        Modificado = modificado;

    }


    public Prestamo(String nombreCliente, String documentoCliente, double balance, double interes, double mora, double capital, int cuotas, String fechaInicio) {
        this.nombreCliente = nombreCliente;
        this.documentoCliente = documentoCliente;
        this.balance = balance;
        this.interes = interes;
        this.mora = mora;
        Capital = capital;
        Cuotas = cuotas;
        FechaInicio = fechaInicio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getMora() {
        return mora;
    }

    public void setMora(double mora) {
        this.mora = mora;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getClientes_Id() {
        return Clientes_Id;
    }

    public void setClientes_Id(int clientes_Id) {
        Clientes_Id = clientes_Id;
    }

    public double getCapital() {
        return Capital;
    }

    public void setCapital(double capital) {
        Capital = capital;
    }

    public float getPorcientoInteres() {
        return PorcientoInteres;
    }

    public void setPorcientoInteres(float porcientoInteres) {
        PorcientoInteres = porcientoInteres;
    }

    public float getPorcientoMora() {
        return PorcientoMora;
    }

    public void setPorcientoMora(float porcientoMora) {
        PorcientoMora = porcientoMora;
    }

    public String getPlazo() {
        return Plazo;
    }

    public void setPlazo(String plazo) {
        Plazo = plazo;
    }

    public int getCuotas() {
        return Cuotas;
    }

    public void setCuotas(int cuotas) {
        Cuotas = cuotas;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public String getFechaSaldo() {
        return FechaSaldo;
    }

    public void setFechaSaldo(String fechaSaldo) {
        FechaSaldo = fechaSaldo;
    }

    public int getActivo() {
        return Activo;
    }

    public void setActivo(int activo) {
        Activo = activo;
    }

    public int getSaldado() {
        return Saldado;
    }

    public void setSaldado(int saldado) {
        Saldado = saldado;
    }

    public List<PrestamoDetalle> getDetalle() {
        return Detalle;
    }

    public void setDetalle(List<PrestamoDetalle> detalle) {
        Detalle = detalle;
    }

    public void aplicarSanidad(){
        this.setUpdated_at(this.getUpdated_at() == null? UTiempo.obtenerTiempo():this.getUpdated_at());
        this.setModificado(0);
    }

    public Boolean compararCon(Prestamo prestamo){
        return Id==prestamo.getId() &&
        Clientes_Id ==prestamo.getClientes_Id() &&
        Capital ==prestamo.getCapital() &&
        PorcientoInteres == prestamo.getPorcientoInteres() &&
        PorcientoMora == prestamo.getPorcientoMora() &&
        Plazo.equals(prestamo.getPlazo()) &&
        Cuotas == prestamo.getCuotas() &&
        FechaInicio.equals(prestamo.getFechaInicio()) &&
        FechaSaldo.equals(prestamo.getFechaSaldo()) &&
        Activo ==prestamo.getActivo() &&
        Saldado ==prestamo.getSaldado();

    }

    public int esMasReciente(Prestamo match){
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


}
