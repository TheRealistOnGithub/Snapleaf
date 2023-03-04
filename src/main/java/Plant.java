import java.util.ArrayList;

public abstract class Plant extends Organism implements Cloneable, java.io.Serializable{
	protected ArrayList<Lep> supportedLeps;
	protected int size;
	protected SoilType soilType;
	protected MoistureType moistureType;
	protected SunType sunType;
	protected boolean isPerennial;
	protected double x;
	protected double y;
	protected int price;
	protected String imagePath;
	protected String id;
	
	/**
	 * Plant object constructor.
	 * @param name plant name
	 * @param info plant information
	 * @param latinName plant scientific name
	 * @param supportedLeps plant supported leps
	 * @param size plant size
	 * @param soilType plant soil type
	 * @param moistureType plant moisture type
	 * @param sunType plant sun type
	 * @param isPerennial plant is perennial
	 * @param x plant x coordinate
	 * @param y plant y coordinate
	 * @param imagePath plant image path
	 */
	public Plant() {
	
	}

	/**
	 * Add lep to plant supported leps.
	 * @param lep
	 */
	public void addLep(Lep lep) {
		this.supportedLeps.add(lep);
	}
	
	/**
	 * Move plant to coordinates
	 * @param newX new x coordinate
	 * @param newY new y coordinate
	 */
	public void moveTo(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}

	/**
	 * Is the plant perennial getter.
	 * @return boolean of is perennial
	 */
	public boolean isPerennial() {
		return isPerennial;
	}

	/**
	 * Perennial boolean setter.
	 * @param isPerennial is plant perennial
	 */
	public void setPerennial(boolean isPerennial) {
		this.isPerennial = isPerennial;
	}

	/**
	 * Get plant x coordinate
	 * @return x coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Set plant x coordinate
	 * @param x set plant x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get plant y coordinate.
	 * @return plant y coordinate
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Set plant y coordinate.
	 * @param y plant y coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Set plant id
	 * @param id plant id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Plant supported leps getter.
	 * @return list of supported leps
	 */
	public ArrayList<Lep> getSupportedLeps() {
		return supportedLeps;
	}

	/**
	 * Get plant size.
	 * @return plant size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Set plant size.
	 * @param size plant size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Get plant soil type
	 * @return soil type
	 */
	public SoilType getSoilType() {
		return soilType;
	}

	/**
	 * Get plant moisture type.
	 * @return moisture type
	 */
	public MoistureType getMoistureType() {
		return moistureType;
	}

	/**
	 * Get plant sun type.
	 * @return sun type
	 */
	public SunType getSuntype() {
		return sunType;
	}
	
	/**
	 * Get plant price.
	 * @return plant price
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * Get plant image path.
	 * @return plant image path
	 */
	public String getImagePath() {
		return this.imagePath;
	}
	
	/**
	 * Get plant id.
	 * @return plant id
	 */
	public String getId() {
		return this.id;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
}
