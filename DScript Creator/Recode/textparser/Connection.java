package textparser;

import java.awt.geom.AffineTransform;

import utils.Utilitys;

public class Connection {
	private Glyph start;
	private Glyph end;
	private String type;
	private String transform;
	
	public Connection(Glyph startGlyph,Glyph endGlyph,String connectionType) {
		this.start= startGlyph;
		this.end  = endGlyph;
		
		String[] typeArgs = connectionType.split(",");
		this.type = typeArgs[0]+","+typeArgs[1];//connectionType;
		
		this.setTransform("");
		for(int i=2;i<typeArgs.length;i++) {
			this.setTransform(this.getTransform() + typeArgs[i]);
			if((i+1)!=typeArgs.length) {
				this.setTransform(this.getTransform() + ",");
			}
		}
	}

	public Glyph getStart() {
		return this.start;
	}

	public void setStart(Glyph startGlyph) {
		this.start = startGlyph;
	}

	public Glyph getEnd() {
		return this.end;
	}

	public void setEnd(Glyph endGlyph) {
		this.end = endGlyph;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String connectionType) {
		this.type = connectionType;
	}

	public void debugConnection(int tabs) {
		String tab = Utilitys.repeatString("\t", tabs);
		System.out.println(tab+this.type);
		this.end.debugGlyph(tabs+1);
	}

	public static void parseTransformations(String transString) {
		if(transString.equals("")) {
			return;
		}
		
		AffineTransform trans = new AffineTransform();
		
		String[] transfomrations = transString.split(",");		
		
		for(String transformation:transfomrations) {
			boolean isRotation = transformation.startsWith("R"); // Rxxx
			boolean isXScale   = transformation.startsWith("SX");// SXxxx
			boolean isYScale   = transformation.startsWith("SY");// SYxxx
			boolean isFullScale= transformation.startsWith("SF");// SFxxx
			boolean isHFlip	   = transformation.equals("HF");	 // HF
			boolean isVFlip    = transformation.equals("VF");	 // VF
			
			if(isRotation) {
				try {
					String rotation = transformation.substring(1);
					double rot = Double.parseDouble(rotation)/180*Math.PI;
					trans.rotate(rot);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			} else if(isXScale) {
				try {
					String scalar = transformation.substring(1);
					double scale = Double.parseDouble(scalar);
					trans.scale(scale, 0);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			} else if(isYScale) {
				try {
					String scalar = transformation.substring(1);
					double scale = Double.parseDouble(scalar);
					trans.scale(0, scale);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			} else if(isFullScale) {
				try {
					String scalar = transformation.substring(1);
					double scale = Double.parseDouble(scalar);
					trans.scale(scale, scale);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			} else if(isHFlip) {
				trans.scale(1, -1);
				
			} else if(isVFlip) {
				trans.scale(-1, 1);
			}
		}
		
		System.out.println(trans.toString());
		
	}

	/**
	 * @return the transform
	 */
	public String getTransform() {
		return this.transform;
	}

	/**
	 * @param newTransform the transform to set
	 */
	public void setTransform(String newTransform) {
		this.transform = newTransform;
	}
}
