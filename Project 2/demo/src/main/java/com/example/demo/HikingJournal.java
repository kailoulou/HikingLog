package com.example.demo;

import com.sothawo.mapjfx.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

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

    //initialize tableview
    private TableView<Trip> logTable = new TableView<Trip>();
    private ObservableList<Trip> logData = FXCollections.observableArrayList(
            new Trip("7/20/21", "[PA] West Rim Trail", "33", "75", "This was a great 3 day backpacking trip!"),
            new Trip("5/2/22", "[NY] Colgate Hiking Trail", "1", "55", "Too short"),
            new Trip("9/12/22", "[PA] Appalachian Trail", "2190", "62", "Very rocky :("),
            new Trip("3/7/23", "[CA] Pacific Crest Trail", "2650", "84", "A lot of bears!")
            //new Trip("-", "-", "-", "-", "-")
    );



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

    Label addLabel = new Label("Add Trip");
    Label locationLabel = new Label("Location:");
    TextField addLocation = new TextField();

    Label dateLabel = new Label("Date (m/d/y):");
    TextField addDate = new TextField();

    Label distanceLabel = new Label("Distance (M):");
    TextField addDistance = new TextField();

    Label tempLabel = new Label("Weather:");
    TextField addTemp = new TextField();

    Label noteLabel = new Label("Notes:");
    TextArea addNote = new TextArea();

    Button submitButton = new Button("Submit");

    ArrayList<String> suggestions = new ArrayList<>();
    private AutoCompletionBinding<String> autoCompletionBinding;
    //init size
    private final int WIDTH = 800;
    private final int HEIGHT = 500;

    // MIN WIDTH & HEIGHT
    private final int MINWIDTH = 760;
    private final int MINHEIGHT = 500;

    //log tab
    final VBox totalBox = new VBox();
    final VBox sumBox = new VBox();
    final VBox buttonBox = new VBox();
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


    public static void main(String[] args){
        System.out.print("Hello there!\n");
        launch(args);
    }


    public void start(Stage primaryStage) throws IOException {
        // add test trips to the list
        tripList.addTrip(new Trip("7/20/21", "[PA] West Rim Trail", "33", "75", "This was a great 3 day backpacking trip!"));
        tripList.addTrip(new Trip("5/2/22", "[NY] Colgate Hiking Trail", "1", "55", "Too short"));
        tripList.addTrip(new Trip("9/12/22", "[PA] Appalachian Trail", "2190", "62", "Very rocky :("));
        tripList.addTrip(new Trip("3/7/23", "[CA] Pacific Crest Trail", "2650", "84", "A lot of bears!"));

        planList.addPlan(new Plan("[NM] Philmont Scout Ranch", "180", "Hard"));

        TabPane tabPane = new TabPane();

        logTable.setPlaceholder(new Label("No hikes in your journal"));
        planTable.setPlaceholder(new Label("No plans in your journal"));
        
        Tab log = new Tab("Hiking Log");
        log.setContent(mainLogBox);
        Tab plan = new Tab("Planning Board");
        plan.setContent(mainPlanBox);
        Tab map = new Tab("Pin Board");

        System.out.print("Hello from top!\n");

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(log, plan, map);
        
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

        details.getChildren().addAll(trailDetailsLabel, tempDetailsLabel, tempDetails, noteDetailsLabel, noteDetails);
        logBox.getChildren().addAll(accor);
        detailClick();
        detailArrowClick(scene);
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
        autoComplete();
        //private Set<String> possibleSuggestions = new HashSet<>(List.of(suggestions));


        primaryStage.setScene(introScene);
        primaryStage.setMinWidth(MINWIDTH);
        primaryStage.setMinHeight(MINHEIGHT);
        primaryStage.show();
    }
    private void setAlignments(HBox[] hb, VBox[] vb, Pos pos) {

        for (int i=0; i<hb.length; i++) {
            hb[i].setAlignment(pos);
        }
        for (int i=0; i<vb.length; i++) {
            vb[i].setAlignment(pos);
        }
    }

    public void autoComplete(){
        //TextFields.bindAutoCompletion(addLocation, "Hey", "Hello", "Hello World", "Apple", "Cool", "Costa", "Cola", "Coca Cola");

        /*ReadData.suggestionsArray(suggestions, "Project 2/Edited AllTrails data - nationalpark.csv");
        String[] possibleSuggestionsArray = new String[suggestions.size()];
        possibleSuggestionsArray = suggestions.toArray(possibleSuggestionsArray);
        Set<String> possibleSuggestions = new HashSet<>(Arrays.asList(possibleSuggestionsArray));

        autoCompletionBinding = TextFields.bindAutoCompletion(addLocation, possibleSuggestions);

        addLocation.setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case ENTER:
                    autoCompletionLearnWord(possibleSuggestions, addLocation.getText().trim());
                    break;
                default:
                    break;
            }
        });
         */
    }

    private void autoCompletionLearnWord(Set<String> possibleSuggestions, String newWord){
        possibleSuggestions.add(newWord);
        // we dispose the old binding and recreate a new binding
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }
        autoCompletionBinding = TextFields.bindAutoCompletion(addLocation, possibleSuggestions);
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

    public void addHandler(Stage stage){
        addButton.setOnAction(evt -> {
            stage.show();
        });

        submitButton.setOnAction(evt ->{
            tripList.addTrip(new Trip(addDate.getText(), addLocation.getText(), addDistance.getText(), addTemp.getText(), addNote.getText()));
            logData.add(new Trip(addDate.getText(), addLocation.getText(), addDistance.getText(), addTemp.getText(), addNote.getText()));
            update();
            addLocation.setText(""); //clears old user inputs
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
        totalBox.setMargin(total, new Insets(0, 40, 0, 10));

        sum.setEditable(false);
        sum.setMinHeight(25);
        sum.setMinWidth(100);
        sum.setMaxWidth(100);
        sumBox.setMargin(sum, new Insets(0, 0, 0, 40));
    }

    public void buttonSizing(){
        addButton.setMinHeight(25);
        addButton.setMinWidth(100);
        addButton.setMaxWidth(100);
        buttonBox.setMargin(addButton, new Insets(10, 0, 5, 400));

        removeButton.setMinHeight(25);
        removeButton.setMinWidth(100);
        removeButton.setMaxWidth(100);
        buttonBox.setMargin(removeButton, new Insets(0, 0, 10, 400));

        saveButton.setMinHeight(25);
        saveButton.setMinWidth(100);
        saveButton.setMaxWidth(100);
        buttonBox.setMargin(saveButton, new Insets(0, 0, 10, 400));

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

    protected void detailClick() {
        //String h = housemates.getSelectionModel().getSelectedItem();

        logTable.setOnMouseClicked(evt -> {
            Trip row = logTable.getSelectionModel().getSelectedItem();
            System.out.println("CLICKED");

            if(row != null){
                tempDetails.setText("" + row.getTemp());
                noteDetails.setText("" + row.getNote());
                trailDetailsLabel.setText("" + row.getLocation());
            }
        });
    }

    protected void detailArrowClick(Scene sceneIn) {
        sceneIn.addEventFilter(KeyEvent.ANY, evt -> {
            Trip row = logTable.getSelectionModel().getSelectedItem();

            if (evt.getCode() == KeyCode.DOWN || evt.getCode() == KeyCode.UP) {
                System.out.println("ARROW PRESSED");
                if(row != null){
                    tempDetails.setText("" + row.getTemp());
                    noteDetails.setText("" + row.getNote());
                    trailDetailsLabel.setText("" + row.getLocation());
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
}