package files;

import gui.Main;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Stack;
import java.util.Vector;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.XLinkSupport;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import textparser.Connection;
import textparser.Glyph;

public class DocumentBuilder {

	public static SVGDocument buildDocument(Glyph root, double xStart, double yStart) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		double width = 100;
		double height= 100;

		Glyph rootGlyph = root;
		String inConnection = "2,1";
		//If Root is Empty move on
		while(rootGlyph.getLigature()=="") {

			Connection rootConnect = root.getConnection(0);
			if(rootConnect!=null) {
				inConnection = rootConnect.getType();
				//Set new Root
				rootGlyph = rootConnect.getEnd();
			}

		}

		Stack<BuilderState> states 			= new Stack<>();

		BuilderState state 					= new BuilderState();
		state.connectionType				= inConnection;
		state.posX 							= 0;//xStart;
		state.posY 							= 0;//yStart;
		state.connectionState 				= 0;
		state.glyphState					= rootGlyph;
		state.inverseTransform				= null;
		
		Element mainGroup					= doc.createElementNS(svgNS, "g");
		mainGroup.setAttributeNS(null, "transform", "translate("+xStart+","+yStart+")");

		svgRoot.appendChild(mainGroup);

		state.groupState					= mainGroup;

		states.push(state);

		while(!states.isEmpty()) {
			state = states.pop();

			String currentConnectionType	= state.connectionType;
			Glyph currentGlyph 				= state.glyphState;
			int currentConnectionID			= state.connectionState;
			Element currentGroup			= state.groupState;
			double stateX					= state.posX;
			double stateY					= state.posY;
			AffineTransform inverseTrans	= state.inverseTransform;
			String svgPath					= PropetyInformation.getSVGPath(currentGlyph.getLigature());


			Ligature currentLigature		= new Ligature(currentGlyph.getLigature());
			Connection currentConnection 	= currentGlyph.getConnection(currentConnectionID);

			if(currentConnectionID==0) {
				//Build Transformation Group

				//Move the Glyph to the end of the Chain (Added first, executed last)
				Element outPrevGroup 	= doc.createElementNS(svgNS, "g");
				outPrevGroup.setAttributeNS(null, "transform", "translate("+stateX+","+stateY+")");

				//Add transformations
				Element transformGroup 	= doc.createElementNS(svgNS, "g");

				String transform 		= "";

				if(inverseTrans!=null) {
					transform = createMatrix(inverseTrans);
					inverseTrans = null;
				}
				
				
				
				boolean hasToCarry = true;
				String trans = currentGlyph.getTransformation();
				if(trans!=null && !trans.equals("")) {
					String[] splitTrans = trans.split(",");


					//Find if we have to carry the transformation

					for(String subTransform:splitTrans) {
						if(subTransform.trim().toUpperCase().equals("NCARRY")) {
							hasToCarry = false;
						}
					}
					
					AffineTransform afTransform = reparseTransformation(splitTrans);
					String reparse = createMatrix(afTransform);
					if(!reparse.equals("")) {
						transform+=" "+reparse;
					}
					
					if(!hasToCarry) {
						try {
							inverseTrans = afTransform.createInverse();
						} catch (NoninvertibleTransformException e) {
							e.printStackTrace();
							inverseTrans = null;
						}
					}
				}

				transformGroup.setAttributeNS(null, "transform", transform);

				//Move the Glyph from the start configuration so, that the in Point matches with the origin of the coordinate System
				Element inCurrGroup 	= doc.createElementNS(svgNS, "g");

				String glyphInType  = currentConnectionType.split(",")[1];
				int glyphInID		= Integer.parseInt(glyphInType);
				ConnectionPoint inPoint = currentLigature.getConnectionPoint(glyphInID);

				double inMoveX = -1*inPoint.getX();
				double inMoveY = -1*inPoint.getY();

				inCurrGroup.setAttributeNS(null, "transform", "translate("+inMoveX+","+inMoveY+")");

				//Build Groups Together
				currentGroup.appendChild(outPrevGroup);
				outPrevGroup.appendChild(transformGroup);
				transformGroup.appendChild(inCurrGroup);


				//Import Current Connection
				System.out.println("Glyph:"+currentGlyph);

				//Add Image to Group
				Element img = doc.createElementNS(svgNS, "image");

				String jarPath = ClassLoader.getSystemResource("")+"";

				String link = "";
				if(Main.isInWorkspace) {
					link = jarPath+"../"+svgPath;
				} else {
					link = jarPath+svgPath;
				}

				XLinkSupport.setXLinkHref( img,link);

				img.setAttributeNS(null, "x", "0");
				img.setAttributeNS(null, "y", "0");
				img.setAttributeNS(null, "width", "200");
				img.setAttributeNS(null, "height", "200");
				inCurrGroup.appendChild(img);

				/*if(!hasToCarry) {
					//Build a Group without transformation

					Element nCarryGroup = doc.createElementNS(svgNS,"g");
					double nCarryX	= inMoveX+stateX;
					double nCarryY	= inMoveY+stateY;
					nCarryGroup.setAttributeNS(null,"transform", "translate("+nCarryX+","+nCarryY+")");
					currentGroup.appendChild(nCarryGroup);
					currentGroup = nCarryGroup;
				} else {
					currentGroup = inCurrGroup;
				}*/
				currentGroup = inCurrGroup;

				//Update current State

				state.groupState = currentGroup;
			}

			//Push old and Push next
			if(currentConnection!=null) {


				Connection nextConnect 			= currentGlyph.getConnection(currentConnectionID);
				if(nextConnect!=null && nextConnect.getEnd()!=null) {
					//Next State
					BuilderState nextState			= new BuilderState();
					nextState.glyphState			= nextConnect.getEnd();

					nextState.connectionState		= 0;
					nextState.connectionType		= currentConnection.getType();

					//Calculate new Position
					String outType					= nextState.connectionType.split(",")[0];
					int outTypeID					= Integer.parseInt(outType);
					ConnectionPoint outPoint		= currentLigature.getConnectionPoint(outTypeID);

					nextState.posX					= outPoint.getX();
					nextState.posY					= outPoint.getY();
					nextState.groupState			= currentGroup;
					nextState.inverseTransform		= inverseTrans;
					
					//Push current State
					state.connectionState++;
					state.inverseTransform = inverseTrans;
					states.push(state);
					states.push(nextState);
				}
			}
		}

