package build;

import java.awt.geom.Point2D;
import java.util.HashMap;

import org.w3c.dom.Document;

public class DScriptVector {
	private HashMap<String, Point2D.Double> inputPoints;
	private HashMap<String, Point2D.Double> outputPoints;
	private Document vectorGraphic;
	
	public DScriptVector() {
		this.inputPoints = new HashMap<>();
		this.outputPoints = new HashMap<>();
	}
	
	public void setInputPoint(String Position, Point2D.Double Point) {
		this.inputPoints.put(Position, Point);
	}
	
	public Point2D.Double getInputPoint(String Position) {
		return this.inputPoints.get(Position);
	}
	
	public void setOutputPoint(String Position, Point2D.Double Point) {
		this.outputPoints.put(Position, Point);
	}
	
	public Point2D.Double getOutputPoint(String Position) {
		return this.outputPoints.get(Position);
	}

	
	public Document getVectorGraphic() {
		return this.vectorGraphic;
	}
	
	public void setVectorGraphic(Document newVectorGraphic) {
		this.vectorGraphic = newVectorGraphic;
	}

}
