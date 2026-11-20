package topic4_vector_graphics;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.geometry.Vector;

import static xyz.marsavic.geometry.Vector.*;


public class SemaphoreSign implements Drawing {
	
	private double a = 200;
	private Vector[] corners = new Vector[] {
			xy(a, 0),
			xy(0, a),
			xy(-a, 0),
			xy(0, -a),
	};
	
	private double r = 32; // Poluprečnik svetla
	private double d = 12; // Vertikalni razmak između svetla

	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.25));
		
		// Kvadrat
		view.setFill(Color.hsb(60, 0.7, 0.9));
		view.fillPolygon(corners);
		// Ivica kvadrata
		view.setLineWidth(20);
		view.setStroke(Color.BLACK);
		view.strokePolygon(corners);
		// Pravougaonik
		view.setFill(Color.BLACK);
		view.fillBoxCentered(ZERO, xy(r + d, 3 * r + 2 * d));
		// Crveno svetlo
		view.setFill(Color.hsb(0, 0.7, 0.9));
		view.fillCircleCentered(xy(0, 2 * r + d), r);
		// Žuto svetlo
		view.setFill(Color.hsb(60, 0.7, 0.9));
		view.fillCircleCentered(ZERO, r);
		// Zeleno svetlo
		view.setFill(Color.hsb(120, 0.7, 0.9));
		view.fillCircleCentered(xy(0, - 2 * r - d), r);
}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}
