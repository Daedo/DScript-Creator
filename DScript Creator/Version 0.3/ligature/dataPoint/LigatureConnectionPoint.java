package ligature.dataPoint;

public class LigatureConnectionPoint {
	public double x;
	public double y;
	public boolean isEntry;
	public boolean isExit;
	
	public LigatureConnectionPoint() {
		this(0,0,false,false);
	}
	
	public LigatureConnectionPoint(double xPos,double  yPos,boolean entry, boolean exit) {
		this.x = xPos;
		this.y = yPos;
		this.isEntry = entry;
		this.isExit = exit;
	}
}
