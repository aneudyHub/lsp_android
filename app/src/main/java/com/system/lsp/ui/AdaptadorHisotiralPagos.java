package com.system.lsp.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.system.lsp.R;
import com.system.lsp.modelo.CuotaPaga;
import com.system.lsp.utilidades.UPreferencias;
import com.example.printer.ZebraPrint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suarez on 20/01/2018.
 */

public class AdaptadorHisotiralPagos extends RecyclerView.Adapter<AdaptadorHisotiralPagos.ViewHolder> implements Filterable {

    private List<CuotaPaga> mArrayList;
    private ArrayList<CuotaPaga> mFilteredList;
    private ArrayList<CuotaPaga> mArrayListOriginal;
    private Context context;

    public AdaptadorHisotiralPagos(ArrayList<CuotaPaga> mArrayList,Context context) {
        this.mArrayList=mArrayList;
        this.context = context;
        this.mFilteredList=mArrayList;
        this.mArrayListOriginal=mArrayList;

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombreCliente;
        public TextView fecha;
        public TextView monto;
        public TextView ipPrestamo;
        public View statusIndicator;
        public CardView mLayout;
        public Button mReimprimir;

        public ViewHolder(View v) {
            super(v);
            nombreCliente = (TextView) v.findViewById(R.id.nombre_cliente);
            fecha = (TextView) v.findViewById(R.id.fechaCobro);
            monto = (TextView) v.findViewById(R.id.montoCobrado);
            ipPrestamo= (TextView) v.findViewById(R.id.idPrestamoH);
            statusIndicator = v.findViewById(R.id.appointment_status);
            mLayout= (CardView) v.findViewById(R.id.Layout);
            mReimprimir =(Button)v.findViewById(R.id.reimprimir);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e("Este es el valor","Estoy aca");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_pagos, parent, false);
        return new AdaptadorHisotiralPagos.ViewHolder(v);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CuotaPaga c = mArrayList.get(position);

        View statusIndicator = holder.statusIndicator;
        final String fecha =  String.valueOf(c.getFecha());
        final String idPrestamos = String.valueOf(c.getPrestamoId());
        final String nombreCliente = String.valueOf(c.getNombreCliente());
        final String datos = String.valueOf(c.getCadenaString());
        final double totalMora = c.getTotalMora();
        final double monto = c.getMonto();
        final String nombreCobrador = String.valueOf(c.getNombreCobrador());
        final String telefonoCobrador = UPreferencias.obtenerTelefonoCobrador(context);




        holder.mReimprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Soy el nombre",nombreCliente);
                Log.e("STRING:",datos);
                Log.e("VALOR-TOTAL-MORA",String.valueOf(totalMora));
                ZebraPrint zebraprint = new ZebraPrint(view.getContext(),"imprimir", fecha,
                        idPrestamos,nombreCliente, datos,monto,totalMora, nombreCobrador,telefonoCobrador);
                zebraprint.probarlo();
            }
        });


        /*Log.e("Valor Cuota",pago);
        if(pago.equals("1")){
            statusIndicator.setBackgroundResource(R.color.activeStatus);
        }else {
            statusIndicator.setBackgroundResource(R.color.colorPrimaryDark);
        }*/


        //indicator_appointment_status
        holder.ipPrestamo.setText("#"+idPrestamos);
        holder.nombreCliente.setText(nombreCliente);
        holder.fecha.setText(fecha);
        holder.monto.setText("RD$ "+monto);
        



    }

    @Override
    public int getItemCount() {
       return (mArrayList==null)?0:mArrayList.size();
    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                Log.e("filter 1","estoy aca");

                if (charString.isEmpty()) {
                    Log.e("filter 2","estoy aca");
                    mFilteredList = mArrayListOriginal;
                } else {
                    Log.e("filter 3","estoy aca");
                    ArrayList<CuotaPaga> filteredList = new ArrayList<>();

                    for (CuotaPaga androidVersion : mArrayList) {

                        if (androidVersion.getNombreCliente().toLowerCase().contains(charString)||
                                String.valueOf(androidVersion.getPrestamoId()).toLowerCase().contains(charSequence) ) {
                            Log.e("filter 4","estoy aca");
                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mArrayList = (ArrayList<CuotaPaga>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
