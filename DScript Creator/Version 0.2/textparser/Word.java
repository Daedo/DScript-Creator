package textparser;

public class Word {
	public String text;
	public Glyph glyph;
	public Word(String wordText, Glyph wordGlyph) {
		super();
		this.text = wordText;
		this.glyph = wordGlyph;
	}
	public void debugGlyph(int i) {
		this.glyph.debugGlyph(i);
	}
	
	
}
