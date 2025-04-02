import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int DOT_SIZE = 10;
    private final int DELAY = 140;

    private final LinkedList<Point> snake;
    private Point food;
    private int direction;
    private boolean running;

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != DOWN) direction = UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != UP) direction = DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != RIGHT) direction = LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != LEFT) direction = RIGHT;
                        break;
                }
            }
        });

        snake = new LinkedList<>();
        direction = RIGHT;
        running = false;

        Timer timer = new Timer(DELAY, this);
        timer.start();
        startGame();
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        spawnFood();
        running = true;
    }

    private void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / DOT_SIZE);
        int y = rand.nextInt(HEIGHT / DOT_SIZE);
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(food.x * DOT_SIZE, food.y * DOT_SIZE, DOT_SIZE, DOT_SIZE);

            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * DOT_SIZE, p.y * DOT_SIZE, DOT_SIZE, DOT_SIZE);
            }
        } else {
            showGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Game Over! Press R to Restart", WIDTH / 4, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFood();
        }
        repaint();
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

        snake.addFirst(newHead);
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        // Check wall collision
        if (head.x < 0 || head.x >= WIDTH / DOT_SIZE || head.y < 0 || head.y >= HEIGHT / DOT_SIZE) {
            running = false;
        }

        // Check self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
            }
        }
    }

    private void checkFood() {
        Point head = snake.getFirst();
        if (head.equals(food)) {
            spawnFood();
        } else {
            snake.removeLast(); // Remove the tail
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!game.running && e.getKeyCode() == KeyEvent.VK_R) {
                    game.startGame();
                }
            }
        });
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }
}