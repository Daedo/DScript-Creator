package scriptRepräsentation;

import java.awt.geom.AffineTransform;

public class TransformationParser {
	private enum TransType{
		ROTATION,X_SCALE,Y_SCALE,FULL_SCALE,HORIZONTAL_FLIP,VERTICAL_FLIP;
	}
	private enum ParserState {
		TYPE,PARAMETER,APPLY;
	}

	private TransType currentType;
	private String currentParsePart;
	private ParserState currentState;
	private double currentParameter;
	private AffineTransform transform;


	public AffineTransform parseTransformation(String transString) throws ParseException {
		this.transform = new AffineTransform();

		String realIn = transString.replaceAll("\\s", "").toUpperCase();
		this.currentType = null;
		this.currentParsePart = "";
		this.currentState = ParserState.TYPE;
		this.currentParameter = 0;

		/*
		 * Legal Transformations:
		 * R(x)		Clockwise Rotation (deg)
		 * SX(x)	X Scaling
		 * SY(x)	Y Scaling
		 * SF(x)	X/Y Scaling
		 * HF		Horizontal Flip
		 * VF		Vertical Flip
		 */

		for(int i=0;i<realIn.length();i++) {
			this.currentParsePart+=realIn.charAt(i);

			switch (this.currentState) {
			case TYPE:
				parseType();
				break;
				
			case PARAMETER:
				parseParameter();
				break;
				
			case APPLY:
				applyType();
				break;

			default:
				break;
			}

		}


		return this.transform;
	}


	private void parseParameter()  throws ParseException{
		if(!this.currentParsePart.endsWith(")")) {
			return;
		}
		
		this.currentParsePart = this.currentParsePart.replaceAll(")", "");
		
		try {
			this.currentParameter = Double.parseDouble(this.currentParsePart);
		} catch (NumberFormatException e) {
			throw new ParseException("Unparseable Parameter \""+this.currentParsePart+"\"");
		}
		this.currentParsePart = "";
		this.currentState = ParserState.APPLY;
		
	}


	private void parseType() throws ParseException {
		if(!this.currentParsePart.endsWith("(")) {
			return;
		}
		
		this.currentParsePart = this.currentParsePart.replaceAll("(", "");
		//Look for Type
		switch(this.currentParsePart) {
		case "R":	
			this.currentType = TransType.ROTATION;
			this.currentState = ParserState.PARAMETER;
			break;

		case "SX":	
			this.currentType = TransType.X_SCALE;
			this.currentState = ParserState.PARAMETER;
			break;
		case "SY":	
			this.currentType = TransType.Y_SCALE;
			this.currentState = ParserState.PARAMETER;
			break;
		case "SF": 	
			this.currentType = TransType.FULL_SCALE;
			this.currentState = ParserState.PARAMETER;
			break;

			//These have no parameters
		case "HF":
			this.currentType = TransType.HORIZONTAL_FLIP;
			this.currentState = ParserState.APPLY;
			break;
		case "VF":
			this.currentType = TransType.VERTICAL_FLIP;
			this.currentState = ParserState.APPLY;
			break;	

		default:
			throw new ParseException("Unkown Transformation \""+this.currentParsePart+"\"");
		}

		this.currentParsePart = "";

	}

	private void applyType() {
		switch(this.currentType) {
		case ROTATION:
			this.transform.rotate(Math.toRadians(this.currentParameter));
			break;
		case X_SCALE:
			this.transform.scale(this.currentParameter, 1);
			break;
			
		case Y_SCALE:
			this.transform.scale(1, this.currentParameter);
			break;
			
		case FULL_SCALE:
			this.transform.scale(this.currentParameter, this.currentParameter);
			break;
		case HORIZONTAL_FLIP:
			this.transform.scale(-1, 1);
			break;

		case VERTICAL_FLIP:
			this.transform.scale(1, -1);
			break;
		default:
			break;
		}
		this.currentParameter = 0;
		this.currentState = ParserState.TYPE;

	}
}
