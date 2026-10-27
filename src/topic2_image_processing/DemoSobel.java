package topic2_image_processing;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.application.Options;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetBoolean;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetImageChooser;
import xyz.marsavic.geometry.Vector;
import topic2_image_processing.filters.misc.Sobel;



public class DemoSobel implements Drawing {
	
	@GadgetImageChooser
	Image originalImage;
	
	@GadgetDouble(p = 0, q = 1)
	double angle = 0;
	
	@GadgetBoolean
	Boolean applyFilter = false;
	
	
	
	@Override
	public void init() {
		originalImage = new Image("images/couple.png");
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		Vector[][] gradient = Sobel.gradient(originalImage);
		Image filteredImage = Sobel.imgEmboss(gradient, angle);
		
		view.drawImageCenteredNativeOrientation(Vector.ZERO, applyFilter ? filteredImage : originalImage);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(Options.redrawOnEvents());
	}
}
