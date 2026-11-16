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
		
		// Ivica kvadrata
				
		// Pravougaonik
		
		// Crveno svetlo

		// Žuto svetlo
		
		// Zeleno svetlo
	}
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}
