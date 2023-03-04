import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;
import java.util.HashMap;

@TestInstance(Lifecycle.PER_CLASS)
class GardenTest {
    Garden garden = new Garden();
    //Plant testPlant;
    Lep testLep;
    int[][] testVerts;
    double[] testVertex;
    String CSVPath = "herb_plant_info.csv";
    ArrayList<String> info = new ArrayList<String>();
    private ArrayList<Plant> placedPlants = new ArrayList<Plant>();
    private HashMap<String, Lep> availableLeps = new HashMap<String, Lep>(); // Key is latin name for lep, Value is the
    // lep object
    private HashMap<String, Plant> availablePlants = new HashMap<String, Plant>(); // Key is image path for plant, Value
    // is plant object
    private ArrayList<Plant> currentList = new ArrayList<Plant>();
    ArrayList<Lep> leps = new ArrayList<Lep>();
    Plant testPlant = new Herbacious("Common Milkweed", info, "Asclepias syriaca L.", leps, 10, SoilType.CLAY, MoistureType.DRY, SunType.FULLSHADE, false, 0, 0, "commonMilkweed.png");

    @BeforeAll
    public void init() {
        garden.setMoistureType(MoistureType.DRY);
        garden.setSunType(SunType.FULLSUN);
        garden.setSoilType(SoilType.LOAM);
        garden.setAvailableLeps();
        garden.setAvailablePlants();
    }

    @Test
    void testAddPlant() {
        Assert.assertEquals(garden.addPlant("indiangrass.jpg"), garden.getPlacedPlants().size() - 1);

    }

    @Test
    void testGetPlantX() throws CloneNotSupportedException {
        int lastIndex = garden.addPlant("indiangrass.jpg");
        Assert.assertEquals(garden.getPlantX(String.valueOf(lastIndex)), 0.0, 0.01);


    }

    @Test
    void testGetPlantY() {
        int lastIndex = garden.addPlant("indiangrass.jpg");
        Assert.assertEquals(garden.getPlantY(String.valueOf(lastIndex)), 0.0, 0.01);
    }

    @Test
    void testGetLepCount() {
        garden.addPlant("indiangrass.jpg");
        Assert.assertEquals(garden.getLepCount(), 1);
    }

    @Test
    void testRemovePlant() {
        int lastIndex = garden.addPlant("indiangrass.jpg");
        garden.removePlant(lastIndex);
        Assert.assertNull(garden.getPlacedPlants().get(lastIndex));
    }


    @Test
    void testAddVertex() {
        garden.addVertex(testVertex);
        Assert.assertEquals(garden.getGardenVerticesCoordinates().contains(testVertex), true);
    }

    @Test
    void testSetPlantX() {
        int lastIndex = garden.addPlant("indiangrass.jpg");
        garden.setPlantX(String.valueOf(lastIndex), 0.0);
        Assert.assertEquals(garden.getPlantX(String.valueOf(lastIndex)), 0.0, 0.01);

    }

    @Test
    void testSetPlantY() {
        int lastIndex = garden.addPlant("indiangrass.jpg");
        garden.setPlantY(String.valueOf(lastIndex), 0.0);
        Assert.assertEquals(garden.getPlantY(String.valueOf(lastIndex)), 0.0, 0.01);

    }

    @Test
    void testGetParameters() {
        //should return 0 since no parameters are set
        Assert.assertEquals(garden.getParameters().size(), 0);
    }

    @Test
    void testAddLep() {
        garden.addLep(testLep);
        Assert.assertEquals(garden.getLepCount(), 2);
    }

    @Test
    void testRemoveLep() {
        garden.removeLep(testLep);
        Assert.assertEquals(garden.getSupportedLeps().contains(testLep), false);
    }

    @Test
    void testGetGardenVerticesCoordinates() {
        Assert.assertEquals(garden.getGardenVerticesCoordinates().size(), 0);
    }

    @Test
    void testsetGardenVerticesCoordinates() {
        ArrayList<double[]> TestGardVerts = new ArrayList<>();
        TestGardVerts.add(new double[]{0.0});
        garden.setGardenVerticesCoordinates(TestGardVerts);
        Assert.assertEquals(garden.getGardenVerticesCoordinates().size(), 1);
    }

    @Test
    void testGetWidth() {
        garden.setWidth(1);
        Assert.assertEquals(garden.getWidth(), 1);
    }

    @Test
    void testSetWidth() {
        garden.setWidth(1);
        Assert.assertEquals(garden.getWidth(), 1);
    }

    @Test
    void testGetSunType() {
        garden.setSunType(SunType.FULLSHADE);
        Assert.assertEquals(garden.getSunType(), SunType.FULLSHADE);
    }

    @Test
    void testSetSunType() {
        garden.setSunType(SunType.FULLSHADE);
        Assert.assertEquals(garden.getSunType(), SunType.FULLSHADE);
    }

