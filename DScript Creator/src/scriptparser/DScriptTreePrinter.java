package scriptparser;

import java.util.Vector;

public class DScriptTreePrinter {
	boolean isReady;
	DScriptBlock treeRoot;

	public DScriptTreePrinter() {
		this.isReady = false;
		this.treeRoot = null;
	}

	public DScriptTreePrinter(DScriptBlock root) {
		if(root != null) {
			this.isReady = true;
			this.treeRoot= root;
		} else {
			this.isReady = false;
		}
	}

	public void setTreeRoot(DScriptBlock root) {
		if(root != null) {
			this.isReady = true;
			this.treeRoot= root;
		}
	}


	public void printTree() {
		if(this.isReady) {
			Vector<DScriptBlock> roots = new Vector<>();
			roots.add(this.treeRoot);

			while(!roots.isEmpty()) {
				int maxHeight = 0;
				for(DScriptBlock root:roots) {
					maxHeight = Math.max(maxHeight, root.getText().length());
				}

				//We always want one free Space between groups
				maxHeight++;

				for(int position = 0; position < maxHeight; position++) {

					String outText = "";

					for(DScriptBlock block:roots) {
						String rootText = block.getText();
						int leftSpace = block.getLeftSpace();

						String whitespace = utils.Utilitys.repeatString("  ", leftSpace);
						outText+=whitespace;
						
						if(rootText.length()>position) {
							outText += rootText.charAt(position)+" ";
						} else {
							if(block.chainExist("c")) {
								outText += "| ";
							} else {
								outText += "  ";
							}
						}
					}
					

					System.out.println(outText);
				}
				
				//Add next layer of Blocks to the Vector
				//Reading order: l i c r
				Vector<DScriptBlock> tempVector = new Vector<>();

				for(DScriptBlock root:roots) {
					if(root.chainExist("l")) {
						tempVector.add(root.getChain("l"));
					}

					if(root.chainExist("i")) {
						tempVector.add(root.getChain("i"));
					}

					if(root.chainExist("c")) {
						tempVector.add(root.getChain("c"));
					}
					
					if(root.chainExist("r")) {
						tempVector.add(root.getChain("r"));
					}
				}
				roots.clear();
				roots.addAll(tempVector);
			}

		} else {
			System.out.println("ERROR: DScriptTreePrinter wasn't initialized");
		}
	}
}
