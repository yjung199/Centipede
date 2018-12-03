import javax.swing.*;
import java.awt.*;

public class Centipede {

    public int[] centXlength;
    public int[] centYlength;

    // The direction of centipede's head
    // When head collides with wall or mush,
    public boolean left;
    // public boolean right;

    public ImageIcon icoHeadLeft;
    public ImageIcon icoHeadRight;
    public ImageIcon icoBody;

    public int x, y, length, life;

    public Centipede(int length){
        icoHeadLeft = new ImageIcon("images/centHeadL.png");
        icoHeadRight = new ImageIcon("images/centHeadR.png");
        icoBody = new ImageIcon("images/cBody.png");

        centXlength = new int [300];
        centYlength = new int [400];
        x = 0;
        y = 0;
        left = true;
        //right = false;
        this.length = length;
        life = 2;
    }

    public void gotHit()
    {
        life--;

    }

}

