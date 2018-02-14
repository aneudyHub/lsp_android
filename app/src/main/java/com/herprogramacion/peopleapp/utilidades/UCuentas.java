package com.herprogramacion.peopleapp.utilidades;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.herprogramacion.peopleapp.R;

/**
 * Utilidades para uso de cuentas
 */
public class UCuentas {

    public static Account obtenerCuentaActiva(final Context context) {

        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account cuenta = new Account(context.getString(R.string.app_name),
                context.getString(R.string.tipo_cuenta));

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(cuenta)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(cuenta, "", null))
                return null;

        }
        return cuenta;
    }

}
