module jueguito.juegopracticafinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires annotations;
    requires com.google.gson;
    requires ch.qos.logback.classic;

    exports jueguito.juegopracticafinal.main;
    opens jueguito.juegopracticafinal.main to javafx.fxml;
    exports jueguito.juegopracticafinal.Modelo.Mundo to com.google.gson;
    exports jueguito.juegopracticafinal.Modelo.Core to com.google.gson;
    opens jueguito.juegopracticafinal.Modelo.Core to com.google.gson;

}