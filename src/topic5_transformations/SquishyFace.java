package topic5_transformations;

import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetVector;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;

import static xyz.marsavic.geometry.Vector.*;


public class SquishyFace implements Drawing {

	double r = 50.0;                       // Poluprečnik glave.
	double xEye = 17.5;                    // Udaljenost oka od centra po x-osi.
	double yEye = 17.5;                    // Udaljenost oka od centra po y-osi.
	double rEye = 7.5;                     // Poluprečnik oka.
	double rMouth = 32.5;                  // Poluprečnik luka usta.
	double phiMouth = 1.0/3;               // Ugao luka usta.

	double rWall = 350;                    // Poluprečnik zida.
	
	
	@GadgetVector
	Vector p = ZERO;
	
	
	private void drawFace(View view) {
		// Glava
		view.setFill(Color.hsb(60, 0.9, 0.9));
		view.fillCircleCentered(ZERO, r);
		
		// Oči
		view.setFill(Color.hsb(0, 0, 0));
		view.fillCircleCentered(xy(-xEye, yEye), rEye);
		view.fillCircleCentered(xy(xEye, yEye), rEye);
		
		// Usta
		view.setLineWidth(7.5);
		view.setStroke(Color.hsb(0, 0, 0));
		view.strokeArcCentered(ZERO, xy(rMouth), 0.75 - phiMouth/2, phiMouth);
	}
	
	
	@Override
	public void draw(View view) {
		view.stateStore();
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		// Crtamo zid
		view.setFill(Color.gray(0.375));
		view.fillCircleInvertedCentered(ZERO, rWall);              // Crta invertovani krug, tj. boji spoljašnost kruga.
		view.setFill(Color.gray(0.25));
		view.fillCircleInvertedCentered(ZERO, rWall + 8);          // Još jedan invertovani krug, malo većeg poluprečnika.
		
		
		double d = p.length();                 // Udaljenost kursora od koordinatnog početka
		double phi = p.angle();                // Ugao između kursora i x-ose (u okretima)

		Vector c;                              // Konačna pozicija centra smajlija
		double k;		                       // Faktor kompresije smajlija. Broj iz (0, 1], gde 1 znači da nije kompresovan, a 0 da je skroz spljošten.
		
		if (d <= rWall - r) {                  // Ukoliko je smajli u potpunosti unutar granica:
			k = 1;                             // - ne kompresujemo ga
			c = p;                             // - centar mu je u kursoru
		} else {                               // Ukoliko izlazi iz granica:
			k = (rWall - r) / d;               // - izračunavamo kompresiju nekom opadajućom funkcijom po d koja za d = rWall-r daje 1, a teži 0 kad d teži beskonačnosti. 
			c = p.normalizedTo(rWall - r * k); // - računamo centar tako da kompresovan smajli dodiruje zid
		}
		
		Transformation t = new Transformation()
				.rotate(-phi)                  // Pre-rotacija, da bi nakon post-rotacije ostao isto orijentisan.
				.scale(k, 1/k)                 // Skaliramo x koordinate k puta, y koordinate 1/k puta. Površina je očuvana. (Walt Disney's principles of animation)
				.rotate(phi)                   // Post-rotacija. Rotiramo ga tako da pravac po kome se kompresuje bude upravo pravac od koordinatnog početka do centra smajlija.
				.translate(c)                  // Pomeramo ga u izračunati centar.
				;
		
		view.addTransformation(t);
		drawFace(view);
		
		view.stateRestore();
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}
}
