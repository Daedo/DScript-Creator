package ligatures;

import java.util.HashMap;

import ligature.dataPoint.LigatureConnectionPoint;

public class Ligature {
	public String picturePath;
	public String dataPath;
	public String ligatureName;
	
	public Ligature(String name,String picPath,String datPath) {
		this.ligatureName= name;
		this.picturePath = picPath;
		this.dataPath	 = datPath;
	}
	
	public HashMap<Integer, LigatureConnectionPoint> connecions;
}
