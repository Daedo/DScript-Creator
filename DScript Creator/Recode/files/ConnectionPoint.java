package files;

public class ConnectionPoint {
	private int id;
	private int x;
	private int y;
	private boolean isInpoint;
	private boolean isOutpoint;
	
	public ConnectionPoint(int connectionID,int xPos,int yPos,boolean in,boolean out) {
		this.setId(connectionID);
		this.x = xPos;
		this.y = yPos;
		this.isInpoint = in;
		this.isOutpoint = out;
	}
	
	@Override
	public String toString() {
		return "ID: "+this.id+" X: "+this.x+" Y: "+this.y+" In: "+this.isInpoint+" Out: "+this.isOutpoint;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int connectionID) {
		this.id = connectionID;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int xPos) {
		this.x = xPos;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int yPos) {
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
