import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Thanks to our hero Abe Pazos at https://vimeo.com/channels/p5idea, who
 * teaches us how to use Processing inside IDEA
 */
public class NetworkStation extends PApplet {
	// essentials
	JSONArray json;
	PeasyCam cam;
	PFont font;
	PImage textFrame;

	// all the lists!
	List<String> passages;
	List<Integer> durations;
	List<List<List<Integer>>> highlightIndices;

	// classes
	DialogBox dialog;

	public static void main(String[] args) {
		PApplet.main(new String[]{NetworkStation.class.getName()});
	}

	@Override
	public void settings() {
		size(640, 360, P3D);
	}

	@Override
	public void setup() {
		rectMode(RADIUS);
		colorMode(HSB, 360f, 100f, 100f, 100f);
		cam = new PeasyCam(this, 0, 0, 0, 500);
		font = createFont("data/gigamarujr.ttf", 14);
		textFrame = loadImage("data/textFrame.png");
		textFont(font, 14);

		loadData();

		dialog = new DialogBox(this, passages, highlightIndices, textFrame);
	}


	private void renderTextFrame() {
		cam.beginHUD();
		image(textFrame, 0, 0, width, height);
		cam.endHUD();
	}

	final int SATURATION = 100;
	final int P_BRIGHTNESS = 100;
	final int N_BRIGHTNESS = 40;

	private void drawBlenderAxes() {
		// x-axis
		stroke(0, SATURATION, P_BRIGHTNESS);
		line(0, 0, 4000, 0);

		stroke(0, SATURATION, N_BRIGHTNESS);
		line(-4000, 0, 0, 0);

		// y-axis (Webstorm has the values inverted!)
		stroke(120, SATURATION, P_BRIGHTNESS);
		line(0, 0, 0, 4000);

		stroke(120, SATURATION, N_BRIGHTNESS);
		line(0, -4000, 0, 0);

		// z-axis
		stroke(240, SATURATION, P_BRIGHTNESS);
		line(0, 0, 0, 0, 0, 4000);

		stroke(240, SATURATION, N_BRIGHTNESS);
		line(0, 0, -4000, 0, 0, 0);
	}

	private void loadData() {
		passages = new ArrayList<>();
		durations = new ArrayList<>();
		highlightIndices = new ArrayList<>();

		// creating the json object
		json = loadJSONArray("data/passages.json");
		// my attempts to try to understand java json manipulation...
		// the first thing we can see is the entire json, then we just need
		// the array that comes with it. then we need to make sure we extract
		// the json object at a certain index that we can control so now extract
		// the json array at the highlight index.

		for (int i = 0; i < json.size(); i++) {
			// our json object
			JSONObject obj = json.getJSONObject(i);

			passages.add(obj.getString("text"));
			durations.add(obj.getInt("ms"));

			List<List<Integer>> doubledIndices = new ArrayList<>();
			JSONArray arr = obj.getJSONArray("highlightIndices");
			// loop through the contents of the JSONArray
			for (int j = 0; j < arr.size(); j++) {
				JSONObject indexPair = arr.getJSONObject(j);
				List<Integer> indices = new ArrayList<>();

				indices.add(indexPair.getInt("start"));
				indices.add(indexPair.getInt("end"));

				doubledIndices.add(indices);
			}
			highlightIndices.add(doubledIndices);
		}

		System.out.println(passages);
		System.out.println(durations);
		System.out.println(highlightIndices);

	}

	@Override
	public void draw() {
		background(210, 100, 30, 100);

		// basically the setup of this dialog system
		drawBlenderAxes();
		renderTextFrame();

		// the real dialog part!
		cam.beginHUD();
		dialog.render();
		cam.endHUD();
	}
}