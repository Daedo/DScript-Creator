package scriptRepräsentation;

import java.awt.geom.AffineTransform;

public class Connection {
	public int exitID;
	public AffineTransform carryingTransform;
	public AffineTransform nonCarryingTransform;
	public Glyph end;
	
	public Connection(int exit) {
		this.exitID 				= exit;
		this.carryingTransform 		= new AffineTransform();
		this.nonCarryingTransform 	= new AffineTransform();
	}
}
