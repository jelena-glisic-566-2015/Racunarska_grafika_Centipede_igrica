import java.awt.*;

public class Bullet implements GameObject {
    int x;
    int y;
    private GameContext ctx;

    Bullet(int x, int y, GameContext ctx) {
        this.x = x;
        this.y = y;
        this.ctx = ctx;
    }

    @Override
    public boolean paint(Graphics g) {
        g.setColor(ctx.getColorsQueue().get(Constants.BULLET_COLOR_INDEX));
        g.drawLine((int) ((x + 0.5) * Constants.X_NORM), y * Constants.Y_NORM,
                (int) ((x + 0.5) * Constants.X_NORM), (y + 1) * Constants.Y_NORM);
        return true;
    }

    @Override
    public void react(GameObject o) {
        if (y <= 0) {
            // Ako je van ekrana, samo se unisti
            ctx.getGameObjectsMap().get(Bullet.class).remove(this);
            return;
        } else {
            //Pomeranje metka na gore
            y--;
        }
        GameObject go = ctx.getGameField()[x][y];
        // Da moze da prodje kroz igraca
        if (go != null && !(go instanceof Player)) {
            //Ako nije igrac, pozovi rutinu react iz objekta u kog je udario, i unisti se
            go.react(this);
            ctx.getGameObjectsMap().get(Bullet.class).remove(this);
        }

    }

    @Override
    public void render() {
        ctx.getGameField()[x][y] = this;
    }
}
