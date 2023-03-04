import java.io.Serializable;
import java.util.ArrayList;

public class Lep extends Organism implements Serializable{
	private ArrayList<Plant> favoredPlants;
	private int wingSpan;
	private String color;
	private Endangered endangerStatus;
	private String imagePath;
	
	/**
	 * Instantiate lep object
	 * @param latinName latin name of lep
	 * @param info lep information
	 */
	public Lep(String latinName, String imagePath, ArrayList<String> info) {
		this.setLatinName(latinName);
		this.setInfo(info);
		this.imagePath = imagePath;
	}
	
	/**
	 * Get favored plants for lep
	 * @return
	 */
	public ArrayList<Plant> getFavoredPlants() {
		return this.favoredPlants;
	}
	
	/**
	 * Add favored plant to lep.
	 * @param plant plant favored by lep
	 */
	public void addFavoredPlant(Plant plant) {
		this.favoredPlants.add(plant);
	}
	
	/**
	 * Get lep color
	 * @return lep color
	 */
	public String getColor() {
		return this.color;
	}
	
	/**
	 * Get lep endangered status
	 * @return endangered status
	 */
	public Endangered getEndangeredStatus() {
		return this.endangerStatus;
	}
	
	/**
	 * Get lep wing span
	 * @return wing span
	 */
	public int getWingSpan() {
		return this.wingSpan;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
}
