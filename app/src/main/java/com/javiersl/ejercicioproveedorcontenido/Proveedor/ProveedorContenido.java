package com.javiersl.ejercicioproveedorcontenido.Proveedor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

/**
 * Created by JavierSL on 30/04/2018.
 */

public class ProveedorContenido extends ContentProvider
{
    //Regresa un registro
    //content://com.javiersl.ejercicioproveedorcontenido.Proveedor.ProveedorContenido/categoria/#
    private static final int CATEGORIA_ONE_REGISTRO = 1;

    //Regresa todos los registros
    //content://com.javiersl.ejercicioproveedorcontenido.Proveedor.ProveedorContenido/categoria
    private static final int CATEGORIA_ALL_REGISTROS = 2;

    private SQLiteDatabase sqlDB;
    public BD dbContenido;

    //Indica cuando una URI es invalida
    public static final int INVALID_URI = -1;

    //Agrega las direcciones de las URI
    private static final UriMatcher uriMatcher;

    private static final SparseArray<String> mimeTypes;

    static
    {
        uriMatcher = new UriMatcher(0);
        mimeTypes = new SparseArray<>();

        //Agrega URLs al UriMatcher (agrega las 2 URL de arriba)
        uriMatcher.addURI(Contrato.AUTHORITY, BD.TABLE_CATEGORIA, CATEGORIA_ALL_REGISTROS);
        uriMatcher.addURI(Contrato.AUTHORITY, BD.TABLE_CATEGORIA + "/#", CATEGORIA_ONE_REGISTRO);

        //Los tipos MIME son la forma de representar la informacion (text/html, application/json, etc)

        //Los que son varios registros son "dir"
        mimeTypes.put(CATEGORIA_ALL_REGISTROS, "vnd.android.cursor.dir/vnd." + Contrato.AUTHORITY +
        "." + BD.TABLE_CATEGORIA);

        //Los que son de un solo registro es "item"
        mimeTypes.put(CATEGORIA_ONE_REGISTRO, "vnd.android.cursor.item/vnd." + Contrato.AUTHORITY +
        "." + BD.TABLE_CATEGORIA);
    }

    @Override
    public boolean onCreate()
    {
        dbContenido = new BD(getContext());
        return (dbContenido == null) ? false : true;
    }

    public void resetDatabase()
    {
        dbContenido.close();
        dbContenido = new BD(getContext());
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbContenido.getReadableDatabase();

        //Al realizar alguna consulta
        switch (uriMatcher.match(uri))
        {
            case CATEGORIA_ONE_REGISTRO:
                if(selection == null)
                    selection = "";

                selection += Contrato.Categoria._ID + " = " + uri.getLastPathSegment();
                queryBuilder.setTables(BD.TABLE_CATEGORIA);
                break;

            case CATEGORIA_ALL_REGISTROS:
                if(TextUtils.isEmpty(sortOrder))
                    sortOrder = Contrato.Categoria._ID + " ASC";
                queryBuilder.setTables(BD.TABLE_CATEGORIA);
                break;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        //Notifica a todos los suscritos de la URI de los cambios
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        sqlDB = dbContenido.getWritableDatabase();
        String table = "";

        //Depende de la URL se elige una opcion
        switch (uriMatcher.match(uri))
        {
            case CATEGORIA_ALL_REGISTROS:
                table = BD.TABLE_CATEGORIA;
                break;
        }

        //Hace una inserción
        long rowID = sqlDB.insert(table, "", values);
        if(rowID > 0)
        {
            //Manda la inserción dentro del content
            Uri rowUri = ContentUris.appendId(uri.buildUpon(), rowID).build();

            //Notifica a todos los suscritos de la URI de los cambios
            getContext().getContentResolver().notifyChange(rowUri, null);

            return rowUri;
        }

        throw new SQLException("Falló al insertar un row en " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        sqlDB = dbContenido.getWritableDatabase();
        String table = "";

        switch (uriMatcher.match(uri))
        {
            //Cuando es solo 1 registro
            case CATEGORIA_ONE_REGISTRO:
                if(selection == null)
                    selection = "";

                //Toma el ultimo segmento de la URI
                selection += Contrato.Categoria._ID + " = " + uri.getLastPathSegment();
                table = BD.TABLE_CATEGORIA;
                break;

            //Cuando es toda la tabla
            case CATEGORIA_ALL_REGISTROS:
                table = BD.TABLE_CATEGORIA;
                break;
        }

        int rows = sqlDB.delete(table, selection, selectionArgs);
        if(rows > 0)
        {
            //Notifica a todos los suscritos de la URI de los cambios
            getContext().getContentResolver().notifyChange(uri, null);
            return rows;
        }

        throw new SQLException("Falló al eliminar en " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        sqlDB = dbContenido.getWritableDatabase();

        String table = "";
        switch (uriMatcher.match(uri))
        {
            case CATEGORIA_ONE_REGISTRO:
                if(selection == null)
                    selection = "";

                selection += Contrato.Categoria._ID + " = " + uri.getLastPathSegment();
                table = BD.TABLE_CATEGORIA;
                break;

            case CATEGORIA_ALL_REGISTROS:
                table = BD.TABLE_CATEGORIA;
                break;
        }

        int rows = sqlDB.update(table, values, selection, selectionArgs);
        if(rows > 0)
        {
            //Notifica a todos los suscritos de la URI de los cambios
            getContext().getContentResolver().notifyChange(uri, null);
            return rows;
        }

        throw new SQLException("Falló actualizar en " + uri);
    }
}
