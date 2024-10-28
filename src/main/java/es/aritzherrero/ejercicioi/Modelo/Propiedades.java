package es.aritzherrero.ejercicioi.Modelo;

import java.io.FileInputStream;
import java.util.Properties;

public abstract class Propiedades {
    private static Properties props = new Properties();
    static {
        try (FileInputStream input = new FileInputStream("configuration.properties")){
            props.load(input);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getValor(String clave) {
        String valor=props.getProperty(clave);
        if (valor!=null) {
            return valor;
        }
        throw new RuntimeException("El fichero de configuracion no existe");
    }

}
