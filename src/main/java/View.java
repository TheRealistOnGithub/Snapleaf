import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class View extends Application {
    private HashMap<String, ImageView> plantImages = new HashMap<String, ImageView>();
    private HashMap<String, ImageView> lepImages = new HashMap<String, ImageView>();
    private Scene startScene;
    private Scene parameterScene;
    private Scene gardenScene;
    private Scene exportScene;
    private Scene infoScene;
    private Scene motivationScene;
    private Scene plantInfoScene;
    private Scene lepInfoScene;
    private Scene lepCatalogScene;
    private Scene plantCatalogScene;
    private Stage stage;
    private Controller controller;
    private final double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private final double screenWidth = Screen.getPrimary().getBounds().getWidth();
    private String plant_image_file_base = "plant_img/";
    private String lep_image_file_base = "lep_img/";
    private String background_image_path = "startMenuImage.jpg";
    private String parameter_image_path = "blurredParameterBackground.png";
    private String image_file_base = "imgs/";
    private String css_path = "css/";
    private String garden_css_path = "garden.css";
    private ToolBar gardenDisplayToolbar;
    private Pane drawPane = new Pane();
    private StackPane exportScreen;
    private ScrollPane plantScrollPane;
    private ScrollPane lepScrollPane;
    private ComboBox<String> filterBox;
    private ComboBox<String> sortBox;
    private GridPane sidebarButtons;
    private int popUpWidth = 300;
    private int popUpHeight = 300;
    private int plantImageSize = 100;
    private Text budgetDisplayText;
    private Text lepDisplayText = new Text("Leps Supported: 0");
    private int motivationImageSize = 350;
    private int motivationTextWrappingWidth = 325;
    private int motivationImageSpacing = 50;
    private int fitDimension = 100;
    private final String TOOLTIP = "tooltip";
    private final String SORT_BY = "Sort By:";
    private final String FILTER_BY = "Filter By:";
    private final String NO_SORTING = "No Sorting";
    private final String HERBACIOUS = "Herbacious";
    private final String WOODY = "Woody";
    private final String PERENNIAL = "Perennial";
    private final String ANNUAL = "Annual";
    private final String LEP_COUNT = "Leps Supported";
    private final String DISPLAY_TEXT_CSS = "display-text";
    private final String PARAMETER_TOOLBAR_CSS = "parameter-toolbar";
    private final String GARDEN_SCREEN_CSS = "garden-screen";
    private final String PLANT_SCROLL_PANE_CSS = "plant-scroll-pane";
    private final String SIDEBAR_TEXT_CSS = "sidebar-text";

    /**
     * View constructor that instantiates the controller.
     */
    public View() {
        this.controller = new Controller(this);
    }

    /**
     * Start method to launch the application
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setMaximized(true);
        showStartScene();
        createMotivationScene();
        showExport();
        stage.show();
    }

    /**
     * Create and set up garden scene.
     *
     * @param moistureType garden moisture type
     * @param soilType     garden soil type
     * @param sunType      garden sun type
     * @param budget       garden budget
     */
    public void createGardenScene(MoistureType moistureType, SoilType soilType, SunType sunType, Integer budget,
                                  ArrayList<Plant> plants) {
        // Create Root Pane
        BorderPane gardenScreen = new BorderPane();
        gardenScreen.getStyleClass().add(GARDEN_SCREEN_CSS);

        // Create Top Toolbar Display
        showSelectedParameters(moistureType, soilType, sunType, budget);
        gardenDisplayToolbar.prefWidthProperty().bind(stage.widthProperty());
        VBox parameters = new VBox(gardenDisplayToolbar);
        gardenDisplayToolbar.getStyleClass().add(PARAMETER_TOOLBAR_CSS);
        parameters.getStyleClass().add(DISPLAY_TEXT_CSS);
        parameters.setFillWidth(false);
        gardenScreen.setTop(parameters);
        BorderPane.setAlignment(parameters, Pos.TOP_CENTER);

        // Create Button Options
        Text buttonText = new Text("Sidebar Options");
        buttonText.getStyleClass().add(SIDEBAR_TEXT_CSS);
        Button btnPolygon = new Button("Draw Outline Polygon");
        Button btnStopDrawing = new Button("Stop Drawing Outline");
        Button btnExport = new Button("Export Garden");
        Button saveGarden = new Button("Save Garden");
        Button resetPolygon = new Button("Reset Polygon");
        Button btnChooseParams = new Button("Return to Parameter Selection");
        Button btnSeeLeps = new Button("See Supported Leps");
        Button btnSeePlants = new Button("See List of Plants Chosen");
        controller.setUpSaving(saveGarden);
        controller.lepCatalogHandler(btnSeeLeps);
        controller.plantListHandler(btnSeePlants);
        setButtonOptions(buttonText, btnPolygon, btnStopDrawing, btnExport, saveGarden, btnChooseParams, btnSeeLeps, btnSeePlants);


        // Scroll Pane of Plants
        setPlantScrollPane(plants);
        plantScrollPane.getStyleClass().add(PLANT_SCROLL_PANE_CSS);
        controller.handleFilter(plantScrollPane, filterBox, sortBox);
        plantScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

        // Create SideBar
        GridPane sidebar = new GridPane();
        sidebar.add(plantScrollPane, 0, 0);
        sidebar.add(sidebarButtons, 0, 1);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        sidebar.getRowConstraints().addAll(row1, row2);
        gardenScreen.setLeft(sidebar);

        // Create Main Garden Pane
        controller.drawGardenPolygon(stage, btnPolygon, btnStopDrawing, btnExport, plantImages.values(),
                resetPolygon, btnChooseParams);
        StackPane gardenPane = new StackPane();
        gardenPane.setStyle("-fx-background-color: saddlebrown");
        gardenPane.getChildren().addAll(drawPane);
        gardenScreen.setCenter(gardenPane);
        // Update Garden Scene Attribute
        gardenScene = new Scene(gardenScreen, screenWidth, screenHeight);
        gardenScene.getStylesheets().add(css_path + garden_css_path);
    }

    /**
     * Creates lep catalog scene to see supported leps
     *
     * @param supportedLeps HashSet containing the supportedLeps
     */
    public void createLepCatalog(Set<Lep> supportedLeps) {
        double imgTextSpacing = 25;
        double imageSize = 400;
        double borderPanePadding = 50;
        double spacingBetweenLeps = 15;
        BorderPane lepCatalogScreen = new BorderPane();
        GridPane lepCatalogGridPane = new GridPane();
        lepCatalogGridPane.setStyle("-fx-background-color: lightcyan");
        ScrollPane lepCatalogScrollPane = new ScrollPane();
        lepCatalogScrollPane.setFitToHeight(true);
        lepCatalogScrollPane.setFitToWidth(true);
        Button btnReturn = new Button("Return to Garden");
        btnReturn.getStyleClass().add("button");
        controller.returnToGardenHandler(btnReturn);
        int row = 0;
        for (Lep l : supportedLeps) {
            VBox lep = new VBox();
            lep.setAlignment(Pos.CENTER_LEFT);
            try {
                ImageView lepImage = lepImages.get(l.getLatinName());
                lepImage.setFitHeight(imageSize);
                lepImage.setFitWidth(imageSize);
                Text latinName = new Text(l.getLatinName());
                latinName.setFont(Font.font("Verdana", FontPosture.ITALIC, 30));
                lep.getChildren().addAll(lepImage, latinName);
                lep.setSpacing(imgTextSpacing);
                StackPane lepPane = new StackPane();
                lepPane.getChildren().add(lep);
                lepPane.setAlignment(Pos.CENTER);
                lepCatalogGridPane.getChildren().add(lepPane);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        lepCatalogGridPane.setVgap(spacingBetweenLeps);
        Insets insets = new Insets(0, 0, 0, borderPanePadding);
        lepCatalogScrollPane.setContent(lepCatalogGridPane);
        lepCatalogScreen.setCenter(lepCatalogScrollPane);
        lepCatalogScreen.setTop(btnReturn);
        BorderPane.setMargin(btnReturn, insets);
        BorderPane.setMargin(lepCatalogScrollPane, insets);
        lepCatalogScreen.setStyle("-fx-background-color: lightcyan");
        lepCatalogScrollPane.setStyle("-fx-background-color: lightcyan");
        lepCatalogScene = new Scene(lepCatalogScreen, screenWidth, screenHeight);
        lepCatalogScene.getStylesheets().add(css_path + garden_css_path);
    }

    /**
     * creates scene for user to see list of placed plants
     *
     * @param plants ArrayList of plants that have been placed
     */
    public void createPlantList(ArrayList<Plant> plants) {
        double imgTextSpacing = 25;
        double imageSize = 400;
        double borderPanePadding = 50;
        double spacingBetweenPlants = 15;
        BorderPane plantCatalogScreen = new BorderPane();
        TilePane plantCatalogGridPane = new TilePane(spacingBetweenPlants, spacingBetweenPlants);
        plantCatalogGridPane.setStyle("-fx-background-color: lightcyan");
        ScrollPane plantCatalogScrollPane = new ScrollPane();
        plantCatalogScrollPane.setFitToHeight(true);
        plantCatalogScrollPane.setFitToWidth(true);
        Button btnReturn = new Button("Return to Garden");
        btnReturn.getStyleClass().add("button");
        controller.returnToGardenHandler(btnReturn);
        for (Plant p : plants) {
            VBox plant = new VBox();
            plant.setAlignment(Pos.CENTER_LEFT);
            try {
                ImageView plantImage = new ImageView(loadImage(plant_image_file_base + p.getImagePath()));
                plantImage.setFitHeight(imageSize);
                plantImage.setFitWidth(imageSize);
                Text latinName = new Text(p.getLatinName());
                Text commonName = new Text("(" + p.getName() + ")");
                commonName.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
                latinName.setFont(Font.font("Verdana", FontPosture.ITALIC, 30));

                plant.getChildren().addAll(plantImage, latinName, commonName);
                plant.setSpacing(imgTextSpacing);
                StackPane plantPane = new StackPane();
                plantPane.getChildren().add(plant);
                plantPane.setAlignment(Pos.CENTER);
                plantCatalogGridPane.getChildren().add(plantPane);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        plantCatalogGridPane.setVgap(spacingBetweenPlants);
        Insets insets = new Insets(0, 0, 0, borderPanePadding);
        plantCatalogScrollPane.setContent(plantCatalogGridPane);
        plantCatalogScreen.setCenter(plantCatalogScrollPane);
        plantCatalogScreen.setTop(btnReturn);
        BorderPane.setMargin(btnReturn, insets);
        BorderPane.setMargin(plantCatalogScrollPane, insets);
        plantCatalogScreen.setStyle("-fx-background-color: lightcyan");
        plantCatalogScrollPane.setStyle("-fx-background-color: lightcyan");
        plantCatalogScene = new Scene(plantCatalogScreen, screenWidth, screenHeight);
        plantCatalogScene.getStylesheets().add(css_path + garden_css_path);
    }

    /**
     * Set scroll pane for plants of garden.
     *
     * @param plants list of plants from garden
     */
    public void setPlantScrollPane(ArrayList<Plant> plants) {
        double dataSpacing = 10;
        if (plantScrollPane == null)
            plantScrollPane = new ScrollPane();
        VBox plantList = new VBox();
        for (Plant p : plants) {
            HBox plant = new HBox();
            plant.setSpacing(dataSpacing);
            plant.setAlignment(Pos.CENTER_LEFT);
            ImageView plantImage = plantImages.get(p.getLatinName());
            setPlantToolTip(p, plantImage);
            Text latinName = new Text(p.getLatinName());
            Text commonName = new Text(p.getName());
            commonName.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            latinName.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
            plant.getChildren().addAll(plantImage, latinName, commonName);
            plantList.getChildren().add(plant);
        }
        plantScrollPane.setContent(plantList);
        plantScrollPane.setFitToHeight(true);
        addImageHandlers();
    }

//	/**
//	 * Set scroll pane for supported leps.
//	 * @param leps list of plants from garden
//	 */
//	public void setLepScrollPane(ArrayList<Lep> leps) {
//		double dataSpacing = 10;
//		lepScrollPane = new ScrollPane();
//		VBox lepList = new VBox();
//		for (Lep l : leps) {
//			HBox lep = new HBox();
//			lep.setSpacing(dataSpacing);
//			lep.setAlignment(Pos.CENTER_LEFT);
//			ImageView lepImage = lepImages.get(l.getLatinName());
//			Text latinName = new Text(l.getLatinName());
//			latinName.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
//			lep.getChildren().addAll(lepImage, latinName);
//			lepList.getChildren().add(lep);
//		}
//		lepScrollPane.setContent(lepList);
//		lepScrollPane.setFitToHeight(true);
//		//lepScrollPane.show();
//		//addImageHandlers();
//		//return lepScrollPane;
//	}


    /**
     * Set plant image tool tip
     *
     * @param plant plant object
     * @param iv    plant image view
     */
    public void setPlantToolTip(Plant plant, ImageView iv) {
        String plantInfo = "Common Name: " + plant.getName() + "\n" + "Scientific Name: " + plant.getLatinName() + "\n"
                + "Size (cm): " + plant.getSize() + "\n" + "Leps Supported: " + plant.getSupportedLeps().size();
        Tooltip.install(iv, new Tooltip(plantInfo));
        iv.getProperties().put(TOOLTIP, plantInfo);
    }

    /**
     * Set side buttons for garden tools.
     *
     * @param buttonText     text for garden
     * @param btnPolygon     start drawing garden button
     * @param btnStopDrawing stop drawing garden button
     * @param btnExport      export garden button
     */
    public void setButtonOptions(Text buttonText, Button btnPolygon, Button btnStopDrawing, Button btnExport,
                                 Button btnSave, Button btnChooseParams, Button btnSeeLeps, Button btnSeePlants) {
        filterBox = new ComboBox<>();
        filterBox.getItems().addAll(NO_SORTING, HERBACIOUS, WOODY, PERENNIAL, ANNUAL);
        filterBox.setPromptText(FILTER_BY);
        sortBox = new ComboBox<>();
        sortBox.getItems().addAll(NO_SORTING, HERBACIOUS, WOODY, PERENNIAL, ANNUAL, LEP_COUNT);
        sortBox.setPromptText(SORT_BY);
        sidebarButtons = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        sidebarButtons.getColumnConstraints().addAll(column1, column2);
        sidebarButtons.add(buttonText, 0, 0, 2, 1);
        sidebarButtons.add(sortBox, 1, 1);
        sidebarButtons.add(filterBox, 1, 2);
        sidebarButtons.add(btnPolygon, 0, 1);
        sidebarButtons.add(btnStopDrawing, 0, 2);
        sidebarButtons.add(btnExport, 0, 3);
        sidebarButtons.add(btnSave, 1, 3);
        sidebarButtons.add(btnChooseParams, 1, 4);
        sidebarButtons.add(btnSeeLeps, 0, 4);
        sidebarButtons.add(btnSeePlants, 0, 5);
        sidebarButtons.setHalignment(buttonText, HPos.CENTER);
        sortBox.setMaxWidth(Double.MAX_VALUE);
        filterBox.setMaxWidth(Double.MAX_VALUE);
        btnPolygon.setMaxWidth(Double.MAX_VALUE);
        btnStopDrawing.setMaxWidth(Double.MAX_VALUE);
        btnExport.setMaxWidth(Double.MAX_VALUE);
        btnSave.setMaxWidth(Double.MAX_VALUE);
        btnChooseParams.setMaxWidth(Double.MAX_VALUE);
        btnSeeLeps.setMaxWidth(Double.MAX_VALUE);
        btnSeePlants.setMaxWidth(Double.MAX_VALUE);
        sidebarButtons.setHgap(10);
        sidebarButtons.setVgap(10);
        sidebarButtons.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Create garden motivation scene.
     */
    public void createMotivationScene() {
        BorderPane motivationScreen = new BorderPane();
        int titleFontSize = 50;
        Text motivationText = new Text("Motivation");
        motivationText.setFont(Font.font("Helvetica", FontWeight.BOLD, titleFontSize));
        motivationText.setStyle("-fx-fill: green");
        motivationScreen.setTop(motivationText);
        BorderPane.setAlignment(motivationText, Pos.CENTER);

        StackPane lepInfoPane = createLepInfoPane();
        StackPane nativeSpeciesInfoPane = createNativeSpeciesInfoPane();
        StackPane summaryInfoPane = createSummaryInfoPane();

        StackPane buttonPane = buttonDesign("Start Garden", Color.BLACK);
        Insets insets = new Insets(motivationImageSpacing);
        controller.goToParameterButton(buttonPane);
        motivationScreen.setBottom(buttonPane);
        motivationScreen.setLeft(lepInfoPane);
        motivationScreen.setCenter(nativeSpeciesInfoPane);
        motivationScreen.setRight(summaryInfoPane);
        BorderPane.setMargin(lepInfoPane, insets);
        BorderPane.setMargin(nativeSpeciesInfoPane, insets);
        BorderPane.setMargin(summaryInfoPane, insets);
        BorderPane.setMargin(buttonPane, insets);
        motivationScreen.setStyle("-fx-background-color: lightcyan");
        motivationScene = new Scene(motivationScreen, screenWidth, screenHeight);
    }

    /**
     * Create set garden parameter scene.
     */
    public void createParameterScreen() {
        GridPane parameterScreen = new GridPane();
        parameterScreen.setHgap(15);
        parameterScreen.setVgap(15);
        parameterScreen.setPadding(new Insets(0, 10, 0, 10));
        parameterScreen.setStyle("-fx-background-image: url(" + image_file_base + parameter_image_path + "); "
                + "-fx-background-position: center center;" + "-fx-background-size: cover;");
        int titleFontSize = 75;
        int subtextSize = 35;
        Text ParameterTitle = new Text("	Enter Garden Conditions");
        ParameterTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, titleFontSize));
        ParameterTitle.setStyle("-fx-fill: white; -fx-border-color: black");
        ComboBox<String> moistureTypeBox = new ComboBox<String>();
        moistureTypeBox.getItems().addAll("Wet", "Dry", "Average");

        ComboBox<String> soilTypeBox = new ComboBox<String>();
        soilTypeBox.getItems().addAll("Clay", "Loam", "Sand");

        ComboBox<String> sunTypeBox = new ComboBox<String>();
        sunTypeBox.getItems().addAll("Full Shade", "Part Shade", "Full Sun");

        TextField budgetField = new TextField();

        Text moisture = new Text("Moisture Type: ");
        moisture.setFont(Font.font("Helvetica", FontWeight.BOLD, subtextSize));
        moisture.setStyle("-fx-fill: #4DFFF3");
        Text soil = new Text("Soil Type: ");
        soil.setFont(Font.font("Helvetica", FontWeight.BOLD, subtextSize));
        soil.setStyle("-fx-fill: #A38752");
        Text sun = new Text("Sun Type: ");
        sun.setFont(Font.font("Helvetica", FontWeight.BOLD, subtextSize));
        sun.setStyle("-fx-fill: #FFAD0A");
        Text budget = new Text("Budget ($): ");
        budget.setFont(Font.font("Helvetica", FontWeight.BOLD, subtextSize));
        budget.setStyle("-fx-fill: #379634");

        int buttonImageSize = 50;
        ImageView buttonImage = new ImageView(image_file_base + "rightArrow.png");
        buttonImage.setFitHeight(buttonImageSize);
        buttonImage.setFitWidth(buttonImageSize);

        ImageView motivationButtonImage = new ImageView(image_file_base + "leftArrow.png");
        motivationButtonImage.setFitHeight(buttonImageSize);
        motivationButtonImage.setFitWidth(buttonImageSize);

        Button btnGarden = new Button("Go To Garden", buttonImage);
        btnGarden.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        btnGarden.setStyle("-fx-background-color: #F8EBFF");
        controller.goToGardenButton(btnGarden, moistureTypeBox, soilTypeBox, sunTypeBox, budgetField);

        Button btnMotivation = new Button("Go To Motivation", motivationButtonImage);
        btnMotivation.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        btnMotivation.setStyle("-fx-background-color: #F8EBFF");
        controller.goToMotivation(this.stage, btnMotivation);
        parameterScreen.add(ParameterTitle, 0, 0);
        parameterScreen.add(moisture, 0, 1);
        parameterScreen.add(moistureTypeBox, 1, 1);
        parameterScreen.add(soil, 0, 2);
        parameterScreen.add(soilTypeBox, 1, 2);
        parameterScreen.add(sun, 0, 3);
        parameterScreen.add(sunTypeBox, 1, 3);
        parameterScreen.add(budget, 0, 4);
        parameterScreen.add(budgetField, 1, 4);
        parameterScreen.add(btnGarden, 1, 5);
        parameterScreen.add(btnMotivation, 0, 5);


        parameterScreen.setAlignment(Pos.CENTER);

        parameterScreen.getStylesheets().add(css_path + garden_css_path);

        this.parameterScene = new Scene(parameterScreen, screenWidth, screenHeight);
    }

    /**
     * Start garden introduction scene.
     */
    public void showStartScene() {
        StackPane startScreen = new StackPane();
        Text titleName = new Text("Build-A-Garden");
        titleName.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
        titleName.setFill(Color.WHITE);
        titleName.setEffect(new DropShadow());
        Button btnStart = new Button("Start Garden");
        Button btnMotivation = new Button("See Motivation");
        Button btnLoad = new Button("Load Garden");
        btnStart.getStyleClass().add("button");
        btnMotivation.getStyleClass().add("button");
        btnLoad.getStyleClass().add("button");
        controller.startGarden(this.stage, btnStart, btnMotivation);
        controller.setUpLoading(btnLoad);
        VBox startMenu = new VBox(5);
        startMenu.setStyle("-fx-background-image: url(" + image_file_base + background_image_path + "); "
                + "-fx-background-position: center center;" + "-fx-background-size: cover;");
        startMenu.getChildren().addAll(titleName, btnStart, btnMotivation, btnLoad);
        startScreen.getChildren().addAll(startMenu);
        startMenu.setAlignment(Pos.CENTER);
        startScene = new Scene(startScreen, screenWidth, screenHeight);
        startScene.getStylesheets().add(css_path + garden_css_path);
        this.stage.setScene(startScene);
    }

    /**
     * Show information of a selected plant
     *
     * @param iv1   The imageView selected
     * @param plant The plant object associated with the imageView selected
     */
    public void showPlantInfo(ImageView iv1, Plant plant) {

        //Copies image view of clicked image
        ImageView iv2 = new ImageView();
        iv2.setImage(iv1.getImage());
        ;
        iv2.setPreserveRatio(true);
        iv2.setFitHeight(300);

        BorderPane infoScene = new BorderPane();
        infoScene.setStyle("-fx-background-image: url(" + image_file_base + parameter_image_path + "); "
                + "-fx-background-position: center center;" + "-fx-background-size: cover;");
        //Creating return button
        StackPane buttonPane = buttonDesign("Return to Garden Design", Color.WHITE);
        controller.goToMainGarden(buttonPane);

        //Creating link
        Hyperlink link = new Hyperlink("https://mtcubacenter.org/native-plant-finder/");
        link.setText("Click here to learn more!");
        link.setPrefHeight(75);
        HostServices browser = getHostServices();
        controller.plantInfoBtns(stage, link, browser);

        //Create list of supported leps

        ListView lepList = new ListView<>();
        ObservableList<String> lepNames = FXCollections.observableArrayList();
        ObservableList<Image> lepImages = FXCollections.observableArrayList();
        for (Lep i : plant.getSupportedLeps()) {
            lepNames.add(i.getLatinName());
            lepImages.add(i.getImage());
        }
        Label lepLabel = new Label();
        lepLabel.setText("Supported Lep Species");
        lepLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        lepLabel.setTextFill(Color.WHITE);

        Label plantLabel = new Label();
        plantLabel.setText(plant.getName());
        plantLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        plantLabel.setTextFill(Color.WHITE);
        plantLabel.setMinWidth(iv2.getFitWidth());

        //setLepScrollPane(plant.getSupportedLeps());
        //lepScrollPane.getStyleClass().add(PLANT_SCROLL_PANE_CSS);
        //lepScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        lepList.setItems(lepNames);
        VBox lists = new VBox(5);
        lists.getChildren().addAll(lepLabel, lepList);
        VBox links = new VBox(5);
        links.getChildren().addAll(link);
        VBox images = new VBox(5);
        images.getChildren().addAll(plantLabel, iv2);
        lists.setMaxSize(600, 500);

        //Adding nodes to scene
        infoScene.setRight(links);
        infoScene.setLeft(images);
        infoScene.setCenter(lists);
        infoScene.setTop(buttonPane);

        plantInfoScene = new Scene(infoScene, screenWidth, screenHeight);
        stage.setScene(startScene);
    }

    /**
     * Load image given a file path.
     *
     * @param path image file path
     * @return image object of path
     */
    public Image loadImage(String path) {
        return new Image(path);
    }

    /**
     * Show export scene for garden.
     */
    public void showExport() {
        StackPane btnReturn = buttonDesign("Return to Garden Design", Color.WHITE);
        StackPane btnPrintPDF = buttonDesign("Click here to Print a PDF of your Garden", Color.WHITE);
        controller.exportGarden(stage, btnPrintPDF, btnReturn);
        GridPane exportScreen = new GridPane();
        Image background = new Image(image_file_base + "ConeFlower.jpg");
        //HBox buttons = new HBox(btnReturn.getMaxWidth(), btnReturn, btnPrintPDF);
        exportScreen.add(btnReturn, 0, 1, 4, 2);
        exportScreen.add(btnPrintPDF, 2, 1, 4, 2);
        exportScreen.setHgap(screenWidth / 5);
        exportScreen.setVgap(screenHeight / 3);
        exportScreen.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        exportScene = new Scene(exportScreen, screenWidth, screenHeight);
        stage.setScene(startScene);

    }

    /**
     * Set available plants to be displayed on scroll pane.
     *
     * @param plants list of plant information to select from
     */
    public void setAvailablePlantImages(ArrayList<Plant> plants) {
        plantImages.clear();
        for (Plant plant : plants) {
            //System.out.println(plant.getImagePath());
            ImageView imv = new ImageView(loadImage(plant_image_file_base + plant.getImagePath()));
            imv.setPickOnBounds(true);
            imv.setFitHeight(fitDimension);
            imv.setFitWidth(fitDimension);
            plantImages.put(plant.getLatinName(), imv);

        }
    }

    /**
     * Update the imageview associated with plant latin name.
     *
     * @param latinName latin name of the plant
     * @param newIV     new image view of plant
     */
    public void updateAvailablePlantImages(String latinName, ImageView newIV) {
        plantImages.put(latinName, newIV);
    }

    /**
     * Set available leps to be displayed in the garden.
     *
     * @param leps list of leps
     */
    public void setAvailableLepImages(Set<Lep> leps) {
        for (Lep lep : leps) {
            try {
                ImageView imv = new ImageView(lep_image_file_base + lep.getImagePath());
                imv.setFitHeight(fitDimension);
                imv.setFitWidth(fitDimension);
                lepImages.put(lep.getLatinName(), imv);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Set ImageView handlers for garden images.
     */
    public void addImageHandlers() {
        for (String name : plantImages.keySet()) {
            controller.setImageHandlers(plantImages.get(name));
        }
    }

    /**
     * Main method to start application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }


    /**
     * Show pop-up to prompt user to start drawing the garden polygon
     */
    public void showStartPopUp() {
        double gardenPromptWrappingWidth = 275;
        Stage promptPoly = new Stage();
        GridPane dimEntry = new GridPane();
        Text widthPrompt = new Text("Please select the 'Draw Garden Outline' button to begin designing your garden!");
        widthPrompt.setWrappingWidth(gardenPromptWrappingWidth);
        dimEntry.add(widthPrompt, 0, 0);
        widthPrompt.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        dimEntry.setAlignment(Pos.CENTER);
        Scene dimensionScreen = new Scene(dimEntry, popUpWidth, popUpHeight);
        promptPoly.setScene(dimensionScreen);
        promptPoly.show();
    }

    /**
     * Show pop-up to set garden width parameters.
     */
    public void showDimensionPopUp() {
        Stage dimensions = new Stage();
        GridPane dimEntry = new GridPane();

        TextField width = new TextField();
        Text widthPrompt = new Text("Enter Garden Width (m): ");
        Button setWidth = new Button("Set Width");
        controller.setWidthHandler(setWidth, width, dimensions);
        dimEntry.add(widthPrompt, 0, 0);
        dimEntry.add(width, 1, 0);
        dimEntry.add(setWidth, 0, 1);
        dimEntry.setAlignment(Pos.CENTER);

        Scene dimensionScreen = new Scene(dimEntry, popUpWidth, popUpHeight);
        dimensions.setScene(dimensionScreen);
        dimensions.show();
    }

    /**
     * Show selected garden parameters.
     *
     * @param moistureType garden moisture type
     * @param soilType     garden soil type
     * @param sunType      garden sun type
     * @param budget       garden budget
     * @return tool bar of garden parameters
     */
    public void showSelectedParameters(MoistureType moistureType, SoilType soilType, SunType sunType, Integer budget) {

        ArrayList<Text> labels = new ArrayList<Text>();
        Text moisture = new Text("Moisture Type: " + moistureType.toString());
        Text soil = new Text("Soil Type: " + soilType.toString());
        Text sun = new Text("Sun Type: " + sunType.toString());
        labels.add(moisture);
        labels.add(soil);
        labels.add(sun);

        budgetDisplayText = new Text("Budget($): " + budget.toString());
        labels.add(budgetDisplayText);

        ToolBar gardenConditions = new ToolBar();

        for (Text label : labels) {
            gardenConditions.getItems().add(label);
            gardenConditions.getItems().add(new Separator());
        }

        gardenConditions.getItems().add(lepDisplayText);

        this.gardenDisplayToolbar = gardenConditions;
    }

    /**
     * Load a previously saved garden outline polygon
     *
     * @param vertices garden outline polygon vertices
     */
    public void loadDrawPane(ArrayList<double[]> vertices) {
        Polygon outline = new Polygon();
        outline.setStroke(Color.GREEN);
        outline.setFill(Color.GREEN);

        for (double[] vertex : vertices) {
            outline.getPoints().addAll(vertex[0], vertex[1]);
        }

        drawPane.setShape(outline);
        drawPane.setStyle("-fx-background-color: green");
    }

    /**
     * Stage getter.
     *
     * @return application stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Start scene getter.
     *
     * @return start scene
     */
    public Scene getStartScene() {
        return startScene;
    }

    /**
     * Garden scene getter.
     *
     * @return garden scene
     */
    public Scene getGardenScene() {
        return gardenScene;
    }

    /**
     * Export scene getter.
     *
     * @return export scene
     */
    public Scene getExportScene() {
        return exportScene;
    }

    /**
     * Info scene getter.
     *
     * @return information scene
     */
    public Scene getInfoScene() {
        return infoScene;
    }

    /**
     * Motivation scene getter.
     *
     * @return motivation scene
     */
    public Scene getMotivationScene() {
        return motivationScene;
    }

    /**
     * Parameter scene getter.
     *
     * @return parameter scene
     */
    public Scene getParameterScene() {
        return parameterScene;
    }

    /**
     * Plant information scene getter.
     *
     * @return plant information scene
     */
    public Scene getPlantInfoScene() {
        return plantInfoScene;
    }

    /**
     * Lep information scene getter.
     *
     * @return lep information scene.
     */
    public Scene getLepInfoScene() {
        return lepInfoScene;
    }

    /**
     * Images of plants available within the garden.
     *
     * @return hash map of images of garden plants
     */
    public HashMap<String, ImageView> getPlantImages() {
        return plantImages;
    }

    /**
     * Garden draw pane.
     *
     * @return draw pane
     */
    public Pane getDrawPane() {
        return this.drawPane;
    }

    /**
     * Set Garden draw pane.
     *
     * @param drawPane Pane to set drawPane to
     * @return draw pane
     */
    public void setDrawPane(Pane drawPane) {
        this.drawPane = drawPane;
    }

    /**
     * cPlant image size getter.
     *
     * @return plant image size
     */
    public int getPlantImageSize() {
        return this.plantImageSize;
    }

    /**
     * Image file name base getter.
     *
     * @return image file name base
     */
    public String getPlantImageFileBase() {
        return plant_image_file_base;
    }

    public GridPane getSidebarButtons() {
        return sidebarButtons;
    }

    /**
     * Set the budget display text attribute so it will update when plants are dragged
     *
     * @param budgetDisplay Text field to set budget display attribute to
     */
    public void updateBudgetDisplayText(Text budgetDisplay) {
        this.gardenDisplayToolbar.getItems().remove(budgetDisplayText);
        this.gardenDisplayToolbar.getItems().remove(6);
        this.budgetDisplayText = budgetDisplay;
        this.gardenDisplayToolbar.getItems().add(budgetDisplayText);
        this.gardenDisplayToolbar.getItems().add(new Separator());
    }

    /**
     * Set the lep display text attribute so it will update when plants are dragged
     *
     * @param lepCount Integer to set lepCount display in garden toolbar to to
     */
    public void updateLepDisplayText(Integer lepCount) {
        this.gardenDisplayToolbar.getItems().remove(lepDisplayText);
        this.lepDisplayText = new Text("Leps Supported: " + lepCount.toString());
        this.gardenDisplayToolbar.getItems().add(lepDisplayText);
    }

    /**
     * Creates a pane with information about what leps are for the motivation screen
     *
     * @return StackPane containing image with lep info text over it as well as hover listeners
     */
    public StackPane createLepInfoPane() {
        ImageView background = new ImageView(image_file_base + "motivation1.jpg");
        Text titleText = new Text("What are leps?");
        Text moreInfo = new Text("Lepidoptera, or leps, are a type of insect that includes "
                + "butterflies and moths. If leps are not supported, ecosystem services like "
                + "pollination, nutrient cycling, flood regulation, etc. which are essential "
                + "for humans may collapse");
        return createMotivationStackPane(titleText, moreInfo, background);
    }

    /**
     * Creates a pane with information about what native species are for the motivation screen
     *
     * @return StackPane containing image with native species info text over it as well as hover listeners
     */
    public StackPane createNativeSpeciesInfoPane() {
        ImageView background = new ImageView(image_file_base + "motivation2.jpg");
        Text titleText = new Text("What are native species?");
        Text moreInfo = new Text("Native species are species that are indigenous to a region "
                + "and whose presence in that region is the result of only natural processes. In "
                + "other words, they are plants that would still be found in a region even if there "
                + "had been no human intervention");
        return createMotivationStackPane(titleText, moreInfo, background);
    }

    /**
     * Creates a pane with information about why native species are important for the motivation screen
     *
     * @return StackPane containing image with info over image and hover listeners
     */
    public StackPane createSummaryInfoPane() {
        ImageView background = new ImageView(image_file_base + "motivation3.jpg");
        Text titleText = new Text("Why is planting native plants important?");
        Text moreInfo = new Text("Native plants are the basis of the food chain. Non-native "
                + "plants cannot support leps in the way that native plants can. "
                + "If leps are not supported, the ecosystem collapses which can have catastrophic"
                + "effects on all species in the ecosystem (including humans).");
        return createMotivationStackPane(titleText, moreInfo, background);
    }

    /**
     * Generic method that takes information to be displayed on motivation page and an image and formats it, gives it listeners, and returns a stackpane with the image and associated text
     *
     * @param title           text to display when not hovering over pane
     * @param explanation     text to display when hovering over pane
     * @param backgroundImage image to display text over
     * @return
     */
    public StackPane createMotivationStackPane(Text title, Text explanation, ImageView backgroundImage) {
        StackPane infoPane = new StackPane();
        ImageView background = backgroundImage;
        background.setFitHeight(motivationImageSize);
        background.setFitWidth(motivationImageSize);
        Rectangle clip = new Rectangle(motivationImageSize, motivationImageSize);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        background.setClip(clip);
        HBox infoText = new HBox();
        Text titleText = title;
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        titleText.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
        titleText.setFill(Color.WHITE);
        titleText.setEffect(shadow);
        titleText.setWrappingWidth(motivationTextWrappingWidth);
        infoText.getChildren().addAll(titleText);
        infoText.setAlignment(Pos.CENTER);
        Text moreInfo = explanation;
        moreInfo.setWrappingWidth(motivationTextWrappingWidth);
        //moreInfo.setEffect(shadow);
        moreInfo.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        moreInfo.setFill(Color.BLACK);
        infoPane.getChildren().addAll(background, infoText);
        infoPane.setAlignment(Pos.CENTER);
        controller.motivationHoverListeners(infoPane, background, titleText, moreInfo, infoText);
        return infoPane;
    }

    /**
     * Generic method that allows for the design of consistent buttons
     *
     * @param btnText Text to be displayed in the button
     * @return buttonPane    StackPane with button added
     */
    public StackPane buttonDesign(String btnText, Color color) {
        int buttonImageSize = 100;
        ImageView buttonImage = new ImageView(image_file_base + "startGardenButton.png");
        buttonImage.setFitHeight(buttonImageSize);
        buttonImage.setFitWidth(buttonImageSize);
        HBox gardenButton = new HBox();
        Text gardenButtonLabel = new Text(btnText);
        gardenButtonLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gardenButtonLabel.setFill(color);
        gardenButton.getChildren().add(gardenButtonLabel);
        StackPane buttonPane = new StackPane();
        buttonPane.getChildren().addAll(buttonImage, gardenButtonLabel);
        return buttonPane;
    }

    /**
     * Getter for the lepCatalog Scene
     *
     * @return lep catalog scene
     */
    public Scene getLepCatalogScene() {
        return lepCatalogScene;
    }

    /**
     * Getter for the plantCatalog Scene
     *
     * @return plant catalog scene
     */
    public Scene getPlantCatalogScene() {
        return plantCatalogScene;
    }

    /**
     * Shows pop-up to notify user that they are overdrafted
     */
    public void showOverDraftPopup() {
        double gardenPromptWrappingWidth = 275;
        Stage promptPoly = new Stage();
        GridPane dimEntry = new GridPane();
        Text widthPrompt = new Text("Your garden is over budget! Remove a plant if you wish to stay under budget :) ");
        widthPrompt.setWrappingWidth(gardenPromptWrappingWidth);
        dimEntry.add(widthPrompt, 0, 0);
        widthPrompt.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        dimEntry.setAlignment(Pos.CENTER);
        Scene dimensionScreen = new Scene(dimEntry, popUpWidth, popUpHeight);
        promptPoly.setScene(dimensionScreen);
        promptPoly.show();
    }
}
