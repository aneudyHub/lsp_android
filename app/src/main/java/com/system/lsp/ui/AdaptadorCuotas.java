package com.system.lsp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.system.lsp.R;
import com.system.lsp.modelo.DatosCliente;
import com.system.lsp.provider.OperacionesBaseDatos;

import java.util.ArrayList;

/**
 * Adaptador para la lista de contactos
 */
public class AdaptadorCuotas extends RecyclerView.Adapter<AdaptadorCuotas.ViewHolder> implements Filterable {

    private Cursor items;
    private TelephonyManager mTelephonyManager;
    String TAG1 = "yourLogCatTag";
    private OperacionesBaseDatos datosBD;
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
        datosBD = OperacionesBaseDatos
                .obtenerInstancia(context);
    }

    // Instancia de escucha
    private OnItemClickListener escucha;

    /**
     * Interfaz para escuchar clicks del recycler
     */
    public interface OnItemClickListener {
        public void onClick(String idContacto, double pendiente, double montoPendiente, String nombre);
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

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_cuota, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final DatosCliente c = mArrayList.get(position);
        cantiCuota = c.getCUOTA();

        View statusIndicator = holder.statusIndicator;
        String cantida_cuota =  c.getCUOTA();


        if(cantida_cuota.equals("1")){
            statusIndicator.setBackgroundResource(R.color.activeStatus);
        }else {
            statusIndicator.setBackgroundResource(R.color.colorPrimaryDark);
        }

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
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String po = c.getPRESTAMO();
                String nombre = c.getCLIENTE();
                escucha.onClick(po,Double.parseDouble(c.getTOTAL()),Double.parseDouble(c.getTotal_cuota()),nombre);
            }
        });

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

    public Cursor getCursor() {
        return items;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayListOriginal;
                } else {
                    ArrayList<DatosCliente> filteredList = new ArrayList<>();

                    for (DatosCliente androidVersion : mArrayList) {

                        if (androidVersion.getCLIENTE().toLowerCase().contains(charString) || androidVersion.getDIRECCION().toLowerCase().contains(charString) ||
                                androidVersion.getPRESTAMO().toLowerCase().contains(charString)|| androidVersion.getTELEFONO().toLowerCase().contains(charString)) {
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
