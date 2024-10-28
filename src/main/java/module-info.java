module es.aritzherrero.ejercicioi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens es.aritzherrero.ejercicioi to javafx.fxml;
    exports es.aritzherrero.ejercicioi;
    opens es.aritzherrero.ejercicioi.Controlador to javafx.fxml;
    opens es.aritzherrero.ejercicioi.Modelo to javafx.fxml, javafx.base;
    opens es.aritzherrero.ejercicioi.DAO to javafx.fxml, javafx.base;
    opens es.aritzherrero.ejercicioi.Conexion to javafx.fxml, javafx.base;
}