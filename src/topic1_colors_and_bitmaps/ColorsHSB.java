package topic1_colors_and_bitmaps;


import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetColorPicker;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.geometry.Vector;

public class ColorsHSB implements Drawing {

	@GadgetColorPicker
	Color colorBackground = Color.gray(0.125);
	
	@GadgetInteger(min = 1, max = 400)
	int size = 400;
	
	@GadgetDouble
	double brightness = 1.0;
	
	
	
	public Image image() {
		WritableImage image = new WritableImage(size, size);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				double dx = 2 * x / image.getWidth() - 1;              // Normalizujemo na interval [-1, 1)
				double dy = 2 * y / image.getHeight() - 1;
				
				double r = Math.sqrt(dx*dx + dy*dy);                   // Udaljenost od centra.
				double phi = Math.atan2(dy, dx) * 360 / (2 * Math.PI); // Ugao (0-360)
				
				Color c = r <= 1 ? Color.hsb(phi, r, brightness) : Color.TRANSPARENT;
				pw.setColor(x, y, c);
			}
		}
		
		return image;
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, colorBackground);
		view.drawImageCenteredNativeOrientation(Vector.ZERO, image());
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
		
}
