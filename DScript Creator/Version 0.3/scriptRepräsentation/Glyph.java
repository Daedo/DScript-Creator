package scriptRepräsentation;

import java.util.Vector;

public class Glyph {
	public String ligature;
	public int entryID;
	public Vector<Connection> connections;
	
	public Glyph(int entry,String lig) {
		this.connections = new Vector<>();
		this.ligature = lig;
		this.entryID = entry;
	}
}
