import java.awt.*;

public interface GameObject {

    //Iscrtavanje figure
    boolean paint(Graphics g);


    //Interakciju sa objektima (pomeranje...)
    void react(GameObject o);

    //Postavlajnje objekata u matricu da bi moglo da se vrsi detekcija sudara
    void render();

}
