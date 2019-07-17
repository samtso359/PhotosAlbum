package models;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.scene.image.Image;

/**
 * This is the class for photo object
 *
 */
public class photo implements Serializable {
	/**
	 * This is a Caldendar object to store the last modified date of the photo
	 */
	Calendar lastModDate = null;
	/**
	 * This is a SerializeImg object to store the serialized version of the photo
	 */
	SerializeImg image = null;
	/**
	 * This is a string to store the caption of the photo
	 */
	String caption = "";
	/**
	 * This is a string to store the file path of the photo
	 */
	String path = "";
	/**
	 * This arrayList stores different tags
	 */
	ArrayList<tag> tags = new ArrayList<tag> ();
	
	/**
	 * photo constructor1
	 * @param image - takes the image file of the photo
	 * @param lastModDate - takes the last modified date of the photo
	 * @param path - takes the file path of the photo
	 */
	public photo(Image image, Calendar lastModDate, String path) {
		this.image = new SerializeImg(image);
		this.path = path;
		caption = "";
		this.lastModDate = lastModDate;
	}

	/**
	 * photo constructor2, only takes the image file
	 * @param image - takes the image file of the photo
	 */
	public photo(Image image) {
		caption = "";
		this.image = new SerializeImg(image);
	}
	
	/**
	 * This function allows access to the String form of file path
	 * @return the access point of the String form of the file path
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * This function allows access to the caption string
	 * @return the access point of caption string
	 */
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * This function allows access to the last modified date in Calendar type
	 * @return the access point of the last modified date
	 */
	public Calendar getDate() {
		return this.lastModDate;
	}
	
	 /**
		 * This function changes the caption of the photo 
		 * @param caption - the input caption that the user wants to change to
		 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * This function allows access to the image file in Image type
	 * @return the access point of the image file in Image type
	 */
	public Image getImage() {
		return image.getImage();
	}

	/**
	 * given a tag type and tag name, adds the tag to the corresponding tag list of that type
	 * @param type - the type of the tag
	 * @param tagName - the tag that wants to be added
	 */
	public void addTagToList(String type, String tagName) {
		for (int i =0; i <tags.size();i++) {
			if (tags.get(i).type.equals(type)){
				tags.get(i).addTag(tagName);
			}
		}
		tag holder = new tag(type,tagName);
		tags.add(holder);
	}
	
	/**
	 * This function allows access to the list of tags of the photo
	 * @return the access point of the list of tags of the photo
	 */
	public ArrayList<tag> getTagList(){
		return tags;
	}
	
	/**
	 * copy a given photo object and the caption and tags
	 * @param orig - the photo that the user wants to copy
	 * @return the copy of the photo
	 */
	public photo makeCopy(photo orig) {
		photo newCopy = new photo(orig.getImage(),orig.getDate(),orig.getPath());
		newCopy.setCaption(orig.getCaption());
		for(int i =0; i < orig.tags.size();i++) {
			for (int j =0; j< orig.tags.get(i).getTagList().size();j++) {
				newCopy.addTagToList(orig.tags.get(i).getType(), orig.tags.get(i).getTagList().get(j));

			}
		}
		
		return newCopy;
	}
}
