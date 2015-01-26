package gui;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.batik.swing.JSVGCanvas;

import textparser.Glyph;
import dsvg.ConnectionPoint;
import dsvg.DSVGParser;
import dsvg.Ligature;

public class DisplayGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;



	/**
	 * Create the frame.
	 * @param glyph 
	 */
	public DisplayGUI(String input, Glyph glyph) {
		if(input==null || glyph==null) {
			this.dispose();
			return;
		}
		
		System.out.println(input);
		
		String title = "";
		if(input.length()<=10) {
			title = input;
		} else {
			title = input.substring(0,10)+"...";
		}
		
		this.setTitle(title);
		this.setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);
		//addGlyphs(glyph);
		testAdd(input);
	}
	
	void addGlyphs(Glyph root) {
		try {
			JSVGCanvas canvas = new JSVGCanvas();
			canvas.setURI(new File("Ligatures\\Basic\\a1.dsvg").toURI().toURL().toString());
			getContentPane().add(canvas);
			canvas.setBounds(0, 0, 200, 200);
			canvas.setBackground(new Color(255, 255, 255, 0));
			
			canvas = new JSVGCanvas();
			canvas.setURI(new File("Ligatures\\Basic\\o1.dsvg").toURI().toURL().toString());
			getContentPane().add(canvas);
			canvas.setBounds(100, 60, 200, 200);
			canvas.setBackground(new Color(255, 255, 255, 0));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	void testAdd(String in) {
		Ligature prevLig = null;
		int outX = 50;
		int outY = 50;
		
		for(int i=0;i<in.length();i++) {
			String svgPath = "Ligatures\\Basic\\"+in.charAt(i)+"1.svg";
			String xmlPath = "Ligatures\\Basic\\"+in.charAt(i)+"1.xml";
			Ligature lig = DSVGParser.parseDSVFile(svgPath,xmlPath);
			
			System.out.println(lig.toString());
			
			JSVGCanvas canvas = new JSVGCanvas();
			try {
				canvas.setURI(new File(lig.getSvgDocument()).toURI().toURL().toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			getContentPane().add(canvas);
			

			if(prevLig!=null) {
				ConnectionPoint outCP = prevLig.getConnectionPoint(2);
				ConnectionPoint outInCP = prevLig.getConnectionPoint(1);
				outX += outCP.getX()-outInCP.getX();
				outY += outCP.getY()-outInCP.getY();
			}
			ConnectionPoint inCP  = lig.getConnectionPoint(1);
			int inX = inCP.getX();
			int inY = inCP.getY();
			
			System.out.println(outX+"|"+outY);
			System.out.println(inX+"|"+inY);
			int posX = outX-inX;
			int posY = outY-inY;
			System.out.println(posX +"|"+ posY);
			canvas.setBounds(posX, posY, 200, 200);
			canvas.setBackground(new Color(255, 255, 255, 0));
			
			prevLig = lig;
			//outX += inX;
			//outY += inY;
			
		}
	}
}
