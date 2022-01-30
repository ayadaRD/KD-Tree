package Pack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) { launch(args);}


    @Override
    public void start(Stage primaryStage) throws Exception {

        //load file from path
        Parent root = FXMLLoader.load(this.getClass().getResource("View/mainPage.fxml"));

        //creating scene
        Scene scene = new Scene(root);

        //disabling maximize button
        primaryStage.resizableProperty().setValue(Boolean.FALSE);

        //setting title
        primaryStage.setTitle("Tree");

        //setting width and height
        primaryStage.setWidth(1880);
        primaryStage.setHeight(980);

        //setting icon
        primaryStage.getIcons().add(new Image("Pack/icon.png"));

        //connecting
        primaryStage.setScene(scene);

        //showing
        primaryStage.show();

    }
}
