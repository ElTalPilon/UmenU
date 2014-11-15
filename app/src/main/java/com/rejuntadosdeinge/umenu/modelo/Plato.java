package com.rejuntadosdeinge.umenu.modelo;

/*
    Cuando usamos consultas con POST, se modela los datos que regresan del Web-Service,
    cuando hacemos una consulta relacionada con platos por cada etiqueta del json obtenido
    se crea una variable junto a sus getters y setters para modificarlos y leerlos
 */
public class Plato {

    private int id;
    private int soda_id;
    private String nombre;
    private String precio;
    private String categoria;
    private String tipo;
    private int calificaciones;
    private int total;
    private float promedio;

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

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getCalificaciones() {
        return calificaciones;
    }
    public void setCalificaciones(int calificaciones) {
        this.calificaciones = calificaciones;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public float getPromedio() { return promedio; }
    public void setPromedio(int promedio) {
        this.promedio = promedio;
    }
}
