package com.javiersl.ejercicioproveedorcontenido.Proveedor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JavierSL on 30/04/2018.
 */

public class BD extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "categoria.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CATEGORIA = "categoria";

    public BD(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);

        //Línea que permite referenciar tablas
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Crea la tabla categoria
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIA + "(" + Contrato.Categoria._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Contrato.Categoria.NOMBRE + " TEXT NOT NULL);");

        inicializarDatos(db);
    }

    private void inicializarDatos(SQLiteDatabase db)
    {
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + Contrato.Categoria.NOMBRE + ") VALUES ('Alumno')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + Contrato.Categoria.NOMBRE + ") VALUES ('Profesor')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + "(" + Contrato.Categoria.NOMBRE + ") VALUES ('Director')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);
        onCreate(db);
    }
}
