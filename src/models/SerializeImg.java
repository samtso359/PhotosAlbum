 package models;
 /**
  * @author Laishek Tso, Andrew Ma
  *
  */
import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * This is the class for serialized photo
 *
 */
public class SerializeImg implements Serializable {


	  /**
	 * The serial number of the serialized photo
	 */

	private static final long serialVersionUID = 8560179233789253818L;
	
	/**
	 * This function gets the width of the serialized photo
	 * @return the width in integer
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * This function sets the width of the serialized photo
	 * @param width takes a width in integer
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * This function gets the height of the serialized photo
	 * @return the height in integer
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * This function sets the height of the serialized photo
	 * @param height takes a height in integer
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * This function serialized data of the photo in a 2-D array
	 * @return the pixel data of the photo in a 2-D array
	 */
	public int[][] getData() {
		return data;
	}

	/**
	 * This function sets the data of the serialized photo to another set of data
	 * @param data - another 2-D array of pixels of image
	 */
	public void setData(int[][] data) {
		this.data = data;
	}

	/**
	 * This function returns the serial number
	 * @return the serial number
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * This is width of the image
	 */
	private int width; 
	
	/**
	 * This is height of the image
	 */
	private int height;
	/**
	 * This is 2-D array of pixels of the image
	 */
	    private int[][] data;

	    /**
		 * SerializeImg constructor
		 * @param image - takes an image file of the photo and serialize it
		 */
	    public SerializeImg(Image image) {
	    	 setImage(image);
	    }
	   
		/**
		 * This function sets the data of the serialized photo to another set of data
		 * @param image - the image that is to be stored in a 2-D array of pixels
		 */
	    public void setImage(Image image) {
	        width = ((int) image.getWidth());
	        height = ((int) image.getHeight());
	        data = new int[width][height];

	        PixelReader r = image.getPixelReader();
	        for (int i = 0; i < width; i++) {
	            for (int j = 0; j < height; j++) {
	                data[i][j] = r.getArgb(i, j);
	            }
	        }

	    }
	    
	    /**
		 * This function creates the writable type for the image 
		 * @return the writable type for the image 
		 */
	    public Image getImage() {
	        WritableImage img = new WritableImage(width, height);

	        PixelWriter w = img.getPixelWriter();
	        for (int i = 0; i < width; i++) {
	            for (int j = 0; j < height; j++) {
	                w.setArgb(i, j, data[i][j]);
	            }
	        }

	        return img;
	    }
	
}
