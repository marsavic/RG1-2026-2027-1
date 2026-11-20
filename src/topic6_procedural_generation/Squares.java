package topic6_procedural_generation;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.application.Options;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.input.InputEvent;
import xyz.marsavic.input.InputState;
import xyz.marsavic.input.KeyCode;
import xyz.marsavic.random.fixed.continuous.PerlinNoise;

import java.util.SplittableRandom;

import static xyz.marsavic.geometry.Vector.*;


public class Squares implements Drawing {

	int nx = 40;
	int ny = 30;
	Vector n = xy(nx, ny);
	
	Vector p = ZERO;
	PerlinNoise pn;
	
	{
		setup();
	}
	
	
	void setup() {
		pn = new PerlinNoise(new SplittableRandom().nextLong());
	}
	
	
	@Override
	public void draw(View view) {
		view.addTransformation(Transformation.scaling(20));
		DrawingUtils.clear(view, Color.BLACK);
		
		for (int ix = 0; ix < nx; ix++) {
			for (int iy = 0; iy < ny; iy++) {
				Vector i = xy(ix, iy);
				Vector c = i.mul(2).sub(n).add(xy(1, 1));
				
				double vp = pn.getValue(c.x() / 20, c.y() / 20, 0);     // u [-1, 1]
				double vm = 2 / (1 + p.distanceTo(c) / 6) - 1;      // u [-1, 1]
				
				double k = (vp + vm) / 2;                           // u [-1, 1]
				double k1 = (k+1)/2;                                // k skalirano na [0, 1]
				
				Transformation t = new Transformation().rotate(0.25*k).translate(c);
				
				view.stateStore();
				
				view.addTransformation(t);
				
				view.setFill(Color.gray(k1));
				view.fillBoxCentered(ZERO, xy(0.9));
				
				view.setFill(Color.gray(k1 * 0.75));
				view.fillBoxCentered(ZERO, xy(0.7));
				
				view.stateRestore();
			}
		}
	}
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		if (event.isKeyPress(KeyCode.ENTER)) {
			setup();
		}
		p = pointerWorld;
	}
	
	
	public static void main(String[] args) {
		Options options = new Options();
		options.drawingSize = xy(800, 800);
		options.hideMouseCursor = true;
		DrawingApplication.launch(options);
	}
	
}
