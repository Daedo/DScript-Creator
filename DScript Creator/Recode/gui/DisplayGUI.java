package gui;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.batik.swing.JSVGCanvas;

import textparser.Connection;
import textparser.Glyph;
import utils.Point;
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
		addGlyphs(glyph);
		//testAdd(input);
	}
	
	void addGlyphs(Glyph root) {
		Stack<Glyph> workGlyphs				= new Stack<>();
		Stack<Point<Integer>> workPositions = new Stack<>();
		Stack<Integer> workConnection		= new Stack<>();
		
		workGlyphs.push(root);
		workPositions.push(new Point<>(new Integer(50), new Integer(50)));
		//hasToPaint.push(new Boolean(true));
		workConnection.push(new Integer(0));
		
		while(!workGlyphs.isEmpty()) {
			Glyph currentGlyph 				= workGlyphs.pop();
			int currentConnectionID			= workConnection.pop().intValue();
			Point<Integer> currentPosition	= workPositions.pop();
			
			if(currentConnectionID==0) {
				try {
					JSVGCanvas canvas = new JSVGCanvas();
					canvas.setURI(new File("Ligatures\\Basic\\"+currentGlyph.getLigature()+"1.svg").toURI().toURL().toString());
					getContentPane().add(canvas);
					canvas.setBounds(currentPosition.x.intValue(), currentPosition.y.intValue(), 200, 200);
					canvas.setBackground(new Color(255, 255, 255, 0));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.print("Paint: ");
			}
			System.out.println(currentGlyph+" "+currentConnectionID+currentPosition.x+" | "+currentPosition.y);
			
			
			Connection currentConnection = currentGlyph.getConnection(currentConnectionID);
			if(currentConnection!=null) {
				workGlyphs.push(currentGlyph);
				workConnection.push(new Integer(currentConnectionID+1));
				workPositions.push(currentPosition);
				
				//Calculate new Position
				String type = currentConnection.getType();
				
				
				Integer newX = new Integer(currentPosition.x.intValue());
				Integer newY = new Integer(currentPosition.y.intValue()+50);
				Point<Integer> newPoint = new Point<>(newX, newY);
				
				Glyph endGlyph = currentConnection.getEnd();
				workGlyphs.push(endGlyph);
				workConnection.push(new Integer(0));
				workPositions.push(newPoint);
			}	
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
				outX += outCP.getX();
				outY += outCP.getY();
			}
			ConnectionPoint inCP  = lig.getConnectionPoint(1);
			int inX = inCP.getX();
			int inY = inCP.getY();
			outX -= inX;
			outY -= inY;
			
			System.out.println(inX+"|"+inY);
			System.out.println(outX +"|"+ outY);
			
			canvas.setBounds(outX, outY, 200, 200);
			canvas.setBackground(new Color(255, 255, 255, 0));
			
			prevLig = lig;
			
			
		}
	}
}
