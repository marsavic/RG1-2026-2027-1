package topic2_image_processing.filters.color;

import javafx.scene.paint.Color;
import topic2_image_processing.filters.ColorFilter;

/**
 * Čini boje na slici zasićenijim u zavisnosti od parametra zadatog u konstruktoru.
 */
public class Saturate extends ColorFilter {
	final double k;
	
	
	public Saturate(double k) {
		this.k = k;
	}


	@Override
	public Color processColor(Color input) {
		double s = input.getSaturation();
		
		return Color.hsb(0,0,0,0);
	}
	
}	
