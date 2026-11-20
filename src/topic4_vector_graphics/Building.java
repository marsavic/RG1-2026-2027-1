package topic4_vector_graphics;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.geometry.Vector;

import static xyz.marsavic.geometry.Vector.*;


public class Building implements Drawing {
	
	@GadgetInteger(min = 1, max = 10)
	int n = 2;                           // Broj vrsta

	@GadgetInteger(min = 1, max = 10)
	int m = 3;                           // Broj kolona
	
	
	Vector w = xy(30, 20);          // Dimenzije prozora
	Vector g = xy(10, 10);          // Razmak između prozora

	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.2));

		// Žuti zid
		Vector r = w.mul(xy(m, n)).add(g.mul(xy(m + 1, n + 1))).div(2); 
		view.setFill(Color.hsb(50, 0.6, 0.9));
		view.fillBoxCentered(ZERO, r);
		
		// Prozori
		view.setFill(Color.CORNFLOWERBLUE);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				view.fillBox((w.add(g).mul(xy(j,i))).add(g).sub(r), w);
			}
		}
		
		// Krov
		view.setFill(Color.hsb(10, 0.6, 0.9));
		view.fillPolygon(r, r.mul(xy(-1,1)), r.mul(xy(0,1)).add(xy(0,40)));
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
	
}
