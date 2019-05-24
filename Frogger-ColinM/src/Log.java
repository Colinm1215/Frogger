class Log extends Sprite {

    Log(int x, int y, int direction) {
        super(x, y, direction,2);
        setRandLogType();
        setSpeed(5); //GUESS?!
    }

    private void setRandLogType() {
        switch((int) (Math.random()*3)) {
            case 0:
                setPic("logLarge.png", getDir());
                break;
            case 1:
                setPic("logMedium.png", getDir());
                break;
            case 2:
                setPic("logShort.png", getDir());
                break;
            default:
                break;
        }
    }
}
