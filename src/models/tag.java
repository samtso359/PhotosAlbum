package models;
/**
 * @author Laishek Tso, Andrew Ma
 *
 */
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the class for tag object
 *
 */
public class tag implements Serializable {
	/**
	 * the string that stores the type of the tag
	 */
	String type;
	
	/**
	 * an arrayList that store the tags
	 */
	ArrayList<String> tags = new ArrayList<String> ();
	boolean singular = true;
	
	/**
	 * tag constructor1
	 * @param type - takes the type of the tag in String
	 * @param tag - takes an arrayList of tag 
	 * @param single - boolean that tells if the tag is unique for its type
	 */
	public tag(String type, ArrayList<String> tag, boolean single) {
		singular = single;
		this.type = type;
		tags = tag;
	}
	
	/**
	 * tag constructor2 , only takes type and the tag
	 * @param type - takes the type of the tag in String
	 * @param tag - takes an arrayList of tag 
	 */
	public tag(String type, String tag) {
		this.type = type;
		tags.add(tag);
	}
	
	 @Override
     public String toString() {
     	return this.tags.toString();
     }
	 
	 /**
		 * This function returns the access to the type of the tag in String
		 * @return the type of the tag
		 */
	 public String getType() {
		 return this.type;
	 }
	 
	 
		
		/**
		 * adds a tag for unique tags
		 * @param newTag - the tag that wants to be added
		 * 
		 */
	 public void addTag(String newTag) {
		 if (tags.contains(newTag)) {
			 
		 }
		 else {
			 tags.add(newTag);
		 }
	 }
	 
	 /**
		 * This function returns the access to array list of tags
		 * @return the array list of tags
		 */
	 public ArrayList<String> getTagList(){
		 return tags;
	 }
}
