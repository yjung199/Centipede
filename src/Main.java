import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {


        JFrame obj = new JFrame();

        // Arguments for Gampeplay
        // Gameplay(double Mushroom_Density, int centipedeLength)
        GamePlay gameplay = new GamePlay(0.6, 6);

        obj.setTitle("Centipede");
        obj.setBounds(10, 10, (32*15)+16, (32*20));

        obj.setBackground(Color.BLACK);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gameplay);

        Thread t1 = new Thread(gameplay);
        t1.start();



    }
}
