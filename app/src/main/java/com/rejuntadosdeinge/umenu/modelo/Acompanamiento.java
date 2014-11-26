package com.rejuntadosdeinge.umenu.modelo;

/*
    Cuando usamos consultas con POST, se modela los datos que regresan del Web-Service,
    cuando hacemos una consulta relacionada con platos por cada etiqueta del json obtenido
    se crea una variable junto a sus getters y setters para modificarlos y leerlos
 */
public class Acompanamiento {

    private int id;
    private String acompanamiento;
    private String guarnicion;
    private String ensalada1;
    private String ensalada2;
    private String ensalada3;
    private String ensalada4;
    private String ensalada5;
    private String ensalada6;
    private String fruta1;
    private String fruta2;
    private String fresco1;
    private String fresco2;
    private String frescosinazucar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcompanamiento() {
        return acompanamiento;
    }
    public void setAcompanamiento(String acompanamiento) {
        this.acompanamiento = acompanamiento;
    }

    public String getGuarnicion() {
        return guarnicion;
    }
    public void setGuarnicion(String guarnicion) {
        this.guarnicion = guarnicion;
    }

    public String getEnsalada1() {
        return ensalada1;
    }
    public void setEnsalada1(String ensalada1) {
        this.ensalada1 = ensalada1;
    }
    public String getEnsalada2() {
        return ensalada2;
    }
    public void setEnsalada2(String ensalada2) {
        this.ensalada2 = ensalada2;
    }
    public String getEnsalada3() {
        return ensalada3;
    }
    public void setEnsalada3(String ensalada3) {
        this.ensalada3 = ensalada3;
    }
    public String getEnsalada4() {
        return ensalada4;
    }
    public void setEnsalada4(String ensalada4) {
        this.ensalada4 = ensalada4;
    }
    public String getEnsalada5() {
        return ensalada5;
    }
    public void setEnsalada5(String ensalada5) {
        this.ensalada5 = ensalada5;
    }
    public String getEnsalada6() {
        return ensalada6;
    }
    public void setEnsalada6(String ensalada6) {
        this.ensalada6 = ensalada6;
    }

    public String getFruta1() {
        return fruta1;
    }
    public void setFruta1(String fruta1) {
        this.fruta1 = fruta1;
    }
    public String getFruta2() {
        return fruta2;
    }
    public void setFruta2(String fruta2) {
        this.fruta2 = fruta2;
    }

    public String getFresco1() {
        return fresco1;
    }
    public void setFresco1(String fresco1) {
        this.fresco1 = fresco1;
    }
    public String getFresco2() {
        return fresco2;
    }
    public void setFresco2(String fresco2) {
        this.fresco2 = fresco2;
    }
    public String getFrescoSinAzucar() {
        return frescosinazucar;
    }
    public void setFrescoSinAzucar(String frescosinazucar) {
        this.frescosinazucar = frescosinazucar;
    }
}

