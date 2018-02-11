package jose.johnson.jose.mazemetrial;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Jose Johnson on 2/2/2018.
 */

public class Player implements GameObject {
    private Rect rectangle;
    private int color;

    public Rect getRectangle(){
        return rectangle;
    }

    public Player(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point){
        rectangle.set(point.x - 30, point.y - 30, point.x + 30, point.y + 30);
    }
}
