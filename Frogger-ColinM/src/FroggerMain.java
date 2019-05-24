import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

// Colin Mettler

class FroggerMain extends JPanel {

    //instance fields for the general environment
    static final int FRAMEWIDTH = 1000, FRAMEHEIGHT = 720;
    private Timer timer;
    private final boolean[] keys;

    //instance fields for frogger.
    private static Sprite frog;
    private static final ArrayList<Terrain> terrains = new ArrayList<Terrain>();
    static double levelMod = 0.5;
    private static int livesLeft = 5;
    private static final JFrame window = new JFrame(String.format("Frogger! Lives Left : %d, Points : 0", livesLeft));
    private static double bonusPointMod = 1;
    private int goalsReached = 0;
    private static int totalGoals = 1;
    private static final ArrayList<Goal> goals = new ArrayList<Goal>();
    private static double pointsGainedPrev = 0;



    private FroggerMain() {
        keys = new boolean[512]; //should be enough to hold any key code.
        setKeyListener();

        frog = new Frog();

        timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                /*
                ALL TIME DRIVEN CHANGES ARE CODED HERE!
                MOVING THINGS, HIT DETECTION, GAME STATE CHANGES
                 */
                //move the frog
                if (goalsReached == totalGoals) {
                    timer.stop();
                    JOptionPane.showMessageDialog(window,
                            String.format("You got all goals with a total score of %s",
                                    new DecimalFormat("#.##").format((((int)((levelMod-0.5)/0.25))*5)*bonusPointMod + pointsGainedPrev)));
                    Object[] options = {"Continue",
                            "Exit"};
                    int n = JOptionPane.showOptionDialog(window,
                            "Would you like to continue?",
                            "Continue?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,     //do not use a custom Icon
                            options,  //the titles of buttons
                            options[0]); //default button title
                    if (n == JOptionPane.YES_OPTION) {
                        newGoals();
                        nextLevel();
                    } else {
                        System.exit(0);
                    }
                }
                if (livesLeft <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(window,"You are out of Lives! You Lost!");
                    int n = JOptionPane.showConfirmDialog(
                            window,
                            "Would you like to try again?",
                            "Restart?",
                            JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        setup();
                    } else {
                        System.exit(0);
                    }
                }
                moveTheFrog();

                //noinspection ForLoopReplaceableByForEach
                for (int i = 0; i < terrains.size(); i++) {
                    Terrain terrain = terrains.get(i);
                    if (terrain instanceof Goal)
                        if (((Goal)terrain).intersect(frog)) {
                            goalReached((Goal)terrain);
                        }

                    if (terrain instanceof River)
                        if (((River) terrain).onWater(frog))
                            frogDead();

                    for (int j = 0; j < terrain.obstacles.size(); j++) {
                        Sprite spr = terrain.obstacles.get(j);
                        spr.update();
                        if (spr.getLoc().x > FroggerMain.FRAMEWIDTH+200|| spr.getLoc().x < -200) {
                            terrain.obstacles.remove(spr);
                            int ySel = terrain.obstacleYPos.indexOf(spr.getLoc().y);
                            terrain.addObs(spr.getLoc().y, ySel % 2, spr.getType());
                        }
                    }

                    for (Sprite spr : terrain.obstacles) {
                        if (spr instanceof Car) {
                            if (frog.intersects(spr))
                                frogDead();
                        }
                        if (spr instanceof Log) {
                            if (frog.intersects(spr)) {
                                if (spr.facingEast()) {
                                    frog.setLoc(new Point(frog.getLoc().x + spr.getSpeed(), frog.getLoc().y));
                                } else {
                                    frog.setLoc(new Point(frog.getLoc().x - spr.getSpeed(), frog.getLoc().y));
                                }
                                break;
                            }
                        }
                    }
                }

                repaint(); //always the last line.  after updating, refresh the graphics.
            }
        });
        setup();
    }

    private void setup(){
        Object[] possibilities = {"Easy", "Medium", "Hard"};
        String s = (String)JOptionPane.showInputDialog(
                window,
                "What difficulty would you like to play at?",
                "Difficulty Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                "Hard");

        if ((s != null) && (s.length() > 0)) {
            totalGoals = 1;
            if (s.equals("Easy")) {
                totalGoals = 2;
                bonusPointMod = 1;
            }
            else if (s.equals("Medium")) {
                totalGoals = 4;
                bonusPointMod = 2;
            }
            else if (s.equals("Hard")) {
                totalGoals = 6;
                bonusPointMod = 3;
            }
            else
                System.exit(0);
            frogDead();
            pointsGainedPrev = 0;
            levelMod = 0.5;
            livesLeft = 5;
            goalsReached = 0;
            newGoals();
            terrains.clear();
            newLevel(false);
            timer.start();
        } else {
            System.exit(0);
        }
    }

    private void newGoals(){
        goals.clear();
        goalsReached = 0;
        int goalsLeft = totalGoals;
        int areaDiv = FRAMEWIDTH/totalGoals;
        int x = areaDiv/2 - 40;
        while (goalsLeft > 0){
            goals.add(new Goal(x));
            x += areaDiv;
            goalsLeft--;
        }
        timer.start();
    }

    private static void newLevel(boolean isFinal) {
        terrains.clear();
        terrains.add(new Grass(0));
        terrains.add(new Grass(FRAMEHEIGHT-60));
        int rowsLeft = (FRAMEHEIGHT-(60*2))/60;
        int prevType = -1;
        while (rowsLeft>0){
            int type = (int) (Math.random()*2);
            int terrainSize = (int) (Math.random()*(rowsLeft)) + 1;
            if (prevType != type) {
                switch (type) {
                    case 0:
                        terrains.add(new Road(FRAMEHEIGHT - (60 * (rowsLeft + 1)), terrainSize * 60, (int) ((terrainSize * 10) * levelMod)));
                        break;
                    case 1:
                        terrains.add(new River(FRAMEHEIGHT - (60 * (rowsLeft + 1)), terrainSize * 60,  (int) ((terrainSize * 10) / levelMod)));
                        break;
                    default:
                        break;
                }
                prevType = type;
                rowsLeft -= terrainSize;
            }
        }
        if (isFinal) {
            terrains.addAll(goals);
        }
    }

    private void goalReached(Goal goal) {
        goalsReached++;
        goal.setReached();
        nextLevel();
        pointsGainedPrev += 5;
    }

    static void nextLevel(){
        pointsGainedPrev += (((levelMod-0.5)/0.25)*5)*bonusPointMod;
        frog.setLoc(new Point(500, FroggerMain.FRAMEHEIGHT-26));
        if (levelMod == 2) {
            levelMod = 0.5;
            newLevel(false);
        } else {
            levelMod += 0.25;
            if (levelMod == 2)
                newLevel(true);
            else
                newLevel(false);
        }
    }

    //Our paint method.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Terrain terrain : terrains) {
            terrain.draw(g2);
        }
        frog.draw(g2);
        window.setTitle(String.format("Frogger! Lives Left : %d, Points : %s", livesLeft,
                new DecimalFormat("#.##").format((((int)((levelMod-0.5)/0.25))*5)*bonusPointMod + pointsGainedPrev)));
    }

    static void frogDead() {
        frog.setLoc(new Point(500, FroggerMain.FRAMEHEIGHT-26));
        livesLeft--;
    }

    private void moveTheFrog() {
        if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
            frog.setDir(Sprite.NORTH);
            frog.update();
            keys[KeyEvent.VK_W] = false;
            keys[KeyEvent.VK_UP] = false;
        }
        if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
            frog.setDir(Sprite.WEST);
            frog.update();
            keys[KeyEvent.VK_A] = false;
            keys[KeyEvent.VK_LEFT] = false;
        }
        if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
            frog.setDir(Sprite.SOUTH);
            frog.update();
            keys[KeyEvent.VK_S] = false;
            keys[KeyEvent.VK_DOWN] = false;
        }
        if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
            frog.setDir(Sprite.EAST);
            frog.update();
            keys[KeyEvent.VK_D] = false;
            keys[KeyEvent.VK_RIGHT] = false;
        }
    }

    /*
      You probably don't need to modify this keyListener code.
       */
    private void setKeyListener() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {/*intentionally left blank*/ }

            //when a key is pressed, its boolean is switch to true.
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                keys[keyEvent.getKeyCode()] = true;
                if (keyEvent.getKeyCode() == KeyEvent.VK_R) {
                    setup();
                }
            }

            //when a key is released, its boolean is switched to false.
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                keys[keyEvent.getKeyCode()] = false;
            }
        });
    }

    //sets ups the panel and frame.  Probably not much to modify here.
    public static void main(String[] args) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(0, 0, FRAMEWIDTH, FRAMEHEIGHT + 22); //(x, y, w, h) 22 due to title bar.

        FroggerMain panel = new FroggerMain();
        panel.setSize(FRAMEWIDTH, FRAMEHEIGHT);

        panel.setFocusable(true);
        panel.grabFocus();

        window.add(panel);
        window.setVisible(true);
        window.setResizable(false);
    }
}