package com.lsp.printer;

import java.net.InetAddress;

/**
 * Created by Suarez on 04/01/2018.
 */

public class Resolve {

    public static final String EXTRA_RESULTADO = "extra.resultado";
    private static final String EXTRA_MENSAJE = "extra.mensaje";
    public static String alinea_centro(String Texto, int Maximo){

        StringBuilder SB = new StringBuilder(Texto);
        Maximo = Math.round((Maximo - Texto.length()) / 2);

        for (Integer x = 0; x < Maximo ; x++ ) {
            SB.insert(0, " ");
        }

        return SB.toString();
    }


    public static String dos_columna(String Texto, Integer Maximo, String Texto_dos){

        StringBuilder SB = new StringBuilder(Texto);
        Integer cantidad = Maximo - Texto.length() - Texto_dos.length();

        if (cantidad > 0) for (Integer x = 0; x < cantidad; x++) SB.append(" ");

        SB.append(Texto_dos);

        return SB.toString();

    }

    public static boolean isInternetAvailable() {
        try{
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


}
