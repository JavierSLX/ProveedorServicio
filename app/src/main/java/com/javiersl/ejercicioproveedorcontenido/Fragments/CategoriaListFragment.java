package com.javiersl.ejercicioproveedorcontenido.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.javiersl.ejercicioproveedorcontenido.CategoriaInsertActivity;
import com.javiersl.ejercicioproveedorcontenido.CategoriaModificarActivity;
import com.javiersl.ejercicioproveedorcontenido.Constantes.Constantes;
import com.javiersl.ejercicioproveedorcontenido.Proveedor.CategoriaProveedor;
import com.javiersl.ejercicioproveedorcontenido.Proveedor.Contrato;
import com.javiersl.ejercicioproveedorcontenido.R;

/**
 * Created by JavierSL on 30/04/2018.
 */

//Fragmento que implementa un ListView
public class CategoriaListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private CategoriaCursorAdapter adapter;
    private LoaderManager.LoaderCallbacks<Cursor> callbacks;
    private ActionMode actionMode;
    private View viewSeleccionado;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Activa el menu de la toolbar
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_categoria_list, container, false);

        //El adaptador del Fragmento
        adapter = new CategoriaCursorAdapter(getContext());
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        callbacks = this;

        //Carga la información correspondiente al cursor
        getLoaderManager().initLoader(0, null, callbacks);

        //Cuando se presiona de manera mantenida la lista
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Cuando ya esta el menú contextual desplegado
                if(actionMode != null)
                    return false;

                actionMode = getActivity().startActionMode(actionModeCallback);
                view.setSelected(true);
                viewSeleccionado = view;

                return true;
            }
        });
    }

    //Crea un ActionModeCallback para el menu contextual
    ActionMode.Callback actionModeCallback = new ActionMode.Callback()
    {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contextual, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        //Al presionar sobre un elemento del menú contextual
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.menu_borrar:
                    //Recupera el tag guardado desde el adapter
                    int id = (Integer)viewSeleccionado.getTag();
                    CategoriaProveedor.getInstance().delete(getActivity().getContentResolver(), id);
                    break;

                case R.id.menu_editar:
                    Intent intent = new Intent(getActivity(), CategoriaModificarActivity.class);
                    intent.putExtra(Contrato.Categoria._ID, (Integer)viewSeleccionado.getTag());
                    startActivity(intent);
                    break;
            }

            //Termina el menu contextual
            actionMode.finish();

            return true;
        }

        //Cuando se sale del menu
        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            actionMode = null;
        }
    };

    //Crea las opciones del menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        //Crea el elemento de insertar
        MenuItem insertar = menu.add(Menu.NONE, Constantes.INSERTAR, Menu.NONE, "Insertar");
        insertar.setIcon(R.drawable.ic_add);
        insertar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case Constantes.INSERTAR:
                Intent intent = new Intent(getActivity(), CategoriaInsertActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Se crea el cursor
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        //Define las columnas y la URI de Categoria
        String columns[] = new String[]{Contrato.Categoria._ID, Contrato.Categoria.NOMBRE};
        Uri uri = Contrato.Categoria.CONTENT_URI;

        //Se pide que no se eliga nada en especifico (regresa todos los registros)
        String selection = null;

        //Regresa el cursor creado
        return new CursorLoader(getContext(), uri, columns, selection, null, null);
    }

    //Cuando la carga termina
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        //Se suscribe a la URI (para obtener los datos cuando haya un cambio)
        Uri uri = Contrato.Categoria.CONTENT_URI;
        data.setNotificationUri(getActivity().getContentResolver(), uri);

        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }

    public class CategoriaCursorAdapter extends CursorAdapter
    {

        public CategoriaCursorAdapter(Context context)
        {
            super(context, null, false);
        }

        //Infla el elemento básico
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            View vista = inflater.inflate(R.layout.item_lista, parent, false);
            bindView(vista, context, cursor);

            return vista;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            int ID = cursor.getInt(cursor.getColumnIndex(Contrato.Categoria._ID));
            String nombre = cursor.getString(cursor.getColumnIndex(Contrato.Categoria.NOMBRE));

            TextView txtID = (TextView)view.findViewById(R.id.txtID);
            TextView txtNombre = (TextView)view.findViewById(R.id.txtNombre);
            ImageView imgView = (ImageView)view.findViewById(R.id.imgView);

            txtID.setText(String.valueOf(ID));
            txtNombre.setText(nombre);

            //Genera el color segun el nombre y coloca  una imagen
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(nombre);
            TextDrawable drawable = TextDrawable.builder().buildRound(nombre.substring(0, 1), color);

            imgView.setImageDrawable(drawable);

            //Guarda en una etiqueta el id de cada elemento
            view.setTag(ID);
        }
    }
}
