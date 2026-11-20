package topic6_procedural_generation;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.application.Options;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDoubleLogarithmic;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetEnum;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.functions.FD_DD;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.input.InputEvent;
import xyz.marsavic.input.InputState;
import xyz.marsavic.input.KeyCode;
import xyz.marsavic.random.fixed.continuous.PerlinNoise;

import java.util.Map;
import java.util.SplittableRandom;

import static xyz.marsavic.geometry.Vector.*;


public class Terrain implements Drawing {
	
	enum Shape {
		NO_SHAPE, ARCHIPELAGO, ATOLL
	}

	static final int size = 720;
	
	@GadgetDoubleLogarithmic(p = 0.01, q = 100)
	double zoom = 1.5;
	
	@GadgetDouble(p = 0, q = 10)
	double z = 0.5;
	
	@GadgetInteger(min = 0, max = 10)
	int nLevels = 8;
	
	@GadgetEnum(enumClass = Shape.class)
	Shape shape = Shape.NO_SHAPE;
	

	double factor = 1.62;
	
	Color c0 = Color.hsb(210, 0.7, 0.8); // duboka voda
	double h0 = -0.5;
	Color c1 = Color.hsb(210, 0.7, 0.9); // voda
	double h1 =  0.0;
	Color c2 = Color.hsb( 50, 0.5, 1.0); // pesak
	double h2 =  0.1;
	Color c3 = Color.hsb(110, 0.6, 0.8); // trava
	double h3 =  0.4;
	Color c4 = Color.hsb( 30, 0.6, 0.5); // planina
	double h4 =  0.8;
	Color c5 = Color.hsb(  0, 0.0, 1.0); // sneg
	
	
	PerlinNoise[] pn;
	
	{
		setup();
	}
	
	
	void setup() {
		pn = new PerlinNoise[10];
		SplittableRandom rng = new SplittableRandom();
		for (int i = 0; i < pn.length; i++) {
			pn[i] = new PerlinNoise(rng.nextLong());
		}
	}
	
	
	Color colorForHeight(double h) {
		if (h < h0) return c0;
		if (h < h1) return c1;
		if (h < h2) return c2;
		if (h < h3) return c3;
		if (h < h4) return c4;
		return c5;
	}
	
	
	Map<Shape, FD_DD> shapeHeightFunctions = Map.of(
			Shape.NO_SHAPE   , (dx, dy) -> 0,
			Shape.ARCHIPELAGO, (dx, dy) -> 0.4 - 1.5 * Math.hypot(dx, dy),
			Shape.ATOLL      , (dx, dy) -> 0.3 - 3.0 * Math.abs(Math.hypot(dx, dy) - 0.6)
	);


	
	public Image generateMap() {
		WritableImage image = new WritableImage(size, size);
		PixelWriter pw = image.getPixelWriter();
		
		var shapeHeightFunction = shapeHeightFunctions.get(shape);
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				double dx = 2 * x / image.getWidth () - 1; // Normalizujemo na interval [-1, 1)
				double dy = 2 * y / image.getHeight() - 1;
				
				double h = shapeHeightFunction.at(dx, dy);

				dx *= zoom;
				dy *= zoom;
				
				// Postižemo više nivoa detaljnosti tako što Perlin Noise koristimo više puta
				// skaliran različitim faktorima.
				
				double amplitude = 1.0;
				double frequency = 1.0;
				
				for (int l = 0; l < nLevels; l++) {
					h += amplitude * pn[l].getValue(frequency * dx, frequency * dy, z);
					amplitude /= factor;
					frequency *= factor;
				}
				
				pw.setColor(x, y, colorForHeight(h));
			}
		}
		
		return image;
	}
	
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		view.drawImageCenteredNativeOrientation(ZERO, generateMap());
	}
	
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		if (event.isKeyPress(KeyCode.ENTER)) {
			setup();
		}
	}
	
	
	
	public static void main(String[] args) {
		Options options = Options.redrawOnEvents();
		options.drawingSize = xy(size, size);
		DrawingApplication.launch(options);
	}
	
}
