import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean isMoving;
    private Timer timer;

    public SnakeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        food = new Point(10, 10);
        direction = KeyEvent.VK_RIGHT; // Initial direction is right
        isMoving = true;

        timer = new Timer(100, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) direction = key;
                if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) direction = key;
                if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) direction = key;
                if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) direction = key;
            }
        });

        setFocusable(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (isMoving) {
            move();
            checkCollision();
            checkFood();
            repaint();
        }
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead = (Point) head.clone();

        if (direction == KeyEvent.VK_LEFT) newHead.translate(-1, 0);
        if (direction == KeyEvent.VK_RIGHT) newHead.translate(1, 0);
        if (direction == KeyEvent.VK_UP) newHead.translate(0, -1);
        if (direction == KeyEvent.VK_DOWN) newHead.translate(0, 1);

        snake.add(0, newHead);
        snake.remove(snake.size() - 1);
    }

    public void checkCollision() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            isMoving = false;
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over");
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isMoving = false;
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over");
                return;
            }
        }
    }

    public void checkFood() {
        Point head = snake.get(0);

        if (head.equals(food)) {
            snake.add(food);
            generateFood();
        }
    }

    public void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt(20);
        int y = rand.nextInt(20);
        food = new Point(x, y);

        for (Point bodyPart : snake) {
            if (bodyPart.equals(food)) {
                generateFood(); // Regenerate if food spawns on the snake's body
                return;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GREEN);
        for (Point bodyPart : snake) {
            g.fillRect(bodyPart.x * 20, bodyPart.y * 20, 20, 20);
        }

        g.setColor(Color.RED);
        g.fillRect(food.x * 20, food.y * 20, 20, 20);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
