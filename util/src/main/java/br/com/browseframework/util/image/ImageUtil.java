package br.com.browseframework.util.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Load image from class path, convert between images types, resizes etc. 
 * @author Eduardo
 *
 */
public class ImageUtil {
	
	/**
	 * Load an image from class path rousources.
	 * @param dim
	 * @param imageResoucePath
	 * @return
	 */
	public static Icon loadImageResource(String imageResoucePath) {
		return loadImageResource(null, imageResoucePath);
	}

	/**
	 * Load an image from class path rousources and resizes.s
	 * @param dim
	 * @param imageResoucePath
	 * @return
	 */
	public static Icon loadImageResource(Dimension dim, String imageResoucePath){
		Icon result = null;
		Image ir = null;
		if (dim != null) {
			ir = new ImageIcon(ImageUtil.class.getClass().getResource(imageResoucePath)).getImage().getScaledInstance((int) dim.getWidth(), (int) dim.getHeight(), Image.SCALE_SMOOTH);
		} else {
			ir = new ImageIcon(ImageUtil.class.getClass().getResource(imageResoucePath)).getImage();
		}

		result = new ImageIcon(ir);
		return result;
	}

	/**
	 * Converts a byte array image in its corresponding image.
	 * @param bytes
	 * @return
	 */
	public static Image convertByteToImage(byte[] bytes) {
		if (bytes == null) {
			return null;
		} else {
			return Toolkit.getDefaultToolkit().createImage(bytes);
		}
	}

	/**
	 * Converts an image into a byte array.
	 * @param image
	 * @return
	 */
	public static byte[] convertImageToByte(Image image) {
		final BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(image, 0, 0, null);
		bg.dispose();

		ByteArrayOutputStream buff = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "PNG", buff); // FIXME static PNG
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff.toByteArray();
	}

	/**
	 * Converts a Buffred Image into a byte array.
	 * @param originalImage
	 * @return
	 * @throws IOException
	 */
	public static byte[] convertBufferedImageToByte(BufferedImage originalImage)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "PNG", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

	/**
	 * Converts Image to Buffred Image
	 * @param image
	 * @return
	 */
	public static BufferedImage convertImageToBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see Determining If an Image Has Transparent Pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null),
					image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	/**
	 * Used by br.com.browseframework.util.image.ImageUtil.convertImageToBufferedImage(Image) method
	 * @param image
	 * @return
	 */
	private static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage) image;
			return bimage.getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}

	/**
	 * 
	 * @param bufferedImage
	 * @return
	 */
	public static Image convertBufferedImageToImage(BufferedImage bufferedImage) {
		return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
	}

	/**
	 * Convert Buffred Image to Input Stream
	 * @param bi
	 * @param formato
	 * @return
	 * @throws IOException
	 */
	public static InputStream convertBufferedImageToInputStream(
			BufferedImage bi, String formato) throws IOException {
		if (formato == null) {
			formato = "png"; // FIXME static PNG
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bi, formato, os);
		InputStream retorno = new ByteArrayInputStream(os.toByteArray());
		return retorno;
	}
	
	/**
	 * Resizes a Buffred Image
	 * - Do not use with transparent images
	 * @param new_widht
	 * @param new_height
	 * @param imagem
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage resizeBuffredImage(int new_widht, int new_height, BufferedImage imagem) throws IOException {
		final BufferedImage new_img = new BufferedImage(new_widht, new_height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = new_img.createGraphics();
		// redrwas image with new size
		g.drawImage(imagem, 0, 0, new_widht, new_height, null);
		return convertImageToBufferedImage(new_img);
	}

	/**
	 * Resizes an Image
	 * @param new_widht
	 * @param new_heigh
	 * @param imagem
	 * @return
	 */
	public static Image resizeImage(int new_widht, int new_heigh, Image imagem) {
		return imagem.getScaledInstance(new_widht, new_heigh, Image.SCALE_FAST);
	}
	
	/**
	 * Calculate image size limits to fix on given boundaries.
	 * @param widthMax
	 * @param heightMax
	 * @param width
	 * @param height
	 * @return
	 */
	public static float[] calculateImageMaximumProportion(float widthMax, float heightMax, float width, float height) {
		float[] retorno = new float[2];

		float imgAlt = height;
		float imgLarg = width;
		float novaLargura = 0, novaAltura = 0;
		// checks whether the image is greater than the maximum allowed
		if (imgLarg > heightMax || imgAlt > widthMax) {
			// checks if the width is greater than the height
			if (imgLarg > imgAlt) {
				novaLargura = widthMax;
				novaAltura = Math.round((novaLargura / imgLarg) * imgAlt);
			}
			// if the height is larger than the width
			else if (imgAlt > imgLarg) {
				novaAltura = heightMax;
				novaLargura = Math.round((novaAltura / imgAlt) * imgLarg);
			}
			//  Height == width
			else {
				novaAltura = novaLargura = heightMax;
			}

			retorno[0] = novaLargura;
			retorno[1] = novaAltura;
		}
		return retorno;
	}


}
