import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

@SuppressWarnings("serial")
public class RotationPlanets extends Canvas implements Runnable {

    private Thread thread;
    private JFrame frame;
    public final static int WIDTH = 800, HEIGHT = 600;
    private static String title = "Planet rotation";
    private static boolean running = false;
    private BufferedImage sunImage;
    private BufferedImage earthImage;
    private BufferedImage uranImage;

    public RotationPlanets() {
        this.frame = new JFrame();
        this.frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadImages();
    }

    private void loadImages() {
        try {
            sunImage = ImageIO.read(new File("./res/sun.png"));
            earthImage = ImageIO.read(new File("./res/earth.png"));
            uranImage = ImageIO.read(new File("./res/urano.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RotationPlanets display = new RotationPlanets();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);
        display.start();
    }

    public synchronized void start() {
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            this.thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1e9 / 30;
        double delta = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                update();
                delta--;
                render();
            }
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title);
            }
        }
        stop();
    }

    // random point on circle;
    int xEarth = 387;
    int yEarth = 129;
    int xUran = 387;
    int yUran = 48;
    //circle center and radius
    double a = 367,b = 250;
    double rEarth;
    double rUran;
    double tEarth = 0.01;
    double tUran = 0.01;
    int timer = 1000 / 50;

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        //Background
        g.setColor(Color.black);
        g.fillRect(-1, -1, WIDTH, HEIGHT);

        //Draw here!
        g.setFont(new Font("Comic Sans MS", 10, 20));

        g.drawImage(sunImage, (int)a, (int)b,null);
        g.drawImage(earthImage, xEarth, yEarth,null);
        g.drawImage(uranImage, xUran, yUran,null);
        getCircleCoordinatesEarth();
        getCircleCoordinatesUran();
        g.dispose();

        bs.show();
        timer--;
        if(timer <= 0) {
            timer = 1000 / 50;
        }
    }
    public void update() {}

    public void getCircleCoordinatesEarth() {
        rEarth = Math.sqrt(Math.pow(xEarth - a,2) + Math.pow(yEarth - b,2));
        xEarth = (int) (a + rEarth * Math.cos(tEarth));
        yEarth = (int) (b + rEarth * Math.sin(tEarth));
        tEarth = tEarth < 3.14 * 2 ? tEarth + 0.1 : 0.01;
    }

    public void getCircleCoordinatesUran() {
        rUran = Math.sqrt(Math.pow(xUran - a,2) + Math.pow(yUran - b,2));
        xUran = (int) (a + rUran * Math.cos(tUran));
        yUran = (int) (b + rUran * Math.sin(tUran));
        tUran = tUran < 3.14 * 2 ? tUran + 0.1 : 0.01;
    }

//    public static void main(String[] args) throws IOException {
//
//
//        //ДЛЯ ПОЯВЛЕНИЯ ОКОШКА С НУЖНЫМИ НАМ ПАРАМЕТРАМИ РАЗМЕРА
//        JFrame frame = new JFrame();
//        frame.setVisible(true); // окошко
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //для его закрытия
//        frame.setTitle("головные планеты");
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setBounds(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2, WIDTH, WIDTH + 30);
//        frame.getContentPane().setLayout(null);//выравнивание, чтобы координаты объектов в дальнейшем считались от верхнего левого угла
//        frame.getContentPane().setBackground(Color.BLACK);//фон окна
//
//
//        //ДОБАВЛЕНИЕ КАРТИНКИ И УСТАНОВКА ПОЯВЛЕНИЯ
//        BufferedImage screamHead = ImageIO.read(new File("./res/sun.png"));
//        BufferedImage sunnyHead = ImageIO.read(new File("./res/earth.png"));
//        BufferedImage screamHead2 = ImageIO.read(new File("./res/urano.jpg"));//скачиваем картинку
//
//        int screamHeadWidth = screamHead.getWidth(); //запоминаем ее размер (понадобится, когда будем двигать JLabel)
//        int screamHeadHeight = screamHead.getHeight();//запоминаем ее размер (понадобится, когда будем двигать JLabel)
//
//        int sunnyHeadWidth = sunnyHead.getWidth();
//        int sunnyHeadHeight = sunnyHead.getHeight();
//
//        int screamHead2Width = screamHead2.getWidth();
//        int screamHead2Height = screamHead2.getHeight();
//
//        JLabel wIcon = new JLabel(new ImageIcon(screamHead));//создаем объект с картинкой, который будем размещать и двигать
//        JLabel wIcon2 = new JLabel(new ImageIcon(sunnyHead));
//        JLabel wIcon3 = new JLabel(new ImageIcon(screamHead2));
//
//        int startX = WIDTH / 2 - screamHeadWidth / 2, startY = HEIGHT / 20;//задаём координаты для движущейся картинки
//        int startX2 = WIDTH /2 - screamHead2Width / 2, startY2 = HEIGHT / 20; //задаём координаты для движущейся картинки
//        int newStartX = WIDTH / 2 - sunnyHeadWidth / 2, newStartY = HEIGHT / 2 - sunnyHeadHeight / 2; //координаты для картинки по центру
//        wIcon.setBounds(newStartX, newStartY, sunnyHeadWidth, sunnyHeadHeight);//устанавливаем начальное положение картинки, которая по центру
//        wIcon2.setBounds(startX,startY,screamHeadWidth,screamHeadHeight);//координаты для головы, которая будет кружить
//        wIcon3.setBounds(startX2,startY2,screamHead2Width,screamHead2Height);
//
//        frame.add(wIcon);//добавляем кружащуюся картинку
//        frame.add(wIcon2);//картинка по центру
//        frame.add(wIcon3);//добавляем кружащуюся картинку
//
//
//        int radius1=Math.max(WIDTH-100, HEIGHT-100)/2-Math.max(sunnyHeadWidth, sunnyHeadHeight);// находим оптимальный радиус окружности
//        int radius2 = Math.max(WIDTH+100, HEIGHT+100)/2-Math.max(sunnyHeadWidth, sunnyHeadHeight);// находим оптимальный радиус окружности
//        int deltaX=WIDTH/2-sunnyHeadWidth/2, deltaY=HEIGHT/2-sunnyHeadHeight/2;//константы для смещения картинки, чтобы не рассчитывать их постоянно
//
//        //ДЕЛАЕМ БЕСКОНЕЧНЫЙ ЦИКЛ ПЕРЕМЕЩЕНИЯ КАРТИНКИ
//        for (int t = 0; t < 360;) {
//            try {//сперва работает таймер
//                Thread.sleep(20);
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
//            wIcon2.setBounds((int) (radius1*Math.cos(t*Math.PI/180))+deltaX, (int) (radius1*Math.sin(t*Math.PI/180))+deltaY, sunnyHeadWidth, sunnyHeadHeight);//перемещаем картинку в нужную точку, которую вычисляем по формуле параметрических уравнений линий, приводя градусы t к радианам
//            wIcon2.repaint();//перерисовываем картинку
//            wIcon3.setBounds((int) (radius2*Math.cos(t*Math.PI/180))+deltaX, (int) (radius2*Math.sin(t*Math.PI/180))+deltaY, sunnyHeadWidth, sunnyHeadHeight);
//            wIcon3.repaint();
//            t=t==359?0:t+1;//этой формулой обеспечиваем постоянный цикл и обнуление t при 359
//        }
//    }
}
