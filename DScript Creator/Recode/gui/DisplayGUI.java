package gui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import textparser.Glyph;

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
			add(canvas);
			canvas.setBounds(0, 0, 200, 200);
			canvas.setBackground(new Color(255, 255, 255, 0));
			
			canvas = new JSVGCanvas();
			canvas.setURI(new File("Ligatures\\Basic\\o1.dsvg").toURI().toURL().toString());
			add(canvas);
			canvas.setBounds(100, 60, 200, 200);
			canvas.setBackground(new Color(255, 255, 255, 0));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	void testAdd(String in) {
		try {
			for(int i=0;i<in.length();i++) {
				String path = "Ligatures\\Basic\\"+in.charAt(i)+"1.dsvg";
				JSVGCanvas canvas = new JSVGCanvas();
				canvas.setURI(new File(path).toURI().toURL().toString());
				add(canvas);
				canvas.setBounds(0, 200*i, 200, 200);
				canvas.setBackground(new Color(255, 255, 255, 0));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	static void debugDocument(Document doc) {
		OutputFormat format = new OutputFormat(doc);
		format.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer(System.out, format);
		try {
			serializer.serialize(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
