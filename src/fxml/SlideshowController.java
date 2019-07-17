package fxml;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.photo;
import models.user;
import models.album;
/**
 * This is the class for SlideshowController, which contains all the features for the slideshow page
 *
 */
public class SlideshowController {
	
	/**
	 * window stores the access point to the current window
	 */
	private Stage window;
	/**
	 * user stores the access point to current user
	 */
	user user;
	/**
	 * album stores the access point to current album
	 */
	album album;
	
	@FXML
	ImageView imageview;
	@FXML
	Pane pane1;
	@FXML
	/**
	 * the previous photo button to go to the previous slide
	 */
	Button pbutton;
	@FXML
	/**
	 * the next photo button to go to the next slide
	 */
	Button nbutton;
	/**
	 * stores the index number of the current showing image
	 */
	int index = 0;
	
	
	/**
	 * starts the stage, loads the data of the search page
	 * @param user - passes over the access point of the current user
	 * @param album - passes over the access point of the current album
	 * @param mainStage - passes over the access point of mainStage to this stage
	 */
	public void start(Stage mainStage, album album, user user) {
		
		this.window = mainStage;
		this.album = album;
		this.user = user;
		ImageView imageView = new ImageView();
		imageView.setPreserveRatio(true);
		imageView.fitWidthProperty().bind(pane1.widthProperty());
		imageView.fitHeightProperty().bind(pane1.heightProperty());
		
		StackPane stack = new StackPane();
		stack.getChildren().add(imageView);

	    stack.translateXProperty()
	            .bind(pane1.widthProperty().subtract(stack.widthProperty())
	                    .divide(2));

	    stack.translateYProperty()
	            .bind(pane1.heightProperty().subtract(stack.heightProperty())
	                    .divide(2));
		pane1.getChildren().add(stack);
		this.imageview = imageView;
		imageview.setImage(album.getPhotos().get(0).getImage());
		pbutton.setDisable(true);
		if(album.getPhotos().size()==1) {
			nbutton.setDisable(true);
		}
	}
	
	@FXML
	/**
	 * once the < button is pressed, slideshows the previous image
	 * 
	 */
	private void previous(ActionEvent evt) throws IOException {
		index--;
		
		if(index==0) {
			pbutton.setDisable(true);
		
		}
		else {
			pbutton.setDisable(false);
		}
		if(index==album.getPhotos().size()-1) {
			nbutton.setDisable(true);
			
		}
		else {
			nbutton.setDisable(false);
		}
		imageview.setImage(album.getPhotos().get(index).getImage());
	}
	
	
	@FXML
	/**
	 * if the ESC key is pressed, returns the user back to the album page
	 * @throw IOException - required to load anchor pane
	 */
	private void esc(KeyEvent event) throws IOException {
		FXMLLoader loader2 = new FXMLLoader();   
		loader2.setLocation(
				getClass().getResource("/fxml/album.fxml"));
		AnchorPane root2 = (AnchorPane)loader2.load();
		AlbumController albumController = loader2.getController();
		albumController.start(window, album, user);
		window.setScene(new Scene(root2));
		window.show();
	}
	@FXML
	/**
	 * once the > button is pressed, slideshows the next image
	 * 
	 */
	private void next(ActionEvent evt) throws IOException {
		index++;
		if(index==album.getPhotos().size()-1) {
			nbutton.setDisable(true);
			
		}
		else {
			nbutton.setDisable(false);
		}
		if(index==0) {
			pbutton.setDisable(true);
		
		}
		else {
			pbutton.setDisable(false);
		}
		imageview.setImage(album.getPhotos().get(index).getImage());
	}
	
 
	@FXML			//sometimes the esc function glitches out
	/**
	 * there is a bug with the ESC key detection, user can instead press the X button to exit
	 * @throw IOException - required to load anchor pane
	 */
	private void return1(ActionEvent evt) throws IOException {
		FXMLLoader loader2 = new FXMLLoader();   
		loader2.setLocation(
				getClass().getResource("/fxml/album.fxml"));
		AnchorPane root2 = (AnchorPane)loader2.load();
		AlbumController albumController = loader2.getController();
		albumController.start(window, album, user);
		window.setScene(new Scene(root2));
		window.show();
	}
}
