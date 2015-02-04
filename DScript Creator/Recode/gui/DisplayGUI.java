package gui;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.w3c.dom.svg.SVGDocument;

import textparser.Connection;
import textparser.Glyph;
import utils.Point;
import files.ConnectionPoint;
import files.DocumentBuilder;
import files.Ligature;
import files.PropetyInformation;

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
	public DisplayGUI(String input, Vector<Glyph> words) {
		if(input==null || words==null) {
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
		addWords(words);
	}
	
	void addWords(Vector<Glyph> words) {
		SVGDocument doc = DocumentBuilder.buildWords(words);
		JSVGCanvas canvas = new JSVGCanvas();
		canvas.setDocument(doc);
		canvas.setBounds(10,10, 2000, 2000);
		canvas.setBackground(new Color(255, 255, 255, 0));
		JSVGScrollPane scroll = new JSVGScrollPane(canvas);
		setContentPane(scroll);
		//getContentPane().add(scroll);
		
		/*
		for(int i=0;i<words.size();i++) {
			SVGDocument doc = DocumentBuilder.buildDocument(words.elementAt(i), 10+100*i, 0);
			wordDocs.add(doc);
			JSVGCanvas canvas = new JSVGCanvas();
			canvas.setDocument(doc);
			canvas.setBounds(10,10, 2000, 2000);
			canvas.setBackground(new Color(255, 255, 255, 0));
			getContentPane().add(canvas);
		}*/
		
		
		
		/*JSVGCanvas canvas = new JSVGCanvas();
		for(int i=0;i<words.size();i++) {
			
			addGlyphs(words.elementAt(i), 10+100*i, 0);
			
		}
		canvas.setDocument(DocumentBuilder.buildDocument(null, 0, 0));
		this.contentPane.add(canvas);
		canvas.setBounds(10,10, 200, 200);
		canvas.setBackground(new Color(255, 255, 255, 0));
		/*for(int i=0;i<words.size();i++) {
			addGlyphs(words.elementAt(i), 10+100*i, 0);
		}*/
	}
	
	void addGlyphs(Glyph root, double startX, double startY) {
		Stack<Glyph> workGlyphs				= new Stack<>();
		Stack<Point<Double>> workPositions = new Stack<>();
		Stack<Integer> workConnection		= new Stack<>();
		
		workGlyphs.push(root);
		workPositions.push(new Point<>(new Double(startX), new Double(startY)));
		workConnection.push(new Integer(0));
		
		while(!workGlyphs.isEmpty()) {
			Glyph currentGlyph 				= workGlyphs.pop();
			int currentConnectionID			= workConnection.pop().intValue();
			Point<Double> currentPosition	= workPositions.pop();
			
			String svgPath					= PropetyInformation.getSVGPath(currentGlyph.getLigature());
			
			if(currentConnectionID==0) {
				try {
					JSVGCanvas canvas = new JSVGCanvas();
					canvas.setURI(new File(svgPath).toURI().toURL().toString());
					this.contentPane.add(canvas);
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
				System.out.println(type);
				String[] attribs = type.split(",");
				if(attribs.length<2) {
					//Error
					return;
				}
				int outAtrib = Integer.parseInt(attribs[0].trim());
				int inAtrib  = Integer.parseInt(attribs[1].trim());
				
				Glyph endGlyph = currentConnection.getEnd();
				Ligature prevLig = new Ligature(currentGlyph.getLigature());
				Ligature newLig   = new Ligature(endGlyph.getLigature());
				
				ConnectionPoint outP = prevLig.getConnectionPoint(outAtrib);
				ConnectionPoint inP  = newLig.getConnectionPoint(inAtrib);
				
				double outMoveX = 0;
				double outMoveY = 0;
				
				if(outP!=null) {
					outMoveX = outP.getX();
					outMoveY = outP.getY();
				}
				
				double inMoveX  = 0;
				double inMoveY  = 0;
				if(inP!=null) {
					inMoveX = inP.getX();
					inMoveY = inP.getY();
				}
				
				double xMove = outMoveX - inMoveX;
				double yMove = outMoveY - inMoveY;
				
				Double newX = new Double(currentPosition.x.intValue()+xMove);
				Double newY = new Double(currentPosition.y.intValue()+yMove);
				Point<Double> newPoint = new Point<>(newX, newY);
				
				
				workGlyphs.push(endGlyph);
				workConnection.push(new Integer(0));
				workPositions.push(newPoint);
			}	
		}
	}
}
