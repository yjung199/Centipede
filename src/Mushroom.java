import javax.swing.*;
import java.awt.*;

public class Mushroom {

    private int life;
    private ImageIcon[] ico = new ImageIcon[3];
    private Image[] img = new Image[3];

    public Mushroom (int i) {
        life = i;

        ico[0] = new ImageIcon("images/mushroom1.png");
        img[0] = ico[0].getImage();
        ico[1] = new ImageIcon("images/mushroom2.png");
        img[1] = ico[1].getImage();
        ico[2] = new ImageIcon("images/mushroom3.png");
        img[2] = ico[2].getImage();

    }

    public int getLife() {
        return life;
    }

    public void hitByLaser() {
        life--;
    }

    public Image mushImage(){
        switch(life) {
            case 3:
                return img[0];
            case 2:
                return img[1];
            case 1:
                return img[2];
            default:
                return img[0];
        }
    }
}
