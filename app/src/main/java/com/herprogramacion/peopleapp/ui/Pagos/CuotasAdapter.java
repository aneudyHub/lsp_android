package com.herprogramacion.peopleapp.ui.Pagos;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.herprogramacion.peopleapp.R;
import com.herprogramacion.peopleapp.provider.Contract;
import com.herprogramacion.peopleapp.ui.AdaptadorCuotas;
import com.herprogramacion.peopleapp.ui.Login.LoginActivity;
import com.herprogramacion.peopleapp.utilidades.Resolve;
import com.herprogramacion.peopleapp.utilidades.UConsultas;
import com.herprogramacion.peopleapp.utilidades.UPreferencias;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aneudy on 12/23/2017.
 */


public class CuotasAdapter extends RecyclerView.Adapter<CuotasAdapter.CuotaViewHolder>{

    private Cursor mItems;
    private HashMap<String,String[]> modificaCuota=null;
    private Context mCtx;
    public static String datos = "";
    public static double totalMora;
    double abonoM,abonado;
    private double montoRestante =0.00 ;
    private String cantidadCuota ="";
    private int totalCuotas =0;
    DecimalFormat precision = new DecimalFormat("0.00");;

    public CuotasAdapter(Context mCtx){
        this.mCtx=mCtx;
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
        double cap = Double.parseDouble(UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.CAPITAL));
        double interes = Double.parseDouble(UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.INTERES));
        double mora = Double.parseDouble(UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.MORA));
        abonado = Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.MONTO_PAGADO)));
        abonoM = Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ABONO_MORA)));
        Log.e("VALOR-CP",String.valueOf(cap));
        Log.e("VALOR-INTERES",String.valueOf(interes));
        Log.e("VALOR-MORA",String.valueOf(mora));
        Log.e("VALOR-ABONADO",String.valueOf(abonado));
        double cuta_monto = (cap + interes);
        double total = (cap + interes + mora) - (abonado);


        Log.e("VALOR-TOTAL",String.valueOf(total));

        String numeroCuota = String.format("%s", UConsultas.obtenerString(mItems, Contract.PrestamoDetalle.CUOTA));
        totalCuotas += Integer.parseInt(numeroCuota) ;

        holder.cuota_monto.setText(precision.format(cap + interes));
        holder.moraMonto.setText(precision.format(mora - abonoM));
        holder.total_monto.setText("RD$ "+precision.format(total));
        holder.mLayout.setBackgroundColor(mCtx.getResources().getColor(R.color.cardview_light_background));
        holder.mIcon.setVisibility(View.GONE);
        holder.mRestante.setVisibility(View.GONE);

        if(mora>0){
            holder.total_monto.setTextColor(mCtx.getResources().getColor(R.color.mora));
        }else{
            holder.total_monto.setTextColor(mCtx.getResources().getColor(R.color.negro87));
        }

        if(modificaCuota!=null){
            Log.e("MODIFICA","1");

            for(Map.Entry<String,String[]> h:modificaCuota.entrySet()){
                Log.e("MODIFICA","2");
                String key = h.getKey();
                String[] value = h.getValue();
                Log.e("VALOR-DE-VALUE",value[0]);
                if(key.equalsIgnoreCase(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)))){
                    if(value[0].equalsIgnoreCase("1")){
                        Log.e("MODIFICA","3");
                        holder.mIcon.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                        holder.mIcon.setVisibility(View.VISIBLE);
                        holder.mRestante.setVisibility(View.GONE);
                    }else{
                        Log.e("MODIFICA","4");
                        holder.mIcon.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp));
                        holder.mIcon.setVisibility(View.VISIBLE);
                        holder.mRestante.setVisibility(View.VISIBLE);
                        montoRestante = Double.parseDouble(value[3]);
                        Log.e("RESTAANTE",String.valueOf(montoRestante));
                        double restante = Math.abs(montoRestante) - total;
                        holder.mRestante.setText("Resta RD$ "+String.valueOf(precision.format(restante)));
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
        public TextView mFecha;
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
        }
    }

    public double getTotalPendiente(){
        double r =0;
        double mp=0;
        double restante=0;

        if(mItems!=null){
            while (mItems.moveToNext()){
                r += (  Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CAPITAL))) +
                        Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.INTERES))) +
                        Double.parseDouble(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.MORA))));
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
            double tMora =0;
            double restanM=0;
            double restaMotoP=0;
            StringBuilder sb= new StringBuilder() ;
            for (mItems.moveToFirst(); !mItems.isAfterLast(); mItems.moveToNext()) {
                double c = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.CAPITAL));
                double i = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.INTERES));
                double m = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.MORA));
                double mp = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.MONTO_PAGADO));
                abonoM = mItems.getDouble(mItems.getColumnIndex(Contract.PrestamoDetalle.ABONO_MORA));

                Log.e("CUOTA",mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CUOTA)));
                Log.e("VALOR-CP",String.valueOf(c));
                Log.e("VALOR-INTERES",String.valueOf(i));
                Log.e("VALOR-MORA",String.valueOf(m));
                Log.e("VALOR-MPAGADO",String.valueOf(mp));
                Log.e("VALOR-ABONADO",String.valueOf(abonoM));

                restanM = m - abonoM;
                if(montoAPagar==0){
                    notifyDataSetChanged();
                    break;
                }
                restaMotoP = abonoM - mp;

                double cot = (c + i ) - Math.abs(restaMotoP);
                Log.e("VALOR COT",String.valueOf(cot));
                Log.e("Restante Moraaaa",String.valueOf(restanM));
                Log.e("MONTO-A-PAGAR",String.valueOf(montoAPagar));
                if(restanM > 0){
                    if (montoAPagar >= restanM){
                        modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),new String[]{"2",String.valueOf((montoAPagar + abonoM)),
                                String.valueOf((mp+montoAPagar)),String.valueOf((montoAPagar))});

                        montoAPagar -= restanM;
                        tMora +=restanM;
                        sb.append("Pago Mora a la fecha."+";"+restanM+";");
                    }else {
                        modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),new String[]{"3",String.valueOf(montoAPagar+abonoM),
                                String.valueOf((mp+montoAPagar)),String.valueOf((montoAPagar))});
                        sb.append("Abono Mora a la fecha."+";"+Math.abs(montoAPagar)+";");
                        Log.e("Valor Monto A",String.valueOf(montoAPagar));
                        tMora +=montoAPagar;
                        montoAPagar = 0;
                    }
                }
                if(montoAPagar==0){
                    notifyDataSetChanged();
                    continue;
                }else {

                    if (montoAPagar >= cot){
                        modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),new String[]{"1",String.valueOf((c + i + restanM)),
                                String.valueOf((mp+montoAPagar)),String.valueOf((montoAPagar))});
                        montoAPagar -=cot;
                        sb.append("Pago Cuota(s)N."+mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CUOTA))+"/"+ cantidadCuota +";"+Math.abs(cot)+";");

                    }else {
                        modificaCuota.put(mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.ID)),new String[]{"0",String.valueOf(montoAPagar),
                                String.valueOf((mp+montoAPagar)),String.valueOf((montoAPagar))});
                        sb.append("Abono Cuota(s)N."+mItems.getString(mItems.getColumnIndex(Contract.PrestamoDetalle.CUOTA))+"/"+ cantidadCuota +";"+
                                String.valueOf(precision.format(montoAPagar))+";");
                        montoAPagar=0;
                    }
                }
                notifyDataSetChanged();
            }
            datos = sb.toString();
            totalMora = tMora;

            Log.e("TOTAL-MORA",String.valueOf(datos));
            Log.e("TOTAL-MORA",String.valueOf(totalMora));
        }
    }
}
