package topic5_transformations;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetBoolean;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.graphics.javafx.Graphics;

import static xyz.marsavic.geometry.Vector.*;


public class Transformations implements Drawing {
	
	@GadgetBoolean
	boolean applyTransformation = true;
	
	@GadgetInteger(min = 0, max = 13)
	int index = 0;
	
	
	Transformation[] transformations = {
			Transformation.identity(),
			Transformation.translation(xy(120, 70)),
			Transformation.rotation(0.1),
			Transformation.scaling(1.5),
			Transformation.scaling(0.8, 1.2),
			Transformation.shearing(0, 0.5),
			Transformation.shearing(0.2, 0.5),
			Transformation.scaling(-1, 1),
			new Transformation().rotate(0.1).translate(xy(120, 70)),
			new Transformation().translate(xy(120, 70)).rotate(0.1),
			new Transformation().scale(2.0/3.0, 3.0/2.0).rotate(0.25),
			new Transformation().rotate(0.25).scale(2.0/3.0, 3.0/2.0),
			new Transformation().translate(xy(60, 90)).scale(1.5),
			new Transformation().scale(1.5).translate(xy(60, 90)),
	};
	
	String[] descriptions = {
			"Transformation.identity()",
			"Transformation.translation(xy(120, 70))",
			"Transformation.rotation(0.1)",
			"Transformation.scaling(1.5)",
			"Transformation.scaling(0.8, 1.2)",
			"Transformation.shearing(0, 0.5)",
			"Transformation.shearing(0.2, 0.5)",
			"Transformation.scaling(-1, 1)",
			"new Transformation().rotate(0.1).translate(xy(120, 70))",
			"new Transformation().translate(xy(120, 70)).rotate(0.1)",
			"new Transformation().scale(2.0/3.0, 3.0/2.0).rotate(0.25)",
			"new Transformation().rotate(0.25).scale(2.0/3.0, 3.0/2.0)",
			"new Transformation().translate(xy(60, 90)).scale(1.5)",
			"new Transformation().scale(1.5).translate(xy(60, 90))",
	};
	
	
	Color color = Color.MEDIUMORCHID;
	
	
	void drawObject(View view, Transformation t) {
		view.stateStore();
		
		view.addTransformation(t);

		DrawingUtils.drawGrid(view, 100, 10, Color.gray(1, 0.125));

		Vector[] polygon = {
				xy(  0,   0),
				xy(  0, 100),
				xy(100, 100),
				xy(100,  80),
				xy( 20,  80),
				xy( 20,  60),
				xy( 60,  60),
				xy( 60,  40),
				xy( 20,  40),
				xy( 20,   0),
		};

		
		view.setFill(Graphics.scaleOpacity(color, 0.25));
		view.fillPolygon(polygon);
		
		view.setStroke(color);
		view.strokePolygon(polygon);
		
		view.stateRestore();
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		Transformation t = applyTransformation ? transformations[index] : Transformation.identity();
		drawObject(view, t);
		
		DrawingUtils.drawInfoText(view, transformations[index].toStringAsMatrixExtended().split("\n"));
		DrawingUtils.drawInfoText(view, descriptions[index]);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(720, 720);
	}
	
}
