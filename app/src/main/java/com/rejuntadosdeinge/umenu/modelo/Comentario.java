package com.rejuntadosdeinge.umenu.modelo;

/*
    Cuando usamos consultas con POST, se modela los datos que regresan del Web-Service,
    cuando hacemos una consulta relacionada con platos por cada etiqueta del json obtenido
    se crea una variable junto a sus getters y setters para modificarlos y leerlos
 */
public class Comentario {

    private int id;
    private int usuario_id;
    private int plato_id;
    private String comentario;
    private int puntos;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUsuarioId() {
        return usuario_id;
    }
    public void setUsuarioId(int usuario_id) {
        this.usuario_id = usuario_id;
    }
    public int getPlatoId() {
        return plato_id;
    }
    public void setPlatoId(int plato_id) {
        this.plato_id = plato_id;
    }
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
