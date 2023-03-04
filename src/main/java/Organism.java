import java.util.ArrayList;

import javafx.scene.image.Image;

public abstract class Organism implements java.io.Serializable {
	private String name;
	private ArrayList<String> info;
	private String latinName;
	private Image image;

	/**
	 * Get organism name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set organism name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get organism info list
	 * @return organism info list
	 */
	public ArrayList<String> getInfo() {
		return info;
	}

	/**
	 * Set organism info list
	 * @param info organism info list
	 */
	public void setInfo(ArrayList<String> info) {
		this.info = info;
	}

	/**
	 * Add item to organism info list.
	 * @param info information item to be added
	 */
	public void addInfo(String info) {
		this.info.add(info);
	}

	/**
	 * Get latin (scientific) name of organism
	 * @return latin name
	 */
	public String getLatinName() {
		return latinName;
	}

	/**
	 * Set latin (scientific) name of organism
	 * @param latinName latin name
	 */
	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	/**
	 * Get plant image object.
	 * @return plant image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Set plant image object
	 * @param image plant image
	 */
	public void setImage(Image image) {
		this.image = image;
	}
}
