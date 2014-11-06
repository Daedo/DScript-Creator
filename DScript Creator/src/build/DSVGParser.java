package build;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class DSVGParser {
	
	/*
	 * Used Flags:
	 * Wordstart
	 * Wordend
	 * Input c, l, r, i
	 * Output c, l, r, i
	 */
	
	
	
	public static Document parseDSVGFile(String FileURI, int flags) throws Exception{
		//Open File
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		File DSVGFile = new File(FileURI);
		Document DSVGDocument = builder.parse(DSVGFile);



		Element rootElement = DSVGDocument.getDocumentElement(); 
		String rootName = rootElement.getNodeName();
		boolean isValidSVGDocument = rootName.equals("ligature");


		if(isValidSVGDocument) {
			//Find a valid connection
			NodeList childNodes = rootElement.getChildNodes();
			int nodeCount = childNodes.getLength();

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

			boolean hasConnection = !connection.equals("");
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

					//Return the new Document
					return outDoc;

				}
				//ERROR NO SVG TAG FOUND;
				System.err.println("ERROR NO SVG TAG FOUND");
			} else {
				//ERROR NO CONNECTION FOUND
				System.err.println("ERROR NO CONNECTION FOUND");
			}
		}
		return null;
	}

	private static boolean isMatchingInput(String input,int flag) {
		//TODO write input type - Flag validator
		return true;
	}

	private static boolean isMatchingOutput(String input,int flag) {
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
