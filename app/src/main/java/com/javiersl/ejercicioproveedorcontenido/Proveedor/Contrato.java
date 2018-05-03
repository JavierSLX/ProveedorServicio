package com.javiersl.ejercicioproveedorcontenido.Proveedor;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JavierSL on 30/04/2018.
 */

public class Contrato
{
    public static final String AUTHORITY = "com.javiersl.ejercicioproveedorcontenido.Proveedor.ProveedorContenido";

    public static final class Categoria implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/categoria");

        //Nombre de la columna
        public static final String NOMBRE = "nombre";
    }
}
