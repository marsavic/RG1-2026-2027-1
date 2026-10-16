
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import xyz.marsavic.drawingfx.application.DrawingApplication;
import xyz.marsavic.drawingfx.drawing.Drawing;
import xyz.marsavic.drawingfx.drawing.DrawingUtils;
import xyz.marsavic.drawingfx.drawing.View;
import xyz.marsavic.drawingfx.gadgets.annotations.GadgetAnimation;
import xyz.marsavic.geometry.Transformation;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.utils.Numeric;


public class HelloWorld implements Drawing {
	
	@GadgetAnimation(start = true)
	double time = 0;
	
	Font font = new Font(100);
	
	
	@Override
	public void draw(View view) {
		view.addTransformation(Transformation.identity()
				.scale(0.1 * (Numeric.sinT(0.17 * time) + 3))
				.translate(new Vector(200 * Numeric.sinT(time * 0.21), 0))
				.rotate(0.17 * time + Numeric.sinT(time * 0.13))
		);
		DrawingUtils.clear(view, Color.BLACK);
		view.setFont(font);
		view.setTextAlign(TextAlignment.CENTER);
		view.setFill(Color.hsb(time * 23, 0.8, 1));
		view.fillTextNativeOrientation("Grafikaaaaaa!", Vector.ZERO);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(720, 720);
	}
	
}
