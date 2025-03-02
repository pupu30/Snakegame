import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 25;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int DELAY = 120; // Adjusted for smoother gameplay
    
    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private int score = 0;
    private Random random;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                changeDirection(e);
            }
        });
        
        random = new Random();
        startGame();
    }

    public void startGame() {
        snake = new ArrayList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2)); // Centering the initial snake position
        generateFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void generateFood() {
        int x, y;
        do {
            x = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
            y = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
            food = new Point(x, y);
        } while (snake.contains(food)); // Ensure food does not appear on the snake
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw food
            g.setColor(Color.RED);
            g.fillOval(food.x, food.y, TILE_SIZE, TILE_SIZE);

            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                g.fillRect(snake.get(i).x, snake.get(i).y, TILE_SIZE, TILE_SIZE);
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }

    public void move() {
        if (!running) return;
        
        Point head = new Point(snake.get(0));
        
        switch (direction) {
            case 'U': head.y -= TILE_SIZE; break;
            case 'D': head.y += TILE_SIZE; break;
            case 'L': head.x -= TILE_SIZE; break;
            case 'R': head.x += TILE_SIZE; break;
        }
        
        // Check collisions
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT || snake.contains(head)) {
            running = false;
            timer.stop();
            return;
        }
        
        // Eat food
        if (head.equals(food)) {
            score += 10;
            generateFood();
        } else {
            snake.remove(snake.size() - 1); // Move forward
        }
        
        snake.add(0, head);
    }

    public void changeDirection(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH / 3, HEIGHT / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, WIDTH / 2 - 40, HEIGHT / 2 + 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }
}
