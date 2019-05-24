import java.awt.*;
import java.util.ArrayList;

class Terrain {
    final ArrayList<Sprite> obstacles = new ArrayList<Sprite>();
    final ArrayList<Integer> obstacleYPos = new ArrayList<Integer>();
    Rectangle area;
    final int topY;
    final int height;

    Terrain(int y, int height) {
        this.topY = y;
        this.height = height;
        area = new Rectangle(0, y, FroggerMain.FRAMEWIDTH, height);
        for (y = topY+4; y < topY+height; y+=30) {
            obstacleYPos.add(y);
        }
    }

    public void draw(Graphics2D g2) {
    }

    void initObs(int num, int type) {
        for (int i = 0; i < num; i++) {
            int ySel = (int) (Math.random()*obstacleYPos.size());
            addObsStart(obstacleYPos.get(ySel), ySel%2, type);
        }
    }

    private Sprite obsOnSpot(int x, int y) {
        for (Sprite spr : obstacles) {
            if (spr.getBoundingRectangle().intersects(new Rectangle(x, y,
                    spr.getBoundingRectangle().height, spr.getBoundingRectangle().width)))
                return spr;
        }
        return null;
    }

    private void addObsStart(int y, int side, int type){
        int x = (int) (Math.random() * FroggerMain.FRAMEWIDTH);
        Sprite otherObs = obsOnSpot(x, y);
        while (otherObs != null) {
            x -= otherObs.getBoundingRectangle().width;
            otherObs = obsOnSpot(x, y);
        }
        if (side == 0) {
                    switch(type) {
                        case 1:
                            obstacles.add(new Car(x, y, Sprite.EAST));
                            break;
                        case 2:
                            obstacles.add(new Log(x, y, Sprite.EAST));
                            break;
                        default:
                            break;
                    }
        } else {
            switch (type) {
                case 1:
                    obstacles.add(new Car(x, y, Sprite.WEST));
                    break;
                case 2:
                    obstacles.add(new Log(x, y, Sprite.WEST));
                    break;
                default:
                    break;
            }
        }
    }

    void addObs(int y, int side, int type){
        int x = (int) -(Math.random() * 151)-50;
        if (side != 0)
            x = (int) (Math.random()*151)+FroggerMain.FRAMEWIDTH+50;
        Sprite otherObs = obsOnSpot(x, y);
        while (otherObs != null) {
            if (side == 0)
                x -= otherObs.getBoundingRectangle().width;
            else
                x += otherObs.getBoundingRectangle().width;
            otherObs = obsOnSpot(x, y);
        }
        if (side == 0) {
            switch(type) {
                case 1:
                    obstacles.add(new Car(x, y, Sprite.EAST));
                    break;
                case 2:
                    obstacles.add(new Log(x, y, Sprite.EAST));
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case 1:
                    obstacles.add(new Car(x, y, Sprite.WEST));
                    break;
                case 2:
                    obstacles.add(new Log(x, y, Sprite.WEST));
                    break;
                default:
                    break;
            }
        }
    }
}
