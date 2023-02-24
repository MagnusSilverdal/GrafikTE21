import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Grafik extends Canvas implements Runnable{
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    private int sunX = -20;
    private int sunVX = 5;

    private BufferedImage mario;
    private int marioX = 100;
    private int marioY = 100;
    private int marioVX = 0;
    private int marioVY = 0;

    public Grafik() {
        try {
            mario = ImageIO.read(new File("supermario.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSize(600,400);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Rita ut den nya bilden
        draw(g);

        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        drawBackGround(g);
        drawSun(sunX, g);
        drawHouse(new Color(0xff0000),100,100,g);
        drawTree(400,250,g);
        g.drawImage(mario, marioX, marioY, mario.getWidth()/4, mario.getHeight()/4, null);
    }

    public void drawSun(int x, Graphics g) {
        g.setColor(new Color(0xFFCC00));
        g.fillOval(x, 50,20,20);
    }

    private void drawTree(int x, int y, Graphics g) {
        g.setColor(new Color(0x965629));
        g.fillRect(x-10,y-150,20,150);
        g.setColor(new Color(0x2D942D));
        g.fillOval(x-20,y-170,60,60);
        g.fillOval(x-60,y-190,60,60);
        g.fillOval(x-20,y-200,60,60);
        g.fillOval(x,y-190,60,60);
        g.setColor(new Color(0xFF0000));
        int num = (int)(Math.random()*10);
        for (int i = 0 ; i < num ; i++) {
            g.fillOval((int)(x+120*Math.random()-60),(int)(y+40*Math.random()-20-170),10,10);
        }
    }

    private void drawBackGround(Graphics g) {
        g.setColor(new Color(0x0088ff));
        g.fillRect(0,0,600,400);
        g.setColor(new Color(0x8D8D8D));
        int[] mountainX = {0,100,210,250,300,350,400};
        int[] mountainY = {200,100,175,125,200,100,200};
        g.fillPolygon(mountainX,mountainY,7);
        g.setColor(new Color(0x11aa11));
        g.fillRect(0,200,600,200);
    }

    public void drawHouse(Color c, int x, int y, Graphics g) {
        g.setColor(c);
        g.fillRect(x,y,200,150);
        g.setColor(new Color(0x000000));
        int[] roofX = {x-10,x+100,x+210};
        int[] roofY = {y,y-50,y};
        g.fillPolygon(roofX,roofY,3);
        g.setColor(new Color(0x00ffff));
        g.fillRect(x+25,y+25,50,50);
        g.setColor(new Color(0x995333));
        g.fillRect(x+100,y+25,75,125);
    }

    public static void main(String[] args) {
        Grafik minGrafik = new Grafik();
        minGrafik.start();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        double ns = 1000000000.0 / 25.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med uppdaterad data
                render();
                delta--;
            }
        }
        stop();
    }

    private void update() {
        marioX += marioVX;
        marioY += marioVY;

        sunX = sunX + sunVX;

        if (sunX > getWidth()) {
            sunVX = -5;
        }
        if (sunX < -20) {
            sunVX = 5;
        }
    }

    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                marioVX = -3;
            }
            if (e.getKeyChar() == 'd') {
                marioVX = 3;
            }
            if (e.getKeyChar() == 'w') {
                marioVY = -3;
            }
            if (e.getKeyChar() == 's') {
                marioVY = 3;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                marioVX = 0;
            }
            if (e.getKeyChar() == 'd') {
                marioVX = 0;
            }
            if (e.getKeyChar() == 'w') {
                marioVY = 0;
            }
            if (e.getKeyChar() == 's') {
                marioVY = 0;
            }
        }
    }
}

