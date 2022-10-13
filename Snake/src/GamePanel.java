import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 90;
	private final int[] x = new int[GAME_UNITS];
	private final int[] y = new int[GAME_UNITS];
	private final String FONT_NAME = "Arial";
	private int bodyParts = 6;
	private int applesEaten;
	private int appleX;
	private int appleY;
	private char direction = 'R'; // U - Up , D - Down , L - Left, R - Right;
	private boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel (){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
	
		for(int i =0; i < bodyParts; i++) {
			if (i == 0) {
				g.setColor(Color.GREEN);
				g.fillRect(x[0],y[0], UNIT_SIZE, UNIT_SIZE);
			} else {
				g.setColor(new Color(45,180,0));
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
		g.setColor(Color.yellow);
		g.setFont(new Font(FONT_NAME , Font.BOLD, 30));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Pontos: " + applesEaten , (SCREEN_WIDTH - metrics.stringWidth("Pontos: " + applesEaten))/2, g.getFont().getSize());;
	
		} else {
			gameOver(g);
		}
	}
		
	public void newApple() {
		appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	} 
	
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;

			break;
		default:
			break;
		}	
	}
	
	public void checkApple() {
		if(x[0] == appleX && y[0] == appleY) {
			bodyParts++;
			applesEaten++;
			newApple();	
		}
	}
	
	public void checkCollisions() {
		for(int i = bodyParts; i > 0; i--) {
			if(x[0] == x[i] && y[0] == y[i]) {
				running = false;
				break;
			}
		}
		
		if(x[0] < 0 || x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		if(y[0] < 0 || y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font(FONT_NAME, Font.BOLD, 40));
		FontMetrics fontXP = getFontMetrics(g.getFont());
		g.drawString("Pontos: " + applesEaten,(SCREEN_WIDTH - fontXP.stringWidth("Pontos: " + applesEaten))/2, g.getFont().getSize());
		g.setColor(Color.red);
		g.setFont(new Font(FONT_NAME, Font.BOLD, 75));
		FontMetrics finalFont = getFontMetrics(g.getFont());
		g.drawString("Game Over! ",(SCREEN_WIDTH - finalFont.stringWidth("Game Over!"))/2, SCREEN_HEIGHT/2);
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
				default:
					break;
				}		
			}	
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();		
		}
		repaint();
	}

}
