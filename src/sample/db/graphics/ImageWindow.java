package sample.db.graphics;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ImageWindow {

	JFrame editorFrame;

	public ImageWindow() {

	}

	public void showImage(final String filename) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				editorFrame = new JFrame("Image Window");
				editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File(filename));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				ImageIcon imageIcon = new ImageIcon(image);
				JLabel jLabel = new JLabel();
				jLabel.setIcon(imageIcon);
				editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
	}

	public void showBlob(final InputStream stream) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				editorFrame = new JFrame("Image Window");
				editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				BufferedImage image = null;
				try {
					image = ImageIO.read(stream);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				ImageIcon imageIcon = new ImageIcon(image);
				JLabel jLabel = new JLabel();
				jLabel.setIcon(imageIcon);
				editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
	}
}