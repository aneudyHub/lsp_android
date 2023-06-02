package com.system.lsp.modelo;

import com.system.lsp.utilidades.UTiempo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aneudy on 8/7/2017.
 */

public class CuotaPaga {

    private int Id;
    private int CobradorId;
    private String nombreCobrador;
    private String nombreCliente;
    private String cadenaString;
    private String fechaConsulta;
    private String Fecha;
    private int PrestamoId;
    private double Monto;
    private double TotalMora;
    private String Updated_at;
    private int Modificado;


    public CuotaPaga() {
    }

    public CuotaPaga(int id, int cobradorId, String nombreCobrador, String nombreCliente, String cadenaString, String fechaConsulta, String fecha, int prestamoId, double monto,Double totalMora,String updated_at, int modificado) {
        Id = id;
        CobradorId = cobradorId;
        this.nombreCobrador = nombreCobrador;
        this.nombreCliente = nombreCliente;
        this.cadenaString = cadenaString;
        this.fechaConsulta = fechaConsulta;
        Fecha = fecha;
        PrestamoId = prestamoId;
        Monto = monto;
        TotalMora = totalMora;
        Updated_at = updated_at;
        Modificado = modificado;
    }

    public CuotaPaga(int id, int cobradorId, String fecha, int prestamoId, double monto, int modificado) {

        Id = id;
        CobradorId = cobradorId;
        Fecha = fecha;
        PrestamoId = prestamoId;
        Modificado = modificado;
        Monto=monto;
    }

    public String getUpdated_at() {return Updated_at;}

    public void setUpdated_at(String updated_at) {Updated_at = updated_at;}

    public double getMonto() {
        return Monto;
    }

    public void setMonto(double monto) {
        Monto = monto;
    }

    public double getTotalMora() {return TotalMora;}

    public void setTotalMora(double totalMora) {
        TotalMora = totalMora;
    }

    public int getId() {return Id;}

    public void setId(int id) {Id = id;}

    public int getCobradorId() {
        return CobradorId;
    }

    public void setCobradorId(int cobradorId) {
        CobradorId = cobradorId;
    }


    public String getNombreCobrador() {
        return nombreCobrador;
    }

    public void setNombreCobrador(String nombreCobrador) {
        this.nombreCobrador = nombreCobrador;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCadenaString() {
        return cadenaString;
    }

    public void setCadenaString(String cadenaString) {
        this.cadenaString = cadenaString;
    }

    public String getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(String fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public int getPrestamoId() {
        return PrestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        PrestamoId = prestamoId;
    }

    public int getModificado() {
        return Modificado;
    }

    public void setModificado(int modificado) {
        Modificado = modificado;
    }

    public Boolean compararCon(CuotaPaga cuotaPaga){
        return  Id == cuotaPaga.getId()&&
                CobradorId == cuotaPaga.getCobradorId() &&
                Fecha.equals(cuotaPaga.getFecha()) &&
                PrestamoId == cuotaPaga.getPrestamoId() &&
                Monto == cuotaPaga.getMonto();


    }

    public void aplicarSanidad(){
        this.setUpdated_at(this.getUpdated_at() == null? UTiempo.obtenerTiempo():this.getUpdated_at());
        this.setModificado(0);
    }


    public int esMasReciente(CuotaPaga match){
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
