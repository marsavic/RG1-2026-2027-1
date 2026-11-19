package topic5_transformations;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetAnimation;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetColorPicker;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetDouble;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.utils.Numeric;

import static xyz.marsavic.geometry.Vector.*;


public class StickManWithFlower implements Drawing {
	
	// ===================== Gadgeti za animaciju =====================
	
	@GadgetAnimation(start = true)
	double time = 0.0;
	
	@GadgetDouble(p = 0, q = 4)
	double walkSpeed = 1.5;                 // Brzina hodanja (koliko brzo se pomeraju noge)
	
	@GadgetDouble(p = 0, q = 200)
	double walkRange = 80;                  // Koliko levo-desno čovečuljak šeta
	
	@GadgetDouble(p = 0, q = 0.15)
	double waveAmplitude = 0.05;            // Koliko se ruka sa cvetom njiše (u obrtajima)
	
	@GadgetDouble(p = 0, q = 1)
	double flowerSpin = 0.2;                // Brzina rotacije latica
	
	@GadgetDouble(p = 0, q = 100)
	double cloudSpeed = 20;                 // Brzina oblaka
	
	
	// ===================== Gadgeti za oblik =====================
	
	@GadgetDouble(p = 0.3, q = 2)
	double scale = 1.0;                     // Veličina čovečuljka
	
	@GadgetInteger(min = 3, max = 12)
	int nPetals = 6;                        // Broj latica
	
	
	// ===================== Gadgeti za boje =====================
	
	@GadgetColorPicker
	Color skyColor    = Color.hsb(200, 0.4, 0.95);
	
	@GadgetColorPicker
	Color grassColor  = Color.hsb(110, 0.6, 0.7);
	
	@GadgetColorPicker
	Color sunColor    = Color.hsb(50, 0.8, 1.0);
	
	@GadgetColorPicker
	Color manColor    = Color.hsb(0, 0, 0.15);   // Boja linija čovečuljka
	
	@GadgetColorPicker
	Color petalColor  = Color.hsb(330, 0.5, 1.0);
	
	@GadgetColorPicker
	Color centerColor = Color.hsb(45, 0.9, 1.0);
	
	@GadgetColorPicker
	Color stemColor   = Color.hsb(120, 0.7, 0.5);
	
	
	// ===================== Dimenzije čovečuljka (pre skaliranja) =====================
	
	double headR   = 14;     // Poluprečnik glave
	double bodyLen = 40;     // Dužina trupa
	double legLen  = 35;     // Dužina noge
	double armLen  = 30;     // Dužina ruke
	double stemLen = 45;     // Dužina stabljike
	double petalR  = 7;      // Poluprečnik jedne latice
	double petalD  = 9;      // Udaljenost latice od centra cveta
	
	
	// ===================== Pomoćne metode za crtanje =====================
	
