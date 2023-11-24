module GestionePista {
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.jfoenix;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.rmi;

    opens it.voltats.gestionepista to javafx.fxml;
    opens it.voltats.gestionepista.ui.controllers to javafx.fxml;

    exports it.voltats.gestionepista;
}