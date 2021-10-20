import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameMain extends JFrame implements ActionListener,KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -925518549680460124L;
	
	private bomber bomberman;
	private enemy enemy,enemy_2,enemy_3,enemy_4;
	
	private bomb bomb_ex,bomb_ex_down,bomb_ex_up,bomb_ex_left,bomb_ex_right;
	private walls bricks;
	private int[][] map = { {0,0,1,0,1,1,1,1,1,0,1,0},{0,2,1,0,2,0,1,2,1,0,2,0},{0,1,0,0,0,0,1,1,1,1,0,1},{0,0,0,0,2,1,0,2,1,1,0,1},{0,1,0,0,1,0,0,1,1,1,0,1},{0,2,0,1,2,1,0,2,1,1,2,1},{0,1,0,1,1,1,0,1,1,1,0,1} };
	private int[][]bombermanPosition = new int [7][12];
	private int flag=0; //flag to identify when the game is over 
	//labels to show the graphics
	private JLabel bombermanLabel,enemyLabel, enemy2Label, enemy3Label, enemy4Label, bombLabel,bombLabel_up,bombLabel_down,bombLabel_right,bombLabel_left;
	private JButton startButton;
	private JLabel[][] brickLabel = new JLabel[map.length][map[0].length];
	private ImageIcon bombermanImage, bombermanDownImage,bricksImage, bricksImage2,emptyImage, bombImage, enemyImage;
	private JLabel  scoreboardLabel,playerLabel,scoresLabel;
	private String player_name="";
	private int points, enemy1_down,enemy2_down,enemy3_down,enemy4_down; 
	//container to hold graphics
	private Container content;
	private Connection conn = null;
	private Statement stmt = null;
	
	//GUI setup
	public GameMain() {
		super("Bomberman");
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		bombermanPosition[0][0]=1;
		/////////////////////////////////////////////////Maze///////////////////////////////////////////////////////
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
		
		
		/////////////////////////////////////////////////Bomberman///////////////////////////////////////////////////////
		bomberman = new bomber();
		bombermanLabel = new JLabel();
		bombermanImage = new ImageIcon( getClass().getResource( bomberman.getFilename() ) );
		bombermanLabel.setIcon(bombermanImage); 
		bombermanLabel.setSize(bomberman.getWidth(),bomberman.getHeight());	
		bombermanLabel.setVisible(bomberman.getVisible());
		
		bombermanDownImage = new ImageIcon( getClass().getResource( "smallninja2.png" ) );
		
		/////////////////////////////////////////////////Bomb///////////////////////////////////////////////////////
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
		
		/////////////////////////////////////////////////Enemy///////////////////////////////////////////////////////
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
		enemyLabel.setVisible(enemy.getVisible());
		
		enemy_2 = new enemy ();
		enemy2Label = new JLabel();
		enemy2Label.setIcon(enemyImage); 
		enemy2Label.setSize(enemy.getWidth(),enemy.getHeight());	
		
		enemy_2.setEnemyLabel(enemy2Label);
		enemy_2.setBomberman(bomberman);
		enemy_2.setBombermanLabel(bombermanLabel);
		enemy_2.setBomb(bomb_ex);
		enemy_2.setBombLabel(bombLabel);
		enemy_2.setBombEx(bomb_ex_right,bomb_ex_left,bomb_ex_up, bomb_ex_down);
		enemy2Label.setVisible(enemy_2.getVisible());
		
		enemy_3 = new enemy (false);
		enemy3Label = new JLabel();
		enemy3Label.setIcon(enemyImage); 
		enemy3Label.setSize(enemy.getWidth(),enemy.getHeight());	
		
		enemy_3.setEnemyLabel(enemy3Label);
		enemy_3.setBomberman(bomberman);
		enemy_3.setBombermanLabel(bombermanLabel);
		enemy_3.setBomb(bomb_ex);
		enemy_3.setBombLabel(bombLabel);
		enemy_3.setBombEx(bomb_ex_right,bomb_ex_left,bomb_ex_up, bomb_ex_down);
		enemy3Label.setVisible(enemy_3.getVisible());
		
		enemy_4 = new enemy (false);
		enemy4Label = new JLabel();
		enemy4Label.setIcon(enemyImage); 
		enemy4Label.setSize(enemy.getWidth(),enemy.getHeight());	
		
		enemy_4.setEnemyLabel(enemy4Label);
		enemy_4.setBomberman(bomberman);
		enemy_4.setBombermanLabel(bombermanLabel);
		enemy_4.setBomb(bomb_ex);
		enemy_4.setBombLabel(bombLabel);
		enemy_4.setBombEx(bomb_ex_right,bomb_ex_left,bomb_ex_up, bomb_ex_down);
		enemy4Label.setVisible(enemy_4.getVisible());
		
		/////////////////////////////////////////////////Interface///////////////////////////////////////////////////////
		startButton = new JButton("Start Game");
		startButton.setSize(100,100);
		startButton.setLocation(GameProperties.SCREEN_WIDTH-150,GameProperties.SCREEN_HEIGHT-150);
		add(startButton);
		startButton.addActionListener(this);
		startButton.setFocusable(false);
		enemy.setAnimationButton(startButton);
		enemy_2.setAnimationButton(startButton);
		enemy_3.setAnimationButton(startButton);
		enemy_4.setAnimationButton(startButton);
		
		this.addKeyListener(this);
		
		scoreboardLabel = new JLabel("<html><body><center>CURRENT<br>SCORE</center></body></html>");
		scoreboardLabel.setSize(100,50);
		scoreboardLabel.setLocation(GameProperties.SCREEN_WIDTH-130,GameProperties.SCREEN_HEIGHT-700);
		
		playerLabel = new JLabel(player_name);
		playerLabel.setSize(100,50);
		playerLabel.setLocation(GameProperties.SCREEN_WIDTH-150,GameProperties.SCREEN_HEIGHT-600);
		
		scoresLabel = new JLabel();
		scoresLabel.setSize(100,50);
		scoresLabel.setLocation(GameProperties.SCREEN_WIDTH-100,GameProperties.SCREEN_HEIGHT-600);
		
		add(playerLabel);
		add(scoreboardLabel);
		add(scoresLabel);
		
		content = getContentPane();
		content.setBackground(Color.white);
		setLayout(null);
		
		bomberman.setCoordinates(25, 0);
		enemy.setCoordinates(125, 300);
		enemy_2.setCoordinates(425, 200);
		enemy_3.setCoordinates(625, 500);
		enemy_4.setCoordinates(1025, 300);
		bomb_ex.setCoordinates(0, 0);
		
		//adding labels 		
		add(bombermanLabel);
		add(enemyLabel);
		add(enemy2Label);
		add(enemy3Label);
		add(enemy4Label);
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
		enemy2Label.setLocation(enemy_2.getX(),enemy_2.getY());
		enemy3Label.setLocation(enemy_3.getX(),enemy_3.getY());
		enemy3Label.setLocation(enemy_4.getX(),enemy_4.getY());
		
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
					
					// checking if there is an unbreakable wall besides the bomb, if not then there is an explosion
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
			
			//Erase walls in case there was an explosion		
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
							
							
							//check if bomberman was caught in the explosions 
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
					
					//hide enemy if they are caught by explosion
					if (enemy.getEnemyAlive()==false) { 
						enemy.hide();
						if (enemy1_down==0) {
							points = points + 1000;
							scoresLabel.setText(String.valueOf(points));
							scorePoints(points);
							displayAllScores();
							enemy1_down=1;
						}
					}
					
					if (enemy_2.getEnemyAlive()==false) {
						enemy_2.hide();
						if (enemy2_down==0) {
							points = points + 1000;
							scoresLabel.setText(String.valueOf(points));
							scorePoints(points);
							enemy2_down=1;
						}
					}
					
					if (enemy_3.getEnemyAlive()==false) {
						enemy_3.hide();
						if (enemy3_down==0) {
							points = points + 1000;
							scoresLabel.setText(String.valueOf(points));
							scorePoints(points);
							enemy3_down=1;
						}
					}
					
					if (enemy_4.getEnemyAlive()==false) {
						enemy_4.hide();
						if (enemy4_down==0) {
							points = points + 1000;
							scoresLabel.setText(String.valueOf(points));
							scorePoints(points);
							enemy4_down=1;
						}
					}
					
					//send message if bomberman dies
					if (flag==1) {
						JOptionPane.showMessageDialog(null, "Game Over!");
					}
					
					//message if all enemies are down
					if(enemy.getEnemyAlive()==false && enemy_2.getEnemyAlive()==false && enemy_3.getEnemyAlive()==false && enemy_4.getEnemyAlive()==false) {
						JOptionPane.showMessageDialog(null, "YOU WON!");
						bomberman.hide();
						
					}
					
					enemyLabel.setVisible(enemy.getVisible());
					enemy2Label.setVisible(enemy_2.getVisible());
					enemy3Label.setVisible(enemy_3.getVisible());
					enemy4Label.setVisible(enemy_4.getVisible());
					
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
			
			points = 0;
			enemy1_down=0;
			enemy2_down=0;
			enemy3_down=0;
			enemy4_down=0;
			
			player_name = JOptionPane.showInputDialog("Enter the player's name");
			scoresLabel.setText(String.valueOf(points));
			////////////////////////////Save player's name into database
			try {
				Class.forName("org.sqlite.JDBC");
				System.out.println("Database Driver Loaded");
				
				String dbURL = "jdbc:sqlite:product.db";
				conn = DriverManager.getConnection(dbURL);
				
				if (conn != null) {
					System.out.println("Connected to database");
					conn.setAutoCommit(false);
					
					stmt = conn.createStatement();
										
					//String sql = "DROP TABLE SCORES";
					//stmt.executeUpdate(sql);
					//conn.commit();
					
					String sql = "CREATE TABLE IF NOT EXISTS SCORES " +
					             "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
							     " NAME TEXT NOT NULL, " + 
					             " SCORE INT NOT NULL) ";
					
					stmt.executeUpdate(sql);
					conn.commit();
					System.out.println("Table Created Successfully");
					
					sql = "INSERT INTO SCORES (NAME, SCORE) VALUES " + 
	                        "('"+ player_name+"', 0)";
					stmt.executeUpdate(sql);
					conn.commit();
										
					conn.close();
				}
				
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			//Display player name into scoreboard
			playerLabel.setText(player_name);
			
			map = new int[][] { {0,0,1,0,1,1,1,1,1,0,1,0},{0,2,1,0,2,0,1,2,1,0,2,0},{0,1,0,0,0,0,1,1,1,1,0,1},{0,0,0,0,2,1,0,2,1,1,0,1},{0,1,0,0,1,0,0,1,1,1,0,1},{0,2,0,1,2,1,0,2,1,1,2,1},{0,1,0,1,1,1,0,1,1,1,0,1} };
			
			//setting values to the bricks label according to the map
			for (int i=0; i< map.length ; i++) {
				for (int j=0; j< map[i].length ; j++) {
					if( map[i][j]==1) {
						brickLabel[i][j].setIcon(bricksImage);
						}else if( map[i][j]==2) {
							brickLabel[i][j].setIcon(bricksImage2);
						} else {
								brickLabel[i][j].setIcon(emptyImage);
						}
				}
			}
			
			bomberman.show();
			bombermanLabel.setVisible(bomberman.getVisible());
			bombermanLabel.setIcon(bombermanImage);
			bomberman.setCoordinates(25, 0);
			bombermanPosition = new int [7][12];
			bombermanPosition[0][0] = 1; 
			bombermanLabel.setLocation(bomberman.getX(), bomberman.getY());
			flag=0;
			
			enemyLabel.setIcon(enemyImage); 
			enemy2Label.setIcon(enemyImage); 
			enemy3Label.setIcon(enemyImage); 
			enemy4Label.setIcon(enemyImage); 
			
			enemy.setEnemyLabel(enemyLabel);
			enemy_2.setEnemyLabel(enemy2Label);
			enemy_3.setEnemyLabel(enemy3Label);
			enemy_4.setEnemyLabel(enemy4Label);
			
			enemy.show();
			enemy.setEnemyAlive(true);
			enemyLabel.setVisible(enemy.getVisible());
			
			enemy_2.show();
			enemy_2.setEnemyAlive(true);
			enemy2Label.setVisible(enemy_2.getVisible());
			
			enemy_3.show();
			enemy_3.setEnemyAlive(true);
			enemy3Label.setVisible(enemy_3.getVisible());
			
			enemy_4.show();
			enemy_4.setEnemyAlive(true);
			enemy4Label.setVisible(enemy_4.getVisible());

			startButton.setText("Re-start");
			
			if (!enemy.getMoving()) { //check if enemies are moving
				//start moving
				enemy.moveEnemy();
			}
			
			if (!enemy_2.getMoving()) { //check if enemies are moving
				//start moving
				enemy_2.moveEnemy();
			}
			
			if (!enemy_3.getMoving()) { //check if enemies are moving
				//start moving
				enemy_3.moveEnemy();
			}
			
			if (!enemy_4.getMoving()) { //check if enemies are moving
				//start moving
				enemy_4.moveEnemy();
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

	public void scorePoints(int current_score) {
		try {
			Class.forName("org.sqlite.JDBC");
			String dbURL = "jdbc:sqlite:product.db";
			conn = DriverManager.getConnection(dbURL);
			
			if (conn != null) {
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				String sql ="UPDATE SCORES SET SCORE = "+current_score+" WHERE NAME='"+player_name +"'"; 
                stmt.executeUpdate(sql);
				conn.commit();
				conn.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void displayAllScores() {
		
		String[] id_array = new String[1] ;
		String[] name_array = new String[1];
		String[] score_array = new String[1];
		String[] value = new String[1];
		int counter= 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			String dbURL = "jdbc:sqlite:product.db";
			conn = DriverManager.getConnection(dbURL);
			
			if (conn != null) {
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				String sql ="SELECT * FROM SCORES ORDER BY SCORE DESC"; 
                ResultSet rs = stmt.executeQuery(sql);
				while ( rs.next() ) {
					counter=counter+1;
				}
				rs.close();
			}
			
			if (conn != null) {
				System.out.println("Connected to database");
				conn.setAutoCommit(false);
				
				stmt = conn.createStatement();
				
				String sql ="SELECT * FROM SCORES ORDER BY SCORE DESC"; 
                
				ResultSet rs = stmt.executeQuery(sql);
				
				id_array = new String [counter];
				name_array = new String [counter];
				score_array = new String [counter];
				value = new String [counter];
				counter = 0;
				
				while ( rs.next() ) {
					
					int id = rs.getInt("id");
					String name = rs.getString("name");
					int score = rs.getInt("score");
					System.out.println("ID = " + id);
					id_array[counter] = String.valueOf(id);
					name_array[counter] = name;
					score_array[counter] = String.valueOf(score);
					counter=counter+1;
				}
				
				rs.close();
				conn.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i=0; i<5; i++) {
			System.out.println(id_array[i]);
			System.out.println(name_array[i]);
			System.out.println(score_array[i]);
		}
		
		
		
		
		//JOptionPane.showMessageDialog(null, "YOU WON!");
	}
	
		
}
