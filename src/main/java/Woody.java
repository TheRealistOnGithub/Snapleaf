import java.io.Serializable;
import java.util.ArrayList;

public class Woody extends Plant implements Serializable{
	
	private int price = 20;
	
	/**
	 * Woody plant constructor
	 * @param name
	 * @param info
	 * @param latinName
	 * @param supportedLeps
	 * @param size
	 * @param soilType
	 * @param moistureType
	 * @param sunType
	 * @param isPerennial
	 * @param x
	 * @param y
	 * @param imagePath
	 */
	public Woody(String name, ArrayList<String> info, String latinName, ArrayList<Lep> supportedLeps, int size, SoilType soilType, MoistureType moistureType, SunType sunType, boolean isPerennial, int x, int y, String imagePath) {
		this.setName(name);
		this.setInfo(info);
		this.setLatinName(latinName);
		this.supportedLeps = supportedLeps;
		this.size = size;
		this.soilType = soilType;
		this.moistureType = moistureType;
		this.sunType = sunType;
		this.isPerennial = isPerennial;
		this.x = x;
		this.y = y;
		this.imagePath = imagePath;
	}
	
	/**
	 * Get plant price.
	 * @return plant price
	 */
	public int getPrice() {
		return price;
	}
}
