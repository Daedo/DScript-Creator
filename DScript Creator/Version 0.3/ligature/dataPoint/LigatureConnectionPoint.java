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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.isEntry ? 1231 : 1237);
		result = prime * result + (this.isExit ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(this.x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LigatureConnectionPoint other = (LigatureConnectionPoint) obj;
		if (this.isEntry != other.isEntry)
			return false;
		if (this.isExit != other.isExit)
			return false;
		if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
	
}
