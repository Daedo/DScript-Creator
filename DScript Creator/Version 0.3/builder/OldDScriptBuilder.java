package builder;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import textparser.Connection;
import textparser.Glyph;
import files.BuilderState;
import gui.GUIData;
import gui.GUIWord;

public class OldDScriptBuilder {
	public SVGDocument buildDScript(GUIData data) {

		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		svgRoot.setAttributeNS(null, "width",  data.width+"");
		svgRoot.setAttributeNS(null, "height", data.height+"");

		for(GUIWord word:data.words) {
			buildWord(doc,word);
		}


		return (SVGDocument) doc;
	}

	private void buildWord(Document doc,GUIWord word) {
		Glyph root = word.word.glyph;
		String entryConnection = "2,1";

		//If Root is Empty move on (-> If the user starts like  {2,5 xxxx})
		while(root.getLigature()=="") {
			Connection rootConnect = root.getConnection(0);
			if(rootConnect!=null) {
				entryConnection = rootConnect.getType();
				//Set new Root
				root = rootConnect.getEnd();
			}

		}
		
		//Init Wordbuild Vars
		BuilderState state 					= new BuilderState();
		state.connectionType				= entryConnection;
		state.posX 							= word.x;
		state.posY 							= word.y;
		state.connectionState 				= 0;
		state.glyphState					= root;
		state.inverseTransform				= null;
		
		buildGlyph(state);
	}
	
	/*
	 * svg
	 * 		Group (Word) (Transformation for the entire Word)
	 * 			Group(Chain) (Transformation for the current Chain)
	 * 				Group(Glyph) (Transformation for the current Glyph)
	 * 					Glyph
	 * 				Group(Chain) (Transformation for the current Chain)
	 * 					Group(Glyph) (Transformation for the current Glyph)
	 * 						Glyph
	 */
	
	private void buildGlyph(BuilderState state) {
		//Build Group
		
		//Build Carrying Transformation
		
		//Build Non-Carrying Transformations
		
		//Import Glyph
	}
}
