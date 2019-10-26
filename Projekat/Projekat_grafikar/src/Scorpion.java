import java.awt.*;

public class Scorpion implements GameObject {
    private GameContext ctx;
    private int x;
    private int y;

    Scorpion(GameContext ctx) {
        this.ctx = ctx;
        this.x = 0;
        this.y = 5 + ctx.random.nextInt(Constants.ROWS -  10);
    }

    @Override
    public boolean paint(Graphics g) {
        g.setColor(ctx.getColorsQueue().get(Constants.SCORPION_COLOR_INDEX));
        g.fillRect(x * Constants.X_NORM, y * Constants.Y_NORM, Constants.X_NORM, Constants.Y_NORM);
        return true;
    }

    @Override
    public void react(GameObject o) {
        if (o == this) {
            if (++x > Constants.COLS - 1) {
                ctx.getGameObjectsMap().get(Scorpion.class).remove(this);
                return;
            }
            GameObject go = ctx.getGameField()[x][y];
            if (go != null){
                go.react(this);
            }
        } else if (o instanceof Bullet) {
            ctx.setScore(ctx.getScore() + 20);
            ctx.setScoreLife(ctx.getScoreLife() + 20);
            ctx.getGameObjectsMap().get(Scorpion.class).remove(this);
        }
    }

    @Override
    public void render() {
        ctx.getGameField()[x][y] = this;
    }
}
