package com.desmov.practicadesmov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class BaseDatos {
    SQLiteDatabase con;
    ArrayList<Producto> lista = new ArrayList<>();
    Producto p;
    Context ctx;
    String nombre_bd = "bd_productos";
    String tabla = "create table if not exists producto (id integer primary key, nombre text, " +
                    "cantidad text, precio text, detalle text)";
    public BaseDatos( Context c )
    {
        this.ctx = c;
        con = c.openOrCreateDatabase(nombre_bd, Context.MODE_PRIVATE, null);
        con.execSQL(tabla);
    }

    public boolean insertarProducto( Producto pro ) {
        ContentValues contenedor = new ContentValues();
        contenedor.put("id", pro.getId());
        contenedor.put("nombre", pro.getNombre());
        contenedor.put("cantidad", pro.getCantidad());
        contenedor.put("precio", pro.getPrecio());
        contenedor.put("detalle", pro.getDetalle());
        return (con.insert("producto", null, contenedor))>0;
    }

    public boolean eliminarProducto( int id ){
        return (con.delete("producto", "id="+id,null))>0;
    }

    public boolean editarProducto( Producto pro ){
        ContentValues contenedor = new ContentValues();
        contenedor.put("id", pro.getId());
        contenedor.put("nombre", pro.getNombre());
        contenedor.put("cantidad", pro.getCantidad());
        contenedor.put("precio", pro.getPrecio());
        contenedor.put("detalle", pro.getDetalle());
        return (con.update("producto", contenedor, "id="+pro.getId(),null))>0;
    }

    public ArrayList<Producto> verTodos(){
        lista.clear();
        Cursor cursor = con.rawQuery("select * from producto", null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                lista.add(new Producto(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }while(cursor.moveToNext());
        }
        return lista;
    }

    public Producto verUno( int position ){
        Cursor cursor = con.rawQuery("select * from producto", null);
        cursor.moveToPosition(position);
        p = new Producto(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return p;
    }

    public ArrayList<Producto> verFiltros(String parametro)
    {
        lista.clear();
        Cursor cursor = con.rawQuery("select * from producto where nombre like '%"+parametro+"%'", null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                lista.add(new Producto(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }while(cursor.moveToNext());
        }
        return lista;
    }
}