	// Oblak sastavljen od tri kruga, centar u koordinatnom početku (pomera se transformacijom).
	void drawCloud(View view) {
		view.setFill(Color.gray(1.0, 0.9));
		view.fillCircleCentered(xy(-25, 0), 22);
		view.fillCircleCentered(xy(  0, 8), 28);
		view.fillCircleCentered(xy( 28, 0), 20);
		view.fillBoxCentered(xy(0, -8), xy(40, 12));
	}
	
	
	// Sunce sa zracima koji se okreću.
	void drawSun(View view) {
		view.setStroke(sunColor);
		view.setLineWidth(5);
		view.setLineCap(StrokeLineCap.ROUND);
		for (int i = 0; i < 12; i++) {
			double phi = i / 12.0 + time * 0.03;
			view.strokeLineSegment(polar(48, phi), polar(66, phi));
		}
		view.setFill(sunColor);
		view.fillCircleCentered(ZERO, 38);
	}
	
	
	// Cvet. Koordinatni početak je na dnu stabljike (tamo gde ga čovečuljak drži),
	// a stabljika raste naviše.
	void drawFlower(View view) {
		Vector head = xy(0, stemLen);
		
		// Stabljika
		view.setStroke(stemColor);
		view.setLineWidth(3);
		view.strokeLineSegment(ZERO, head);
		
		// List
		view.setFill(stemColor);
		view.fillCircleCentered(xy(6, stemLen * 0.35), 5);
		
		// Latice se okreću oko centra cveta
		view.setFill(petalColor);
		for (int k = 0; k < nPetals; k++) {
			double phi = (double) k / nPetals + time * flowerSpin;
			view.fillCircleCentered(head.add(polar(petalD, phi)), petalR);
		}
		
		// Sredina cveta
		view.setFill(centerColor);
		view.fillCircleCentered(head, petalR * 0.9);
	}
	
	
	// Čovečuljak. Koordinatni početak je u kuku (dno trupa).
	void drawStickMan(View view) {
		double swing = 0.08 * Math.sin(2 * Math.PI * time * walkSpeed * 0.5);   // Zamah nogu i ruku
		
		view.setStroke(manColor);
		view.setLineWidth(4);
		view.setLineCap(StrokeLineCap.ROUND);
		
		Vector hip      = ZERO;
		Vector shoulder = xy(0, bodyLen);
		Vector neck     = shoulder;
		Vector headC    = neck.add(xy(0, headR));
		
		// Trup
		view.strokeLineSegment(hip, shoulder);
		
		// Noge (naizmenično se njišu, 0.75 obrtaja je pravo naniže)
		view.strokeLineSegment(hip, hip.add(polar(legLen, 0.75 + 0.04 + swing)));
		view.strokeLineSegment(hip, hip.add(polar(legLen, 0.75 - 0.04 - swing)));
		
		// Leva ruka (opuštena, njiše se suprotno)
		view.strokeLineSegment(shoulder, shoulder.add(polar(armLen, 0.75 - 0.10 + swing)));
		
		// Desna ruka podignuta, drži cvet
		double armAngle = 0.12 + waveAmplitude * Math.sin(2 * Math.PI * time * 0.7);
		Vector hand = shoulder.add(polar(armLen, armAngle));
		view.strokeLineSegment(shoulder, hand);
		
		// Cvet se crta u lokalnom koordinatnom sistemu šake: rotacija pa translacija.
		// Cvet je blago nagnut zajedno sa rukom.
		view.stateStore();
		view.addTransformation(new Transformation()
				.rotate(armAngle - 0.25 + 0.1)
				.translate(hand));
		drawFlower(view);
		view.stateRestore();
		
		// Glava
		view.setFill(Color.hsb(30, 0.25, 1.0));
		view.fillCircleCentered(headC, headR);
		view.strokeCircleCentered(headC, headR);
		
		// Oči
		view.setFill(manColor);
		view.fillCircleCentered(headC.add(xy(-5, 3)), 2);
		view.fillCircleCentered(headC.add(xy( 5, 3)), 2);
		
		// Osmeh
		view.setLineWidth(2);
		view.strokeArcCentered(headC.add(xy(0, 1)), xy(7), 0.75 - 0.15, 0.3);
	}
	
	
	// ===================== Glavna metoda crtanja =====================
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, skyColor);
		
		// Sunce (gore levo)
		view.stateStore();
		view.addTransformation(Transformation.translation(xy(-200, 200)));
		drawSun(view);
		view.stateRestore();
		
		// Oblaci koji lebde zdesna ulevo i vraćaju se kad izađu iz kadra (Numeric.mod)
		for (int i = 0; i < 3; i++) {
			double x = Numeric.mod(-time * cloudSpeed + i * 250, 750) - 375;
			double y = 150 + 40 * i - 60 * (i % 2);
			view.stateStore();
			view.addTransformation(new Transformation()
					.translate(xy(x, y)));
			drawCloud(view);
			view.stateRestore();
		}
		
		// Trava
		view.setFill(grassColor);
		view.fillBoxCentered(xy(0, -220), xy(300, 80));
		
		// Čovečuljak šeta levo-desno i blago poskakuje pri hodu.
		double walkPhase = 2 * Math.PI * time * walkSpeed * 0.25;
		double x   = walkRange * Math.sin(walkPhase);
		double bob = 3 * Math.abs(Math.sin(2 * Math.PI * time * walkSpeed * 0.5));
		
		// Okrenut je u smeru kretanja (cos je izvod od sin).
		double facing = Math.cos(walkPhase) >= 0 ? 1 : -1;
		
		view.stateStore();
		view.addTransformation(new Transformation()
				.translate(xy(x, -145 + bob)));
		// Ceo čovečuljak skaliramo ručno preko dimenzija (scale gadget):
		// koristimo veći ili manji "zoom" pomoću View-a
		view.addTransformation(Transformation.scaling(xy(scale * facing, scale)));
		drawStickMan(view);
		view.stateRestore();
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
	
}