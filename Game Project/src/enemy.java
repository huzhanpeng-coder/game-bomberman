import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class enemy extends Sprite implements Runnable{
	
	private Boolean moving;
	private Thread t;
	private JLabel enemyLabel, bombermanLabel, bombLabel;
	private int limit = 0;
	private Boolean direction;
	private JButton animationButton;
	private bomber bomberman;
	private bomb bomb;
	
	public Boolean getMoving() {return moving;}
	public int getLimit() {return limit;}
	
	public void setMoving(Boolean moving) {	this.moving = moving;}
	
	//Work with bomberman features
	public void setBomberman (bomber temp) {this.bomberman=temp;}
	public void setBomb(bomb temp) {this.bomb= temp;}
	
	public void setEnemyLabel(JLabel temp) {this.enemyLabel = temp;}
	public void setBombermanLabel(JLabel temp) {this.bombermanLabel = temp;}
	public void setBombLabel(JLabel temp) {this.bombLabel= temp;}
	public void setAnimationButton(JButton temp) {this.animationButton = temp;}
	public void setLimit(int temp) {this.limit = temp;}
	
	
	public enemy() {
		   super(75,100,"enemy.png");
		   this.moving=false;
		   this.direction=true;
	}
	
	public enemy(JLabel temp) {
		   super(75,100,"enemy.png");
		   this.moving=false;
		   this.direction=true;
		   this.enemyLabel= temp;
	 }
	
	
	public void moveEnemy() {
		t = new Thread(this,"Enemy Thread");
		t.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		this.moving = true;
		
		while(moving) {
			//movement routine
			
			//get current X/Y
			int tx= this.x;
			int ty = this.y;
			
			
			if (direction) {
				tx += 20;
				limit += 20;
				if(limit>=100) {
					direction=false;
				}
			} else {
				tx -= 20;
				limit -= 20;
				if(limit<=-100) {
					direction=true;
				}
			}
			
			this.setX(tx);
			this.setY(ty);
			
			enemyLabel.setLocation(this.x,this.y);
			detectBombermanCollision();
			detectBombCollision();
			
			try {
				Thread.sleep(50);
			} catch(Exception e) { 
				
			}
		}
	}
	
	private void detectBombermanCollision() {
		if(this.r.intersects(bomberman.getRectangle())) {
			System.out.println("Boom!");
			this.moving = false;
			animationButton.setText("Run");
			//enemyLabel.setIcon( new ImageIcon( getClass().getResource("enemy.png")));
			bombermanLabel.setIcon( new ImageIcon( getClass().getResource("smallninja2.png")));
			
		}
	}
	
	private void detectBombCollision() {
		if(this.r.intersects(bomb.getRectangle())) {
			System.out.println("Boom!");
			this.moving = false;
			//enemyLabel.setIcon( new ImageIcon( getClass().getResource("enemy.png")));
			bombLabel.setIcon( new ImageIcon( getClass().getResource("smallninja2.png")));
			
		}
	}
}
