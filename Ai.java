import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Ai{

    private static int level = 1;

    private static int[][] playerMovmentPositions = new int[][] {{0,0},{0,1},{0,-1},{1,0},{-1,0}};

    public static int[][] getPlayerMovmentPositions(){
        return playerMovmentPositions;
    }
    public static int getlevel(){
        return level;
    }
    /** takes an int and adds it to the curent level **/
    public static void addLevel(int i){
        level = level  + i;
    }
    /** valadates user input, returns falces if condishions are met **/
    public static boolean setLevel(int i){
        if (i > 0 && i < 4){
            level = i;
            GameMenu.setMessage("Level changed to: " + i);
            return false;
        }
        else{
             System.out.println("Level not changed to " + i + " as it is invalid, please chouse between 1-3");
             return true;
        } 
    }
    /** places the disease in a random valid loaction by using setDesiseLocation() condishions **/
    private static void diseasePlacement(){

        boolean set = false;
        System.out.println("Attempting to place DISEASE in a random and valid location.");
        Random a1 = new Random();
            do{
                if (DisplayBoard.setDesiseLocation(
                                                a1.nextInt(DisplayBoard.columns),
                                                a1.nextInt(DisplayBoard.rows)
                                                ) == true)
                    set = true;
            }
            while (set == false);
    }
    /** valadates user input, returns falces if condishions are met **/
    private static void diseasePlacement(int cMax, int cMin, int rMax,int rMin){

        boolean set = false;
        System.out.println("Attempting to place DISEASE in a far away location.");
            while(! DisplayBoard.setDesiseLocation(cMin + (int)(Math.random() * ((cMax - cMin) + 1)),
                                                   rMin + (int)(Math.random() * ((rMax - rMin) + 1))
                                                   ));
    }
    /* Tells diseasePlacement to randomly select a disease spawn that is within a section
     * (the board being divided into 4 rsections) that is diagonally opposite from the current player spawn. */
    private static void diseasePlacementIntelligent(){
    int cMax;
    int cMin;
    int rMax;
    int rMin;
    if (SpreadAndDie.getPlayerLocaion(0) == (DisplayBoard.columns / 2))
    {
        cMin = ((DisplayBoard.columns / 2) -1);
        cMax = (DisplayBoard.columns -1);
    }
    else
    {
        cMin = 0;
        cMax = (DisplayBoard.columns -1);
    }
    if (SpreadAndDie.getPlayerLocaion(1) == (DisplayBoard.rows / 2))
    {
        rMin = (DisplayBoard.rows / 2);
        rMax = (DisplayBoard.rows -1);
    }
    else
    {
        rMin = 0;
        rMax = ((DisplayBoard.rows / 2) -1);
    }
    // System.out.println(" cMax " + cMax + " cMin " + cMin + " rMax " +rMax + " rMin " + rMin); //debug code
    diseasePlacement(  (cMax),  (cMin),  (rMax),  (rMin));
}
    private static List<int[]> l = new ArrayList<int[]>();

    // public static void movePlayer(){ 
      // moves = ran.nextInt(l.size());
        // SpreadAndDie.getPlayerLocaion() = l.get(moves);
    // }

    /** tesst to see if  an int is in a cartan range**/                               
    private static boolean isBetween(int x, int lower, int upper){
        return lower <= x && x <= upper;
    }

    /**Clears the current list of valid moves and finds places to move to that are within the board (specific to level criteria).**/
    public static void movePlayer(){ 
        l.clear();
        for(int pos[] : playerMovmentPositions){
            int c = SpreadAndDie.getPlayerLocaion(0) + pos[0];            
            int r = SpreadAndDie.getPlayerLocaion(1) + pos[1];
            if(c >= 0 && c < DisplayBoard.columns && r >= 0 && r < DisplayBoard.rows){
                if (level == 1)
                    l.add(new int[] {c, r});
                else if (isBetween(level, 2, 3)) {
                    if (DisplayBoard.diseasedAndInfected[c][r] != '1' &&
                        DisplayBoard.diseasedAndInfected[c][r] != '0' &&
                        DisplayBoard.diseasedAndInfected[c][r] != 'I')
                            {
                                l.add(new int[] {c, r});
                            }
                }
                else{
                    System.out.println("error ai level not defined");
                }
            }
        }
        // System.out.println("i have " + l.size() + " options"); //debug code
        if (l.isEmpty() == false){         //stops the folowing code from trying to update the current location to nothing (crash!).
            Random ran = new Random();
            int moves = ran.nextInt(l.size());
            SpreadAndDie.setPlayerLocation(l.get(moves));
             // = l.get(moves)
        }
    }
    /** calls the correct method depending on what level is currenly running **/
    public static void setDesieseLocation(){
        if (! SpreadAndDie.getplayerSetDesiseLocation()){
            switch (level) {
                case 1:  diseasePlacement();
                        break;
                case 2: //runs to case 3

                case 3:  diseasePlacementIntelligent();
                        break;
                default: System.out.println("Invalid ai level");
                        break;
            }
        }
    }

} //class end