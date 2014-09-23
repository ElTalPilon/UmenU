package rejuntadosdeinge.com.umenu.data;

import java.util.ArrayList;
import java.util.List;

import rejuntadosdeinge.com.umenu.R;

public class DatosSoda {

    /**
     *  Un Array de Sodas
     */
    private List<Soda> Sodas = new ArrayList<Soda>();
    public List<Soda> getSodas() {
        return Sodas;
    }

    public DatosSoda() {
        addItem(new Soda("Restaurante Universitario", R.drawable.restaurante_universitario, "Amplio espacio disponible. Precios bajos. Abierto de 7 am a 7 pm jornada continua. Horario de desyuno de 7 am a 10 am. Almuerzo de 10am a 2 pm. Cena de 3 pm a 7 pm."));
        addItem(new Soda("Soda de Odontología", R.drawable.soda_odontologia, "Amplio espacio disponible. Precios bajos. Abierto de 7 am a 7 pm jornada continua. Horario de desyuno de 7 am a 10 am. Almuerzo de 10am a 2 pm. Cena de 3 pm a 7 pm."));
        addItem(new Soda("Soda de Agronomia", R.drawable.soda_agronomia, "Amplio espacio disponible. Precios bajos. Abierto de 7 am a 7 pm jornada continua. Horario de desyuno de 7 am a 10 am. Almuerzo de 10am a 2 pm. Cena de 3 pm a 7 pm."));
    }

    private void addItem(Soda item) {
        Sodas.add(item);
    }

}
