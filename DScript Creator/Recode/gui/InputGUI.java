package gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import textparser.Glyph;
import textparser.ParseException;
import textparser.Parser;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.w3c.dom.svg.SVGDocument;

import files.DScriptPresenter;
import files.DocumentBuilder;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class InputGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					InputGUI frame = new InputGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InputGUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelpContent = new JMenuItem("Help Content");
		mntmHelpContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayHelpContent();
			}
		});
		mnHelp.add(mntmHelpContent);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayAboutContent();
			}
		});
		mnHelp.add(mntmAbout);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{205, 213, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		this.contentPane.setLayout(gbl_contentPane);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		this.contentPane.add(scrollPane, gbc_scrollPane);

		final JTextArea textArea = new JTextArea();
		textArea.setText("[Th]e [qu][i2]ck bro(4,1;w)n fox jumps over [th]e lazy dog");
		scrollPane.setViewportView(textArea);

		JButton btnTranslate = new JButton("Translate");
		btnTranslate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				translate(textArea.getText());
			}
		});
		GridBagConstraints gbc_btnTranslate = new GridBagConstraints();
		gbc_btnTranslate.insets = new Insets(0, 0, 0, 5);
		gbc_btnTranslate.gridx = 0;
		gbc_btnTranslate.gridy = 1;
		this.contentPane.add(btnTranslate, gbc_btnTranslate);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridheight = 2;
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 0;
		this.contentPane.add(separator, gbc_separator);
		
		JSVGCanvas display = new JSVGCanvas();
		
		SVGDocument doc = DScriptPresenter.buildFullPresentationDocument();
		Dimension docDim = DocumentBuilder.getSVGSize(doc);
		
		display.setBounds(0, 0, docDim.width, docDim.height);
		
		display.setDocument(doc);
		JSVGScrollPane scroll = new JSVGScrollPane(display);
		scroll.getCanvas().setBounds(0, 0, 200, 200);
		GridBagConstraints gbc_scroll = new GridBagConstraints();
		gbc_scroll.fill = GridBagConstraints.BOTH;
		gbc_scroll.gridx = 1;
		gbc_scroll.gridheight = 2;
		gbc_scroll.gridy = 0;
		gbc_separator.gridheight = 2;
		gbc_separator.gridx = 2;
		gbc_separator.gridy = 0;
		this.contentPane.add(scroll,gbc_scroll);
	}

	void translate(final String text) {
		if(text==null || text.equals("")) {
			return;
		}		
		Parser p = new Parser();
		try {
			final Vector<Glyph> words = p.parse(text);
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						DisplayGUI frame = new DisplayGUI(text,words); 
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (ParseException e1) {
			displayErrorMessage(e1.getMessage());
		}
	}
	
	void displayHelpContent() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TextFrame frame = TextFrame.createFrameFromFile("Ligatures\\Help.txt");
					frame.setVisible(true);
				} catch (Exception e) {
					displayErrorMessage(e.getMessage());
				}
			}
		});
	}
	
	void displayAboutContent() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TextFrame frame = TextFrame.createFrameFromFile("Ligatures\\About.txt");
					frame.setVisible(true);
				} catch (Exception e) {
					displayErrorMessage(e.getMessage());
				}
			}
		});
	}
	
	void displayErrorMessage(String message) {
		   JOptionPane.showMessageDialog(this,message,"Error", JOptionPane.ERROR_MESSAGE);
	}
}
