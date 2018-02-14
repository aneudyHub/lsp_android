package com.herprogramacion.peopleapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Servicio de autenticación de cuentas auxiliar
 */
public class ServicioAutenticacion extends Service {
    // Instancia del autenticador
    private Autenticador autenticador;

    @Override
    public void onCreate() {
        // Nueva instancia del autenticador
        autenticador = new Autenticador(this);
    }

    /*
     * Ligando el servicio al framework de Android
     */
    @Override
    public IBinder onBind(Intent intent) {
        return autenticador.getIBinder();
    }
}
