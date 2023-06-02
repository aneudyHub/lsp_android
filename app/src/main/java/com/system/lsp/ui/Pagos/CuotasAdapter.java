package com.system.lsp.ui.Pagos;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.system.lsp.R;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.ui.AdaptadorCuotas;
import com.system.lsp.ui.Login.LoginActivity;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.UConsultas;
import com.system.lsp.utilidades.UPreferencias;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aneudy on 12/23/2017.
 */


public class CuotasAdapter extends RecyclerView.Adapter<CuotasAdapter.CuotaViewHolder>{

    public Cursor mItems;
    private Cursor cursor;
    private OperacionesBaseDatos datosBD;
    private HashMap<String,String[]> modificaCuota=null;
    private Context mCtx;
    public static String datos = "";
    public static double totalMora;
    private String idPrestamo;
    double abonoM,abonado;
    double abonoMora,abonoCapital;
    private double montoRestante =0.00 ;
    private String cantidadCuota ="";
    private int totalCuotas =0;
    DecimalFormat precision = new DecimalFormat("0.00");;

    public CuotasAdapter(Context mCtx,String idPrestamo){

        datosBD = OperacionesBaseDatos
                .obtenerInstancia(mCtx);
        this.mCtx=mCtx;
        this.idPrestamo=idPrestamo;
    }


