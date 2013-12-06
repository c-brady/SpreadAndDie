import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/** @author Cormac Brady **/
public class SpreadAndDie extends DisplayBoard{

    private static String ANSI_RESET = "\u001B[0m";
    private static String ANSI_RED = "\u001B[31m";
    private static String ANSI_GREEN = "\u001B[32m";
    private static String ANSI_YELLOW = "\u001B[33m";
    private static String ANSI_BLUE = "\u001B[34m";
    private static String ANSI_PURPLE = "\u001B[35m";

    protected static int score = 0 ;
    protected static int regionNumber = 4;

    private static int DiseaseLocation[] = {0,0};
    private static int playerLocaion[];
    private static int totalScore = 0 ;
    private static String p = "prompt: ";
    private static boolean playerSetLocation = false;  
    private static boolean playerSetDesiseLocation = false;

    public static String getANSI_RESET(){
        return ANSI_RESET;
    }
    public static boolean getplayerSetDesiseLocation(){
        return playerSetDesiseLocation;
    } 
    public static String getANSI_RED(){ 
        return ANSI_RED;
    }
    public static String getANSI_GREEN(){
        return ANSI_GREEN; 
    }
    public static String getANSI_YELLOW(){
        return ANSI_YELLOW;
    }
    public static String getANSI_BLUE(){
        return ANSI_BLUE;
    }
    public static String getANSI_PURPLE(){
        return ANSI_PURPLE;
    }
    public static int getDiseaseLocation(int i){
        return DiseaseLocation[i];
    }
    public static void setDiseaseLocation(int c, int r){
        DiseaseLocation = new int[]{c,r};
    }
    public static int getPlayerLocaion(int i){
            return playerLocaion[i];
    }
    public static void setPlayerLocation(int c, int r){
        playerLocaion = new int[]{c,r};
    }
    public static void setPlayerLocation(int[] moves){
        playerLocaion = moves;
    }
    public static boolean getPlayerSetLocation(){
        return playerSetLocation;
    }

