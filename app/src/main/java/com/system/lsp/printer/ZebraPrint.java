package com.system.lsp.printer;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.EditText;

import com.system.lsp.modelo.Recibo;
import com.system.lsp.modelo.RecieptDetail;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utils.BluetoothPermissionHelper;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

import androidx.core.app.ActivityCompat;


/**
 * Created by Suarez on 23/01/2018.
 */

public class ZebraPrint {

    private Context _context;
    private List<HashMap<String, String>> detalles;// Conceptos de pago
    private String[] parametros;// TRN Conceptopago etc... etc...
    //private List<NameValuePair> param_post; //Parametros por post

    private String fechaPago;
    private String numPrestamo;
    private String nombreCliente;
    private String detalleFactura = "";
    private Double totalPagado;
    private String nombreCobrador;
    private Double totalMora;
    private String telefono;

    private final String TAG_IMPRESION = "Normal";
    private final String TAG_REIMPRESION = "Reimprimir";

    private final Integer caracteres_X_linea = 47;
    private final String Final_Linea = "\r\n";
    private final String linea_mitad = "- - - - - - - - - - - - ";
    private final String linea_entera = "- - - - - - - - - - - - - - - - - - - - - - - -";

    //private Cliente cliente = new Cliente();

    private String imp_dat = "";
    private Recibo receipt;

    public ZebraPrint(Context context, String TAG, String nombreCobrador, String fechaCobro,
                      String detalleCobro, Double totalPagado) {
        this._context = context;
        this.imp_dat = TAG;
        this.nombreCobrador = nombreCobrador;
        this.fechaPago = fechaCobro;
        this.detalleFactura = detalleCobro;
        this.totalPagado = totalPagado;
    }

    public ZebraPrint(Context context, String TAG, String fechaPago, String numPrestamo, String nombreCliente,
                      String detalleFactura, Double totalPagado, Double totalMora, String nombreCobrador, String telefono) {
        this._context = context;
        this.imp_dat = TAG;
        this.fechaPago = fechaPago;
        this.numPrestamo = numPrestamo;
        this.nombreCliente = nombreCliente;
        this.detalleFactura = detalleFactura;
        this.totalPagado = totalPagado;
        this.totalMora = totalMora;
        this.nombreCobrador = nombreCobrador;
        this.telefono = telefono;
    }


    /* public ZebraPrint(Context context,List<HashMap<String,String>> detalles, String[] parametros, List<NameValuePair> param_post, Cliente cl){
        _context = context;
        this.detalles = detalles;
        this.parametros = parametros;
        //this.param_post = param_post;
        //this.cliente = cl;
    }*/

    public ZebraPrint(Context context, Recibo receipt, String TAG) {
        this._context = context;
        this.receipt = receipt;
        this.imp_dat = TAG;
    }

    public ZebraPrint(Context _context) {
        this._context = _context;
    }

    public ZebraPrint(Context context, String dato) {
        this._context = context;
        this.imp_dat = dato;
    }

    public void probarlo() {
        new Thread(new Runnable() {
            public void run() {


                try {
                    Looper.prepare();
                    doConnectionTest(imp_dat);
                    Looper.loop();
                    Looper.myLooper().quit();
                } catch (InterruptedException e) {
                    Log.e("CONECT ERROR", "error conection");

                    e.printStackTrace();
                }

            }
        }).start();
    }


    private Connection printerConnection;
    private ZebraPrinter printer;


    @SuppressLint("MissingPermission")
    public ZebraPrinter connect() throws InterruptedException {
        setStatus("Connecting...", Color.YELLOW);
        printerConnection = null;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //((MainActivity)this._context).showAlert("NO BLUETOOH ADAPTER AVAIBLE!!");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            _context.startActivity(enableBluetooth);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.isEmpty()) {
            // TODO: 27/4/24  return with a msg saying no devices found
            return null;
        }


        for (BluetoothDevice device : pairedDevices) {
            if(device.getBluetoothClass().getDeviceClass() == 1664){
                printerConnection = new BluetoothConnection(device.getAddress());
            }
        }


        try {
            printerConnection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException | NullPointerException e) {
            //((MainActivity)this._context).showAlert("FAILD, CONECTING TO PRINTER");
            setStatus("Comm Error! Disconnecting", Color.RED);
            sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (!printerConnection.isConnected()) {
            // TODO: 27/4/24 handle not connected printer
        }


        try {
            printer = ZebraPrinterFactory.getInstance(printerConnection);
        } catch (ConnectionException | ZebraPrinterLanguageUnknownException e) {
            //setStatus("Unknown Printer Language", Color.RED);
            printer = null;
            sleep(1000);
            disconnect();
        }

        return printer;
    }

