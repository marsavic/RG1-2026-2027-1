package topic6_procedural_generation.demos;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetAnimation;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDoubleLogarithmic;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetLongRandomStream;
import xyz.marsavic.drawingfx.utils.camera.CameraSimple;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.input.InputEvent;
import xyz.marsavic.input.InputState;
import xyz.marsavic.random.fixed.continuous.FractalNoise1D;
import xyz.marsavic.random.fixed.continuous.SimplexNoise;
import xyz.marsavic.random.sampling.Sampler;
import xyz.marsavic.utils.Hash;
import xyz.marsavic.utils.Numeric;

import static xyz.marsavic.geometry.Vector.*;
import static xyz.marsavic.utils.Numeric.*;

// Ovaj program je samo za demonstraciju, ne morate se truditi da razumete detalje implementacije.

public class WindyTree implements Drawing {
	@GadgetAnimation(start = true)
	double time = 0.0;

	@GadgetLongRandomStream
	long seed = 0L;

	
	
	@GadgetDoubleLogarithmic(p = 0.01, q = 1)
	double limit = 0.02;
	
//	@GadgetInteger(min = 0, max = 1000)
	int levelLimit = 100;
	
	

//	@GadgetDouble(p = 0, q = 30)
	double trunkSize = 14;

	@GadgetDouble(p = 0, q = 20)
	double thickness = 1.4;
	
	@GadgetDouble(p = 0, q = 20)
	double lengthFactor = 10;

	@GadgetDouble(p = 0, q = 2)
	double twigginess = 0.86;

	
	
	@GadgetDouble(p = 0, q = 1)
	double branching = 0.64;

	@GadgetDouble(p = 0, q = 1)
	double uniformity = 0.72;

	@GadgetDouble(p = 0, q = 1)
	double spread = 1.0;
	
	
	
	@GadgetDouble(p = -0.9, q = 1)
	double gravity = 0.26;

	@GadgetDouble(p = 0, q = 10)
	double windFactor = 1;

	
	@GadgetDouble(p = 0, q = 40)
	double crownness = 20.0;


	
	
//	@GadgetColorPicker
	Color colorTrunk = Color.hsb(310, 0.4, 0.3);

//	@GadgetColorPicker
	Color colorLeaves = Color.hsb(310, 0.2, 1);

//	@GadgetColorPicker
	Color colorSky = Color.hsb(310, 0.4, 0.3);
	
//	@GadgetColorPicker
	Color colorGround = Color.hsb(310, 0.2, 1);


	
	Hash hash;
	CameraSimple camera = new CameraSimple();
	
	
	

	
	FractalNoise1D windGlobal = new FractalNoise1D(8, Numeric.PHI, Numeric.PHI, 2983749283436014926L);
	double windGlobalAtTheMoment;
	
	Vector getWind(Vector p) {
		Vector q = p.mul(0.004);
		Vector windLocal = xy(
				SimplexNoise.noise(time/2           , q.x(), q.y()),
				SimplexNoise.noise(time/2 + 317387.5, q.x(), q.y())
		).mul(0.1);
		return xy(windGlobalAtTheMoment, 0).add(windLocal);
	}
	
	
	void drawTree(View view, int level, int id, Vector root, Vector dir) {
		Sampler sampler = new Sampler(hash.add(id));

		double r = dir.length();
		dir = dir.add(xy(0, dir.length() * gravity)).add(getWind(root).mul(windFactor)).normalizedTo(r);
		Vector dirN = dir.div(r);

		double l = lengthFactor * Math.pow(r, twigginess) * Math.pow(sampler.exponential(1), 1 - uniformity);
		
		Vector end = root.add(dirN.mul(l));

		double ck = Math.pow(1 - r / trunkSize, crownness);
		Color color = colorTrunk.interpolate(colorLeaves, ck).deriveColor(
				0,
				1,
				1 - 0.2 * (1-ck) * (1 - Math.abs(dirN.dot(UNIT_X))),
				1
			);
		
		view.setStroke(color);
		view.setLineWidth(2*r + thickness);
		view.setLineCap(StrokeLineCap.ROUND);
		view.strokeLineSegment(root, end);

		if (r / trunkSize > limit && level < levelLimit) {
			Vector endL = end.add(dir.perp());
			Vector endR = end.add(dir.perp().inverse());

			double d = sampler.uniform(-0.5, 0.5);
			double fBranching = Math.pow(tanT(branching / 4), 2);  // [0,1] -> [0,inf]
			double k = Math.signum(d) * 0.5 * Math.pow(Math.abs(d) / 0.5, fBranching);
			
			double phi = k / 2;
			Vector v = dir.rotate(phi);
			Vector u = dirN.mul(v.dot(dirN)*spread).add(dirN.perp().mul(v.dot(dirN.perp())));			
			Vector m = end.add(u);
			
			Vector startL = m.add(endL).div(2);
			Vector startR = m.add(endR).div(2);
			
			Vector dirL = m.sub(startL).perp();
			Vector dirR = startR.sub(m).perp();
			
			drawTree(view, level + 1, 615123587 * id + 1715994805, startL, dirL);
			drawTree(view, level + 1, 793948173 * id +  814064562, startR, dirR);
		}	
	}
	

	
	@Override
	public void draw(View view) {
		view.stateStore();
		view.addTransformation(camera.getTransformation());
		
		view.setFill(new LinearGradient(
				0, -300,
				0,    0,
				false,
				CycleMethod.NO_CYCLE,
				new Stop(1, colorSky),
				new Stop(0.0, colorGround)
			));
		view.fillBox(view.boundingBox());
		
		hash = new Hash(seed);
		windGlobalAtTheMoment = 0.6 * windGlobal.getGaussian(time / 26);

		drawTree(view, 0, 0, xy(0, -250), xy(0, trunkSize));
		view.applyEffect(new GaussianBlur(8));
		
		drawTree(view, 0, 0, xy(0, -250), xy(0, trunkSize));
		view.applyEffect(new Bloom(0.62));
		view.stateRestore();
	}
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}

}

