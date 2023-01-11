import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.text.DefaultEditorKit.CopyAction;

public class ImageProcessor {
	
	private BufferedImage orginalImage;
	private BufferedImage resizeImage;
	
	private int width;
	private int height;
	
	public	ImageProcessor(String path) {
		try {
			File pathToFile = new File(path);
			orginalImage = ImageIO.read(pathToFile);
			width = orginalImage.getWidth();
			height = orginalImage.getHeight();
			resizeImage = deepCopy();
					
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Path is not found");
		}
	}
	

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public RGBColor getRGB(int row, int col) {
		return convertToRBG(orginalImage.getRGB(col, row));
	}
	
	public int compareColor(int x1, int y1, int x2, int y2) {
		RGBColor color1 = getRGB(x1, y1);
		RGBColor color2 = getRGB(x2, y2);
		
		return Math.abs(color1.getRed() - color2.getRed()) + Math.abs(color1.getGreen() - color2.getGreen())
			+  Math.abs(color1.getBlue() - color2.getBlue());
		
	}
	private RGBColor convertToRBG(int color) {
		int R = (color & 0xff0000) >> 16;
		int G = (color & 0xff00) >> 8;
		int B = color & 0xff;
		return new RGBColor(R, G, B);
	}
	

	
	public void printImage() {
	    for (int i = 0; i < height; i++) {
	    	System.out.println("\nRow" + "i=" + i + ":");
	    	
	        for (int j = 0; j < width; j++) {
	        	System.out.print(j + ":" + getRGB(i, j).toString() + ",");
	          
	        }
	    }
	}
	
	
	/**
	 * 
	 * {@link https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage}
	 * @return 
	 */
	private BufferedImage deepCopy () {
		    ColorModel cm = orginalImage.getColorModel();
		    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		    WritableRaster raster = orginalImage.copyData(orginalImage.getRaster().createCompatibleWritableRaster());
		    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);

	}
	@Override
	public String toString() {

		return "ImageProcessor [image=," + "width=" + width + ", height=" + height + "] \n";
	}

	public static void main(String [] args) throws IOException {
		ImageProcessor ip = new ImageProcessor("images/1.png");
		SeamProcessor sp = new SeamProcessor(ip);
		int [] seam = sp.compueteVerticalSeam();
		Graphics g = ip.resizeImage.getGraphics();
		for (int i = 0; i < ip.height; i++) {
				ip.resizeImage.setRGB(seam[i], i, ip.resizeImage.getRGB(0, 0));
		}
        File outputFile = new File("images/1copy.png");
        ImageIO.write(ip.resizeImage,"png", outputFile);
        System.out.println("Writing completed.");
		
		
	}
}
