import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameMain extends JFrame implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -925518549680460124L;
	
	private bomber bomberman;
	private bomber2 bombermanDown;
	private bomb bomb_ex,bomb_ex_down,bomb_ex_up,bomb_ex_left,bomb_ex_right;
	private walls bricks;
	private walls2 bricks2;
	private int[][] map = { {0,0,1,0,1,1,1,1},{0,1,1,0,0,1,1,1},{0,1,0,0,0,1,1,1},{0,1,0,0,0,1,1,1},{0,1,0,0,1,0,1,1},{0,1,0,1,1,1,1,1} };
	private int[][]bombermanPosition = {{1,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	private int flag=0;
	//labels to show the graphics
	private JLabel bombermanLabel,bombLabel,bombLabel_up,bombLabel_down,bombLabel_right,bombLabel_left;
	private JLabel[][] brickLabel = new JLabel[map.length][map[0].length];
	private ImageIcon bombermanImage, bricksImage, emptyImage, bombImage;
	
	//button to control 
	private JButton HideTardisButton;
	
	//container to hold graphics
	private Container content;
	
	//GUI setup
	public GameMain() {
		super("Bomberman");
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		
		bricks = new walls();
		bricks2 = new walls2();
		
		bomberman = new bomber();
		bombermanLabel = new JLabel();
		bombermanImage = new ImageIcon( getClass().getResource( bomberman.getFilename() ) );
		bombermanLabel.setIcon(bombermanImage); 
		bombermanLabel.setSize(bomberman.getWidth(),bomberman.getHeight());	
		
		bomb_ex = new bomb();
		bombLabel = new JLabel();
		bombImage = new ImageIcon( getClass().getResource( bricks2.getFilename() ) ); // The image is bricks2 to make the bomb invisible
		bombLabel.setIcon(bombImage); 
		bombLabel.setSize(bomb_ex.getWidth(),bomb_ex.getHeight());
		
		bomb_ex_up = new bomb();
		bombLabel_up = new JLabel();
		bombLabel_up.setIcon(bombImage); 
		bombLabel_up.setSize(bomb_ex_up.getWidth(),bomb_ex_up.getHeight());
		
		bomb_ex_down = new bomb();
		bombLabel_down = new JLabel();
		bombLabel_down.setIcon(bombImage); 
		bombLabel_down.setSize(bomb_ex_down.getWidth(),bomb_ex_down.getHeight());
		
		bomb_ex_right = new bomb();
		bombLabel_right = new JLabel();
		bombLabel_right.setIcon(bombImage); 
		bombLabel_right.setSize(bomb_ex_right.getWidth(),bomb_ex_right.getHeight());
		
		bomb_ex_left = new bomb();
		bombLabel_left = new JLabel();
		bombLabel_left.setIcon(bombImage); 
		bombLabel_left.setSize(bomb_ex_left.getWidth(),bomb_ex_left.getHeight());
		
		this.addKeyListener(this);
		
		bricksImage = new ImageIcon( getClass().getResource( bricks.getFilename() ) );
		emptyImage = new ImageIcon( getClass().getResource( bricks2.getFilename() ) );
		
		for (int i=0; i< map.length ; i++) {
			
			for (int j=0; j< map[i].length ; j++) {
				if( map[i][j]==1) {
					brickLabel[i][j] = new JLabel();
					brickLabel[i][j].setIcon(bricksImage);
					brickLabel[i][j].setSize(bricks.getWidth(),bricks.getHeight());
					}else {
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
		
		
		
		content = getContentPane();
		content.setBackground(Color.gray);
		setLayout(null);
		
		bomberman.setX(25);
		bomberman.setY(0);
		
		bomb_ex.setX(0);
		bomb_ex.setY(0);
		
		
		add(bombermanLabel);
		add(bombLabel);
		add(bombLabel_up);
		add(bombLabel_down);
		add(bombLabel_left);
		add(bombLabel_right);
		
		for (int i=0; i< map.length ; i++) {
			for (int j=0; j< map[i].length ; j++) {
				add(brickLabel[i][j]);
			}
		}
		
		//update the label position to match the stored values
		
		bombermanLabel.setLocation(bomberman.getX(), bomberman.getY());
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
					
						if(bombermanPosition[i][j]==1) { // check for bomberman position
							if(map[i+1][j]==0) {    // check if the next space is available
								bombermanPosition[i][j]=0;
								bombermanPosition[i+1][j]=1;
								System.out.printf("%d and %d and %d \n", i , j , bombermanPosition[i+1][j]);
								bomberman.setY(bomberman.getY()+100);
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
									bomberman.setY(bomberman.getY()-100);
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
									bomberman.setX(bomberman.getX()+100);
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
									bomberman.setX(bomberman.getX()-100);
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
					bombLabel_up.setIcon(bombImage); 
					bombLabel_down.setIcon(bombImage); 
					bombLabel_left.setIcon(bombImage);
					bombLabel_right.setIcon(bombImage);
					
					bombLabel_up.setLocation(bombLabel.getX(), bombLabel.getY()-100);
					bombLabel_down.setLocation(bombLabel.getX(), bombLabel.getY()+100);
					bombLabel_right.setLocation(bombLabel.getX()+100, bombLabel.getY());
					bombLabel_left.setLocation(bombLabel.getX()-100, bombLabel.getY());
					
					for (int i=0; i< map.length ; i++) {
						for (int j=0; j< map[i].length ; j++) {
							
							//Collision walls and bomb explosion
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_right.getLocation().getX()-25)) && (brickLabel[i][j].getLocation().getY() == (bombLabel_right.getLocation().getY()))) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_left.getLocation().getX()-25)) && (brickLabel[i][j].getLocation().getY() == (bombLabel_left.getLocation().getY()))) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_up.getLocation().getX()-25)) && (brickLabel[i][j].getLocation().getY() == (bombLabel_up.getLocation().getY()))) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							if ((brickLabel[i][j].getLocation().getX() == (bombLabel_down.getLocation().getX()-25)) && (brickLabel[i][j].getLocation().getY() == (bombLabel_down.getLocation().getY()))) {
								brickLabel[i][j].setIcon(emptyImage);
								map[i][j]=0;
							}
							
							
							//Collision bomberman and bomb explosion
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel.getLocation().getX()-25)) && (bombermanLabel.getLocation().getY() == (bombLabel.getLocation().getY()))) {
								bombermanDown = new bomber2();
								bombermanImage = new ImageIcon( getClass().getResource( bombermanDown.getFilename() ) );
								bombermanLabel.setIcon(bombermanImage); 
								flag=1;
								
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_right.getLocation().getX()-25)) && (bombermanLabel.getLocation().getY() == (bombLabel_right.getLocation().getY()))) {
								bombermanDown = new bomber2();
								bombermanImage = new ImageIcon( getClass().getResource( bombermanDown.getFilename() ) );
								bombermanLabel.setIcon(bombermanImage); 
								flag=1;
								
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_left.getLocation().getX()-25)) && (bombermanLabel.getLocation().getY() == (bombLabel_left.getLocation().getY()))) {
								bombermanDown = new bomber2();
								bombermanImage = new ImageIcon( getClass().getResource( bombermanDown.getFilename() ) );
								bombermanLabel.setIcon(bombermanImage);
								flag=1;
								
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_up.getLocation().getX()-25)) && (bombermanLabel.getLocation().getY() == (bombLabel_up.getLocation().getY()))) {
								bombermanDown = new bomber2();
								bombermanImage = new ImageIcon( getClass().getResource( bombermanDown.getFilename() ) );
								bombermanLabel.setIcon(bombermanImage);
								flag=1;
								
							}
							
							if (((bombermanLabel.getLocation().getX()-25) == (bombLabel_down.getLocation().getX()-25)) && (bombermanLabel.getLocation().getY() == (bombLabel_down.getLocation().getY()))) {
								bombermanDown = new bomber2();
								bombermanImage = new ImageIcon( getClass().getResource( bombermanDown.getFilename() ) );
								bombermanLabel.setIcon(bombermanImage);
								flag=1;
								
							}
						}
					}
				}
			};
			
			TimerTask task2 = new TimerTask(){
				public void run() {
					bombLabel.setIcon(emptyImage); 
					bombLabel_up.setIcon(emptyImage); 
					bombLabel_down.setIcon(emptyImage); 
					bombLabel_left.setIcon(emptyImage);
					bombLabel_right.setIcon(emptyImage);
					
					if (flag==1) {
						JOptionPane.showMessageDialog(null, "GAME OVER!");
					}
				}
			};
			
			//initial bomb 
			bombImage = new ImageIcon( getClass().getResource( bomb_ex.getFilename() ) );
			bombLabel.setIcon(bombImage); 
			bombLabel.setLocation(bomberman.getX(), bomberman.getY());
			
			
			// explosion and collisions with walls and bomberman
			timer.schedule(task, 1000);
			
			// reseting the bomb image
			timer.schedule(task2, 2000);
			
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
