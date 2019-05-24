/**
 * Created by michael_hopps on 2/5/18.
 */
class Car extends Sprite {

    Car(int x, int y, int direction) {
        super(x, y, NORTH, 1);
        setRandCarType();
        setDir(direction);
        setSpeed(5);
    }

    private void setRandCarType() {
        switch((int) (Math.random()*4)) {
            case 0:
                setPic("car1.png", getDir());
                break;
            case 1:
                setPic("car2.png", getDir());
                break;
            case 2:
                setPic("car3.png", getDir());
                break;
            case 3:
                setPic("car4.png", getDir());
                break;
            default:
                break;
        }
    }
}
