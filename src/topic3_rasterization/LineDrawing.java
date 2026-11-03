package topic3_rasterization;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetBoolean;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetVector;
import xyz.marsavic.drawingfx.utils.camera.CameraSimple;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.input.InputEvent;
import xyz.marsavic.input.InputState;


public class LineDrawing implements Drawing {
	
	CameraSimple camera = new CameraSimple();
	
	@GadgetVector
	Vector p0 = new Vector(10, 29);
	
	@GadgetVector
	Vector p1 = new Vector(120, 76);

	@GadgetBoolean
	boolean showLine = false;
	
	
	
	void drawLineBresenham(PixelWriter pw, int ax, int ay, int bx, int by) {
		// Brezenhamov algoritam smatra da centri piksela imaju celobrojne koordinate. U drugim kodovima ćemo češće
		// smatrati da su centri piksela na koordinatama oblika (x+0.5, y+0.5) gde su x i y celi brojevi.

		// Napomena: Ovo nije originalni Brezenhamov algoritam. Ovde koristimo isti princip, ali je implementacija
		// još jednostavnija.
		
		int dx = Math.abs(bx - ax);
		int dy = Math.abs(by - ay);
		
		int sx = ax < bx ? 1 : -1;  // Koliko treba da dodajemo u svakom koraku na x, tj. y, da bi išli u pravom smeru.
		int sy = ay < by ? 1 : -1;
		
		int cx = ax;                 // (cx, cy) je sledeći piksel koji treba iscrtati.
		int cy = ay;

		int err = dx - dy;
		
		while (cx != bx || cy != by) {
			pw.setColor(cx, cy, Color.WHITE);
			int err2 = 2 * err;

			if (err2 > -dy) {
				cx += sx;
				err -= dy;
			}			
			if (err2 < dx) {
				cy += sy;
				err += dx;
			}
		}
		pw.setColor(cx, cy, Color.WHITE);
	}
	
	
	Vector offset = new Vector(0.5, 0.5);
	
	@Override
	public void draw(View view) {
		view.setImageSmoothing(false);
		view.addTransformation(camera.getTransformation());
		DrawingUtils.clear(view, Color.gray(0.125));
		DrawingUtils.drawGrid(view, Color.gray(0.25));
		
		int size = 800;
		WritableImage image = new WritableImage(size, size);
		PixelWriter pw = image.getPixelWriter();

		p0 = p0.floor();
		p1 = p1.floor();
		
		int x0 = (int) (p0.x() + size/2);
		int y0 = (int) (size/2 - 1 - p0.y());
		int x1 = (int) (p1.x() + size / 2);
		int y1 = (int) (size/2 - 1 - p1.y());
		
		drawLineBresenham(pw, x0, y0, x1, y1);
		view.drawImageCenteredNativeOrientation(Vector.ZERO, image);

		if (showLine) {
			view.setStroke(Color.RED);
			view.setLineWidth(1.0 / view.transformation().getScale());
			view.strokeLineSegment(p0.add(offset), p1.add(offset));
		}

	}
	
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}
	
}
