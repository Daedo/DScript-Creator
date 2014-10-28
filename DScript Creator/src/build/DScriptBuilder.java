package build;

import scriptparser.DScriptBlock;
import scriptparser.DScriptLigature;

public class DScriptBuilder {
	
	public static final int BUILD_WORDSTART 	= 1;
	public static final int BUILD_WORDEND		= 2;
	
	public static final int BUILD_INPUT_CENTER	= 4;
	public static final int BUILD_INPUT_RIGHT	= 8;
	public static final int BUILD_INPUT_LEFT	= 16;
	public static final int BUILD_INPUT_INSIDE	= 32;
	
	public static final int BUILD_OUTPUT_RIGHT	= 64;
	public static final int BUILD_OUTPUT_LEFT	= 128;
	public static final int BUILD_OUTPUT_CENTER	= 256;
	public static final int BUILD_OUTPUT_INSIDE	= 512;
	
	public static void buildWord(DScriptBlock wordStart,int flag) {
		
	}
	
	public static void buildBlock(DScriptBlock block,int flag) {
		
	}
}
