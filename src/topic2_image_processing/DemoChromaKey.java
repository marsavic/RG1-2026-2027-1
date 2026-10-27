package topic2_image_processing;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.application.Options;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetImageChooser;
import xyz.marsavic.geometry.Vector;
import topic2_image_processing.filters.BinaryFilter;
import topic2_image_processing.filters.binary.ChromaKey;


public class DemoChromaKey implements Drawing {
	
	@GadgetImageChooser
	Image originalImage1, originalImage2;

	@GadgetDouble(p = 0, q = 360)
	double hue = 120;
	
	
	@Override
	public void init() {
		originalImage1 = new Image("images/meterologist.png");
		originalImage2 = new Image("images/forecast.png");
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.2));

		BinaryFilter filter = new ChromaKey(hue);
		try {
			Image filteredImage = filter.process(originalImage1, originalImage2);
			view.drawImageCenteredNativeOrientation(Vector.ZERO, filteredImage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			DrawingUtils.drawInfoText(view, e.toString());
		}
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(Options.redrawOnEvents());
	}
}
