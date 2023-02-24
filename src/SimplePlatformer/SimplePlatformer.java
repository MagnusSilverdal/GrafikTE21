package SimplePlatformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class SimplePlatformer extends Canvas implements Runnable{
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    int x = 0;
    int y = 0;
    int vx = 0;
    int vy = 0;

    public SimplePlatformer() {
        setSize(600,400);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        //this.addMouseMotionListener(new MyMouseMotionListener());
        //this.addMouseListener(new MyMouseListener());
        //requestFocus();
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
        g.clearRect(0,0,getWidth(),getHeight());
        g.setColor(Color.RED);
        g.fillRect(x,y,100,100);
    }

    private void update() {
        x += vx;
        y += vy;
    }

    public static void main(String[] args) {
        SimplePlatformer minGrafik = new SimplePlatformer();
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
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }

    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                vx = -3;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                vx = 3;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                vy = -3;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                vy = 3;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                vx = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                vx = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                vy = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                vy = 0;
            }
        }
    }
}

