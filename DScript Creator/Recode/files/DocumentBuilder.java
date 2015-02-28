package files;

import gui.Main;

import java.awt.Dimension;
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
import utils.Point;

public class DocumentBuilder {

	public static SVGDocument newBuildDocument(Glyph root, double xStart, double yStart) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);
		
		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();
		
		double width = 100;
		double height= 100;
		
		Stack<BuilderState> states 			= new Stack<>();
		
		BuilderState state 					= new BuilderState();
		state.posX 							= xStart;
		state.posY 							= yStart;
		state.connectionState 				= 0;
		state.glyphState					= root;
		
		Element mainGroup					= doc.createElementNS(svgNS, "g");
		mainGroup.setAttributeNS(null, "transform", "translate("+xStart+","+yStart+")");
		
		svgRoot.appendChild(mainGroup);

		state.groupState					= mainGroup;
		state.carryTransformation			= true;
		
		states.push(state);
		
		while(!states.isEmpty()) {
			state = states.pop();
			
			Glyph currentGlyph 				= state.glyphState;
			int currentConnectionID			= state.connectionState;
			Element currentGroup			= state.groupState;
			double outX						= state.posX;
			double outY						= state.posY;
			boolean carryTransformation		= state.carryTransformation;
			String svgPath					= PropetyInformation.getSVGPath(currentGlyph.getLigature());
			
			if(currentConnectionID == 0) {
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
				currentGroup.appendChild(img);				
			}
			Connection currentConnection = currentGlyph.getConnection(currentConnectionID);
			if(currentConnection!=null) {
				//Edit Current State
				state.connectionState++;
				
				if(!carryTransformation) {
					//The transformation applies only to the current Glyph
					if(states.isEmpty()) {
						state.groupState = mainGroup;
					} else {
						state.groupState = states.peek().groupState;
					}
				}
				
				//Push current State
				states.push(state);
				
				//Next State
				BuilderState nextState			= new BuilderState();
				nextState.glyphState			= currentConnection.getEnd();
				nextState.connectionState		= 0;
				
				//Create Transformation
				String type = currentConnection.getType();
				System.out.println(type);
				String[] attribs = type.split(",");
				if(attribs.length!=2) {
					//TODO Throw Error
					return null;
				}
				int outAtrib = Integer.parseInt(attribs[0].trim());
				int inAtrib  = Integer.parseInt(attribs[1].trim());

				Glyph endGlyph = currentConnection.getEnd();
				Ligature prevLig = new Ligature(currentGlyph.getLigature());
				Ligature newLig   = new Ligature(endGlyph.getLigature());

				ConnectionPoint outP = prevLig.getConnectionPoint(outAtrib);	// Where the previous Glyph ends (Relative)
				ConnectionPoint inP  = newLig.getConnectionPoint(inAtrib);		// Where this Glyph starts (Relative)
				
				//Build the transformation... The last transformation must be at the first position...
				//translate(POutX-PInX+CInX,POutY-PInY+CInY) <transform> translate(-CInX,-CInY);
				
				// 3. Move Group towards Destination
				double newMoveX = outP.getX()+outX;
				double newMoveY = outP.getY()+outY;
				
				width = Math.max(newMoveX, width);
				height= Math.max(newMoveY, height);
				
				String transformation = " translate("+newMoveX+","+newMoveY+")";
				

				nextState.carryTransformation 	= true;
				
				//Transform Group
				String trans = currentConnection.getTransform();
				if(trans!=null && !trans.equals("")) {
					String[] splitTrans = trans.split(",");
					
					
					//Find if we have to carry the transformation
					for(String subTransform:splitTrans) {
						if(subTransform.trim().toUpperCase().equals("NCARRY")) {
							nextState.carryTransformation = false;
						}
					}
					
					String reparse = reparseTransformation(splitTrans);
					if(!reparse.equals("")) {
						transformation+=" "+reparse;
					}
				}
				
				//First we move absolute
				double moveX	= -outX;
				double moveY	= -outY;
				
				//Now we Move relative.
				if(inP!=null) {
					moveX -= inP.getX();
					moveY -= inP.getY();
				}
				
				transformation += "translate("+moveX+","+moveY+")";
				//The start point of of the Glyph is now at (0|0)
				
				//Create new Group
				Element nextGoup			= doc.createElementNS(svgNS, "g");
				nextGoup.setAttributeNS(null, "transform", transformation);
				currentGroup.appendChild(nextGoup);
				
				nextState.groupState	= nextGoup;
				nextState.posX 			= newMoveX;
				nextState.posY 			= newMoveY;
				
				states.push(nextState);
			}
		}
		
		// Set the width and height attributes on the root 'svg' element.
		svgRoot.setAttributeNS(null, "width",  width + 100 +"");
		svgRoot.setAttributeNS(null, "height", height+ 100 +"");
		
		try {
			printDocument(doc, System.out);
		} catch (IOException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (SVGDocument) doc;
	}

	public static SVGDocument buildDocument(Glyph root, double xStart, double yStart)  {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		Stack<Glyph> workGlyphs				= new Stack<>();
		Stack<Point<Double>> workPositions = new Stack<>();
		Stack<Integer> workConnection		= new Stack<>();

		workGlyphs.push(root);
		workPositions.push(new Point<>(new Double(xStart), new Double(yStart)));
		workConnection.push(new Integer(0));
		double xMax = xStart;
		double yMax = yStart;

		while(!workGlyphs.isEmpty()) {
			Glyph currentGlyph 				= workGlyphs.pop();
			int currentConnectionID			= workConnection.pop().intValue();
			Point<Double> currentPosition	= workPositions.pop();

			String svgPath					= PropetyInformation.getSVGPath(currentGlyph.getLigature());

			if(currentConnectionID==0) {
				Element img = doc.createElementNS(svgNS, "image");

				String jarPath = ClassLoader.getSystemResource("")+"";

				String link = "";
				if(Main.isInWorkspace) {
					link = jarPath+"../"+svgPath;
				} else {
					link = jarPath+svgPath;
				}

				XLinkSupport.setXLinkHref( img,link);

				img.setAttributeNS(null, "x", ""+currentPosition.x.doubleValue());
				img.setAttributeNS(null, "y", ""+currentPosition.y.doubleValue());
				img.setAttributeNS(null, "width", "200");
				img.setAttributeNS(null, "height", "200");
				svgRoot.appendChild(img);
			}			
			Connection currentConnection = currentGlyph.getConnection(currentConnectionID);
			if(currentConnection!=null) {
				workGlyphs.push(currentGlyph);
				workConnection.push(new Integer(currentConnectionID+1));
				workPositions.push(currentPosition);

				//Calculate new Position
				String type = currentConnection.getType();
				System.out.println(type);
				String[] attribs = type.split(",");
				if(attribs.length!=2) {
					//TODO Throw Error
					return null;
				}
				int outAtrib = Integer.parseInt(attribs[0].trim());
				int inAtrib  = Integer.parseInt(attribs[1].trim());

				Glyph endGlyph = currentConnection.getEnd();
				Ligature prevLig = new Ligature(currentGlyph.getLigature());
				Ligature newLig   = new Ligature(endGlyph.getLigature());

				ConnectionPoint outP = prevLig.getConnectionPoint(outAtrib);
				ConnectionPoint inP  = newLig.getConnectionPoint(inAtrib);

				double outMoveX = 0;
				double outMoveY = 0;

				if(outP!=null) {
					outMoveX = outP.getX();
					outMoveY = outP.getY();
				}

				double inMoveX  = 0;
				double inMoveY  = 0;
				if(inP!=null) {
					inMoveX = inP.getX();
					inMoveY = inP.getY();
				}

				/*AffineTransform inTranslation = new AffineTransform();
				inTranslation.translate(-inMoveX, -inMoveY);

				AffineTransform trans = parseTransformation(currentConnection.getTransform());

				AffineTransform outTranslation = new AffineTransform();
				outTranslation.translate(outMoveY, outMoveY);

				AffineTransform fullTransform = new AffineTransform();
				fullTransform.concatenate(inTranslation);
				fullTransform.concatenate(trans);
				fullTransform.concatenate(outTranslation);*/

				double xMove = outMoveX - inMoveX;
				double yMove = outMoveY - inMoveY;

				Double newX = new Double(currentPosition.x.intValue()+xMove);
				Double newY = new Double(currentPosition.y.intValue()+yMove);
				Point<Double> newPoint = new Point<>(newX, newY);

				xMax = Math.max(xMax,newX.doubleValue());
				yMax = Math.max(yMax,newY.doubleValue());

				workGlyphs.push(endGlyph);
				workConnection.push(new Integer(0));
				workPositions.push(newPoint);
			}	
		}

		// Set the width and height attributes on the root 'svg' element.
		svgRoot.setAttributeNS(null, "width",  xMax+100+"");
		svgRoot.setAttributeNS(null, "height", yMax+100+"");

		return (SVGDocument) doc;
	}

	private static String reparseTransformation(String[] transString) {
		if(transString==null) {
			return "";
		}

		String trans = "";

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
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isXScale) {
				try {
					String scalar = transformation.substring(1);
					double scale = Double.parseDouble(scalar);
					trans="scale("+scale+",1) "+trans;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isYScale) {
				try {
					String scalar = transformation.substring(1);
					double scale = Double.parseDouble(scalar);
					trans="scale(1,"+scale+") "+trans;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isFullScale) {
				try {
					String scalar = transformation.substring(1);
					double scale = Double.parseDouble(scalar);
					trans="scale("+scale+","+scale+") "+trans;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			} else if(isHFlip) {
				trans="scale(1,-1) "+trans;

			} else if(isVFlip) {
				trans="scale(-1,1) "+trans;
			}
		}

		System.out.println(trans.toString());

		return trans;
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

			try {
				SVGDocument sdoc = buildDocument(words.elementAt(i), 50, 0);
				sdoc = newBuildDocument(words.elementAt(i), 50, 0);
				
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