    @Override
    public CuotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuota_item,parent,false);
        return new CuotaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CuotaViewHolder holder, int position) {
        mItems.moveToPosition(position);




        holder.mFecha.setText(String.format("%s", UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.FECHA)));
        holder.mNumero.setText("#"+String.format("%s", UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.CUOTA)));
        holder.diasAtrasados.setText(String.format("%s", UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.DIAS_ATRASADOS)));
        double cap = Double.parseDouble(UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.CAPITAL));
        double interes = Double.parseDouble(UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.INTERES));
        double mora = Double.parseDouble(UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.MORA));
        double mora_acumulada = Double.parseDouble(UConsultas.obtenerString(mItems,Contract.PrestamoDetalle.MORA_ACUMULADA));
        abonado = Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.MONTO_PAGADO)));
        abonoM = Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ABONO_MORA)));
        double cuta_monto = (cap + interes);
        double valorMora = mora - abonoM;
        double total = (cap + interes + valorMora) - (abonado);

        String numeroCuota = String.format("%s", UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.CUOTA));
        totalCuotas += Integer.parseInt(numeroCuota) ;

        holder.cuota_monto.setText(precision.format(cap + interes));
        holder.moraMonto.setText(precision.format(valorMora));
        holder.total_monto.setText("RD$ "+precision.format(total));
        holder.mLayout.setBackgroundColor(mCtx.getResources().getColor(R.color.cardview_light_background));
        holder.mIcon.setVisibility(View.GONE);
        holder.mRestante.setVisibility(View.GONE);
        holder.mAbonado.setText(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.MONTO_PAGADO)));

        if(mora>0){
            holder.total_monto.setTextColor(mCtx.getResources().getColor(R.color.mora));
        }else{
            holder.total_monto.setTextColor(mCtx.getResources().getColor(R.color.negro87));
        }

        if(modificaCuota!=null){

            for(Map.Entry<String,String[]> h:modificaCuota.entrySet()){
                String key = h.getKey();
                String[] value = h.getValue();
                if(key.equalsIgnoreCase(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)))){
                    if(value[0].equalsIgnoreCase("1")){

                        //holder.mIcon.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                        //holder.mIcon.setVisibility(View.VISIBLE);
                        holder.mRestante.setVisibility(View.GONE);
                    }else{

                        //holder.mIcon.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp));
                       // holder.mIcon.setVisibility(View.VISIBLE);
                        holder.mRestante.setVisibility(View.VISIBLE);
                        double restante = Math.abs(total) - Math.abs(montoRestante);

                    }
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.getCount();
        else
            return 0;
    }

    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            mItems = nuevoCursor;
            notifyDataSetChanged();
        }
    }

    public static class CuotaViewHolder extends RecyclerView.ViewHolder{
        public CardView mLayout;
        public TextView mFecha,mAbonado,diasAtrasados;
        public TextView cuota_monto,moraMonto,total_monto,mNumero,mRestante;
        public ImageView mIcon;

        public CuotaViewHolder(View itemView) {
            super(itemView);
            mLayout=(CardView)itemView.findViewById(R.id.Layout);
            mFecha=(TextView)itemView.findViewById(R.id.Cuota_Fecha);
            cuota_monto=(TextView)itemView.findViewById(R.id.Cuot_Monto);
            moraMonto=(TextView)itemView.findViewById(R.id.Mora_Monto);
            total_monto=(TextView)itemView.findViewById(R.id.Total_Monto);
            mNumero=(TextView)itemView.findViewById(R.id.Cuota_Numero);
            mIcon =(ImageView)itemView.findViewById(R.id.Cuota_Modificacion);
            mRestante=(TextView)itemView.findViewById(R.id.Restante);
            mAbonado=(TextView)itemView.findViewById(R.id.Abonado);
            diasAtrasados = (TextView)itemView.findViewById(R.id.dias_atrasados);
        }
    }

    public double getTotalPendiente(){
        double r =0;
        double mp=0;
        double restante=0;
        double ValorMora=0;

        if(mItems!=null){
            while (mItems.moveToNext()){
                ValorMora = Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.MORA))) -
                        Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ABONO_MORA)));

                r += (  Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CAPITAL))) +
                        Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.INTERES))) +
                        ValorMora);

                mp +=(Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.MONTO_PAGADO))));
            }

        }
        restante = r - mp;
        return restante ;
    }


    public HashMap<String,String[]> getModificaCuota(){
        return this.modificaCuota;
    }

    public void marcarCuotas(double montoAPagar){
        if(modificaCuota==null)
            modificaCuota= new HashMap<>();

        modificaCuota.clear();

        if(mItems!=null){
            for (mItems.moveToFirst(); !mItems.isAfterLast(); mItems.moveToNext()) {
                cantidadCuota = mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CUOTA));
            }
            double valorTotalMora;


            valorTotalMora = datosBD.obtenerTotalMora(idPrestamo);
            double pagoMora;
            double abonoMora;
            double tMora =0;
            double restanM;
            StringBuilder sb= new StringBuilder() ;

            pagoMora =valorTotalMora;
            abonoMora=montoAPagar;
            if(valorTotalMora > 0) {

                for (mItems.moveToFirst(); !mItems.isAfterLast(); mItems.moveToNext()) {
                    double m = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.MORA));
                    abonoM = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.ABONO_MORA));

                    restanM = m - abonoM;


                        if (montoAPagar >= valorTotalMora) {
                            modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),
                                    new String[]{"2", String.valueOf((restanM + abonoM))});

                            montoAPagar -= restanM;
                            tMora = valorTotalMora;

                            valorTotalMora -=restanM ;
                        } else if(montoAPagar >= restanM){
                           modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),
                                    new String[]{"2", String.valueOf(restanM + abonoM)});

                            tMora += montoAPagar;
                            valorTotalMora -= restanM;
                            montoAPagar=0 ;

                        }
                    
                    notifyDataSetChanged();
                }

                if(valorTotalMora>0){
                    sb.append("Abono Mora Generada a la fecha." + ";" + Math.abs(abonoMora) + ";");
                }else {
                    sb.append("Pago Mora Generada a la fecha." + ";" + pagoMora + ";");
                }
            }




            for (mItems.moveToFirst(); !mItems.isAfterLast(); mItems.moveToNext()) {
                double c = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.CAPITAL));
                double i = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.INTERES));
                double mp = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.MONTO_PAGADO));

                Double montoRestante = montoAPagar;

                double cot = (c + i) - Math.abs(mp);

                if (montoAPagar == 0) {
                    notifyDataSetChanged();
                    continue;
                } else {
                    if (montoAPagar >= cot) {
                        modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),
                                new String[]{"1", String.valueOf((c + i))});
                        montoAPagar -= cot;
                        sb.append("Pago Cuota(s)N." + mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CUOTA)) + "/" + cantidadCuota + ";" + Math.abs(cot) + ";");

                    } else {
                        modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),
                                new String[]{"0", String.valueOf((montoAPagar))});
                        sb.append("Abono Cuota(s)N." + mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CUOTA)) + "/" + cantidadCuota + ";" +
                                String.valueOf(precision.format(montoAPagar)) + ";");
                        montoAPagar = 0;
                    }
                }
                notifyDataSetChanged();
            }



            datos = sb.toString();
            totalMora = tMora;

            notifyDataSetChanged();

        }
    }
}
