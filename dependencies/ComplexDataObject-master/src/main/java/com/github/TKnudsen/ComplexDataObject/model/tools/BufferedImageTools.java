package com.github.TKnudsen.ComplexDataObject.model.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * <p>
 * Title: BufferedImageTools
 * </p>
 *
 * <p>
 * Description: some helpers when applying standard operations on BufferedImages
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.03
 */
public class BufferedImageTools {

	public static double getLuminanceforPixel(BufferedImage bi, int x, int y) {
		if (bi == null)
			throw new IllegalArgumentException("BufferedImageTools.getLuminanceforPixel(...): BufferedImage was null.");

		if (x < 0 || x >= bi.getWidth() || y < 0 || y >= bi.getHeight())
			throw new IndexOutOfBoundsException("BufferedImageTools.getLuminanceforPixel(...): pixel coordinates ill-defined.");

		int color = bi.getRGB(x, y);
		int R = (color >>> 16) & 0xFF;
		int G = (color >>> 8) & 0xFF;
		int B = (color >>> 0) & 0xFF;
		double luminance = (0.2126 * R + 0.7152 * G + 0.0722 * B) / 255.0;
		return luminance;
	}

	/**
	 * Gathers the Color of a given pixel in a given BufferedImage.
	 * 
	 * @param bi
	 * @param x
	 * @param y
	 * @return
	 */
	public static Color getColor(BufferedImage bi, int x, int y) {
		int rgb = bi.getRGB(x, y);
		Color color = new Color(rgb, true);
		return color;
	}

	/**
	 * gathers the colors of all pixels from a given BufferedImage
	 * 
	 * @param bufferedImage
	 * @return
	 */
	public static List<Color> getColors(BufferedImage bufferedImage) {
		if (bufferedImage == null)
			return null;

		List<Color> colors = new ArrayList<>();
		for (int x = 0; x < bufferedImage.getWidth(); x++)
			for (int y = 0; y < bufferedImage.getHeight(); y++)
				colors.add(getColor(bufferedImage, x, y));

		return colors;
	}

	/**
	 * 
	 * @param im
	 * @param color
	 * @return
	 */
	public static Image setTransparentColor(BufferedImage im, final Color color) {
		ImageFilter filter = new RGBImageFilter() {

			// the color we are looking for... Alpha bits are set to opaque
			public int markerRGB = color.getRGB() | 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else {
					// nothing to do
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	public static BufferedImage toBufferedImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();

		return bufferedImage;
	}

	/**
	 * resizes a bufferedImage to the given size properties.
	 * 
	 * @param source
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static BufferedImage resize(BufferedImage source, int targetWidth, int targetHeight) {
		Image tmp = source.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
		BufferedImage output = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = output.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return output;
	}

	/**
	 * rescales a bufferedImage with the given properties.
	 * 
	 * @param source
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static BufferedImage rescale(BufferedImage source, double factorX, double factorY) {
		int w = source.getWidth();
		int h = source.getHeight();

		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(factorX, factorY);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		output = scaleOp.filter(source, output);

		return output;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedImage loadBufferedImage(File file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedImage loadImage(String file) {
		File imageFile = new File(file);
		BufferedImage bufferedImage = null;

		if (imageFile.exists()) {
			try {
				bufferedImage = loadBufferedImage(imageFile);
			} catch (IndexOutOfBoundsException iobe) {
				iobe.printStackTrace();
			}
		}

		return bufferedImage;
	}
}
