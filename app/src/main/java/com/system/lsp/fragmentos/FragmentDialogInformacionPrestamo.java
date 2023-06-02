package com.system.lsp.fragmentos;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.system.lsp.R;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.OperacionesBaseDatos;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Suarez on 24/03/2018.
 */

public class FragmentDialogInformacionPrestamo extends DialogFragment {

    private final String LOG_TAG = FragmentDialogInformacionPrestamo.class.getSimpleName();

    private OperacionesBaseDatos datos;
    public Cursor cursor,cursor1;
    private String message = "message";
    private String idPrestamo;
    private Context context;

    public FragmentDialogInformacionPrestamo setMessage(Context context, String customMessage, String idPrestamo) {
        message = customMessage;
        this.idPrestamo = idPrestamo;
        this.context = context;
        return this;
    }

    // onCreate --> (onCreateDialog) --> onCreateView --> onActivityCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");


        getDialog().setTitle("INFO PRESTAMO");
        TextView textView=(TextView) getDialog().findViewById(android.R.id.title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);

        View dialogView = inflater.inflate(R.layout.dialog_info_content, container, false);
        // "Got it" button
        Button buttonPos = (Button) dialogView.findViewById(R.id.pos_button);
        TextView nombreCliente = (TextView)dialogView.findViewById(R.id.nombre_cliente_dia);
        nombreCliente.setText(message);
        TextView idprestamo = (TextView)dialogView.findViewById(R.id.id_prestamo);
        idprestamo.setText(idPrestamo);
        TextView montoPrestamo = (TextView) dialogView.findViewById(R.id.montoPrestamo);
        TextView valorCuota = (TextView) dialogView.findViewById(R.id.valorCuota);
        TextView plasoCuota = (TextView) dialogView.findViewById(R.id.plazoCuota);
        TextView cuotasPagadas = (TextView) dialogView.findViewById(R.id.cuotasPagadas);
        TextView diasAtrasados = (TextView) dialogView.findViewById(R.id.diasAtrasados);
        TextView cuotasAtrasadas = (TextView)dialogView.findViewById(R.id.cuotasAtrasadas);
        TextView valorMora = (TextView)dialogView.findViewById(R.id.valorMora);
        TextView abonoMora = (TextView)dialogView.findViewById(R.id.abonoMora);
        TextView costoPorDias = (TextView)dialogView.findViewById(R.id.valorPorDiasAtrasados);


        datos = OperacionesBaseDatos
                .obtenerInstancia(context);


        cursor = datos.ObtenerInfoPrestamoPorId(idPrestamo);
        DatabaseUtils.dumpCursor(datos.ObtenerInfoPrestamoPorId(idPrestamo));

        DatabaseUtils.dumpCursor(datos.ObtenerInfoPrestamoDiasAtrasadoAndMora(idPrestamo));
        montoPrestamo.setText("RD$ "+cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CAPITAL)));
        String capital = cursor.getString(cursor.getColumnIndex(Contract.PrestamoDetalle.CAPITAL));
        String interes = cursor.getString(cursor.getColumnIndex(Contract.PrestamoDetalle.INTERES));
        int valorCuotas = Integer.parseInt(capital)+Integer.parseInt(interes);
        valorCuota.setText(String.valueOf(valorCuotas));

        String plaso = cursor.getString(cursor.getColumnIndex(Contract.Prestamo.PLAZO));
        switch (plaso){
            case "D":
                plasoCuota.setText(cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CUOTAS)) + " DIAS");
                break;
            case "S":
                plasoCuota.setText(cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CUOTAS)) + " SEMANA");
                break;
            case "Q":
                plasoCuota.setText(cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CUOTAS)) + " QUINCENA");
                break;
            case "M":
                plasoCuota.setText(cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CUOTAS)) + " MESES");
                break;
            default:
                plasoCuota.setText(" ");
        }

        cuotasPagadas.setText(cursor.getString(cursor.getColumnIndex("CuotaPagada"))+"/"+
                cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CUOTAS)));
        String capitalPrestamo = cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CAPITAL));
        String porciento = cursor.getString(cursor.getColumnIndex(Contract.Prestamo.PORCIENTO_MORA));
        int porcientoMora = (Integer.parseInt(capitalPrestamo)*Integer.parseInt(porciento))/1000;
        costoPorDias.setText(String.valueOf(porcientoMora));


        cursor1 = datos.ObtenerInfoPrestamoDiasAtrasadoAndMora(idPrestamo);
        diasAtrasados.setText(cursor1.getString(cursor1.getColumnIndex("DiasAtrasados")));
        cuotasAtrasadas.setText(cursor1.getString(cursor1.getColumnIndex("CuotasAtrasadas")));
        Log.e("Valor",cursor1.getString(cursor1.getColumnIndex("ValorMora")));
        valorMora.setText(cursor1.getString(cursor1.getColumnIndex("ValorMora")));
        abonoMora.setText(cursor1.getString(cursor1.getColumnIndex("AbonoMora")));


        buttonPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast(getString(R.string.pos_button));
                // Dismiss the DialogFragment (remove it from view)
                dismiss();
            }
        });

        // "Cancel" button
       /* Button buttonNeg = (Button) dialogView.findViewById(R.id.neg_button);
        buttonNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getString(R.string.neg_button));
                // If shown as dialog, cancel the dialog (cancel --> dismiss)
                if (getShowsDialog())
                    getDialog().cancel();
                    // If shown as Fragment, dismiss the DialogFragment (remove it from view)
                else
                    dismiss();
            }
        });*/

        return dialogView;
    }

    // If shown as dialog, set the width of the dialog window
    // onCreateView --> onActivityCreated -->  onViewStateRestored --> onStart --> onResume
    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume");
        if (getShowsDialog()) {
            // Set the width of the dialog to the width of the screen in portrait mode
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            int dialogWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
            getDialog().getWindow().setLayout(dialogWidth, WRAP_CONTENT);
        }
    }

    private void showToast(String buttonName) {
        Toast.makeText(getActivity(), "Clicked on \"" + buttonName + "\"", Toast.LENGTH_SHORT).show();
    }

    // If dialog is cancelled: onCancel --> onDismiss
    @Override
    public void onCancel(DialogInterface dialog) {
        Log.v(LOG_TAG, "onCancel");
    }

    // If dialog is cancelled: onCancel --> onDismiss
    // If dialog is dismissed: onDismiss
    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.v(LOG_TAG,"onDismiss");
    }


}
