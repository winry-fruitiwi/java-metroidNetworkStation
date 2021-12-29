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
	int index;

	DialogBox(PApplet app, List<String> passages,
			  List<List<List<Integer>>> highlightIndices, PImage textFrame) {
		// the text frame we use. I don't think this is necessary yet.
		this.textFrame = textFrame;
		// just a hardcoded passage for now.
		this.hardCodePassage = "Hello, Spherical Geometry! And hello, Adam! I" +
				" don't like the new gigamarujr font because it's really soft" +
				". It doesn't feel as crisp as notjustgroovy, which I much " +
				"prefer (you have violated the notjustgroovy rule) due to it " +
				"being (preferably) crisp. ";
		// we'll need to make use of fills soon and this is not an applet.
		this.app = app;
		// a list of Adam's passages. I fixed a typo and a data point.
		this.passages = passages;
		this.highlightIndices = highlightIndices;
		this.index = 0;
	}

	// this is what we made dialogBox for! renders the box.
	public void render() {
		int margin = 72;

		// my javascript comment from the same place:
		// our current position. since text coordinates start at bottom
		// left, we have to modify the height so that even the largest font
		// won't be clipped at the top.
		PVector cursor = new PVector(margin, 270 + this.app.textAscent());

		this.app.textSize(18);
		this.app.text("TIGREX", 62, 260);
		this.app.textSize(14);

		for (int i = 0; i < this.index; i++) {
			char letter = this.hardCodePassage.charAt(i);

			this.app.fill(0, 0, 100);
			this.app.text(letter, cursor.x, cursor.y);

			// start of word wrap.
			if (letter == ' ') {
				// we don't need a currentDelimiter, it's redundant!
				int nextDelimiter = this.hardCodePassage.indexOf(" ", i + 1);

				String nextWord = this.hardCodePassage.substring(
						i,
						nextDelimiter + 1
				);

				// if we're about to go over our space, move to a new line.
				if (app.textWidth(nextWord) + cursor.x >=
						app.width-margin) {
					cursor.x = margin;
					cursor.y+=app.textAscent()+this.app.textDescent()+2;
					continue;
				}
			}

			// advance the cursor. TODO Do not code past here, nya!
			cursor.add(app.textWidth(letter), 0);
		}

		// if the frameCount is divisible by a certain number
		if (app.frameCount % 3 == 0) {
			this.index++;
			if (this.index >= this.hardCodePassage.length()-1) {
				this.index = this.hardCodePassage.length()-1;
			}
		}
	}
}
