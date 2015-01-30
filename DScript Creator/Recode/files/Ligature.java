package files;

import java.util.Vector;

public class Ligature {
	private Vector<ConnectionPoint> points;
	private String value;	
	
	public Ligature(String ligature) {
		this.value 		= ligature;
		String XMLPath 	= PropetyInformation.getXMLPath(ligature);
		this.points 	= PointParser.parsePoints(XMLPath);
		if(this.points==null) {
			this.points = new Vector<>();
		}
	}
	
	public void addConnectionPoint(ConnectionPoint point) {
		this.points.add(point);
	}

	public ConnectionPoint getConnectionPoint(int id) {
		for(ConnectionPoint c:this.points) {
			if(c.getId()==id) {
				return c;
			}
		}
		return null;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		String out = "Ligature: "+this.value+"\n";
		for(ConnectionPoint c:this.points) {
			out+=c.toString()+"\n";
		}
		return out.trim();
	}
}
