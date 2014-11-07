package com.rejuntadosdeinge.umenu;

public class Globals{
    private static Globals instance;

    // Global variables
    private int idSoda;
    private int idPlato;
    private String nombreSoda;
    private String nombrePlato;
    private String categoria;

    /**
     * Restringe la creación de instancias
     */
    private Globals(){

    }

    /**
     * Se encarga de crear la única instancia de esta clase
     */
    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    /**
     * SETTERS
     */
    public void setIdSoda(int idSoda){
        this.idSoda=idSoda;
    }
    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }
    public void setNombreSoda(String nombreSoda){
        this.nombreSoda = nombreSoda;
    }
    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }
    public void setCategoria(String categoria){
        this.categoria=categoria;
    }

    /**
     * GETTERS
     */
    public int getIdSoda(){
        return this.idSoda;
    }
    public int getIdPlato() {
        return idPlato;
    }
    public String getNombreSoda(){
        return this.nombreSoda;
    }
    public String getNombrePlato() {
        return nombrePlato;
    }
    public String getCategoria(){
        return this.categoria;
    }
}