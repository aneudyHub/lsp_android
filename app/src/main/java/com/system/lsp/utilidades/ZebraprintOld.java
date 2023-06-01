package com.system.lsp.utilidades;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import com.system.lsp.modelo.Recibo;
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
 * Created by Juan on 1/26/2015.
 */
public class ZebraprintOld {

    private Context _context;
    private List<HashMap<String, String>> detalles;// Conceptos de pago
    private String[] parametros;// TRN Conceptopago etc... etc...
    //private List<NameValuePair> param_post; //Parametros por post

    public static final String TAG_IMPRESION = "Normal";
    public static final String TAG_REIMPRESION = "Reimprimir";
    public static final String TAG_PAGO = "Pago";
    public static final String TAG_PAGO_REIMPRESION="pago_reimpresion";

    private final Integer caracteres_X_linea = 47;
    private final String Final_Linea = "\r\n";
    private final String linea_mitad = "- - - - - - - - - - - - ";
    private final String linea_entera = "- - - - - - - - - - - - - - - - - - - - - - - -";



    private String imp_dat = "";
    private Recibo receipt;




//    public ZebraprintOld(Context context, List<HashMap<String,String>> detalles, String[] parametros, List<NameValuePair> param_post, Cliente cl){
//        _context = context;
//        this.detalles = detalles;
//        this.parametros = parametros;
//        //this.param_post = param_post;
//        //this.cliente = cl;
//    }

    public ZebraprintOld(Context context, Recibo receipt, String TAG){
        this._context = context;
        this.receipt=receipt;
        this.imp_dat=TAG;
    }

