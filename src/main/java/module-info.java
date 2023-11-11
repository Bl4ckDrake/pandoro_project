module it.voltats.gestionepista {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires java.sql;
    requires java.desktop;
    requires java.rmi;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;

    opens it.voltats.gestionepista.ui.controllers to javafx.fxml;
    exports it.voltats.gestionepista;
}