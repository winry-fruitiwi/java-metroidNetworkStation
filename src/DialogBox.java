import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;

public class DialogBox {
	PImage textFrame;
	List<String> passages;
	List<List<List<Integer>>> highlightIndices;
	String hardCodePassage;
	PApplet app;

	DialogBox(PApplet app, List<String> passages,
			  List<List<List<Integer>>> highlightIndices, PImage textFrame) {
		this.textFrame = textFrame;
		this.hardCodePassage = "Hello, NotJustGroovy! And hello, Adam!";
		this.app = app;
	}
}
