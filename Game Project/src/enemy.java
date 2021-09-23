import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class enemy extends Sprite implements Runnable{
	
	private Boolean moving;
	private Thread t;
	private JLabel enemyLabel;
	
	public Boolean getMoving() {return moving;}
	public void setMoving(Boolean moving) {	this.moving = moving;}
	
	public void setEnemyLabel(JLabel temp) {this.enemyLabel = temp;}
	
	
	public enemy() {
		   super(75,100,"enemy.png");
		   this.moving=false;
	}
	
	public enemy(JLabel temp) {
		   super(75,100,"enemy.png");
		   this.moving=false;
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
			
			tx += 10;
			
			//this.x = tx;
			//this.y = ty;
			
			this.setX(tx);
			this.setY(ty);
			
			enemyLabel.setLocation(this.x,this.y);
			
			try {
				Thread.sleep(200);
			} catch(Exception e) { 
				
			}
		}
	}
}