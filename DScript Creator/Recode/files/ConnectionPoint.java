package files;

public class ConnectionPoint {
	private int id;
	private double x;
	private double y;
	private boolean isInpoint;
	private boolean isOutpoint;
	
	public ConnectionPoint(int connectionType,double xPos,double yPos,boolean in,boolean out) {
		this.setID(connectionType);
		this.x = xPos;
		this.y = yPos;
		this.isInpoint = in;
		this.isOutpoint = out;
	}
	
	@Override
	public String toString() {
		return "ID: "+this.id+" X: "+this.x+" Y: "+this.y+" In: "+this.isInpoint+" Out: "+this.isOutpoint;
	}

	public int getID() {
		return this.id;
	}

	public void setID(int connectionID) {
		this.id = connectionID;
	}

	public double getX() {
		return this.x;
	}

	public void setX(double xPos) {
		this.x = xPos;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double yPos) {
		this.y = yPos;
	}

	public boolean isInpoint() {
		return this.isInpoint;
	}

	public void setInpoint(boolean inpoint) {
		this.isInpoint = inpoint;
	}

	public boolean isOutpoint() {
		return this.isOutpoint;
	}

	public void setOutpoint(boolean outpoint) {
		this.isOutpoint = outpoint;
	}
}
