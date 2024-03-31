import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;

public class Maze implements ActionListener {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Maze();
			}
		});
		
	}

	final static int SIZE = 40; // grid size
	static int panW = 600;
	static int panH = 600;

	int[][] board = new int[SIZE][SIZE];
	int[][] map;
	JLabel message = new JLabel();	

	JFrame frame;
	JPanel panel;
	DrawingPanel dPanel;
	boolean init = true;

	int boxW, boxH; // size of one box/square

	MyKL mainKL = new MyKL();

	PlayerRect player = new PlayerRect((int)((panW/SIZE + 0.5)/4), (int)((panH/SIZE + 0.5)/4), (int)((panW/SIZE + 0.5)/2), (int)((panH/SIZE + 0.5)/2));

	Timer timer;

	Maze() {
		init();
		createGUI();

		timer = new Timer(10, this);
		timer.start();
	}

	void init() { // reset the game
		board = new int[SIZE][SIZE];
		message.setText("Epic Maze");
	}

	void createGUI() {
		//Set up window
		frame = new JFrame("Maze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		message.setFont(new Font("Dialog", Font.BOLD, 18));
		message.setHorizontalAlignment(JLabel.CENTER);

		// Drawing panel for game
		dPanel = new DrawingPanel();
		dPanel.setPreferredSize(new Dimension(panW,panH));

		// Panel outside for messages
		panel = new JPanel(new MigLayout("wrap, insets 20"));

		panel.add(message, "align center");
		panel.add(dPanel, "align center");

		panel.setBackground(new Color(74, 212, 118));

		panel.add(dPanel);
		frame.add(panel);
		frame.pack();

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	// For Timer
	@Override
	public void actionPerformed(ActionEvent e) {
		//get keys and move player
		if ((mainKL.isKeyDown('A') || mainKL.isKeyDown(KeyEvent.VK_LEFT))  && player.x > 0) player.x -= player.speed;
		if ((mainKL.isKeyDown('D') || mainKL.isKeyDown(KeyEvent.VK_RIGHT))  && player.x+player.width < panW-2) player.x += player.speed;
		if ((mainKL.isKeyDown('W') || mainKL.isKeyDown(KeyEvent.VK_UP))  && player.y > 0) player.y -= player.speed;
		if ((mainKL.isKeyDown('S') || mainKL.isKeyDown(KeyEvent.VK_DOWN))  && player.y+player.height < panH-2) player.y += player.speed;

		dPanel.repaint();
		checkCollision();
	}

	public void checkCollision() {

		//get map location of player
		//top left corner
		int rTL = player.y * map.length / panH;
		int cTL = player.x * map.length / panW;

		//top right corner
		int rTR = player.y * map.length / panH;
		int cTR = (player.x + player.width) * map.length / panW;

		//bottom left corner
		int rBL = (player.y + player.height) * map.length / panH;
		int cBL = player.x * map.length / panW;

		//bottom right corner
		int rBR = (player.y + player.height) * map.length / panH;
		int cBR = (player.x + player.width) * map.length / panW;

		//Top side - Checks the corners on the top side.
		if ((map[rTL][cTL] == 1 && map[rTR][cTR] == 1) || ((map[rTL][cTL] == 0 || map[rTL][cTL] == 2 || map[rTL][cTL] == 3) && map[rTR][cTR] == 1) || (map[rTL][cTL] == 1 && (map[rTR][cTR] == 0 || map[rTR][cTR] == 2 || map[rTR][cTR] == 3))) {
			player.y += player.speed;
		}
		//Right Side - Checks the corners on the right side.
		if ((map[rTR][cTR] == 1 && map[rBR][cBR] == 1) || ((map[rTR][cTR] == 0 || map[rTR][cTR] == 2 || map[rTR][cTR] == 3) && map[rBR][cBR] == 1) || (map[rTR][cTR] == 1 && (map[rBR][cBR] == 0 || map[rBR][cBR] == 2 || map[rBR][cBR] == 3))) {
			player.x -= player.speed;
		}
		//Bottom Side - Checks the corners on the bottom side.
		if ((map[rBL][cBL] == 1 && map[rBR][cBR] == 1) || ((map[rBL][cBL] == 0 || map[rBL][cBL] == 2 || map[rBL][cBL] == 3) && map[rBR][cBR] == 1) || (map[rBL][cBL] == 1 && (map[rBR][cBR] == 0 || map[rBR][cBR] == 2 || map[rBR][cBR] == 3))) {
			player.y -= player.speed;
		}
		//Left Side - Checks the corners on the left side.
		if ((map[rTL][cTL] == 1 && map[rBL][cBL] == 1) || ((map[rTL][cTL] == 0 || map[rTL][cTL] == 2 || map[rTL][cTL] == 3) && map[rBL][cBL] == 1) || (map[rTL][cTL] == 1 && (map[rBL][cBL] == 0 || map[rBL][cBL] == 2 || map[rBL][cBL] == 3))) {
			player.x += player.speed;
		}
		//Top Left Corner - checks 3 corners for diagonal movement.
		if (map[rTL][cTL] == 1 && map[rTR][cTR] == 1 && map[rBL][cBL] == 1) {
			player.y += player.speed;
			player.x += player.speed;
		}
		//Top Right Corner - checks 3 corners for diagonal movement.
		if (map[rTL][cTL] == 1 && map[rTR][cTR] == 1 && map[rBR][cBR] == 1) {
			player.y += player.speed;
			player.x -= player.speed;
		}
		//Bottom Left Corner - checks 3 corners for diagonal movement.
		if (map[rTL][cTL] == 1 && map[rBL][cBL] == 1 && map[rBR][cBR] == 1) {
			player.y -= player.speed;
			player.x += player.speed;
		}
		//Bottom Right Corner - checks 3 corners for diagonal movement.
		if ((map[rTR][cTR] == 1 && map[rBR][cBR] == 1 && map[rBL][cBL] == 1)) {
			player.y -= player.speed;
			player.x -= player.speed;
		}
		
		//Win - if all corners are in win square.
		if(map[rTL][cTL] == 2 && map[rTR][cTR] == 2 && map[rBL][cBL] == 2 && map[rBR][cBR] == 2) {
			message.setText("You Win!");
			
			timer.stop();
		}
		
		//debugging
		//frame.setTitle("TL: " + rTL + "," + cTL + " TR: " + rTR + "," + cTR +" BL: " + rBL + "," + cBL + " BR: " + rBR + "," + cBR);
	}

	class DrawingPanel extends JPanel {
		int panW, panH; //size of panel

		DrawingPanel() {
			this.setBackground(new Color(240,240,240));

			this.addKeyListener(mainKL); 
			this.setFocusable(true);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			initGrid();
			loadMap();

			// draw grid
			g.setColor(Color.GRAY);
			for (int i = 0; i<SIZE; i++) {
				g2.drawLine(boxW*i, 0, boxW*i, panH);
				g2.drawLine(0, boxH*i, panW, boxH*i);
			}

			for (int r = 0; r < map.length; r++) {
				for (int c = 0; c < map.length; c++) {
					//draw start
					if (map[r][c] == 3) {
						g2.setColor(Color.ORANGE);
						g2.fillRect(c*boxW, r*boxH, boxW, boxH);

						if (init) {
							player.x = (int) (((panW/SIZE + 0.5)/4) + (c*boxW));
							player.y = (int) (((panH/SIZE + 0.5)/4) + (r*boxH));
						}
					}
					//draw walls
					if (map[r][c] == 1) {
						g2.setColor(Color.BLACK);
						g2.fillRect(c*boxW, r*boxH, boxW, boxH);
					}
					//draw win
					if (map[r][c] == 2) {
						g2.setColor(Color.GREEN);
						g2.fillRect(c*boxW, r*boxH, boxW, boxH);
					}
					//draw background
					if (map[r][c] == 0) {
						g2.setColor(Color.WHITE);
						g2.fillRect(c*boxW, r*boxH, boxW, boxH);
					}
				}
			}

			g.setColor(Color.RED);
			g.fillRect(player.x, player.y, player.width, player.height);
			init = false;
		}

		void initGrid() {
			panW = this.getSize().width;
			panH = this.getSize().height;
			boxW = (int) (panW/SIZE + 0.5);
			boxH = (int) (panH/SIZE + 0.5);
		}

		void loadMap() {
			//map orignally from Dennis
			map = new int[][] {
				{1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
				{0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1},
				{1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1},
				{0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0},
				{0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0},
				{0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0},
				{0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0},
				{0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 2, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0},
				{1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1},
				{1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0},
				{0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0},
				{0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0},
				{0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0},
				{1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 3, 1, 1, 1, 0, 0, 0, 1, 0},
				{0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0},
				{1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0},
				{0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0},
				{1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0},
				{1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0},
				{0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0},
				{0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0},
				{1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1},
				{0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
				{1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0},
				{0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0},
				{0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1},
				{0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1},
				{1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0},
				{1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0},
				{1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0},
				{1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1},
				{0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0},
				{0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0},
				{0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
				{0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0},
				{0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1},
				{1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0}
				};
			}

		}

		class MyKL implements KeyListener {
			private boolean[] keys = new boolean[256]; //hopefully default=false

			boolean isKeyDown(int n) {
				return keys[n];
			}
			@Override
			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;
			}		
			@Override
			public void keyReleased(KeyEvent e) {
				keys[e.getKeyCode()] = false;
			}
			@Override
			public void keyTyped(KeyEvent e) {} //slow!!!
		}

	}