     /** waits the inputed number of milliseconds **/
    private static void hangOn(int i){ //waits the desired amount of milliseconds
        try {
            Thread.sleep(i + 'L'); 
        }
        catch (Exception e) {
            System.out.println("nope!");
        }
    }
    /**validates user input for regions, returns false if conditions are met and sets the corresponding attributes**/
    private static boolean setregions(int i){
        if(i > 1 && i < 5){
            regionNumber = i;
            GameMenu.setMessage(i + " regions to be generated for the board.");
            return false;
        }
        else if (i == 20) 
            return true;
        else{
            System.out.println("Invalid entry!"); 
            System.out.println("Only numbers between 2-4 are allowed!");
            System.out.println(p);
            return true;
        }
    }
    private static String OS = null;
    /**Finds what OS the program is running on.
    http://stackoverflow.com/questions/20158809/how-can-i-tell-in-java-if-the-program-is-running-on-a-windows-or-linux-machine**/
    private static String getOsName(){
            if(OS == null){ OS = System.getProperty("os.name"); }
            return OS;
    }
    /**Tests to see if the program is windows.**/
    private static boolean isWindows(){
            return getOsName().startsWith("Windows");
    }
    /**removes ANSI escape codes from code (as CMD prompt does not natively support this)**/
    private static void windowsMode(){
        if ( isWindows() ){
            ANSI_RESET = "";
            ANSI_RED = "";
            ANSI_GREEN = "";
            ANSI_YELLOW = "";
            ANSI_BLUE = "";
            ANSI_PURPLE = "";
        }
    }
    /** clears screen depending on current platform**/
    public static void clearScreen(){
        if (isWindows() == false){
            System.out.println("\f");
        }
        else{
            for (int y = 0; y < 50; y++)
            System.out.println("\n");
        }
    }
    /** Adds current score for that round to the total round, then rests current to 0**/
    private static void totalScore(){
        totalScore = totalScore + score;
        score = 0;
    }
    /** validates user input, returns faces if conditioners are met , notifies user and sets var**/
    private static boolean playerSetLocation(int c, int r){
        if (r < DisplayBoard.rows && r > -1 && c < DisplayBoard.columns && c > -1){
            playerLocaion = new int[]{c,r};
            GameMenu.setMessage("Player spawn set to " + c +'x' + r);
            playerSetLocation = true;
            return false;
        }
        else{
            System.out.println("Spawn can not be created in " + c + 'x' + r + " as game board is only " + DisplayBoard.rows + "x" + DisplayBoard.columns + ".");
            return true;
        }
    }
     /** notifies user and sets var to false**/
    private static void undoPlayerLocation(){
        GameMenu.setMessage("Player spawn will now be randomly chosen");
        playerSetLocation = false;
    } 
    /** loops until a valid cmd is made **/
    private static void playerMenu(){
        clearScreen();
        boolean contunue = true;
        do{
            switch (GameMenu.playerMenu()){

                case 0:     while(playerSetLocation(GameMenu.listenInt("Enter X axis coranate (starting at 0)--:"),GameMenu.listenInt("Enter Y axis coranate                --:")));
                            contunue  = false;
                            break;
                case 1:     undoPlayerLocation();
                            contunue = false;
                            break;
                case 20:    contunue = false;
                            break;
                default:    System.out.println("Command not found, please try again! ");
                            break;
            }
        }
        while (contunue);
    }
    /** passes input to setDesiseLocation (and inverts the return)**/
    private static boolean DiseaseSetLocation(int c, int r){        //only inverts the Bool...sry
        DiseaseLocation[0] = c; DiseaseLocation[1] = r;                     
        return (! setDesiseLocation(c,r) );
    }
    /** sets attribute as informs user **/
    private static void undoDesiseLocation(){
        GameMenu.setMessage("Desise spawn will now be set by AI acording to level");
        playerSetDesiseLocation = false;
    }
    /** runs commands based on user input**/
    private static void DiseaseMenu(){
        clearScreen();
        boolean contunue = true;
        do{
            switch (GameMenu.DiseaseMenu()){

                case 0:     while(DiseaseSetLocation(GameMenu.listenInt("Enter X axis coranate (starting at 0)--:"),GameMenu.listenInt("Enter Y axis coranate                --:")));
                            playerSetDesiseLocation = true;
                            contunue  = false;
                            break;
                case 1:     undoDesiseLocation();
                            contunue = false;
                            break;
                case 20:    contunue = false;
                            break;
                default:    System.out.println("Command not found, please try again! ");
                            break;
            }
        }
        while (contunue);
    }
    /**returnsTrue if score is more than 20**/
    private static boolean gameRuns(){
        return (score > 20);
    }
    /** main method, runs game and contains main menu**/
    public static void main(String[] arg){ 
        DisplayBoard b = new DisplayBoard();
        Scanner reader = new Scanner(System.in);
        windowsMode();
        DisplayBoard.DisplayBoard();
        boolean I = true;
        b.populateArrays();
       do {
            clearScreen();
            System.out.println(ANSI_YELLOW + "The total Score is " + totalScore + ANSI_RESET);
            switch (GameMenu.menu()){
                case '0': 
                        b.populateArrays();
                        Ai.setDesieseLocation();
                        b.percistantDeseasedSpawn();
                        // char y = 'n';
                        do{
                            do{
                                hangOn(500);
                                score++;
                            }while(b.runTurn());
                            if (Ai.getlevel() < 4)
                                Ai.addLevel(1);
                            System.out.println(ANSI_PURPLE + "The round has has ended after " + score + " rounds." + ANSI_RESET);
                            totalScore();
                            System.out.println(ANSI_YELLOW + "The totalScore score is " + totalScore + " rounds." + ANSI_RESET);
                            hangOn(3000);
                        }while (gameRuns());
                        break;
                case '1': while (setregions(GameMenu.listenInt("Enter the number of regions to set the game board to--: ")));
                        break;
                case '2': while (Ai.setLevel(GameMenu.listenInt("Enter the number of the level you wish to skip to--: ")));
                        break;
                case '3': while (b.testRegionSize(GameMenu.listenInt("To change the game board size enter fist the number of rows--: "),GameMenu.listenInt("                                  Now the number or columns: "))){
                               System.out.println("Invalid input, please try again.");
                                }
                        break;
                case '4':       playerMenu();
                        break;
                case '5':       DiseaseMenu();        
                        break;
                case 'q': I = false;
                        break;
                default:
                        System.out.println("Command not found, please try again! ");
                        break;
            }
        }
        while (I);
        System.out.println("Quiting...");
    }   
}
