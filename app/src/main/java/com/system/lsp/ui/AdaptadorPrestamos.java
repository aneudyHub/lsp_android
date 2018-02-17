package com.system.lsp.ui;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.system.lsp.R;
import com.system.lsp.modelo.DatosCliente;

import java.util.ArrayList;

/**
 * Created by Suarez on 13/01/2018.
 */

public class AdaptadorPrestamos extends RecyclerView.Adapter<AdaptadorPrestamos.ViewHolder> implements Filterable {

    private Cursor items;


    private ArrayList<DatosCliente> mFilteredList;
    ArrayList<DatosCliente> mArrayList;
    ArrayList<DatosCliente> mArrayListOriginal;




    public AdaptadorPrestamos(AdaptadorPrestamos.OnItemClickListener escucha, ArrayList<DatosCliente> mArrayList) {
        this.escucha = escucha;
        this.mArrayList=mArrayList;
        this.mFilteredList=mArrayList;
        this.mArrayListOriginal=mArrayList;
    }

    // Instancia de escucha
    private AdaptadorPrestamos.OnItemClickListener escucha;

    /**
     * Interfaz para escuchar clicks del recycler
     */
    public interface OnItemClickListener {
        public void onClick(String idContacto,String montoPendiente);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView fecha;
        public TextView nombre_cliente;
        public TextView id_prestamo;
        public TextView cuota;
        public TextView telefono;
        public TextView celular;
        public TextView total;
        public TextView valor;
        public View statusIndicator;
        public CardView mLayout;

        public ViewHolder(View v) {
            super(v);
            fecha = (TextView) v.findViewById(R.id.fecha);
            nombre_cliente = (TextView) v.findViewById(R.id.nombre_cliente);
            id_prestamo = (TextView) v.findViewById(R.id.prestamo_id);
            valor = (TextView) v.findViewById(R.id.valor);
            total = (TextView) v.findViewById(R.id.total);
            telefono = (TextView) v.findViewById(R.id.telefono);
            celular = (TextView) v.findViewById(R.id.celular);
            cuota = (TextView) v.findViewById(R.id.cuota);
            statusIndicator = itemView.findViewById(R.id.appointment_status);
            mLayout= (CardView) itemView.findViewById(R.id.Layout);
        }
    }

    /**
     * Obtiene el valor de la columna 'idContacto' basado en la posición actual del cursor
     * @param
     * @return Identificador del contacto
     */
    /*private String obtenerIdCliente(int posicion) {

        return mFilteredList.get(posicion).getId();

    }*/

