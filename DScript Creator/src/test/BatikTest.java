package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.batik.swing.JSVGCanvas;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JList;

import java.awt.Insets;

public class BatikTest extends JFrame {

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
			public void run() {
				try {
					BatikTest frame = new BatikTest();
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
	public BatikTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{50, 50, 0};
		gbl_contentPane.rowHeights = new int[]{252, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.contentPane.setLayout(gbl_contentPane);
		try {
			
			
			GridBagConstraints gbc_svgCanvas = new GridBagConstraints();
			gbc_svgCanvas.anchor = GridBagConstraints.EAST;
			gbc_svgCanvas.insets = new Insets(0, 0, 5, 0);
			gbc_svgCanvas.fill = GridBagConstraints.VERTICAL;
			gbc_svgCanvas.gridx = 1;
			gbc_svgCanvas.gridy = 0;
			JSVGCanvas svgCanvas = new JSVGCanvas();
			this.contentPane.add(svgCanvas, gbc_svgCanvas);
			svgCanvas.setURI(new File("Ligatures\\Basic\\a1.dsvg").toURL().toString());
			
			GridBagConstraints gbc_list = new GridBagConstraints();
			gbc_list.anchor = GridBagConstraints.WEST;
			gbc_list.fill = GridBagConstraints.VERTICAL;
			gbc_list.gridx = 0;
			gbc_list.gridy = 0;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
