/**
 * Javadoc description part:
 * The program finds the stations a person need to pass to arrive at its destination.
 * Also, it demonstrates the way with an animation.
 * Javadoc tags part:
 * @author Nuri Basar, Student ID: 2021400129
 * @since date: 17.03.2023
 */
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class Nuri_Basar {
    /**
     * reads information about metro lines and breakpoints, and holds all of them in a related array
     * Also, prints out all the station names,and makes an animation
     * @param args Main input arguments are not used
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Create List and Same import method to read file and take input
        Scanner scan = new Scanner(System.in); // to take input from user
        File file = new File("input.txt"); // to open the file
        Scanner fileContent = new Scanner(file); // this enables to read file content
        String initialStation = scan.next(); // Taking initial station input
        String targetStation = scan.next(); // Taking final station input
        ArrayList <String> metroLinesName = new ArrayList<>(); // to add the names of metro lines each of them is an element
        ArrayList < ArrayList <Integer > > metroLinesColor = new ArrayList<>(); // each metro line has different rgb numbers, so it is a multidimensional list.
        ArrayList < ArrayList<String> > stationName = new ArrayList<>(); // each metro line has a lot of station, and
        // the list is multidimensional to separate the station of a metro line from the others'
        ArrayList < ArrayList< ArrayList<Integer> > > stationCoordinates = new ArrayList<>(); //this list conserves the coordinates ([x,y]) of each station and
        // enables to have same index with their station.
        ArrayList < String > breakpointNamesArray = new ArrayList<>(); // this conserve the names of stations which are breakpoints
        ArrayList < ArrayList < String > > metroNamesRelatedBreakpoint = new ArrayList<>(); // this list holds the names of metro lines which intersect
        // with the breakpoint which is on the same index.
        int timer = 0; // we know how many station we have, so timer is used to collect information about metro lines such as name and coordinate of their stations.
        //  Taking Data From The File

        while (timer<27) { // the folder has 27 lines
            String sentence = fileContent.nextLine(); // reading each line in the folder
            String[] sentenceArray = sentence.split(" "); // we have a space between each information, so we can separate each information.
            if (timer < 20) { // while timer smaller than 20, we read the lines which contain information related to metro line such as their color numbers and coordinates.
                if (timer % 2 == 0) { // when timer is an even number, the sentence which is related to the name of metro line and its rgb numbers is read.
                    metroLinesName.add(sentenceArray[0]); // to add metro line name to the list respectively
                    String[] colorNumbers = sentenceArray[1].split(","); // there is a comma between each rgb number.
                    // By splitting them from the comma, we can take each number separately.
                    ArrayList<Integer> temporarilyArrayList = new ArrayList<>(); // to add each color number of a metro line,
                    // and thanks to this, I can add this array to the outside array
                    for (String color : colorNumbers) {
                        int convertedColorNum = Integer.parseInt(color); // converting the rgb numbers whose type is string to integer type
                        temporarilyArrayList.add(convertedColorNum);
                    }
                    metroLinesColor.add(temporarilyArrayList); // adding an extra list to the outside list
                } else { // when timer is odd, we read information about the station of the metro line at same index such as their name and their coordinates
                    ArrayList<String> stations = new ArrayList<>();// to add each station names of a metro
                    // and thanks to this, I can add this array to the outside array
                    ArrayList<ArrayList<Integer>> stationCoordinateOutsideArray = new ArrayList<>(); // I create a temporary array-list to prevent from mach a coordinate with wrong index
                    for (int i = 0; i < sentenceArray.length; i++) {
                        if (i % 2 == 0) { // when "i" is even, we take the name of a station
                            stations.add(sentenceArray[i]);
                        } else { // when "i" is odd, we take the coordinate of previous station
                            ArrayList<Integer> coordinates = new ArrayList<>(); // the list is created to carry information another inside called  stationCoordinateOutsideArray.
                            String[] coordinateArray = sentenceArray[i].split(","); // each x and y coordinate has a comma between them. If I separate them from comma, I can take them directly
                            coordinates.add(Integer.parseInt(coordinateArray[0])); // x coordinate number
                            coordinates.add(Integer.parseInt(coordinateArray[1])); // y coordinate number
                            stationCoordinateOutsideArray.add(coordinates); // adding the inside array to the other inside array
                        }
                    }
                    stationCoordinates.add(stationCoordinateOutsideArray); // adding the biggest inside array to the outside array which will use.
                    stationName.add(stations); //
                }
            } else { // when timer is greater than 19, the computer reads the sentences related to breakpoints.
                breakpointNamesArray.add(sentenceArray[0]); // the first element of the separated array is the name of a breakpoint station.
                ArrayList<String> metroNamesInnerArray = new ArrayList<>(); // the same reason with the other inside arrays
                for (int i = 1; i < sentenceArray.length; i++) {// the other elements of the array are the names of metro lines which have a station there.
                    metroNamesInnerArray.add(sentenceArray[i]);
                }
                metroNamesRelatedBreakpoint.add(metroNamesInnerArray);
            }
            timer += 1;
        }
        // To select the names and coordinates of the stations which should be written on the output animation
        ArrayList<ArrayList<String>> writtenStationName = new ArrayList<>(); // The list of Stations which should be written on the background map
        ArrayList<ArrayList<ArrayList<Integer>>> writtenStationCoordinate = new ArrayList<>(); // the list of coordinates of the stations in the previous list
        for (int i=0 ; i<stationName.size(); i++){
            ArrayList<String>subStationNameArray=new ArrayList<>(); // All lists between loops have the same purpose
            ArrayList<ArrayList<Integer>> subStationCoordinates = new ArrayList<>();
            for (int j=0; j<stationName.get(i).size(); j++){
                if (stationName.get(i).get(j).startsWith("*")){ // stations which have "*" in their zeroth element should be written
                    subStationNameArray.add(stationName.get(i).get(j).substring(1));
                    stationName.get(i).set(j,stationName.get(i).get(j).substring(1)); // to make my search easy in incoming loops related to station names
                    subStationCoordinates.add(stationCoordinates.get(i).get(j));
                }
            }
            writtenStationCoordinate.add(subStationCoordinates);
            writtenStationName.add(subStationNameArray);
        }
        // Find Which metro line we start the path and which metro line we finish the path
        String initialMetroLine = null;
        String finalMetroLine = null;
        for (int i = 0; i < stationName.size(); i++) {
            for (String station : stationName.get(i)) {
                if (station.equals(initialStation))
                    initialMetroLine = metroLinesName.get(i);
                if (station.equals(targetStation))
                    finalMetroLine = metroLinesName.get(i);
            }
        }
        // Control if there are stations with named same as inputs or not
        if (initialMetroLine == null || finalMetroLine == null) {
            System.out.println("No such station names in this map");
            return;
        }
        // Finding Metro Lines to arrive the target station
        ArrayList<String> metroLinesFollows= new ArrayList<>(); // to use in the incoming method
        ArrayList<ArrayList<String>> metroLinesOutsideArray = new ArrayList<>(); // to hold all tried path by the incoming method
        findMetroLineWayWithRecursion(initialMetroLine,finalMetroLine,metroNamesRelatedBreakpoint,metroLinesFollows,metroLinesOutsideArray); //the method finds the paths which are correct and wrong
        ArrayList<String> metroLinesForTheDestination= new ArrayList<>(); // the array holds the metro line names which should be followed to reach to the target station
        for (ArrayList<String> subList:metroLinesOutsideArray){
            if (! subList.contains("Wrong")) // wrong paths from the previous method include "Wrong" and I need to select them
                metroLinesForTheDestination=subList;
        }
        // Controlling if there is a path to arrive the target station
        if (metroLinesForTheDestination.size()==0){
            System.out.println("These two stations are not connected");
            return;
        }

        // Determining which station we need to pass
        ArrayList<String> followingStations = new ArrayList<>(); // contains all stations we need to pass
        ArrayList<ArrayList<Integer>> followingStationCoordinates = new ArrayList<>(); // contains the coordinates of the station
        // in the previous array named followingStations
        if (metroLinesForTheDestination.size()==1){ // This means that target station and initial station
            // are on the same metro line
            String metroLine = metroLinesForTheDestination.get(0);  // the only metro line we have
            int metroLineIndex = metroLinesName.indexOf(metroLine); // the index we use to find
            // the station name of the metro line
            ArrayList <String> theMetroLineStations = stationName.get(metroLineIndex); // the station of the metro line
            ArrayList < ArrayList <Integer>> theStationsCoordinates = stationCoordinates.get(metroLineIndex); // the coordinate of the stations
            int initialStationIndex= theMetroLineStations.indexOf(initialStation); // the index of initial station on the station array
            int targetStationIndex= theMetroLineStations.indexOf(targetStation); // the index of target station on the station array
            // to find which side we need to move
            if (targetStationIndex==initialStationIndex) {
                followingStations.add(initialStation);
                followingStationCoordinates.add(theStationsCoordinates.get(initialStationIndex));
            }else if(targetStationIndex>initialStationIndex){ // (to the right) means that I should move to increasing index order
                for (int i=initialStationIndex; i<=targetStationIndex;i++){
                    followingStations.add(theMetroLineStations.get(i));
                    followingStationCoordinates.add(theStationsCoordinates.get(i));
                }
            }else{// (to the left) means that I should move to decreasing index order
                int currentIndex = targetStationIndex;
                while (currentIndex<=initialStationIndex){
                    followingStations.add(theMetroLineStations.get(currentIndex));
                    followingStationCoordinates.add(theStationsCoordinates.get(currentIndex));
                    currentIndex ++;
                }
            }
        }else{
            // To find Which breakpoints we need to change metro lines
            ArrayList<String> breakpointToChangeMetroLine = findingBreakpointWeUse(metroLinesForTheDestination,metroNamesRelatedBreakpoint,breakpointNamesArray);
            // To check whether there are unnecessary metro line in the array which contains metro lines we need to change
            if (breakpointToChangeMetroLine.get(0).equals(initialStation))
                metroLinesForTheDestination.remove(0);
            if (breakpointToChangeMetroLine.get(breakpointToChangeMetroLine.size()-1).equals(targetStation))
                metroLinesForTheDestination.remove(metroLinesForTheDestination.size()-1);
            //Again we need to check where we need to change metro lines
            breakpointToChangeMetroLine = findingBreakpointWeUse(metroLinesForTheDestination,metroNamesRelatedBreakpoint,breakpointNamesArray);
            // Determining the stations
            followingStations.add(initialStation);
            int initialMetroIndex=metroLinesName.indexOf(initialMetroLine); // the index we use to find
            // the station name of initial metro line
            int initialStationIndex=stationName.get(initialMetroIndex).indexOf(initialStation);
            followingStationCoordinates.add(stationCoordinates.get(initialMetroIndex).get(initialStationIndex));
            // To find all station with changing the metro line
            for (int i=0; i<breakpointToChangeMetroLine.size(); i++){
                String currentMetroLine = metroLinesForTheDestination.get(i);
                int currentMetroLineIndex= metroLinesName.indexOf(currentMetroLine);
                ArrayList <String> theMetroLineStations = stationName.get(currentMetroLineIndex);// the station of a metro Line which we are following
                ArrayList < ArrayList <Integer>> theStationsCoordinates = stationCoordinates.get(currentMetroLineIndex);
                int currentStationIndex = theMetroLineStations.indexOf(followingStations.get(followingStations.size()-1)); // the last element of station array is
                // the initial element for the following metro Line
                int breakpointIndex = theMetroLineStations.indexOf(breakpointToChangeMetroLine.get(i));// every breakpoint station is a target station for its metro line
                // to find which side we need to move (The same as the above if - else)
                if(breakpointIndex>currentStationIndex){
                    for (int j=currentStationIndex+1; j<=breakpointIndex;j++){
                        followingStations.add(theMetroLineStations.get(j));
                        followingStationCoordinates.add(theStationsCoordinates.get(j));
                    }
                }else{
                    int currentIndex = currentStationIndex-1;
                    while (currentIndex>=breakpointIndex){
                        followingStations.add(theMetroLineStations.get(currentIndex));
                        followingStationCoordinates.add(theStationsCoordinates.get(currentIndex));
                        currentIndex --;
                    }
                }
            }
            // We can find all station except for the last metro line. so this find the station on the last metro line
            String currentMetroLine = metroLinesForTheDestination.get(metroLinesForTheDestination.size()-1);
            int currentMetroLineIndex= metroLinesName.indexOf(currentMetroLine);
            ArrayList <String> theMetroLineStations = stationName.get(currentMetroLineIndex);
            ArrayList < ArrayList <Integer>> theStationsCoordinates = stationCoordinates.get(currentMetroLineIndex);
            int currentStationIndex = theMetroLineStations.indexOf(followingStations.get(followingStations.size()-1));
            int breakpointIndex = theMetroLineStations.indexOf(targetStation);
            if(breakpointIndex>currentStationIndex){
                for (int j=currentStationIndex+1; j<=breakpointIndex;j++){
                    followingStations.add(theMetroLineStations.get(j));
                    followingStationCoordinates.add(theStationsCoordinates.get(j));
                }
            }else{
                int currentIndex = currentStationIndex-1;
                while (currentIndex>=breakpointIndex){
                    followingStations.add(theMetroLineStations.get(currentIndex));
                    followingStationCoordinates.add(theStationsCoordinates.get(currentIndex));
                    currentIndex --;
                }
            }
        }
        // Printing the stations to the screen
        for (String station : followingStations)
            System.out.println(station);
        // Creating Canvas and setting related things
        int canvasWidth = 1024;
        int canvasHeight = 482;
        StdDraw.setCanvasSize(canvasWidth,canvasHeight); // Creating a canvas
        StdDraw.setXscale(0,1024);
        StdDraw.setYscale(0,482);
        StdDraw.picture(512,241,"background.jpg");
        double penLineRadius = 0.012;
        double stationRadius = 0.01;
        StdDraw.setPenRadius(penLineRadius);
        StdDraw.enableDoubleBuffering();
        // Putting all related to Canvas
        ArrayList < ArrayList <Integer > > shouldOrange = new ArrayList<>(); // this array holds the coordinates of the station we passed
        //Drawing Lines
        drawingLines(metroLinesColor,stationCoordinates);
        // Drawing circles
        drawingCircle(stationCoordinates,stationRadius,shouldOrange);
        // Writing the names of stations
        writingStationName(writtenStationName,writtenStationCoordinate);
        // Animating the transition

        int numStationWePass = followingStations.size();
        double animationBallRadius = 0.02;
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        int pauseDuration = 300;
        int currentPositionX;
        int currentPositionY;
        for (int i=0; i<numStationWePass; i++){
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            currentPositionX=followingStationCoordinates.get(i).get(0);
            currentPositionY=followingStationCoordinates.get(i).get(1);
            shouldOrange.add(followingStationCoordinates.get(i));
            StdDraw.setPenRadius(animationBallRadius);
            StdDraw.point(currentPositionX,currentPositionY);
            StdDraw.show();
            StdDraw.pause(pauseDuration);
            // the orange point on the station cannot be undone,
            // so we need to reset and change the color of the station we have passed
            if (i!= numStationWePass-1) {
                StdDraw.clear();
                StdDraw.picture(512, 241, "background.jpg");
                StdDraw.setPenRadius(penLineRadius);
                drawingLines(metroLinesColor, stationCoordinates);
                drawingCircle(stationCoordinates, stationRadius, shouldOrange);
                writingStationName(writtenStationName, writtenStationCoordinate);
                StdDraw.show();
            }
        }
    }
    /**
     *the method finds recursively the metro line way we need to follow to arrive the destination
     * @param currentMetroLine holds the metro line the person uses now.
     * @param finalMetroLine is the metro line the person try to arrive at
     * @param  breakpointMetroLines holds the names of metro lines which intersect at a breakpoint
     * @param metroLinesFollows holds the metro lines which have passed to find the way
     * @param outerArrayList takes all path tried to arrive at the finalMetroLine
     *                       both correct and incorrect
     */
    //
    public static void findMetroLineWayWithRecursion(String currentMetroLine, String finalMetroLine, ArrayList<ArrayList <String> > breakpointMetroLines , ArrayList<String> metroLinesFollows,ArrayList<ArrayList<String>> outerArrayList){
        // if current metro line is equal to the metro line we want to arrive, we need to add and break the method
        if (currentMetroLine.equals(finalMetroLine)) {
            metroLinesFollows.add(currentMetroLine);
            outerArrayList.add(metroLinesFollows);
            return;
        }else{
            ArrayList<String> availableMetroLines = findingAvailableMetroLines(currentMetroLine,metroLinesFollows,breakpointMetroLines);// function call to find which metro lines we can change for the current metro line
            metroLinesFollows.add(currentMetroLine);
            // if there is no metro line we can pass, this means we follow the wrong path
            if (availableMetroLines.size()==0){
                metroLinesFollows.add("Wrong"); // to make easy the following event such as finding which path is correct
                outerArrayList.add(metroLinesFollows);
                return;
            }else{
                // every available metro line for current metro line should be tried
                for(String newMetroLine: availableMetroLines){
                    currentMetroLine=newMetroLine;
                    ArrayList<String> copyList = new ArrayList<>(metroLinesFollows);
                    findMetroLineWayWithRecursion(currentMetroLine,finalMetroLine,breakpointMetroLines,metroLinesFollows,outerArrayList);
                    metroLinesFollows=copyList;
                }
            }
        }
        return;
    }
    /**
     * gives an array which holds the name of metro lines to which can be changed from current metro line
     * @param currentMetroLine holds the metro line the person uses now.
     * @param breakpointMetroLines holds the names of metro lines which intersect at a breakpoint
     * @param followingMetroLine holds the metro lines which have passed to find the way not to turn back again.
     * @return an array including the names of the metro lines.
     */
    public static ArrayList<String> findingAvailableMetroLines (String currentMetroLine, ArrayList < String > followingMetroLine, ArrayList < ArrayList < String >> breakpointMetroLines) {
        ArrayList<String> availableMetroLines = new ArrayList<>();
        for (ArrayList<String> breakpointMetroLinesInnerArray : breakpointMetroLines) { // to find which breakpoint we can use to change current metro line
            if (followingMetroLine.size() == 0) { // if there is no metro line we have used, we can add all metro line at the same breakpoint with current metro line
                if (breakpointMetroLinesInnerArray.contains(currentMetroLine)) { // if the array contains the current metro line we should handle the array
                    for (String neighboorMetroLine : breakpointMetroLinesInnerArray) {
                        if (!neighboorMetroLine.equals(currentMetroLine)) // we should not turn back by taking current metro line
                            availableMetroLines.add(neighboorMetroLine);
                    }
                }
            } else {
                if (breakpointMetroLinesInnerArray.contains(currentMetroLine)) { // if the array contains the current metro line we should handle the array
                    // to look if there is a station we passed
                    boolean isItOkay=true;
                    for (String metroLine:breakpointMetroLinesInnerArray){ // we don't need to turn back.
                        // because recursion will already try the all point at the previous breakpoints
                        // if I turn back, I move like a circle
                        if (followingMetroLine.contains(metroLine)){
                            isItOkay=false;
                            break;
                        }
                    }
                    if (isItOkay){ // If not, we can add them to available point
                        for (String neighboorMetroLine : breakpointMetroLinesInnerArray) {
                            if (!neighboorMetroLine.equals(currentMetroLine))
                                availableMetroLines.add(neighboorMetroLine);
                        }
                    }

                }
            }
        }
        return availableMetroLines;
    }
    /**
     * gives an array which holds the names of breakpoint we need to arrive to change current metro line to incoming metro line we need to use
     * @param metroLinesForTheDestination holds the metro lines which should be used to arrive at the destination
     * @param metroNamesRelatedBreakpoint holds the names of metro lines which intersect at a breakpoint
     * @param breakpointNamesArray includes the names of all breakpoints
     * @return the array including the names of the breakpoints
     */
    public static ArrayList<String> findingBreakpointWeUse (ArrayList<String> metroLinesForTheDestination,ArrayList < ArrayList < String > > metroNamesRelatedBreakpoint,ArrayList < String > breakpointNamesArray){
        ArrayList<String> breakpointToChangeMetroLine = new ArrayList<>();
        for (int j=1; j<metroLinesForTheDestination.size(); j++){  // starts with 1 because we need to all metro lines and the metro line from which is passed to them
            String leftMetroLineName =metroLinesForTheDestination.get(j-1); // previous metro line
            String rightMetroLinesName = metroLinesForTheDestination.get(j); // metro line
            for (int i=0; i<metroNamesRelatedBreakpoint.size(); i++ ){
                if (metroNamesRelatedBreakpoint.get(i).contains(rightMetroLinesName) && metroNamesRelatedBreakpoint.get(i).contains(leftMetroLineName)) { // to find the breakpoint at which we can
                    // change the current metro line with the other. It enables us to determine the side which we need to move on the metro line (to the left or to the right)
                    breakpointToChangeMetroLine.add(breakpointNamesArray.get(i));
                }
            }
        }
        return breakpointToChangeMetroLine;
    }

    /**
     * draws the metro lines
     * @param metroLinesColor holds the RGB numbers of each metro line
     * @param stationCoordinates includes the coordinates of all station of all metro lines
     */
    public static void drawingLines(ArrayList < ArrayList <Integer > > metroLinesColor,ArrayList<ArrayList < ArrayList <Integer > > > stationCoordinates){
        for (int i=0 ; i<metroLinesColor.size(); i++){ // to draw lines, I write two different loop to draw line and circle because The two color and coordinate can be mixed.
            StdDraw.setPenColor(metroLinesColor.get(i).get(0),metroLinesColor.get(i).get(1),metroLinesColor.get(i).get(2));
            for (int j = 1 ; j<stationCoordinates.get(i).size(); j++){
                StdDraw.line(stationCoordinates.get(i).get(j-1).get(0),stationCoordinates.get(i).get(j-1).get(1),stationCoordinates.get(i).get(j).get(0),stationCoordinates.get(i).get(j).get(1));
            }
        }
    }

    /**
     * puts the point of each station
     * @param stationCoordinates includes the coordinates of all station of all metro lines
     * @param stationRadius is the size of each point
     * @param shouldOrange holds the coordinates of the stations which have passed.
     */
    public static void drawingCircle(ArrayList<ArrayList < ArrayList <Integer > > > stationCoordinates,double stationRadius,ArrayList < ArrayList <Integer > > shouldOrange){
        StdDraw.setPenRadius(stationRadius);
        for (int i = 0; i < stationCoordinates.size(); i++) { // to draw each circle
            StdDraw.setPenColor(StdDraw.WHITE);
            for (int j = 0; j < stationCoordinates.get(i).size(); j++) {
                if (! shouldOrange.contains(stationCoordinates.get(i).get(j))) //this line to paint the metro station points we have passed
                    // if a coordinate is in the list, it is passed and should be orange
                    StdDraw.point(stationCoordinates.get(i).get(j).get(0), stationCoordinates.get(i).get(j).get(1));
                else {
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                    StdDraw.point(stationCoordinates.get(i).get(j).get(0), stationCoordinates.get(i).get(j).get(1));
                    StdDraw.setPenColor((StdDraw.WHITE));
                }
            }
        }

    }

    /**
     * writes the name of metro lines which should be written on the canvas
     * @param writtenStationName holds the names of the stations which must be written on the background
     * @param writtenStationCoordinate holds the coordinates of the stations.
     *
     */
    public static void writingStationName(ArrayList<ArrayList<String>> writtenStationName,ArrayList<ArrayList<ArrayList<Integer>>> writtenStationCoordinate){
        for (int i=0; i<writtenStationName.size(); i++) {
            StdDraw.setPenColor(StdDraw.BLACK);
            for (int j=0; j<writtenStationName.get(i).size(); j++) {
                StdDraw.setFont(new Font("Helvetica", Font.BOLD, 8));
                StdDraw.text(writtenStationCoordinate.get(i).get(j).get(0), writtenStationCoordinate.get(i).get(j).get(1) + 5, writtenStationName.get(i).get(j));
            }
        }
    }
}
