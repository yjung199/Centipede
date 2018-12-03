import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GamePlay extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener, Runnable {


    public enum GameState {
        GAME_START,
        GAME_PLAYING,
        GAME_OVER
    }

    public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage(""),
            new Point(0, 0),
            "invisible");

    private Image dbImage;
    private Graphics dbGraphics;
    private boolean play = false;
    private int score = 0;
    private int totalSrms = 30;
    private int centOriginLength;
    private int playerX = 310;
    private Timer timer;
    private Timer timer2;
    private Timer timer3;
    private int delay = 8;

    private Centipede[] cents;
    private boolean mouseDragged;
    private boolean mouseClicked;
    private boolean spacePressed;

    private Component comp;
    private Player player;
    private Rectangle bullet;
    private TileGenerator tiles;
    private Mushroom mush;


    // from 0.0 to 1.0 scale the density of mushroom in the field
    public double shroomNum;

    private Spider spider;
    private Centipede centipede;
    private Centipede centipede2;
    private Centipede centipede3;
    Rectangle mushHitBox;

    private GameState gameState;
    private double rNum = 0;

    private int screenWidth, screenHeight;

    private int mushCol;
    private int mushRow;
    private int x, y, cx, cy, sx, sy ,xDirection, yDirection, playerLife;



    /*
    Player hits a centipede segment: 2 point
    Player destroys a centipede segment: 5 points
    A mushroom is hit: 1 point
    A mushroom is destroyed: 5 points
    For each mushroom hit but not destroyed, and restored when the player dies: 10 points
    Hitting the spider: 100 points
    Killing the spider: 600 points
    Completely killing off a centipede: 600 points
    */

    public GamePlay(double shroomNum, int centipedeLength) {

        sx = 0;
        sy = 32;
        cx = (32*12);
        cy = 0;
        mushRow = 15;
        mushCol = 13;
        this.shroomNum = shroomNum;
        this.centOriginLength = centipedeLength;



        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addMouseListener(this);
        addMouseMotionListener(this);
        timer = new Timer(delay, this);
        timer.start();

        tiles = new TileGenerator(mushRow, mushCol, shroomNum);

        player = new Player();
        x = getWidth()/2-32;
        y = getHeight();
        playerLife = player.getLife();
        spider = new Spider(sx, sy);
        centipede = new Centipede(centOriginLength);
        score = 0;
        gameState = GameState.GAME_START;
//        spider = new Spider();
//        enemySpawner.start();
        setCursor(INVISIBLE_CURSOR);
        timer2 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rNum = Math.random();

            }
        });

        timer3 = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(centipede!=null)
                {

                    if(!centipede.left)
                    {
                        for (int r = centipede.length-1; r >= 0; r--)
                        {
                            centipede.centYlength[r+1] = centipede.centYlength[r];
                        }
                        for (int r = centipede.length; r >= 0; r--)
                        {
                            if(r==0)
                            {
                                centipede.centXlength[r] = centipede.centXlength[r]+32;
                            } else {
                                centipede.centXlength[r] = centipede.centXlength[r-1];
                            }

                            if(centipede.centXlength[r] > getWidth()-32)
                            {
                                if(centipede.centYlength[r] < 32*13) {
                                    centipede.centYlength[r] = centipede.centYlength[r]+32;
                                    centipede.centXlength[r] = getWidth()-32;
                                    centipede.left = Boolean.TRUE;
                                }
                                else {
                                    centipede.centXlength[r] = getWidth()-32;
                                    centipede.left = Boolean.TRUE;
                                }

                            }
                        }
                    } else {
                        for (int r = centipede.length-1; r >= 0; r--)
                        {
                            centipede.centYlength[r+1] = centipede.centYlength[r];
                        }
                        for (int r = centipede.length; r >= 0; r--)
                        {
                            if(r==0)
                            {
                                centipede.centXlength[r] = centipede.centXlength[r]-32;
                            } else {
                                centipede.centXlength[r] = centipede.centXlength[r-1];
                            }

                            if(centipede.centXlength[r] < 0 )
                            {
                                if(centipede.centYlength[r] < 32*13) {
                                    centipede.centYlength[r] = centipede.centYlength[r]+32;
                                    centipede.centXlength[r] = 0;
                                    centipede.left = Boolean.FALSE;
                                }
                                else {
                                    centipede.centXlength[r] = 0;
                                    centipede.left = Boolean.FALSE;
                                }


                            }
                        }

                    }
                    Rectangle centHitBox = new Rectangle(centipede.centXlength[0], centipede.centYlength[0], 32, 32);
                    for(int i = 0; i < mushRow; i++) {
                        for (int j = 0; j < mushCol; j++) {
                            if (tiles.tile[i][j] > 0) {
                                Rectangle mushHitBox = new Rectangle(i * 32, j * 32, 32, 32);
                                if(centHitBox.intersects(mushHitBox)){
                                    if(!centipede.left)
                                    {
                                        centipede.centYlength[0] = centipede.centYlength[0]+32;
                                        centipede.centXlength[0] = centipede.centXlength[0]-32;
                                        centipede.left = Boolean.TRUE;


                                    } else {
                                        centipede.centYlength[0] = centipede.centYlength[0]+32;
                                        centipede.centXlength[0] = centipede.centXlength[0]+32;
                                        centipede.left = Boolean.FALSE;

                                    }

                                }
                            }
                        }
                    }
                }

                if(centipede2!=null)
                {

                    if(!centipede2.left)
                    {
                        for (int r = centipede2.length-1; r >= 0; r--)
                        {
                            centipede2.centYlength[r+1] = centipede2.centYlength[r];
                        }
                        for (int r = centipede2.length; r >= 0; r--)
                        {
                            if(r==0)
                            {
                                centipede2.centXlength[r] = centipede2.centXlength[r]+32;
                            } else {
                                centipede2.centXlength[r] = centipede2.centXlength[r-1];
                            }

                            if(centipede2.centXlength[r] > getWidth()-32)
                            {
                                if(centipede2.centYlength[r] < 32*13) {
                                    centipede2.centYlength[r] = centipede2.centYlength[r]+32;
                                    centipede2.centXlength[r] = getWidth()-32;
                                    centipede2.left = Boolean.TRUE;
                                }
                                else {
                                    centipede2.centXlength[r] = getWidth()-32;
                                    centipede2.left = Boolean.TRUE;
                                }

                            }
                        }
                    } else {
                        for (int r = centipede2.length-1; r >= 0; r--)
                        {
                            centipede2.centYlength[r+1] = centipede2.centYlength[r];
                        }
                        for (int r = centipede2.length; r >= 0; r--)
                        {
                            if(r==0)
                            {
                                centipede2.centXlength[r] = centipede2.centXlength[r]-32;
                            } else {
                                centipede2.centXlength[r] = centipede2.centXlength[r-1];
                            }

                            if(centipede2.centXlength[r] < 0 )
                            {
                                if(centipede2.centYlength[r] < 32*13) {
                                    centipede2.centYlength[r] = centipede2.centYlength[r]+32;
                                    centipede2.centXlength[r] = 0;
                                    centipede2.left = Boolean.FALSE;
                                }
                                else {
                                    centipede2.centXlength[r] = 0;
                                    centipede2.left = Boolean.FALSE;
                                }


                            }
                        }

                    }
                    Rectangle centHitBox2 = new Rectangle(centipede2.centXlength[0], centipede2.centYlength[0], 32, 32);
                    for(int i = 0; i < mushRow; i++) {
                        for (int j = 0; j < mushCol; j++) {
                            if (tiles.tile[i][j] > 0) {
                                Rectangle mushHitBox = new Rectangle(i * 32, j * 32, 32, 32);
                                if(centHitBox2.intersects(mushHitBox)){
                                    if(!centipede2.left)
                                    {
                                        centipede2.centYlength[0] = centipede2.centYlength[0]+32;
                                        centipede2.centXlength[0] = centipede2.centXlength[0]-32;
                                        centipede2.left = Boolean.TRUE;


                                    } else {
                                        centipede2.centYlength[0] = centipede2.centYlength[0]+32;
                                        centipede2.centXlength[0] = centipede2.centXlength[0]+32;
                                        centipede2.left = Boolean.FALSE;

                                    }

                                }
                            }
                        }
                    }
                }

                if(centipede3!=null)
                {

                    if(!centipede3.left)
                    {
                        for (int r = centipede3.length-1; r >= 0; r--)
                        {
                            centipede3.centYlength[r+1] = centipede3.centYlength[r];
                        }
                        for (int r = centipede3.length; r >= 0; r--)
                        {
                            if(r==0)
                            {
                                centipede3.centXlength[r] = centipede3.centXlength[r]+32;
                            } else {
                                centipede3.centXlength[r] = centipede3.centXlength[r-1];
                            }

                            if(centipede3.centXlength[r] > getWidth()-32)
                            {
                                if(centipede3.centYlength[r] < 32*13) {
                                    centipede3.centYlength[r] = centipede3.centYlength[r]+32;
                                    centipede3.centXlength[r] = getWidth()-32;
                                    centipede3.left = Boolean.TRUE;
                                }
                                else {
                                    centipede3.centXlength[r] = getWidth()-32;
                                    centipede3.left = Boolean.TRUE;
                                }

                            }
                        }
                    } else {
                        for (int r = centipede3.length-1; r >= 0; r--)
                        {
                            centipede3.centYlength[r+1] = centipede3.centYlength[r];
                        }
                        for (int r = centipede3.length; r >= 0; r--)
                        {
                            if(r==0)
                            {
                                centipede3.centXlength[r] = centipede3.centXlength[r]-32;
                            } else {
                                centipede3.centXlength[r] = centipede3.centXlength[r-1];
                            }

                            if(centipede3.centXlength[r] < 0 )
                            {
                                if(centipede3.centYlength[r] < 32*13) {
                                    centipede3.centYlength[r] = centipede3.centYlength[r]+32;
                                    centipede3.centXlength[r] = 0;
                                    centipede3.left = Boolean.FALSE;
                                }
                                else {
                                    centipede3.centXlength[r] = 0;
                                    centipede3.left = Boolean.FALSE;
                                }


                            }
                        }

                    }
                    Rectangle centHitBox3 = new Rectangle(centipede3.centXlength[0], centipede3.centYlength[0], 32, 32);
                    for(int i = 0; i < mushRow; i++) {
                        for (int j = 0; j < mushCol; j++) {
                            if (tiles.tile[i][j] > 0) {
                                Rectangle mushHitBox = new Rectangle(i * 32, j * 32, 32, 32);
                                if(centHitBox3.intersects(mushHitBox)){
                                    if(!centipede3.left)
                                    {
                                        centipede3.centYlength[0] = centipede3.centYlength[0]+32;
                                        centipede3.centXlength[0] = centipede3.centXlength[0]-32;
                                        centipede3.left = Boolean.TRUE;


                                    } else {
                                        centipede3.centYlength[0] = centipede3.centYlength[0]+32;
                                        centipede3.centXlength[0] = centipede3.centXlength[0]+32;
                                        centipede3.left = Boolean.FALSE;

                                    }

                                }
                            }
                        }
                    }
                }



            }
        });

        if(gameState != GameState.GAME_PLAYING)
        {
            timer2.start();
            timer3.start();
        }



    }

    public void move() {
        x += xDirection;
        y += yDirection;

        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x > getWidth() - 32) {
            x = getWidth() - 32;
        }
        if (y > getHeight() - 32) {
            y = getHeight() - 32;
        }

    }



    public void setXDirection(int xdir) {
        xDirection = xdir;
    }

    public void setYDirection(int ydir) {
        yDirection = ydir;
    }


    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (gameState == GameState.GAME_START) {
            cx = getWidth() - 32;
            cy = 0;
            for (int i = 0; i < centipede.length; i++) {
                centipede.centXlength[i] = cx + 32 * i;
                centipede.centYlength[i] = cy;
            }

        }

        screenHeight = getHeight();
        screenWidth = getWidth();

        // Rendering Antialiasing
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw background
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());


        // Draw mushroom tiles
        tiles.draw(g2);

        // Draw player
        g2.setColor(Color.DARK_GRAY);
        if (gameState == GameState.GAME_START) {
            g2.drawImage(player.getPlayerImage(), getWidth() / 2 - 32, getHeight()-32, this);
        } else{
            g2.drawImage(player.getPlayerImage(), x, y, this);
        }



        // Draw centipede
        if(centipede != null)
        {
            if (centipede.life > 0)
            {
                for (int i = 0; i < centipede.length; i++)
                {

                    if(i==0 && centipede.left){
                        centipede.icoHeadLeft.paintIcon(this, g2, centipede.centXlength[i], centipede.centYlength[i]);
                    }
                    if(i==0 && !(centipede.left)){
                        centipede.icoHeadRight.paintIcon(this, g2, centipede.centXlength[i], centipede.centYlength[i]);
                    }

                    if(i != 0) {
                        centipede.icoBody.paintIcon(this, g2, centipede.centXlength[i], centipede.centYlength[i]);
                    }
                    // Draw cent's hitbox for debugging
                    // g2.drawRect(centipede.centXlength[i], centipede.centYlength[i], 32, 32);

                }
            }
        }


        // Draw centipede2
        if(centipede2 != null)
        {
            if (centipede2.life > 0)
            {
                for (int i = 0; i < centipede2.length; i++)
                {

                    if(i==0 && centipede2.left){
                        centipede2.icoHeadLeft.paintIcon(this, g2, centipede2.centXlength[i], centipede2.centYlength[i]);
                    }
                    if(i==0 && !(centipede2.left)){
                        centipede2.icoHeadRight.paintIcon(this, g2, centipede2.centXlength[i], centipede2.centYlength[i]);
                    }

                    if(i != 0) {
                        centipede2.icoBody.paintIcon(this, g2, centipede2.centXlength[i], centipede2.centYlength[i]);
                    }

                    // Draw cent2's hitbox for debugging
                    // g2.drawRect(centipede2.centXlength[i], centipede2.centYlength[i], 32, 32);

                }
            }
        }

        // Draw centipede3
        if(centipede3 != null) {
            if (centipede3.life > 0)
            {
                for (int i = 0; i < centipede3.length; i++)
                {

                    if(i==0 && centipede3.left){
                        centipede3.icoHeadLeft.paintIcon(this, g2, centipede3.centXlength[i], centipede3.centYlength[i]);
                    }
                    if(i==0 && !(centipede3.left)){
                        centipede3.icoHeadRight.paintIcon(this, g2, centipede3.centXlength[i], centipede3.centYlength[i]);
                    }

                    if(i != 0) {
                        centipede3.icoBody.paintIcon(this, g2, centipede3.centXlength[i], centipede3.centYlength[i]);
                    }

                    // Draw cent3's hitbox for debugging
                    // g2.drawRect(centipede3.centXlength[i], centipede3.centYlength[i], 32, 32);

                }
            }
        }



        // Draw spider
        if(gameState == GameState.GAME_START)
        {
            spider.x = -100;
            spider.y = -100;
        } else
        {
            spider.draw(g2);
        }

        // Draw spider hitbox for debugging
        // g2.drawRect(spider.x, spider.y, 32, 32);

        // Draw bullet
        if (player.shot) {
            g2.setColor(Color.BLACK);
            g2.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        if(gameState == GameState.GAME_START)
        {
            Font font = new Font("Courier New", Font.BOLD, 24);
            g2.setColor(Color.BLACK);
            g2.setFont(font);
            g2.drawString("Bring Mouse Over the Game Board", 20, getHeight()/3-30);
            g2.drawString("To Start the Game", 20, getHeight()/3);
        }
        else
        {
            g2.drawString("Mushroom count:" + tiles.mushCount, 10, 10);
            g2.drawString("Life: " + player.getLife(), getWidth()-50, 10);
            g2.drawString("Score: " + score , getWidth()/2 - 20, 10);
        }

        if (playerLife < 0)
        {
            gameState = GameState.GAME_OVER;
            ImageIcon ico = new ImageIcon("images/GAME_OVER.png");
            Image img = ico.getImage();
            g2.drawImage(img, getWidth()/2-170, getHeight()/3,this);
            timer.stop();

        }
        g2.dispose();

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        timer.start();
        Rectangle playerHitbox = new Rectangle(x, y, 32, 32);
        if(player.shot) {
            bullet.y -= 15;
            for(int i = 0; i < mushRow; i++){
                for(int j = 0; j < mushCol; j++){
                    if(tiles.tile[i][j] > 0){
                        Rectangle mushHitBox = new Rectangle(i*32,j*32, 32, 32);

                        if(bullet.intersects(mushHitBox)){
                            bullet = new Rectangle(0,0,0,0);
                            tiles.gotHit(i,j);
                            if(tiles.tile[i][j] <= 0)
                            {
                                score = score+5;
                            } else {
                                score = score+1;
                            }


                        }

                    }
                }
            }

            if(spider != null) {
                Rectangle spidHitBox = new Rectangle(spider.x, spider.y, 32, 32);
                if(bullet.intersects(spidHitBox)) {
                    bullet = new Rectangle(0,0,0,0);
                    spider.gotHit();
                    if(spider.life <= 0)
                    {
                        score = score + 600;
                        if(rNum < 0.5)
                        {
                            spider = new Spider(-(32*20),(32*15));
                        } else
                        {
                            spider = new Spider(getWidth()+(32*20),(32*15));
                        }
                    } else {
                        score = score + 100;
                    }


                }
            }

            if(centipede != null && centipede.life > 0) {

                for(int i = 0; i < centipede.length; i++)
                {
                    Rectangle centHitBox = new Rectangle(centipede.centXlength[i], centipede.centYlength[i], 32, 32);
                    if(bullet.intersects(centHitBox)) {
                        bullet = new Rectangle(0,0,0,0);
                        centipede.gotHit();
                        if(centipede.life <= 0)
                        {
                            score = score+5;

                            centipede2 = new Centipede(centipede.length/2);
                            centipede3 = new Centipede(centipede.length/2);

                            for (int j = 0; j < centipede2.length; j++)
                            {
                                centipede2.left = true;
                                centipede2.centXlength[j] = centipede.centXlength[j] + 32*j;
                                centipede2.centYlength[j] = centipede.centYlength[j];
                            }
                            for (int j = 0; j < centipede3.length; j++)
                            {
                                centipede3.left = false;
                                centipede3.centXlength[j] = centipede.centXlength[j] - 32*j;
                                centipede3.centYlength[j] = centipede.centYlength[j];
                            }


                        }
                        else
                        {
                            score = score +2;
                        }


                    }
                }

            }

            if(centipede2 != null && centipede2.life > 0) {
                for(int i = 0; i < centipede2.length; i++)
                {
                    Rectangle centHitBox2 = new Rectangle(centipede2.centXlength[i], centipede2.centYlength[i], 32, 32);
                    if(bullet.intersects(centHitBox2)) {
                        bullet = new Rectangle(0,0,0,0);
                        centipede2.gotHit();
                        if(centipede2.life <= 0)
                        {

                            if(centipede3.life <= 0)
                            {
                                score = score+600;
                                centipede = new Centipede(centOriginLength);
                                for (int j = 0; j < centipede.length; j++)
                                {
                                    centipede.left = true;
                                    centipede.centXlength[j] = cx + 32*j;
                                    centipede.centYlength[j] = 0;
                                }


                            } else if (centipede3.life > 0 )
                            {
                                score = score+5;
                            }
                        } else
                        {
                            score = score +2;
                        }


                    }
                }

            }

            if(centipede3 != null && centipede3.life > 0) {

                for(int i = 0; i < centipede3.length; i++)
                {
                    Rectangle centHitBox3 = new Rectangle(centipede3.centXlength[i], centipede3.centYlength[i], 32, 32);
                    if(bullet.intersects(centHitBox3)) {
                        bullet = new Rectangle(0,0,0,0);
                        centipede3.gotHit();
                        if(centipede3.life <= 0)
                        {

                            if(centipede2.life <= 0)
                            {
                                score = score+600;

                                centipede = new Centipede(centOriginLength);
                                for (int j = 0; j < centipede.length; j++)
                                {
                                    centipede.left = true;
                                    centipede.centXlength[j] = cx + 32*j;
                                    centipede.centYlength[j] = 0;
                                }


                            } else if (centipede2.life > 0)
                            {
                                score = score+5;
                            }
                        } else
                        {
                            score = score +2;
                        }

                    }
                }

            }



        }

        if(spider != null) {
            Rectangle spidHitBox = new Rectangle(spider.x, spider.y, 32, 32);
            if (spidHitBox.intersects(playerHitbox))
            {
                timer.setDelay(1000);
                playerLife--;
                if(playerLife >= 0)
                {
                    removeMouseMotionListener(this);
                    player = new Player();
                    player.setLife(playerLife);
                    x = getWidth()/2-32;
                    y = getHeight();
                    if(rNum < 0.5)
                    {
                        spider = new Spider(-(32*20),(32*15));
                    } else
                    {
                        spider = new Spider(getWidth()+(32*20),(32*15));
                    }
                    centipede = new Centipede(centOriginLength);
                    centipede3 = null;
                    centipede2 = null;
                    for (int i = 0; i < centipede.length; i++)
                    {

                        centipede.left = true;
                        centipede.centXlength[i] = cx + 32*i;
                        centipede.centYlength[i] = 0;
                    }


                    for(int i = 0; i < mushRow; i++) {
                        for (int j = 0; j < mushCol; j++) {
                            if(tiles.tile[i][j] < 3 && tiles.tile[i][j] > 0) {
                                tiles.tile[i][j] = 3;
                                score = score + 10;
                            }

                        }
                    }

                    addMouseMotionListener(this);
                }

                timer.setDelay(8);
            }
            if (rNum < 0.25)
            {
                spider.x += 2;
                spider.y += 2;
            } else if (rNum > 0.25 && rNum < 0.5)
            {
                spider.x -= 2;
                spider.y -= 2;
            } else if (rNum > 0.5 && rNum < 0.75)
            {
                spider.x += 2;
                spider.y -= 2;
            } else
            {
                spider.x -= 2;
                spider.y += 2;
            }

            if (spider.x < 0) {
                spider.x += 2 ;
            }
            else if (spider.x > this.getWidth() - 32) {
                spider.x -= 2;
            }
            if (spider.y < 0) {
                spider.y += 2;
            }
            else if (spider.y > this.getHeight() - 32) {
                spider.y -= 2;
            }

        }

        if(centipede != null) {
            for(int i = 0; i < centipede.length; i++)
            {
                Rectangle centHitBox = new Rectangle(centipede.centXlength[i], centipede.centYlength[i], 32, 32);
                if(centHitBox.intersects(playerHitbox)) {
                    playerLife --;
                    if(playerLife >= 0)
                    {

                        player = new Player();
                        player.setLife(playerLife);
                        x = getWidth()/2-32;
                        y = getHeight();
                        if(rNum < 0.5)
                        {
                            spider = new Spider(-(32*20),(32*15));
                        } else
                        {
                            spider = new Spider(getWidth()+(32*20),(32*15));
                        }

                        centipede = new Centipede(centOriginLength);
                        centipede2 = null;
                        centipede3 = null;
                        for (int j = 0; j < centipede.length; j++)
                        {
                            centipede.left = true;
                            centipede.centXlength[j] = cx + 32*i;
                            centipede.centYlength[j] = 0;
                        }

                        for(int j = 0; j < mushRow; j++) {
                            for (int k = 0; k < mushCol; k++) {
                                if(tiles.tile[j][k] < 3 && tiles.tile[j][k] > 0) {
                                    tiles.tile[j][k] = 3;
                                    score = score + 10;
                                }

                            }
                        }

                        addMouseMotionListener(this);
                    }

                }
            }

        }






        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                setYDirection(-2);
                break;

            case KeyEvent.VK_DOWN:
                setYDirection(2);
                break;

            case KeyEvent.VK_LEFT:
                setXDirection(-2);
                break;

            case KeyEvent.VK_RIGHT:
                setXDirection(2);
                break;

            case KeyEvent.VK_SPACE:
                spacePressed = true;
                if (bullet == null) {
                    player.fireReady = true;
                }

                if (player.fireReady) {
                    bullet = new Rectangle(x + 16, y - 1, 3, 5);
                    player.shot = true;
                    player.fireReady = false;
                    playSound(new File("sound/MISSILE_SHOT.wav"), 1.0f, false);
                }

                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                setYDirection(0);
                break;

            case KeyEvent.VK_DOWN:
                setYDirection(0);
                break;

            case KeyEvent.VK_LEFT:
                setXDirection(0);
                break;

            case KeyEvent.VK_RIGHT:
                setXDirection(0);
                break;

            case KeyEvent.VK_SPACE:
                player.fireReady = false;
                if (bullet.y <= -5) {
                    player.shot = false;
                    player.fireReady = true;
                }
                break;
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        mouseClicked = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX() - 16;
        y = e.getY() - 16;
        if (bullet == null) {
            player.fireReady = true;
        }

        if (player.fireReady) {
            bullet = new Rectangle(x + 16, y - 1, 3, 5);

            player.shot = true;
            player.fireReady = false;
            playSound(new File("sound/MISSILE_SHOT.wav"), 1.0f, false);
        }

        e.consume();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (bullet.y < -5) {
            player.shot = false;
            player.fireReady = true;
        }


    }

    @Override
    public void mouseEntered(MouseEvent e) {

        gameState = GameState.GAME_PLAYING;
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX() - 16;
        y = e.getY() - 16;

        mouseDragged = true;
        e.consume();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX() - 16;
        y = e.getY() - 16;

        e.consume();
    }


    @Override
    public void run() {
        try {
            while (true) {
                move();
                Thread.sleep(5);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void playSound(File file, float vol, boolean repeat){
        try{
            final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    // TODO Auto-generated method stub
                    if(event.getType()== LineEvent.Type.STOP){

                        clip.close();
                    }
                }
            });
            FloatControl volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(vol);
            clip.start();
            if(repeat)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch(Exception e){
            e.printStackTrace();
        }
    }





}
