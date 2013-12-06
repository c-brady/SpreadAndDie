import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class DisplayBoard{
    
    public static int columns;
    public static int rows;
    private static char regionTypes[];
    private static int diseaseMovementPositions[][];

    public static char region[][]; 
    public static char diseasedAndInfected[][];


    public static char fullBoard[][];
    /** class constructor, sets the default elements/values for many of the vars **/
    public static void DisplayBoard(){
        columns                  = 12; //grid size should be an even number
        rows                     = 12;
        regionTypes              = new char[] {',','.',';',':'};
        diseaseMovementPositions = new int[][] {{0,1},{0,-1},{1,0},{-1,0}};
    }
    public static boolean runTurn(){
        if (playerDeathCheck() == false){
            SpreadDisease();
            changeRegions();
            Ai.movePlayer();
            printBoard();
            infectionsToDesease();
            return true;
            }
            else return false;
    }
     /**changes the columns and rows attributes according to input, then set message to confirm to the user**/
    private static void setRegionSize(int c, int r){
        columns = c;
        rows    = r;
        GameMenu.setMessage("Grid changed to " + c + " x " + r);
    }
     /** validates user input, pases the valid data to setRegionSize then returns false**/
    public Boolean testRegionSize(int c, int r){
        if ((c >= 3 && r >= 3) && (c != 20 || r != 20)){ 
            if ( ( (c % 2) == 0) && ( (r % 2) == 0) ){      //ideally grid size should be an even number
                setRegionSize(c,r);
                return false;
            }
            else if (GameMenu.listen("Ideally grid size should be an even number, enter 'y' to ignore this: ") == 'y'){
                setRegionSize(c,r);
                return false;
            }
        }
       else if (c <= 3 && r <= 3){
            System.out.println("There must be a larger grid then 3x3");
            return true;
        }
        return true;
    }
    /** validates user input, pases the valid data to setRegionSize then returns false
    *populates tables by first making them the required size then generating the correct elements**/
    public static void populateArrays() 
    {
        System.out.println("Populating Board");
        region = new char[columns][rows];
        diseasedAndInfected = new char[columns][rows];
        Random random = new Random();
        for(int i = 0; i < columns; i++)
        {
            for(int j = 0; j < rows; j++)
                {
                region[i][j] = regionTypes[random.nextInt(SpreadAndDie.regionNumber)];
                diseasedAndInfected[i][j] = '_';
                }
        }
        if (! SpreadAndDie.getPlayerSetLocation())
            randomSpawn();
    }
    /**places player in random place on board then notifies the user**/
    private static void randomSpawn(){
        System.out.println("Attempting to place PLAYER in a valid location.");
        Random r = new Random();
        int xrows = r.nextInt(rows);
        int xcolumns = r.nextInt(columns);
        SpreadAndDie.setPlayerLocation(xcolumns,xrows);
        System.out.println("Random spawn created in " + xcolumns + ',' + xrows);
    }
    /** Adds Disease to diseasedAndInfected array but first makes sure the location is valid (within a location on the board and not top of the player)**/
    public static Boolean setDesiseLocation(int xcolumns, int xrows) 
    {                       
        if (xcolumns > -1 && xcolumns < columns &&
            xrows > -1 && xrows < rows && 
            xcolumns != SpreadAndDie.getPlayerLocaion(0) && 
            xrows != SpreadAndDie.getPlayerLocaion(1))
        {
            diseasedAndInfected[xcolumns][xrows] = '1';
            System.out.println("Disease placed in " +  xrows + ',' + xcolumns);
            GameMenu.setMessage("Disease placed in " +  xrows + ',' + xcolumns);
            return true;
        }
        else
        {
            System.out.println("Disease was attempted to be placed in invalid location " +  xrows + ',' + xcolumns);
            return false;
        }
    }
    /** if the user has set the DesiseLocation this function copy's it to the diseasedAndInfected array **/
    public static void percistantDeseasedSpawn(){
        if (SpreadAndDie.getplayerSetDesiseLocation())
            diseasedAndInfected[SpreadAndDie.getDiseaseLocation(0)][SpreadAndDie.getDiseaseLocation(1)] = '1';
    }
    /** runs if game is on level 3, randomly changes regions around (and at) the player **/
    private static void changeRegions(){
        Random ra = new Random();
        if (Ai.getlevel() == 3){
            for(int pos[] : Ai.getPlayerMovmentPositions()){
                int c = SpreadAndDie.getPlayerLocaion(0) + pos[0];            
                int r = SpreadAndDie.getPlayerLocaion(1) + pos[1];
                if(c >= 0 && c < columns && r >= 0 && r < rows)
                    region[c][r] = regionTypes[ra.nextInt(SpreadAndDie.regionNumber)];
            }
        }
    }
    /** clones the provided array 
      * http://stackoverflow.com/questions/5617016/how-do-i-copy-a-2-dimensional-array-in-java 
      * this is necessary to stop shallow copy problems making the Disease only spreading in only direction**/
    private static char[][] cloneArray(char[][] inputArray) {
        int length = inputArray.length;
        char[][] targetedArray = new char[length][inputArray[0].length];
        for (int i = 0; i < length; i++) 
            System.arraycopy(inputArray[i], 0, targetedArray[i], 0, inputArray[i].length);
        return targetedArray;
    }
    /** CORE function:
      * First creates local copy of array and iterates through it. When a Disease is found the surrounding cells (excluding diagonally located ones)
      * are then checked if they are the same region, if this is true then it is made a fresh Disease (represented as a 1) else they are made infected (I)**/
    private static void SpreadDisease(){ 
        char[][] TempDiseasedAndInfected = new char [columns][rows];
        TempDiseasedAndInfected = cloneArray(diseasedAndInfected);
        for(int i = 0; i < columns; i++){
            for(int j = 0; j < rows; j++){
                if (diseasedAndInfected[i][j] == '1'){
                    for(int pos[] : diseaseMovementPositions){
                        int c = i + pos[0];
                        int r = j + pos[1];
                        if(c >= 0 && c < columns && r >= 0 && r < rows){
                            if (diseasedAndInfected[c][r] != '0'){
                                if (region[i][j] == region[c][r]){
                                        TempDiseasedAndInfected[c][r] = '1';
                                }
                                else if (region[i][j] != region[c][r])
                                     TempDiseasedAndInfected[c][r] = 'I';
                            }
                        }
                    }
                }
            }         
        }
        diseasedAndInfected  = cloneArray(TempDiseasedAndInfected);
    }
     // public void spreadDisease(){                                 //old vershion of spreadDisease 
 //     for(int i = 0; i < columns; i++){
 //         for(int j = 0; j < rows; j++){
 //             if (diseasedAndInfected[i][j] == '0'){
 //                 if (i != 0)
 //                     if (region[i][j] == region[i - 1][j]) //left
 //                         diseasedAndInfected[i - 1][j] = '0';
 //                     else diseasedAndInfected[i - 1][j] = 'I';
 //                 if (i != columns - 1)
 //                     if (region[i][j] == region[i + 1][j]) //right
 //                         diseasedAndInfected[i + 1][j] = '0';
 //                     else diseasedAndInfected[i + 1][j] = 'I';
 //                 if (j != rows - 1)
 //                     if (region[i][j] == region[i][j + 1]) //down
 //                         diseasedAndInfected[i][j + 1] = '0';
 //                     else diseasedAndInfected[i][j + 1] = 'I';
 //                 if (j != 0)
 //                     if (region[i][j] == region[i][j - 1]) //up
 //                         diseasedAndInfected[i][j - 1] = '0';
 //                     else diseasedAndInfected[i][j - 1] = 'I';
 //             }   
 //         }
 //     }
 // }
    /** between rounds the Infected cells are made into Diseased cells, Diseased cells are made into dormant cells (reprsented as 0) **/
    private static void infectionsToDesease(){
        char[][] TempDiseasedAndInfected = new char [columns][rows];
        TempDiseasedAndInfected = cloneArray(diseasedAndInfected);
            for(int i = 0; i < columns; i++){
                for(int j = 0; j < rows; j++){
                    if (diseasedAndInfected[i][j] == '1')
                       TempDiseasedAndInfected[i][j] = '0';     //Diseased cells are made into dormant cells (represented as 0) this is so that old Diseased cells are not reevaluated every time
                    else if (diseasedAndInfected[i][j] == '2')  // by no means the best solution as it sometime results in errors but is better than nothing 
                       TempDiseasedAndInfected[i][j] = '1';
                    else if (diseasedAndInfected[i][j] == 'I')
                           TempDiseasedAndInfected[i][j] = '1';
                }
            }
        diseasedAndInfected  = cloneArray(TempDiseasedAndInfected);  
        }
    /** Returns true or false depending on whether the player is in a position also occupied by a disease **/
    private static boolean playerDeathCheck(){
        return (diseasedAndInfected[SpreadAndDie.getPlayerLocaion(0)][SpreadAndDie.getPlayerLocaion(1)] == '0' || diseasedAndInfected[SpreadAndDie.getPlayerLocaion(0)][SpreadAndDie.getPlayerLocaion(1)] == '1'); 
    }   

    /** Builds a combined array called fullBoard by iterating through each element and deciding witch to add. used in  the printBoard function  **/
    private static void buildBoard(){ 

        fullBoard = new char[columns][rows];
        
        for(int i = 0; i < columns; i++){
            for(int j = 0; j < rows; j++){
                if (diseasedAndInfected[i][j] == '_')
                    fullBoard[i][j] = region[i][j];
                    
                else if (diseasedAndInfected[i][j] == '0' || diseasedAndInfected[i][j] == '1') 
                        fullBoard[i][j] = 'D';
                else
                    fullBoard[i][j] = diseasedAndInfected[i][j];
            }
        }
        int pColumns = SpreadAndDie.getPlayerLocaion(0);
        int pRows    = SpreadAndDie.getPlayerLocaion(1);
        fullBoard[pColumns][pRows] = 'P';
    }
    /**calls build boad iterates throuh fullBoard and displays board to user, clears screen if during game, and colors output**/
    private static void printBoard()     {   
        buildBoard();
        SpreadAndDie.clearScreen();
        for (int c=0;c<columns;c++)
        {
            for (int r=0;r<rows;r++)
            {   
                switch (fullBoard[c][r]){
                    case 'P':   System.out.print(SpreadAndDie.getANSI_RED() + "P" + SpreadAndDie.getANSI_RESET());
                                break;
                    case 'D':   System.out.print(SpreadAndDie.getANSI_GREEN() + "D" + SpreadAndDie.getANSI_RESET());
                                break;
                    case 'I':   System.out.print(SpreadAndDie.getANSI_YELLOW() + "I" + SpreadAndDie.getANSI_RESET());
                                break;
                    default:    System.out.print(fullBoard[c][r]);
                }
                System.out.print(SpreadAndDie.getANSI_BLUE() + "|" + SpreadAndDie.getANSI_RESET());
            }
            System.out.println();
        }
        System.out.println("Score: " + SpreadAndDie.score + " Level: " + Ai.getlevel());
    }
}


