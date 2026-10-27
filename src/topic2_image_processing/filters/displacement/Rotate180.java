package topic2_image_processing.filters.displacement;

import xyz.marsavic.geometry.Vector;
import topic2_image_processing.filters.DisplacementFilter;

/**
 * Rotira sliku za 180 stepeni.
 */
public class Rotate180 extends DisplacementFilter {

	@Override
	public Vector source(Vector dst, Vector dim) {
		// Vraćamo poziciju koja je centralno simetrična u odnosu na centar slike.
		return new Vector(dim.x() - 1 - dst.x(), dim.y() - 1 - dst.y());
	}
	
}
