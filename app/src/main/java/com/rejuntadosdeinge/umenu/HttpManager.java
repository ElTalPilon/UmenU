package com.rejuntadosdeinge.umenu;

import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/*
    Envía data al servidor formato JSON

    Ésta clase se maneja la conexión a la base de datos utilizando HttpURLConnection para
    consultas HTTP. Inicialmente se hizo getData(String uri) para correr la consulta usando "get"
    en DetallesSoda. Para ListaPlatos y DetallesPlato se necesita consultar con "post",
    se hizo otro metodo sobrecargado getData(RequestPackage p) donde los p son los parámetros que
    utiliza el post y acepta diferentes tipos en los parámetros. No tuve tiempo para hacer
    refactor a DetallesSoda y que funcionaran todas las consultas con el mismo método pero esa es la idea.
 */
public class HttpManager {

    /*
        El método siguiente se utiliza en consultas GET donde los parámetros van "appended" al uri
        El parámetro uri representa la ubicación de los datos.
    */
    public static String getData(String uri) {

        BufferedReader reader = null;

        try {
            // creamos un objeto URL usando parámetro nuestro uri
            URL url = new URL(uri);
            // se abre la conexión Http
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // para obtener el contenido Web, una línea a la vez del sitio remoto
            StringBuilder sb = new StringBuilder();
            // definir reader con el inputStream que obtengo de la conexión URL
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // listos para leer el código una línea a la vez
            String line;
            // cuando el ciclo while esté completo, recibimos el contenido del web request
            while ((line = reader.readLine()) != null) {
                //  mientras que no se acabe el contenido se agrega contenido a variable line
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // nos aseguramos que el reader no es null
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    /*
        El método siguiente se utiliza en consultas POST donde parámetros están dentro del cuerpo del request
        El parámetro p contiene trés elementos:
            uri: direccion donde se encuentran los datos en formato JSON
            method: Get ó POST
            params: los que sean necesarios
    */
    public static String getData(RequestPackage p) {

        BufferedReader reader = null;
        String uri = p.getUri();

        if(p.getMethod().equals("GET")){
            uri+="?" + p.getEncodedParams();
        }

        try {
            // creamos un objeto URL usando parámetro nuestro uri
            URL url = new URL(uri);
            // se abre la conexión Http
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());

            // creamos instancia JSONObject y le pasamos los parámetros del diccionario
            JSONObject json = new JSONObject(p.getParams());
            // obtenemos los valores para mandar al servidor
            String params = "params=" + json.toString();

            // agrega parametros en el cuerpo del Http request
            if (p.getMethod().equals("POST")) {
                // para enviar una consulta en el cuerpo del request
                con.setDoOutput(true);
                // creamos un objeto writer que puede escribir al output string y enviar a travéz de la conexión
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                // le paramos los parámetros encoded
                writer.write(params);
                // enviado al servidor
                writer.flush();
            }

            // para obtener el contenido Web, una línea a la vez del sitio remoto
            StringBuilder sb = new StringBuilder();
            // definir reader con el inputStream que obtengo de la conexión URL
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // listos para leer el código una línea a la vez
            String line;
            // cuando el ciclo while esté completo, recibimos el contenido del web request
            while ((line = reader.readLine()) != null) {
                //  mientras que no se acabe el contenido se agrega contenido a variable line
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}