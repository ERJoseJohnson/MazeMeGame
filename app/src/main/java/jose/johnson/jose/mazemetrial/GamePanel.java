package jose.johnson.jose.mazemetrial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.concurrent.CancellationException;

/**
 * Created by Jose Johnson on 2/2/2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private Player player;
    private Point playerPoint;

    private ObstacleManager obstacleManager;
    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    public GamePanel(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

        player = new Player(new Rect(100, 100, 200, 200), Color.BLUE);
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 100);
        setFocusable(true);
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager( 200, 100);
        movingPlayer = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while (true){
            try {
                thread.setRunning(false);
                thread.join();
            }catch (Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Make sure player is MOVING and NOT SKIPPING from place to place
                //Check to see if player actually pressed
                if(!gameOver  && player.getRectangle().contains((int)event.getX(),(int)event.getY()))
                    movingPlayer =  true;
                if (gameOver  && (System.currentTimeMillis()-gameOverTime >= 10)){
                    reset();
                    gameOver = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(),(int)event.getY());
                break;
            //Ensures no skipping occurs
            case MotionEvent.ACTION_UP:
                movingPlayer=false;
                break;

        }
        return true;
    }

    public void update(){
        if (!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();
        }
        if (obstacleManager.playerCollide(player)){
            //gameOver = true;
            //gameOverTime = System.currentTimeMillis();
        }
    }

    @Override
    public void draw (Canvas canvas){

        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        obstacleManager.draw(canvas);
        /*if (gameOver){
            Paint textP = new Paint();
            textP.setColor(Color.RED);
            textP.setTextSize(100);
            float x = Constants.SCREEN_WIDTH/4;
            float y = Constants.SCREEN_HEIGHT/2;
            canvas.drawText("Game Over!", x, y, textP);
        }
        */
    }

}
