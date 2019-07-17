package models;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * This is the class for album object
 *
 */
public class album implements Serializable{
	
	
	/**
	 * This is a String for album name
	 */
	String albumname;
	/**
	 * This is a ArrayList structure that store the photos in this current album
	 */
	ArrayList<photo> photos;
	
	/**
	 * album constructor
	 * @param albumname - takes a string to be the name of the album
	 */
	public album(String albumname) {
		this.albumname = albumname;
		this.photos = new ArrayList<photo>();
	}
	
	 @Override
     public String toString() {
     	return this.albumname;
     }
	 
	 /**
		 * This function changes the name of the album 
		 * @param albumname - the input album name that will be changed to
		 */
	 public void setAlbumName(String albumname){
		 this.albumname = albumname;
	 }
	
	 /**
		 * This function allow access to the album name
		 * @return the access point of the album name
		 */
	public String getAlbum() {
		return this.albumname;
	}
	
	/**
	 * This function allow access to the arraylist of the photos in this album
	 * @return the access point of the arraylist of the photos in this album
	 */
	public ArrayList<photo> getPhotos(){
		return this.photos;
	}
	
	/**
	 * This function copies another Arraylist of photos
	 * @param temp - is the target Arraylist of photos that needed to be copy 
	 */
	public void setPhotos(ArrayList<photo> temp) {
		for(int i =0; i < temp.size();i++) {
			this.photos.add(temp.get(i).makeCopy(temp.get(i)));
		}
	}
}
