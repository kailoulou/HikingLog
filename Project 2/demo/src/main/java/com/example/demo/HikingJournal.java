package com.example.demo;

import com.sothawo.mapjfx.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.FileInputStream;
import java.io.IOException;
//Adding the FXML loader so that we can squeeze in the map feature
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

import javafx.stage.FileChooser;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;


public class HikingJournal extends Application {
    // attributes
    private TripList tripList = new TripList();
    private PlanList planList = new PlanList();

    public Controller controller = new Controller();
    //initialize tableview
    private TableView<Trip> logTable = new TableView<Trip>();
    private ObservableList<Trip> logData = FXCollections.observableArrayList(
            new Trip("7/20/21", "[PA] West Rim Trail", "33", "75", "This was a great 3 day backpacking trip!"),
            new Trip("5/2/22", "[NY] Colgate Hiking Trail", "1", "55", "Too short"),
            new Trip("9/12/22", "[PA] Appalachian Trail", "2190", "62", "Very rocky :("),
            new Trip("3/7/23", "[CA] Pacific Crest Trail", "2650", "84", "A lot of bears!"),
            new Trip("3/7/23", "Colgate","2", "80", "Nice!")
            //new Trip("-", "-", "-", "-", "-")
    );

    Plan dummyPlan = new Plan(" Test", "220", "EZ");
    private TableView<Plan> planTable = new TableView<Plan>();
    private ObservableList<Plan> planData = FXCollections.observableArrayList(
            new Plan("[NM] Philmont Scout Ranch", "180", "Hard")
            //new Plan("-", "-", "-")
    ); 

    Label todoLabel = new Label ("To-Do List:");
    ListView<String> todoList = new ListView<String>();
    private ObservableList<String> todoData = FXCollections.observableArrayList(
        new String("Buy Hiking Boots")
    );
    ImageView imageView = new ImageView();
    Button newJournal = new Button("Create New Journal");
    Button continueJournal = new Button("Continue Previous Journal");
    Button addButton = new Button("Add Trip");
    Button removeButton = new Button("Remove Trip");
    Button planAddButton = new Button("Add Plan");
    Button planRemoveButton = new Button("Remove Plan");
    Button todoAddButton = new Button("Add Item");
    Button todoRemoveButton = new Button("Remove Item");

    Button saveButton = new Button("Save and Quit");

    TextField sum = new TextField();
    TextField total = new TextField();

    Label totalLabel = new Label("Total Trips Taken:");
    Label sumLabel = new Label("Total Miles Hiked:");

    TextField tempDetails = new TextField();
    TextArea noteDetails = new TextArea();

    Label trailDetailsLabel = new Label("-----");
    Label tempDetailsLabel = new Label("Weather:");
    Label noteDetailsLabel = new Label("Notes:");
    Label locationLabel = new Label("Location:");
    ObservableList<String> suggestions = FXCollections.observableArrayList();
    ComboBox addLocation = new ComboBox(ReadData.suggestionsArray(suggestions, "Project 2/AllTrails data - nationalpark.csv"));
    AutoCompleteComboBoxListener addBox = new AutoCompleteComboBoxListener(addLocation);

    Label dateLabel = new Label("Date (m/d/y):");
    TextField addDate = new TextField();

    Label distanceLabel = new Label("Distance (M):");
    TextField addDistance = new TextField();

    Label tempLabel = new Label("Weather:");
    TextField addTemp = new TextField();

    Label noteLabel = new Label("Notes:");
    TextArea addNote = new TextArea();

    Button submitButton = new Button("Submit");
    Button chooseImage = new Button("Upload Image");

    //init size
    private final int WIDTH = 900;
    private final int HEIGHT = 700;
    // MIN WIDTH & HEIGHT
    private final int MINWIDTH = WIDTH;
    private final int MINHEIGHT = HEIGHT;
    private double startX;
    private double startY;

    //log tab
    final VBox totalBox = new VBox();
    final VBox sumBox = new VBox();
    final HBox buttonBox = new HBox();
    final HBox infoBox = new HBox(totalBox, sumBox, buttonBox);
    final HBox logBox = new HBox(logTable);
    final VBox mainLogBox = new VBox(logBox, infoBox);


