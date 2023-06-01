package com.system.lsp.modelo;

import com.system.lsp.utilidades.UTiempo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aneudy on 8/7/2017.
 */

public class PrestamoDetalle {
    private int Id;
    private int PrestamoId;
    private int Cuota;
    private double Capital;
    private double Interes;
    private double Mora;
    private String Fecha;
    private String dias_atrasados;
    private String FechaPagado;
    private int Pagado;
    private int Activo;
    private double MontoPagado;
    private double Abono_mora;
    private double mora_acumulada;
    private String Updated_at;
    private int Modificado;

    public PrestamoDetalle() {
    }

    public PrestamoDetalle(int cuota, double capital, double interes, double mora, String fecha, int pagado) {
        Cuota = cuota;
        Capital = capital;
        Interes = interes;
        Mora = mora;
        Fecha = fecha;
        Pagado = pagado;
    }

    public PrestamoDetalle(int id, int prestamoId, int cuota, double capital, double interes, double mora, String fecha,String dias_atrasados, String FechaPagado, int pagado, int activo, double montoPagado,double abono_mora,double mora_acumulada,String updated_at, int modificado) {
        Id = id;
        PrestamoId = prestamoId;
        Cuota = cuota;
        Capital = capital;
        Interes = interes;
        Mora = mora;
        Fecha = fecha;
        this.dias_atrasados = dias_atrasados;
        Pagado = pagado;
        Activo = activo;
        MontoPagado = montoPagado;
        Abono_mora = abono_mora;
        this.mora_acumulada = mora_acumulada;
        Updated_at = updated_at;
        Modificado = modificado;
        this.FechaPagado=FechaPagado;
    }


    public String getFechaPagado() {
        return FechaPagado;
    }

    public void setFechaPagado(String fechaPagado) {
        FechaPagado = fechaPagado;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPrestamoId() {
        return PrestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        PrestamoId = prestamoId;
    }

    public int getCuota() {
        return Cuota;
    }

    public void setCuota(int cuota) {
        Cuota = cuota;
    }

    public double getCapital() {
        return Capital;
    }

    public void setCapital(double capital) {
        Capital = capital;
    }

    public double getInteres() {
        return Interes;
    }

    public void setInteres(double interes) {
        Interes = interes;
    }

    public double getMora() {
        return Mora;
    }

    public void setMora(double mora) {
        Mora = mora;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getDias_atrasados() {return dias_atrasados;}

    public void setDias_atrasados(String dias_atrasados) {this.dias_atrasados = dias_atrasados;}

    public int getPagado() {
        return Pagado;
    }

    public void setPagado(int pagado) {
        Pagado = pagado;
    }

    public int getActivo() {
        return Activo;
    }

    public void setActivo(int activo) {
        Activo = activo;
    }

    public double getMontoPagado() {
        return MontoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        MontoPagado = montoPagado;
    }

    public double getAbono_mora() {return Abono_mora;}

    public void setAbono_mora(double abono_mora) {Abono_mora = abono_mora;}

    public double getMora_acumulada() {return mora_acumulada;}

    public void setMora_acumulada(double mora_acumulada) {mora_acumulada = mora_acumulada;}

    public String getUpdated_at() {return Updated_at;}

    public void setUpdated_at(String version) {Updated_at = version;}
    public int getModificado() {
        return Modificado;
    }

    public void setModificado(int modificado) {
        Modificado = modificado;
    }



    public Boolean compararCon(PrestamoDetalle prestamoDetalle){
        return Id==prestamoDetalle.getId() &&
                PrestamoId ==prestamoDetalle.getPrestamoId() &&
                Cuota ==prestamoDetalle.getCuota() &&
                Capital == prestamoDetalle.getCapital() &&
                Interes == prestamoDetalle.getInteres() &&
                Mora == prestamoDetalle.getMora() &&
                Fecha.equalsIgnoreCase(prestamoDetalle.getFecha()) &&
                dias_atrasados.equalsIgnoreCase(prestamoDetalle.getDias_atrasados()) &&
                Pagado == prestamoDetalle.getPagado() &&
                Activo == prestamoDetalle.getActivo() &&
                MontoPagado ==prestamoDetalle.getMontoPagado()&&
                Abono_mora ==prestamoDetalle.getAbono_mora()&&
                mora_acumulada ==prestamoDetalle.getMora_acumulada();

    }

    public void aplicarSanidad(){
        this.setUpdated_at(this.getUpdated_at() == null? UTiempo.obtenerTiempo():this.getUpdated_at());
        this.setModificado(0);
    }


    public int esMasReciente(PrestamoDetalle match){
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
