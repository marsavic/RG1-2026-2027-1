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
import topic2_image_processing.filters.displacement.Swirl;


public class DemoSwirl implements Drawing {
	
	@GadgetImageChooser
	Image originalImage;
	
	@GadgetDouble(p = 0, q = 16)
	double f = 6;

	@GadgetDouble(p = -1, q = 1)
	double a = 0.012;


	
	@Override
	public void init() {
		originalImage = new Image("images/monalisa.png");
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.2));

		Filter filter = new Swirl(f, a);
		Image filteredImage = filter.process(originalImage);
		view.drawImageCenteredNativeOrientation(Vector.ZERO, filteredImage);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(Options.redrawOnEvents());
	}
}
