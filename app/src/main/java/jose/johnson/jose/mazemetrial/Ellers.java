package jose.johnson.jose.mazemetrial;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jose Johnson on 2/2/2018.
 */

public class Ellers {
    private static final int  UNDETERMINED = -2;
    private static final int  SET_WALL = -1;

    public int[]     current;        //the current row, excluding the outer walls
    public int[]     next;           //the next row, excluding the outer walls

    private int       numSet;         //track set numbers to make sure not to duplicate

    public ArrayList<Rect> AddRect = new ArrayList<Rect>();
    private int RectGap = (Constants.SCREEN_WIDTH/16); // Gap between columns, used for rectangles coordi
    private Rect Vrect;
    private Rect Hrect1;
    private Rect Hrect2;
    private Constants con = new Constants();


    /* constructor */
    public Ellers (int currY, int obstacleGap)
    {
        int act_cols = 8;
        current = new int[act_cols*2-1];
        next    = new int[act_cols*2-1];

        for(int i=0; i<next.length; i++){
            next[i] = UNDETERMINED;
        }

        /* initialize the first row to unique sets */
        for(int i=0; i<current.length; i+=2){
            current[i] = i/2+1;
            if(i != current.length-1)
                current[i+1] = SET_WALL;
        }
        numSet = current[current.length-1];

        // Make the first Current and Next

        joinSets(currY, obstacleGap);
        makeVerticalCuts(currY);

            /* populate the rest of the next row */

        for(int j=0; j<current.length; j+=2){

            if(next[j] == UNDETERMINED)
                next[j] = ++numSet;

            if(j != current.length-1)
                next[j+1] = SET_WALL;
        }
    }

    public void update(int currY, int obstacleGap)
    {
        AddRect.clear();
        /* get the current row from the last iteration */
        for(int i=0; i<current.length; i++){
            current[i] = next[i];
            next[i] = UNDETERMINED;

        }
        joinSets(currY, obstacleGap);
        makeVerticalCuts(currY);


            /* populate the rest of the next row */

        for(int j=0; j<current.length; j+=2){

            if(next[j] == UNDETERMINED)
                next[j] = ++numSet;

            if(j != current.length-1)
                next[j+1] = SET_WALL;
        }
    }

    private void joinSets(int currY, int obstalceGap)
    {
        Random rand = new Random();

        /* Randomly join sets together */
        for(int i=1; i<current.length-1; i+=2){ //checks only at wall locations

            /* make sure they are eligible to be combined:
             *      they have wall between then
             *      they are not part of the same set
             *
             * then get a random boolean to pick if they actually get combine
             */
            if(current[i] == SET_WALL && current[i-1] != current[i+1]
                    && rand.nextBoolean()){


                current[i] = 0; //take away the barrier

                int old  = Math.max(current[i-1],current[i+1]);
                int next = Math.min(current[i-1],current[i+1]);

                /* combine the two sets into 1 (the smallest numbered
                 * set)
                 */
                for(int j=0; j<current.length; j++){

                    if(current[j] == old) {
                        current[j] = next;
                    }
                }
            }
        }
        int be = 0;
        int en;
        int i;

        do {

            /* find the end of this section */
            i = be;
            while (i < current.length - 1 && current[i] == current[i + 2]) {
                i += 2;
            }
            en = i;

            float left = con.pos((float) (en + 1) / 15);
            float right = con.pos((float) (en + 2) / 15);
            Vrect = new Rect((int) (left * Constants.SCREEN_WIDTH), currY, (int) (right * Constants.SCREEN_WIDTH), currY + obstalceGap + RectGap);
            AddRect.add(Vrect);
            be = en + 2;
        } while(en < 16);
    }


    /* Randomly pick vertical paths for each set, making sure there
     * is at least 1 vertical path per set
     */
    private void makeVerticalCuts(int currY)
    {
        Random   rand          = new Random();

        int      begining;     //the begining of the section (inclusive)
        int      end;          //the end of teh section (inclusive)

        boolean madeVertical;  /* tracks if a vertical path has been made
                                * in the section
                                */

        int i;
        begining = 0;
        do{

            /* find the end of this section */
            i=begining;
            while(i<current.length-1 && current[i] == current[i+2]){
                i+=2;
            }
            end = i;

            /* loop trying to cut a vertical path in the section until it
             * is sucessful at least 1 time in the section
             */
            madeVertical = false;
            int prev = begining-1;
            do{
                for(int j=begining; j<=end; j+=2){

                    if(rand.nextBoolean()) {
                        next[j] = current[j];
                        madeVertical = true;
                        float left = con.pos((float) (prev + 1) / 15);
                        float right = con.pos((float) (j) / 15);
                        Hrect1 = new Rect((int) (left * Constants.SCREEN_WIDTH), currY + 100 + RectGap, (int) (right * Constants.SCREEN_WIDTH), currY + 200 + RectGap);
                        AddRect.add(Hrect1);
                        prev = j;
                        }
                    }
            }while(!madeVertical);

            begining = end+2;  //go to the next section in the row

        }while(end != current.length-1);
    }

    public ArrayList<Rect> returnRects(){
        return AddRect;
    }
}
