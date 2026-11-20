package topic6_procedural_generation.demos;

// mars

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetVector;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;

import java.util.SplittableRandom;

import static xyz.marsavic.geometry.Vector.*;


public class IteratedSierpinski implements Drawing {
	
	Vector[] vertices;
	
	@GadgetInteger
	int nVertices = 3;
	
	@GadgetVector
	Vector c = xy(0.5, 0);
	
	@GadgetInteger(min = 0, max = 1000000)
	int nIterations = 50000;
	
	
	
	public void draw(View view) {
		view.addTransformation(Transformation.scaling(300));
		
		DrawingUtils.clear(view, Color.gray(0.125));
		view.setFill(Color.gray(1.0, 0.5));
		
		vertices = new Vector[nVertices];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = polar(1, 1.0 * i / vertices.length);
		}
		
		SplittableRandom rng = new SplittableRandom(-2307298370289059238L);
		Vector p = vertices[0];
		for (int iter = 0; iter < nIterations; iter++) {
			Vector q = vertices[rng.nextInt(vertices.length)];
			p = (q.sub(p)).mulComplex(c).add(p);
			view.fillCircleCentered(p, 0.001);
		}
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(720, 720);
	}
	
}
