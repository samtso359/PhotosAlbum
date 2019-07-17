package fxml;

/**
 * @author Laishek Tso, Andrew Ma
 *
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.user;
import java.io.Serializable;

/**
 * This is the class for AdminController, which contains all the features for admin
 *
 */
public class AdminController implements Serializable {
	/**
	 * This is an ArrayList of existing users that admin is able to see
	 */
ArrayList<user> users;

/**
 * This String stores the directory name for the serialized data, in this case being "dat" folder
 */
public static final String storeDir = "dat";
/**
 * This String stores the filename for save data 
 */
public static final String storeFile = "admin.dat";
private ObservableList<user> obsList = FXCollections.observableArrayList();  
/**
 * mainStage stores the access point to the current window
 */
Stage mainStage;

/**
 * set this current stage as main stage
 * @param stage - mainStage will set equal to this stage
 * 
 */
public void setMainStage(Stage stage) {
	mainStage = stage;
}
	@FXML         
	ListView<user> listView;
	Stage window;
	
	/**
	 * starts the stage, loads the data
	 * @param users - passes over the access point of an ArrayList of users
	 * @param mainStage - passes over the access point of mainStage to this stage
	 */
	public void start(Stage mainStage, ArrayList<user> users) {
		this.users = users;
		window = mainStage;
		obsList.addAll(users);
		
		
		listView.setItems(obsList); 
		
	      listView.getSelectionModel().select(0);
	      window.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  try {
					writeApp(users);
				} catch (IOException e) {
					e.printStackTrace();
				}
	          }
	      });        
	      
	}
	/**
	 * Write objects for serialization
	 * @param users2 - is a list of existing users
	 * @throws IOException - required for creating new FileOutputStream
	 */
	public static void writeApp(ArrayList<user> users2) throws IOException{
		
		FileOutputStream temp = new FileOutputStream(storeDir+File.separator+storeFile);
		
		ObjectOutputStream oss = new ObjectOutputStream(temp);
		oss.writeObject(users2);
		oss.flush();
		oss.close();
		temp.close();
	}
	
	
	@FXML
	/**
	 * the function allows logOut
	 */
	private void logOut(ActionEvent evt) throws IOException {

		writeApp(users);
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
	 * the function of creating a new user base on input
	 */
	private void createUser(ActionEvent evt){
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(mainStage);
		dialog.setTitle("Add");
		dialog.setHeaderText("Add user");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
		TextField name = new TextField("");
		name.setPromptText("Username");
		dialogPane.setContent(new VBox(8, name));
		dialog.setResultConverter((ButtonType button) -> {
			int badInput = 0;
            if (button == ButtonType.OK) {
            	if(name.getText().equals("")) {
            		badInput= 1;
            		Alert alert = 
          		         new Alert(AlertType.INFORMATION);
          	      alert.initOwner(mainStage);
        	      alert.setTitle("ERROR: Input Required");
        	      alert.setHeaderText(
        	           "Name is Required");

        	          alert.showAndWait();
        	          return null;
            	}
            	for(int i= 0; i <users.size(); i++) {
            		
            	
            	if(name.getText().equals(users.get(i).getUsername())) {
            		badInput= 1;
            		Alert alert = 
          		         new Alert(AlertType.INFORMATION);
          	      alert.initOwner(mainStage);
        	      alert.setTitle("ERROR: Duplicate entry");
        	      alert.setHeaderText(
        	           "User already exist!");

        	          alert.showAndWait();
        	          return null;
            	}
            	}
            
            	if (badInput == 0) {
            		user temp = new user(name.getText());
            		users.add(temp);
        			obsList.add(temp);
                    return null;
        		}
            }
            return null;
        });
		dialog.show();
	}

	
	@FXML
	/**
	 * the function deletes an selected user from ListView
	 */
	private void deleteUser(ActionEvent evt){
		if(obsList.size()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
     	    alert.initOwner(mainStage);
   	      	alert.setTitle("Invalid Action");
   	      	alert.setHeaderText("ERROR: Nothing left to delete");
   	        alert.showAndWait();
   	        return;
    	}
		user item = listView.getSelectionModel().getSelectedItem();
		Dialog<String> dialog = new Dialog<>();
		dialog.initOwner(mainStage);
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
        		           showItemInputDialog(mainStage));

            		obsList.remove(item);
            		users.remove(item);
            		File file = new File(storeDir+File.separator+item.getUsername()+".dat");
            		file.delete();
            	}
     
            	else if ( obsList.size()-1 == obsList.indexOf(item)) {
            		obsList.remove(item);
            		users.remove(item);
            		File file = new File(storeDir+File.separator+item.getUsername()+".dat");
            		file.delete();
        			listView.getSelectionModel().select(obsList.size()-1);
            	}
            	else {
            		int index =obsList.indexOf(item);
            		if (obsList.indexOf(item) == 0) {
            			listView.getSelectionModel().select(index+ 1);
            			obsList.remove(item);
                		users.remove(item);
                		File file = new File(storeDir+File.separator+item.getUsername()+".dat");
                		file.delete();

            		}
            		else {
            			obsList.remove(item);
            			users.remove(item);
            			listView.getSelectionModel().select(index);
            			File file = new File(storeDir+File.separator+item.getUsername()+".dat");
                		file.delete();
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
		user item = listView.getSelectionModel().getSelectedItem();

      	  
   }

}
