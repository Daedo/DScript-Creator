package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.xerces.impl.xs.identity.Field;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JTextArea;

import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;

public class GUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
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
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0};
		gbl_contentPane.rowWeights = new double[]{Double.MIN_VALUE, 1.0};
		this.contentPane.setLayout(gbl_contentPane);


		final JSVGCanvas svgCanvas = new JSVGCanvas();
		GridBagConstraints gbc_svgCanvas = new GridBagConstraints();
		gbc_svgCanvas.gridwidth = 2;
		gbc_svgCanvas.insets = new Insets(0, 0, 5, 0);
		gbc_svgCanvas.gridx = 0;
		gbc_svgCanvas.gridy = 0;
		gbc_svgCanvas.fill = GridBagConstraints.BOTH;
		this.contentPane.add(svgCanvas, gbc_svgCanvas);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		this.contentPane.add(scrollPane, gbc_scrollPane);

		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JButton btnDisplay = new JButton("Display");
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = textArea.getText();

				try {
					File f = new File(input);
					svgCanvas.setURI(f.toURI().toURL().toString());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		});
		GridBagConstraints gbc_btnDisplay = new GridBagConstraints();
		gbc_btnDisplay.gridx = 1;
		gbc_btnDisplay.gridy = 1;
		this.contentPane.add(btnDisplay, gbc_btnDisplay);
	}

}
