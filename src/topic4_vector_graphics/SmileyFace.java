package topic4_vector_graphics;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;

import static xyz.marsavic.geometry.Vector.*;

public class SmileyFace implements Drawing {

	@GadgetDouble(p = 0, q = 200)
	double r = 200;                        // Poluprečnik glave.
	
	@GadgetDouble(p = 0, q = 100)
	double rEye = 30;                      // Poluprečnik oka.

	@GadgetDouble(p = 0, q = 200)
	double xEye = 70;                      // Udaljenost oka od centra po x-osi.

	@GadgetDouble(p = 0, q = 300)
	double yEye = 70;                      // Udaljenost oka od centra po y-osi.

	@GadgetDouble(p = 0, q = 200)
	double rMouth = 130;                   // Poluprečnik luka usta.
	
	@GadgetDouble(p = 0, q = 0.5)
	double phiMouth = 1.0/3;               // Ugao luka usta.
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		// Glava
		view.setFill(Color.hsb(60, 0.9, 0.9));
		view.fillCircleCentered(ZERO, r);
		
		// Oči
		view.setFill(Color.BLACK);
		view.fillCircleCentered(xy( xEye, yEye), rEye);
		view.fillCircleCentered(xy(-xEye, yEye), rEye);
		
		// Usta
		view.setLineWidth(15);
		view.setStroke(Color.BLACK);
		view.strokeArcCentered(ZERO, xy(rMouth), 0.75 - phiMouth/2, phiMouth);
	}
	
	
	public static void main() {
		DrawingApplication.launch(600, 600);
	}
}
