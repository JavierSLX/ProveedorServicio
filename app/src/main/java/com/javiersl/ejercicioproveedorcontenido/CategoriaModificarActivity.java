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
import com.javiersl.ejercicioproveedorcontenido.Proveedor.Contrato;

public class CategoriaModificarActivity extends AppCompatActivity
{
    private EditText edtNombre;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_modificar);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Coloca la flecha de regreso hacia el padre (Hay que colocar su padre en el Manifest)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNombre = (EditText)findViewById(R.id.edtNombre);

        id = getIntent().getIntExtra(Contrato.Categoria._ID, 0);

        //Saca el objeto desde el proveedor y lo coloca por default
        Categoria categoria = CategoriaProveedor.getInstance().leerRegistro(getContentResolver(), id);
        edtNombre.setText(categoria.getNombre());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem guardar = menu.add(Menu.NONE, Constantes.ACTUALIZAR, Menu.NONE, "Guardar");
        guardar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        guardar.setIcon(R.drawable.ic_archive);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case Constantes.ACTUALIZAR:
                actualizar();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void actualizar()
    {
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
            //Arma el objeto
            Categoria categoria = new Categoria(id, edtNombre.getText().toString());

            //Actualiza el registro
            CategoriaProveedor.getInstance().update(getContentResolver(), categoria);
            finish();
        }
    }
}