    //plan tab
    final VBox planButtonBox = new VBox();
    final VBox todoButtonBox = new VBox();
    final VBox todoBox = new VBox(todoLabel, todoList);
    final HBox planBox = new HBox(planTable, todoBox);
    final HBox holdingButtonBox = new HBox(planButtonBox, todoButtonBox);
    final VBox mainPlanBox = new VBox(planBox, holdingButtonBox);

    //---Kailoulou--- Map Tab stuff
    final ArrayList<Coordinate> tripCoordinates = new ArrayList<Coordinate>();


    public static void main(String[] args){
        System.out.print("Hello there!\n");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //This part is building the FXML loader, which creates a node which we can

        /**
         * The following FXML loading methods were developed by @author P.J. Meisch (pj.meisch@sothawo.com) and adapted
         * to our HikingLog
         *
         * **/

        String fxmlFile = "/com/example/demo/fxml/DemoApp.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent rootNode = fxmlLoader.load(getClass().getResourceAsStream("/com/example/demo/fxml/DemoApp.fxml"));
        System.out.println(rootNode);


        final Controller controller = fxmlLoader.getController();
        final Projection projection = getParameters().getUnnamed().contains("wgs84") ? Projection.WGS_84 : Projection.WEB_MERCATOR;
        controller.initMapAndControls(projection);


        // add test trips to the list
        tripList.addTrip(new Trip("7/20/21", "[PA] West Rim Trail", "33", "75", "This was a great 3 day backpacking trip!"));
        tripList.addTrip(new Trip("5/2/22", "[NY] Colgate Hiking Trail", "1", "55", "Too short"));
        tripList.addTrip(new Trip("9/12/22", "[PA] Appalachian Trail", "2190", "62", "Very rocky :("));
        tripList.addTrip(new Trip("3/7/23", "[CA] Pacific Crest Trail", "2650", "84", "A lot of bears!"));
        tripList.addTrip(new Trip("3/7/23", "Colgate","2", "80", "Nice!"));

        //Temporary method to set up the coordinates
        tripList.search("Colgate").setxCord("42.8192806095");
        tripList.search("Colgate").setyCord("-75.5354365999");
        tripList.search("[NY] Colgate Hiking Trail").setxCord("42.8122807300");
        tripList.search("[NY] Colgate Hiking Trail").setyCord("-75.5364375300");
        System.out.println(tripList.search("Colgate").toString());
        planList.addPlan(new Plan("[NM] Philmont Scout Ranch", "180", "Hard"));
        preLoadCoordinates(tripList);
        createNewMapPin(controller);

        //KAIKAI ADD STUFF HERE

        TabPane tabPane = new TabPane();

        logTable.setPlaceholder(new Label("No hikes in your journal"));
        planTable.setPlaceholder(new Label("No plans in your journal"));
        
        Tab log = new Tab("Hiking Log");
        log.setContent(mainLogBox);
        Tab plan = new Tab("Planning Board");
        plan.setContent(mainPlanBox);
        Tab mapTab = new Tab("Your Trips");
        mapTab.setContent(rootNode);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(log, plan, mapTab);
        
//-------------Log-------------
        TableColumn dateCol = new TableColumn("Date (m/d/y)");
        TableColumn locCol = new TableColumn("Location");
        TableColumn distCol = new TableColumn("Distance (M)");
        //TableColumn tempCol = new TableColumn("Temp. (°F)");
        //TableColumn noteCol = new TableColumn("Notes");

        logColSetOnCommit(dateCol, locCol, distCol);
        logSizing(dateCol, locCol, distCol);
    
        logTable.setEditable(true);
        logTable.setPrefWidth(Integer.MAX_VALUE);
        logTable.setPrefHeight(Integer.MAX_VALUE);
        logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        logTable.setItems(logData);
        logTable.getColumns().addAll(dateCol, locCol, distCol);

//-------------Plan-------------
        TableColumn planCol = new TableColumn("Location");
        TableColumn approxCol = new TableColumn("Distance (M)");
        TableColumn diffCol = new TableColumn("Difficulty");

        planColSetOnCommit(planCol, approxCol, diffCol);      
        planSizing(planCol, approxCol, diffCol);

        planTable.setEditable(true);
        planTable.setMinWidth(WIDTH / 2);
        planTable.setPrefWidth(Integer.MAX_VALUE);
        planTable.setPrefHeight(Integer.MAX_VALUE);
        planTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        planTable.setItems(planData);
        planTable.getColumns().addAll(planCol, approxCol, diffCol);
        System.out.print("Hello from plantable!\n");
        
//-------------To-DO-------------
        todoBox.setAlignment(Pos.CENTER);
        todoBox.setPadding(new Insets(0, 10, 0, 10));
        todoList.setItems(todoData);
        todoList.setPrefWidth(Integer.MAX_VALUE);
        todoList.setPrefHeight(Integer.MAX_VALUE);
        todoList.setEditable(true);

        todoList.setCellFactory(TextFieldListCell.forListView());
        todoList.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> e) {
                todoList.getItems().set(e.getIndex(), e.getNewValue());
                System.out.println(e.getNewValue());
            }
        });

