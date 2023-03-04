import javafx.application.HostServices;
import javafx.beans.value.ObservableValue;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

//import javafx.scene.web.WebView;
public class Controller {

    private Garden garden;
    private View view;
    private Polygon currentPolygon;
    private WorkMode workMode = WorkMode.CREATE;
    private final String TOOLTIP = "tooltip";

    /**
     * Instantiate the controller object and start the garden.
     *
     * @param view garden view object
     */
    public Controller(View view) {
        this.view = view;
        garden = new Garden();
        //garden.setAvailablePlants();
        //garden.setAvailableLeps();
        //view.setAvailablePlantImages(garden.getAvailablePlants());
    }

    /**
     * Set event listeners for garden introduction buttons.
     *
     * @param stage         stage for the view
     * @param btnStart      garden start button
     * @param btnMotivation motivation scene button
     */
    public void startGarden(Stage stage, Button btnStart, Button btnMotivation) {

        btnStart.setOnAction(e -> {
            view.createParameterScreen();
            stage.setScene(view.getParameterScene());
            e.consume();
        });

        btnMotivation.setOnAction(e -> {
            stage.setScene(view.getMotivationScene());
            e.consume();
        });

    }

    /**
     * Sets event listeners
     *
     * @param stage
     * @param btnMotivation
     */
    public void goToMotivation(Stage stage, Button btnMotivation) {

        btnMotivation.setOnAction(e -> {
            stage.setScene(view.getMotivationScene());
            e.consume();
        });

    }

    /**
     * Set event handlers for garden setup.
     *
     * @param btnGarden       set start button for garden
     * @param moistureTypeBox combobox to set garden moisture type
     * @param soilTypeBox     combobox to set garden soil type
     * @param sunTypeBox      combobox to set garden sun type
     * @param budget          field to enter garden budget
     */
    public void goToGardenButton(Button btnGarden, ComboBox<String> moistureTypeBox, ComboBox<String> soilTypeBox,
                                 ComboBox<String> sunTypeBox, TextField budget) {

        btnGarden.setOnAction(e -> {
            setGardenParameters(moistureTypeBox.getValue(), soilTypeBox.getValue(), sunTypeBox.getValue(),
                    budget.getText());
            garden.setAvailablePlants();
            garden.setAvailableLeps();
            view.setAvailablePlantImages(garden.getAvailablePlants());
            view.createGardenScene(garden.getMoistureType(), garden.getSoilType(), garden.getSunType(),
                    garden.getTotalBudget(), garden.getAvailablePlants());
            view.getStage().setScene(view.getGardenScene());
            view.showStartPopUp();
            e.consume();
        });

    }

