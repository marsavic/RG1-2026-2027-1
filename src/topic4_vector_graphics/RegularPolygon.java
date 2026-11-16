package topic4_vector_graphics;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.geometry.Vector;


public class RegularPolygon implements Drawing {
	@GadgetInteger
	int n = 6;

	@GadgetDouble(p = 0, q = 300)
	double r = 200.0;                          // Poluprečnik poligona (rastojanje od centra do temena)

	@GadgetDouble(p = 0, q = 1)
	double alpha = 0.0;                        // Ugao početnog temena (u okretima)
	
	

	Vector getVertex(int i) {                  // Vraća položaj i-tog temena
		return Vector.polar(0, 0);
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.hsb(0, 0, 0.2));
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}
