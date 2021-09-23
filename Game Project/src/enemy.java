import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class enemy extends Sprite implements Runnable{
	
	private Boolean moving;
	private Thread t;
	private JLabel enemyLabel;
	private int limit = 0;
	private Boolean direction;
	
	public Boolean getMoving() {return moving;}
	public void setMoving(Boolean moving) {	this.moving = moving;}
	
	public void setEnemyLabel(JLabel temp) {this.enemyLabel = temp;}
	
	
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
				tx += 100;
				limit += 100;
				if(limit>=100) {
					direction=false;
				}
			} else {
				tx -= 100;
				limit -= 100;
				if(limit<0) {
					direction=true;
				}
			}
			
			this.setX(tx);
			this.setY(ty);
			
			enemyLabel.setLocation(this.x,this.y);
			
			try {
				Thread.sleep(600);
			} catch(Exception e) { 
				
			}
		}
	}
}
