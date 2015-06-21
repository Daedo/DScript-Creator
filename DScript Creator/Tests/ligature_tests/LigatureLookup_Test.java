package ligature_tests;

import static org.junit.Assert.*;
import ligature.dataPoint.LigatureConnectionPoint;
import ligatures.Ligature;
import ligatures.LigatureLookup;

import org.junit.Test;
import org.w3c.dom.NodeList;

import files.BuildingException;

public class LigatureLookup_Test {

	@Test
	public void testLookupLigature() throws BuildingException {
		Ligature lig = LigatureLookup.lookupLigature("g");
		
		assertEquals("Ligature Name","g",lig.ligatureName);
		assertEquals("Data Path","Ligatures\\Basic\\g1.xml",lig.dataPath);
		assertEquals("Draw Path","Ligatures\\Basic\\g1.svg",lig.picturePath);
	
		LigatureConnectionPoint p1 = new LigatureConnectionPoint(62.7, 5.47, true, false);
		LigatureConnectionPoint p2 = new LigatureConnectionPoint(50, 88, false, true);
		LigatureConnectionPoint p3 = new LigatureConnectionPoint(55, 55, false, true);
		
		assertEquals("Connection 1: ",p1,lig.connecions.get(new Integer(1)));
		assertEquals("Connection 2: ",p2,lig.connecions.get(new Integer(2)));
		assertEquals("Connection 3: ",p3,lig.connecions.get(new Integer(3)));
		
		NodeList list = lig.drawData;
		assertEquals("Draw data size", 1,list.getLength());
		assertEquals("Draw data type", "g",list.item(0).getNodeName());
	}
	
	@Test(expected = BuildingException.class)
	public void testLookupLigature_NullPointer() throws BuildingException {
		LigatureLookup.lookupLigature(null);
	}
	
	@Test(expected = BuildingException.class)
	public void testLookupLigature_NonExistingLig() throws BuildingException {
		LigatureLookup.lookupLigature("gh gkgbagbkga 423414");
	}
}
