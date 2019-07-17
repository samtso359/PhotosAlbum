package models;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the class for user object
 *
 */
public class user implements Serializable {
	/**
	 * This string stores the username
	 *
	 */
	String username;
	
	/**
	 * This arraylist stores the albums that the user has 
	 *
	 */
	ArrayList<album> albums;
	
	/**
	 * This arraylist stores the tag type that the user created 
	 *
	 */
	ArrayList<String> tagTypes = new ArrayList<String>();

	
	/**
	 * user constructor
	 * @param username - takes the username in string that the admin wants to create
	 */
	public user(String username){
		this.username = username;
		this.albums = new ArrayList<album>();
	}
	 @Override
     public String toString() {
     	return this.username;
     }
	 /**
		 * This function allows access to the String of username
		 * @return the access point of the username
		 */
	 public String getUsername() {
		 return this.username;
	 }
	 
	 /**
		 * This function allows access to the arrayList of the albums
		 * @return the access point to the arrayList of the albums
		 */
	 public ArrayList<album> getAlbums() {
		 return this.albums;
	 }
	 
	 /**
		 * This function allows access to the arrayList of the tag types
		 * @return the access point to the arrayList of the tag types
		 */
	 public ArrayList<String> getTagTypes(){
		 return this.tagTypes;
	 }
	 

	 /**
		 * This function adds tag type into the arrayList of tag type
		 * @param tagType - the tag Type in string that needs to be added
		 */
	 public void addTagType(String tagType) {
		 boolean alreadyThere = false;
		 for (int i =0; i < tagTypes.size();i++) {
			 if (tagTypes.get(i).equals(tagType)){
				 alreadyThere = true;
			 }
		 }
		 if (!alreadyThere) {
			 tagTypes.add(tagType);
		 }
	 }
}
