package model;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author estudiante
 */
public class MainApp extends Application {
    
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage)

    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Matricula");

        mostrarAgenda();
    }
    private void mostrarAgenda()
    {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/Visual/Borde.fxml"));
            BorderPane Margen = (BorderPane) loader.load();

            Scene scene = new Scene(Margen);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        launch(args);
    }




    
}
