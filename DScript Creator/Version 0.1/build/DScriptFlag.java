package build;

import java.util.HashMap;

public class DScriptFlag {
	private boolean  isWordstart;
	private boolean  isWordend;
	private HashMap<String, Boolean> inputFlags;
	private HashMap<String, Boolean> outputFlags;
	
	public DScriptFlag() {
		this.isWordstart = false;
		this.isWordend = false;
		this.inputFlags = new HashMap<>();
		this.outputFlags = new HashMap<>();
	}

	public void setInputFlag(String position,boolean flagValue) {
		this.inputFlags.put(position, new Boolean(flagValue));
	}
	
	public boolean getInputFlag(String position) {
		Boolean flagValue = this.inputFlags.get(position);
		
		if(flagValue==null) {
			return false;
		}
		return flagValue.booleanValue();
	}
	
	public void setOutputFlag(String position,boolean flagValue) {
		this.outputFlags.put(position, new Boolean(flagValue));
	}
	
	public boolean getOutputFlag(String position) {
		Boolean flagValue = this.outputFlags.get(position);
		
		if(flagValue==null) {
			return false;
		}
		return flagValue.booleanValue();
	}
	
	public boolean isWordstart() {
		return this.isWordstart;
	}

	public void setWordstart(boolean newIsWordstart) {
		this.isWordstart = newIsWordstart;
	}

	public boolean isWordend() {
		return this.isWordend;
	}

	public void setWordend(boolean newIsWordend) {
		this.isWordend = newIsWordend;
	}
	
	@Override
	public String toString() {
		String out = "Flag: ";
		if(this.isWordstart){
			out+="<Start> ";
		} else {
			out+="In: ";
			if(this.inputFlags.containsKey("c")) {
				out+="c ";
			}
			if(this.inputFlags.containsKey("l")) {
				out+="l ";
			}
			if(this.inputFlags.containsKey("r")) {
				out+="r ";
			}
			if(this.inputFlags.containsKey("i")) {
				out+="i ";
			}
		}
		
		if(this.isWordend){
			out+="<End> ";
		} else {
			out+="Out: ";
			if(this.outputFlags.containsKey("c")) {
				out+="c ";
			}
			if(this.outputFlags.containsKey("l")) {
				out+="l ";
			}
			if(this.outputFlags.containsKey("r")) {
				out+="r ";
			}
			if(this.outputFlags.containsKey("i")) {
				out+="i ";
			}
		}
		
		return out.trim();
	}
}
