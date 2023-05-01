# Hiking Log Project

## Statement
### The Nature and Purpose of our Project
The purpose of this project is to create a digital equivalent of a traditional Hiking Log. It will have all of the personal characteristics of a physical hiking journal with the ease and organization of a web application. This program is intended to be used before and after the hike, so that the user can prepare and reflect on their journey.

Like a physical hiking log, users can first plan out their hikes on the designated “Planning Board” tab. Users can specify the hike’s:
* Location
* Distance
* Difficulty level
* Required equipment

Once the hike is completed, the user can add it to the “Hiking Log” tab, specifying the hike’s:
* Name
* Distance
* Date
* Temperature on the day hiked
* Notes

Using this information, the program calculate and displays the user’s hiking statistics including:
* The total amount of hiking trips taked
* The total amount of miles traveled 

Our program will evolve from this initial application by first reorganizing and formatting our current visual components in order to make the program visually appealing and easy for the user to navigate. We will:
1. Creating a start window to introduce our application and its purpose 
2. To improve the look of our program, we will focus on stylizing the current visual components by implementing color backgrounds and customized buttons using CSS so that it will have a cleaner, more aesthetically pleasing look
3. Reformat the program’s buttons and tableviews so that they are centered within the window, allowing for the application to be seamlessly resized.

In addition, we will also implement a search feature in which users can filter through a database of hiking trails in the United States and select one to add to their log. If the user searches for a trail that is not in the database, they can select the custom add feature which allows them to add any trail to the log. Our process is to:
1. Create a TextField with AutoComplete functionality based on the supplied trail list from the dataset
2. Display a pop up window to enter the necessary information to add trails not contained in the dataset to the hiking log

Another visual element of our program is a map of the United States in which has pins geographically displaying where the user has hiked. Our process is to:
1. Create pins using a prebuilt library by Eingestellt von Solo displaying a country map of the United States 
2. Add the MapView to the tab’s scene and use the mapping library’s API to add the trail pins to their designated location and access additional functionality such as panning and zooming

We also intend to add a drag-and-drop feature in which users can upload pictures from their hike into the log. When the user hovers over one of the pins on the map, the name of the trail will be displayed in addition to any pictures from that hike that have been uploaded to the program. Our program will have a save button so that all of the information inputted will be stored in a file so that users can continue to edit and grow their hiking logs. 

## Mockups for the Hiking Log

Main Screen for the Hiking Log by which users will be able to primarily interact with the program.


An accordion menu will appear on the right hand side of the screen when the user clicks “Add Trip.” Here, the user can input their log and will be assisted by an autocomplete function should they wish.

The right hand side accordion menu will display the input information for a hike that they click on within the in-the-table display.


This map will be under “Your Journeys” and will place pins or “Points of Interest” in the map which users can hover over, accessing the same data as in the hiking log. Upon hovering, it displays the name and the date of the hike. If the user clicks on it, they will be redirected to its respective entry in the log.

## Technical Outline
### File Responsibilities
* Trip.java
    * Intializes the trip objects based off of the user’s “Hiking Log” input
    
* Plan.java
    * Intializes the plan objects based off of the user’s “Planning Board” input

* Trailinit.java
    * This file will be loaded before any of the main parts of the program are accessible to the user and stored as final global variable so that it may only be accessed. 
    * This file will be responsible for creating a hashmap which will then point to another hashmap so that it may be readily accessed within our main file. The formatting for this hashmap will go as follows:
        * myMap : TrailsHashMap : TrailName -> Information
    * The specific information in “Information” will be latitude, longitude, and length of the trail.  

* Country.java
    * While our source does have a lot of data, for this version we will be only using the US Country to help map out all of the data and make it easier. Placing the Country object in context of the Globe object will also allow us to get precise information about the coordinates of the trails and we will not have to recontextualize the geographic information. 

* HikingJournal.java
    * This is the primary file, containing all of the code for the GUI as well as a number of helper functions

* Style.css
    * This file simply contains the css style code to customize the application’s colors and widgets

### Objects and Data Structures
* One of the unique data structures we are building is the access to the maps functionality from within the main file of the program. This data structure is known as the “Trails Library'' which is a hashmap pointing to another hashmap. The top layer hashmap is meant to be a readily accessible method which allows the program to quickly access the nested hashmap. The nested hashmap contains:
    * the trail names 
    * points to an array containing the information of the trails including:
        * longitude
        * latitude
        * state location
        * park name
        * city location
This hashmap will be accessed by the rest of the program including the country map developed by Eingestellt von Solo which we will be using. Kai is primarily responsible for the integration and interfacing between these two aspects of the program in development.

* The majority of the data presented to the user is through the two tableviews. 
    * Displays logged trips
    * Displays planned trips. 
Both tableviews utilize an observable list of trip and plan objects respectively, the constructors of these being defined in the helper files. Additionally, there is a listview todo list which uses a simple observable list of type String.

