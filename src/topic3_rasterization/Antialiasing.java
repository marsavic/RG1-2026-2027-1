package topic3_rasterization;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetAnimation;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetInteger;
import xyz.marsavic.drawingfx.utils.camera.CameraSimple;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.input.InputEvent;
import xyz.marsavic.input.InputState;
import xyz.marsavic.utils.Numeric;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class Antialiasing implements Drawing {
	
	@GadgetAnimation(start = true)
	double time = 0.0;
	
	@GadgetInteger(min = 1)
	int subdivision = 3; // Svaki piksel će biti podeljen na subdivision*subdivision subpixela.
	// Što je subdivision veći, to je antialias bolji, ali sporiji.
	
	@GadgetInteger(min = 0, max = 16)
	int imageIndex = 0;
	
	CameraSimple camera = new CameraSimple();
	
	
	
	/**
	 * Crta antialiasovanu sliku implicitno zadatog oblika.
	 *
	 * @param inside Funkcija: Vector -> Boolean. Vraća da li je zadata pozicija unutar oblika. Vektor (0, 0) je u sredini slike.
	 * @param sizeX Širina slike.
	 * @param sizeY Visina slike.
	 * @param subdivision Svaki piksel se deli na subdivision^2 subpiksela koji se koriste za antialiasing.
	 * @return Antialiasovana slika zadatog oblika.
	 */
	Image antialiasedShape(Predicate<Vector> inside, int sizeX, int sizeY, int subdivision) {
		WritableImage image = new WritableImage(sizeX, sizeY);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				// Pixel (x, y)
				
				int count      = 0; // Koliko subpiksela je unutar oblika.
				int countTotal = 0; // Ukupan broj testiranih subpiksela.
				
				for (int sy = 0; sy < subdivision; sy++) {
					for (int sx = 0; sx < subdivision; sx++) {
						// Subpiksel (sx, sy) piksela (x, y).
						
						// Vektor p je položaj centra subpiksela.
						Vector p = new Vector(
								x - sizeX / 2.0 + (sx + 0.5) / subdivision,
								sizeY / 2.0 - y + (sy + 0.5) / subdivision
						);
						
						if (inside.test(p)) { // Ako je posmatrani subpixel u obliku, povećavamo brojač.
							count++;
						}
						countTotal++;
					}
				}
				
				double k = 1.0 * count / countTotal; // Udeo subpixela koji su u obliku.
				pw.setColor(x, y, Color.gray(1.0, k));
			}
		}
		
		return image;
	}
	
	//Vežbe
	
	// Prava x=y
	Predicate<Vector> line45(double thickness) {
		return p -> Math.abs(p.y() - p.x()) <= thickness / 2.0;
	}
	
	
	// Poluravan
	Predicate<Vector> halfPlane(double coefficient) {
		return p -> p.y() - p.x() * coefficient >= 0;
	}
	
	
	// Pravougaonik (axis-aligned) dimenzija axb
	Predicate<Vector> rectangle(double a, double b) {
		return p -> Math.abs(p.x()) <= a/2 && Math.abs(p.y()) <= b/2;
	}
	
	
	// Kvadrat rotiran za 45 stepeni, stranice s
	Predicate<Vector> rotatedSquare45(double s) {
		double h = s / Math.sqrt(2);
		return p -> (p.y() <= p.x() + h) && (p.y() >= p.x() - h)
				&& (p.y() <= -p.x() + h) && (p.y() >= -p.x() - h);
	}
	
	
	// Jednakokraki trougao s baznom stranicom dužine base i visinom h
	Predicate<Vector> triangle(double base, double h) {
		return p -> {
			// Leva ivica
			boolean left = p.y() <= (2 * h / base) * (p.x() + base / 2);
			
			// Desna ivica
			boolean right = p.y() <= (2 * h / base) * (-p.x() + base / 2);
			
			// Donja stranica
			boolean bottom = p.y() >= 0;
			
			return bottom && left && right;
		};
	}
	
	
	// Kružnica
	Predicate<Vector> circle(double radius, double thickness) {
		return p -> Math.abs(p.length() - radius) < thickness / 2.0;
	}
	
	
	// Grafik sinusoide
	Predicate<Vector> sineGraph() {
		return p -> Math.abs(p.y() - 20 * Math.sin(p.x() / 20)) <= 1;
	}
	
	
	// Elipsa
	Predicate<Vector> ellipse(double a, double b) {
		return p -> (p.x() * p.x()) / (a * a) + (p.y() * p.y()) / (b * b) <= 1;
	}
	
	
	// Šahovska tabla - dimenzije svakog polja dxd
	Predicate<Vector> chessboard(double d) {
		return p -> {
			// Određujemo da li se x koordinata tačke nalazi u parnom ili neparnom bloku širine d
			boolean parx = Numeric.mod(p.x(), 2 * d) < d;
			
			// Analogno za y koordinatu
			boolean pary = Numeric.mod(p.y(), 2 * d) < d;
			
			// XOR (^): daje 1 ako je tačka u blokovima iste parnosti po x i y koordinati,
			// a 0 u suprotnom, što daje naizmenično obojena polja kao na šahovskoj tabli
			return parx ^ pary;
		};
	}
	
	
	// Koncentrični krugovi
	Predicate<Vector> concentricCircles(double d) {
		return p -> {
			int dist = (int) p.length();
			// Logika je slična kao kod šahovske table, s tim da se posmatra samo jedan parametar,
			// a to je udaljenost tačke od centra
			return Numeric.mod(dist, 2*d) < d;
		};
	}
	
	
	// Srce
	Predicate<Vector> heart(double coefficient) {
		return p -> {
			double x = p.x()/coefficient;
			double y = p.y()/coefficient;
			return Math.pow(x*x + y*y - 1, 3) - x*x*y*y*y <= 0;
		};
	}
	
	
	
	
	// Dodatni primeri
	
	// [*] 5-point flower
	Predicate<Vector> flower() {
		return p -> {
			double r = p.length();
			double a = Math.atan2(p.y(), p.x());
			return r <= 40 * Math.cos(5 * a);
		};
	}
	
	// [*] Perspective checkerboard
	Predicate<Vector> perspectiveCheckerboard(double d) {
		return p -> {
			double px = p.x() * (d / (d / 2 - p.y()));
			double py = d * (d / (d / 2 - p.y()) - 1);
			return (Numeric.mod(px, 100) < 50)
					^ (Numeric.mod(py, 100) < 50);
		};
	}
	
	// [*] Kinder Bueno
	Predicate<Vector> kinderBueno() {
		return p -> (Math.cos(2 * Math.PI * p.x() / 80) *
				Numeric.cosT(2 * Math.PI * p.y() / 80))  >
				Math.sin(time + (p.x() + p.y()) / 100);
	}
	

	List<Supplier<Image>> images = List.of(
			() -> antialiasedShape(line45(2)                   , 200, 200, subdivision),
			() -> antialiasedShape(halfPlane(0.173)            , 200, 200, subdivision),
			() -> antialiasedShape(rectangle(200, 100)         , 250, 250, subdivision),
			() -> antialiasedShape(rotatedSquare45(140)        , 200, 200, subdivision),
			() -> antialiasedShape(triangle(200, 100)          , 400, 400, subdivision),
			() -> antialiasedShape(circle(100,3)               , 250, 250, subdivision),
			() -> antialiasedShape(sineGraph()                 , 300, 300, subdivision),
			() -> antialiasedShape(ellipse(100.0, 50.0)        , 300, 200, subdivision),
			() -> antialiasedShape(chessboard(20)              , 278, 278, subdivision),
			() -> antialiasedShape(concentricCircles(10)       , 278, 278, subdivision),
			() -> antialiasedShape(heart(80.0)                 , 278, 278, subdivision),
			() -> antialiasedShape(flower()                    , 278, 278, subdivision),
			() -> antialiasedShape(perspectiveCheckerboard(283), 283, 283, subdivision),
			() -> antialiasedShape(kinderBueno()               , 200, 200, subdivision)
	);
		
	
	@Override
	public void draw(View view) {
		view.setImageSmoothing(false);
		view.addTransformation(camera.getTransformation());
		DrawingUtils.clear(view, Color.gray(0.125));
		view.drawImageCenteredNativeOrientation(Vector.ZERO, images.get(imageIndex).get());
	}
	
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}
	
	
}
