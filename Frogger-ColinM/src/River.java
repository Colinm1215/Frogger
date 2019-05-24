import java.awt.*;

public class River extends Terrain {

    River(int y, int height, int numLogs) {
        super(y, height);
        initObs(numLogs, 2);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fill(area);
        for (Sprite spr : obstacles) {
            spr.draw(g2);
        }
    }

    boolean onWater(Sprite frog) {
        if (area.intersects(frog.getBoundingRectangle())) {
            for (Sprite log : obstacles) {
                if (log instanceof Log) {
                    if (log.intersects(frog))
                        return false;
                }
            }
            return true;
        }
        return false;
    }
}
