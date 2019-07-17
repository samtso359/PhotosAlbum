package fxml;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.album;
import models.user;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This is the class for UserController, which contains all the features for the user page after logged in as a user
 *
 */
public class UserController implements Serializable {
	/**
	 * This is an ArrayList of saved albums that the user is able to see
	 */
	ArrayList<album> albums;
	/**
	 * This String stores the directory name for the serialized data, in this case being "dat" folder
	 */
	public static final String storeDir = "dat";
	/**
	 * This String stores the filename for save data 
	 */
	public static String storeFile = null;
	
	private ObservableList<album> obsList = FXCollections.observableArrayList();  
	/**
	 * window stores the access point to the current window
	 */
	private Stage window;
	@FXML         
	ListView<album> listView;
	user user;
	
	
	/**
	 * starts the stage, loads the user data 
	 * @param user - passes over the access point of the current user
	 * @param mainStage - passes over the access point of mainStage to this stage
	 */
	public void start(Stage mainStage, user user) {
		this.user = user;
		storeFile = user.getUsername()+".dat";
				
		window = mainStage;
		albums = user.getAlbums();
		
		obsList.addAll(albums);
		
		
		listView.setItems(obsList); 
		
	      listView.getSelectionModel().select(0);
	      window.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  try {
					writeApp(user);
				} catch (IOException e) {
					e.printStackTrace();
				}
	          }
	      });      

	}
	
	/**
	 * Write object for serialization on user data
	 * @param user - the user that needs to be saved
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
	 * once the logOut button is pressed, returns to the login page
	 * 
	 */
	private void logOut(ActionEvent evt) throws IOException {

		writeApp(user);
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/fxml/login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		LoginController loginController = loader.getController();
		loginController.start(window);
		window.setScene(new Scene(root));
		window.show();
		
		
	}
	@FXML
	/**
	 * once the create album button is pressed, a new window will pop out to ask the user to enter the new album name
	 * 
	 */
	private void createAlbum(ActionEvent evt){
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Add");
		dialog.setHeaderText("Add album");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		TextField name = new TextField("");
		name.setPromptText("Album Name");
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
        			obsList.add(temp);
        			
                    return name.getText();
        		}
            }
            return null;
        });
		dialog.show();
	}
	
	@FXML
	/**
	 * once the Change Album Name button is pressed, a new window will pop out to ask the user to enter the new album name
	 * 
	 */
	private void changeAlbumname(ActionEvent evt){
		if(obsList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Nothing to change name with");
   	        alert.showAndWait();
   	        return;
    	}
		album item = listView.getSelectionModel().getSelectedItem();
		if(item == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Please select an Album to change name with");
   	        alert.showAndWait();
   	        return;
		}
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(window);
		dialog.setTitle("Change album name");
		dialog.setHeaderText("Change album name");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		TextField name = new TextField("");
		name.setPromptText("Album Name");
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
        	           "Album name with at least 1 character is Required");

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
            		item.setAlbumName(name.getText());
            		obsList.setAll(albums);
            		listView.setItems(obsList); 
                    return name.getText();
        		}
            }
            return null;
        });
		dialog.show();
	}
	
	@FXML
	/**
	 * with an album selected, user can press Open Album button to open up the album to see the photos in that album
	 * 
	 */
	private void openAlbum(ActionEvent evt) throws IOException {
		if(obsList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Nothing to Open");
   	        alert.showAndWait();
   	        return;
    	}
		
		album item = listView.getSelectionModel().getSelectedItem();
		if(item == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Please select an Album to open");
   	        alert.showAndWait();
   	        return;
		}
		FXMLLoader loader2 = new FXMLLoader();   
		loader2.setLocation(
				getClass().getResource("/fxml/album.fxml"));
		AnchorPane root2 = (AnchorPane)loader2.load();
		AlbumController albumController = loader2.getController();
		albumController.start(window, item, user);
		window.setScene(new Scene(root2));
		window.show();
		
	}
	
	
	@FXML
	/**
	 * With an album selected, pressing the Delete Album button will result in a pop up window asking for confirmation on deleting the corresponding album
	 * 
	 */
	private void deleteAlbum(ActionEvent evt){
		if(obsList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(window);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Nothing left to delete");
   	        alert.showAndWait();
   	        return;
    	}
		album item = listView.getSelectionModel().getSelectedItem();
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
            	
            	if (obsList.size() == 1) {
        		      listView
        		        .getSelectionModel()
        		        .selectedIndexProperty()
        		        .removeListener(
        		           (obs, oldVal, newVal) -> 
        		           showItemInputDialog(window));

            		obsList.remove(item);
            		albums.remove(item);
            	}
     
            	else if ( obsList.size()-1 == obsList.indexOf(item)) {
            		obsList.remove(item);
            		albums.remove(item);
        			listView.getSelectionModel().select(obsList.size()-1);
            	}
            	else {
            		int index =obsList.indexOf(item);
            		if (obsList.indexOf(item) == 0) {
            			listView.getSelectionModel().select(index+ 1);
            			obsList.remove(item);
            			albums.remove(item);

            		}
            		else {
            			obsList.remove(item);
            			albums.remove(item);
            			listView.getSelectionModel().select(index);
            		}
            	}

            }
            return null;
        });
	}
	
	/**
	 * the function allows details of an selected object from listView to be shown 
	 * 
	 */
	private void showItemInputDialog(Stage mainStage) {      
		if (obsList.size() == 0) {
			return;
		}
		album item = listView.getSelectionModel().getSelectedItem();
		

      	  
   }
}
