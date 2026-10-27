package topic2_image_processing.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xyz.marsavic.geometry.Vector;

/**
 * Displacement filter je filter koji "premešta" piksele. Definisan je funkcijom koja nam za svaki položaj na rezultujućoj
 * slici vraća odakle treba uzeti piksel sa originalne slike.
 */
public abstract class DisplacementFilter extends Filter {

	/**
	 * @param dst - položaj piksela na rezultujućoj slici
	 * @param dim - dimenzije slike
	 * @return Položaj piksela sa originalne slike koji kopiramo na poziciju dst rezultujuće slike
	 */
	public abstract Vector source(Vector dst, Vector dim);
	
	
	@Override
	public Image process(Image input) {
		final int w = (int) input.getWidth();
		final int h = (int) input.getHeight();
		Vector dim = new Vector(w, h);
		
		WritableImage output = new WritableImage(w, h);

		PixelReader pr = input.getPixelReader();
		PixelWriter pw = output.getPixelWriter();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				// Pomoću funkcije source nalazimo koordinate odakle treba kopirati.
				Vector dst = new Vector(x, y);
				Vector src = source(dst, dim);
				
				// Zaokružujemo te koordinate na najbliži ceo broj.
				int srcX = (int) (src.x() + 0.5);
				int srcY = (int) (src.y() + 0.5);
				
				Color color;
				if (srcX < 0 || srcY < 0 || srcX >= w || srcY >= h) {
					color = Color.TRANSPARENT;         // Ako source koordinate dolaze van slike stavljamo potpuno providnu boju. 
				} else {
					color = pr.getColor(srcX, srcY);   // Inače, boja se uzima sa source koordinata.
				}
				pw.setColor(x, y, color);
			}
		}
		
		return output;
	}	
}
