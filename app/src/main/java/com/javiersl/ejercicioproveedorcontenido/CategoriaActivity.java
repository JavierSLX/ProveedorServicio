package com.javiersl.ejercicioproveedorcontenido;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.javiersl.ejercicioproveedorcontenido.Fragments.CategoriaListFragment;

public class CategoriaActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CategoriaListFragment fragment = new CategoriaListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, fragment)
                .commit();

        FloatingActionButton btInsertar = (FloatingActionButton)findViewById(R.id.fab);
        btInsertar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CategoriaActivity.this, CategoriaInsertActivity.class);
                startActivity(intent);
            }
        });
    }

}
