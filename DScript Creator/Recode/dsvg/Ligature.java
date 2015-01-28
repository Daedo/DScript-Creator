package dsvg;

import java.util.Vector;

public class Ligature {
	private Vector<ConnectionPoint> points;
	private String svgDocument;
	
	public Ligature(String svgURI) {
		this.points = new Vector<>();
		this.svgDocument = svgURI;
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
	
	public String getSvgDocument() {
		return this.svgDocument;
	}
	
	@Override
	public String toString() {
		String out = "Ligature: "+this.svgDocument+"\n";
		for(ConnectionPoint c:this.points) {
			out+=c.toString()+"\n";
		}
		return out.trim();
	}
}
