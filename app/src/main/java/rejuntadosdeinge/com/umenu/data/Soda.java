package rejuntadosdeinge.com.umenu.data;


public class Soda {

    public String nombreSoda;
    public int imagen;
    public String detalles;

    public Soda(String id, int imagen, String detalles) {
        this.nombreSoda = id;
        this.imagen = imagen;
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return nombreSoda;
    }
}
