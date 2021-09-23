
public class Sprite {
    //define any data members
	protected int x, y;
	protected int width, height;
	protected String filename;
	
	public int getX() {	return x;}
	public int getY() {	return y;}
	public int getWidth() {	return width;}
	public int getHeight() {return height;	}
	public String getFilename() {return filename;	}
	
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setWidth(int width) {	this.width = width;}
	public void setHeight(int height) {this.height = height;}
	public void setFilename(String filename) {this.filename = filename;}
	
	public Sprite() {
		super();
		this.x= 0;
		this.y=0;
		this.width=0;
		this.height=0;
		this.filename= "";
	}
	
	public Sprite(int x, int y, int width, int height, String filename) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.filename = filename;
	}
	
	public Sprite(int width, int height, String filename) {
		super();
		this.x= 0;
		this.y=0;
		this.width = width;
		this.height = height;
		this.filename = filename;
	}
	
	public void Display() {
		System.out.println("X,Y: " + this.x + "," + this.y);
	}
	
}
