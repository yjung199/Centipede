import javax.swing.*;
import java.awt.*;

public class Spider {

    private ImageIcon ico;
    private Image img;
    public int life, x, y;

    public Spider(int x, int y){
        life = 2;
        ico = new ImageIcon("images/spider.png");
        img = ico.getImage();
        this.x = x;
        this.y = y;


    }

    public Image getSpiderImage(){ return img; }

    public void draw(Graphics2D g) {
//        this.x = x;
//        this.y = y;
        if(life > 0) {
            g.drawImage(getSpiderImage(), this.x, this.y, null);
        }
    }

    public void gotHit() {
        life--;
    }

}
