package com.system.lsp.modelo;

/**
 * Created by aneudy on 8/7/2017.
 */

public class CuotaPendiente {
    private int Id;
    private int PrestamoId;
    private int Cuota;
    private double Capital;
    private double Interes;
    private double Mora;
    private String Fecha;
    private String FechaPagado;
    private int Pagado;
    private int Activo;
    private double MontoPagado;
    private int Modificado;
    private int Insertado;
    private int Eliminado;


    public CuotaPendiente() {
    }

    public CuotaPendiente(int cuota, double capital, double interes, double mora, String fecha, int pagado) {
        Cuota = cuota;
        Capital = capital;
        Interes = interes;
        Mora = mora;
        Fecha = fecha;
        Pagado = pagado;
    }

    public CuotaPendiente(int id, int prestamoId, int cuota, double capital, double interes, double mora, String fecha, String FechaPagado, int pagado, int activo, double montoPagado, int modificado, int insertado, int eliminado) {
        Id = id;
        PrestamoId = prestamoId;
        Cuota = cuota;
        Capital = capital;
        Interes = interes;
        Mora = mora;
        Fecha = fecha;
        Pagado = pagado;
        Activo = activo;
        MontoPagado = montoPagado;
        Modificado = modificado;
        Insertado = insertado;
        Eliminado = eliminado;
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


    public int getModificado() {
        return Modificado;
    }

    public void setModificado(int modificado) {
        Modificado = modificado;
    }

    public int getInsertado() {
        return Insertado;
    }

    public void setInsertado(int insertado) {
        Insertado = insertado;
    }

    public int getEliminado() {
        return Eliminado;
    }

    public void setEliminado(int eliminado) {
        Eliminado = eliminado;
    }


    public Boolean compararCon(CuotaPendiente prestamoDetalle){
        return Id==prestamoDetalle.getId() &&
                PrestamoId ==prestamoDetalle.getPrestamoId() &&
                Cuota ==prestamoDetalle.getCuota() &&
                Capital == prestamoDetalle.getCapital() &&
                Interes == prestamoDetalle.getInteres() &&
                Mora == prestamoDetalle.getMora() &&
                Fecha.equalsIgnoreCase(prestamoDetalle.getFecha()) &&
                Pagado == prestamoDetalle.getPagado() &&
                Activo == prestamoDetalle.getActivo() &&
                MontoPagado ==prestamoDetalle.getMontoPagado();

    }

    /*public int esMasReciente(PrestamoDetalle match){
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date fechaA = formato.parse(this.getVersion());
            Date fechaB = formato.parse(match.getVersion());

            return fechaA.compareTo(fechaB);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }*/
}
