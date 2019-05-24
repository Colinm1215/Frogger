import java.awt.*;

public class Road extends Terrain {
    Road(int y, int height, int numCars) {
        super(y, height);
        initObs(numCars, 1);
    }

    @Override
    public void draw(Graphics2D g2) { // 57 for lanes - car spot = y + (13*n)
        g2.setColor(Color.darkGray);
        g2.fill(area);
        g2.setColor(Color.yellow);
        for (int y = topY+30; y < topY+height; y+=30) {
            g2.fillRect(0, y,FroggerMain.FRAMEWIDTH,4);
        }
        for (Sprite spr : obstacles) {
            spr.draw(g2);
        }
    }
}
