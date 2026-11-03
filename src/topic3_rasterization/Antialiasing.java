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

<<<<<<< HEAD
import java.util.function.Predicate;


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
		return p -> p.y()-p.x() * coefficient >= 0;
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
				Math.cos(2 * Math.PI * p.y() / 80))  >
				Math.sin(time + (p.x() + p.y()) / 100);
	}
	
	
	// ===================== Operacije nad skupovima =====================
	// Oblik je skup tačaka, tj. predikat. Nove oblike dobijamo logičkim kombinovanjem postojećih.
	
	// Unija: tačka je u uniji ako je u A ili u B
	Predicate<Vector> union(Predicate<Vector> a, Predicate<Vector> b) {
		return p -> a.test(p) || b.test(p);
	}
	
	
	// Presek: tačka je u preseku ako je i u A i u B
	Predicate<Vector> intersection(Predicate<Vector> a, Predicate<Vector> b) {
		return p -> a.test(p) && b.test(p);
	}
	
	
	// Razlika A \ B: tačka je u A, ali nije u B
	Predicate<Vector> difference(Predicate<Vector> a, Predicate<Vector> b) {
		return p -> a.test(p) && !b.test(p);
	}
	
	
	// Komplement: sve tačke koje nisu u A
	Predicate<Vector> complement(Predicate<Vector> a) {
		return p -> !a.test(p);
	}
	
	
	// Simetrična razlika (XOR): tačka je u tačno jednom od skupova A i B
	Predicate<Vector> symmetricDifference(Predicate<Vector> a, Predicate<Vector> b) {
		return p -> a.test(p) ^ b.test(p);
	}
	
	
	// Translacija oblika za vektor (dx, dy).
	// Tačka p pripada pomerenom obliku ako (p - d) pripada originalnom obliku.
	Predicate<Vector> translate(Predicate<Vector> shape, double dx, double dy) {
		return p -> shape.test(new Vector(p.x() - dx, p.y() - dy));
	}
	
	
	// Pomoćni oblici (puni, a ne samo obruč kao circle)
	
	// Krug (popunjen disk) poluprečnika radius sa centrom u koordinatnom početku
	Predicate<Vector> disk(double radius) {
		return p -> p.length() <= radius;
	}
	
	
	// Leva poluravan (x < 0)
	Predicate<Vector> leftHalfPlane() {
		return p -> p.x() < 0;
	}
	
	
	// Primeri upotrebe operacija
	
	// [*] Dva kruga, unija
	Predicate<Vector> twoCirclesUnion() {
		return union(translate(disk(60), -40, 0), translate(disk(60), 40, 0));
	}
	
	// [*] Dva kruga, presek (sočivo)
	Predicate<Vector> lens() {
		return intersection(translate(disk(60), -40, 0), translate(disk(60), 40, 0));
	}
	
	// [*] Mesec (srp): veliki krug bez pomerenog manjeg kruga
	Predicate<Vector> crescent() {
		return difference(disk(80), translate(disk(65), 30, 0));
	}
	
	// [*] Prsten: disk bez manjeg diska (analogno circle, ali dobijeno razlikom)
	Predicate<Vector> ring(double outer, double inner) {
		return difference(disk(outer), disk(inner));
	}
	
	// [*] Dva kruga, XOR
	Predicate<Vector> twoCirclesXor() {
		return symmetricDifference(translate(disk(60), -40, 0), translate(disk(60), 40, 0));
	}
	
	// [*] Sve izvan kruga (komplement)
	Predicate<Vector> outsideDisk() {
		return complement(disk(80));
	}
	
	// Jin-jang znak poluprečnika R
	Predicate<Vector> yinYang(double R) {
		Predicate<Vector> big      = disk(R);
		Predicate<Vector> upper    = translate(disk(R / 2), 0,  R / 2); // Gornji polukrug (veliki)
		Predicate<Vector> lower    = translate(disk(R / 2), 0, -R / 2); // Donji polukrug (veliki)
		Predicate<Vector> dotUpper = translate(disk(R / 8), 0,  R / 2); // Tačkica u gornjem delu
		Predicate<Vector> dotLower = translate(disk(R / 8), 0, -R / 2); // Tačkica u donjem delu
		
		// Obojeni deo: leva polovina velikog kruga bez gornjeg malog kruga, plus donji mali krug
		Predicate<Vector> body = intersection(
				big,
				union(difference(leftHalfPlane(), upper), lower)
		);
		
		// Tačkice: gornja tačkica leži u neobojenom delu (XOR je boji),
		// a donja u obojenom (XOR je "briše")
		Predicate<Vector> withDots = symmetricDifference(
				symmetricDifference(body, dotUpper),
				dotLower
		);
		
		// Spoljašnja kružnica da se vidi ceo znak
		return union(withDots, circle(R, 2));
	}
	
	// ===================== Xiaolin Wu algoritam =====================
	
	// Pomoćne funkcije
	double fpart (double x) { return x - Math.floor(x); }   // Razlomljeni deo
	double rfpart(double x) { return 1 - fpart(x); }        // 1 - razlomljeni deo
	
	
	// Dodaje intenzitet c piksel (x, y) u baferu. Ako je linija strma, koordinate su zamenjene
	// pa ih ovde vraćamo nazad. Intenzitet se sabira, ali ograničava na 1.
	void wuPlot(double[][] buffer, int x, int y, double c, boolean steep) {
		if (steep) {
			int t = x; x = y; y = t;
		}
		if (x < 0 || y < 0 || x >= buffer.length || y >= buffer[0].length) return;
		buffer[x][y] = Math.min(1.0, buffer[x][y] + c);
	}
	
	
	/**
	 * Crta antialiasovanu duž Xiaolin Wu algoritmom.
	 *
	 * @param x0, y0 Prvi kraj duži (koordinate slike, piksel (0,0) je gore levo).
	 * @param x1, y1 Drugi kraj duži.
	 * @param sizeX Širina slike.
	 * @param sizeY Visina slike.
	 * @return Slika sa antialiasovanom duži.
	 */
	Image xiaolinWuLine(double x0, double y0, double x1, double y1, int sizeX, int sizeY) {
		double[][] buffer = new double[sizeX][sizeY];
		
		// Ako je duž strma, zamenimo uloge x i y, da bi se uvek kretali po "dužoj" osi
		boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		if (steep) {
			double t;
			t = x0; x0 = y0; y0 = t;
			t = x1; x1 = y1; y1 = t;
		}
		
		// Crtamo uvek sleva udesno
		if (x0 > x1) {
			double t;
			t = x0; x0 = x1; x1 = t;
			t = y0; y0 = y1; y1 = t;
		}
		
		double dx = x1 - x0;
		double dy = y1 - y0;
		double gradient = (dx == 0) ? 1.0 : dy / dx; // Nagib duži
		
		// Prvi kraj
		double xEnd  = Math.round(x0);
		double yEnd  = y0 + gradient * (xEnd - x0);
		double xGap  = rfpart(x0 + 0.5);
		int    xPx1  = (int) xEnd;
		int    yPx1  = (int) Math.floor(yEnd);
		wuPlot(buffer, xPx1, yPx1,     rfpart(yEnd) * xGap, steep);
		wuPlot(buffer, xPx1, yPx1 + 1, fpart(yEnd)  * xGap, steep);
		
		double interY = yEnd + gradient; // y koordinata duži u prvom sledećem koraku
		
		// Drugi kraj
		xEnd = Math.round(x1);
		yEnd = y1 + gradient * (xEnd - x1);
		xGap = fpart(x1 + 0.5);
		int xPx2 = (int) xEnd;
		int yPx2 = (int) Math.floor(yEnd);
		wuPlot(buffer, xPx2, yPx2,     rfpart(yEnd) * xGap, steep);
		wuPlot(buffer, xPx2, yPx2 + 1, fpart(yEnd)  * xGap, steep);
		
		// Sredina duži: u svakoj koloni dva piksela dele intenzitet srazmerno blizini duži
		for (int x = xPx1 + 1; x < xPx2; x++) {
			int yi = (int) Math.floor(interY);
			wuPlot(buffer, x, yi,     rfpart(interY), steep);
			wuPlot(buffer, x, yi + 1, fpart(interY),  steep);
			interY += gradient;
		}
		
		// Prebacujemo bafer u sliku
		WritableImage image = new WritableImage(sizeX, sizeY);
		PixelWriter pw = image.getPixelWriter();
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				pw.setColor(x, y, Color.gray(1.0, buffer[x][y]));
			}
		}
		return image;
	}
	
	
	
	@Override
	public void draw(View view) {
		view.setImageSmoothing(false);
		view.addTransformation(camera.getTransformation());
		DrawingUtils.clear(view, Color.gray(0.125));
		
		//Za pokretanje odkomentarišete liniju koda koja poziva odgovarajuću funkciju
		
		Image image = antialiasedShape(line45(2), 200, 200, subdivision);

//      Image image = antialiasedShape(halfPlane(0.173), 200, 200, subdivision);

//      Image image = antialiasedShape(rectangle(200, 100), 250, 250, subdivision);

//      Image image = antialiasedShape(rotatedSquare45(140), 200, 200, subdivision);

//      Image image = antialiasedShape(triangle(200, 100), 400, 400, subdivision);

//		Image image = antialiasedShape(circle(100,3), 250, 250, subdivision);

//		Image image = antialiasedShape(sineGraph(), 300, 300, subdivision);

//      Image image = antialiasedShape(wavyStripe(), 300, 200, subdivision);

//      Image image = antialiasedShape(ellipse(100.0, 50.0), 300, 200, subdivision);

//      Image image = antialiasedShape(chessboard(20), 278, 278, subdivision);

//      Image image = antialiasedShape(concentricCircles(10), 278, 278, subdivision);

//      Image image = antialiasedShape(heart(80.0), 278, 278, subdivision);

//      Image image = antialiasedShape(flower(), 278, 278, subdivision);

//      Image image = antialiasedShape(perspectiveCheckerboard(283), 283, 283, subdivision);

//		Image image = antialiasedShape(kinderBueno(), 200, 200, subdivision);
		
//      Image image = antialiasedShape(twoCirclesUnion(), 300, 300, subdivision);

//      Image image = antialiasedShape(lens(), 300, 300, subdivision);

//      Image image = antialiasedShape(crescent(), 300, 300, subdivision);

//      Image image = antialiasedShape(ring(100, 80), 300, 300, subdivision);

//      Image image = antialiasedShape(twoCirclesXor(), 300, 300, subdivision);

//      Image image = antialiasedShape(outsideDisk(), 300, 300, subdivision);

//      Image image = antialiasedShape(yinYang(100), 300, 300, subdivision);

//      Image image = xiaolinWuLine(10, 20, 190, 120, 200, 200);		
		
		view.drawImageCenteredNativeOrientation(Vector.ZERO, image);
=======
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
>>>>>>> branch 'master' of https://github.com/marsavic/RG1-2026-2027-1.git
	}
	
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}
	
	
}
