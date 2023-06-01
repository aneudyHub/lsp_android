package com.system.lsp.modelo;

/**
 * Created by Mrcos Suarez on 1/9/2018.
 */

public class DatosCliente {

    private String CUOTA ;
    private String CUOTA_ID;
    private String CLIENTE ;
    private String CEDULA ;
    private String FECHA;
    private String DIRECCION;
    private String TELEFONO;
    private String CELULAR;
    private String VALOR;
    private String TOTAL;
    private String Total_cuota;
    private String MONTO_PAGADO;
    private String PRESTAMO;


    public DatosCliente(String CUOTA, String CUOTA_ID, String CLIENTE,String CEDULA, String FECHA, String DIRECCION, String TELEFONO, String CELULAR, String VALOR,String TOTAL,String total_cuota, String PRESTAMO) {
        this.CUOTA = CUOTA;
        this.CUOTA_ID = CUOTA_ID;
        this.CLIENTE = CLIENTE;
        this.CEDULA = CEDULA;
        this.FECHA = FECHA;
        this.DIRECCION = DIRECCION;
        this.TELEFONO = TELEFONO;
        this.CELULAR = CELULAR;
        this.VALOR = VALOR;
        this.TOTAL = TOTAL;
        this.Total_cuota = total_cuota;
        this.PRESTAMO = PRESTAMO;
    }

    public DatosCliente(String CUOTA, String CUOTA_ID, String CLIENTE,String CEDULA, String FECHA, String DIRECCION, String TELEFONO, String CELULAR, String TOTAL,String total_cuota, String PRESTAMO) {
        this.CUOTA = CUOTA;
        this.CUOTA_ID = CUOTA_ID;
        this.CLIENTE = CLIENTE;
        this.CEDULA = CEDULA;
        this.FECHA = FECHA;
        this.DIRECCION = DIRECCION;
        this.TELEFONO = TELEFONO;
        this.CELULAR = CELULAR;
        this.TOTAL = TOTAL;
        this.Total_cuota = total_cuota;
        this.PRESTAMO = PRESTAMO;
    }

    public String getCUOTA() {
        return CUOTA;
    }

    public void setCUOTA(String CUOTA) {
        this.CUOTA = CUOTA;
    }

    public String getCUOTA_ID() {
        return CUOTA_ID;
    }

    public void setCUOTA_ID(String CUOTA_ID) {
        this.CUOTA_ID = CUOTA_ID;
    }

    public String getCLIENTE() {
        return CLIENTE;
    }

    public void setCLIENTE(String CLIENTE) {
        this.CLIENTE = CLIENTE;
    }

    public String getCEDULA() {return CEDULA;}

    public void setCEDULA(String CEDULA) {this.CEDULA = CEDULA;}

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getCELULAR() {
        return CELULAR;
    }

    public void setCELULAR(String CELULAR) {
        this.CELULAR = CELULAR;
    }

    public String getVALOR() {
        return VALOR;
    }

    public void setVALOR(String VALOR) {
        this.VALOR = VALOR;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }

    public String getTotal_cuota() {return Total_cuota;}

    public void setTotal_cuota(String total_cuota) {Total_cuota = total_cuota;}


    public String getPRESTAMO() {
        return PRESTAMO;
    }

    public void setPRESTAMO(String PRESTAMO) {
        this.PRESTAMO = PRESTAMO;
    }
}
