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


    exports jueguito.juegopracticafinal.main;
    opens jueguito.juegopracticafinal.main to javafx.fxml;

}