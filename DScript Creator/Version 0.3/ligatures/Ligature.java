package ligatures;

import java.util.HashMap;

import org.w3c.dom.NodeList;

import ligature.dataPoint.DataExtractor;
import ligature.dataPoint.LigatureConnectionPoint;

public class Ligature {
	public String picturePath;
	public String dataPath;
	public String ligatureName;
	
	public Ligature(String name,String picPath,String datPath) throws ExtractException {
		this.ligatureName	= name;
		this.picturePath 	= picPath;
		this.dataPath	 	= datPath;
		
		this.connecions 	= DataExtractor.extractData(datPath);
		this.drawData		= LigatureExtractor.extractDrawData(picPath);
	}
	
	public HashMap<Integer, LigatureConnectionPoint> connecions;
	public NodeList drawData;
}
