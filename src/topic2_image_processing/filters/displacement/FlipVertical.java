package topic2_image_processing.filters.displacement;

import xyz.marsavic.geometry.Vector;
import topic2_image_processing.filters.DisplacementFilter;

/**
 * Obrće sliku po y-osi.
 */
public class FlipVertical extends DisplacementFilter {
	
	@Override
	public Vector source(Vector dst, Vector dim) {
		// Vraćamo poziciju koja je osno simetrična u odnosu na srednju horizontalu.
		return new Vector(dst.x(), dim.y() - 1 - dst.y());
	}
	
}
