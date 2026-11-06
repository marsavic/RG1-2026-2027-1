package topic4_vector_graphics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetBoolean;
import xyz.marsavic.drawingfx.utils.camera.CameraSimple;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.input.InputEvent;
import xyz.marsavic.input.InputState;

import static xyz.marsavic.geometry.Vector.*;


public class RasterVsVector implements Drawing {

	static {
		System.setProperty("prism.fontSizeLimit", "0.1");
	}

	@GadgetBoolean
	boolean interpolation = false;

	CameraSimple camera = new CameraSimple();
	
	Image image = img();

	
	Image img() {
		WritableImage image = new WritableImage(200, 200);
		PixelWriter pw = image.getPixelWriter();
		
		for (int j = 0; j < image.getHeight(); j++) {
			for (int i = 0; i < image.getWidth(); i++) {
				int x = i - 100;
				int y = j - 100;
				Color c = (x >= -60 && y >= -60 && x+y < 0) ? Color.BLACK : Color.SPRINGGREEN;
				pw.setColor(i, j, c);
			}
		}
		
		return image;
	}

	
	
	@Override
	public void draw(View view) {
		view.addTransformation(camera.getTransformation());
		DrawingUtils.clear(view, Color.gray(0.125));
		
		view.setImageSmoothing(interpolation);
		view.drawImageCenteredNativeOrientation(xy(-200, 0), image);
		
		view.setFill(Color.HOTPINK);
		view.fillBoxCentered(xy(200, 0), xy(100, 100));
		view.setFill(Color.BLACK);
		view.fillPolygon(xy(140, 60), xy(260, 60), xy(140, -60));

		view.setFill(Color.WHITE);
		view.fillTextNativeOrientation("Raster graphics", xy(-280, -120));
		view.fillTextNativeOrientation("Vector graphics", xy( 120, -120));
		
		DrawingUtils.drawGrid(view, 100, 5, Color.hsb(0, 0, 1, 0.125));
	}

	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}

	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 400);
	}

}
