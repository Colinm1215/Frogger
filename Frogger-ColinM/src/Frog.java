import java.awt.*;

/**
 * Created by michael_hopps on 2/13/17.
 */
public class Frog extends Sprite {

    Frog(){
        super(500, FroggerMain.FRAMEHEIGHT-26, NORTH, 0);
        setPic("frog1.png", NORTH); //overrides the default "blank.png"
        setSpeed(30);
    }

    @Override
    public void update() {
        super.update();   //moves the Frog in the dir it's facing
        Point location = getLoc();
        if (location.y < 0)
            if (FroggerMain.levelMod == 2)
                setLoc(new Point(location.x, 0));
            else
                FroggerMain.nextLevel();
        if (location.y > FroggerMain.FRAMEHEIGHT)
            setLoc(new Point(location.x, FroggerMain.FRAMEHEIGHT-26));
        if (location.x > FroggerMain.FRAMEWIDTH || location.x < 0)
            FroggerMain.frogDead();
    }
}
