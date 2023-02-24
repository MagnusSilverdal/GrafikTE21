import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class GraphicsWithLotsOfStuff extends Canvas implements Runnable{
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    int WIDTH = 600;
    int HEIGHT = 400;

    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();
    ArrayList<Integer> vx = new ArrayList<Integer>();
    ArrayList<Integer> vy = new ArrayList<Integer>();

    /*    int x = 10;
    int y = 10;
    int vx = 2;
    int vy = 2;
*/
    int size = 50;

    public GraphicsWithLotsOfStuff() {
        setSize(600,400);
        JFrame frame = new JFrame();
        frame.add(this);
        this.addMouseListener(new MyMouseListener());
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
        //g.fillOval(x,y,size,size);
        for (int i = 0 ; i < x.size() ; i++) {
            g.fillOval(x.get(i),y.get(i),size,size);
        }
    }

    private void update() {
        for (int i = 0 ; i < x.size() ; i++) {
            if (x.get(i) >= WIDTH - size || x.get(i) <= 0)
                vx.set(i, -vx.get(i));
            if (y.get(i) >= HEIGHT - size || y.get(i) <= 0)
                vy.set(i, -vy.get(i));

            x.set(i,x.get(i)+vx.get(i));
            y.set(i,y.get(i)+vy.get(i));
        }

/*        if (x >= WIDTH - size || x <= 0)
            vx = -vx;
        if (y >= HEIGHT - size || y <= 0)
            vy = -vy;
        x += vx;
        y += vy;*/
    }

    public static void main(String[] args) {
        GraphicsWithLotsOfStuff minGrafik = new GraphicsWithLotsOfStuff();
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
        double ns = 1000000000.0 / 50.0;
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

    private class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            x.add(e.getX());
            y.add(e.getY());
            vx.add(x.size());
            vy.add(x.size());
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
    /*        System.out.println("Mouse pressed");
            x.add(e.getX());
            y.add(e.getY());
            vx.add(x.size());
            vy.add(x.size());
      */  }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}


