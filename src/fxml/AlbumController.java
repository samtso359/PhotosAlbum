package fxml;

/**
 * @author Laishek Tso, Andrew Ma
 *
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.geometry.Orientation;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import models.album;
import models.photo;
import models.tag;
import models.user;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.image;

/**
 * This is the class for AlbumController, which contains all the features in an album page
 */
public class AlbumController implements Serializable{
	/**
	 * album stores the access point to the current album
	 */
	album album;
	/**
	 * user stores the access point to the current user
	 */
	user user;
	
	/**
	 * window stores the access point to the current window
	 */
	private Stage window;
	/**
	 * This String stores the directory name for the serialized data, in this case being "dat" folder
	 */
	public static final String storeDir = "dat";
	/**
	 * This String stores the filename for save data 
	 */
	public static String storeFile = null;
	
	ObservableList<photo> ophotoList;
	ObservableList<String> oTagList;
	ObservableList<String> TagTypeList;

	@FXML         
	ListView<photo> listView;
	@FXML
	Text displayDate;
	@FXML
	Text displayCaption;
	@FXML
	Text displayLocationTag;
	@FXML
	ListView<String> tagNames;
	@FXML
	DialogPane addTagsDiag;
	@FXML
	ImageView imageview;
	@FXML
	Pane pane1;
	@FXML
	ChoiceBox tagTypes;
	
