package topic2_image_processing.filters.displacement;

import xyz.marsavic.geometry.Vector;
import topic2_image_processing.filters.DisplacementFilter;


/**
 * Uveličava gornji levi ćošak slike koristeći zadati zoom faktor.
 */
public class Zoom extends DisplacementFilter {
	final double k;
	
	
	public Zoom(double k) {
		this.k = k;
	}
	
	
	@Override
	public Vector source(Vector dst, Vector dim) {
		// Delimo obe koordinate vektora sa k.
		return dst.div(k);
	}
	
}
