import java.awt.*;

public class Mushroom implements GameObject {

    private int x;
    private int y;
    private int health;
    private boolean infected = false;
    private GameContext ctx;
    Mushroom(int x, int y, GameContext ctx) {
        this.x = x;
        this.y = y;
        health = 4;
        infected = false;
        this.ctx = ctx;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    @Override
    public boolean paint(Graphics g) {
        if (health > 0) {
            g.setColor(ctx.getColorsQueue().get(infected ? Constants.POISON_COLOR_INDEX : Constants.MUSHROOM_COLOR_INDEX));
            //glava pecurke
            g.fillRect(x * Constants.X_NORM, y * Constants.Y_NORM, Constants.X_NORM, (int) (Constants.Y_NORM / 4.));
            //telo pecurke
            g.fillRect((int) ((x + 0.3) * Constants.X_NORM), y * Constants.Y_NORM, (int) (Constants.X_NORM * 0.3), (int) (Constants.Y_NORM / 4. * health));
            return true;
        }
        return false;
    }

    @Override
    public void react(GameObject o) {
        //10 poena za pogodak, 20 ako se skroz unisti
        if (o instanceof Bullet || o instanceof Flea) {
            ctx.setScore(ctx.getScore() + 10);
            ctx.setScoreLife(ctx.getScoreLife() + 10);
            health--;
            if (health < 1) {
                ctx.setScore(ctx.getScore() + 10);
                ctx.setScoreLife(ctx.getScoreLife() + 10);
                ctx.getGameObjectsMap().get(Mushroom.class).remove(this);
                ctx.getGameField()[x][y] = null;
            }
        }
        if (o instanceof Scorpion) {
            infected = true;
        }
    }

    @Override
    public void render() {
        ctx.getGameField()[x][y] = this;
    }


}
