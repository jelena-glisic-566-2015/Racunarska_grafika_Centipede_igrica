import java.awt.*;
import java.util.Random;

public class Flea implements GameObject {
    private GameContext ctx;
    private int x;
    private int y1;
    private int y;
    private int counter;
    private int incr;
    Flea(GameContext ctx) {
        counter = 0;
        this.ctx = ctx;
        this.x = Constants.COLS - 1;
        //Moze da se pojavi u donjih 13 redova, minimalno od treceg
        this.y1 = Constants.ROWS - 3 - ctx.random.nextInt(10);
        this.y = y1;
    }

    @Override
    public boolean paint(Graphics g) {
        g.setColor(ctx.getColorsQueue().get(Constants.FLEA_COLOR_INDEX));
        g.fillOval(x * Constants.X_NORM, y * Constants.Y_NORM + 10, Constants.X_NORM, Constants.Y_NORM - 10);
        return true;
    }

    @Override
    public void react(GameObject o) {
        if (o == this) {
            x--;
            if (x < 0) {
                ctx.getGameObjectsMap().get(Flea.class).remove(this);
                return;
            }
            //Cik-cak
            y = y1 + (int)(5 * Math.sin(counter++ / 2));
            //Ogranici y
            y = y > Constants.ROWS - 1 ? Constants.ROWS - 1 : y < 0 ? 0 : y;
            GameObject go = ctx.getGameField()[x][y];
            if (go != null) {
                go.react(this);
            }
        } else if (o instanceof Bullet) {
            ctx.setScore(ctx.getScore() + 20);
            ctx.setScoreLife(ctx.getScoreLife() + 20);
            ctx.getGameObjectsMap().get(Flea.class).remove(this);
        }
    }

    @Override
    public void render() {
        ctx.getGameField()[x][y] = this;
    }
}
