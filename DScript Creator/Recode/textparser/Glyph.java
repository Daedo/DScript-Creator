package textparser;

import java.util.Vector;

import utils.Utilitys;

public class Glyph {
	private String ligature;
	private Vector<Connection> connections;
	
	public Glyph(String ligatureString) {
		this.ligature 		= ligatureString;
		this.connections 	= new Vector<>();
	}
	
	public void addGlyph(Glyph newGlyph,String connectionType) {
		if(newGlyph==null || connectionType==null) {
			return;
		}
		Connection connection = new Connection(this, newGlyph, connectionType);
		this.connections.add(connection);
	}

	public void debugGlyph(int tabs) {
		String tab = Utilitys.repeatString("\t", tabs);
		System.out.println(tab+"Glyph: "+this.ligature+"\t"+GlyphDisplayer.getDSVGFile(this.ligature));
		for(Connection connection:this.connections) {
			connection.debugConnection(tabs+1);
		}
	}
}
