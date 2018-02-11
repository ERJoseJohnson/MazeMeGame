package jose.johnson.jose.mazemetrial;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Jose Johnson on 2/2/2018.
 */

public class Obstacle implements GameObject {
    private Rect rectangle;
    private int color;
    private Rect rectangle2;

    public Rect getRectangle(){
        return rectangle;
    }

    public void incrementY (float Y){
        rectangle.top += Y;
        rectangle.bottom += Y;
        rectangle2.top += Y;
        rectangle2.bottom += Y;
    }

    public Obstacle(int rectheight, int color, int startX, int startY, int playerGap){
        this.color = color;
        rectangle = new Rect(0, startY, startX, startY+rectheight);
        rectangle2 = new Rect(startX+playerGap, startY, Constants.SCREEN_WIDTH, startY+rectheight);
    }
    public boolean playerCollide(Player player){
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {

    }
}