*  In order for the user to upload pictures of their hike to the log, we will create a drag-and-drop operation within the program. This operation will:
    * Transfer data between two objects
        * Gesture source object
        * Gesture target object
    * Be implemented between our JavaFX program and a desktop application
    * Transfer data from the users desktop to the program using a dragboard
    * Utilize the DragEvent class to implement the drag-and-drop gesture. 

* Once the user is finished editing their hiking journal, they will have the option to save it. The user can also reload one of their previously saved hiking journals to the program by selecting the specified “Continue Previous Log” button in the start screen. These operation will: 
    * Utilize the FileChooser class to create a file choosing dialog to select files for saving and opening
    * Use the default system’s progam for file choosing
    * Only allow .txt files to be loaded
    * Write and save the text content to the file once the file name is specified and the save button is pressed

* More in depth details will be stored in an accordion to the right of the log tab’s tableview. The goal of this is to limit clutter on the main page so user experience is not cluttered. This will display:
    * Trail name
    * Weather Conditions/temperature
    * Note section
    * User uploadable picture position
        * with default picture as a placeholder

* On the map of “Your Journeys” part, users will be able to interact with a map which will display the various hikes that they have done along with their locations using “pins” which are automatically made with their data entries. 
    * The pin is technically called “Point of interest” and it directly interacts with Country Pane where it has precise longitude and latitude coordinates which are displayed upon the map. 
    * The country pane is a display for the United States containing the mapping of longitude and latitude and is used to display previous hikes


## Objectives for this project
1. Working with the World Map Library to implement an interactive map of the United States in which the location of each hike contained in the log is geographically pinned in the appropriate region
2. Reorganize the visual components of our GUI to make it more aesthetically pleasing and easy for the user to navigate
3. Create a search feature for hiking trails in which the name of the trail is autofilled from a dataset of hiking trails in the United States
4. Initialize a custom “Add Trail” button in which users can add a trail to the hiking log, inputting the trail’s name into a search bar that will have autofill suggestions based on the trails contained in our dataset
5. Implement graphics contexts to visually display and compare the data of each hike
6. Add a Drag and Drop feature in which users can upload their own photos, which can be shown when the user hovers over a map pin
7. Check off feature in to do list that strikes-through an element contained in the list, conveying that it has been completed
8. Implement a save feature in which the contents of the hiking log can be saved to a file and later edited in the application
9. Create a start screen for the program in which the user can decide to create a new hiking log or continue editing a previously saved hiking log
10. Stylize program with CSS to customize background colors and buttons

## Bibliography
### URLs
https://coderslegacy.com/java/switch-between-scenes-in-javafx/ 
* Switching scenes in JavaFX

https://stackoverflow.com/questions/36861056/javafx-textfield-auto-suggestions 

https://gist.github.com/floralvikings/10290131 
* TextField with AutoFill

https://docs.oracle.com/javase/8/javafx/api/javafx/collections/transformation/FilteredList.html 
* filtered list to order the contents of the list view by date, distance, temperature

https://docs.oracle.com/javafx/2/layout/style_css.htm 
* Guide for CSS formatting

https://stackoverflow.com/questions/37647933/how-can-i-use-the-google-maps-apis-in-a-javafx-desktop-application 
* Google maps API in JavaFx

https://stackoverflow.com/questions/26783907/how-to-access-a-variable-defined-in-another-scope-in-less 
* Custom widget hovering

https://docs.oracle.com/javase/8/javafx/events-tutorial/drag-drop.htm#CHDJFJDH 
* Drag and drop operation

https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html 
* Dialogue window

https://genuinecoder.com/save-files-javafx-filechooser/ 
* Saving files using FileChooser

https://github.com/HanSolo/countries
* Repository containing parts of the code for our solution to drawing and implementing an interactive map which users can place pins upon. 

https://www.kaggle.com/datasets/planejane/national-park-trails?resource=download
* Publicly available dataset containing all trails within the United States National Parks used for the auto filling function 

### Books

William P. Ehling. 1990. Fifty hikes in western New York: walks and day hikes from the Cattaraugus Hills to the Genesee Valley. Backcountry Publications, Woodstock, Vt.

John Forti. 2021. The heirloom gardener: traditional plants & skills for the modern world. Timber Press, Portland, Oregon.

Herman Shugart, Peter White, Sassan Saatchi, and Jérôme Chave. 2022. The world atlas of trees and forests: exploring Earth’s forest ecosystems. Princeton University Press, Princeton.

## Authors

* [Kai Davis](kdavis@colgate.edu)
* [Luke Spagnoli](lspagnoli@colgate.edu)
* [Riley Del Priore](rdelpriore@colgate.edu)

## Version History

* 0.1 (3/29/23)
    * Initial Release
* 0.2 (5/11/23)
    * Save feature 
    * Improved UI
    * Map function
