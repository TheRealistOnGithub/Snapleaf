import java.io.File;
import java.util.*;

public class Garden implements java.io.Serializable {
    private ArrayList<double[]> gardenVerticesCoordinates = new ArrayList<double[]>();
    private int width;
    private ArrayList<Plant> placedPlants = new ArrayList<Plant>();
    private HashMap<String, Lep> availableLeps = new HashMap<String, Lep>(); // Key is latin name for lep, Value is the
    // lep object
    private HashMap<String, Plant> availablePlants = new HashMap<String, Plant>(); // Key is image path for plant, Value
    // is plant object
    private ArrayList<Plant> currentList = new ArrayList<Plant>();
    private ArrayList<Lep> duplicateLeps = new ArrayList<Lep>();
    private int lepCount = 0;
    private SunType sunType;
    private MoistureType moistureType;
    private SoilType soilType;
    private int totalBudget;
    private int totalCost = 0;
    private int annual = 0;
    private int perennial = 1;
    private int herbacious = 6;
    private int woody = 20;
    private int bylepsupp = -1;
    private Set<Lep> supportedLeps = new HashSet<Lep>();
    private String resource_folder_path = "./src/main/resources/";
    private String plant_info_path = "plant_info/";
    private String plant_csv_path = "plant_info.csv";
    private String lep_info_path = "lep_info/";
    private String lep_csv_path = "lep_data.csv";
    private String default_plant_image_path = "defaultPlant.jpg";
    private String COMMA_DELIMITER = ",";
    private double gardenDrawingWidth;

