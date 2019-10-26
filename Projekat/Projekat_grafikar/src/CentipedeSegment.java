import java.awt.*;
import java.util.Objects;

public class CentipedeSegment{
    int x;
    int y;
    int xFraction = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CentipedeSegment segment = (CentipedeSegment) o;
        return x == segment.x &&
                y == segment.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    CentipedeSegment(int x, int y){
        this.x = x;
        xFraction = x * 10;
        this.y = y;
    }

    void incrX(int delta){
        xFraction += delta;
        if (xFraction >= Constants.CENTIPEDE_SPEED || xFraction <= -Constants.CENTIPEDE_SPEED){
            x += Math.signum(xFraction);
            xFraction = 0;
        }
    }
}
