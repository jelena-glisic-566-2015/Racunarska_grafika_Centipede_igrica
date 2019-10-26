import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Centipede implements GameObject {
    int length;
    private GameContext ctx;
    private java.util.List<CentipedeSegment> segmentList;
    private CentipedeState state;
    private int upOrDown = 1;
    //4 reda moze da se najvise popne
    private int maxUp = 4;
    private int upCount = 0;
    // Brzina, sto je broj manji, brze ide
    private int speed = 4;
    private int speedCounter = 0;
    private CentipedeSegment previousPosition;
    //Ukoliko se menja red, da glava pokazuje na gore ili dole
    private int changingRow = 0;

    Centipede(int length, GameContext ctx) {
        this.length = length;
        this.ctx = ctx;
        segmentList = new ArrayList<>();
        segmentList.add(new CentipedeSegment(Constants.COLS / 2, 0));
        for (int i = 1; i < length; i++) {
            segmentList.add(new CentipedeSegment(Constants.COLS / 2, 0));
        }
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            state = CentipedeState.LEFT;
        } else {
            state = CentipedeState.RIGHT;
        }
        ctx.getGameField()[Constants.COLS / 2][0] = this;
        this.speed = this.length * 4 / 10;

    }


    //Za novu stonogu, kad staru ubiju
    private Centipede(java.util.List<CentipedeSegment> segments, GameContext ctx, CentipedeState state, int upOrDown) {
        this.length = segments.size();
        segmentList = new ArrayList<>();
        for (CentipedeSegment seg : segments) {
            segmentList.add(new CentipedeSegment(seg.x, seg.y));
        }
        this.upOrDown = upOrDown;
        this.state = state;
        this.ctx = ctx;
        this.speed = this.length * 4 / 10;
    }

    @Override
    public boolean paint(Graphics g) {
        g.setColor(ctx.getColorsQueue().get(Constants.CENTIPEDE_COLOR_INDEX));
        for (int i = 0; i < segmentList.size(); i++) {
            CentipedeSegment segment = segmentList.get(i);
            if (segment != null) {
                //Crtanje glavu
                if (i == 0) {
                    //Provera da li silazi ili se penje
                    if (changingRow == 1 || state == CentipedeState.POISONED) {
                        g.fillPolygon(new int[]{
                                        segment.x * Constants.X_NORM,
                                        (1 + segment.x) * Constants.X_NORM,
                                        (int) ((0.5 + segment.x) * Constants.X_NORM)},
                                new int[]{
                                        (segment.y) * Constants.Y_NORM,
                                        (segment.y) * Constants.Y_NORM,
                                        (1 + segment.y) * Constants.Y_NORM,
                                },
                                3);
                    } else if (changingRow == -1) {
                        g.fillPolygon(new int[]{
                                        segment.x * Constants.X_NORM,
                                        (1 + segment.x) * Constants.X_NORM,
                                        (int) ((0.5 + segment.x) * Constants.X_NORM)},
                                new int[]{
                                        (segment.y + 1) * Constants.Y_NORM,
                                        (segment.y + 1) * Constants.Y_NORM,
                                        (segment.y) * Constants.Y_NORM,
                                },
                                3);
                    }
                    //Provera da li se krece levo ili desno
                    else if (state == CentipedeState.LEFT) {
                        g.fillPolygon(new int[]{
                                        segment.x * Constants.X_NORM,
                                        (1 + segment.x) * Constants.X_NORM,
                                        (1 + segment.x) * Constants.X_NORM},
                                new int[]{
                                        (int) ((segment.y + 0.5) * Constants.Y_NORM),
                                        (1 + segment.y) * Constants.Y_NORM,
                                        (segment.y) * Constants.Y_NORM
                                },
                                3);
                    } else {
                        g.fillPolygon(new int[]{
                                        (1 + segment.x) * Constants.X_NORM,
                                        segment.x * Constants.X_NORM,
                                        segment.x * Constants.X_NORM},
                                new int[]{
                                        (int) ((segment.y + 0.5) * Constants.Y_NORM),
                                        (1 + segment.y) * Constants.Y_NORM,
                                        (segment.y) * Constants.Y_NORM
                                },
                                3);

                    }
                } else {
                    //Crtanje segmenata
                    g.fillRoundRect(segment.x * Constants.X_NORM, segment.y * Constants.Y_NORM,
                            Constants.X_NORM, Constants.Y_NORM, 10, 10);
                }
            }
        }
        return false;
    }

    @Override
    public void react(GameObject o) {
        if (o == this) {
            if (++speedCounter > speed) {
                progress();
                speedCounter = 0;
            }
        } else if (o instanceof Bullet) {
            hit((Bullet) o);
        }

    }

    private void hit(Bullet o) {
        ctx.setScore(ctx.getScore() + 50);
        ctx.setScoreLife(ctx.getScoreLife() + 50);

        for (CentipedeSegment segment : segmentList) {
            if (segment.y == o.y && segment.x == o.x) {
                int index = segmentList.indexOf(segment);
                ctx.getGameObjectsMap().get(Mushroom.class).add(new Mushroom(segment.x, segment.y, ctx));
                ctx.getGameObjectsMap().get(Centipede.class).remove(this);
                //Pravljenje nove ako se pogodilo bilo gde
                if (index > 0) {
                    ctx.getGameObjectsMap().get(Centipede.class).add(
                            new Centipede(segmentList.subList(0, index), ctx, state, upOrDown));
                }
                //Ako nije rep, napravi novu stonogu
                if (index < segmentList.size() - 1) {
                    ctx.getGameObjectsMap().get(Centipede.class).add(
                            new Centipede(segmentList.subList(index + 1, segmentList.size()), ctx,
                                    state  ,
                                    upOrDown));
                }
                break;
            }
        }
    }

    private void progress() {
        boolean changedPosition = false;
        if (!segmentList.get(0).equals(previousPosition)) {
            changedPosition = true;
            previousPosition = new CentipedeSegment(segmentList.get(0).x, segmentList.get(0).y);
        }
        if (length > 1 && changedPosition) {
            for (int i = length - 1; i > 0; i--) {
                if (!segmentList.get(i).equals(segmentList.get(i - 1)))
                    segmentList.set(i, new CentipedeSegment(segmentList.get(i - 1).x, segmentList.get(i - 1).y));
            }
        }
        CentipedeSegment head = segmentList.get(0);
        switch (state) {
            case LEFT:
                if (head.x == 0){
                    state = CentipedeState.RIGHT;
                    changeRow(head);
                } else {
                    boolean mushroomPresent = onMushroom(head);
                    if (!mushroomPresent){
                        head.x --;
                    }
                }
                break;
            case RIGHT:
                if (head.x == Constants.COLS - 1){
                    state = CentipedeState.LEFT;
                    changeRow(head);
                } else {
                    boolean mushroomPresent = onMushroom(head);
                    if (!mushroomPresent){
                        head.x ++;
                    }
                }
                break;
            case POISONED:
                if (head.y < Constants.COLS - 1){
                    head.y++;
                } else {
                    // Kad dodje do kraja table, nasumicno izaberi novi smer
                    state = ctx.random.nextBoolean() ? CentipedeState.LEFT : CentipedeState.RIGHT;
                }
        }
        //Provera da li imamo samo glavu, ili imamo i telo
        if (segmentList.size() == 1 || segmentList.get(1).y == segmentList.get(0).y) {
            changingRow = 0;
        }
    }

    // Reagovanje na pecrku, i vrati true ako je pecurka bila na putu
    private boolean onMushroom(CentipedeSegment head){
        // Zavisi od smera, proverava polje jedan segment ispred ili iza glave, da vidi da li je u pitanju pecurka
        int lookAhead = (state == CentipedeState.LEFT) ? - 1 : 1;
        GameObject go = ctx.getGameField()[head.x + lookAhead][head.y];
        if (go instanceof Mushroom){
            if (((Mushroom)go).isInfected()){
                state = CentipedeState.POISONED;
            } else {
                state = state == CentipedeState.LEFT ? CentipedeState.RIGHT : CentipedeState.LEFT;
            }
            changeRow(head);
            return true;
        }
        return false;
    }
    private void changeRow(CentipedeSegment head) {
        //Da ne ide do kraja ekrana, vec se vrati nazad
        if (upOrDown == -1) {
            if (++upCount == maxUp) {
                upOrDown = -upOrDown;
                upCount = 0;
            }
        }
        // Spusta se ili dize
        head.y += upOrDown;
        //Postavljanje smera u kome glava da gleda
        changingRow = upOrDown;
        if (head.y > Constants.ROWS - 1) {
            head.y = Constants.ROWS - 2;
            upOrDown = -upOrDown;
        }
        if (head.y < 0) {
            head.y = 1;
            upOrDown = -upOrDown;

        }
    }

    @Override
    public void render() {
        for (CentipedeSegment segment : segmentList) {
            ctx.getGameField()[segment.x][segment.y] = this;
        }
    }

    private enum CentipedeState {
        LEFT, //Kretanje na levo
        RIGHT, //Ketanje na desno
        POISONED //otrovana je zutom pecurkom, silazi dole
    }
}
