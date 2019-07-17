package fxml;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.album;
import models.image;
import models.photo;
import models.user;

/**
 * This is the class for SearchController, which contains all the features for the search page
 *
 */
public class SearchController {
	/**
	 * album stores the access point to current album
	 */
	album album;
	/**
	 * user stores the access point to current user
	 */
	user user;
	
	/**
	 * A new ArrayList of type photo to be used to hold the search result photos
	 */
	ArrayList<photo> listOfPhotos = new ArrayList<photo> ();
	private ObservableList<photo> photoList = FXCollections.observableArrayList();  
	private ObservableList<String> tagList;

    private SortedList<photo> sortedPhotoList = new SortedList(photoList);
    
    /**
	 * An ArrayList of type album to be used as access point for the list of albums that the current user has
	 */
    ArrayList<album> albums;
	@FXML
	ListView<photo> listView;
	@FXML
	ToggleGroup group1;
	@FXML
	ChoiceBox tag1Choices;
	@FXML
	ChoiceBox tag2Choices;
	@FXML
	ChoiceBox logOperator;
	@FXML
	TextField tag1;
	@FXML 
	TextField tag2;
	@FXML
	DatePicker fromDate;
	@FXML
	DatePicker toDate;
	@FXML
	ImageView imageview;
	@FXML
	Text displayTags;
	@FXML
	Text displayCaption;
	@FXML
	Text displayDate;
	@FXML 
	RadioButton radio1;
	@FXML
	RadioButton radio2;
	@FXML
	Pane pane1;
	
