import java.awt.*;

public class Player implements GameObject {
    GameContext ctx;
    int x;
    int y;

    Player(GameContext ctx) {
        this.ctx = ctx;
        //Na pola ekrena, na donjoj ivici
        y = Constants.ROWS - 1;
        x = Constants.COLS / 2;
    }

    @Override
    public boolean paint(Graphics g) {

        g.setColor(Color.WHITE);
        Polygon p = new Polygon();
        p.addPoint(x * Constants.X_NORM, (int) ((y + 2 / 3.) * Constants.Y_NORM));
        p.addPoint((int) ((x + 1 / 2.) * Constants.X_NORM), y * Constants.Y_NORM);
        p.addPoint(((x + 1) * Constants.X_NORM), (int) ((y + 2 / 3.) * Constants.Y_NORM));
        p.addPoint((int) ((x + 2 / 3.) * Constants.X_NORM), (y + 1) * Constants.Y_NORM);
        p.addPoint((int) ((x + 1 / 3.) * Constants.X_NORM), (y + 1) * Constants.Y_NORM);
        g.fillPolygon(p);

        return false;
    }


    @Override
    public void react(GameObject o) {
        if (o == this) {
            checkIsDead();
            move();
            makeBullets();
        }
    }

    private void makeBullets() {
        //Maksimalno 5 metkova na ekranu
        if (ctx.getGameObjectsMap().get(Bullet.class).size() > 4) ctx.setClicked(false);
        if (ctx.isClicked()) {
            ctx.getGameObjectsMap().get(Bullet.class).add(new Bullet(x, y, ctx));
            ctx.setClicked(false);
        }
    }

    private void checkIsDead(){
        GameObject go = ctx.getGameField()[x][y];
        if (go != null && !(go instanceof Player) && !(go instanceof Mushroom) && !(go instanceof Bullet)){
            ctx.getGameObjectsMap().get(Player.class).remove(this);
            ctx.setLives(ctx.getLives() - 1);
            if (ctx.getLives() == 0){
                ctx.setGameOver(true);
            }
        }
    }

    private void move() {
        int tmpX = ctx.getMouseNew().x / Constants.X_NORM;
        int tmpY = ctx.getMouseNew().y / Constants.Y_NORM;
        //Da moze maksimalno pet redova da se pomera
        tmpY = tmpY > Constants.ROWS - 5 ? tmpY : Constants.ROWS - 5;
        //Da ne moze da prodje kroz pecurku
        if (!(ctx.getGameField()[tmpX][tmpY] instanceof Mushroom)) {
            x = tmpX;
            y = tmpY;
        }
    }


    //Da se ne renderuje igraca na matricu, jer sve provere ko ga udara vrsi on sam
    @Override
    public void render() {
    }
}