//-------------Sum & Total-------------
        counterSizing();

        update();

        totalBox.getChildren().addAll(totalLabel, total);
        sumBox.getChildren().addAll(sumLabel, sum);
    
//-------------Buttons-------------
        buttonSetOnAction(planCol, approxCol, diffCol);
        buttonSizing();

        // save just placed there for implementation
        buttonBox.getChildren().addAll(addButton, removeButton, saveButton);
        planButtonBox.getChildren().addAll(planAddButton, planRemoveButton);
        todoButtonBox.getChildren().addAll(todoAddButton, todoRemoveButton);
//-------------Main Stage-------------
        primaryStage.setTitle("Hiking Journal");
        primaryStage.setResizable(true);

        mainLogBox.getStyleClass().add("bg");
        mainPlanBox.getStyleClass().add("bg");

        Scene scene = new Scene(tabPane, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/com/example/demo/style.css").toExternalForm());

//-------------Accordion-------------
        //creating Vbox
        VBox details = new VBox(10);
        details.setPadding(new Insets(10));
        //Creating the TitlePane
        TitledPane pane1 = new TitledPane("Trip Details", details);
        pane1.setLayoutX(1);
        pane1.setLayoutY(1);
        //Creating a Accordion
        Accordion accor = new Accordion();
        accor.getPanes().add(pane1);

        accor.setPrefWidth(Integer.MAX_VALUE);
        accor.setPrefHeight(Integer.MAX_VALUE);

        noteDetails.setWrapText(true);

        Image image = new Image(new FileInputStream("Project 2/demo/src/main/resources/com/example/demo/default.png")); //default image
        imageView = new ImageView(image);

        imageView.setFitWidth(380);
        imageView.setPreserveRatio(true);

        Group handler = makeHandler();
        handler.translateXProperty().bindBidirectional(imageView.fitWidthProperty());
        handler.translateYProperty().bindBidirectional(imageView.fitHeightProperty());

        Group imageGroup = new Group(imageView, handler);

        chooseImage.setVisible(false);
        accor.setExpandedPane(pane1);

        details.getChildren().addAll(trailDetailsLabel, tempDetailsLabel, tempDetails, noteDetailsLabel, noteDetails, imageGroup, chooseImage);
        logBox.getChildren().addAll(accor);
        detailClick(primaryStage);
        detailArrowClick(scene, primaryStage);
        //imageUploadHandle(primaryStage, logTable.getSelectionModel().getSelectedItem());
        setNote(scene);
        setTemp(scene);

        //-------------Intro Stage-------------
        VBox introBox = new VBox();
        HBox introButtons = new HBox();

        Label title = new Label("Hiking Journal");
        Font font = new Font("Calibri", 80);
        title.setFont(font);
        title.setTextFill(Color.GHOSTWHITE);

        newJournal.setOnAction(e -> primaryStage.setScene(scene));

        introButtons.getChildren().addAll(newJournal, continueJournal);
        introButtons.setSpacing(10);
        introBox.getChildren().addAll(title, introButtons);
        introBox.setSpacing(5);

        HBox[] hBoxes = {introButtons};
        VBox[] vBoxes = {introBox};
        setAlignments(hBoxes, vBoxes, Pos.CENTER);

        introBox.setId("pane");
        Scene introScene = new Scene(introBox, WIDTH, HEIGHT);
        System.out.print("Hello from right before css getting!\n");
        introScene.getStylesheets().add(getClass().getResource("/com/example/demo/introStyle.css").toExternalForm());

        saveAction(primaryStage, scene);
        //-------------Add Stage-------------
        GridPane layout = new GridPane();

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        addDate.setMaxSize(250, 50);
        addLocation.setMaxSize(250, 50);
        addDistance.setMaxSize(250, 50);
        addTemp.setMaxSize(250, 50);
        addNote.setMaxSize(250, 50);

        layout.add(addDate, 1,1);
        layout.add(addLocation, 1,2);
        layout.add(addDistance, 1,3);
        layout.add(addTemp, 1,4);
        layout.add(addNote, 1, 5);
        layout.add(dateLabel, 0,1);
        layout.add(locationLabel, 0,2);
        layout.add(distanceLabel, 0,3);
        layout.add(tempLabel, 0,4);
        layout.add(noteLabel, 0,5);
        layout.add(submitButton, 1, 6);
        GridPane.setHalignment(submitButton, HPos.RIGHT);

        Scene addScene = new Scene(layout, 350, 230);
        Stage addWindow = new Stage();
        addWindow.setMaxHeight(250);
        addWindow.setMinHeight(250);
        addWindow.setMaxWidth(350);
        addWindow.setMinWidth(350);
        addWindow.setTitle("Add Trip");
        addWindow.setScene(addScene);

        addHandler(addWindow);


        primaryStage.setScene(introScene);
        primaryStage.setMinWidth(MINWIDTH);
        primaryStage.setMinHeight(MINHEIGHT);
        primaryStage.show();

        controller.refreshMap();
    }


    private void setAlignments(HBox[] hb, VBox[] vb, Pos pos) {

        for (int i=0; i<hb.length; i++) {
            hb[i].setAlignment(pos);
        }
        for (int i=0; i<vb.length; i++) {
            vb[i].setAlignment(pos);
        }
    }

    public void update(){
        try {
            total.setText("" + tripList.getTotal());
            System.out.println(tripList.getTotal());
            sum.setText("" + tripList.calculateTotalDistance());
            System.out.println(tripList.calculateTotalDistance());
        }
        catch (NumberFormatException nfe) {
            System.out.println("Not a number");
        }
    }

    //This loads all of the coordinates that may already be in the uploaded files.
    //Why the heck is the array not obj iterable?

    /**
     * This loads all of the coordinates which will be used to display on the map
     * @param tripList List of trips that are currently loaded
     * */
    private void preLoadCoordinates(TripList tripList){
        for (int i =0; i < tripList.getTotal(); i++){
            Coordinate temp = new Coordinate(Double.parseDouble(tripList.getTrip(i).getxCord()),Double.parseDouble(tripList.getTrip(i).getyCord()));
            tripCoordinates.add(temp);
        }
        System.out.println(tripCoordinates.toString());
    }

    //Stud(muffin) of a function. TODO: Implement the process for adding a coordinate to the mapView object
    public void createNewMapPin(Controller controller){
        //controller.addCoordinatetoMapView(tripList.getTrip(i));
        for (int i =0; i < tripList.getTotal(); i++){
            System.out.println("Attempting to add map Pins w/ " + tripList.getTrip(i).getLocation());
            controller.addCoordinatetoMapView(tripList.getTrip(i));
        }

    }

    public void addHandler(Stage stage){
        addButton.setOnAction(evt -> {
            stage.show();
        });

        submitButton.setOnAction(evt ->{
            Trip tripToAdd = new Trip(addDate.getText(), addLocation.getValue().toString(), addDistance.getText(), addTemp.getText(), addNote.getText());
            tripList.addTrip(tripToAdd);
            logData.add(tripToAdd);
            //KAI - DELETE THIS LATER!
            tripToAdd.setxCord("42.9122807300");
            tripToAdd.setyCord("-75.6354365999");
            System.out.println("X COORDINATE ADDED:" + tripToAdd.getxCord());
            controller.addCoordinatetoMapView(tripToAdd);
            update();
            //controller.addCoordinatetoMapView(tripToAdd);
             //clears old user inputs
            addDate.setText("");
            addDistance.setText("");
            addTemp.setText("");
            addNote.setText("");
            stage.close();
        });
    }
    public void buttonSetOnAction(TableColumn planCol, TableColumn approxCol, TableColumn diffCol){

        removeButton.setOnAction(evt -> {
            if (logTable.getSelectionModel().getSelectedItem()!= null){
                tripList.removeTrip(logTable.getSelectionModel().getSelectedItem());
                logData.remove(logTable.getSelectionModel().getSelectedItem());
                update();
            }
        });


        planAddButton.setOnAction(evt -> {
            planData.add(new Plan("", "", ""));
            planTable.edit(planList.getTotal(), planCol);
            planTable.getSelectionModel().select(planData.size() - 1);
            planList.addPlan(new Plan(planData.get(planData.size()-1).getLocation(), planData.get(planData.size()-1).getDistance(), planData.get(planData.size()-1).getDifficulty()));
        });

        planRemoveButton.setOnAction(evt -> {
            if (planTable.getSelectionModel().getSelectedItem()!= null){
                planList.removePlan(planTable.getSelectionModel().getSelectedItem());
                planData.remove(planTable.getSelectionModel().getSelectedItem());
            }
        });

        todoAddButton.setOnAction(evt -> {
            todoData.add(new String(""));
            todoList.edit(todoData.size() - 1);
            todoList.getSelectionModel().select(todoData.size() - 1);
        });

        todoRemoveButton.setOnAction(evt -> {
            if (todoList.getSelectionModel().getSelectedItem()!= null){
                todoData.remove(todoList.getSelectionModel().getSelectedItem());
                if(todoData.size() == 0){
                    todoAddButton.fire(); //prevents tableview from being empty
                }
            }
        });
    }
    private void updateTripList(){
        tripList.clearList();
        for (int i = 0; i < logData.size(); i++){
            tripList.addTrip(logData.get(i));

        }
    }
    private void updatePlanList(){
        planList.clearList();
        for (int i = 0; i < planData.size(); i++){
            planList.addPlan(planData.get(i));
        }
    }
    public void saveAction(Stage primaryStage, Scene scene){

        saveButton.setOnAction(evt -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                updateTripList();
                updatePlanList();
                SaveAndLoad.saveToFile(tripList, planList, todoData, file);
            }
            primaryStage.close();
        });

        continueJournal.setOnAction(evt -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                SaveAndLoad.openFile(logData, planData, tripList, planList, todoData, file);
                update();
                primaryStage.setScene(scene);
            }
        });

    }

    public void logColSetOnCommit(TableColumn dateCol, TableColumn locCol, TableColumn distCol){
        dateCol.setCellValueFactory(
            new PropertyValueFactory<Trip, String>("date"));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
        dateCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Trip, String>>() {
                @Override
                public void handle(CellEditEvent<Trip, String> e) {
                    ((Trip) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                    ).setDate(e.getNewValue());
                System.out.println(e.getNewValue());
                }
            }
        );

        
        locCol.setCellValueFactory(
            new PropertyValueFactory<Trip, String>("location"));
        locCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
        locCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Trip, String>>() {
                @Override
                public void handle(CellEditEvent<Trip, String> e) {
                    ((Trip) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                    ).setLocation(e.getNewValue());
                System.out.println(e.getNewValue());
                }
            }
        );
        
        
        distCol.setCellValueFactory(
            new PropertyValueFactory<Trip, Integer>("distance"));
        distCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
        distCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Trip, String>>() {
                @Override
                public void handle(CellEditEvent<Trip, String> e) {
                    ((Trip) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                    ).setDistance(e.getNewValue());
                System.out.println(e.getNewValue());
                update();
                }
            }
        );

        
