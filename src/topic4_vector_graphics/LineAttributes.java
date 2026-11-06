package topic4_vector_graphics;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetEnum;
import xyz.marsavic.geometry.Vector;

import static xyz.marsavic.geometry.Vector.*;


public class LineAttributes implements Drawing {

	@GadgetDouble(p = 1, q = 100)
	double lineWidth = 40;                 // Debljina linije.

	@GadgetEnum(enumClass = StrokeLineCap.class)
	StrokeLineCap lineCap = StrokeLineCap.BUTT;        // Izgled krajeva linije.

	@GadgetEnum(enumClass = StrokeLineJoin.class)
	StrokeLineJoin lineJoin = StrokeLineJoin.MITER;    // Izgled spoja dve duži.

	@GadgetDouble(p = 1, q = 10)
	double miterLimit = 10;                // Najveći odnos dužine šiljka i debljine linije.

	@GadgetDouble(p = 0, q = 100)
	double dash = 0;                       // Dužina crtice.

	@GadgetDouble(p = 0, q = 100)
	double gap = 0;                        // Dužina razmaka. Ako su dash i gap 0, linija je puna.

	@GadgetDouble(p = 0, q = 200)
	double dashOffset = 0;                 // Pomeraj šablona crtica duž linije.

	@GadgetDouble(p = 0, q = 0.5)
	double angle = 0.125;                  // Ugao između dve duži.



	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));

		Vector q = xy(0, 200);             // Spoj dve duži.
		Vector p = q.add(polar(450, 0.75 - angle/2));
		Vector r = q.add(polar(450, 0.75 + angle/2));

		// Linija sa zadatim atributima
		view.setStroke(Color.hsb(200, 0.7, 0.9));
		view.setLineWidth(lineWidth);
		view.setLineCap(lineCap);
		view.setLineJoin(lineJoin);
		view.setMiterLimit(miterLimit);
		view.setLineDashes(dash, gap);
		view.setLineDashOffset(dashOffset);
		
		view.strokePolyline(p, q, r);

		// Ista putanja, tanka puna linija
		view.setStroke(Color.WHITE);
		view.setLineWidth(1);
		view.setLineDashes();
		
		view.strokePolyline(p, q, r);
	}


	public static void main(String[] args) {
		DrawingApplication.launch(720, 720);
	}
}
