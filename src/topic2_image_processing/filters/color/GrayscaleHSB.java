package topic2_image_processing.filters.color;

import javafx.scene.paint.Color;
import topic2_image_processing.filters.ColorFilter;

/**
 * Filter koji sliku pretvara u "crno-belu" sliku, odnosno sliku iscrtanu samo nijansama sive. Radi tako što u HSB
 * modelu postavlja S komponentu na 0, dok druge dve zadržava neizmenjene.
 */
public class GrayscaleHSB extends ColorFilter {

	@Override
	public Color processColor(Color input) {
		double bri = input.getBrightness();
		
		return Color.BLACK;
	}
	
}	
