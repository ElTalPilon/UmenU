package com.rejuntadosdeinge.umenu.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase creada para modelar los resultados de las consultas a la BD.
 * Se implementó como Parcelable para poder enviar objetos de esta clase como extras.
 */
public class Plato implements Parcelable{

    private int id;
    private int soda_id;
    private String precio;
    private int calificaciones;
    private int total;
    private float promedio;
    private String nombre;
    private String categoria;
    private String tipo;

    public Plato(){}

    private Plato(Parcel p){
        this.id = p.readInt();
        this.soda_id = p.readInt();
        this.calificaciones = p.readInt();
        this.total = p.readInt();
        this.promedio = p.readFloat();
        this.precio = p.readString();
        this.nombre = p.readString();
        this.categoria = p.readString();
        this.categoria = p.readString();
    }

    public static final Creator<Plato> CREATOR = new Parcelable.Creator<Plato>() {
        public Plato createFromParcel(Parcel p){
            return new Plato(p);
        }

        public Plato[] newArray(int size){
            return new Plato[size];
        }
    };

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeInt(id);
        out.writeInt(soda_id);
        out.writeInt(calificaciones);
        out.writeInt(total);
        out.writeFloat(promedio);
        out.writeString(precio);
        out.writeString(nombre);
        out.writeString(categoria);
        out.writeString(tipo);
    }

    public int getId() {
        return id;
    }
    public int getSodaId() {
        return soda_id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getPrecio() {
        return precio;
    }
    public String getCategoria() {
        return categoria;
    }
    public String getTipo() {
        return tipo;
    }
    public int getCalificaciones() {
        return calificaciones;
    }
    public int getTotal() {
        return total;
    }
    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public void setSodaId(int soda_id) {
        this.soda_id = soda_id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setCalificaciones(int calificaciones) {
        this.calificaciones = calificaciones;
    }
}
