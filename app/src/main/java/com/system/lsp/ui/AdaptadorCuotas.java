package com.system.lsp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.system.lsp.R;
import com.system.lsp.modelo.DatosCliente;


import java.util.ArrayList;

/**
 * Adaptador para la lista de contactos
 */
public class AdaptadorCuotas extends RecyclerView.Adapter<AdaptadorCuotas.ViewHolder> implements Filterable {

    private Cursor items;
    private TelephonyManager mTelephonyManager;
    String TAG1 = "yourLogCatTag";
    private ArrayList<DatosCliente> mFilteredList;
    ArrayList<DatosCliente> mArrayList;
    ArrayList<DatosCliente> mArrayListOriginal;
    public static String cantiCuota;
    private Context context;



    public AdaptadorCuotas(Context context,OnItemClickListener escucha, ArrayList<DatosCliente> mArrayList) {
        this.context = context;
        this.escucha = escucha;
        this.mArrayList=mArrayList;
        this.mFilteredList=mArrayList;
        this.mArrayListOriginal=mArrayList;
    }

    // Instancia de escucha
    private OnItemClickListener escucha;

    /**
     * Interfaz para escuchar clicks del recycler
     */
    public interface OnItemClickListener {
        public void onClick(String idContacto,double montoPendiente,String nombre);
        void showFoto(String documento);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView fecha;
        public TextView nombre_cliente;
        public TextView telefono;
        public TextView celular;
        public TextView cuota;
        public TextView direccion;
        public TextView total;
        public TextView cedula_cliente;
        public TextView idPrestamo;
        public View statusIndicator;
        public CardView mLayout;
        public ImageView mFoto;

        public ViewHolder(View v) {
            super(v);
            fecha = (TextView) v.findViewById(R.id.fecha);
            nombre_cliente = (TextView) v.findViewById(R.id.nombre_cliente);
            cedula_cliente = (TextView) v.findViewById(R.id.cedula_cliente);
            telefono = (TextView) v.findViewById(R.id.telefono);
            celular = (TextView)v.findViewById(R.id.celular);
            total = (TextView) v.findViewById(R.id.total);
            direccion = (TextView) v.findViewById(R.id.direccion);
            cuota = (TextView) v.findViewById(R.id.cuota);
            idPrestamo = (TextView) v.findViewById(R.id.idPrestamo);
            statusIndicator = itemView.findViewById(R.id.indicator_appointment_status);
            mLayout= (CardView) itemView.findViewById(R.id.Layout);
            cantiCuota = cuota.getText().toString();
            mFoto = (ImageView) itemView.findViewById(R.id.foto);



            telefono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewIn) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telefono.getText().toString()));
                        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!=
                                PackageManager.PERMISSION_GRANTED)
                            return;
                        context.startActivity(intent);
                    } catch (Exception except) {
                        Log.e(TAG1,"Ooops GMAIL account selection problem "+except.getMessage());
                    }
                }
            });

            celular.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewIn) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+celular.getText().toString()));
                        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!=
                                PackageManager.PERMISSION_GRANTED)
                            return;
                        context.startActivity(intent);
                    } catch (Exception except) {
                        Log.e(TAG1,"Ooops GMAIL account selection problem "+except.getMessage());
                    }
                }
            });



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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e("Este es el valor","Estoy aca");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_cuota, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final DatosCliente c = mArrayList.get(position);
        Log.e("Valor cuota",c.getCUOTA());
        cantiCuota = c.getCUOTA();
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
       // cantiCuota = c.getCUOTA();
        holder.cuota.setText(cantida_cuota);
        holder.nombre_cliente.setText(c.getCLIENTE());
        holder.cedula_cliente.setText(c.getCEDULA());
        if (c.getTELEFONO().equals("NULL")||c.getTELEFONO().equals("null")){
            holder.telefono.setText("");

        }else {
            holder.telefono.setText(c.getTELEFONO());
        }
        if (c.getCELULAR().equals("NULL")||c.getCELULAR().equals("null")){
            holder.celular.setText("");

        }else {
            holder.celular.setText(c.getCELULAR());
        }
        holder.direccion.setText(c.getDIRECCION());
        holder.fecha.setText(c.getFECHA());
        holder.total.setText("RD$ "+c.getTOTAL());
        holder.idPrestamo.setText("# "+c.getPRESTAMO());
        Log.e("TOTAL-GETOTAL",""+String.valueOf(c.getTOTAL()));
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String po = c.getPRESTAMO();
                String nombre = c.getCLIENTE();
                escucha.onClick(po,Double.parseDouble(c.getTOTAL()),nombre);
            }
        });
        //Log.e("Este es el valor",""+nombre);
       /* holder.nombre.setText(mFilteredList.get(position).getPrimerNombre());
        holder.telefono.setText(mFilteredList.get(position).getTelefono());
        holder.correo.setText(mFilteredList.get(position).getCorreo());
       // holder.status.setText(status);
        holder.fecha.setText(mFilteredList.get(position).getVersion());*/


       holder.mFoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               escucha.showFoto(c.getCEDULA());
           }
       });




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

                        if (androidVersion.getCLIENTE().toLowerCase().contains(charString) || androidVersion.getDIRECCION().toLowerCase().contains(charString) ||
                                androidVersion.getPRESTAMO().toLowerCase().contains(charString)|| androidVersion.getTELEFONO().toLowerCase().contains(charString)) {
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
