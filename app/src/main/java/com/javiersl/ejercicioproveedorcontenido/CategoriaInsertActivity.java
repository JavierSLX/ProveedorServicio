package com.javiersl.ejercicioproveedorcontenido;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.javiersl.ejercicioproveedorcontenido.Constantes.Constantes;
import com.javiersl.ejercicioproveedorcontenido.Entidades.Categoria;
import com.javiersl.ejercicioproveedorcontenido.Proveedor.CategoriaProveedor;

public class CategoriaInsertActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_insert);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Coloca la flecha de regreso hacia el padre (Hay que colocar su padre en el Manifest)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem guardar = menu.add(Menu.NONE, Constantes.GUARDAR, Menu.NONE, "Guardar");
        guardar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        guardar.setIcon(R.drawable.ic_archive);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case Constantes.GUARDAR:
                guardar();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void guardar()
    {
        EditText edtNombre = (EditText)findViewById(R.id.edtNombre);

        //Borra mensaje de validacion anterior
        edtNombre.setError(null);

        String nombre = edtNombre.getText().toString();

        if(nombre.equals(""))
        {
            edtNombre.setError("Coloque un nombre");

            //Recoje el foco el EditText
            edtNombre.requestFocus();
        }
        else
        {
            //Inserta un nuevo registro
            CategoriaProveedor.getInstance().insert(getContentResolver(), new Categoria(nombre));
            finish();
        }
    }
}
