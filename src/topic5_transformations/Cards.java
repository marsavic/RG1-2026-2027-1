package topic5_transformations;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;

import java.util.SplittableRandom;

import static xyz.marsavic.geometry.Vector.*;


public class Cards implements Drawing {

	@GadgetInteger(min = 1, max = 16)
	int nCards = 6;

	Vector size = xy(150, 200);		// Dimenzije karte
	Vector a = xy(20, 20);			// Poluprečnik zakrivljenja ćoška karte
		
	double r = 48;					// Poluprečnik simbola na karti
	
	Vector d = xy(16, 16);          // Pomeraj između dve uzastopne karte

	
	void drawCard(View view) {
		// Okvir karte sa senkom
		
		         // Efekt se odnosi na svaki naredni iscrtani objekat sve dok ne postavimo efekt na null.
		

		// Simbol na karti
		
		
	}
	
	
	@Override
	public void draw(View view) {
		view.addTransformation(Transformation.translation(xy(-120, -120)));
		DrawingUtils.clear(view, Color.hsb(120, 0.5, 0.2));
		
		SplittableRandom rng = new SplittableRandom(6380977105788498275l);

		for (int i = nCards - 1; i >= 0; i--) {
			
			drawCard(view);
			
		}
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 600);
	}
	
}
