import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;

public class GameField extends JPanel {

    private GameContext ctx;
    private Timer timer;

    GameField() {
        timer = new Timer(50, listener -> mainLoop());
        //Panel da bude u fokusu
        setFocusable(true);
        requestFocus();
        //event handleri za mis i tastaturu
        addMouseListener();
        addKeyListeners();
    }

    private void addKeyListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    //Pocetak igre - SPACE
                    case KeyEvent.VK_SPACE:
                        if (ctx == null || ctx.isGameOver()) {
                            initializeGame();
                        }
                        break;
                    //Pauza - P
                    case KeyEvent.VK_P:
                        if (timer.isRunning()) {
                            timer.stop();
                        } else {
                            timer.start();
                        }
                }

            }
        });
    }


    private void addMouseListener() {
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (ctx != null)
                    ctx.setClicked(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (ctx != null)
                    ctx.setClicked(false);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (ctx != null)
                    ctx.setMouseNew(e.getPoint());
            }

        });
    }

    private void initializeGame() {
        ctx = new GameContext();
        ctx.setLevel(1);
        ctx.setLives(5);
        ctx.setScore(0);
        ctx.setMouseOld(new Point());
        ctx.setMouseNew(new Point());
        for (int i = 0; i < Constants.COLS - 1; i++) {
            for (int j = 0; j < Constants.ROWS; j++) {
                //Da bude oko 7.5% pecuraka na polju
                if (ctx.random.nextInt(Constants.ROWS * Constants.COLS) < 0.075 * (Constants.ROWS * Constants.COLS)){
                    ctx.getGameField()[i][j] = new Mushroom(i, j, ctx);
                    ctx.getGameObjectsMap().get(Mushroom.class).add(ctx.getGameField()[i][j]);
                }
            }
        }
        ctx.getGameObjectsMap().get(Player.class).add(new Player(ctx));
        ctx.getGameObjectsMap().get(Centipede.class).add(new Centipede(10, ctx));
        timer.start();
    }

    private void mainLoop() {
        createCentipedeIfAny();
        checkPlayer();
        //Da nije igrac mrtav
        if (ctx.getGameObjectsMap().get(Player.class).size() > 0 && !ctx.isGameOver()) {
            makeScorpio();
            makeSpider();
            makeFlea();
            checkBullets();
            checkSpider();
            checkScorpio();
            checkFlea();
            checkCentipede();
            checkLevel();
            checkLives();
            render();
        }
        repaint();
    }

    private void checkFlea(){
        java.util.List<GameObject> fleaList = new ArrayList<>(ctx.getGameObjectsMap().get(Flea.class));
        fleaList.forEach(x -> x.react(x));
    }
    private void checkLives() {
        //Na svakih 2000 poena, dobij zivot
        if (ctx.getScoreLife() > 2000) {
            ctx.setLives(ctx.getLives() + 1);
            ctx.setScoreLife(0);
        }
    }

    private void checkLevel() {
        //Novi nivo ako nema vise stonoga
        if (ctx.getGameObjectsMap().get(Centipede.class).size() == 0 && ctx.getCentipedesToCreateQueue().size() == 0) {
            //Svaki novi nivo ima stonogu za jedan segment kracu
            //i jos jednu dodatnu glavu
            //na svakih 10 nivoa, dolazi nova stonoga sa 10 segmenata
            ctx.getCentipedesToCreateQueue().add(new Centipede(10 - (ctx.getLevel()  % 11) > 0 ? 10 - ctx.getLevel() % 11 : 1, ctx));
            for (int i = 0; i < ctx.getLevel(); i++) {
                ctx.getCentipedesToCreateQueue().add(new Centipede(1, ctx));
            }
            ctx.setLevel(ctx.getLevel() + 1);
            //Menjanje boje
            //Boje se pomeraju za jednu poziciju
            Color c = ctx.getColorsQueue().remove(ctx.getColorsQueue().size() - 1);
            ctx.getColorsQueue().add(0, c);
            ctx.getGameObjectsMap().get(Flea.class).clear();
            ctx.getGameObjectsMap().get(Spider.class).clear();
            ctx.getGameObjectsMap().get(Scorpion.class).clear();

        }
    }

    private void createCentipedeIfAny() {
      
        if (++ctx.centipedeCreateCounter < 20) return;
        ctx.centipedeCreateCounter = 0;
        if (ctx.getCentipedesToCreateQueue().size() != 0) {
            ctx.getGameObjectsMap()
                    .get(Centipede.class)
                    .add(ctx.getCentipedesToCreateQueue().remove(ctx.getCentipedesToCreateQueue().size() - 1));
        }
    }

    ;

    private void checkPlayer() {
        //Proveri da li je igrac mrtav
        if (ctx.getGameObjectsMap().get(Player.class).size() == 0) {
            deathRoutine();
        } else {
            Player player = (Player) ctx.getGameObjectsMap().get(Player.class).get(0);
            player.react(player);
        }
    }

    private void checkSpider(){
        java.util.List<GameObject> spiderList = new ArrayList<>(ctx.getGameObjectsMap().get(Spider.class));
        spiderList.forEach(x -> x.react(x));
    }
    private void deathRoutine() {
        //Na svaki deseti kadar da pretvara inficirane u normalne, da bi se videlo kao animacija
        if (++ctx.deathRoutineCount < 10) {
            return;
        }
        ctx.deathRoutineCount = 0;
        java.util.List<GameObject> mushrooms = ctx.getGameObjectsMap().get(Mushroom.class);
        //Brisanje svih metaka
        ctx.getGameObjectsMap().get(Bullet.class).clear();
        //Dezinficiranje pecurke, jedna po jedna, i dodaj za svaku dezificiranu po 10 poena
        boolean found = false;
        for (int i = 0; i < mushrooms.size(); i++) {
            Mushroom mushroom = (Mushroom) mushrooms.get(i);
            if (mushroom.isInfected()) {
                found = true;
                mushroom.setInfected(false);
                ctx.setScore(ctx.getScore() + 10);
                ctx.setScoreLife(ctx.getScoreLife() + 10);
                break;
            }
        }
        //Nema vise zarazenih pecurki, napravi opet igraca
        if (!found) {
            ctx.getGameObjectsMap().get(Player.class).add(new Player(ctx));
            //Postavljanje stonoga na vrh ekrana ponovo
            java.util.List<GameObject> centipedes = new ArrayList<>(ctx.getGameObjectsMap().get(Centipede.class));
            ctx.getGameObjectsMap().get(Centipede.class).clear();
            for (GameObject centipede : centipedes) {
                int centipedeLength = ((Centipede) centipede).length;
                ctx.getCentipedesToCreateQueue().add(new Centipede(centipedeLength, ctx));
            }
        }
    }

    private void checkCentipede() {
        java.util.List<GameObject> centipedes = ctx.getGameObjectsMap().get(Centipede.class);
        for (GameObject centipede : centipedes) {
            centipede.react(centipede);
        }
    }

    private void render() {
        ctx.setGameField(new GameObject[Constants.COLS][Constants.ROWS]);
        ctx.getGameObjectsMap()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(GameObject::render);
    }

    private void makeScorpio() {
        //na 500 frejmeova, odnosno na 25 sekundi, postoji sansa 50% da se pojavi skorpija
        if (ctx.scorpionCounter++ > 500) {
            if (ctx.random.nextBoolean()) {
                ctx.getGameObjectsMap().get(Scorpion.class).add(new Scorpion(ctx));
            }
            ctx.scorpionCounter = 0;
        }
    }

    private void makeSpider(){
        //na 400 frejmeova, odnosno na 20 sekundi, postoji sansa 75% da se pojavi pauk
        if (ctx.spyderCounter++ > 400) {
            if (ctx.random.nextInt(4) < 3) {
                ctx.getGameObjectsMap().get(Spider.class).add(new Spider(ctx));
            }
            ctx.spyderCounter = 0;
        }
    }

    private void makeFlea() {
        //na 300 frejmeova, postoji sansa 50% da se pojavi buva
        if (ctx.fleaCounter++ > 300) {
            if (ctx.random.nextBoolean()) {
                ctx.getGameObjectsMap().get(Flea.class).add(new Flea(ctx));
            }
            ctx.fleaCounter = 0;
        }
    }
    private void checkBullets() {
        java.util.List<GameObject> bulletList = new ArrayList<>(ctx.getGameObjectsMap().get(Bullet.class));
        bulletList.forEach(bullet -> bullet.react(bullet));
    }

    private void checkScorpio() {
        GameObject scorpion = ctx.getGameObjectsMap().get(Scorpion.class).size() > 0 ?
                ctx.getGameObjectsMap().get(Scorpion.class).get(0) : null;
        if (scorpion != null) {
            scorpion.react(scorpion);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        super.paintComponent(g);
        if (ctx != null && !ctx.isGameOver()) {
            ctx.getGameObjectsMap()
                    .values()
                    .stream()
                    .flatMap(Collection::stream)
                    .forEach(gameObject -> gameObject.paint(g));
            g.setColor(Color.WHITE);
            makeText(g);
        } else {
        	int yInc = 0;
            g.setColor(Color.WHITE);
            g.drawString("SPACE - Pocetak\n P - pauza", Constants.WIDTH / 2 - 3 * Constants.X_NORM, Constants.HEIGHT / 2 + yInc);
            yInc += 12;
            g.drawString("Cilj je ubiti snogogu. Kada je pogodite, deli se i postaje brza. Nista ne sme da Vas dotakne sem pecurke. Srecno!", 0 * Constants.X_NORM, Constants.HEIGHT / 2 + yInc);
            yInc += 12;
            
            if (ctx != null) {
                g.drawString(format("Broj poena: %d", ctx.getScore()), Constants.WIDTH / 2 + 2 * Constants.X_NORM, Constants.HEIGHT / 2 + 2 * Constants.Y_NORM);
            }
        }
    }

    private void makeText(Graphics g) {
        g.drawString(format("Poeni: %d, Zivoti: %d, Nivo: %d", ctx.getScore(), ctx.getLives(), ctx.getLevel()), 0, Constants.Y_NORM);
    }
}
