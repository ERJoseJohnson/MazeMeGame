package jose.johnson.jose.mazemetrial;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jose Johnson on 2/2/2018.
 */

public class ObstacleManager {
    private ArrayList<ArrayList<Rect>> obstacles = new ArrayList<ArrayList<Rect>>();
    private ArrayList<Rect> AddRect = new ArrayList<Rect>();

    private int obstacleGap;
    private int obstacleHeight;

    private int RectGap = (Constants.SCREEN_WIDTH/16);

    private long startTime;
    private long scoreTime = System.currentTimeMillis();
    private long initTime; //Makes the game go faster with time
    private int score = 0;

    private int currY = -5*Constants.SCREEN_HEIGHT/4;       //Makes the obstacles flow from the top  Change 2
    private int loola= currY;
    Ellers ellrow = new Ellers(currY,obstacleGap);


    public ObstacleManager(int obstacleGap, int obstacleHeight){
        //this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        //this.color = color;
        this.obstacleHeight = obstacleHeight;

        startTime = initTime =  System.currentTimeMillis();

        obstacles = new ArrayList<ArrayList<Rect>>();
        populateObstacles();

    }

    private void populateObstacles(){
        AddRect = new ArrayList<>(ellrow.returnRects());
        obstacles.add(AddRect);  //Change1
        currY -= (obstacleGap + RectGap);   //Change 2

        while (loola<0) {  //Change 2
            ellrow.update(currY,obstacleGap);
            AddRect = new ArrayList<>(ellrow.returnRects());
            obstacles.add(0,AddRect);   //Change1
            currY-= obstacleGap + RectGap;
            loola += obstacleGap + RectGap;   //Change 2
            //System.out.println("This is the current"+ellrow.current);
            System.out.println(obstacles.get(obstacles.size()-1).get(0).top);
        }

    }

    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime); //This is so we can manipulate the speed at which the obstacles move
        startTime = System.currentTimeMillis();
        float speed =(float) Math.sqrt(1+(startTime-initTime)/7500)*(Constants.SCREEN_HEIGHT / 10000.0f);
        for(ArrayList<Rect> list : obstacles){
            for(Rect r : list){
                increementY(r,speed*elapsedTime);       //Change 2
            }
        }
        // THis is a worst version
        if ((obstacles.get(obstacles.size()-1).get(0).top) >= Constants.SCREEN_HEIGHT) {
            ellrow.update(obstacles.get(0).get(0).top - obstacleGap - RectGap,obstacleGap);    // The problem is here!!!
            obstacles.remove(obstacles.size() - 1);
            AddRect = new ArrayList<>(ellrow.returnRects());
            obstacles.add(0,AddRect);   // Change1
        }
        score = scoreInc(score);
    }

    private int scoreInc(int score){
        if (System.currentTimeMillis()-scoreTime>1000){
            scoreTime = System.currentTimeMillis();
            score++;
        }
        return score;
    }

    public void draw(Canvas canvas){
        Paint pa = new Paint();
        pa.setColor(Color.BLACK);
          for(ArrayList<Rect> list : obstacles){
            for(Rect r : list){
                canvas.drawRect(r,pa);
            }
        }
        Paint p = new Paint();
        p.setTextSize(100);
        p.setColor(Color.BLUE);
        canvas.drawText(""+score,50, 100, p);
    }

    public void increementY(Rect r, float Y){
        r.top += Y;
        r.bottom += Y;
    }

    public boolean playerCollide(Player player){
       
    }

}
