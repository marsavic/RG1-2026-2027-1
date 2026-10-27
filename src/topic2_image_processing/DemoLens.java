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
import topic2_image_processing.filters.Filter;
import topic2_image_processing.filters.displacement.Lens;


public class DemoLens implements Drawing {
	
	@GadgetImageChooser
	Image originalImage;
	
	@GadgetDouble(p = 0, q = 4)
	double f = 0.5;

	
	@Override
	public void init() {
		originalImage = new Image("images/monalisa.png");
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.2));

		Filter filter = new Lens(f);
		Image filteredImage = filter.process(originalImage);
		view.drawImageCenteredNativeOrientation(Vector.ZERO, filteredImage);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(Options.redrawOnEvents());
	}
}
