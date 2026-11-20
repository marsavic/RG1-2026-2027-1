package topic4_vector_graphics;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetColorPicker;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;

import static xyz.marsavic.geometry.Vector.*;


public class Chessboard implements Drawing {

	@GadgetInteger(min = 1, max = 20)
	int n = 8;                           // Broj vrsta

	@GadgetInteger(min = 1, max = 20)
	int m = 8;                           // Broj kolona

	@GadgetDouble(p = 0, q = 100)
	double d = 50.0;                     // Dužina stranice jednog polja
	
	
	@GadgetColorPicker
	Color c1 = Color.hsb(30, 0.4, 0.4);  // Tamnija boja
	
	@GadgetColorPicker
	Color c2 = Color.hsb(30, 0.4, 0.6);  // Svetlija boja
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.1));
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if ((i + j) % 2 == 0) {
					view.setFill(c1);
				}
				else {
					view.setFill(c2);
				}
				// Svetlija i tamnija polja naizmenično
				
				view.fillBox(xy(d).mul(xy(j - m/2.0, i - n/2.0)), xy(d));
				
				// d*(j, i) - d*(m/2, n/2) = d*(j - m/2, i - n/2)
			}
		}
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
	
}
