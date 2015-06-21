package ligature_tests;

import static org.junit.Assert.*;
import ligatures.ExtractException;
import ligatures.LigatureExtractor;

import org.junit.Test;

public class LigatureExtractor_Test {

	@Test
	public void testExtractDrawData() throws ExtractException {
		LigatureExtractor.extractDrawData("Ligatures\\Basic\\e1.svg");
		
	}

}
