import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameMain extends JFrame implements ActionListener,KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -925518549680460124L;
	
	private bomber bomberman;
	private enemy enemy,enemy2;
	private bomb bomb_ex,bomb_ex_down,bomb_ex_up,bomb_ex_left,bomb_ex_right;
	private walls bricks;
	private int[][] map = { {0,0,1,0,1,1,1,1,1,0,1,0},{0,2,1,0,2,0,1,2,1,0,2,0},{0,1,0,0,0,0,1,1,1,1,0,1},{0,0,0,0,2,1,0,2,1,1,0,1},{0,1,0,0,1,0,0,1,1,1,0,1},{0,2,0,1,2,1,0,2,1,1,2,1},{0,1,0,1,1,1,0,1,1,1,0,1} };
	private int[][]bombermanPosition = {{1,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0}};
	private int flag=0; //flag to identify when the game is over 
	//labels to show the graphics
	private JLabel bombermanLabel,enemyLabel, enemy2Label, bombLabel,bombLabel_up,bombLabel_down,bombLabel_right,bombLabel_left;
	private JButton startButton;
	private JLabel[][] brickLabel = new JLabel[map.length][map[0].length];
	private ImageIcon bombermanImage, bombermanDownImage,bricksImage, bricksImage2,emptyImage, bombImage, enemyImage;
	
	//container to hold graphics
	private Container content;
	
	//GUI setup
	public GameMain() {
		super("Bomberman");
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		
		bricks = new walls();
		bricksImage = new ImageIcon( getClass().getResource( bricks.getFilename() ) );
		bricksImage2 = new ImageIcon( getClass().getResource( "walls2.png" ) );
		emptyImage = new ImageIcon( getClass().getResource( "white.png" ) );
		
		//setting values to the bricks label according to the map
				for (int i=0; i< map.length ; i++) {
					
					for (int j=0; j< map[i].length ; j++) {
						if( map[i][j]==1) {
							brickLabel[i][j] = new JLabel();
							brickLabel[i][j].setIcon(bricksImage);
							brickLabel[i][j].setSize(bricks.getWidth(),bricks.getHeight());
							}else if( map[i][j]==2) {
								brickLabel[i][j] = new JLabel();
								brickLabel[i][j].setIcon(bricksImage2);
								brickLabel[i][j].setSize(bricks.getWidth(),bricks.getHeight());
							} else {
									brickLabel[i][j] = new JLabel();
									brickLabel[i][j].setIcon(emptyImage);
									brickLabel[i][j].setSize(bricks.getWidth(),bricks.getHeight());	
								}
						if( j==0 ) {
							bricks.setX(bricks.getX());
							brickLabel[i][j].setLocation(bricks.getX(), bricks.getY());
						}else {
							bricks.setX(bricks.getX()+100);
							brickLabel[i][j].setLocation(bricks.getX(), bricks.getY());
							}
					}
					bricks.setX(0);
					bricks.setY(bricks.getY()+100);
				}
		
		bomberman = new bomber();
		bombermanLabel = new JLabel();
		bombermanImage = new ImageIcon( getClass().getResource( bomberman.getFilename() ) );
		bombermanLabel.setIcon(bombermanImage); 
		bombermanLabel.setSize(bomberman.getWidth(),bomberman.getHeight());	
		
		bombermanDownImage = new ImageIcon( getClass().getResource( "smallninja2.png" ) );
		
		bomb_ex = new bomb();
		bombLabel = new JLabel();
		bombLabel.setIcon(emptyImage); 
		bombLabel.setSize(bomb_ex.getWidth(),bomb_ex.getHeight());
		
		bomb_ex_up = new bomb();
		bombLabel_up = new JLabel();
		bombLabel_up.setIcon(emptyImage); 
		bombLabel_up.setSize(bomb_ex_up.getWidth(),bomb_ex_up.getHeight());
		
		bomb_ex_down = new bomb();
		bombLabel_down = new JLabel();
		bombLabel_down.setIcon(emptyImage); 
		bombLabel_down.setSize(bomb_ex_down.getWidth(),bomb_ex_down.getHeight());
		
		bomb_ex_right = new bomb();
		bombLabel_right = new JLabel();
		bombLabel_right.setIcon(emptyImage); 
		bombLabel_right.setSize(bomb_ex_right.getWidth(),bomb_ex_right.getHeight());
		
		bomb_ex_left = new bomb();
		bombLabel_left = new JLabel();
		bombLabel_left.setIcon(emptyImage); 
		bombLabel_left.setSize(bomb_ex_left.getWidth(),bomb_ex_left.getHeight());
		
		enemy = new enemy ();
		enemyLabel = new JLabel();
		enemyImage = new ImageIcon( getClass().getResource( enemy.getFilename() ) );
		enemyLabel.setIcon(enemyImage); 
		enemyLabel.setSize(enemy.getWidth(),enemy.getHeight());	
		
		enemy.setEnemyLabel(enemyLabel); 
		enemy.setBomberman(bomberman); //collision with bomberman
		enemy.setBombermanLabel(bombermanLabel);
		enemy.setBomb(bomb_ex); //collision for initial bomb
		enemy.setBombLabel(bombLabel);
		enemy.setBombEx(bomb_ex_right,bomb_ex_left,bomb_ex_up, bomb_ex_down);//for collision with the explosion
		
		enemy2 = new enemy ();
		enemy2Label = new JLabel();
		enemy2Label.setIcon(enemyImage); 
		enemy2Label.setSize(enemy.getWidth(),enemy.getHeight());	
		
		enemy2.setEnemyLabel(enemy2Label);
		enemy2.setBomberman(bomberman);
		enemy2.setBombermanLabel(bombermanLabel);
		enemy2.setBomb(bomb_ex);
		enemy2.setBombLabel(bombLabel);
		enemy2.setBombEx(bomb_ex_right,bomb_ex_left,bomb_ex_up, bomb_ex_down);
		
		startButton = new JButton("Start");
		startButton.setSize(100,50);
		startButton.setLocation(GameProperties.SCREEN_WIDTH-150,GameProperties.SCREEN_HEIGHT-150);
		add(startButton);
		startButton.addActionListener(this);
		startButton.setFocusable(false);
		enemy.setAnimationButton(startButton);
		enemy2.setAnimationButton(startButton);
		
		this.addKeyListener(this);
		
				
		content = getContentPane();
		content.setBackground(Color.gray);
		setLayout(null);
		
		bomberman.setCoordinates(25, 0);
		enemy.setCoordinates(125, 300);
		enemy2.setCoordinates(425, 200);
		bomb_ex.setCoordinates(0, 0);
		
				
		add(bombermanLabel);
		add(enemyLabel);
		add(enemy2Label);
		add(bombLabel);
		add(bombLabel_up);
		add(bombLabel_down);
		add(bombLabel_left);
		add(bombLabel_right);
		
		
		//adding bricksLabel
		for (int i=0; i< map.length ; i++) {
			for (int j=0; j< map[i].length ; j++) {
				add(brickLabel[i][j]);
			}
		}
		
		//update the label position to match the stored values
		
		bombermanLabel.setLocation(bomberman.getX(), bomberman.getY());
		
		enemyLabel.setLocation(enemy.getX(),enemy.getY());
		enemy2Label.setLocation(enemy2.getX(),enemy2.getY());
		
		bombLabel.setLocation(bomb_ex.getX(), bomb_ex.getY());
		bombLabel_up.setLocation(bomb_ex_up.getX(), bomb_ex_up.getY());
		bombLabel_down.setLocation(bomb_ex_down.getX(), bomb_ex_down.getY());
		bombLabel_right.setLocation(bomb_ex_right.getX(), bomb_ex_right.getY());
		bombLabel_left.setLocation(bomb_ex_left.getX(), bomb_ex_left.getY());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main (String[] args) {
		GameMain myGame = new GameMain();
		myGame.setVisible(true);
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			
			//Set the boundaries for bomberman
			if ((bomberman.getY()+200)<GameProperties.SCREEN_HEIGHT) { 
				
				loop :  // Loop to handle the position of bomberman and positions available in the map
				for (int i=0; i< map.length ; i++) {
					for (int j=0; j< map[i].length ; j++) {
					
						if(bombermanPosition[i][j]==1) { // check for current bomberman position
							if(map[i+1][j]==0) {    // check if the next space is available
								bombermanPosition[i][j]=0;		//change current position to 0
								bombermanPosition[i+1][j]=1;	//the new bomberman position
								bomberman.setY(bomberman.getY()+GameProperties.CHARACTER_STEP);
								bombermanLabel.setLocation(bomberman.getX(), bomberman.getY());
								break loop;// Once the position for bomberman has changed then finish loop to keep bomberman from moving further
							}
						}
					}
				}
			}
			
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_UP) {
			
			//Set the boundaries for bomberman
			if ((bomberman.getY())>0) { 
				
				loop :
					for (int i=0; i< map.length ; i++) {
						for (int j=0; j< map[i].length ; j++) {
						
							if(bombermanPosition[i][j]==1) {
								if(map[i-1][j]==0) {
									bombermanPosition[i][j]=0;
									bombermanPosition[i-1][j]=1;
									bomberman.setY(bomberman.getY()-GameProperties.CHARACTER_STEP);
									bombermanLabel.setLocation(bomberman.getX(), bomberman.getY());
									break loop;
								}
							}
						}
					}	
			}
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			
			//Set the boundaries for bomberman
			if ((bomberman.getX()+125)<GameProperties.SCREEN_WIDTH) { 
				
					loop :
					for (int i=0; i< map.length ; i++) {
						for (int j=0; j< map[i].length ; j++) {
						
							if(bombermanPosition[i][j]==1) {
								if(map[i][j+1]==0) {
									bombermanPosition[i][j]=0;
									bombermanPosition[i][j+1]=1;
									bomberman.setX(bomberman.getX()+GameProperties.CHARACTER_STEP);
									bombermanLabel.setLocation(bomberman.getX(), bomberman.getY());
									break loop;
								}
							}
						}
					}
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			
			//Set the boundaries for bomberman
			if ((bomberman.getX())-25>0) {
				
				loop :
					for (int i=0; i< map.length ; i++) {
						for (int j=0; j< map[i].length ; j++) {
						
							if(bombermanPosition[i][j]==1) {
								if(map[i][j-1]==0) {
									bombermanPosition[i][j]=0;
									bombermanPosition[i][j-1]=1;
									bomberman.setX(bomberman.getX()-GameProperties.CHARACTER_STEP);
									bombermanLabel.setLocation(bomberman.getX(), bomberman.getY());
									break loop;
								}
							}
						}
					}
			}
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			
			Timer timer = new Timer();
			
			TimerTask task = new TimerTask(){
				public void run() {
					
					
					for (int i=0; i< map.length ; i++) {
						for (int j=0; j< map[i].length ; j++) {
							
							//bomb explosions check spaces beside the bomb
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel.getX()-25)) && (brickLabel[i][j].getLocation().getY() == (bombLabel.getY()-100))
									&& map[i][j]!=2) {
								bombLabel_up.setIcon(bombImage); 
								bombLabel_up.setLocation(bombLabel.getX(), bombLabel.getY()-100);
								bomb_ex_up.setCoordinates(bombLabel.getX(),bombLabel.getY()-100);
								}
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel.getX()-25)) && (brickLabel[i][j].getLocation().getY() == (bombLabel.getY()+100))
									&& map[i][j]!=2) {
								bombLabel_down.setIcon(bombImage); 
								bombLabel_down.setLocation(bombLabel.getX(), bombLabel.getY()+100);
								bomb_ex_down.setCoordinates(bombLabel.getX(),bombLabel.getY()+100);
								}
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel.getX()+75)) && (brickLabel[i][j].getLocation().getY() == (bombLabel.getY()))
									&& map[i][j]!=2) {
								bombLabel_right.setIcon(bombImage);
								bombLabel_right.setLocation(bombLabel.getX()+100, bombLabel.getY());
								bomb_ex_right.setCoordinates(bombLabel.getX()+100,bombLabel.getY());	
								}
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel.getX()-125)) && (brickLabel[i][j].getLocation().getY() == (bombLabel.getY()))
								&& map[i][j]!=2) {
								bombLabel_left.setIcon(bombImage);
								bombLabel_left.setLocation(bombLabel.getX()-100,bombLabel.getY() );
								bomb_ex_left.setCoordinates(bombLabel.getX()-100,bombLabel.getY());
							}
							
						}
					}
					
					for (int i=0; i< map.length ; i++) {
						for (int j=0; j< map[i].length ; j++) {
							
							//bomb explosions check spaces beside the bomb
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_right.getLocation().getX()-25)) 
									&& (brickLabel[i][j].getLocation().getY() == (bombLabel_right.getLocation().getY())) 
									&& map[i][j]!=2) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_left.getLocation().getX()-25)) 
									&& (brickLabel[i][j].getLocation().getY() == (bombLabel_left.getLocation().getY()))
									&& map[i][j]!=2) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_up.getLocation().getX()-25)) 
									&& (brickLabel[i][j].getLocation().getY() == (bombLabel_up.getLocation().getY()))
									&& map[i][j]!=2) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_down.getLocation().getX()-25)) 
									&& (brickLabel[i][j].getLocation().getY() == (bombLabel_down.getLocation().getY()))
									&& map[i][j]!=2 ) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							
							//check if there is collision with bomberman and bomb explosion
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel.getLocation().getX()-25)) 
									&& (bombermanLabel.getLocation().getY() == (bombLabel.getLocation().getY()))) {
								bombermanLabel.setIcon(bombermanDownImage); 
								flag=1;
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_right.getLocation().getX()-25)) 
									&& (bombermanLabel.getLocation().getY() == (bombLabel_right.getLocation().getY()))) {
								bombermanLabel.setIcon(bombermanDownImage); 
								flag=1;
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_left.getLocation().getX()-25)) 
									&& (bombermanLabel.getLocation().getY() == (bombLabel_left.getLocation().getY()))) {
								bombermanLabel.setIcon(bombermanDownImage);
								flag=1;
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_up.getLocation().getX()-25)) 
									&& (bombermanLabel.getLocation().getY() == (bombLabel_up.getLocation().getY()))) {
								bombermanLabel.setIcon(bombermanDownImage);
								flag=1;
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_down.getLocation().getX()-25)) 
									&& (bombermanLabel.getLocation().getY() == (bombLabel_down.getLocation().getY()))) {
								bombermanLabel.setIcon(bombermanDownImage);
								flag=1;
							}
						}
					}
					
					
					
				}
			};
			
			TimerTask task2 = new TimerTask(){
				public void run() {
					
					bomb_ex.setCoordinates(0,0);
					bomb_ex_right.setCoordinates(0,0);
					bomb_ex_down.setCoordinates(0,0);
					bomb_ex_up.setCoordinates(0,0);
					bomb_ex_left.setCoordinates(0,0);
					
					bombLabel.setLocation(bomb_ex.getX(), bomb_ex.getY());
					bombLabel_up.setLocation(bomb_ex_up.getX(), bomb_ex_up.getY());
					bombLabel_down.setLocation(bomb_ex_down.getX(), bomb_ex_down.getY());
					bombLabel_right.setLocation(bomb_ex_right.getX(), bomb_ex_right.getY());
					bombLabel_left.setLocation(bomb_ex_left.getX(), bomb_ex_left.getY());
					
					bombLabel.setIcon(emptyImage); 
					bombLabel_up.setIcon(emptyImage);
					bombLabel_down.setIcon(emptyImage);
					bombLabel_right.setIcon(emptyImage);
					bombLabel_left.setIcon(emptyImage);
					
					if (enemy.getAlive()==false) {
						enemyLabel.setIcon(emptyImage);
					}
					
					if (enemy2.getAlive()==false) {
						enemy2Label.setIcon(emptyImage);
					}
										
					if (flag==1) {
						JOptionPane.showMessageDialog(null, "GAME OVER!");
					}
				}
			};
			
			//Process: 
			//Change image for the initial bomb 
			bombImage = new ImageIcon( getClass().getResource( bomb_ex.getFilename() ) );
			bombLabel.setIcon(bombImage);
			//Place the bomb in the same place the bomberman is located
			bombLabel.setLocation(bomberman.getX(), bomberman.getY());
			//giving values X and Y so it can recognize collision with rectangles
			bomb_ex.setCoordinates(bomberman.getX(),bomberman.getY());
			
			
			// explosion and collisions with walls and bomberman
			timer.schedule(task, 1000);
			
			// reseting the bomb image
			timer.schedule(task2, 2000);
			
			
			
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == startButton) {
			
			if (enemy.getMoving()) {
				//enemy is moving, stop
				enemy.setMoving(false);
				startButton.setText("Start");
			} else {
				//enemy is not moving. start
				startButton.setText("Re-start");
				enemy.moveEnemy();
			}
			
			if (enemy2.getMoving()) {
				//enemy is moving, stop
				enemy2.setMoving(false);
				startButton.setText("Start");
			} else {
				//enemy is not moving. start
				startButton.setText("Re-start");
				enemy2.moveEnemy();
			}
			
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	


}