    public void disconnect() {
        try {
            setStatus("Disconnecting", Color.RED);
            if (printerConnection != null) {
                printerConnection.close();
            }
            setStatus("Not Connected", Color.RED);
        } catch (ConnectionException e) {
            setStatus("COMM Error! Disconnected", Color.RED);
        } finally {
            //enableTestButton(true);
        }
    }

    private void setStatus(final String statusMessage, final int color) {
        // TODO: 27/4/24 handle states with a toast
    }

    private void doConnectionTest(String opcion) throws InterruptedException {
        printer = connect();
        if (printer != null) {
            sendTestLabel(opcion);
        } else {
            disconnect();
        }
    }

    private void sendTestLabel(String opcion) throws InterruptedException {
        try {
            Log.d("Impresion", "Tag Impresion que llego: " + opcion + " El valor a comprar ");


            //printer.Connection.write Recibe un byte[]
            switch (opcion) {
                case "prueba": {
                    List<RecieptDetail> details = new ArrayList<>();

                    details.add(new RecieptDetail("Pago Cuota(S) N.18/46",140.00));
                    details.add(new RecieptDetail("Pago Cuota(S) N.18/46",140.00));
                    details.add(new RecieptDetail("Pago Cuota(S) N.18/46",140.00));
                    details.add(new RecieptDetail("Pago Cuota(S) N.18/46",140.00));

                    byte[] document = new RecieptPrinterDocumentBuilder
                            .Builder()
                            .setZebraPrint(printer)
                            .setPrinterLanguage(printer.getPrinterControlLanguage())
                            .setDocumentType(RecieptPrinterDocumentBuilder.RecieptDocumentType.ORIGINAL_DOCUMENT)
                            .setCompanyName("PRESTAMOS LA SOLUCION HERNADEZ")
                            .setAddress("SFM")
                            .setPhone("809-244-3787")
                            .setRecieptDate("2024-04-26 08:21:06")
                            .setLoanId("4009")
                            .setCustomerFullName("Fulano de Tal")
                            .setRecieptDetails(details)
                            .setUserName("Raul Hernandez")
                            .setFinalNote("No somos Responsables de dinero entragdo sin recibo firmado")
                            .build()
                            .getDocument();


//                    printerConnection.write(document);
//                    printerConnection.write(test());
                    break;

                }

                case "imprimir": {
                    printerConnection.write(imprmir());
                    break;

                }

                case "reimprimir": {
                    printerConnection.write(imprmir());
                    break;

                }
                case "imprimirCuadre": {
                    printerConnection.write(imprmirCuadre());
                    break;

                }
            }

            setStatus("Sending Data", Color.BLUE);

            sleep(1500);
            if (printerConnection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) printerConnection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                sleep(500);
            }
            /*if(this._context instanceof MainActivity)
                ((MainActivity)this._context).finish();

            if(this._context instanceof Pay_Credits)
                ((Pay_Credits)this._context).finish();*/

        } catch (ConnectionException e) {
            //((MainActivity)this._context).showAlert("ERROR PRINTER");
            setStatus(e.getMessage(), Color.RED);
        } finally {
            disconnect();
        }
    }

    private byte[] printZPL(String bodyContent){
        return String.format("^XA^FO20,20^A0N,25,25^FD%s^FS^XZ", bodyContent).getBytes();
    }

    private byte[] printCPCL(String bodyContent){
        return String.format("! 0 200 200 1000 1\r\nTEXT 4 0 20 20 %s\r\nFORM\r\nPRINT\r\n", bodyContent).getBytes();
    }



    private byte[] print(String bodyContent){
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        if (printerLanguage == PrinterLanguage.ZPL) {
            return printZPL(bodyContent.replace("\r\n", "^FH^FD0D0A"));
        }
        if (printerLanguage == PrinterLanguage.CPCL){
            return printCPCL(bodyContent);
        }
        return null;
    }




    private byte[] test() {
        return print("Imprimiendo prueba, funciona!");
    }

    private String imprime_detalles(String string_primario, String[] arreglostringConceptoPago, int caracteres_X_linea, String Final_Linea) {

        String string_primario_copia = string_primario;
        if (arreglostringConceptoPago == null) return string_primario;

        int totalLineas = arreglostringConceptoPago.length;

        try {
            if (2 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[0], caracteres_X_linea, arreglostringConceptoPago[1]) + Final_Linea;
            }
            if (4 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[2], caracteres_X_linea, arreglostringConceptoPago[3]) + Final_Linea;
            }
            if (6 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[4], caracteres_X_linea, arreglostringConceptoPago[5]) + Final_Linea;
            }
            if (8 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[6], caracteres_X_linea, arreglostringConceptoPago[7]) + Final_Linea;
            }
            if (10 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[8], caracteres_X_linea, arreglostringConceptoPago[9]) + Final_Linea;
            }
            if (12 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[10], caracteres_X_linea, arreglostringConceptoPago[11]) + Final_Linea;
            }
            if (14 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[12], caracteres_X_linea, arreglostringConceptoPago[13]) + Final_Linea;
            }
            if (16 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[14], caracteres_X_linea, arreglostringConceptoPago[15]) + Final_Linea;
            }
            if (18 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[16], caracteres_X_linea, arreglostringConceptoPago[17]) + Final_Linea;
            }
            if (20 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[18], caracteres_X_linea, arreglostringConceptoPago[19]) + Final_Linea;
            }
            if (22 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[20], caracteres_X_linea, arreglostringConceptoPago[21]) + Final_Linea;
            }
            if (24 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[22], caracteres_X_linea, arreglostringConceptoPago[23]) + Final_Linea;
            }
            if (26 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[24], caracteres_X_linea, arreglostringConceptoPago[25]) + Final_Linea;
            }
            if (28 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[26], caracteres_X_linea, arreglostringConceptoPago[27]) + Final_Linea;
            }
            if (30 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[28], caracteres_X_linea, arreglostringConceptoPago[29]) + Final_Linea;
            }
            if (32 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[30], caracteres_X_linea, arreglostringConceptoPago[31]) + Final_Linea;
            }
            if (34 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[32], caracteres_X_linea, arreglostringConceptoPago[33]) + Final_Linea;
            }
            if (36 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[34], caracteres_X_linea, arreglostringConceptoPago[35]) + Final_Linea;
            }
            if (38 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[36], caracteres_X_linea, arreglostringConceptoPago[37]) + Final_Linea;
            }
            if (40 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[38], caracteres_X_linea, arreglostringConceptoPago[39]) + Final_Linea;
            }
            if (42 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[40], caracteres_X_linea, arreglostringConceptoPago[41]) + Final_Linea;
            }
            if (44 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[42], caracteres_X_linea, arreglostringConceptoPago[43]) + Final_Linea;
            }
            if (46 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[44], caracteres_X_linea, arreglostringConceptoPago[45]) + Final_Linea;
            }
            if (48 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[46], caracteres_X_linea, arreglostringConceptoPago[47]) + Final_Linea;
            }
            if (50 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[48], caracteres_X_linea, arreglostringConceptoPago[49]) + Final_Linea;
            }
            if (52 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[50], caracteres_X_linea, arreglostringConceptoPago[51]) + Final_Linea;
            }
            if (54 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[52], caracteres_X_linea, arreglostringConceptoPago[53]) + Final_Linea;
            }
            if (56 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[54], caracteres_X_linea, arreglostringConceptoPago[55]) + Final_Linea;
            }
            if (58 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[56], caracteres_X_linea, arreglostringConceptoPago[57]) + Final_Linea;
            }
            if (60 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[58], caracteres_X_linea, arreglostringConceptoPago[59]) + Final_Linea;
            }
            if (62 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[60], caracteres_X_linea, arreglostringConceptoPago[61]) + Final_Linea;
            }
            if (64 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[62], caracteres_X_linea, arreglostringConceptoPago[63]) + Final_Linea;
            }
            if (66 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[64], caracteres_X_linea, arreglostringConceptoPago[65]) + Final_Linea;
            }
            if (68 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[66], caracteres_X_linea, arreglostringConceptoPago[67]) + Final_Linea;
            }
            if (70 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[68], caracteres_X_linea, arreglostringConceptoPago[69]) + Final_Linea;
            }
            if (72 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[70], caracteres_X_linea, arreglostringConceptoPago[71]) + Final_Linea;
            }
            if (74 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[72], caracteres_X_linea, arreglostringConceptoPago[73]) + Final_Linea;
            }
            if (76 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[74], caracteres_X_linea, arreglostringConceptoPago[75]) + Final_Linea;
            }

            if (78 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[76], caracteres_X_linea, arreglostringConceptoPago[77]) + Final_Linea;
            }
            if (80 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[78], caracteres_X_linea, arreglostringConceptoPago[79]) + Final_Linea;
            }
            if (82 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[80], caracteres_X_linea, arreglostringConceptoPago[81]) + Final_Linea;
            }
            if (84 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[82], caracteres_X_linea, arreglostringConceptoPago[83]) + Final_Linea;
            }
            if (86 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[84], caracteres_X_linea, arreglostringConceptoPago[85]) + Final_Linea;
            }
            if (88 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[86], caracteres_X_linea, arreglostringConceptoPago[87]) + Final_Linea;
            }
            if (90 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[88], caracteres_X_linea, arreglostringConceptoPago[89]) + Final_Linea;
            }
            if (92 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[90], caracteres_X_linea, arreglostringConceptoPago[91]) + Final_Linea;
            }
            if (94 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[92], caracteres_X_linea, arreglostringConceptoPago[93]) + Final_Linea;
            }
            if (96 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[94], caracteres_X_linea, arreglostringConceptoPago[95]) + Final_Linea;
            }
            if (98 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[96], caracteres_X_linea, arreglostringConceptoPago[97]) + Final_Linea;
            }
            if (100 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[98], caracteres_X_linea, arreglostringConceptoPago[99]) + Final_Linea;
            }

        } catch (Exception e) {
            return string_primario_copia;
        }


        return string_primario;

    }

    private byte[] imprmir() {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
                    "! U1 CONTRAST 3" + Final_Linea +
                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
                    Resolve.alinea_centro("PRESTAMOS LA SOLUCION HERNADEZ", caracteres_X_linea) + Final_Linea +
                    // Resolve.alinea_centro("Una empresa al servicio de su gente", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("AL INSTANTE DONDE SU DINERO.", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("LE SERA DE BUEN PROVECHO", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("Tel: 829-201-3448", caracteres_X_linea) + Final_Linea +

                    Resolve.dos_columna("", caracteres_X_linea, "") + Final_Linea +
                    //"RNC: 104-01619-1" + Final_Linea +
                    Resolve.alinea_centro("*RECIBO DE PAGO*", caracteres_X_linea) + Final_Linea +
                    //"#FATURA: " + "10" + Final_Linea + //TRN, detalles = Resultado, parametros[0] = TRN
                    //"Referencia: " + detalles.get(0).get(parametros[0]) + Final_Linea +
                    "Fecha: " + fechaPago + Final_Linea + //Fecha
                    linea_entera + Final_Linea +
                    "#PRESTAMO: " + numPrestamo + Final_Linea +
                    nombreCliente + Final_Linea +
                    //"BALANCE ANTERIOR: " + "8000" + Final_Linea +
                    //"DIRECCION: " + "C/JUAN DE DIOS VENTURA SIMO #1, SECTOR SALVADOR THEN, CIUDAD SAN FRANCISCO DE MACOIRS || FRENTE AL COMAADO EL AGUILA" + Final_Linea +
                    linea_entera + Final_Linea +
                    Resolve.dos_columna("Concepto", caracteres_X_linea, "Monto Pagado") + Final_Linea +
                    linea_entera + Final_Linea;

            //detalles.get(0).get(parametros[1]).toString() CARGO NOVIEMBRE 2014; 650.00; CARGO DICIEMBRE 2014; 650.00;
                /* cuota 1; 46;
                Devuelve la misma sentencia con los detalles anexados en dado caso de fallar devuelve el mismo string sin los anexos
                 */
            //cpclConfigLabel = imprime_detalles(cpclConfigLabel,SeparaLineas("Saldo Cuota(s) No. 10/46;650.00;Saldo Cuota(s) No. 11/46;650.00;", ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
            //Log.e("DETALLE",detalleFactura);
            cpclConfigLabel = imprime_detalles(cpclConfigLabel, SeparaLineas(detalleFactura, ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
            Log.e("TOTAL-MORA", String.valueOf(totalMora));
            //Double f = Double.valueOf(cliente.balance) -  Double.valueOf(param_post.get(0).getValue());
            cpclConfigLabel += linea_entera + Final_Linea +
                    // Resolve.dos_columna("TOTAL P-MORA", caracteres_X_linea, String.valueOf(totalMora)) + Final_Linea +
                    Resolve.dos_columna("TOTAL PAGADO", caracteres_X_linea, String.valueOf(totalPagado)) + Final_Linea +
                    //Resolve.dos_columna("TOTAL PENDIENTE", caracteres_X_linea,"6700") + Final_Linea +
                    Resolve.alinea_centro(nombreCobrador, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro(telefono, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro(linea_mitad, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("LE ATENDIO", caracteres_X_linea) + Final_Linea +
                    // Resolve.alinea_centro("***No somos responsable de dinero entregado sin recibo firmado ***", caracteres_X_linea) + Final_Linea +
                    " " + Final_Linea;


            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;

    }


    private byte[] imprmirCuadre() {

        String bodyContent = Resolve.alinea_centro("PRESTAMOS LA SOLUCION HERNADEZ", caracteres_X_linea) + Final_Linea +
                // Resolve.alinea_centro("Una empresa al servicio de su gente", caracteres_X_linea) + Final_Linea +
                Resolve.alinea_centro("AL INSTANTE DONDE SU DINERO.", caracteres_X_linea) + Final_Linea +
                Resolve.alinea_centro("LE SERA DE BUEN PROVECHO", caracteres_X_linea) + Final_Linea +
                Resolve.alinea_centro("Tel: 829-201-3448", caracteres_X_linea) + Final_Linea +

                Resolve.dos_columna("", caracteres_X_linea, "") + Final_Linea +
                //"RNC: 104-01619-1" + Final_Linea +
                Resolve.alinea_centro("*CUADRE COBRADOR:*", caracteres_X_linea) + Final_Linea +
                Resolve.alinea_centro(nombreCobrador, caracteres_X_linea) + Final_Linea +
                //"#FATURA: " + "10" + Final_Linea + //TRN, detalles = Resultado, parametros[0] = TRN
                //"Referencia: " + detalles.get(0).get(parametros[0]) + Final_Linea +
                "Fecha: " + fechaPago + Final_Linea + //Fecha
                // "#PRESTAMO: "  + numPrestamo + Final_Linea +
                // nombreCliente + Final_Linea +
                //"BALANCE ANTERIOR: " + "8000" + Final_Linea +
                //"DIRECCION: " + "C/JUAN DE DIOS VENTURA SIMO #1, SECTOR SALVADOR THEN, CIUDAD SAN FRANCISCO DE MACOIRS || FRENTE AL COMAADO EL AGUILA" + Final_Linea +
                linea_entera + Final_Linea +
                Resolve.dos_columna("Prestamo", caracteres_X_linea, "Monto Cobrado") + Final_Linea +
                linea_entera + Final_Linea;


        bodyContent = imprime_detalles(bodyContent, SeparaLineas(detalleFactura, ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
        Log.e("TOTAL-MORA", String.valueOf(totalMora));
        //Double f = Double.valueOf(cliente.balance) -  Double.valueOf(param_post.get(0).getValue());
        bodyContent += linea_entera + Final_Linea +
                //Resolve.dos_columna("TOTAL P-MORA", caracteres_X_linea, String.valueOf(totalMora)) + Final_Linea +
                Resolve.dos_columna("TOTAL COBRADO", caracteres_X_linea, String.valueOf(totalPagado)) + Final_Linea +
                //Resolve.dos_columna("TOTAL PENDIENTE", caracteres_X_linea,"6700") + Final_Linea +
                // Resolve.alinea_centro(nombreCobrador, caracteres_X_linea) + Final_Linea +
                //Resolve.alinea_centro(telefono, caracteres_X_linea) + Final_Linea +
                // Resolve.alinea_centro(linea_mitad, caracteres_X_linea) + Final_Linea +
                // Resolve.alinea_centro("LE ATENDIO", caracteres_X_linea) + Final_Linea +
                //  Resolve.alinea_centro("***No somos responsable de dinero entregado sin recibo firmado ***", caracteres_X_linea) + Final_Linea +
                " " + Final_Linea;

        return print(bodyContent);


//        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
//
//        byte[] configLabel = null;
//        if (printerLanguage == PrinterLanguage.ZPL) {
//            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
//        } else if (printerLanguage == PrinterLanguage.CPCL) {
//            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
//                    "! U1 CONTRAST 3" + Final_Linea +
//                    "! U1 CENTER" + Final_Linea; //No funciona D:!!
//
//
//            //detalles.get(0).get(parametros[1]).toString() CARGO NOVIEMBRE 2014; 650.00; CARGO DICIEMBRE 2014; 650.00;
//                /* cuota 1; 46;
//                Devuelve la misma sentencia con los detalles anexados en dado caso de fallar devuelve el mismo string sin los anexos
//                 */
//            //cpclConfigLabel = imprime_detalles(cpclConfigLabel,SeparaLineas("Saldo Cuota(s) No. 10/46;650.00;Saldo Cuota(s) No. 11/46;650.00;", ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
//            //Log.e("DETALLE",detalleFactura);
//            cpclConfigLabel = imprime_detalles(cpclConfigLabel, SeparaLineas(detalleFactura, ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
//            Log.e("TOTAL-MORA", String.valueOf(totalMora));
//            //Double f = Double.valueOf(cliente.balance) -  Double.valueOf(param_post.get(0).getValue());
//            cpclConfigLabel += linea_entera + Final_Linea +
//                    //Resolve.dos_columna("TOTAL P-MORA", caracteres_X_linea, String.valueOf(totalMora)) + Final_Linea +
//                    Resolve.dos_columna("TOTAL COBRADO", caracteres_X_linea, String.valueOf(totalPagado)) + Final_Linea +
//                    //Resolve.dos_columna("TOTAL PENDIENTE", caracteres_X_linea,"6700") + Final_Linea +
//                    // Resolve.alinea_centro(nombreCobrador, caracteres_X_linea) + Final_Linea +
//                    //Resolve.alinea_centro(telefono, caracteres_X_linea) + Final_Linea +
//                    // Resolve.alinea_centro(linea_mitad, caracteres_X_linea) + Final_Linea +
//                    // Resolve.alinea_centro("LE ATENDIO", caracteres_X_linea) + Final_Linea +
//                    //  Resolve.alinea_centro("***No somos responsable de dinero entregado sin recibo firmado ***", caracteres_X_linea) + Final_Linea +
//                    " " + Final_Linea;
//
//
//            configLabel = cpclConfigLabel.getBytes();
//        }
//        return configLabel;

    }

   /* private byte[] reimpresion() {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
                    "! U1 CONTRAST 3" + Final_Linea +
                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
                    Resolve.alinea_centro("TELEOPERADORA DEL NORDESTE (TELENORD)", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("Una empresa al servicio de su gente", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("Av. Frank Grullon #5 San Francisco de Macoris", caracteres_X_linea) + Final_Linea +
                    Resolve.dos_columna("Tel: 809-588-6238", caracteres_X_linea, "Fax: 809-588-0105") + Final_Linea +
                    "RNC: 104-01619-1" + Final_Linea +
                    Resolve.alinea_centro("R E C I B O", caracteres_X_linea) + Final_Linea +
                    "Numero: " + detalles.get(0).get(parametros[0]) + Final_Linea + //TRN
                    //"Referencia: " + detalles.get(0).get(parametros[0]) + Final_Linea +
                    "Fecha: " + detalles.get(0).get(parametros[3]) + Final_Linea + //Fecha
                    linea_entera + Final_Linea +
                    "CONTRATO: "  + cliente.contrato + Final_Linea +
                    cliente.nombre + Final_Linea +
                    "BALANCE ANTERIOR: " + detalles.get(0).get(parametros[5]).toString() + Final_Linea +
                    "DIRECCION: " + cliente.direccion + Final_Linea +
                    linea_entera + Final_Linea +
                    Resolve.dos_columna("Concepto", caracteres_X_linea, "Monto Pagado") + Final_Linea +
                    linea_entera + Final_Linea;

            //detalles.get(0).get(parametros[1]).toString() CARGO NOVIEMBRE 2014; 650.00; CARGO DICIEMBRE 2014; 650.00;
                /*
                Devuelve la misma sentencia con los detalles anexados en dado caso de fallar devuelve el mismo string sin los anexos
                 */
          /*  cpclConfigLabel = imprime_detalles(cpclConfigLabel,SeparaLineas(detalles.get(0).get(parametros[1]).toString(), ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
            cpclConfigLabel += linea_entera + Final_Linea +
                    Resolve.dos_columna("TOTAL PAGADO", caracteres_X_linea, detalles.get(0).get(parametros[6]).toString()) + Final_Linea +
                    Resolve.dos_columna("TOTAL PENDIENTE", caracteres_X_linea,"8050" + Final_Linea +
                    Resolve.alinea_centro(detalles.get(0).get(parametros[4]), caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro(linea_mitad, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("LE ATENDIO", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("*** ABONO A FACTURA NO EVITA CORTE ***", caracteres_X_linea) + Final_Linea +
                    " " + Final_Linea;



            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;

    }*/

    public static String[] SeparaLineas(String Texto, String Delimitador) {
        return Texto.split(Delimitador);
    }


//TODO: Anexar bien la impresion

}
