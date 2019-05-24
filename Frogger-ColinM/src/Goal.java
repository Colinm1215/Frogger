import java.awt.*;

public class Goal extends Terrain {
    private boolean reached = false;
    private final Sprite frogIcon;
    Goal(int x) {
        super(0, 40);
        area = new Rectangle(x, 0, 40, 40);
        frogIcon = new Sprite(x+10, 10, Sprite.NORTH, 0);
        frogIcon.setPic("frog1.png", Sprite.NORTH); //overrides the default "blank.png"
        frogIcon.setSpeed(0);
        frogIcon.setDir(Sprite.SOUTH);
    }

    @Override
    public void draw(Graphics2D g2) { // 57 for lanes - car spot = y + (13*n)
        frogIcon.setDir(Sprite.SOUTH);
        g2.setColor(Color.black);
        g2.fill(area);
        if (isReached())
            frogIcon.draw(g2);
    }

    private boolean isReached() {
        return reached;
    }

    void setReached() {
        this.reached = true;
    }

    boolean intersect(Sprite sprite) {
        return area.intersects(sprite.getBoundingRectangle());
    }
}
