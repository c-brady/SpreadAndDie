import java.util.Scanner; //omit when not using test class
public class GameMenu{

            private static String mainMenu[] = {"Run Game",
                                               "Change: number of regions",
                                               "Change: starting level",
                                               "Change: game board size",
                                               "Change: the way that the PLAYER starting position is chosen",
                                               "Change: the way that the DISEASE starting position is chosen"};
    private static String PlayerLocation[]   = {"The user sets the starting position of the PLAYER",
                                               "The AI choses the starting position based on the current level"};
    private static String DiseaseLocation[]  = {"The user sets the starting position of the Disease",
                                                "The Ai choses the starting position based on the current level"};

    private static String message = "";

    /** Finds the users name and prints out a greeting. **/
    private static void greet(){
        System.out.println("Hello " + (System.getProperty("user.name")) + "!");
        System.out.println("This is a self playing game made by Cormac Brady for CS12130.");
    }
    /** Prints the imputed array as a menu for the user to see **/
    private static void printMenu(String menu[]){
        //SpreadAndDie.clearScreen();
        System.out.println(SpreadAndDie.getANSI_BLUE() + "--------------------" + SpreadAndDie.getANSI_RESET());
        for (int i = 0; (menu.length - 1) >= i; i++)
            System.out.println(SpreadAndDie.getANSI_RED() + i  + SpreadAndDie.getANSI_RESET() + " "  + menu[i]);
        System.out.println(SpreadAndDie.getANSI_BLUE() + "--------------------"+ SpreadAndDie.getANSI_RESET());
        System.out.println("Enter the corresponding number for the corresponding action.");
    }
    /** Returns a char that is selected by keyboard input from the user 
            returns 'q' instead of throwing  an exception **/
    public static char listen(String message){
        while(true){
            Scanner l = new Scanner(System.in);
            System.out.print(message);
            if (l.hasNext()){
                return l.next().charAt(0);
            }
            else return 'q';
        }
    }
     /** Returns a int that is selected by keyboard input from the user 
            returns 20 instead of throwing an exception **/
    public static int listenInt(String message){
        while(true){
                Scanner l = new Scanner(System.in);
                System.out.print(message);
                if (l.hasNextInt()){
                    return l.nextInt();
                }
                else return 20;
        }
    }
     /** allows functions to place a massage for the next time the menu is refreshed **/
    public static void setMessage(String m){
        message = m;
    }
     /** prints any messages that were added using setMessage() and then clears them**/
    public static void printMessage(){
        System.out.println(message);
        message = " ";
    }
     /** prints the main menu, executes any functions needed that are in the current class then returns validated user input **/
    public static char menu(){
        char n;
        greet();
        printMenu(mainMenu);
        printMessage();
            do{
                n = listen("--:");
                if (n == 0)
                    greet();            
            }
            while (n != 0 && n != 1 && n != 2 && n != 3  && n == 4 && n == 5);
            return n;
    }
    /** prints the player sub-menu then returns validated user input **/
    public static int playerMenu(){
        int n;
        printMenu(PlayerLocation);
        do{ 
            n = listenInt("--:");
        }while (n != 0 && n != 1);
        return n;
    }
    /** prints the Disease sub-menu then returns validated user input **/
    public static int DiseaseMenu(){
        int n;
        printMenu(DiseaseLocation);
        do{
            n = listenInt("--:");
        }while (n != 0 && n != 1);
        return n;
    }

}