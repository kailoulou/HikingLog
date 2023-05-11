/*
 Copyright 2015-2020 Peter-Josef Meisch (pj.meisch@sothawo.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.example.demo;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for the FXML defined code.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class Controller {

    /** logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    /** some coordinates from around town. */
    private static final Coordinate coordKarlsruheCastle = new Coordinate(49.013517, 8.404435);
    private static final Coordinate coordKarlsruheHarbour = new Coordinate(49.015511, 8.323497);
    private static final Coordinate coordKarlsruheStation = new Coordinate(48.993284, 8.402186);

    //private static final Coordinate coordKarlsruheSoccer = new Coordinate(49.020035, 8.412975);
    private static final Coordinate coordKarlsruheUniversity = new Coordinate(49.011809, 8.413639);

    private static final Coordinate coordOldRagTrail = new Coordinate(38.57052, -78.28729);

    private static final Coordinate coordColgateUniversity = new Coordinate(42.8192806095, -75.5354365999);
    private static final Extent extentAllLocations = Extent.forCoordinates(coordKarlsruheCastle, coordKarlsruheHarbour, coordKarlsruheStation);

    private static final Coordinate coordGermanyNorth = new Coordinate(55.05863889, 8.417527778);
    private static final Coordinate coordGermanySouth = new Coordinate(47.27166667, 10.17405556);
    private static final Coordinate coordGermanyWest = new Coordinate(51.0525, 5.866944444);
    private static final Coordinate coordGermanyEast = new Coordinate(51.27277778, 15.04361111);
    private static final Extent extentGermany = Extent.forCoordinates(coordGermanyNorth, coordGermanySouth, coordGermanyWest, coordGermanyEast);

    /** default zoom value. */
    private static final int ZOOM_DEFAULT = 14;

    private final Marker colgateMarker;

    private final Marker markerOldRagTrail;

    /** the labels. */

    private final MapLabel colgateLabel;

    @FXML
    /** button to set the map's zoom. */
    private Button buttonZoom;

    /** the MapView containing the map */
    @FXML
    private MapView mapView;

    /** the box containing the top controls, must be enabled when mapView is initialized */
    @FXML
    private HBox topControls;

    /** Slider to change the zoom value */
    @FXML
    private Slider sliderZoom;

    /** Accordion for all the different options */
    @FXML
    private Accordion leftControls;

    /** section containing the location button */
    @FXML
    private TitledPane optionsLocations;

    /** button to set the map's center */
    @FXML
    private Button buttonKaHarbour;

    /** button to set the map's center */
    @FXML
    private Button buttonKaCastle;

    /** button to set the map's center */
    @FXML
    private Button buttonKaStation;

    /** button to set the map's center */
    @FXML
    private Button buttonKaSoccer;

    /** button to set the map's extent. */
    @FXML
    private Button buttonAllLocations;

    /** for editing the animation duration */
    @FXML
    private TextField animationDuration;

    /** the BIng Maps API Key. */
    @FXML
    private TextField bingMapsApiKey;

    /** Label to display the current center */
    @FXML
    private Label labelCenter;

    /** Label to display the current extent */
    @FXML
    private Label labelExtent;

    /** Label to display the current zoom */
    @FXML
    private Label labelZoom;

    /** label to display the last event. */
    @FXML
    private Label labelEvent;

    /** RadioButton for MapStyle OSM */
    @FXML
    private RadioButton radioMsOSM;

    /** RadioButton for MapStyle Stamen Watercolor */
    @FXML
    private RadioButton radioMsSTW;

    /** RadioButton for MapStyle Bing Roads */
    @FXML
    private RadioButton radioMsBR;

    /** RadioButton for MapStyle Bing Roads - dark */
    @FXML
    private RadioButton radioMsCd;

    /** RadioButton for MapStyle Bing Roads - grayscale */
    @FXML
    private RadioButton radioMsCg;

    /** RadioButton for MapStyle Bing Roads - light */
    @FXML
    private RadioButton radioMsCl;

    /** RadioButton for MapStyle Bing Aerial */
    @FXML
    private RadioButton radioMsBA;

    /** RadioButton for MapStyle Bing Aerial with Label */
    @FXML
    private RadioButton radioMsBAwL;

    /** RadioButton for MapStyle WMS. */
    @FXML
    private RadioButton radioMsWMS;

    /** RadioButton for MapStyle XYZ */
    @FXML
    private RadioButton radioMsXYZ;

    /** ToggleGroup for the MapStyle radios */
    @FXML
    private ToggleGroup mapTypeGroup;

    /** Check button for harbour marker */
    @FXML
    private CheckBox checkKaHarbourMarker;

    /** Check button for castle marker */
    @FXML
    private CheckBox checkKaCastleMarker;

    /** Check button for harbour marker */
    @FXML
    private CheckBox checkKaStationMarker;

    /** Check button for soccer marker */
    @FXML
    private CheckBox checkKaSoccerMarker;

    /** Check button for click marker */
    @FXML
    private CheckBox checkClickMarker;

    /** the first CoordinateLine */
    private CoordinateLine trackMagenta;
    /** Check button for first track */
    @FXML
    private CheckBox checkTrackMagenta;

    /** the second CoordinateLine */
    private CoordinateLine trackCyan;
    /** Check button for first track */
    @FXML
    private CheckBox checkTrackCyan;

    /** Coordinateline for polygon drawing. */
    private CoordinateLine polygonLine;
    /** Check Button for polygon drawing mode. */
    @FXML
    private CheckBox checkDrawPolygon;

    /** Check Button for constraining th extent. */
    @FXML
    private CheckBox checkConstrainGermany;

    @FXML
    private CheckBox checkColgateUniversity;

    /** params for the WMS server. */
    private WMSParam wmsParam = new WMSParam()
        .setUrl("http://ows.terrestris.de/osm/service?")
        .addParam("layers", "OSM-WMS");

    private XYZParam xyzParams = new XYZParam()
        .withUrl("https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x})")
        .withAttributions(
            "'Tiles &copy; <a href=\"https://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer\">ArcGIS</a>'");


    //KAI SHENANIGANS GO HERE!
    public ArrayList<Coordinate> coordinatesList = new ArrayList<>();
    public ArrayList<MapLabel> labelsList = new ArrayList<>();
    public ArrayList<Marker> markerList = new ArrayList<>();


    public Controller() {
        //Creating the markers for the user

        // a couple of markers using the provided ones
        coordinatesList.add(coordColgateUniversity);
        System.out.println("Colgate Coordinates added!");
        markerOldRagTrail = Marker.createProvided(Marker.Provided.GREEN).setPosition(coordOldRagTrail).setVisible(true);
        colgateMarker = Marker.createProvided(Marker.Provided.RED).setPosition(coordColgateUniversity).setVisible(true);
        colgateLabel = new MapLabel("Colgate University",10,-10).setVisible(true).setCssClass("red-label");
        colgateMarker.attachLabel(colgateLabel);
        markerList.add(colgateMarker);
        labelsList.add(colgateLabel);
        //refreshMap();
        //addListCoordinates(tripList);
        // the fix label, default style
    }



    /**
     * called after the fxml is loaded and all objects are created. This is not called initialize any more,
     * because we need to pass in the projection before initializing.
     *
     * @param projection
     *     the projection to use in the map.
     */
    public void initMapAndControls(Projection projection) {
        logger.trace("begin initialize");

        // init MapView-Cache
        final OfflineCache offlineCache = mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";

        // set the custom css file for the MapView
        mapView.setCustomMapviewCssURL(getClass().getResource("/com/example/demo/custom_mapview.css"));

        leftControls.setExpandedPane(optionsLocations);

        // set the controls to disabled, this will be changed when the MapView is intialized
        setControlsDisable(true);

        logger.trace("location buttons done");

        // wire the zoom button and connect the slider to the map's zoom
        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT));
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        // add a listener to the animationDuration field and make sure we only accept int values
        animationDuration.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                mapView.setAnimationDuration(0);
            } else {
                try {
                    mapView.setAnimationDuration(Integer.parseInt(newValue));
                } catch (NumberFormatException e) {
                    animationDuration.setText(oldValue);
                }
            }
        });
        animationDuration.setText("500");

        // bind the map's center and zoom properties to the corresponding labels and format them
        labelCenter.textProperty().bind(Bindings.format("center: %s", mapView.centerProperty()));
        labelZoom.textProperty().bind(Bindings.format("zoom: %.0f", mapView.zoomProperty()));
        logger.trace("options and labels done");

        // watch the MapView's initialized property to finish initialization
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });

        // observe the map type radiobuttons
        mapTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("map type toggled to {}", newValue.toString());
            MapType mapType = MapType.OSM;
            if (newValue == radioMsOSM) {
                mapType = MapType.OSM;
            } else if (newValue == radioMsBR) {
                mapType = MapType.BINGMAPS_ROAD;
            } else if (newValue == radioMsCd) {
                mapType = MapType.BINGMAPS_CANVAS_DARK;
            } else if (newValue == radioMsCg) {
                mapType = MapType.BINGMAPS_CANVAS_GRAY;
            } else if (newValue == radioMsCl) {
                mapType = MapType.BINGMAPS_CANVAS_LIGHT;
            } else if (newValue == radioMsBA) {
                mapType = MapType.BINGMAPS_AERIAL;
            } else if (newValue == radioMsBAwL) {
                mapType = MapType.BINGMAPS_AERIAL_WITH_LABELS;
            } else if (newValue == radioMsWMS) {
                mapView.setWMSParam(wmsParam);
                mapType = MapType.WMS;
            } else if (newValue == radioMsXYZ) {
                mapView.setXYZParam(xyzParams);
                mapType = MapType.XYZ;
            }
            mapView.setBingMapsApiKey(bingMapsApiKey.getText());
            mapView.setMapType(mapType);
        });
        mapTypeGroup.selectToggle(radioMsOSM);

        setupEventHandlers();

        // add the graphics to the checkboxes
        checkColgateUniversity.setGraphic(
                new ImageView(new Image(colgateMarker.getImageURL().toExternalForm(), 16.0, 16.0, true, true)));

        // bind the checkboxes to the markers visibility
        checkColgateUniversity.selectedProperty().bindBidirectional(colgateMarker.visibleProperty());
        logger.trace("marker checks done");

        // load two coordinate lines
        trackMagenta = loadCoordinateLine(getClass().getResource("/com/example/demo/M1.csv")).orElse(new CoordinateLine
            ()).setColor(Color.MAGENTA);
        trackCyan = loadCoordinateLine(getClass().getResource("/com/example/demo/M2.csv")).orElse(new CoordinateLine
            ()).setColor(Color.CYAN).setWidth(7);
        logger.trace("tracks loaded");
        checkTrackMagenta.selectedProperty().bindBidirectional(trackMagenta.visibleProperty());
        checkTrackCyan.selectedProperty().bindBidirectional(trackCyan.visibleProperty());
        logger.trace("tracks checks done");
        // get the extent of both tracks
        Extent tracksExtent = Extent.forCoordinates(
            Stream.concat(trackMagenta.getCoordinateStream(), trackCyan.getCoordinateStream())
                .collect(Collectors.toList()));
        ChangeListener<Boolean> trackVisibleListener =
            (observable, oldValue, newValue) -> mapView.setExtent(tracksExtent);
        trackMagenta.visibleProperty().addListener(trackVisibleListener);
        trackCyan.visibleProperty().addListener(trackVisibleListener);

        // add the polygon check handler
        ChangeListener<Boolean> polygonListener =
            (observable, oldValue, newValue) -> {
                if (!newValue && polygonLine != null) {
                    mapView.removeCoordinateLine(polygonLine);
                    polygonLine = null;
                }
            };
        checkDrawPolygon.selectedProperty().addListener(polygonListener);

        // add the constrain listener
        checkConstrainGermany.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                mapView.constrainExtent(extentGermany);
            } else {
                mapView.clearConstrainExtent();
            }
        }));

        // finally initialize the map view
        logger.trace("start map initialization");
        mapView.initialize(Configuration.builder()
            .projection(projection)
            .showZoomControls(false)
            .build());
        logger.debug("initialization finished");

        long animationStart = System.nanoTime();
    }

    /**
     * initializes the event handlers.
     */
    private void setupEventHandlers() {
        // add an event handler for singleclicks, set the click marker to the new position when it's visible
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            final Coordinate newPosition = event.getCoordinate().normalize();
            labelEvent.setText("Event: map clicked at: " + newPosition);
            if (checkDrawPolygon.isSelected()) {
                handlePolygonClick(event);
            }
        });

        // add an event handler for MapViewEvent#MAP_EXTENT and set the extent in the map
        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            mapView.setExtent(event.getExtent());
        });

        // add an event handler for extent changes and display them in the status label
        mapView.addEventHandler(MapViewEvent.MAP_BOUNDING_EXTENT, event -> {
            event.consume();
            labelExtent.setText(event.getExtent().toString());
        });

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: map right clicked at: " + event.getCoordinate());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker right clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label clicked: " + event.getMapLabel().getText());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label right clicked: " + event.getMapLabel().getText());
        });

        mapView.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> {
            logger.debug("pointer moved to " + event.getCoordinate());
        });

        logger.trace("map handlers initialized");
    }


    private void animateClickMarker(Coordinate oldPosition, Coordinate newPosition) {
        // animate the marker to the new position
        final Transition transition = new Transition() {
            private final Double oldPositionLongitude = oldPosition.getLongitude();
            private final Double oldPositionLatitude = oldPosition.getLatitude();
            private final double deltaLatitude = newPosition.getLatitude() - oldPositionLatitude;
            private final double deltaLongitude = newPosition.getLongitude() - oldPositionLongitude;

            {
                setCycleDuration(Duration.seconds(1.0));
            }

            @Override
            protected void interpolate(double v) {
                final double latitude = oldPosition.getLatitude() + v * deltaLatitude;
                final double longitude = oldPosition.getLongitude() + v * deltaLongitude;
            }
        };
        transition.play();
    }

    /**
     * shows a new polygon with the coordinate from the added.
     *
     * @param event
     *     event with coordinates
     */
    private void handlePolygonClick(MapViewEvent event) {
        final List<Coordinate> coordinates = new ArrayList<>();
        if (polygonLine != null) {
            polygonLine.getCoordinateStream().forEach(coordinates::add);
            mapView.removeCoordinateLine(polygonLine);
            polygonLine = null;
        }
        coordinates.add(event.getCoordinate());
        polygonLine = new CoordinateLine(coordinates)
            .setColor(Color.DODGERBLUE)
            .setFillColor(Color.web("lawngreen", 0.4))
            .setClosed(true);
        mapView.addCoordinateLine(polygonLine);
        polygonLine.setVisible(true);
    }

    /**
     * enables / disables the different controls
     *
     * @param flag
     *     if true the controls are disabled
     */
    private void setControlsDisable(boolean flag) {
        topControls.setDisable(flag);
        leftControls.setDisable(flag);
    }

    /**
     * finishes setup after the mpa is initialzed
     */
    private void afterMapIsInitialized() {
        logger.trace("map intialized");
        logger.debug("setting center and enabling controls...");
        // start at the harbour with default zoom
        mapView.setZoom(ZOOM_DEFAULT);
        mapView.setCenter(coordColgateUniversity);
        // add the markers to the map - they are still invisible
        mapView.addMarker(colgateMarker);
        //mapView.addMarker(markerKaSoccer);
        // can't add the markerClick at this moment, it has no position, so it would not be added to the map

        // add the fix label, the other's are attached to markers.


        // add the tracks
        mapView.addCoordinateLine(trackMagenta);
        mapView.addCoordinateLine(trackCyan);
        refreshMap();

        // now enable the controls
        setControlsDisable(false);
    }

    public void addCoordinatetoMapView(Trip tripInput){
        Coordinate tempCoord = new Coordinate(Double.parseDouble(tripInput.getxCord()), Double.parseDouble(tripInput.getyCord()));
        Marker tempMarker = Marker.createProvided(Marker.Provided.RED)
                .setPosition(tempCoord).setVisible(true);
        MapLabel tempLabel = new MapLabel(tripInput.getLocation(), 10, -10).setVisible(true).setCssClass("red-label");
        //Adding the stuff to the thing
        coordinatesList.add(tempCoord);
        labelsList.add(tempLabel);
        markerList.add(tempMarker);
        //refreshMap();
        //System.out.println(tempCoord + " "+tempMarker + " "+ tempLabel + " "+tempCheckbox);
        //System.out.println(coordinatesList);
    }

    //KAIKAI
    public void refreshMap(){
        for (int i =0; i < coordinatesList.size(); i++){
            markerList.get(i).createProvided(Marker.Provided.RED)
                    .setPosition(coordinatesList.get(i)).setVisible(true);
            markerList.get(i).attachLabel(labelsList.get(i));
            mapView.addMarker(markerList.get(i));
        }
    }

    /**
     * load a coordinateLine from the given uri in lat;lon csv format
     *
     * @param url
     *     url where to load from
     * @return optional CoordinateLine object
     * @throws java.lang.NullPointerException
     *     if uri is null
     */
    private Optional<CoordinateLine> loadCoordinateLine(URL url) {
        try (
            Stream<String> lines = new BufferedReader(
                new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).lines()
        ) {
            return Optional.of(new CoordinateLine(
                lines.map(line -> line.split(";")).filter(array -> array.length == 2)
                    .map(values -> new Coordinate(Double.valueOf(values[0]), Double.valueOf(values[1])))
                    .collect(Collectors.toList())));
        } catch (IOException | NumberFormatException e) {
            logger.error("load {}", url, e);
        }
        return Optional.empty();
    }
}
