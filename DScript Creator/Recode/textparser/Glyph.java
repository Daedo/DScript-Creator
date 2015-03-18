package textparser;

import java.util.Vector;

import utils.Utilitys;
import files.PropetyInformation;

public class Glyph {
	private String ligature;
	private String transformation;
	private Vector<Connection> connections;

	public Glyph(String ligatureString) {
		this.ligature 		= ligatureString;
		this.connections 	= new Vector<>();
		this.setTransformation("");
	}

	public void addGlyph(Glyph newGlyph,String connectionType) {
		if(newGlyph==null || connectionType==null) {
			return;
		}
		
		//Parse Connection
		
		String[] splits = connectionType.split(",");
		if(splits.length<2) {
			return;
		}
		
		String type = splits[0]+","+splits[1];
		String trans= "";
		for(int i=2;i<splits.length;i++) {
			trans+=splits[i];
			if(i!=(splits.length+1)) {
				trans+=",";
			}
		}
		
		newGlyph.setTransformation(trans);
		Connection connection = new Connection(this, newGlyph, type);
		this.connections.add(connection);
	}

	public Connection getConnection(int index) {
		if(index>=this.connections.size()) {
			return null;
		}
		return this.connections.get(index);
	}

	public void debugGlyph(int tabs) {
		String tab = Utilitys.repeatString("\t", tabs);

		System.out.println(tab/*+"Glyph: "+this.ligature*/+toString()+"\t"+PropetyInformation.getSVGPath(this.ligature));

		for(Connection connection:this.connections) {
			connection.debugConnection(tabs+1);
		}
	}

	public String getLigature() {
		return this.ligature;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+" - Ligature: "+this.ligature+" - Transformation: "+this.transformation;
	}

	
	/**
	 * @return the transformation
	 */
	public String getTransformation() {
		return this.transformation;
	}

	
	/**
	 * @param newTransformation the transformation to set
	 */
	public void setTransformation(String newTransformation) {
		this.transformation = newTransformation;
	}
}