	/**
	 * window stores the access point to the current window
	 */
	private Stage window;
	
	
	/**
	 * starts the stage, loads the data of the search page
	 * @param user - passes over the access point of the current user
	 * @param album - passes over the access point of the current album
	 * @param mainStage - passes over the access point of mainStage to this stage
	 */
	public void start(Stage mainStage, album album, user user) {
		this.user = user;
		this.album = album;
		albums = user.getAlbums();
		tagList = FXCollections.observableArrayList(user.getTagTypes());  
		ObservableList<String> andOr =  FXCollections.observableArrayList();
		andOr.add("And");
		andOr.add("Or");
		andOr.add(" ");
        sortedPhotoList.setComparator(new Comparator<photo>(){
            public int compare(photo arg0, photo arg1) {
            	return arg0.getDate().compareTo(arg1.getDate());
            	
            }
        });  
		listView.setItems(sortedPhotoList);
		listView.setCellFactory(new Callback<ListView<photo>, ListCell<photo>>() {
			@Override
			public ListCell<photo> call(ListView<photo> photoList) {
				return new image();
			}	
		});
		window = mainStage;
		this.album = album;
		this.user = user;
		radio1.setUserData("Tags:");
		radio2.setUserData("Date:");
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItemInputDialog(mainStage));
		tag1Choices.setItems(tagList);
		tag2Choices.setItems(tagList);
		logOperator.setItems(andOr);
		logOperator.setValue(" ");
	
	}
	/**
	 * the function allows details of an selected object from listView to be shown 
	 * 
	 */
	private void showItemInputDialog(Stage mainStage) {
		if(listView.getSelectionModel().getSelectedItem() == null) {
			return;
		}
		pane1.getChildren().clear();
		if (photoList.size() == 0) {
			return;
		}
		photo item = listView.getSelectionModel().getSelectedItem();
		if (item.getDate()!= null) {
	  	  	displayDate.setText(item.getDate().getTime().toString());
		}
		else {
	  	  	displayDate.setText("");
		}
  		displayCaption.setText(item.getCaption());
  	  	
		
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
		imageview.setImage(item.getImage());
	}
	@FXML
	/**
	 * once the enter button is pressed, executes the search 
	 * 
	 */
	public void enter(ActionEvent evt) {
		photoList.clear();
		listOfPhotos.clear();
		if (group1.getSelectedToggle().getUserData().toString().equals("Tags:")) {
			if(logOperator.getValue().toString().equals(" ")) {
				for (int i =0;i<album.getPhotos().size();i++) {
					for (int j = 0; j < album.getPhotos().get(i).getTagList().size();j++) {
						if (album.getPhotos().get(i).getTagList().get(j).getType().equals(tag1Choices.getValue().toString())){
							for(int k = 0; k < album.getPhotos().get(i).getTagList().get(j).getTagList().size();k++){
								if(album.getPhotos().get(i).getTagList().get(j).getTagList().get(k).equals(tag1.getText())) {
									photoList.add(album.getPhotos().get(i));
									listOfPhotos.add(album.getPhotos().get(i));
								}
							}
						}
					}
				}
			}
			else if(logOperator.getValue().toString().equals("And")){
				for (int i =0;i<album.getPhotos().size();i++) {
					boolean tag1check = false;
					boolean tag2check = false;
					for (int j = 0; j < album.getPhotos().get(i).getTagList().size();j++) {
						if (album.getPhotos().get(i).getTagList().get(j).getType().equals(tag1Choices.getValue().toString())){
							for(int k = 0; k < album.getPhotos().get(i).getTagList().get(j).getTagList().size();k++){
								if(album.getPhotos().get(i).getTagList().get(j).getTagList().get(k).equals(tag1.getText())) {
									tag1check = true;
								}
							}
						}
						if (album.getPhotos().get(i).getTagList().get(j).getType().equals(tag2Choices.getValue().toString())){
							for(int k = 0; k < album.getPhotos().get(i).getTagList().get(j).getTagList().size();k++){
								if(album.getPhotos().get(i).getTagList().get(j).getTagList().get(k).equals(tag2.getText())) {
									tag2check=true;
								}
							}
						}
					}
					if (tag1check && tag2check) {
						photoList.add(album.getPhotos().get(i));
						listOfPhotos.add(album.getPhotos().get(i));

					}
				}
				
			}
			else if(logOperator.getValue().toString().equals("Or")){
				for (int i =0;i<album.getPhotos().size();i++) {
					boolean tag1check = false;
					boolean tag2check = false;
					for (int j = 0; j < album.getPhotos().get(i).getTagList().size();j++) {
						if (album.getPhotos().get(i).getTagList().get(j).getType().equals(tag1Choices.getValue().toString())){
							for(int k = 0; k < album.getPhotos().get(i).getTagList().get(j).getTagList().size();k++){
								if(album.getPhotos().get(i).getTagList().get(j).getTagList().get(k).equals(tag1.getText())) {
									tag1check = true;
								}
							}
						}
						if (album.getPhotos().get(i).getTagList().get(j).getType().equals(tag2Choices.getValue().toString())){
							for(int k = 0; k < album.getPhotos().get(i).getTagList().get(j).getTagList().size();k++){
								if(album.getPhotos().get(i).getTagList().get(j).getTagList().get(k).equals(tag2.getText())) {
									tag2check=true;
								}
							}
						}
					}
					if (tag1check || tag2check) {
						photoList.add(album.getPhotos().get(i));
						listOfPhotos.add(album.getPhotos().get(i));

					}
				}
				
			}
			
		}
		else if (group1.getSelectedToggle().getUserData().toString().equals("Date:")) {
			
			LocalDate temp;
			if(fromDate.getValue().isAfter(toDate.getValue())) {
				
			}
			for(int i =0; i < album.getPhotos().size();i++) {
				temp = album.getPhotos().get(i).getDate().toInstant().atZone(album.getPhotos().get(i).getDate().getTimeZone().toZoneId()).toLocalDate();
				if(temp.compareTo(toDate.getValue()) <0 && temp.compareTo(fromDate.getValue())>0 ) {
					photoList.add(album.getPhotos().get(i));
					listOfPhotos.add(album.getPhotos().get(i));


				}
			}
			if (photoList.size() == 0) {

			}
			else {
				listView.getSelectionModel().select(0);
				
			}
		}
		
	}
	@FXML
	/**
	 * once the cancel button is pressed, return to the album window before the search windo
	 * 
	 */
	public void cancel(ActionEvent evt)throws IOException  {
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/fxml/album.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		AlbumController albumController = loader.getController();
		albumController.start(window, album, user);
		window.setScene(new Scene(root));
		window.show();
	}
	@FXML
	/**
	 * one the Create Album With Results button is pressed, a new album will be created with the result photos
	 * 
	 */
	public void createAlbumWithResults(ActionEvent evt) throws IOException{
		if(photoList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Need at least 1 photo for this action");
   	        alert.showAndWait();
   	        return;
    	}
		
		ObservableList<album> albumNames = FXCollections.observableArrayList(user.getAlbums());
		photo currPhoto = listView.getSelectionModel().getSelectedItem();
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Copy To New Album");
		dialog.setHeaderText("Copy To New Album");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		TextField name = new TextField("");
		name.setPromptText("New Album Name");
		dialogPane.setContent(new VBox(8, name));
		
		dialog.setResultConverter((ButtonType button) -> {
			int badInput = 0;
			if (button == ButtonType.OK) {
				if(name.getText().equals("")) {
            		badInput= 1;
            		Alert alert = 
          		         new Alert(AlertType.INFORMATION);
          	      alert.initOwner(window);
        	      alert.setTitle("ERROR: Input Required");
        	      alert.setHeaderText(
        	           "Album name is Required");

        	          alert.showAndWait();
        	          return null;
            	}					
				
        	for(int i = 0; i<albums.size(); i++) {
        		if(name.getText().equals(albums.get(i).getAlbum())) {
        			
            		badInput= 1;
            		Alert alert = 
          		         new Alert(AlertType.INFORMATION);
          	      	alert.initOwner(window);
          	      	alert.setTitle("ERROR: Duplicate entry");
          	      	alert.setHeaderText(
        	           "Album already exist!");
          	      	alert.showAndWait();
          	      	return null;
            	}
        	}
        	
        	if (badInput == 0) {
        		album temp = new album(name.getText());
        		albums.add(temp);
        		
        		temp.setPhotos(listOfPhotos);
                return name.getText();
    		}
			}
			//showItemInputDialog(window);
			return null;
		});

		dialog.showAndWait();


		
	}
	
	

}
