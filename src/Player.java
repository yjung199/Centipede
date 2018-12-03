import javax.swing.*;
import java.awt.*;

public class Player {


    private ImageIcon ico;
    private Image img;
    private int life;
    public boolean fireReady, shot;




    public Player () {
        life = 3;
        ico = new ImageIcon("images/player.png");
        img = ico.getImage();
        fireReady = false;

    }

    public Image getPlayerImage() {
        return img;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int val) {
        life = val;
    }

    public void gotHit() {
        life--;
    }




}
