import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


/*
    PRAVILA IGRE:
    Stonoga se krece sa vrha ekrana, i silazi red ispod kada udari o ivicu ili pecurku.
    Ako se nalazi na dnu ekrana, onda se penje, i moze da se penje maksimalno 5 redova
    Ako je pecurka otrovna, prilikom kontakta sa stonogom, stonoga krece da juri ka dnu ekrana, i kada stigne
    vraca se u normalu.
    Skorpija povremeno prodje preko ekrana, i u kontaktu sa pecurkom, pravi od nje otrovnu.
    Pauk  se povremeno spusta preko ekrana, i ostavlja za sobom trag pecurki.
    Muva crveni krug, povremeno se pojavi i skace po ekranu, jede pecurke ako se susretne sa njima.
    Korisnik treba da puca stonogu, i u zavisnosti gde je pogodi, stvara se nova pecurka. Ako je pogodi bilo
    gde u telo.
    Od stonoge se prave dve nove, manje stonoge koje su brze.
    Gubitak zivota se desava kada bilo sta dotakne igraca osim pecurke.
    Igrac moze simultano da ispali 5 metaka.
    Prilikom prolaska u sledeci nivo, boje svega osim igraca se menjaju, dodaje se jedna dodatna glava, 
    i stonoga je kraca.
    Za jedan segment. Na svakih 10 nivoa, stonoga opet iz pocetka ima 10 segmenata
 */
public class App {
    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(() -> {
            //Objekat prozora
            JFrame frame = new JFrame();
            //Panel za crtanje
            JPanel gameField = new GameField();
            //Crna pozadina
            frame.getContentPane().setBackground(Color.BLACK);
            gameField.setBackground(Color.BLACK);
            //Dodavanje prozora
            frame.add(gameField);
            //Velicina prozora 640x640 piksela
            frame.setSize(Constants.WIDTH, Constants.HEIGHT + Constants.Y_NORM);
            //Prikazati prozor
            frame.setVisible(true);
            //Kada se klikne na dugme za izlaz, aplikacija se gasi
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //Title prozora
            frame.setTitle("Centipede - projekat");
            //Nije dozvoljeno menjanje velicine prozora rucno
            frame.setResizable(false);


        });
    }
}
