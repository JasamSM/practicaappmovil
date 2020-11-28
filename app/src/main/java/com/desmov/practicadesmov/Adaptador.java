package com.desmov.practicadesmov;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    ArrayList<Producto> lista;
    BaseDatos database;
    Producto p;
    Activity a;
    int id;
    public Adaptador(Activity ac, ArrayList<Producto> l, BaseDatos db)
    {
        this.lista = l;
        this.a = ac;
        this.database = db;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Producto getItem(int i) {
        p = lista.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        p = lista.get(i);
        return p.getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        if(v==null){
            LayoutInflater li = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item, null);
        }
        p = lista.get(position);
        TextView v1 = v.findViewById(R.id.clave);
        TextView v2 = v.findViewById(R.id.nombre);
        TextView v3 = v.findViewById(R.id.precio);
        TextView v4 = v.findViewById(R.id.cantidad);
        TextView v5 = v.findViewById(R.id.detalle);

        Button editar = v.findViewById(R.id.editar);
        Button eliminar = v.findViewById(R.id.eliminar);

        v1.setText("id: "+p.getId());
        v2.setText(p.getNombre());
        v3.setText("$ "+p.getPrecio());
        v4.setText("Cant. "+p.getCantidad());
        v5.setText("Des. "+p.getDetalle());
        editar.setTag(position);
        eliminar.setTag(position);

        LinearLayout linear = v.findViewById(R.id.items);
        linear.setTag(position);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt(view.getTag().toString());
                final Dialog dialogo = new Dialog(a);
                dialogo.setTitle("Detalle Producto");
                dialogo.setCancelable(true);
                dialogo.setContentView(R.layout.vista_producto);
                dialogo.show();
                TextView v1 = dialogo.findViewById(R.id.val1);
                TextView v2 = dialogo.findViewById(R.id.val2);
                TextView v3 = dialogo.findViewById(R.id.val3);
                TextView v4 = dialogo.findViewById(R.id.val4);
                TextView v5 = dialogo.findViewById(R.id.val5);
                Button cancelar = dialogo.findViewById(R.id.botoncancelar);
                p = lista.get(pos);

                v1.setText("Clave: "+p.getId());
                v2.setText("Nombre: "+p.getNombre());
                v3.setText("Cantidad: "+p.getCantidad());
                v4.setText("Precio: "+p.getPrecio());
                v5.setText("Detalle: "+p.getDetalle());

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogo.dismiss();
                    }
                });
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt(view.getTag().toString());
                final Dialog dialogo = new Dialog(a);
                dialogo.setTitle("Editar Producto");
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
                p = lista.get(pos);
                setId(p.getId());
                v1.setText(""+p.getId());
                v1.setEnabled(false);
                v2.setText(p.getNombre());
                v3.setText(p.getCantidad());
                v4.setText(p.getPrecio());
                v5.setText(p.getDetalle());
                guardar.setText("Actualizar");
                guardar.setBackgroundColor(Color.BLUE);
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            p = new Producto(Integer.parseInt(v1.getText().toString()), v2.getText().toString(),
                                    v3.getText().toString(), v4.getText().toString(), v5.getText().toString());
                            database.editarProducto(p);
                            lista = database.verTodos();
                            notifyDataSetChanged();
                            dialogo.dismiss();
                        }catch (Exception e){
                            Toast.makeText(a, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
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

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder del = new AlertDialog.Builder(a);
                int pos = Integer.parseInt(view.getTag().toString());
                p = lista.get(pos);
                setId(p.getId());
                del.setMessage("¿Está seguro que desea eliminar este producto?");
                del.setCancelable(false);
                del.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.eliminarProducto(getId());
                        lista = database.verTodos();
                        notifyDataSetChanged();
                    }
                });

                del.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                del.show();
            }
        });


        return v;
    }
}
