package topic6_procedural_generation;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;

import static xyz.marsavic.geometry.Vector.*;


public class IFSTree implements Drawing {
	
	@GadgetInteger(min = 0)
	int nLevels = 12;
	
	@GadgetDouble(p = -1, q = 1)
	double k1 = 0.66;
	
	@GadgetDouble(p = -0.5, q = 0.5)
	double phi1 = 0.09;
	
	@GadgetDouble(p = -1, q = 1)
	double k2 = 0.72;

	@GadgetDouble(p = -0.5, q = 0.5)
	double phi2 = -0.19;

	Vector l = xy(0, 200);
	Transformation[] transformations;
	
	
	
	private void drawTrunk(View view, int level) {
		view.setLineWidth(30);
		view.setStroke(Color.hsb(110, 1 - Math.pow(0.88, level), 1));
		view.strokeLineSegment(ZERO, l);
	}
	
	
	void drawTree(View view, int level) {
		if (level == nLevels) return;

		for (Transformation t : transformations) {
			view.stateStore();
			view.addTransformation(t);
			drawTree(view, level + 1);
			view.stateRestore();
		}
		
		drawTrunk(view, level);
	}
	
	
	@Override
	public void draw(View view) {
		view.addTransformation(Transformation.translation(xy(0, -300)));
		DrawingUtils.clear(view, Color.gray(0.125));
		
		transformations = new Transformation[] {
				new Transformation().scale(k1).rotate(phi1).translate(l),
				new Transformation().scale(k2).rotate(phi2).translate(l),
		};
		
		drawTree(view, 0);
	}
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 700);
	}
	
	
}