package PhotoAlbum;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import fxml.LoginController;
import java.io.Serializable;

/**
 * This is the main class that start the very first window which is the login page
 *
 */
public class Photos extends Application {
	@Override
	public void start(Stage primaryStage) 
	throws IOException {
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/fxml/login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();

	
		LoginController loginController = loader.getController();
		loginController.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show(); 
		
		
	}
	
	public static void main(String[] args) {
		
		launch(args);

	}

}
