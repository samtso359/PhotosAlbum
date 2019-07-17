package fxml;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
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
import models.user;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;


import java.util.ArrayList;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
/**
 * This is the class for LoginController, which contains all the features for login page
 *
 */
public class LoginController implements Serializable {
	
	/**
	 * mainStage stores the access point to the current window
	 */
	Stage mainStage;
	/**
	 * This String stores the directory name for the serialized data, in this case being "dat" folder
	 */
	public static final String storeDir = "dat";
	/**
	 * 
	 * This String stores the filename for save data 
	 */
	public static String storeFile = null;
	
	/**
	 * users stores the access point to the ArrayList of existing users
	 */
	ArrayList<user> users = null;
	
	/**
	 * mainStage stores the access point to the future user
	 */
	user user= null;
	//ArrayList<user> users = new ArrayList<user>();	//use this to initialize serializable
	
	/**
	 * set this current stage as main stage
	 * @param stage - mainStage will set equal to this stage
	 * 
	 */
	public void setMainStage(Stage stage) {
		mainStage = stage;
	}

	/**
	 * set the main stage to the current stage
	 * @param primaryStage - the stage that will be used to be mainstage
	 */
	public void start(Stage primaryStage) {
		setMainStage(primaryStage);
		

		
	}
	@FXML
	TextField username;
	
	/**
	 * read a serialized data
	 * 
	 * @throws IOException - required for serialization
	 * @throws ClassNotFoundException - required for readObject
	 * @return an ArrayList of existing users
	 */
	public static ArrayList<user> readapp()throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(storeDir + File.separator + storeFile));
		ArrayList<user> save = (ArrayList<user>)ois.readObject();
		return save;
	}
	@FXML
	/**
	 * once login is pushed, it will redirect to corresponding user page with its user data loaded
	 * @throw IOException - required to load anchor pane
	 * @throw ClassNotFoundException - required for read objects
	 */
	private void login(ActionEvent evt) throws IOException, ClassNotFoundException {
		//We are going to compare the input to a list of saved users later on
		
		//Comment these 5 lines when reset data
		FileInputStream fis = new FileInputStream(storeDir+File.separator+"admin.dat");
		ObjectInputStream ois = new ObjectInputStream(fis);
		users = (ArrayList) ois.readObject();
		ois.close();
		fis.close();
		
		if(username.getText().compareTo("admin")==0) {
			
			FXMLLoader loader2 = new FXMLLoader();   
			loader2.setLocation(
					getClass().getResource("/fxml/admin.fxml"));
			AnchorPane root2 = (AnchorPane)loader2.load();
			AdminController adminController = loader2.getController();
			adminController.start(mainStage, users);
			mainStage.setScene(new Scene(root2));
			mainStage.show();
		}
		else {
			int invalid = 1;
			
			for(int i=0; i<users.size();i++) {
				user=users.get(i);
		    
				if(users.get(i).getUsername().compareTo(username.getText())==0) {
					File temp = new File(storeDir+File.separator+username.getText()+".dat");
					if(temp.exists()) {
						storeFile = username.getText()+".dat";
						FileInputStream fis2 = new FileInputStream(storeDir+File.separator+username.getText()+".dat");
						ObjectInputStream ois2 = new ObjectInputStream(fis2);
						user = (user) ois2.readObject();
						ois2.close();
						fis2.close();
					}
					
					invalid = 0;
					FXMLLoader loader = new FXMLLoader();   
					loader.setLocation(
							getClass().getResource("/fxml/user.fxml"));
					AnchorPane root = (AnchorPane)loader.load();
					UserController userController = loader.getController();
					userController.start(mainStage, user);
					mainStage.setScene(new Scene(root));
					mainStage.show();
					
					
				}
			}
			if(invalid ==1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(mainStage);
				alert.setTitle("Invalid User");
				alert.setHeaderText("ERROR: User does not exist!");
				alert.showAndWait();
			}
	}
	}
	
	
	
}