    public ZebraprintOld(Context context, String dato){
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

//    protected void imprimelo(){
//        Log.d("Impresora", "Imprimiendo Datos");
//
//        new Thread(new Runnable() {
//            public void run() {
//                Looper.prepare();
//                doConnectionTest(TAG_IMPRESION);
//                Looper.loop();
//                Looper.myLooper().quit();
//            }
//        }).start();
//    }

//    protected void reimprimir(){
//        Log.d("Impresora", "Reimprimiendo Datos");
//
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Log.e("Ereimprimir",TAG_REIMPRESION);
//                    Looper.prepare();
//                    doConnectionTest(TAG_REIMPRESION);
//                    Looper.loop();
//                    Looper.myLooper().quit();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }


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
        //setStatus("Connecting...", Color.YELLOW);
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
                    if (device.getName().equals("rrrr")) {
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



//    private byte[] openbravoFactura_reimprimir() {
//
//        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
//
//        byte[] configLabel = null;
//        if (printerLanguage == PrinterLanguage.ZPL) {
//            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
//        } else if (printerLanguage == PrinterLanguage.CPCL) {
//            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
//                    "! U1 CONTRAST 3" + Final_Linea +
//                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
//                    Resolve.alinea_centro(Configs.COMPANY_NAME, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_SLOGAN, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_ADDRESS, caracteres_X_linea) + Final_Linea +
//                    Resolve.dos_columna("Tel: 929-244-7821", caracteres_X_linea, "Fax: 866-920-9064") + Final_Linea +
//                    Final_Linea+
//                    "-----COPY"+
//                    Final_Linea+
//                    Resolve.dos_columna("RECIPT:", 20, receipt.getId()) + Final_Linea +
//                    Resolve.dos_columna("DATE:",42, receipt.getDate()) + Final_Linea +
//                    Resolve.dos_columna("PAYMENT:", 20, receipt.getPayMethod())+
//                    Final_Linea+
//                    Resolve.dos_columna("CUSTOMER:",20,receipt.getClient())+
//                    Final_Linea +
//                    Final_Linea+
//                    Resolve.alinea_centro("R E C E I P T", caracteres_X_linea) +
//                    Final_Linea+
//                    Final_Linea+
//                    "Items                "+"Prices       "+"values        "+"\r\n"+
//                    linea_entera+
//                    Final_Linea+
//                    print_details(receipt.getDetails())+
//                    Final_Linea+
//                    linea_entera+
//                    Final_Linea+
//                    "Items Count: "+receipt.getItemsCount()+
//                    Final_Linea+
//                    ">>SubTotal: $"+receipt.getSubTotal()+
//                    Final_Linea+
//                    ">>Taxes: $"+receipt.getTaxes()+
//                    Final_Linea+
//                    ">>Total: $"+receipt.getTotal()+
//                    Final_Linea+
//                    Final_Linea+
//                    "Cashier: "+receipt.getCashier()+
//                    Final_Linea+
//                    "Thank you for your visit."+
//                    Final_Linea+Final_Linea;
//
//
//
//
//            configLabel = cpclConfigLabel.getBytes();
//        }
//        return configLabel;
//
//    }
//
//    private byte[] openbravoFactura() {
//
//        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
//
//        byte[] configLabel = null;
//        if (printerLanguage == PrinterLanguage.ZPL) {
//            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
//        } else if (printerLanguage == PrinterLanguage.CPCL) {
//            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
//                    "! U1 CONTRAST 3" + Final_Linea +
//                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
//                    Resolve.alinea_centro(Configs.COMPANY_NAME, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_SLOGAN, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_ADDRESS, caracteres_X_linea) + Final_Linea +
//                    Resolve.dos_columna("Tel: 929-244-7821", caracteres_X_linea, "Fax: 866-920-9064") + Final_Linea +
//                    Final_Linea+
//                    "-----ORIGINAL"+
//                    Final_Linea+
//                    Resolve.dos_columna("RECIPT:", 20, receipt.getId()) + Final_Linea +
//                    Resolve.dos_columna("DATE:",42, receipt.getDate()) + Final_Linea +
//                    Resolve.dos_columna("PAYMENT:", 20, receipt.getPayMethod())+
//                    Final_Linea+
//                    Resolve.dos_columna("CUSTOMER:",20,receipt.getClient())+
//                    Final_Linea +
//                    Final_Linea+
//                    Resolve.alinea_centro("R E C E I P T", caracteres_X_linea) +
//                    Final_Linea+
//                    Final_Linea+
//                    "Items                "+"Prices       "+"values        "+"\r\n"+
//                    linea_entera+
//                    Final_Linea+
//                    print_details(receipt.getDetails())+
//                    Final_Linea+
//                    linea_entera+
//                    Final_Linea+
//                    "Items Count: "+receipt.getItemsCount()+
//                    Final_Linea+
//                    ">>SubTotal: $"+receipt.getSubTotal()+
//                    Final_Linea+
//                    ">>Taxes: $"+receipt.getTaxes()+
//                    Final_Linea+
//                    ">>Total: $"+receipt.getTotal()+
//                    Final_Linea+
//                    Final_Linea+
//                    "Cashier: "+receipt.getCashier()+
//                    Final_Linea+
//                    "Thanks for choice Rainbow LandFlowers"+
//                    Final_Linea+Final_Linea;
//
//
//
//
//            configLabel = cpclConfigLabel.getBytes();
//        }
//        return configLabel;
//
//    }
//
//    private byte[] openbravoPago() {
//
//        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
//
//        byte[] configLabel = null;
//        if (printerLanguage == PrinterLanguage.ZPL) {
//            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
//        } else if (printerLanguage == PrinterLanguage.CPCL) {
//            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
//                    "! U1 CONTRAST 3" + Final_Linea +
//                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
//                    Resolve.alinea_centro(Configs.COMPANY_NAME, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_SLOGAN, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_ADDRESS, caracteres_X_linea) + Final_Linea +
//                    Resolve.dos_columna("Tel: 929-244-7821", caracteres_X_linea, "Fax: 866-920-9064") + Final_Linea +
//                    Final_Linea+
//                    "-----ORIGINAL"+
//                    Final_Linea+
//                    Resolve.dos_columna("RECIPT:", 20, receipt.getId()) + Final_Linea +
//                    Resolve.dos_columna("DATE:",42, receipt.getDate()) + Final_Linea +
//                    Resolve.dos_columna("PAYMENT:", 20, receipt.getPayMethod())+
//                    Final_Linea+
//                    Resolve.dos_columna("CUSTOMER:",20,receipt.getClient())+
//                    Final_Linea +
//                    Final_Linea+
//                    Resolve.dos_columna("TOTAL PAID:",20,receipt.getTotal().toString())+
//                    Final_Linea+
//                    Resolve.dos_columna("PENDING:",20,receipt.getPending().toString())+
//                    Final_Linea+
//                    Final_Linea+
//                    receipt.getPayMethod()+
//                    Final_Linea+
//                    "Cashier: "+receipt.getCashier()+
//                    Final_Linea+
//                    "Thanks for choice Rainbow LandFlowers"+
//                    Final_Linea+Final_Linea;
//
//
//
//
//            configLabel = cpclConfigLabel.getBytes();
//        }
//        return configLabel;
//
//    }
//
//    private byte[] openbravoPago_Reprint() {
//
//        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
//
//        byte[] configLabel = null;
//        if (printerLanguage == PrinterLanguage.ZPL) {
//            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
//        } else if (printerLanguage == PrinterLanguage.CPCL) {
//            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
//                    "! U1 CONTRAST 3" + Final_Linea +
//                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
//                    Resolve.alinea_centro(Configs.COMPANY_NAME, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_SLOGAN, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(Configs.COMPANY_ADDRESS, caracteres_X_linea) + Final_Linea +
//                    Resolve.dos_columna("Tel: 929-244-7821", caracteres_X_linea, "Fax: 866-920-9064") + Final_Linea +
//                    Final_Linea+
//                    "-----COPY"+
//                    Final_Linea+
//                    Resolve.dos_columna("RECIPT:", 20, receipt.getId()) + Final_Linea +
//                    Resolve.dos_columna("DATE:",42, receipt.getDate()) + Final_Linea +
//                    Resolve.dos_columna("PAYMENT:", 20, receipt.getPayMethod())+
//                    Final_Linea+
//                    Resolve.dos_columna("CUSTOMER:",20,receipt.getClient())+
//                    Final_Linea +
//                    Final_Linea+
//                    Resolve.dos_columna("TOTAL PAID:",20,receipt.getTotal().toString())+
//                    Final_Linea+
//                    Resolve.dos_columna("PENDING:",20,receipt.getPending().toString())+
//                    Final_Linea+
//                    Final_Linea+
//                    receipt.getPayMethod()+
//                    Final_Linea+
//                    "Cashier: "+receipt.getCashier()+
//                    Final_Linea+
//                    "Thanks for choice Rainbow LandFlowers"+
//                    Final_Linea+Final_Linea;
//
//
//
//
//            configLabel = cpclConfigLabel.getBytes();
//        }
//        return configLabel;
//
//    }


//    private String print_details(List<Product> products){
//        StringBuilder SB = new StringBuilder();
//        int maximo_item=19;
//        int maximo_price=12;
//        int maximo_total=13;
//
//        for (Product product:products) {
//            Log.e("for",product.getNAME());
//            String i = product.getCantidad()+"-"+product.getNAME();
//            //producto
//            int i_size=i.length();
//            double i_pro = i_size / maximo_item;
//
//            int pausa=0;
//            int count=0;
//            while (pausa==0){
//                int inicio =maximo_item*count;
//                int fin =maximo_item *(count + 1);
//
//                if(inicio>=i_size || fin>=i_size){
//                  if(inicio<=i_size){
//                      count++;
//                  }
//                  break;
//                }
//
//                String prueba = i.substring(inicio,fin);
//
//                if(prueba.isEmpty()) pausa=1;
//
//                count++;
//            }
//
//            //price
//            String p="$"+product.getPRICESELL();
//            int p_size=p.length();
//            double p_pro=p_size/maximo_price;
//
//            //total
//            Double total=Double.parseDouble(product.getPRICESELL())*product.getCantidad();
//            String t="$"+total.toString();
//            int t_size=t.length();
//            double t_pro=t_size/maximo_total;
//
//
//            for(int x=0;x<count;x++){
//                int i_k=0;
//                if(x==0){
//                    SB.append(i.substring(0,(maximo_item>i_size)?i_size:maximo_item));
//
//                    i_k=maximo_item-i_size;
//
//                    if(i_k > 0)for (Integer q = 0; q < i_k; q++) SB.append(" ");
//
//                    SB.append("|");
//
//                    SB.append(p.substring(0,(maximo_price>p_size)?p_size:maximo_price));
//
//                    int i_calc=maximo_price-p_size;
//
//                    if(i_calc > 0)for (Integer q = 0; q < i_calc; q++) SB.append(" ");
//
//                    SB.append("|");
//
//                    SB.append(t.substring(0,(maximo_total>t_size)?t_size:maximo_total));
//
//                    int t_calc=maximo_total-t_size;
//
//                    if(t_calc > 0)for (Integer q = 0; q < t_calc; q++) SB.append(" ");
//
//                    SB.append(Final_Linea);
//                }else {
//
//                    int inicio = maximo_item * x;
//                    int fin = maximo_item * (x + 1);
//
//                    if (inicio >= i_size || fin >= i_size) {
//                        if (inicio <= i_size) {
//                            SB.append(i.substring(inicio));
//                        }
//                        break;
//                    }
//                    SB.append(i.substring(inicio, fin));
//                    SB.append(Final_Linea);
//                }
//            }
//            SB.append(Final_Linea);
//
//        }
//        //StringBuilder SB = new StringBuilder(c+i_cut+p_cut);
//        return SB.toString();
//    }


    /*private byte[] printeo() {

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
        } catch (Exception e){
            return string_primario_copia;
        }


        return string_primario;

    }*/

//    private byte[] telenord() {
//
//        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
//
//        byte[] configLabel = null;
//        if (printerLanguage == PrinterLanguage.ZPL) {
//            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
//        } else if (printerLanguage == PrinterLanguage.CPCL) {
//            String cpclConfigLabel = "! U1 SETLP 7 0 20 \r\n" +
//                    "! U1 CONTRAST 3" + Final_Linea +
//                    "! U1 CENTER" + Final_Linea + //No funciona D:!!
//                    Resolve.alinea_centro("TELEOPERADORA DEL NORDESTE (TELENORD)", caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro("Una empresa al servicio de su gente", caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro("Av. Frank Grullon #5 San Francisco de Macoris", caracteres_X_linea) + Final_Linea +
//                    Resolve.dos_columna("Tel: 809-588-6238", caracteres_X_linea, "Fax: 809-588-0105") + Final_Linea +
//                    "RNC: 104-01619-1" + Final_Linea +
//                    Resolve.alinea_centro("R E C I B O", caracteres_X_linea) + Final_Linea +
//                    "Numero: " + detalles.get(0).get(parametros[0]) + Final_Linea + //TRN, detalles = Resultado, parametros[0] = TRN
//                    //"Referencia: " + detalles.get(0).get(parametros[0]) + Final_Linea +
//                    "Fecha: " + detalles.get(0).get(parametros[3]) + Final_Linea + //Fecha
//                    linea_entera + Final_Linea +
//                    "CONTRATO: "  + cliente.contrato + Final_Linea +
//                    cliente.nombre + Final_Linea +
//                    "BALANCE ANTERIOR: " + cliente.balance + Final_Linea +
//                    "DIRECCION: " + cliente.direccion + Final_Linea +
//                    linea_entera + Final_Linea +
//                    Resolve.dos_columna("Concepto", caracteres_X_linea, "Monto Pagado") + Final_Linea +
//                    linea_entera + Final_Linea;
//
//            //detalles.get(0).get(parametros[1]).toString() CARGO NOVIEMBRE 2014; 650.00; CARGO DICIEMBRE 2014; 650.00;
//                /*
//                Devuelve la misma sentencia con los detalles anexados en dado caso de fallar devuelve el mismo string sin los anexos
//                 */
//            cpclConfigLabel = imprime_detalles(cpclConfigLabel,Resolve.SeparaLineas(detalles.get(0).get(parametros[1]).toString(), ";"), caracteres_X_linea, Final_Linea);//ConceptoPago
//            Double f = Double.valueOf(cliente.balance) -  Double.valueOf(param_post.get(0).getValue());
//            cpclConfigLabel += linea_entera + Final_Linea +
//                    Resolve.dos_columna("TOTAL PAGADO", caracteres_X_linea, param_post.get(0).getValue()) + Final_Linea +
//                    Resolve.dos_columna("TOTAL PENDIENTE", caracteres_X_linea, String.valueOf(f)) + Final_Linea +
//                    Resolve.alinea_centro(detalles.get(0).get(parametros[4]), caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro(linea_mitad, caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro("LE ATENDIO", caracteres_X_linea) + Final_Linea +
//                    Resolve.alinea_centro("*** ABONO A FACTURA NO EVITA CORTE ***", caracteres_X_linea) + Final_Linea +
//                    " " + Final_Linea;
//
//
//
//            configLabel = cpclConfigLabel.getBytes();
//        }
//        return configLabel;
//
//    }




//TODO: Anexar bien la impresion

}


