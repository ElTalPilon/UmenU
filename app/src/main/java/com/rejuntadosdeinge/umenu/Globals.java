package com.rejuntadosdeinge.umenu;

public class Globals{
    private static Globals instance;

    // Global variable
    private int idSoda;
    private String sodaElegida;
    private String categoria;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setIdSoda(int idSoda){
        this.idSoda=idSoda;
    }
    public int getIdSoda(){
        return this.idSoda;
    }
    public void setSodaElegida(String sodaElegida){
        this.sodaElegida=sodaElegida;
    }
    public String getSodaElegida(){
        return this.sodaElegida;
    }
    public void setCategoria(String categoria){
        this.categoria=categoria;
    }
    public String getCategoria(){
        return this.categoria;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}