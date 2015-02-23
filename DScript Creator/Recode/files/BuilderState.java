package files;

import org.w3c.dom.Element;

import textparser.Glyph;

public class BuilderState {
	public Glyph glyphState;
	public int connectionState;
	public double posX;
	public double posY;
	public boolean carryTransformation;
	public Element groupState;
}