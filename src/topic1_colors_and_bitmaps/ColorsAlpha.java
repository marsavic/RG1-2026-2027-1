package topic1_colors_and_bitmaps;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetBoolean;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetColorPicker;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.geometry.Box;
import xyz.marsavic.geometry.Vector;


/**
 * Alfa kanal je četvrta komponenta boje, koja određuje njenu neprovidnost
 * (0 - potpuno providna, 1 - potpuno neprovidna).
 *
 * Kada bojom F, čija je neprovidnost alpha, crtamo preko već nacrtane boje B, dobijamo boju
 *     C = alpha * F + (1 - alpha) * B,
 * računato za svaku komponentu (R, G, B) posebno.
 */
public class ColorsAlpha implements Drawing {

	@GadgetDouble(p = 0, q = 1)
	double alpha = 0.5;

	@GadgetBoolean
	boolean checkerboard = true;  // Šahovnica je uobičajen način da se prikaže šta je providno.
	
	@GadgetColorPicker
	Color colorBackground = Color.gray(0.125);


	void drawCheckerboard(View view, double d) {
		DrawingUtils.clear(view, Color.WHITE);
		view.setFill(Color.gray(0.8));

		Box b = view.boundingBox();
		int i0 = (int) Math.floor(b.p().x() / d);
		int i1 = (int) Math.ceil (b.q().x() / d);
		int j0 = (int) Math.floor(b.p().y() / d);
		int j1 = (int) Math.ceil (b.q().y() / d);

		for (int j = j0; j < j1; j++) {
			for (int i = i0; i < i1; i++) {
				if (Math.floorMod(i + j, 2) == 0) {
					view.fillBox(new Vector(i * d, j * d), new Vector(d, d));
				}
			}
		}
	}


	@Override
	public void draw(View view) {
		if (checkerboard) {
			drawCheckerboard(view, 20);
		} else {
			DrawingUtils.clear(view, colorBackground);
		}

		// Ista crvena boja sa različitim neprovidnostima.

		view.setFont(Font.font(18));
		view.setTextAlign(TextAlignment.CENTER);

		for (int i = 0; i <= 4; i++) {
			double a = i / 4.0;
			Vector c = new Vector(-200 + 100 * i, 220);

			view.setFill(new Color(1, 0, 0, a));           // Četvrti parametar je alfa.
			view.fillBoxCentered(c, new Vector(30, 30));

			view.setFill(checkerboard ? Color.BLACK : Color.WHITE);
			view.fillTextNativeOrientation("α = " + a, c.add(new Vector(0, -60)));
		}

		// Tri kruga sa neprovidnošću alpha. Krug koji je nacrtan kasnije je "iznad" ranije nacrtanih.

		Color[] colors = {Color.RED, Color.LIME, Color.BLUE};

		for (int i = 0; i < 3; i++) {
			Color c = colors[i];
			view.setFill(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
			view.fillCircleCentered(Vector.polar(73, 0.25 + i / 3.0).add(new Vector(0, -90)), 120);
		}
	}


	public static void main(String[] args) {
		DrawingApplication.launch(640, 640);
	}

}
