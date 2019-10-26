public interface Constants {
    int WIDTH = 640;
    int HEIGHT = 640;
    int ROWS = 32;
    int COLS = 32;
    int X_NORM = WIDTH / COLS;
    int Y_NORM = HEIGHT / ROWS;
    //gde se boja kog objekta nalazi u listi ColorsQueue (GameContext)
    int FLEA_COLOR_INDEX = 6;
    int SPIDER_COLOR_INDEX = 5;
    int CENTIPEDE_COLOR_INDEX = 4;
    int MUSHROOM_COLOR_INDEX = 3;
    int BULLET_COLOR_INDEX = 2;
    int POISON_COLOR_INDEX = 1;
    int SCORPION_COLOR_INDEX = 0;
    //Veci broj, stonoga je sporija
    int CENTIPEDE_SPEED = 17;
}
