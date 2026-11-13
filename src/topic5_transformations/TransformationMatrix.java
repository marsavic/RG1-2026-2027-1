package topic5_transformations;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetVector;
import xyz.marsavic.geometry.Polygon;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.graphics.javafx.Graphics;

import static xyz.marsavic.geometry.Vector.*;


// Afina transformacija je određena slikama baznih vektora (kolone e i f matrice) i slikom koordinatnog početka (kolona t).
// Tasterima 1, 2, 3 biramo vektor e, f, t, a zatim ga menjamo mišem (sa Ctrl relativno u odnosu na trenutnu vrednost).

public class TransformationMatrix implements Drawing {

	@GadgetVector
	Vector e = xy(1, 0);                   // Slika vektora (1, 0).

	@GadgetVector
	Vector f = xy(0, 1);                   // Slika vektora (0, 1).

	@GadgetVector
	Vector t = xy(0, 0);                   // Slika koordinatnog početka.

	double unit = 100;                     // Broj piksela po jedinici dužine.
	int n = 20;                            // Crtamo prave x = i i y = i za i od -n do n.

	Polygon letter = Polygon.of(           // Slovo F u jediničnom kvadratu. Po njemu se vidi i orijentacija.
			xy(0.0, 0.0), xy(0.0, 1.0), xy(1.0, 1.0), xy(1.0, 0.8), xy(0.2, 0.8),
			xy(0.2, 0.6), xy(0.6, 0.6), xy(0.6, 0.4), xy(0.2, 0.4), xy(0.2, 0.0)
	);

	Color colorE = Color.hsb(  0, 0.7, 1.0);
	Color colorF = Color.hsb(120, 0.7, 0.9);
	Color colorT = Color.hsb(210, 0.7, 1.0);


	// Crta mrežu i slovo F
	void drawSpace(View view, Color color) {
		view.setStroke(color);
		for (int i = -n; i <= n; i++) {
			view.strokeLineSegment(xy(i, -n), xy(i, n));    // prava x = i
			view.strokeLineSegment(xy(-n, i), xy(n, i));    // prava y = i
		}

		view.setFill(Graphics.scaleOpacity(color, 0.5));
		view.fillPolygon(letter);
		view.strokePolygon(letter);
	}


	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));

		// Jedinica dužine je unit piksela. Ovu transformaciju ne uklanjamo na kraju, pa su u jedinicama i vrednosti
		// koje gadgeti dobijaju mišem.
		view.addTransformation(Transformation.scaling(unit));
		view.setLineWidth(1 / unit);

		Transformation m = Transformation.mappingBase(e, f, t);
		
		drawSpace(view, Color.gray(1, 0.2));    // Prostor pre transformacije
		
		view.stateStore();
		view.addTransformation(m);
		drawSpace(view, Color.gray(1, 0.6));    // Prostor posle transformacije
		view.stateRestore();

		view.setLineWidth(3 / unit);
		view.setStroke(colorT);
		DrawingUtils.drawArrow(view, ZERO, t, 0.15);
		view.setStroke(colorE);
		DrawingUtils.drawArrow(view, t, e, 0.15);
		view.setStroke(colorF);
		DrawingUtils.drawArrow(view, t, f, 0.15);

		DrawingUtils.drawInfoText(view, m.toStringAsMatrixExtended().split("\n"));
		DrawingUtils.drawInfoText(view, "kolone: e (crvena), f (zelena), t (plava)");
	}


	public static void main(String[] args) {
		DrawingApplication.launch(720, 720);
	}

}