		try {
			printDocument(doc, System.out);
		} catch (IOException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set the width and height attributes on the root 'svg' element.
		svgRoot.setAttributeNS(null, "width",  width + 100 +"");
		svgRoot.setAttributeNS(null, "height", height+ 100 +"");


		return (SVGDocument) doc;
	}

	private static AffineTransform reparseTransformation(String[] transString) {
		if(transString==null) {
			return new AffineTransform();
			//return "";
		}

		String trans = "";
		AffineTransform transform = new AffineTransform();


		for(String transformation:transString) {
			boolean isRotation = transformation.startsWith("R"); // Rxxx
			boolean isXScale   = transformation.startsWith("SX");// SXxxx
			boolean isYScale   = transformation.startsWith("SY");// SYxxx
			boolean isFullScale= transformation.startsWith("SF");// SFxxx
			boolean isHFlip	   = transformation.equals("HF");	 // HF
			boolean isVFlip    = transformation.equals("VF");	 // VF

			if(isRotation) {
				try {
					String rotation = transformation.substring(1);
					double rot = Double.parseDouble(rotation);
					trans="rotate("+rot+") "+trans;
					transform.rotate(rot/180*Math.PI);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isXScale) {
				try {
					String scalar = transformation.substring(2);
					double scale = Double.parseDouble(scalar);
					trans="scale("+scale+",1) "+trans;
					transform.scale(scale, 1);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isYScale) {
				try {
					String scalar = transformation.substring(2);
					double scale = Double.parseDouble(scalar);
					trans="scale(1,"+scale+") "+trans;
					transform.scale(1, scale);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isFullScale) {
				try {
					String scalar = transformation.substring(2);
					double scale = Double.parseDouble(scalar);
					trans="scale("+scale+","+scale+") "+trans;
					transform.scale(scale, scale);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isHFlip) {
				trans="scale(1,-1) "+trans;
				transform.scale(1, -1);

			} else if(isVFlip) {
				trans="scale(-1,1) "+trans;
				transform.scale(-1, 1);
			}
		}

		System.out.println(trans.toString());
		System.out.println(transform.toString());
		System.out.println(createMatrix(transform));
		//return createMatrix(transform);//trans;
		return transform;
	}

	private static String createMatrix(AffineTransform trans) {
		if(trans==null) {
			return "";
		}
		
		String out = "matrix("+trans.getScaleX()+","+trans.getShearY()+","+trans.getShearX()+trans.getScaleY()+","+trans.getTranslateX()+","+trans.getTranslateY()+")";
		
		return out;
	}
	
	
	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), 
				new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}

	public static SVGDocument buildWords(Vector<Glyph> words) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		double xPos = 10;
		double yPos = 0;

		for(int i=0;i<words.size();i++) {
			//TODO Use Correct Word Alignment
			//TODO Add InfoGUI Datas
			try {
				SVGDocument sdoc = buildDocument(words.elementAt(i), 50, 0);

				Element svgElement = sdoc.getDocumentElement();
				String width  = svgElement.getAttribute("width");
				String height = svgElement.getAttribute("height");
				if(width==null || height==null) {
					continue;
				}

				//Wordgroup
				Element glyphGroup = doc.createElementNS(svgNS, "g");
				glyphGroup.setAttributeNS(null, "transform", "translate("+xPos+","+(yPos*0)+")");
				svgRoot.appendChild(glyphGroup);

				xPos+=Double.parseDouble(width);
				yPos+=Double.parseDouble(height);

				NodeList list = sdoc.getChildNodes();
				for(int j=0;j<list.getLength();j++) {
					Node imp = doc.importNode(list.item(j), true);
					glyphGroup.appendChild(imp);
				}
			} catch (NumberFormatException|DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		svgRoot.setAttributeNS(null, "width",  (xPos+200)+"");
		svgRoot.setAttributeNS(null, "height", (yPos+200)+"");

		return (SVGDocument)doc;
	}

	public static Dimension getSVGSize(Document doc) {
		Element svgElement 	= doc.getDocumentElement();
		if(svgElement.hasAttribute("width") && svgElement.hasAttribute("height")) {
			String width  		= svgElement.getAttribute("width");
			String height 		= svgElement.getAttribute("height");

			int    wDim			= (int) Math.ceil(Double.parseDouble(width));
			int    hDim			= (int) Math.ceil(Double.parseDouble(height));

			return new Dimension(wDim,hDim);
		}
		return null;
	}
}
