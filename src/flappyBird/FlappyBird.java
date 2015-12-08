package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
	
	public final int WIDTH = 800;
	public final int HEIGHT = 800;

	public static FlappyBird flappyBird;
	public Renderer renderer;
	public Rectangle bird;
	public ArrayList<Rectangle> columns;
	public Random rand;
	public int ticks;
	public int yMotion;
	public int score;
	public boolean gameOver;
	public boolean started;

	public FlappyBird() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);
		renderer = new Renderer();
		rand = new Random();
		
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.add(renderer);
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setVisible(true);
		jframe.setTitle("Flappy Bird");
		
		gameSetup(true);
		
		timer.start();
	}
	
	public void gameSetup(boolean newColumns) {
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		
		yMotion = 0;
		score = 0;
		gameOver = false;
		
		if (newColumns) {
			columns = new ArrayList<Rectangle>();
		} else {
			columns.clear();
		}
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
	}
	
	public void jump() {
		if (gameOver) {
			gameSetup(false);
		}
		
		if (!started) {
			started = true;
		} else if (!gameOver) {
			if (yMotion > 0) {
				yMotion = 0;
			} 
			
			yMotion -= 10;
		}
	}
	
	public void addColumn(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		
		if (start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void repaint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.ORANGE);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);
		
		g.setColor(Color.GREEN);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);
		
		g.setColor(Color.RED);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for (Rectangle column : columns) {
			paintColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		
		
		if (!started) {
			g.drawString("Click to Start!", 75, HEIGHT / 2 - 50);
		}
		
		if (gameOver) {
			g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
		}
		
		if (!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		int speed = 10;

		ticks++;
		
		if (started) {
						
			for (int i = 0; i < columns.size(); i++){
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			
			for (int i = 0; i < columns.size(); i++){
				Rectangle column = columns.get(i);
				if (column.x + column.width < 0) {
					columns.remove(column);
					
					if (column.y == 0) {
						addColumn(false);
					}
				}
			}
			
			bird.y += yMotion;
			
			for (Rectangle column : columns) {
				
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
					score++;
				}
				
				if (column.intersects(bird)) {
					gameOver = true;
					
					if (bird.x <= column.x) {
						bird.x = column.x - bird.width;
					} else {
						if (column.y != 0) {
							bird.y = column.y - bird.height;
						} else if (bird.y < column.height) {
							bird.y = column.height;
						}
					}
				}
			}
			
			if (bird.y > HEIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}
			
			if (bird.y + yMotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			}
		}
		
		renderer.repaint();
	}
	
	public static FlappyBird getFlappyBird() {
		return flappyBird;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		jump();
	}

	public static void main(String[] args) {
		flappyBird = new FlappyBird();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