	/**
	 * starts the stage, loads the data
	 * @param user - passes over the access point of the current user
	 * @param mainStage - passes over the access point of mainStage to this stage
	 * @param album - passes over the access point of the current album to this stage
	 */
	public void start(Stage mainStage, album album, user user) {
		String holder = "";
		storeFile = user.getUsername()+".dat";
		ophotoList =FXCollections.observableArrayList(album.getPhotos());
		window = mainStage;
		 window.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  try {
					writeApp(user);
				} catch (IOException e) {
					e.printStackTrace();
				}
	          }
	      });      
		this.album = album;
		this.user = user;
		ArrayList<photo> photos = album.getPhotos();
		listView.setItems(ophotoList);
		listView.setCellFactory(new Callback<ListView<photo>, ListCell<photo>>() {
			@Override
			public ListCell<photo> call(ListView<photo> photoList) {
				return new image();
			}	
		});
		TagTypeList = FXCollections.observableArrayList(user.getTagTypes());
		if(ophotoList.size()>0) {
			listView.getSelectionModel().select(0);
			photo item = listView.getSelectionModel().getSelectedItem();

			
			if (item.getDate()!= null) {
		  	  	displayDate.setText(item.getDate().getTime().toString());
			}
			
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
			
	  		displayCaption.setText(item.getCaption());
	  		}
		tagTypes.setItems(TagTypeList);
  	  	listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItemInputDialog(mainStage));

	}
	/**
	 * Write objects for serialization
	 * @param user - is a list of existing users
	 * @throws IOException - required for creating new FileOutputStream
	 */
	public static void writeApp(user user) throws IOException{
			
			FileOutputStream temp = new FileOutputStream(storeDir+File.separator+storeFile);
			
			ObjectOutputStream oss = new ObjectOutputStream(temp);
			oss.writeObject(user);
			oss.flush();
			oss.close();
			temp.close();
		}

	
	@FXML
	/**
	 * allow user to choose the tag type that wants to be shown for the selected photo
	 * 
	 */
	private void chooseTagType(ActionEvent evt) {
		photo item = listView.getSelectionModel().getSelectedItem();
		if (item == null) {
			return;
		}
		
		oTagList=FXCollections.observableArrayList();
		ArrayList<tag> holder = item.getTagList();
		if(tagTypes.getValue()==null) {
			return;
		}
		for (int i = 0; i < holder.size();i++) {
			if(holder.get(i).getType().equals(tagTypes.getValue().toString())){
				oTagList=FXCollections.observableArrayList(holder.get(i).getTagList());
				break;
			}
		}
		tagNames.setItems(oTagList);
		tagTypes.getValue();
	}
	
	
	@FXML
	/**
	 * add a photo through file explorer
	 * 
	 */
	private void addPhoto(ActionEvent evt) throws IOException {
		final FileChooser chooser = new FileChooser();
		File newPhotoFile = chooser.showOpenDialog(window);
		if (newPhotoFile != null) {
			Image image = new Image(newPhotoFile.toURI().toString());
			newPhotoFile.lastModified();
			Calendar lastModDate = Calendar.getInstance();
			lastModDate.setTimeInMillis(newPhotoFile.lastModified());
			lastModDate.set(Calendar.MILLISECOND,0);
			photo newphoto = new photo(image, lastModDate, newPhotoFile.toURI().toString());
			if (album.getPhotos().contains(newphoto)) {
        		Alert alert = new Alert(AlertType.INFORMATION);
      	      	alert.initOwner(window);
      	      	alert.setTitle("ERROR: Duplicate entry");
      	      	alert.setHeaderText(
    	           "Photo already exists in this Album!");
      	      	alert.showAndWait();
			}
			else {
				
				album.getPhotos().add(newphoto);
				ophotoList.add(newphoto);
				listView.getSelectionModel().select(ophotoList.size()-1);
			}

		}
	}
	
	@FXML
	/**
	 * Deletes the selected photo
	 * 
	 */
	private void deletePhoto(ActionEvent evt) throws IOException {
		if(ophotoList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Nothing left to delete");
   	        alert.showAndWait();
   	        return;
    	}
		photo item = listView.getSelectionModel().getSelectedItem();
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Delete");
		dialog.setHeaderText("Are you sure you want to Delete?");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		dialog.show();
		dialog.setResultConverter((ButtonType button) -> {
			int badInput = 0;
            if (button == ButtonType.OK) {
            	if (ophotoList.size() == 1) {
        		      listView
        		        .getSelectionModel()
        		        .selectedIndexProperty()
        		        .removeListener(
        		           (obs, oldVal, newVal) -> 
        		           showItemInputDialog(window));

            		ophotoList.remove(item);
            		album.getPhotos().remove(item);
       
        				
           			 displayCaption.setText("");
           			 oTagList=FXCollections.observableArrayList();
           			 tagNames.setItems(oTagList);
           		
            	}
            	else if ( ophotoList.size()-1 == ophotoList.indexOf(item)) {
            		ophotoList.remove(item);
            		album.getPhotos().remove(item);
        			listView.getSelectionModel().select(ophotoList.size()-1);
            	}
            	else {
            		int index =ophotoList.indexOf(item);
            		if (ophotoList.indexOf(item) == 0) {
            			listView.getSelectionModel().select(index+ 1);
            			ophotoList.remove(item);
            			album.getPhotos().remove(item);
            		}
            		else {
            			ophotoList.remove(item);
            			album.getPhotos().remove(item);
            			listView.getSelectionModel().select(index);
            		}
            	}
            }
            return null;
        });
	}
	/**
	 * the function allows details of an selected object from listView to be shown 
	 */
	private void showItemInputDialog(Stage mainStage) {      
		if (ophotoList.size() == 0) {
			displayDate.setText("");
			imageview.setImage(null);
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
  	  	
  	  	pane1.getChildren().clear();
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
		oTagList=FXCollections.observableArrayList();
		ArrayList<tag> holder = item.getTagList();
		if(tagTypes.getValue()==null) {
			return;
		}
		for (int i = 0; i < holder.size();i++) {
			if(holder.get(i).getType().equals(tagTypes.getValue().toString())){
				oTagList=FXCollections.observableArrayList(holder.get(i).getTagList());
				break;
			}
		}
		tagNames.setItems(oTagList);

		tagNames.setItems(oTagList);
   }
	@FXML
	/**
	 * Return to the window showing a list of albums
	 * 
	 */
	private void returnTo(ActionEvent evt) throws IOException {
		writeApp(user);
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/fxml/user.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		UserController userController = loader.getController();
		userController.start(window, user);
		window.setScene(new Scene(root));
		window.show();
	}
	@FXML
	/**
	 * Will enter the search window, allows user to search image according to tags and dates
	 * 
	 */
	private void searchPhoto(ActionEvent evt) throws IOException {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/fxml/search.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		SearchController searchController = loader.getController();
		searchController.start(window, album, user);
		window.setScene(new Scene(root));
		window.show();
	}
	
	@FXML
	/**
	 * changes the caption of the current selected photo
	 * 
	 */
	private void changeCaption(ActionEvent evt) throws IOException {
		photo item = listView.getSelectionModel().getSelectedItem();
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Edit");
		dialog.setHeaderText("Change caption");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		TextField caption = new TextField("");
		caption.setPromptText("Caption");
		dialogPane.setContent(new VBox(8, caption));
		dialog.setResultConverter((ButtonType button) -> {
			int badInput = 0;
            if (button == ButtonType.OK) {
        			item.setCaption(caption.getText());
        			displayCaption.setText(item.getCaption());
                    return caption.getText();
        		}
            
            return null;
        });
		dialog.show();
	}
	@FXML
	/**
	 * a pop-up window will ask the user whether to move or copy photos to another existing album
	 * 
	 */
	private void move_copyTo(ActionEvent evt) throws IOException {
		if(ophotoList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Need at least 1 photo for this action");
   	        alert.showAndWait();
   	        return;
    	}
		
		ObservableList<String> actionOptions;
		actionOptions = FXCollections.observableArrayList();
		Label label = new Label("Current Albulms");
		ObservableList<album> albumNames = FXCollections.observableArrayList(user.getAlbums());
		actionOptions.add("Move To");
		actionOptions.add("Copy To");
		photo currPhoto = listView.getSelectionModel().getSelectedItem();
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("To Album");
		dialog.setHeaderText("To Album");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		ChoiceBox action = new ChoiceBox();
		ChoiceBox albumNameBox = new ChoiceBox();
		albumNameBox.setItems(albumNames);
		action.setItems(actionOptions);
		
		dialogPane.setContent(new VBox(8, action,label, albumNameBox ));
		dialog.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK) {
				
			    int index = -1;
			    int index2 =ophotoList.indexOf(currPhoto);
			    int self = 0;
			    if(albumNameBox.getValue().toString().equals(album.getAlbum())) {
			    	self = 1;
			    }
			    for(int i = 0; i<user.getAlbums().size(); i++) {
			    	if(albumNameBox.getValue().toString().equals((user.getAlbums().get(i).getAlbum()))) {
			    		index = i;
			    		
			    		break;
			    	}
			    }
			    
				if (action.getValue().equals("Move To")) {
					if(self==1) {
						Alert alert = new Alert(AlertType.INFORMATION);
			     	    alert.initOwner(window);
			   	      	alert.setTitle("Invalid Action");
			   	      	alert.setHeaderText("ERROR: Your are moving the photo to current album");
			   	        alert.showAndWait();
			   	        return null;
					}
		
					user.getAlbums().get(index).getPhotos().add(currPhoto);
					ophotoList.remove(currPhoto);
        			album.getPhotos().remove(currPhoto);
        			
        			listView.setItems(ophotoList);
        			listView.getSelectionModel().select(index2);
        			if(ophotoList.size()==0) {
        				
        			 displayCaption.setText("");
        			 oTagList=FXCollections.observableArrayList();
        			 tagNames.setItems(oTagList);
        			}
        			
					
				}
				else if (action.getValue().equals("Copy To")) {
					if(self==1) {
						ophotoList.add(currPhoto);
					}
					else{
						user.getAlbums().get(index).getPhotos().add(currPhoto);
					}
					
					listView.setItems(ophotoList);
					listView.getSelectionModel().select(index2);
				}

			}
			showItemInputDialog(window);
			return null;
		});

		dialog.showAndWait();


	}
	@FXML
	/**
	 * Starts the slide show with the photos in the current album
	 * 
	 */
	private void slideShow(ActionEvent evt) throws IOException {
		if(ophotoList.size()>0) {
			FXMLLoader loader2 = new FXMLLoader();   
			loader2.setLocation(
					getClass().getResource("/fxml/slideshow.fxml"));
			AnchorPane root2 = (AnchorPane)loader2.load();
			SlideshowController slideshowController = loader2.getController();
			slideshowController.start(window, album, user);
			window.setScene(new Scene(root2));
			
			window.setFullScreen(true);
			window.show();
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Nothing to slideshow");
   	        alert.showAndWait();
   	        return;
		}
	}
	
	@FXML
	/**
	 * adds another tag type
	 * 
	 */
	private void addTagType(ActionEvent evt) throws IOException{
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Add Tag Type");
		dialog.setHeaderText("Add Tag Type");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		

		TextField newTagTypeName = new TextField("");
		newTagTypeName.setPromptText("Add New Tag Type");
		TextField newTagType = new TextField("");
		newTagType.setPromptText("New Tag Type");
		dialogPane.setContent(new VBox(8, newTagType));
		
		dialog.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK) {
				if(newTagType.getText() != "" ) {
					user.addTagType(newTagType.getText());
					TagTypeList = FXCollections.observableArrayList(user.getTagTypes());
					tagTypes.setItems(TagTypeList);
					tagTypes.setValue(newTagType.getText());

				}
				
			}
			showItemInputDialog(window);
			return null;
		});
		dialog.showAndWait();

	}
	
	@FXML
	/**
	 * pressing Add Tag button will result in a pop-up window, asking user for a tag input to add to the selected photo
	 * 
	 */
	private void addTag(ActionEvent evt) throws IOException {
		ObservableList<String> tagValues;
		tagValues = FXCollections.observableArrayList(user.getTagTypes());
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Add Tag");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		ChoiceBox tagType = new ChoiceBox();
		tagType.setItems(tagValues);
		TextField tag = new TextField("");
		tag.setPromptText("Add Tag(s)");
		Label message = new Label("Must Choose tag type");
		dialogPane.setContent(new VBox(8, tag, tagType, message));
		dialog.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK) {
				photo currPhoto = listView.getSelectionModel().getSelectedItem();
				if (!(tagType.getValue() == null)) {
					currPhoto.addTagToList(tagType.getValue().toString(), tag.getText());
					ArrayList<tag> holder = currPhoto.getTagList();
					if(tagTypes.getValue()==null) {
						return null;
					}
					for (int i = 0; i < holder.size();i++) {
						if(holder.get(i).getType().equals(tagTypes.getValue().toString())){
							oTagList=FXCollections.observableArrayList(holder.get(i).getTagList());
							break;
						}
					}
					tagNames.setItems(oTagList);
				}
				else {
				}
			}
			showItemInputDialog(window);
			return null;
		});

		dialog.showAndWait();


	}
	
	@FXML
	/**
	 * once a tag is selected, user can delete the tag with the Delete tag button
	 * 
	 */
	private void deleteTag(ActionEvent evt){
		photo currPhoto = listView.getSelectionModel().getSelectedItem();
		ObservableList<String> tagValues;
		tagValues = FXCollections.observableArrayList(user.getTagTypes());
		
		String currentTag = tagNames.getSelectionModel().getSelectedItem();
		if(oTagList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: No tag left to delete");
   	        alert.showAndWait();
   	        return;
    	}
		
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Delete");
		dialog.setHeaderText("Are you sure you want to Delete?");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		dialog.show();
		dialog.setResultConverter((ButtonType button) -> {
			int badInput = 0;
            if (button == ButtonType.OK) {
            	
            	if (oTagList.size() == 1) {
        		      tagNames
        		        .getSelectionModel()
        		        .selectedIndexProperty()
        		        .removeListener(
        		           (obs, oldVal, newVal) -> 
        		           showItemInputDialog(window));

            		oTagList.remove(currentTag);
            		
            		for(int i =0; i < currPhoto.getTagList().size();i++) {
            			if(currPhoto.getTagList().get(i).getType().toString().equals(tagTypes.getSelectionModel().getSelectedItem().toString())) {
            				for (int k = 0; k < currPhoto.getTagList().get(i).getTagList().size();k++) {
            					if(currPhoto.getTagList().get(i).getTagList().get(k).equals(currentTag)) {
            						currPhoto.getTagList().get(i).getTagList().remove(k);
            					}
            				}
            			}
            		}            	}
     
            	else if ( oTagList.size()-1 == oTagList.indexOf(currentTag)) {
            		oTagList.remove(currentTag);
            		
            		for(int i =0; i < currPhoto.getTagList().size();i++) {
            			if(currPhoto.getTagList().get(i).getType().toString().equals(tagTypes.getSelectionModel().getSelectedItem().toString())) {
            				for (int k = 0; k < currPhoto.getTagList().get(i).getTagList().size();k++) {
            					if(currPhoto.getTagList().get(i).getTagList().get(k).equals(currentTag)) {
            						currPhoto.getTagList().get(i).getTagList().remove(k);
            					}
            				}
            			}
            		}
            		
            		
        			tagNames.getSelectionModel().select(oTagList.size()-1);
            	}
            	else {
            		int index =oTagList.indexOf(currentTag);
            		if (oTagList.indexOf(currentTag) == 0) {
            			tagNames.getSelectionModel().select(index+ 1);
            			oTagList.remove(currentTag);
                		for(int i =0; i < currPhoto.getTagList().size();i++) {
                			if(currPhoto.getTagList().get(i).getType().toString().equals(tagTypes.getSelectionModel().getSelectedItem().toString())) {
                				for (int k = 0; k < currPhoto.getTagList().get(i).getTagList().size();k++) {
                					if(currPhoto.getTagList().get(i).getTagList().get(k).equals(currentTag)) {
                						currPhoto.getTagList().get(i).getTagList().remove(k);
                					}
                				}
                			}
                		}
            		}
            		else {
            			oTagList.remove(currentTag);
                		for(int i =0; i < currPhoto.getTagList().size();i++) {
                			if(currPhoto.getTagList().get(i).getType().toString().equals(tagTypes.getSelectionModel().getSelectedItem().toString())) {
                				for (int k = 0; k < currPhoto.getTagList().get(i).getTagList().size();k++) {
                					if(currPhoto.getTagList().get(i).getTagList().get(k).equals(currentTag)) {
                						currPhoto.getTagList().get(i).getTagList().remove(k);
                					}
                				}
                			}
                		}            			tagNames.getSelectionModel().select(index);
            		}
            	}

            }
            return null;
        });
	}
	
	/**
	 * the function centers an image
	 * @param imageView - the image that needs to be centered
	 * @return the centered image of ImageView type
	 */
	public static ImageView centerImage(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);
            
            

        }
		return imageView;
    }
	

	
}
