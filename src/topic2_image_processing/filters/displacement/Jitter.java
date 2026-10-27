package topic2_image_processing.filters.displacement;

import java.util.Random;

import xyz.marsavic.geometry.Vector;
import topic2_image_processing.filters.DisplacementFilter;


/**
 * Svaki piksel rezultujuće slike se dobija kopiranjem nasumično odabranog piksela iz njegove okoline.
 * Maksimalna udaljenost sa koje može da se kopira je zadata kao parametar konstruktora.
 */
public class Jitter extends DisplacementFilter {
	final double r;
	Random random = new Random();
	
	
	public Jitter(double r) {
		this.r = r;
	}
	
	
	@Override
	public Vector source(Vector dst, Vector dim) {
		// Source pozicija nam je slučajno generisana pozicija unutar kruga poluprečnika r sa centrom u dst.
		double phi = random.nextDouble();                // Nasumično biramo ugao.
		double d = r * random.nextDouble();              // Nasumično biramo udaljenost.

		// Vector.polar(d, phi) vraća vektor sa uglom phi i intenzitetom d.
		// Da bismo dobili source poziciju, taj vektor dodajemo na dst.
		return dst.add(Vector.polar(d, phi));
	}
	
}
