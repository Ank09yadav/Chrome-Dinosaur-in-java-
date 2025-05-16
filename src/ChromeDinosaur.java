import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener {

    int boaradwidth = 900;
    int boardheight = 400;

    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurjumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;
    Image rsImage;

    // dinosaur
    int dinosaurwidth = 88;
    int dinosaurheight = 100;
    int dinosaurx = 30;
    int dinosaury = boardheight - dinosaurheight;

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    Block dinosaur;
    Block reStartImage;
    // cactus
    int cactus1width = 34;
    int cactus2width = 69;
    int cactus3width = 102;

    int cactusheight = 70;
    int cactusX = 900;
    int cactusY = boardheight - cactusheight;
    ArrayList<Block> cactusArray;

    // restart
    int rswidth = 100;
    int rsheight = 100;
    int rsX = 450;
    int rsY = 150;

    // Game Physics
    int velocityX = -12; // cactus moving left speed
    int velocityY = 0; // dinosaur jump speed
    int gravity = 1;

    // Game Over
    boolean gameover = false;
    int score = 0;

    Timer GameLoop;
    Timer placeCactusTimer;

    public ChromeDinosaur() {
        setPreferredSize(new Dimension(boaradwidth, boardheight));
        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./IMG/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./IMG/dino-dead.png")).getImage();
        dinosaurjumpImg = new ImageIcon(getClass().getResource("./IMG/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./IMG/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./IMG/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./IMG/cactus3.png")).getImage();
        rsImage = new ImageIcon(getClass().getResource("./IMG/reset.png")).getImage();

        // dinosaur
        dinosaur = new Block(dinosaurx, dinosaury, dinosaurwidth, dinosaurheight, dinosaurImg);
        // catus
        cactusArray = new ArrayList<Block>();
        // reset Image
        reStartImage = new Block(rsX, rsY, rswidth, rsheight, rsImage);

        // Game loop
        GameLoop = new Timer(1000 / 60, this);
        GameLoop.start();

        if (score < 500) {
            placeCactusTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    placeCactus();
                }

            });
        }
        if (score > 500) {
            placeCactusTimer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    placeCactus();
                }

            });
        }
        if (score > 1000) {
            placeCactusTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    placeCactus();
                }

            });
        }
        placeCactusTimer.start();
    }

    void placeCactus() {
        if (gameover) {
            return;
        }
        double placeCactusChance = Math.random();// 0-0.9999
        if (placeCactusChance > .90) {
            Block cactus = new Block(cactusX, cactusY, cactus3width, cactusheight, cactus3Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > .70) {
            Block cactus = new Block(cactusX, cactusY, cactus2width, cactusheight, cactus2Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > .50) {
            Block cactus = new Block(cactusX, cactusY, cactus1width, cactusheight, cactus1Img);
            cactusArray.add(cactus);
        }
        if (cactusArray.size() > 10) {
            cactusArray.remove(0);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // dinosaur
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        // cactus
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        // score
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if (gameover) {
            g.drawString("Game Over :" + String.valueOf(score), 15, 30);
            g.drawImage(reStartImage.img, reStartImage.x, reStartImage.y, reStartImage.width, reStartImage.height,
                    null);

        } else {
            g.drawString(String.valueOf(score), 15, 30);
        }
    }

    public void move() {
        // dinosaur
        velocityY += gravity;
        dinosaur.y += velocityY;
        if (dinosaur.y > dinosaury) {
            dinosaur.y = dinosaury;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        // cacuts
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX;

            if (collision(dinosaur, cactus)) {
                gameover = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }
        // score
        score++;

    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && // a's top left corner doen't reach b's top corner
                a.x + a.width > b.x && // a's top right corner passes b's left corner
                a.y < b.y + b.height && // a's left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y; // a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        move();
        repaint();
        if (gameover) {
            placeCactusTimer.stop();
            GameLoop.stop();
        }
    }

    // When a key press
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // System.out.println("jump..");
            if (dinosaur.y == dinosaury) {
                velocityY = -20;
                dinosaur.img = dinosaurjumpImg;
            }

            if (gameover) {
                dinosaur.y = dinosaury;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameover = false;
                GameLoop.start();
                placeCactusTimer.start();
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
