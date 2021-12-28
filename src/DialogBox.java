import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.List;

public class DialogBox {
	PImage textFrame;
	List<String> passages;
	List<List<List<Integer>>> highlightIndices;
	String hardCodePassage;
	PApplet app;

	DialogBox(PApplet app, List<String> passages,
			  List<List<List<Integer>>> highlightIndices, PImage textFrame) {
		// the text frame we use. I don't think this is necessary yet.
		this.textFrame = textFrame;
		// just a hardcoded passage for now.
		this.hardCodePassage = "Hello, Spherical Geometry! And hello, Adam!";
		// we'll need to make use of fills soon and this is not an applet.
		this.app = app;
		// a list of Adam's passages. I fixed a typo and a data point.
		this.passages = passages;
		this.highlightIndices = highlightIndices;
	}

	// this is what we made dialogBox for! renders the box.
	public void render() {
		int margin = 72;

		// my javascript comment from the same place:

		PVector cursor = new PVector(margin, 270 + this.app.textAscent());

		this.app.textSize(18);
		this.app.text("ADAM", 62, 260);
		this.app.textSize(14);

		for (int i = 0; i < this.hardCodePassage.length(); i++) {
			char letter = this.hardCodePassage.charAt(i);

			this.app.fill(0, 0, 100);
			this.app.text(letter, cursor.x, cursor.y);

			// advance the cursor. TODO Do not code past here, nya!
			cursor.add(this.app.textWidth(letter), 0);
		}
	}
}