    /**
     * Sets up event handler for button to go to parameter screen from start screen.
     *
     * @param goToParameters button that sends the user to the parameter screen
     */
    public void goToParameterButton(StackPane goToParameters) {

        goToParameters.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            view.createParameterScreen();
            view.getStage().setScene(view.getParameterScene());
            e.consume();
        });

        goToParameters.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            view.getMotivationScene().setCursor(Cursor.HAND);
            e.consume();
        });

        goToParameters.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            view.getMotivationScene().setCursor(Cursor.DEFAULT);
            e.consume();
        });
    }

    /**
     * Sets the garden parameter values in the model.
     *
     * @param moistureType moisture value to set in garden.
     * @param soilType     soil value to set in garden.
     * @param sunType      sun value to set in garden.
     * @param budget       budget value to set in garden.
     */
    public void setGardenParameters(String moistureType, String soilType, String sunType, String budget) {
        switch (moistureType) {
            case "Wet":
                garden.setMoistureType(MoistureType.WET);
                break;
            case "Dry":
                garden.setMoistureType(MoistureType.DRY);
                break;
            default:
                garden.setMoistureType(MoistureType.AVERAGE);
        }

        switch (soilType) {
            case "Clay":
                garden.setSoilType(SoilType.CLAY);
                break;
            case "Loam":
                garden.setSoilType(SoilType.LOAM);
                break;
            default:
                garden.setSoilType(SoilType.SAND);
        }

        switch (sunType) {
            case "Full Shade":
                garden.setSunType(SunType.FULLSHADE);
                break;
            case "Part Shade":
                garden.setSunType(SunType.PARTSHADE);
                break;
            default:
                garden.setSunType(SunType.FULLSUN);
        }
        garden.setTotalBudget(Integer.parseInt(budget));
    }

    /**
     * Set event listeners for garden buttons
     *
     * @param stage          view stage
     * @param btnPolygon     start drawing garden button
     * @param btnStopDrawing stop drawing garden button
     * @param btnExport      export garden button
     * @param plantImages    collection of plant image views
     */
    public void drawGardenPolygon(Stage stage, Button btnPolygon, Button btnStopDrawing, Button btnExport,
                                  Collection<ImageView> plantImages, Button btnReset, Button btnChooseParams) {

        setDrawPolygonHandler(btnPolygon);

        btnStopDrawing.setOnAction(e -> {
            view.getDrawPane().getChildren().remove(currentPolygon);
            view.getDrawPane().setShape(currentPolygon);
            currentPolygon = null;
            view.getDrawPane().setStyle("-fx-background-color: green");
            view.getDrawPane().setOnMouseClicked(null);
            garden.setGardenDrawingWidth(view.getDrawPane().getWidth());
            view.getSidebarButtons().getChildren().remove(btnPolygon);
            view.getSidebarButtons().add(btnReset, 0, 1);
            btnReset.setMaxWidth(Double.MAX_VALUE);
            setResetHandler(btnReset, btnPolygon);

            // create popup to set garden width
            view.showDimensionPopUp();
            e.consume();
        });

        btnExport.setOnAction(e -> {
            view.showExport();
            stage.setScene(view.getExportScene());
            view.getDrawPane().setOnMouseClicked(null);
            e.consume();
        });

        btnChooseParams.setOnAction(e -> {
            view.getDrawPane().getChildren().clear();
            garden.resetGarden();
            if (view.getParameterScene() == null)
                view.createParameterScreen();
            stage.setScene(view.getParameterScene());
            e.consume();
        });
    }

    /**
     * Sets the event handler for the draw polygon button
     *
     * @param btnPolygon the button for drawing the polygon
     */
    public void setDrawPolygonHandler(Button btnPolygon) {
        switch (workMode) {
            case CREATE:
                btnPolygon.setOnAction(event -> {
                    view.getDrawPane().setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            if (currentPolygon == null) {
                                System.out.println("Polygon clicked");
                                currentPolygon = new Polygon();
                                currentPolygon.setStroke(Color.GREEN);
                                currentPolygon.setFill(Color.GREEN);
                                view.getDrawPane().getChildren().add(currentPolygon);
                            }
                            currentPolygon.getPoints().addAll(e.getX(), e.getY());
                            double[] newVertex = {e.getX(), e.getY()};
                            garden.addVertex(newVertex);
                        } else {
                            currentPolygon = null;
                        }
                    });
                    event.consume();
                });
                break;
            default:
                view.loadDrawPane(garden.getGardenVerticesCoordinates());
        }
    }

    /**
     * Sets event handler for reset polygon button
     *
     * @param reset       Button to reset the polygon
     * @param drawPolygon Button to draw a new polygon
     */
    public void setResetHandler(Button reset, Button drawPolygon) {
        reset.setOnAction(e -> {
            garden.setGardenVerticesCoordinates(new ArrayList<double[]>());
            view.getDrawPane().setShape(new Rectangle(view.getDrawPane().getWidth(), view.getDrawPane().getHeight()));
            view.getDrawPane().setStyle(" -fx-background-color: saddlebrown");
            view.getSidebarButtons().getChildren().remove(reset);
            setDrawPolygonHandler(drawPolygon);
            view.getSidebarButtons().add(drawPolygon, 0, 1);
            drawPolygon.setMaxWidth(Double.MAX_VALUE);
        });
    }

    /**
     * Sets event handlers for the fiter and sort buttons
     *
     * @param pane
     * @param filterBox
     * @param sortBox
     */
    public void handleFilter(ScrollPane pane, ComboBox filterBox, ComboBox sortBox) {

        filterBox.setOnAction(e -> {
            String filterBy = (String) filterBox.getValue();
            view.setPlantScrollPane(garden.applyFilter(filterBy));
            e.consume();
        });

        sortBox.setOnAction(e -> {
            String sortBy = (String) sortBox.getValue();
            view.setPlantScrollPane(garden.applySort(sortBy));
            e.consume();
        });

    }

    /**
     * Sets the event handlers for the button and Hyperlink on the plantInfo page
     *
     * @param stage   The JavaFX stage for the application
     * @param browser This class provides HostServices for an Application.
     * @param link    The link sends users to Mt. Cuba's plant finder website to learn more
     */
    public void plantInfoBtns(Stage stage, Hyperlink link, HostServices browser) {

        link.setOnAction(e -> {
            browser.showDocument(link.getText());
            e.consume();
        });
    }

    /**
     * Sets up event handler for button to go to garden screen from plantInfo.
     *
     * @param buttonPane StakcPane from the plantInfo screen that returns to garden screen when pressed.
     */
    public void goToMainGarden(StackPane buttonPane) {

        buttonPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            view.getStage().setScene(view.getGardenScene());
            e.consume();
        });
    }


    /**
     * Sets the event handling for buttons on the exportGarden scene
     *
     * @param stage    The stage of the JavaFX application
     * @param btnPrint Exports the drawPane (garden polygon + plants) to a PDF
     * @param btnReturn   Sends the user to the gardenScene
     * @return export    Returns the stackPane which houses the buttons
     */
    public void exportGarden(Stage stage, StackPane btnPrint, StackPane btnReturn) {

        btnReturn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            view.getStage().setScene(view.getGardenScene());
            e.consume();
        });

        btnPrint.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                job.showPrintDialog(stage);
                job.printPage(view.getDrawPane());
                job.endJob();
            }
            e.consume();
        });
    }

    /**
     * Set up hover listeners for the motivation screen.
     *
     * @param pane      StackPane over which users will hover to see motivation info.
     * @param infoText  Rectangle which contains the motivation information.
     * @param titleText Text that is displayed when user is not hovering.
     * @param moreInfo  Text that is displayed when user is hovering.
     */
    public void motivationHoverListeners(StackPane pane, ImageView backGround, Text titleText, Text moreInfo, HBox infoText) {
        // Rectangle lepInfo = new Rectangle(300, 300);
        // lepInfo.setFill(Color.WHITE);
        double nonHoverOpacity = 1;
        double hoverOpacity = 0.4;
        pane.hoverProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
                    if (show) {
                        pane.getChildren().clear();
                        infoText.getChildren().clear();
                        infoText.getChildren().addAll(moreInfo);
                        pane.getChildren().add(backGround);
                        pane.getChildren().add(infoText);
                        backGround.setOpacity(hoverOpacity);
                    } else {
                        pane.getChildren().clear();
                        infoText.getChildren().clear();
                        infoText.getChildren().addAll(titleText);
                        pane.getChildren().add(backGround);
                        pane.getChildren().add(infoText);
                        backGround.setOpacity(nonHoverOpacity);
                    }
                });
    }

    /**
     * The event handler for a right click on a plant ImageView. Switches scene
     * to the plantInfoScene
     *
     * @param event mouse click event
     */
    public void plantRightClick(MouseEvent event) {
        ImageView iv = (ImageView) event.getSource();
        String path = getImageFileName(iv.getImage());
        Plant plant = garden.getPlantFromPath(path);
        if (!(view.getDrawPane().getChildren().contains(iv))) {
            view.showPlantInfo(iv, plant);
            view.getStage().setScene(view.getPlantInfoScene());
        } else {
            garden.removePlant(Integer.parseInt(iv.getId()));
            view.getDrawPane().getChildren().remove(iv);

            Integer budgetLeft = garden.getTotalBudget() - garden.getTotalCost();
            view.updateBudgetDisplayText(new Text("Budget($): " + budgetLeft.toString()));
            view.updateLepDisplayText(garden.getLepCount());
        }
    }

    /**
     * ImageView listener for plant drag event
     *
     * @param event mouse drag event
     */
    public void plantDrag(MouseEvent event) {
        if (view.getDrawPane() == null) {
            return;
        }
        ImageView iv = (ImageView) event.getSource();
        String id = iv.getId();
        double oldX = garden.getPlantX(id);
        double oldY = garden.getPlantY(id);
        double newX = garden.getPlantX(id) + event.getX() - iv.getFitHeight() / 2;
        double newY = garden.getPlantY(id) + event.getY() - iv.getFitWidth() / 2;
        iv.setTranslateX(newX);
        iv.setTranslateY(newY);
        if (checkPlantCollision(iv)) {
            iv.setTranslateX(oldX);
            iv.setTranslateY(oldY);
        } else {
            garden.setPlantX(id, newX);
            garden.setPlantY(id, newY);
        }
    }

    /**
     * Checks if the current node is intersecting any other nodes in its parent.
     *
     * @param currentImageView image view being tested
     * @return boolean of if there is a collision
     */
    public boolean checkPlantCollision(ImageView currentImageView) {
        double imageRadius = currentImageView.getFitHeight() / 2;
        Circle currentCircle = new Circle(currentImageView.getTranslateX() + imageRadius, currentImageView.getTranslateY() + imageRadius, currentImageView.getFitHeight() / 2);
        for (Node n : view.getDrawPane().getChildren()) {
            try {
                ImageView iv = (ImageView) n;
                if (!iv.equals(currentImageView)) {
                    Circle newCircle = new Circle(iv.getTranslateX() + iv.getFitHeight() / 2, iv.getTranslateY() + iv.getFitWidth() / 2, iv.getFitHeight() / 2);
                    Shape newShape = Circle.intersect(newCircle, currentCircle);
                    if (newShape.getBoundsInLocal().getWidth() != -1) {
                        return true;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return false;
    }


    /**
     * ImageView listener for plant pressed event
     *
     * @param event mouse pressed event
     */
    public void plantPressedLeftClick(MouseEvent event) {
        if (view.getDrawPane() == null) {
            return;
        }
        ImageView iv = (ImageView) event.getSource();
        HBox ivHBox = (HBox) iv.getParent();
        Image plantImage = iv.getImage();
        int imageViewId = garden.addPlant(getImageFileName(plantImage));
        iv.setId(String.valueOf(imageViewId));
        String id = iv.getId();
        ImageView ivNew = new ImageView(plantImage.getUrl());
        Tooltip.install(ivNew, new Tooltip((String) iv.getProperties().get(TOOLTIP)));
        view.updateAvailablePlantImages(garden.getPlantWithId(id).getLatinName(), ivNew);
        ivNew.setPickOnBounds(true);
        double imageSize = scalePlantSize(garden.getPlantWithId(id).getSize(), garden.getWidth(),
                garden.getGardenDrawingWidth());
        iv.setFitHeight(imageSize);
        iv.setFitWidth(imageSize);
        setImageHandlers(ivNew);
        ivNew.setFitHeight(view.getPlantImageSize());
        ivNew.setFitWidth(view.getPlantImageSize());
        ivHBox.getChildren().add(0, ivNew);
        view.getDrawPane().getChildren().add(iv);
        double imageRadius = imageSize / 2;
        Circle imageCircle = new Circle(iv.getTranslateX() + imageRadius, iv.getTranslateY() + imageRadius, imageRadius);
        iv.setClip(imageCircle);
//		garden.setPlantX(id, iv.getTranslateX() - imageRadius);
//		garden.setPlantY(id, iv.getTranslateY() - imageRadius);
        Integer budgetLeft = garden.getTotalBudget() - garden.getTotalCost();
        view.updateBudgetDisplayText(new Text("Budget($): " + budgetLeft.toString()));
        view.updateLepDisplayText(garden.getLepCount());
        view.setAvailableLepImages(garden.getSupportedLeps());
        iv.setOnMousePressed(null);
        if (budgetLeft < 0) {
            view.showOverDraftPopup();
        }
        iv.setOnMouseClicked(mouseEvent ->
        {
            if (mouseEvent.getClickCount() == 2) {
                plantRightClick(mouseEvent);
            }
        });
    }

    /**
     * Get file name for image
     *
     * @param image
     * @return file name
     */
    public String getImageFileName(Image image) {
        Path imagePath = Path.of(image.getUrl().substring(6));
        return imagePath.getFileName().toString();
    }

    /**
     * Set image view handlers for drag and press
     *
     * @param iv image view object
     */
    public void setImageHandlers(ImageView iv) {
        iv.setOnMouseDragged(event -> plantDrag(event));
        iv.setOnMousePressed(event ->
        {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                plantPressedLeftClick(event);
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                plantRightClick(event);
            }
        });
    }

    /**
     * Event handler to set garden width value to user entered value.
     *
     * @param btnSetWidth Button which will set the width to the entered value.
     * @param width       TextField where user enters width value.
     * @param popUp       Stage which serves as a popup to ask the user for width input.
     */
    public void setWidthHandler(Button btnSetWidth, TextField width, Stage popUp) {
        btnSetWidth.setOnAction(e -> {
            int widthVal = Integer.parseInt(width.getText());
            garden.setWidth(widthVal);
            popUp.close();
            e.consume();
        });
    }

    /**
     * Get size of plant images based on plant size, garden size, and garden drawing size.
     *
     * @param plantSize
     * @param gardenWidth
     * @param drawPaneWidth
     * @return
     */
    public double scalePlantSize(int plantSize, int gardenWidth, double drawPaneWidth) {
        int gardenWidthCM = gardenWidth * 100;
        return drawPaneWidth * plantSize / gardenWidthCM;
    }

    /**
     * Set up save button listener to serialize garden object and save it.
     *
     * @param btnSaveGarden Button from garden screen which user presses to save a garden.
     */
    public void setUpSaving(Button btnSaveGarden) {

        btnSaveGarden.setOnAction(e -> {
            FileDialog dialog = new FileDialog((Frame) null, "Select Save Location");
            dialog.setMode(FileDialog.SAVE);
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();
            try {
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(garden);
                //System.out.println(garden.getPlantWithId("4").getImagePath());
                oos.close();
//				System.out.println(garden.getTotalBudget());
//				System.out.println(garden.getMoistureType());
//				System.out.println(garden.getSoilType());
//				System.out.println(garden.getSunType());
//				System.out.println(garden.getGardenVerticesCoordinates());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            dialog.dispose();
            e.consume();
        });
    }

    /**
     * Set up load button listener to deserialize saved garden object.
     *
     * @param btnLoadGarden button from start screen which user presses to load a garden.
     */
    public void setUpLoading(Button btnLoadGarden) {
        btnLoadGarden.setOnAction(e -> {
            workMode = WorkMode.LOAD;
            FileDialog dialog = new FileDialog((Frame) null, "Select File to Load");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();
            try {
                FileInputStream fis = new FileInputStream(path);
                ObjectInputStream ois = new ObjectInputStream(fis);
                garden = (Garden) ois.readObject();
                ois.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            garden.setAvailablePlants();
            garden.setAvailableLeps();
            view.setAvailablePlantImages(garden.getAvailablePlants());
            view.setAvailableLepImages(garden.getSupportedLeps());
            view.createGardenScene(garden.getMoistureType(), garden.getSoilType(), garden.getSunType(), garden.getTotalBudget(), garden.getAvailablePlants());
            loadPlants(garden.getPlacedPlants());
            view.getStage().setScene(view.getGardenScene());
            Integer costLeft = garden.getTotalBudget() - garden.getTotalCost();
            view.updateBudgetDisplayText(new Text("Budget($): " + costLeft.toString()));
            view.updateLepDisplayText(garden.getLepCount());
            dialog.dispose();
            workMode = WorkMode.CREATE;
            e.consume();
        });
    }

    /**
     * Load previously saved plants that were placed in the garden
     *
     * @param placedPlants all plants which had been placed in the garden
     */
    public void loadPlants(ArrayList<Plant> placedPlants) {
        for (Plant plant : placedPlants) {
            if (plant == null) {
                continue;
            }
            Image plantImage = view.loadImage(view.getPlantImageFileBase() + plant.getImagePath());
            ImageView iv = new ImageView(plantImage);
            view.setPlantToolTip(plant, iv);
            iv.setId(plant.getId());
            double imageSize = scalePlantSize(plant.getSize(), garden.getWidth(), garden.getGardenDrawingWidth());
            iv.setPickOnBounds(true);
            iv.setFitHeight(imageSize);
            iv.setFitWidth(imageSize);
            double imageRadius = imageSize / 2;
            Circle imageCircle = new Circle(iv.getTranslateX() + imageRadius, iv.getTranslateY() + imageRadius, imageRadius);
            iv.setClip(imageCircle);
            iv.setTranslateX(plant.getX());
            iv.setTranslateY(plant.getY());
            setLoadedImageHandlers(iv);
            view.getDrawPane().getChildren().add(iv);
        }
    }

    /**
     * Set event handlers for the loaded image views
     *
     * @param iv image view object
     */
    public void setLoadedImageHandlers(ImageView iv) {
        iv.setOnMouseDragged(event -> plantDrag(event));
        iv.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                plantRightClick(event);
            }
        });
    }

    /**
     * Method to set event handler for button to see supported leps and open lep catalog
     *
     * @param btnSeeLeps Button in garden screen that user clicks to see lep catalog
     */
    public void lepCatalogHandler(Button btnSeeLeps) {
        btnSeeLeps.setOnAction(e -> {
            view.createLepCatalog(garden.getSupportedLeps());
            view.getStage().setScene(view.getLepCatalogScene());
            e.consume();
        });
    }

    /**
     * Method to set event handler for button to see placed plants
     *
     * @param btnSeePlants Button in garden screen that user clicks ot see placed plants
     */
    public void plantListHandler(Button btnSeePlants) {
        btnSeePlants.setOnAction(e -> {
            view.createPlantList(garden.getPlacedPlants());
            view.getStage().setScene(view.getPlantCatalogScene());
            e.consume();
        });
    }

    /**
     * Method to return to garden screen
     *
     * @param btnReturn button to click to return to garden screen
     */
    public void returnToGardenHandler(Button btnReturn) {
        btnReturn.setOnAction(e -> {
            view.getStage().setScene(view.getGardenScene());
            e.consume();
        });
    }
}

	


