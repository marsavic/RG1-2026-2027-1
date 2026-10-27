package topic2_image_processing.filters.displacement;

import xyz.marsavic.geometry.Vector;
import topic2_image_processing.filters.DisplacementFilter;

/**
 * Obrće sliku po x-osi.
 */
public class FlipHorizontal extends DisplacementFilter {
	
	@Override
	public Vector source(Vector dst, Vector dim) {
		// Vraćamo poziciju koja je osno simetrična u odnosu na srednju vertikalu.
		return new Vector(dim.x() - 1 - dst.x(), dst.y());
	}
	
}
