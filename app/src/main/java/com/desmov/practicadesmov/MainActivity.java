package com.desmov.practicadesmov;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BaseDatos db;
    Adaptador adaptador;
    Producto pro;
    ArrayList<Producto> lista;
    EditText buscar;
    ListView li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new BaseDatos(this);
        buscar = findViewById(R.id.buscar);
        lista = db.verTodos();
        adaptador = new Adaptador(this, lista, db);
        buscar.setSelected(false);
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lista = db.verFiltros(charSequence.toString());
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button agregar = findViewById(R.id.agregar);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogo = new Dialog(MainActivity.this);
                dialogo.setTitle("Nuevo Producto");
                dialogo.setCancelable(true);
                dialogo.setContentView(R.layout.obtener_datos);
                dialogo.show();
                final EditText v1 = dialogo.findViewById(R.id.val1);
                final EditText v2 = dialogo.findViewById(R.id.val2);
                final EditText v3 = dialogo.findViewById(R.id.val3);
                final EditText v4 = dialogo.findViewById(R.id.val4);
                final EditText v5 = dialogo.findViewById(R.id.val5);
                Button guardar = dialogo.findViewById(R.id.botonagregar);
                Button cancelar = dialogo.findViewById(R.id.botoncancelar);

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            pro = new Producto(Integer.parseInt(v1.getText().toString()), v2.getText().toString(),
                                    v3.getText().toString(), v4.getText().toString(), v5.getText().toString());
                            db.insertarProducto(pro);
                            lista = db.verTodos();
                            adaptador.notifyDataSetChanged();
                            dialogo.dismiss();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogo.dismiss();
                    }
                });
            }
        });
        li = findViewById(R.id.lista);
        li.setAdapter(adaptador);
    }
}