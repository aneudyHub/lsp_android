package com.herprogramacion.peopleapp.utilidades;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import com.herprogramacion.peopleapp.modelo.Recibo;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;


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
    private String detalleFactura="";
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

    public ZebraPrint(Context context, String TAG,String fechaPago, String numPrestamo, String nombreCliente,
                      String detalleFactura, Double totalPagado,Double totalMora, String nombreCobrador,String telefono) {
        this._context = context;
        this.imp_dat=TAG;
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

    public ZebraPrint(Context context, Recibo receipt, String TAG){
        this._context = context;
        this.receipt=receipt;
        this.imp_dat=TAG;
    }

    public ZebraPrint(Context _context) {
        this._context = _context;
    }

    public ZebraPrint(Context context, String dato){
        this._context = context;
        this.imp_dat = dato;
    }

    public  void probarlo(){
        new Thread(new Runnable() {
            public void run() {


                try {
                    Looper.prepare();
                    doConnectionTest(imp_dat);
                    Looper.loop();
                    Looper.myLooper().quit();
                } catch (InterruptedException e) {
                    Log.e("CONECT ERROR","error conection");

                    e.printStackTrace();
                }

            }
        }).start();
    }

    /*protected void imprimelo(){
        Log.d("Impresora", "Imprimiendo Datos");

        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                doConnectionTest(TAG_IMPRESION);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    protected void reimprimir(){
        Log.d("Impresora", "Reimprimiendo Datos");

        new Thread(new Runnable() {
            public void run() {
                Log.e("Ereimprimir",TAG_REIMPRESION);
                Looper.prepare();
                doConnectionTest(TAG_REIMPRESION);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }*/


    private Connection printerConnection;
    private ZebraPrinter printer;

        /*@Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.connection_screen_with_status);

            ipDNSAddress = (EditText) this.findViewById(R.id.ipAddressInput);
            ipDNSAddress.setText(SettingsHelper.getIp(this));

            portNumber = (EditText) this.findViewById(R.id.portInput);
            portNumber.setText(SettingsHelper.getPort(this));

            macAddress = (EditText) this.findViewById(R.id.macInput);
            macAddress.setText(SettingsHelper.getBluetoothAddress(this));

            statusField = (TextView) this.findViewById(R.id.statusText);
            btRadioButton = (RadioButton) this.findViewById(R.id.bluetoothRadio);

            testButton = (Button) this.findViewById(R.id.testButton);
            testButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    new Thread(new Runnable() {
                        public void run() {
                            enableTestButton(false);
                            Looper.prepare();
                            doConnectionTest();
                            Looper.loop();
                            Looper.myLooper().quit();
                        }
                    }).start();
                }
            });

            RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.bluetoothRadio) {
                        toggleEditField(macAddress, true);
                        toggleEditField(portNumber, false);
                        toggleEditField(ipDNSAddress, false);
                    } else {
                        toggleEditField(portNumber, true);
                        toggleEditField(ipDNSAddress, true);
                        toggleEditField(macAddress, false);
                    }
                }
            });
        }*/

    private void toggleEditField(EditText editText, boolean set) {
        /*
         * Note: Disabled EditText fields may still get focus by some other means, and allow text input.
         *       See http://code.google.com/p/android/issues/detail?id=2771
         */
        editText.setEnabled(set);
        editText.setFocusable(set);
        editText.setFocusableInTouchMode(set);
    }

        /*@Override
        protected void onStop() {
            super.onStop();
            if (printerConnection != null && printerConnection.isConnected()) {
                disconnect();
            }
        }*/

        /*private void enableTestButton(final boolean enabled) {
            runOnUiThread(new Runnable() {
                public void run() {
                    testButton.setEnabled(enabled);
                }
            });
        }*/



    private boolean isBluetoothSelected() {
        return true;
    }

    public ZebraPrinter connect() throws InterruptedException {
        setStatus("Connecting...", Color.YELLOW);
        printerConnection = null;
        if (isBluetoothSelected()) {
            BluetoothDevice mmDevice = null;
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null){
                //((MainActivity)this._context).showAlert("NO BLUETOOH ADAPTER AVAIBLE!!");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                _context.startActivity(enableBluetooth);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("benn")) {
                        mmDevice = device;
                        printerConnection = new BluetoothConnection(mmDevice.getAddress());
                        break;
                    }else if (device.getName().equals("RMI")) {
                        mmDevice = device;
                        printerConnection = new BluetoothConnection(mmDevice.getAddress());
                        break;
                    } else if (device.getName().equals("RM1")) {
                    mmDevice = device;
                    printerConnection = new BluetoothConnection(mmDevice.getAddress());
                    break;
                }
                }
            }

            if(printerConnection==null)
                printerConnection = new BluetoothConnection("00:22:58:01:6B:50");

            //SettingsHelper.saveBluetoothAddress(this._context, "00:22:58:01:6B:50");
        } else {
            Log.e("CONECT ERROR","error conection");

            return null;
        }

        try {
            printerConnection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException e)
        {
            //((MainActivity)this._context).showAlert("FAILD, CONECTING TO PRINTER");
            setStatus("Comm Error! Disconnecting", Color.RED);
            sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
                //setStatus("Determining Printer Language", Color.YELLOW);
                PrinterLanguage pl = printer.getPrinterControlLanguage();
                //setStatus("Printer Language " + pl, Color.BLUE);
            } catch (ConnectionException e) {
                //setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                sleep(1000);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                //setStatus("Unknown Printer Language", Color.RED);
                //((MainActivity)this._context).showAlert("UNKNOW PRINTER LANGUAGE");
                printer = null;
                sleep(1000);
                disconnect();
            }
        }else{
            /*if(this._context instanceof MainActivity){
                ((MainActivity)this._context).showAlert("ERROR CONECTION TO PRINTER");
            }

            if(this._context instanceof Operations){
                ((Operations)this._context).showAlert("ERROR CONECTION TO PRINTER");
            }

            if(this._context instanceof Pay_Credits){
                ((Pay_Credits)this._context).showAlert("ERROR CONECTION TO PRINTER");
            }*/
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
           /* runOnUiThread(new Runnable() {
                public void run() {
                    statusField.setBackgroundColor(color);
                    statusField.setText(statusMessage);
                }
            });
            DemoSleeper.sleep(1000);*/
    }

        /*private String getMacAddressFieldText() {
            return macAddress.getText().toString();
        }

        private String getTcpAddress() {
            return ipDNSAddress.getText().toString();
        }

        private String getTcpPortNumber() {
            return portNumber.getText().toString();
        }*/

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
            switch(opcion) {
//                case TAG_IMPRESION :
//                    Log.d("Impresion", "Impresion Normal");
//                    printerConnection.write(openbravoFactura()); //
//                    break;
//                case TAG_REIMPRESION :
//                    Log.d("Impresion", "Cambie a Reimprimir");
//                    printerConnection.write(openbravoFactura_reimprimir());
//                    break;
//                case "Listo" :{
//                    printerConnection.write(openbravoFactura());
//                    break;
//                }
//                case TAG_PAGO:{
//                    printerConnection.write(openbravoPago());
//                    break;
//                }
//                case TAG_PAGO_REIMPRESION:{
//                    printerConnection.write(openbravoPago_Reprint());
//                    break;
//                }

                case "prueba":{
                    printerConnection.write(test());
                    break;

                }


                case "imprimir":{
                    printerConnection.write(imprmir());
                    break;

                }


                case "reimprimir":{
                    printerConnection.write(imprmir());
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

    /*
    * Returns the command for a test label depending on the printer control language
    * The test label is a box with the word "TEST" inside of it
    *
    * _________________________
    * |                       |
    * |                       |
    * |        TEST           |
    * |                       |
    * |                       |
    * |_______________________|
    *
    *
    */


    private byte[] test(){
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
                    "! U1 CONTRAST 3" + Final_Linea +
                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
                    "funcionaaa" + Final_Linea;
            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;
    }



    private byte[] getConfigLabel() {
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;
    }

    private byte[] printeo() {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 210 1\r\n"
                    + "SETMAG 1 2\r\n" +
                    "TEXT 5 0 0 30 TELEOPERADORA Teleoperadora 5 0\r\n"
                    + "TEXT 7 0 0 60 TELEOPERADORA Teleoperadora 7 0\r\n"
                    + "SETMAG 0 0\r\n" +
                    "PRINT\r\n";
            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;

    }

    private String imprime_detalles(String string_primario, String[] arreglostringConceptoPago, int caracteres_X_linea, String Final_Linea){

        String string_primario_copia = string_primario;
        if (arreglostringConceptoPago == null) return string_primario;

        int totalLineas = arreglostringConceptoPago.length;

        try {
            if (2 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[0], caracteres_X_linea, arreglostringConceptoPago[1]) + Final_Linea;
            }
            if (4 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[2], caracteres_X_linea, arreglostringConceptoPago[3]) + Final_Linea;
            }
            if (6 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[4], caracteres_X_linea, arreglostringConceptoPago[5]) + Final_Linea;
            }
            if (8 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[6], caracteres_X_linea, arreglostringConceptoPago[7]) + Final_Linea;
            }
            if (10 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[8], caracteres_X_linea, arreglostringConceptoPago[9]) + Final_Linea;
            }
            if (12 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[10], caracteres_X_linea, arreglostringConceptoPago[11]) + Final_Linea;
            }
            if (14 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[12], caracteres_X_linea, arreglostringConceptoPago[13]) + Final_Linea;
            }
            if (16 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[14], caracteres_X_linea, arreglostringConceptoPago[15]) + Final_Linea;
            }
            if (18 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[16], caracteres_X_linea, arreglostringConceptoPago[17]) + Final_Linea;
            }
            if (20 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[18], caracteres_X_linea, arreglostringConceptoPago[19]) + Final_Linea;
            }
            if (22 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[20], caracteres_X_linea, arreglostringConceptoPago[21]) + Final_Linea;
            }
            if (24 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[22], caracteres_X_linea, arreglostringConceptoPago[23]) + Final_Linea;
            }
            if (26 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[24], caracteres_X_linea, arreglostringConceptoPago[25]) + Final_Linea;
            }
            if (28 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[26], caracteres_X_linea, arreglostringConceptoPago[27]) + Final_Linea;
            }
            if (30 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[28], caracteres_X_linea, arreglostringConceptoPago[29]) + Final_Linea;
            }
            if (32 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[30], caracteres_X_linea, arreglostringConceptoPago[31]) + Final_Linea;
            }
            if (34 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[32], caracteres_X_linea, arreglostringConceptoPago[33]) + Final_Linea;
            }
            if (36 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[34], caracteres_X_linea, arreglostringConceptoPago[35]) + Final_Linea;
            }
            if (38 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[36], caracteres_X_linea, arreglostringConceptoPago[37]) + Final_Linea;
            }
            if (40 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[38], caracteres_X_linea, arreglostringConceptoPago[39]) + Final_Linea;
            }
            if (42 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[40], caracteres_X_linea, arreglostringConceptoPago[41]) + Final_Linea;
            }
            if (44 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[42], caracteres_X_linea, arreglostringConceptoPago[43]) + Final_Linea;
            }
            if (46 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[44], caracteres_X_linea, arreglostringConceptoPago[45]) + Final_Linea;
            }
            if (48 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[46], caracteres_X_linea, arreglostringConceptoPago[47]) + Final_Linea;
            }
            if (50 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[48], caracteres_X_linea, arreglostringConceptoPago[49]) + Final_Linea;
            }
            if (52 <= totalLineas) {
                string_primario += Resolve.dos_columna(arreglostringConceptoPago[50], caracteres_X_linea, arreglostringConceptoPago[51]) + Final_Linea;
            }
            if (54 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[52], caracteres_X_linea, arreglostringConceptoPago[53]) + Final_Linea;
            }
            if (56 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[54], caracteres_X_linea, arreglostringConceptoPago[55]) + Final_Linea;
            }
            if (58 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[56], caracteres_X_linea, arreglostringConceptoPago[57]) + Final_Linea;
            }
            if (60 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[58], caracteres_X_linea, arreglostringConceptoPago[59]) + Final_Linea;
            }
            if (62 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[60], caracteres_X_linea, arreglostringConceptoPago[61]) + Final_Linea;
            }
            if (64 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[62], caracteres_X_linea, arreglostringConceptoPago[63]) + Final_Linea;
            }
            if (66 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[64], caracteres_X_linea, arreglostringConceptoPago[65]) + Final_Linea;
            }
            if (68 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[66], caracteres_X_linea, arreglostringConceptoPago[67]) + Final_Linea;
            }
            if (70 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[68], caracteres_X_linea, arreglostringConceptoPago[69]) + Final_Linea;
            }
            if (72 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[70], caracteres_X_linea, arreglostringConceptoPago[71]) + Final_Linea;
            }
            if (74 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[72], caracteres_X_linea, arreglostringConceptoPago[73]) + Final_Linea;
            }
            if (76 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[74], caracteres_X_linea, arreglostringConceptoPago[75]) + Final_Linea;
            }

            if (78 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[76], caracteres_X_linea, arreglostringConceptoPago[77]) + Final_Linea;
            }
            if (80 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[78], caracteres_X_linea, arreglostringConceptoPago[79]) + Final_Linea;
            }
            if (82 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[80], caracteres_X_linea, arreglostringConceptoPago[81]) + Final_Linea;
            }
            if (84 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[82], caracteres_X_linea, arreglostringConceptoPago[83]) + Final_Linea;
            }
            if (86 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[84], caracteres_X_linea, arreglostringConceptoPago[85]) + Final_Linea;
            }
            if (88 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[86], caracteres_X_linea, arreglostringConceptoPago[87]) + Final_Linea;
            }
            if (90 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[88], caracteres_X_linea, arreglostringConceptoPago[89]) + Final_Linea;
            }
            if (92 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[90], caracteres_X_linea, arreglostringConceptoPago[91]) + Final_Linea;
            }
            if (94 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[92], caracteres_X_linea, arreglostringConceptoPago[93]) + Final_Linea;
            }
            if (96 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[94], caracteres_X_linea, arreglostringConceptoPago[95]) + Final_Linea;
            }
            if (98 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[96], caracteres_X_linea, arreglostringConceptoPago[97]) + Final_Linea;
            }
            if (100 <= totalLineas){
                string_primario +=  Resolve.dos_columna(arreglostringConceptoPago[98], caracteres_X_linea, arreglostringConceptoPago[99]) + Final_Linea;
            }

        } catch (Exception e){
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
                    Resolve.alinea_centro("INVERSIONES JOSE CASTILLO SANTOS", caracteres_X_linea) + Final_Linea +
                   // Resolve.alinea_centro("Una empresa al servicio de su gente", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("Av. Antonio Guzman Fernandez.", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("Plaza Dereck Mall #202", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("Tel: 809-244-3787", caracteres_X_linea) + Final_Linea +

                    Resolve.dos_columna("", caracteres_X_linea, "") + Final_Linea +
                    //"RNC: 104-01619-1" + Final_Linea +
                    Resolve.alinea_centro("*RECIBO DE PAGO*", caracteres_X_linea) + Final_Linea +
                    //"#FATURA: " + "10" + Final_Linea + //TRN, detalles = Resultado, parametros[0] = TRN
                    //"Referencia: " + detalles.get(0).get(parametros[0]) + Final_Linea +
                    "Fecha: " + fechaPago + Final_Linea + //Fecha
                    linea_entera + Final_Linea +
                    "#PRESTAMO: "  + numPrestamo + Final_Linea +
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
            cpclConfigLabel = imprime_detalles(cpclConfigLabel,SeparaLineas(detalleFactura, ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
            Log.e("TOTAL-MORA",String.valueOf(totalMora));
            //Double f = Double.valueOf(cliente.balance) -  Double.valueOf(param_post.get(0).getValue());
            cpclConfigLabel += linea_entera + Final_Linea +
                    Resolve.dos_columna("TOTAL P-MORA", caracteres_X_linea, String.valueOf(totalMora)) + Final_Linea +
                    Resolve.dos_columna("TOTAL PAGADO", caracteres_X_linea, String.valueOf(totalPagado)) + Final_Linea +
                    //Resolve.dos_columna("TOTAL PENDIENTE", caracteres_X_linea,"6700") + Final_Linea +
                    Resolve.alinea_centro(nombreCobrador, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro(telefono, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro(linea_mitad, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("LE ATENDIO", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("***No somos responsable de dinero entregado sin recibo firmado ***", caracteres_X_linea) + Final_Linea +
                    " " + Final_Linea;



            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;

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
                    Resolve.alinea_centro(linea_mitad, caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("LE ATENDIO", caracteres_X_linea) + Final_Linea +
                    Resolve.alinea_centro("*** ABONO A FACTURA NO EVITA CORTE ***", caracteres_X_linea) + Final_Linea +
                    " " + Final_Linea;



            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;

    }*/

    public static String[] SeparaLineas(String Texto, String Delimitador){
        return Texto.split(Delimitador);
    }


//TODO: Anexar bien la impresion

}
