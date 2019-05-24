import java.awt.*;

public class Grass extends Terrain {

    Grass(int y) {
        super(y, 60);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.fill(area);
    }
}
