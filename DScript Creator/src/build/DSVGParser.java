package build;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DSVGParser {

	/*
	 * Used Flags:
	 * Wordstart
	 * Wordend
	 * Input c, l, r, i
	 * Output c, l, r, i
	 */

	public static Document parseDSVGFile(String FileURI, DScriptFlag flags) throws Exception{
		//Open File

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//Speed Workarounds: http://jdevel.wordpress.com/2011/03/28/java-documentbuilder-xml-parsing-is-very-slow/
		factory.setNamespaceAware(false);
		factory.setValidating(true);																//I Want to keep validation, but you might want to set it to false to increase the speed
		factory.setFeature("http://xml.org/sax/features/namespaces", false);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);		
		DocumentBuilder builder = factory.newDocumentBuilder();
		File DSVGFile = new File(FileURI);
		System.out.println("Start Builder");
		Document DSVGDocument = builder.parse(DSVGFile);
		System.out.println("End Builder");


		Element rootElement = DSVGDocument.getDocumentElement(); 
		String rootName = rootElement.getNodeName();
		boolean isValidSVGDocument = rootName.equals("ligature");

		if(isValidSVGDocument) {
			//Find a valid connection
			NodeList childNodes = rootElement.getChildNodes();
			int nodeCount = childNodes.getLength();

			DScriptVector outputVector = null;


			String connection = "";
			String inputX="";
			String inputY="";
			String outputX="";
			String outputY="";


			for(int i=0;i<nodeCount;i++) {
				Node currentChild = childNodes.item(i);
				String currentName = currentChild.getNodeName();
				boolean isConnection = currentName.equals("connection");

				if(isConnection) {
					//Create Vector from Connection
					DScriptVector connectionVector = parseConnection(currentChild);
					//Test if the Vector(= the connection) matches the Flags
					boolean matchingConnection = false;
					if(matchingConnection) {
						outputVector = connectionVector;
						break;
					}
				}	
			}

			boolean hasConnection = outputVector!=null;
			/*
			for(int i=0;i<nodeCount;i++) {
				Node currentChild = childNodes.item(i);
				String currentName = currentChild.getNodeName();
				boolean isConnection = currentName.equals("connection");

				if(isConnection) {
					//Check Validity
					boolean hasInput = false;
					boolean hasOutput = false;
					NodeList connectionPoints = currentChild.getChildNodes();
					int pointCount = connectionPoints.getLength();

					for(int j=0;j<pointCount;j++) {
						Node currentPoint = connectionPoints.item(j);
						String connectionType = currentPoint.getNodeName();

						boolean isInput = connectionType.equals("start");
						boolean isOutput = connectionType.equals("end");

						if(isInput && !hasInput) {
							//Check inputs
							NamedNodeMap  inputAttributes = currentPoint.getAttributes();
							String inputType = inputAttributes.getNamedItem("type").getNodeValue();
							boolean isValid = isMatchingInput(inputType,flags);
							if(isValid) {
								inputX = inputAttributes.getNamedItem("x").getNodeValue();
								inputY = inputAttributes.getNamedItem("y").getNodeValue();
								hasInput=true;	
							}

						} else {
							if(isOutput && !hasOutput) {
								//Check Outputs
								NamedNodeMap  inputAttributes = currentPoint.getAttributes();
								String outputType = inputAttributes.getNamedItem("type").getNodeValue();
								boolean isValid = isMatchingOutput(outputType, flags);
								if(isValid) {
									outputX = inputAttributes.getNamedItem("x").getNodeValue();
									outputY = inputAttributes.getNamedItem("y").getNodeValue();
									hasOutput=true;	
								}
							}
						}

						if(hasInput && hasOutput) {
							break;
						}
					}

					//If is Valid Exit and set connection to the id;
					if(hasInput && hasOutput) {
						NamedNodeMap attributes = currentChild.getAttributes();
						connection=attributes.getNamedItem("id").getNodeValue();
						break;
					}
				}
			} 
			boolean hasConnection = !connection.equals("");*/


			if(hasConnection) {
				/*System.out.println(connection);
				System.out.println(inputX);
				System.out.println(inputY);
				System.out.println(outputX);
				System.out.println(outputY);*/

				Node SVGTag = null;
				//Find SVG Tag
				for(int i=0;i<nodeCount;i++) {
					Node currentChild = childNodes.item(i);
					String currentType = currentChild.getNodeName();
					boolean isSVGTag = currentType.equals("svg");

					if(isSVGTag) {
						SVGTag = currentChild;
						break;
					}
				}

				boolean hasSVGTag = SVGTag!=null;
				if(hasSVGTag) {
					Node currentChild=SVGTag.getFirstChild();
					Vector<Node> removeNodes = new Vector<>();

					while(true) {
						String currentType = currentChild.getNodeName();
						boolean isGroup = currentType.equals("g");

						//Find all g Tags, that match with the connection
						//Insert all matched Tags, which don't match
						if(isGroup) {
							NamedNodeMap groupAttributes = currentChild.getAttributes();
							String condtion = groupAttributes.getNamedItem("condition").getNodeValue();

							boolean isMatching = isMatchingCondition(condtion, connection);
							if(!isMatching) {
								removeNodes.add(currentChild);
							} 
						} else {
							removeNodes.add(currentChild);
						}
						currentChild=currentChild.getNextSibling();
						if(currentChild==null) {
							break;
						}
					}

					for(Node currentNode:removeNodes) {
						SVGTag.removeChild(currentNode);
					}

					//Create new Document an a SVG Tag
					Document outDoc = builder.newDocument();
					Node newTag = outDoc.importNode(SVGTag, true);
					outDoc.appendChild(newTag);

					System.out.println(outDoc.getDocumentElement());

					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(outDoc);
					//StreamResult result = new StreamResult(new File("C:/Users/Dominik/Desktop/file.xml"));

					// Output to console for testing
					StreamResult result = new StreamResult(System.out);

					transformer.transform(source, result);

					System.out.println();
					//Return the new Document
					outputVector.setVectorGraphic(outDoc);

					return outDoc;

				}
				throw new Exception("ERROR NO SVG TAG FOUND");
			}
			throw new Exception("ERROR NO CONNECTION FOUND");
		}
		throw new Exception("ERROR INVALID DOCUMENT TYPE");
		//return null;
	}

	/**
	 * Creates a DScriptVector from a connectionNode 
	 * 
	 * @param connectionNode
	 * @return
	 */
	private static DScriptVector parseConnection(Node connectionNode) {
		DScriptVector matchedConnection = new DScriptVector();

		NodeList children = connectionNode.getChildNodes();
		int childCount = children.getLength();

		for(int i = 0;i < childCount;i++) {
			Node currentChild = children.item(i);
			String currentType = currentChild.getNodeName();
			NamedNodeMap currentAttributes = currentChild.getAttributes();

			boolean isStart	= currentType.equals("start");
			boolean isEnd	= currentType.equals("end");

			String xPoint,yPoint,type;

			if(isStart|| isEnd) {
				xPoint = currentAttributes.getNamedItem("x").getNodeValue();
				yPoint = currentAttributes.getNamedItem("y").getNodeValue();
				type   = currentAttributes.getNamedItem("type").getNodeValue();

				double xVal,yVal;
				xVal = 0;
				yVal = 0;
				boolean isValid = true;

				try {
					xVal = Double.parseDouble(xPoint);
					yVal = Double.parseDouble(yPoint);
				} catch (NumberFormatException e) {
					// False Number Format
					e.printStackTrace();
					isValid = false;
				}

				if(isValid) {
					
					//Parse Type
					
					
					
					if(isStart) {
						matchedConnection.setInputPoint("", new Point2D.Double(xVal, yVal));
					} else {
						matchedConnection.setOutputPoint("", new Point2D.Double(xVal, yVal));
					}
				}
			}
		}

		return matchedConnection;
	}

	private static boolean isMatchingInput(String input,DScriptFlag flags) {
		//TODO write input type - Flag validator
		return true;
	}

	private static boolean isMatchingOutput(String input,DScriptFlag flags) {
		//TODO write output type - Flag validator
		return true;
	}

	/**
	 * Checks if the Condition of a &lt;g&gt; tag matches with the connection id.
	 * 
	 * @param condition The condition of the Tag as a String
	 * @param connection The connection id of the connection Tag.
	 * @return
	 */
	private static boolean isMatchingCondition(String condition,String connection) {
		String[] subconditions = condition.split(",");
		for(String subcondition:subconditions) {
			String subconditionTrimed = subcondition.trim();
			boolean isMatching = subconditionTrimed.equals(connection);
			if(isMatching) {
				return true;
			}
		}
		return false;
	}
}