    @Test
    void testGetMoistureType() {
        garden.setMoistureType(MoistureType.WET);
        Assert.assertEquals(garden.getMoistureType(), MoistureType.WET);
    }

    @Test
    void testSetMoistureType() {
        garden.setMoistureType(MoistureType.WET);
        Assert.assertEquals(garden.getMoistureType(), MoistureType.WET);
    }

    @Test
    void testGetSoilType() {
        garden.setSoilType(SoilType.CLAY);
        Assert.assertEquals(garden.getSoilType(), SoilType.CLAY);
    }

    @Test
    void testSetSoilType() {
        garden.setSoilType(SoilType.CLAY);
        Assert.assertEquals(garden.getSoilType(), SoilType.CLAY);
    }

    @Test
    void testGetTotalBudget() {
        garden.setTotalBudget(69);
        Assert.assertEquals(garden.getTotalBudget(), 69);
    }

    @Test
    void testSetTotalBudget() {
        garden.setTotalBudget(69);
        Assert.assertEquals(garden.getTotalBudget(), 69);
    }

    @Test
    void testGetPlacedPlants() {
        int id = garden.addPlant("indiangrass.jpg");
        ArrayList<Plant> gardenPlacedPlants = garden.getPlacedPlants();
        Assert.assertEquals(gardenPlacedPlants.get(id).getImagePath(), "indiangrass.jpg");
    }

    @Test
    void testGetCurrentList() {
        garden.getCurrentList().add(testPlant);
        Assert.assertEquals(garden.getCurrentList().contains(testPlant), true);
    }

    @Test
    void testGetTotalCost() {
        int totalCost = 0;
        for (Plant p : garden.getPlacedPlants()) {
            if (p != null)
                totalCost += p.getPrice();
        }
        Assert.assertEquals(garden.getTotalCost(), totalCost);
    }

    @Test
    void testGetSupportedLeps() {
        garden.addPlant("indiangrass.jpg"); // for now contains a test lep as we didnt have time to include all of the supported leps
        Assert.assertEquals(garden.getSupportedLeps().size(), 2);
    }

    @Test
    void testSetAvailableLeps() {
        garden.setAvailableLeps();
        int lastIndex = garden.addPlant("indiangrass.jpg"); //4 supported lep species
        Assert.assertEquals(garden.getPlantWithId(String.valueOf(lastIndex)).getSupportedLeps().size(), 1);

    }

    @Test
    void testGetGardenDrawingWidth() {
        garden.setGardenDrawingWidth(10.0);
        Assert.assertEquals(garden.getGardenDrawingWidth(), 10.0, 0.1);
    }

    @Test
    void testSetGardenDrawingWidth() {
        garden.setGardenDrawingWidth(10.0);
        Assert.assertEquals(garden.getGardenDrawingWidth(), 10.0, 0.1);
    }

    @Test
    void testReadPlantCSV() {
        // garden.readPlantCSV();
    }

    @Test
    void testGetRecordFromLine() {

    }

    @Test
    void testApplyFilter() {
        Assert.assertEquals(garden.applyFilter("Perennial").size(), 6);
        Assert.assertEquals(garden.applyFilter("Annual").size(), 0);
        Assert.assertEquals(garden.applyFilter("Herbacious").size(), 5);
        Assert.assertEquals(garden.applyFilter("Woody").size(), 1);
        Assert.assertEquals(garden.applyFilter("No Filter").size(), 6);
    }

    @Test
    void testApplySort() {
        Assert.assertEquals(garden.applySort("Perennial").size(), 6);
        Assert.assertEquals(garden.applySort("Annual").size(), 6);
        Assert.assertEquals(garden.applySort("Herbacious").size(), 6);
        Assert.assertEquals(garden.applySort("Woody").size(), 6);
        Assert.assertEquals(garden.applySort("Leps Supported").size(), 6);
        Assert.assertEquals(garden.applySort("asdf").size(), 6);
    }

    @Test
    void testGetPlantFromPath() {

        Assert.assertEquals(garden.getPlantFromPath("indiangrass.jpg").getLatinName(), "Sorghastrum nutans");
    }

    @Test
    void testgetRecordFromLine() {
        Assert.assertEquals(garden.getRecordFromLine("0").toString(), "[0]");

    }

    @Test
    void testGetPlantWithLatinName() {

        Assert.assertEquals(garden.getPlantWithLatinName("Sorghastrum nutans"), garden.getAvailablePlants().get(0));
    }

    @AfterAll
    void testResetGarden() {
        garden.resetGarden();
        Assert.assertEquals(garden.getAvailablePlants().size(), 0);
        Assert.assertEquals(garden.getLepCount(), 0);
        Assert.assertEquals(garden.getPlacedPlants().size(), 0);
    }

}
