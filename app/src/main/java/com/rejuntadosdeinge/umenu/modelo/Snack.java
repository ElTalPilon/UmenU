package com.rejuntadosdeinge.umenu.modelo;

public class Snack {

    private int id;
    private int soda_id;
    private String nombre;
    private String precio;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSodaId() {
        return soda_id;
    }
    public void setSodaId(int soda_id) {
        this.soda_id = soda_id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getPrecio() {
        return precio;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
