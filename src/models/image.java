package models;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.photo;
import javafx.util.Callback;

/**
 * This is the class for image object, main for displaying an image
 *
 */
public class image extends ListCell<photo> {

	/**
	 * This is an anchor pane that a stack pane will be on
	 */
	AnchorPane anchorPane = new AnchorPane();
	
	/**
	 * This is an stack pane that a image will be shown on
	 */
	StackPane stackPane = new StackPane();
	
	/**
	 * This is the image that wants to be shown
	 */
	ImageView imageView = new ImageView();
	


	/**
	 * Image Constructor, sets the dimensions of the image, and put it on the panes
	 */

	public image() {
		super();
		imageView.setFitWidth(100.0);
		imageView.setFitHeight(100.0);
		imageView.setPreserveRatio(true);
		StackPane.setAlignment(imageView, Pos.CENTER);
		stackPane.getChildren().add(imageView);
		anchorPane.getChildren().addAll(stackPane);
		anchorPane.setPrefHeight(84.0);
		setGraphic(anchorPane);

	}

	@Override

	/**
	 * Updates the image to a different image
	 * @param photo - the target image to update to
	 * @param empty - check if the image is a null
	 */
	public void updateItem(photo photo, boolean empty) {
		super.updateItem(photo, empty);
		setText(null);
		if (photo == null) {
			imageView.setImage(null);
		}
		if (photo != null) {
			imageView.setImage(photo.getImage());
		}
	}

}
