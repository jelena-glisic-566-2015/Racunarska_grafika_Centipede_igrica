import java.awt.*;

public class Spider implements GameObject {
    int x;
    int y;
    GameContext ctx;

    Spider(GameContext ctx) {
        this.ctx = ctx;
        //Nasumicno biranje kolone gde se javlja, sa vrha ekrana
        x = this.ctx.random.nextInt(Constants.COLS);
        y = -1;
    }

    @Override
    public boolean paint(Graphics g) {
        g.setColor(ctx.getColorsQueue().get(Constants.SPIDER_COLOR_INDEX));
        g.fillOval(x * Constants.X_NORM, y * Constants.Y_NORM, Constants.X_NORM, Constants.Y_NORM);
        return true;
    }

    @Override
    public void react(GameObject o) {
        if (o == this) {
            y++;
            if (y >= Constants.COLS - 1){
                //Da se ukloni ako je dosao do dna ekrana
                ctx.getGameObjectsMap().get(Spider.class).remove(this);
            }

            int chance = ctx.random.nextInt(10);
            //10 posto sanse da se napravi nova pecurka
            if (chance == 0) {
                System.out.println(x);
                System.out.println(y);
                if (ctx.getGameField()[x][y] == null) {
                    Mushroom mushroom = new Mushroom(x, y, ctx);
                    ctx.getGameObjectsMap().get(Mushroom.class).add(mushroom);
                }
            }
        }
        if (o instanceof Bullet){
            ctx.setScore(ctx.getScore()+20);
            ctx.setScoreLife(ctx.getScoreLife()+20);

            ctx.getGameObjectsMap().get(Spider.class).remove(this);
        }
    }

    @Override
    public void render() {
        ctx.getGameField()[x][y] = this;
    }
}
