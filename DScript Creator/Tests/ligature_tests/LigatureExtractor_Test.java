package ligature_tests;

import static org.junit.Assert.*;
import ligatures.LigatureExtractor;

import org.junit.Test;

public class LigatureExtractor_Test {

	@Test
	public void testExtractDrawData() {
		LigatureExtractor.extractDrawData("Ligatures\\Basic\\e1.svg");
		
	}

}
