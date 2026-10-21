package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Busca el archivo directamente en la raíz de resources
        URL fxmlLocation = getClass().getResource("/ReservaView.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("No se encontró el archivo FXML en /ReservaView.fxml");
        }

        Parent root = FXMLLoader.load(fxmlLocation);

        primaryStage.setTitle("RutaVerde Quindío - Sistema de Gestión de Reservas");
        primaryStage.setScene(new Scene(root, 780, 580));
        primaryStage.show();
    }
}