//        tempCol.setCellValueFactory(
//            new PropertyValueFactory<Trip, Integer>("temp"));
//        tempCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
//        tempCol.setOnEditCommit(
//            new EventHandler<CellEditEvent<Trip, String>>() {
//                @Override
//                public void handle(CellEditEvent<Trip, String> e) {
//                    ((Trip) e.getTableView().getItems().get(
//                        e.getTablePosition().getRow())
//                    ).setTemp(e.getNewValue());
//                System.out.println(e.getNewValue());
//                }
//            }
//        );
        
        
//        noteCol.setCellValueFactory(
//            new PropertyValueFactory<Trip, String>("note"));
//        noteCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
//        noteCol.setOnEditCommit(
//            new EventHandler<CellEditEvent<Trip, String>>() {
//                @Override
//                public void handle(CellEditEvent<Trip, String> e) {
//                    ((Trip) e.getTableView().getItems().get(
//                        e.getTablePosition().getRow())
//                    ).setNote(e.getNewValue());
//                System.out.println(e.getNewValue());
//                }
//            }
//        );
    }

    public void planColSetOnCommit(TableColumn planCol, TableColumn approxCol, TableColumn diffCol){
        planCol.setCellValueFactory(
            new PropertyValueFactory<Plan, String>("location"));
        planCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
        planCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Plan, String>>() {
                @Override
                public void handle(CellEditEvent<Plan, String> e) {
                    ((Plan) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                    ).setLocation(e.getNewValue());
                System.out.println(e.getNewValue());
                }
            }
        );
        
        approxCol.setCellValueFactory(
            new PropertyValueFactory<Plan, String>("distance"));
        approxCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
        approxCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Plan, String>>() {
                @Override
                public void handle(CellEditEvent<Plan, String> e) {
                    ((Plan) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                    ).setDistance(e.getNewValue());
                System.out.println(e.getNewValue());
                }
            }
        );

        diffCol.setCellValueFactory(
            new PropertyValueFactory<Plan, String>("difficulty"));
        diffCol.setCellFactory(TextFieldTableCell.forTableColumn()); //CAUSING CRASH ON LAUNCH >:( (EDIT: switched data type from int to str and it resolved itself)
        diffCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Plan, String>>() {
                @Override
                public void handle(CellEditEvent<Plan, String> e) {
                    ((Plan) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                    ).setDifficulty(e.getNewValue());
                System.out.println(e.getNewValue());
                }
            }
        );
    }

    public void logSizing(TableColumn dateCol, TableColumn locCol, TableColumn distCol){
        dateCol.setMinWidth(100);
        dateCol.setMaxWidth(100);

        locCol.setMinWidth(200);

        distCol.setMinWidth(100);
        distCol.setMaxWidth(100);

//        tempCol.setMinWidth(100);
//        tempCol.setMaxWidth(100);
//
//        noteCol.setMinWidth(300);
        //noteCol.setPrefWidth(Integer.MAX_VALUE);
    }

    public void planSizing(TableColumn planCol, TableColumn approxCol, TableColumn diffCol){
        planCol.setMinWidth(200);

        approxCol.setMinWidth(100);
        approxCol.setMaxWidth(100);

        diffCol.setMinWidth(100);
        diffCol.setMaxWidth(100);
    }

    public void counterSizing(){
        totalBox.setMargin(totalLabel, new Insets(10, 40, 5, 10));
        sumBox.setMargin(sumLabel, new Insets(10, 0, 5, 40));

        total.setEditable(false);
        total.setMinHeight(25);
        total.setMinWidth(100);
        total.setMaxWidth(100);
        totalBox.setMargin(total, new Insets(0, 40, 10, 10));

        sum.setEditable(false);
        sum.setMinHeight(25);
        sum.setMinWidth(100);
        sum.setMaxWidth(100);
        sumBox.setMargin(sum, new Insets(0, 0, 10, 40));
    }

    public void buttonSizing(){
        addButton.setMinHeight(25);
        addButton.setMinWidth(100);
        addButton.setMaxWidth(100);
        buttonBox.setMargin(addButton, new Insets(20, 0, 10, 250));

        removeButton.setMinHeight(25);
        removeButton.setMinWidth(100);
        removeButton.setMaxWidth(100);
        buttonBox.setMargin(removeButton, new Insets(20, 0, 10, 10));

        saveButton.setMinHeight(25);
        saveButton.setMinWidth(100);
        saveButton.setMaxWidth(100);
        buttonBox.setMargin(saveButton, new Insets(20, 0, 10, 10));

        planAddButton.setMinHeight(25);
        planAddButton.setMinWidth(100);
        planAddButton.setMaxWidth(100);
        planButtonBox.setMargin(planAddButton, new Insets(10, 0, 5, 10));

        planRemoveButton.setMinHeight(25);
        planRemoveButton.setMinWidth(100);
        planRemoveButton.setMaxWidth(100);
        planButtonBox.setMargin(planRemoveButton, new Insets(0, 0, 10, 10));

        todoAddButton.setMinHeight(25);
        todoAddButton.setMinWidth(100);
        todoAddButton.setMaxWidth(100);
        todoButtonBox.setMargin(todoAddButton, new Insets(10, 0, 5, 310));

        todoRemoveButton.setMinHeight(25);
        todoRemoveButton.setMinWidth(100);
        todoRemoveButton.setMaxWidth(100);
        todoButtonBox.setMargin(todoRemoveButton, new Insets(0, 0, 10, 310));
    }

    protected void detailClick(Stage stageIn) {
        //String h = housemates.getSelectionModel().getSelectedItem();

        logTable.setOnMouseClicked(evt -> {
            Trip row = logTable.getSelectionModel().getSelectedItem();
            System.out.println("CLICKED");

            if(row != null){
                tempDetails.setText("" + row.getTemp());
                noteDetails.setText("" + row.getNote());
                trailDetailsLabel.setText("" + row.getLocation());
                try {
                    updateImage(row);
                } catch (IOException e) {
                    System.out.println("oof");
                    throw new RuntimeException(e);
                }
                chooseImage.setVisible(true);
                imageUploadHandle(stageIn, logTable.getSelectionModel().getSelectedItem());
            }

        });
    }

    protected void detailArrowClick(Scene sceneIn, Stage stageIn) {
        sceneIn.addEventFilter(KeyEvent.ANY, evt -> {
            Trip row = logTable.getSelectionModel().getSelectedItem();

            if (evt.getCode() == KeyCode.DOWN || evt.getCode() == KeyCode.UP) {
                System.out.println("ARROW PRESSED");
                if(row != null){
                    tempDetails.setText("" + row.getTemp());
                    noteDetails.setText("" + row.getNote());
                    trailDetailsLabel.setText("" + row.getLocation());
                    try {
                        updateImage(row);
                    } catch (IOException e) {
                        System.out.println("oof");
                        throw new RuntimeException(e);
                    }
                    chooseImage.setVisible(true);
                    imageUploadHandle(stageIn, logTable.getSelectionModel().getSelectedItem());
                }
            }

        });

    }

    protected void setNote(Scene sceneIn) {
        sceneIn.addEventFilter(KeyEvent.ANY, evt -> {
            Trip row = logTable.getSelectionModel().getSelectedItem();

            if (evt.getCode() == KeyCode.ESCAPE) {
                System.out.println("NOTE UPDATED");
                System.out.println(noteDetails.getText());
                if(row != null && noteDetails.getText() != null){
                    row.setNote(noteDetails.getText());
                    logTable.requestFocus();
                }
            }
        });
    }

    protected void setTemp(Scene sceneIn) {
        sceneIn.addEventFilter(KeyEvent.ANY, evt -> {
            Trip row = logTable.getSelectionModel().getSelectedItem();

            if (evt.getCode() == KeyCode.ESCAPE) {
                System.out.println("NOTE UPDATED");
                System.out.println(tempDetails.getText());
                if(row != null && tempDetails.getText() != null){
                    row.setTemp(tempDetails.getText());
                    logTable.requestFocus();
                }
            }
        });
    }
    private Group makeHandler() {

        Polygon polygon = new Polygon();
        Group group = new Group(polygon);
        polygon.getPoints().addAll(0.0, 0.0, 0.0, -20.0, -20.0, 0.0);
        polygon.setStroke(Color.BLACK);
        polygon.setFill(Color.AZURE);

        polygon.setStrokeWidth(2);
        polygon.setStrokeType(StrokeType.INSIDE);

        group.setOnMousePressed(e -> {

            startX = group.getLayoutX() - e.getX();
            startY = group.getLayoutY() - e.getY();

        });

        group.setOnMouseDragged(e -> {
            group.setTranslateX(group.getTranslateX() + e.getX() + startX);
            group.setTranslateY(group.getTranslateY() + e.getY() + startY);

        });

        return group;
    }
    private void updateImage(Trip row) throws IOException{
        Image image = new Image(new FileInputStream(row.getPathName())); //default image
        imageView.setImage(image);
    }

    private void imageUploadHandle(Stage primaryStage, Trip row){
        chooseImage.setOnAction(evt -> {
            System.out.println("Image Selection Clicked");
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show open file dialog
            File file = fileChooser.showOpenDialog(primaryStage);


            if (file != null) {
                row.setPathName(file.getPath());
                try {
                    updateImage(row);
                } catch (IOException e) {
                    System.out.println("oof");
                    throw new RuntimeException(e);
                }
            }
        });
    }
}