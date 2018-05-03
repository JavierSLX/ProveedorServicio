package com.javiersl.ejercicioproveedorcontenido.Proveedor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.javiersl.ejercicioproveedorcontenido.Entidades.Categoria;

/**
 * Created by JavierSL on 01/05/2018.
 */

//Clase que contiene los métodos que enlaza con el proveedor de contenidos
public class CategoriaProveedor
{
    private CategoriaProveedor()
    {
    }

    public static CategoriaProveedor getInstance()
    {
        return new CategoriaProveedor();
    }

    public void insert(ContentResolver resolver, Categoria categoria)
    {
        //Saca la uri del contenedor
        Uri uri = Contrato.Categoria.CONTENT_URI;

        //Coloca los valores que se van a insertar
        ContentValues values = new ContentValues();
        values.put(Contrato.Categoria.NOMBRE, categoria.getNombre());

        //Inserta los valores con ayuda del proveedor
        resolver.insert(uri, values);
    }

    public void delete(ContentResolver resolver, int id)
    {
        //content://com.javiersl.ejercicioproveedorcontenido.Proveedor.ProveedorContenido/categoria/#
        Uri uri = Uri.parse(Contrato.Categoria.CONTENT_URI + "/" + id);

        //Borra el registro indicado
        resolver.delete(uri, null, null);
    }

    public void update(ContentResolver resolver, Categoria categoria)
    {
        //content://com.javiersl.ejercicioproveedorcontenido.Proveedor.ProveedorContenido/categoria/#
        Uri uri = Uri.parse(Contrato.Categoria.CONTENT_URI + "/" + categoria.getId());

        ContentValues values = new ContentValues();
        values.put(Contrato.Categoria.NOMBRE, categoria.getNombre());

        //Actualiza un elemento
        resolver.update(uri, values, null, null);
    }

    public Categoria leerRegistro(ContentResolver resolver, int id)
    {
        //content://com.javiersl.ejercicioproveedorcontenido.Proveedor.ProveedorContenido/categoria/#
        Uri uri = Uri.parse(Contrato.Categoria.CONTENT_URI + "/" + id);

        //Los campos que se quiere que se devuelvan
        String campos[] = {Contrato.Categoria.NOMBRE};

        Cursor cursor = resolver.query(uri, campos, null, null, null);

        Categoria categoria = null;
        if(cursor.moveToFirst())
            categoria = new Categoria(id, cursor.getString(cursor.getColumnIndex(Contrato.Categoria.NOMBRE)));

        return categoria;
    }
}
