import java.awt.*;
import java.util.*;
import java.util.List;

public class GameContext {

    int scorpionCounter;
    int spyderCounter;
    int fleaCounter;
    int deathRoutineCount;
    int centipedeCreateCounter;

    Random random;
    //Lista sa bojama, kako se menja nivo, boje se samo pomeraju (poslednja postane prva, prva druga...)
    List<Color> colorsQueue;
    //Matrica gde su objekti i njihove pozicije
    private GameObject[][] gameField;
    private int lives;
    private int score;
    private int level;
    private Point mouseNew;
    private Point mouseOld;
    //Mapa gde su svi objekti trenutno u igrici
    private Map<Class<? extends GameObject>, List<GameObject>> gameObjectsMap;
    private boolean clicked;
    private boolean gameOver;
    //Da ako se vise stonoga pojavi, da se ne kreiraju sve odmah vec sa nekim cekanjem
    private List<Centipede> centipedesToCreateQueue;
    //Resetovanje na 2000, za novi zivot
    private int scoreLife;
    public GameContext() {
        gameField = new GameObject[Constants.COLS][Constants.ROWS];
        gameObjectsMap = new HashMap<>();
        gameObjectsMap.put(Bullet.class, new ArrayList<>());
        gameObjectsMap.put(Mushroom.class, new ArrayList<>());
        gameObjectsMap.put(Scorpion.class, new ArrayList<>());
        gameObjectsMap.put(Centipede.class, new ArrayList<>());
        gameObjectsMap.put(Player.class, new ArrayList<>());
        gameObjectsMap.put(Spider.class, new ArrayList<>());
        gameObjectsMap.put(Flea.class, new ArrayList<>());
        random = new Random();
        colorsQueue = new ArrayList<>();
        colorsQueue.add(Color.BLUE);
        colorsQueue.add(Color.YELLOW);
        colorsQueue.add(Color.RED);
        colorsQueue.add(Color.MAGENTA);
        colorsQueue.add(Color.GREEN);
        colorsQueue.add(Color.CYAN);
        colorsQueue.add(Color.ORANGE);
        centipedesToCreateQueue = new ArrayList<>();
        //Prednjacenje u odnosu na pauka
        fleaCounter = 300;
        scorpionCounter = 100;
        scoreLife = 0;
    }

    public int getScoreLife() {
        return scoreLife;
    }

    public void setScoreLife(int scoreLife) {
        this.scoreLife = scoreLife;
    }

    public List<Centipede> getCentipedesToCreateQueue() {
        return centipedesToCreateQueue;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public List<Color> getColorsQueue() {
        return colorsQueue;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Point getMouseNew() {
        return mouseNew;
    }

    public void setMouseNew(Point mouseNew) {
        this.mouseNew = mouseNew;
    }

    public Point getMouseOld() {
        return mouseOld;
    }

    public void setMouseOld(Point mouseOld) {
        this.mouseOld = mouseOld;
    }

    public Map<Class<? extends GameObject>, List<GameObject>> getGameObjectsMap() {
        return gameObjectsMap;
    }

    GameObject[][] getGameField() {
        return gameField;
    }

    public void setGameField(GameObject[][] gameField) {
        this.gameField = gameField;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