    @Override
    public AdaptadorPrestamos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e("Este es el valor","Estoy aca");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_prestamo, parent, false);
        return new AdaptadorPrestamos.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorPrestamos.ViewHolder holder, final int position) {

        final DatosCliente c = mArrayList.get(position);
        Log.e("holder",c.getCLIENTE());
        //items.moveToPosition(position);
        // View statusIndicator = holder.statusIndicator;

        //Log.e("Este es el ID CONTACTO",mFilteredList.get(position).getIdContacto());


        /*String status;

        status = mFilteredList.get(position).getStatus();

        // estado: se colorea indicador según el estado
        switch (status) {
            case "Activa":
                // mostrar botón
                //holder.cancelButton.setVisibility(View.VISIBLE);
                statusIndicator.setBackgroundResource(R.color.activeStatus);
                break;
            case "Cumplida":
                // ocultar botón
                //holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.completedStatus);
                break;
            case "Cancelada":
                // ocultar botón
                //holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.cancelledStatus);
                break;
        }

        UConsultas.obtenerString(items, Tareas.NOMBRE);
*/
        View statusIndicator = holder.statusIndicator;
        String cantida_cuota =  c.getCUOTA();


        Log.e("Valor Cuota",cantida_cuota);
        if(cantida_cuota.equals("1")){
            statusIndicator.setBackgroundResource(R.color.activeStatus);
        }else {
            statusIndicator.setBackgroundResource(R.color.colorPrimaryDark);
        }
       /* switch (cantida_cuota) {
            case "cantida_cuota":
                // mostrar botón
                //holder.cancelButton.setVisibility(View.VISIBLE);
                statusIndicator.setBackgroundResource(R.color.activeStatus);
                break;
            case "Cumplida":
                // ocultar botón
                //holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.completedStatus);
                break;
            case "Cancelada":
                // ocultar botón
                //holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.cancelledStatus);
                break;
        }*/


        //indicator_appointment_status
        Log.e("VALOR CUOTA",cantida_cuota);
        holder.nombre_cliente.setText(c.getCLIENTE());
        holder.id_prestamo.setText("#"+c.getPRESTAMO());
        holder.fecha.setText(c.getFECHA());
        if (c.getTELEFONO().equals("NULL") || c.getTELEFONO().equals("null")){
            holder.telefono.setText("");
        }else {
            holder.telefono.setText(c.getTELEFONO());
        }

        if (c.getCELULAR().equals("NULL") || c.getCELULAR().equals("null")){
            if(holder.celular!=null){
                holder.celular.setText("");
            }

        }else {
            if(holder.celular!=null){
                holder.celular.setText(c.getCELULAR()==null?"":c.getCELULAR());
            }

        }
        holder.valor.setText("RD$ "+c.getVALOR());
        holder.total.setText("RD$ "+c.getTOTAL());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String po = c.getPRESTAMO();
                escucha.onClick(po,String.format(c.getCLIENTE()));
            }
        });
        //Log.e("Este es el valor",""+nombre);
       /* holder.nombre.setText(mFilteredList.get(position).getPrimerNombre());
        holder.telefono.setText(mFilteredList.get(position).getTelefono());
        holder.correo.setText(mFilteredList.get(position).getCorreo());
       // holder.status.setText(status);
        holder.fecha.setText(mFilteredList.get(position).getVersion());*/




    }

    @Override
    public int getItemCount() {

        return (mArrayList==null)?0:mArrayList.size();

    }

//    public void swapCursor(Cursor nuevoCursor) {
//
//        mArrayList = new ArrayList<DatosCliente>();
//
//
//        int cuota = nuevoCursor.getColumnIndex(Contract.Cobrador.CUOTA);
//        int cuota_id = nuevoCursor.getColumnIndex(Contract.Cobrador.CUOTA_ID);
//        int cliente = nuevoCursor.getColumnIndex(Contract.Cobrador.CLIENTE);
//        int fecha = nuevoCursor.getColumnIndex(Contract.Cobrador.FECHA);
//        int direccion = nuevoCursor.getColumnIndex(Contract.Cobrador.DIRECCION);
//        int telefono = nuevoCursor.getColumnIndex(Contract.Cobrador.TELEFONO);
//        int celular = nuevoCursor.getColumnIndex(Contract.Cobrador.CELULAR);
//        int total = nuevoCursor.getColumnIndex(Contract.Cobrador.TOTAL);
//        int prestamo = nuevoCursor.getColumnIndex(Contract.Cobrador.PRESTAMO);
//
//
//
//        if (nuevoCursor != null) {
//            for (nuevoCursor.moveToFirst(); !nuevoCursor.isAfterLast();nuevoCursor.moveToNext()){
//
//                DatosCliente cli = new DatosCliente(nuevoCursor.getString(cuota),nuevoCursor.getString(cuota_id),
//                        nuevoCursor.getString(cliente),nuevoCursor.getString(fecha),nuevoCursor.getString(direccion),
//                        nuevoCursor.getString(telefono),nuevoCursor.getString(celular),nuevoCursor.getString(total),
//                        nuevoCursor.getString(prestamo)
//                        );
//                mArrayList.add(cli);
//                Log.e("Este es el ARRAY",cli.getDIRECCION());
//                notifyDataSetChanged();
//            }
//
//
//
//            //items = nuevoCursor;
//
//        }
//    }

    public Cursor getCursor() {
        return items;
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
                    ArrayList<DatosCliente> filteredList = new ArrayList<>();

                    for (DatosCliente androidVersion : mArrayList) {

                        if (androidVersion.getCLIENTE().toLowerCase().contains(charString) || androidVersion.getDIRECCION().toLowerCase().contains(charString) || androidVersion.getTELEFONO().toLowerCase().contains(charString) || androidVersion.getPRESTAMO().toLowerCase().contains(charString)) {
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
                mArrayList = (ArrayList<DatosCliente>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
