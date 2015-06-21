package builder;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import ligature.dataPoint.LigatureConnectionPoint;
import ligatures.Ligature;
import ligatures.LigatureLookup;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import files.BuildingException;
import scriptRepräsentation.Connection;
import scriptRepräsentation.DScriptText;
import scriptRepräsentation.Glyph;
import scriptRepräsentation.Wordstart;

public class DScriptBuilder {
	private static final String SVG_NAMESPACE = SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	public SVGDocument buildDScript(DScriptText text) throws BuildingException {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		Document doc = impl.createDocument(SVG_NAMESPACE, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		svgRoot.setAttributeNS(null, "width",  text.width+"");
		svgRoot.setAttributeNS(null, "height", text.height+"");

		for(Wordstart word:text.words) {
			try {
				buildWord(doc,svgRoot,word);
			} catch (BuildingException e) {
				throw new BuildingException("Could not build word: \""+word.wordText+"\"", e);
			}
		}

		return (SVGDocument) doc;
	}

	private void buildWord(Document doc,Element parent, Wordstart wordstart) throws BuildingException {
		if(wordstart.end==null) {
			return;
		}
		
		double wordPosX = wordstart.x;
		double wordPosY = wordstart.y;
		
		//Build Transformations
		AffineTransform startPos = AffineTransform.getTranslateInstance(wordPosX, wordPosY);
		
		buildConnection(doc, parent, wordstart, startPos);
	}
	
	private void buildConnection(Document doc,Element parent,Connection connection, AffineTransform dataCarry) throws BuildingException {
		dataCarry.concatenate(connection.carryingTransform);
		dataCarry.concatenate(connection.nonCarryingTransform);
		
		AffineTransform inTransform = AffineTransform.getTranslateInstance(0, 0);
		//-InX,-InY
		dataCarry.concatenate(inTransform);
		
		// Group for the current Glyph
		Element glyphGroup = createGroupFromTransform(doc, parent, dataCarry);
		
		Glyph currentGlyph = connection.end;
		Ligature lig = LigatureLookup.lookupLigature(currentGlyph.ligature);
		
		//Import Glyph Data
		//TODO We currently only use the first Elmenent of the lig.drawData = first <g> in the ligature svg file, we might want to change that, to fit different versions for things like inside inserts 
		Node glyphNode = lig.drawData.item(0);
		doc.importNode(glyphNode, true);
		glyphGroup.appendChild(glyphNode);
		
		
		//Recursive Call: Try to build new Connections
		AffineTransform nonCarryingInverse = new AffineTransform();
		
		try {
			nonCarryingInverse = connection.nonCarryingTransform.createInverse();
		} catch (NoninvertibleTransformException e) {
			throw new BuildingException("Could not invert non-carrying matrix while building the ligature: "+currentGlyph.ligature, e);
		} 
		
		for(Connection con:currentGlyph.connections) {
			Integer outID 							= new Integer(con.exitID);
			LigatureConnectionPoint connectionPoint = lig.connecions.get(outID); 
			double outX								= connectionPoint.x;
			double outY								= connectionPoint.y;

			//OutX,OutY
			AffineTransform newDataCarry = AffineTransform.getTranslateInstance(outX, outY);
			newDataCarry.concatenate(nonCarryingInverse);
			
			buildConnection(doc, glyphGroup, con, newDataCarry);
		}
	}
	
	private static Element createGroupFromTransform(Document doc,Element parent,AffineTransform transform) {
		Element group = doc.createElementNS(SVG_NAMESPACE,"g");
		
		String matrix = "matrix("+transform.getScaleX()+","+transform.getShearY()+","+transform.getShearX()+","+
				transform.getScaleY()+","+transform.getTranslateX()+","+transform.getTranslateY()+")";
		
		group.setAttributeNS(null, "transform", matrix);
		parent.appendChild(group);
		
		return group;
	}
}