    /**
     * Adds a plant into the garden based on the image path provided.
     *
     * @param imagePath Image path of plant being added into garden, used as the key
     *                  for available plants
     * @return
     */
    public int addPlant(String imagePath) {
        Plant currentPlant = availablePlants.get(imagePath);
        int lastIndex = placedPlants.size() - 1;
        try {
            Plant newPlant = (Plant) currentPlant.clone();
            newPlant.setId(String.valueOf(lastIndex + 1));
            this.placedPlants.add(newPlant);
            this.totalCost += newPlant.getPrice();
            for (Lep l : newPlant.supportedLeps) {
                this.addLep(l);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return lastIndex + 1;
    }

    /**
     * Returns an integer of the current supported leps
     *
     * @return a count of leps
     */
    public int getLepCount() {
        return lepCount;
    }


    /**
     * Removes the plant with the unique id from the garden's placed plants.
     *
     * @param id Unique Plant id
     */
    public void removePlant(int id) {

        this.totalCost -= (availablePlants.get(placedPlants.get(id).getImagePath()).getPrice());
        for (Lep l : placedPlants.get(id).supportedLeps) {
            if (duplicateLeps.contains(l)) {
                duplicateLeps.remove(l);
            } else {
                removeLep(l);
            }
        }
        this.placedPlants.set(id, null);
    }

    /**
     * Gets X value of a Plant with a unique id.
     *
     * @param id Unique Plant id
     * @return x coordinate of plant
     */
    public double getPlantX(String id) {
        return getPlantWithId(id).getX();
    }

    /**
     * Gets Y value of a Plant with a unique id.
     *
     * @param id Unique Plant id
     * @return y coordinate of plant
     */
    public double getPlantY(String id) {
        return getPlantWithId(id).getY();
    }

    /**
     * Sets X value of a Plant with a unique id.
     *
     * @param id Unique Plant id
     */
    public void setPlantX(String id, double x) {
        getPlantWithId(id).setX(x);
    }

    /**
     * Sets Y value of a Plant with a unique id.
     *
     * @param id Unique Plant id
     */
    public void setPlantY(String id, double y) {
        getPlantWithId(id).setY(y);
    }

    /**
     * Retrieves Plant object with given unique id.
     *
     * @param id Unique Plant id
     * @return plant object with id
     */
    public Plant getPlantWithId(String id) {
        return placedPlants.get(Integer.parseInt(id));
    }

    /**
     * Add vertex to garden pane.
     *
     * @param vertex coordinates of vertex to be added.
     */
    public void addVertex(double[] vertex) {
        this.gardenVerticesCoordinates.add(vertex);
    }

    /**
     * Getter for the arrayList of parameters
     *
     * @return Arraylist of parameters
     */
    public List<String> getParameters() {
        return new ArrayList<String>();
    }

    /**
     * Add lep to supported leps
     *
     * @param lep lep to be added.
     */
    public void addLep(Lep lep) {
        if (this.supportedLeps.add(lep)) {
            lepCount++;
        } else {
            this.duplicateLeps.add(lep);
        }
    }

    /**
     * Remove lep from supported leps
     *
     * @param lep lep to be removed from supported leps
     */
    public void removeLep(Lep lep) {
        if (this.supportedLeps.remove(lep)) {
            lepCount--;
        }
    }

    /**
     * Retrieves Array of X,Y coordinates for garden border
     *
     * @return garden border vertices
     */
    public ArrayList<double[]> getGardenVerticesCoordinates() {
        return gardenVerticesCoordinates;
    }

    /**
     * Sets Array of X,Y coordinates for garden border
     *
     * @param gardenVerticesCoordinates Array of X,Y coordinates
     */
    public void setGardenVerticesCoordinates(ArrayList<double[]> gardenVerticesCoordinates) {
        this.gardenVerticesCoordinates = gardenVerticesCoordinates;
    }

    /**
     * Garden width getter.
     *
     * @return garden width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Garden width setter.
     *
     * @param width garden width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Garden sun type getter.
     *
     * @return garden sun type
     */
    public SunType getSunType() {
        return sunType;
    }

    /**
     * Garden sun type setter.
     *
     * @param sunType garden sun type
     */
    public void setSunType(SunType sunType) {
        this.sunType = sunType;
    }

    /**
     * Garden moisture type getter.
     *
     * @return garden moisture type
     */
    public MoistureType getMoistureType() {
        return moistureType;
    }

    /**
     * Garden moisture type setter.
     *
     * @param moistureType garden moisture type
     */
    public void setMoistureType(MoistureType moistureType) {
        this.moistureType = moistureType;
    }

    /**
     * Garden soil type getter.
     *
     * @return garden soil type
     */
    public SoilType getSoilType() {
        return soilType;
    }

    /**
     * Garden soil type setter.
     *
     * @param soilType garden soil type
     */
    public void setSoilType(SoilType soilType) {
        this.soilType = soilType;
    }

    /**
     * Garden total budget getter.
     *
     * @return garden total budget
     */
    public int getTotalBudget() {
        return totalBudget;
    }

    /**
     * Garden total budget setter
     *
     * @param totalBudget
     */
    public void setTotalBudget(int totalBudget) {
        this.totalBudget = totalBudget;
    }

    /**
     * Garden placed plant getter.
     *
     * @return plants placed into garden
     */
    public ArrayList<Plant> getPlacedPlants() {
        return placedPlants;
    }

    /**
     * Garden get current list of plants to be displayed
     *
     * @return current list of plants to be displayed
     */
    public ArrayList<Plant> getCurrentList() {
        return currentList;
    }

    /**
     * Garden total purchase cost.
     *
     * @return total cost of purchasing garden
     */
    public int getTotalCost() {
        return totalCost;
    }

    /**
     * Currently supported leps in the garden based on placed plants.
     *
     * @return supported leps in the garden
     */
    public Set<Lep> getSupportedLeps() {
        return supportedLeps;
    }

    /**
     * Read lep_data.csv to add each lep to their plants.
     */
    public void setAvailableLeps() {
        try (Scanner sc = new Scanner(new File(resource_folder_path + lep_info_path + lep_csv_path))) {
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            while (sc.hasNextLine()) {
                ArrayList<String> values = getRecordFromLine(sc.nextLine());
                String latinName = values.get(1) + " " + values.get(2);
                Lep currentLep;
                String plantImagePath = values.get(0);
                if (availablePlants.containsKey(plantImagePath)) {
                    if (!availableLeps.containsKey(latinName)) {
                        ArrayList<String> info = new ArrayList<>();
                        info.add(values.get(4));
                        currentLep = new Lep(latinName, values.get(3), info);
                        availableLeps.put(latinName, currentLep);
                    } else {
                        currentLep = availableLeps.get(latinName);
                    }
                    Plant currentPlant = availablePlants.get(values.get(0));
                    currentPlant.addLep(currentLep);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Read plant info into garden to add each plant object into the garden.
     */
    public void setAvailablePlants() {
        // Future implementation will read data from CSV file to formulate the plant
        // data instead of manual data entry as being done currently
        readPlantCSV(plant_csv_path);
    }

    /**
     * Garden drawing width getter.
     *
     * @return garden drawing width
     */
    public double getGardenDrawingWidth() {
        return this.gardenDrawingWidth;
    }

    /**
     * Garden drawing width setter.
     *
     * @param gardenDrawingWidth garden drawing width
     */
    public void setGardenDrawingWidth(double gardenDrawingWidth) {
        this.gardenDrawingWidth = gardenDrawingWidth;
    }

    /**
     * Add plant data based on the parameters provided
     *
     * @param csvPath path for csv to be read
     */
    public void readPlantCSV(String csvPath) {
        int startingXPosition = 0;
        int startingYPosition = 0;
        try (Scanner sc = new Scanner(new File(resource_folder_path + plant_info_path + csvPath));) {
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            while (sc.hasNextLine()) {
                ArrayList<String> values = getRecordFromLine(sc.nextLine());
                String commonName = values.get(1);
                String latinName = values.get(3);
                // Set plant soil type
                SoilType soilType = SoilType.CLAY;
                switch (values.get(4)) {
                    case "CLAY":
                        soilType = SoilType.CLAY;
                        break;
                    case "LOAM":
                        soilType = SoilType.LOAM;
                        break;
                    case "SAND":
                        soilType = SoilType.SAND;
                        break;
                    default:
                        break;
                }
                // Set plant moisture type
                MoistureType moistureType = MoistureType.AVERAGE;
                switch (values.get(5)) {
                    case "MOIST":
                        moistureType = MoistureType.AVERAGE;
                        break;
                    case "DRY":
                        moistureType = MoistureType.DRY;
                        break;
                    case "WET":
                        moistureType = MoistureType.WET;
                        break;
                    default:
                        break;
                }
                // Set plant sun type
                SunType sunType = SunType.FULLSHADE;
                switch (values.get(6)) {
                    case "FULL SUN":
                        sunType = SunType.FULLSUN;
                        break;
                    case "FULL SHADE":
                        sunType = SunType.FULLSHADE;
                        break;
                    case "PART SHADE":
                        sunType = SunType.PARTSHADE;
                        break;
                    default:
                        break;
                }

                boolean isPerennial = values.get(7).equals("False") ? false : true;
                int size = Integer.parseInt(values.get(11));
                String imagePath = values.get(9);
                if (imagePath.isEmpty()) {
                    imagePath = default_plant_image_path;
                    continue;
                }
                Plant plant;
                if (values.get(13).equals("WOOD")) {
                    plant = new Woody(commonName, new ArrayList<String>(), latinName, new ArrayList<Lep>(), size,
                            soilType, moistureType, sunType, isPerennial, startingXPosition, startingYPosition,
                            imagePath);
                } else {
                    plant = new Herbacious(commonName, new ArrayList<String>(), latinName, new ArrayList<Lep>(), size,
                            soilType, moistureType, sunType, isPerennial, startingXPosition, startingYPosition,
                            imagePath);
                }
                if (plant.getSoilType() == this.soilType && plant.getSuntype() == this.sunType && plant.getMoistureType() == this.moistureType) {
                    this.availablePlants.put(imagePath, plant);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        currentList = new ArrayList<Plant>(availablePlants.values());
    }

    /**
     * Read in values from csv line.
     *
     * @param line string line read from a CSV file
     * @return list of string values from CSV
     */
    public ArrayList<String> getRecordFromLine(String line) {
        ArrayList<String> data = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                data.add(rowScanner.next());
            }
        }
        return data;
    }

    /**
     * Get list of available plants in the garden.
     *
     * @return list of available plants
     */
    public ArrayList<Plant> getAvailablePlants() {
        return new ArrayList<Plant>(availablePlants.values());
    }

    /**
     * Get data for a subset of available plants.
     *
     * @param filterBy integer to filter by
     * @return list of relevant data for plants
     */
    public ArrayList<Plant> subsetAvailablePlants(int filterBy) {
        ArrayList<Plant> keepThese = new ArrayList<Plant>();
        switch (filterBy) {
            case 0:
                for (Plant p : availablePlants.values()) {
                    if (!p.isPerennial() && !(filterBy == 1)) {
                        keepThese.add(p);
                    }
                }
                break;
            case 1:
                for (Plant p : availablePlants.values()) {
                    if (p.isPerennial() && (filterBy == 1)) {
                        keepThese.add(p);
                    }
                }
                break;
            case 6:
                for (Plant p : availablePlants.values()) {
                    if (p.getPrice() == filterBy) {
                        keepThese.add(p);
                    }
                }
                break;
            case 20:
                for (Plant p : availablePlants.values()) {
                    if (p.getPrice() == filterBy) {
                        keepThese.add(p);
                    }
                }
                break;
        }
        currentList = keepThese;
        return keepThese;
    }

    /**
     * Sorts available plant in garden with type that user selects
     *
     * @param sortBy int representing certain options the user can sort by
     * @return Arraylist of plants in sorted order
     */
    public ArrayList<Plant> sortsetAvailablePlants(int sortBy) {
        ArrayList<Plant> sorted = new ArrayList<Plant>();


        switch (sortBy) {
            case 0:
                for (Plant p : currentList) {
                    if (!p.isPerennial() && !(sortBy == 1)) {
                        sorted.add(p);
                    }
                }
                break;
            case 1:
                for (Plant p : currentList) {
                    if (p.isPerennial() && (sortBy == 1)) {
                        sorted.add(p);
                    }
                }
                break;
            case 6:
                for (Plant p : currentList) {
                    if (p.getPrice() == sortBy) {
                        sorted.add(p);
                    }
                }
                break;
            case 20:
                for (Plant p : currentList) {
                    if (p.getPrice() == sortBy) {
                        sorted.add(p);
                    }
                }
                break;
            case -1:
                Collections.sort(currentList, new Comparator<Plant>() {
                    @Override
                    public int compare(Plant p1, Plant p2) {
                        return p2.getSupportedLeps().size() - p1.getSupportedLeps().size();
                    }
                });
                break;
        }
		/*
		if (sortBy == 0 || sortBy == 1) {
			for (Plant p : currentList) {
				if ((p.isPerennial() && (sortBy == 1)) || (!p.isPerennial() && !(sortBy == 1))) {
					sorted.add(p);
				}
			}
		} else if (sortBy == 6 || sortBy == 20) {
			for (Plant p : currentList) {
				if (p.getPrice() == sortBy) {
					sorted.add(p);
				}
			}
		}
		else if (sortBy == -1) {
			for (Plant p : currentList) {
				if (sorted.isEmpty() || (p.getSupportedLeps().size() >= sorted.get(0).getSupportedLeps().size())) {
					sorted.add(0, p);
				}
				else {
					for (Plant q : sorted) {
						if (q.getSupportedLeps().size() < p.getSupportedLeps().size()) {
							sorted.add(sorted.indexOf(q), p);
							break;
						}
						
					}
				}
			}
		}
		*/
        for (Plant p : currentList) {
            if (!sorted.contains(p)) {
                sorted.add(p);
            }
        }
        return sorted;
    }

    /**
     * Applies the filter for the garden
     *
     * @param filterString String that determines filtering
     * @return returns a filtered arraylist of available plants
     */
    public ArrayList<Plant> applyFilter(String filterString) {
        return switch (filterString) {
            case "Annual" -> subsetAvailablePlants(annual);
            case "Perennial" -> subsetAvailablePlants(perennial);
            case "Herbacious" -> subsetAvailablePlants(herbacious);
            case "Woody" -> subsetAvailablePlants(woody);
            default -> getAvailablePlants();
        };
    }

    /**
     * Applies the sorting for the garden
     *
     * @param sortString String that determines sorting
     * @return
     */
    public ArrayList<Plant> applySort(String sortString) {
        return switch (sortString) {
            case "Annual" -> sortsetAvailablePlants(annual);
            case "Perennial" -> sortsetAvailablePlants(perennial);
            case "Herbacious" -> sortsetAvailablePlants(herbacious);
            case "Woody" -> sortsetAvailablePlants(woody);
            case "Leps Supported" -> sortsetAvailablePlants(bylepsupp);
            default -> getAvailablePlants();
        };
    }


    /**
     * Returns a plant object given an image path or null if the plant is not in available plants
     *
     * @param imagePath path to the plants image
     * @return plant object
     */
    public Plant getPlantFromPath(String imagePath) {
        return availablePlants.get(imagePath);
    }

    /**
     * Function to return plant with the passed in latin name
     *
     * @param latinName latin name to try to find in plants
     * @return Plant with the passed in latin name
     */
    public Plant getPlantWithLatinName(String latinName) {
        for (Plant plant : availablePlants.values()) {
            if (plant.getLatinName().equals(latinName)) {
                return plant;
            }
        }
        return null;
    }

    /**
     * Method to reset entire garden parameters.
     */
    public void resetGarden() {
        availablePlants = new HashMap<>();
        availableLeps = new HashMap<>();
        lepCount = 0;
        placedPlants = new ArrayList<Plant>();
        totalCost = 0;
        supportedLeps = new HashSet<>();
    }
}