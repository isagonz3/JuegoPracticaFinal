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

    exports jueguito.juegopracticafinal.Controladores;
    opens jueguito.juegopracticafinal.Controladores to javafx.fxml;
    exports jueguito.juegopracticafinal.Modelo.Mundo to com.google.gson;
    exports jueguito.juegopracticafinal.Modelo.Core to com.google.gson;
    opens jueguito.juegopracticafinal.Modelo.Core to com.google.gson;
    opens jueguito.juegopracticafinal.App to javafx.fxml;
    exports jueguito.juegopracticafinal.App;
    exports jueguito.juegopracticafinal.Controladores.Juego;
    opens jueguito.juegopracticafinal.Controladores.Juego to javafx.fxml;

    opens jueguito.juegopracticafinal.TADs;
    opens jueguito.juegopracticafinal.Modelo.Mundo;
    opens jueguito.juegopracticafinal.Modelo.Entidades;
    opens jueguito.juegopracticafinal.Modelo.Inventario;
    opens jueguito.juegopracticafinal.Modelo.NPC;
    opens jueguito.juegopracticafinal.Vista;


}