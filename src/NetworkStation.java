import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;
// FIXME import processing.sound.*;

/**
 * Thanks to our hero Abe Pazos at https://vimeo.com/channels/p5idea, who
 * teaches us how to use Processing inside IDEA
 */
@SuppressWarnings("NonAsciiCharacters")
public class NetworkStation extends PApplet {
	// essentials
	JSONArray json;
	PeasyCam cam;
	PFont font;
	PImage textFrame;
	int SPHERE_DETAIL;
	float r;

	// all the lists!
	List<String> passages;
	List<Integer> durations;
	List<List<List<Integer>>> highlightIndices;
	PVector[][] globe;
	// FIXME can't figure out audio file type

	// classes
	DialogBox dialog;
    // FIXME SoundFile file;

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
		SPHERE_DETAIL = 16;
		r = 100;
		textFont(font, 14);

		loadData();

		dialog = new DialogBox(this, passages, highlightIndices, textFrame);
		initializeGlobeArray();

		// just making sure that I have the right number of elements!
		// System.out.println(Arrays.deepToString(globe));
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

	private void initializeGlobeArray() {
		// since Java can handle 2D arrays, we don't need to do much work.
		// However, JavaScript doesn't support 2D arrays, so we have to
		// initialize them ourselves.

		// we have to add one because otherwise, we can't close the shape, so
		// there will be a hole in our sphere!

		/*TODO
			  what I really did here
			     found out that you can't really nest ArrayLists
			     you can use a fix me without the space to represent a bad line!
			     initialized the sphere detail
			     made the globe an array of arrays of PVectors
			     added a test in my setup
		*/
		globe = new PVector[SPHERE_DETAIL+1][SPHERE_DETAIL+1];
	}

	private void populateGlobe() {
		float x, y, z, θ, φ;

		for (int i = 0; i < globe.length; i++) {
			θ = map(i, 0, globe.length - 1, 0, PI);
			for (int j = 0; j < globe[i].length; j++) {
				// normally, we live in the x-y-z space. However, in
				// spherical coordinates, we use r-θ-φ space. I have a proof
				// with more details about this convergence. from my
				// JavaScript comments:
				// r is just the radius of our sphere.
				// θ is the clockwise angle from the positive x-axis onto the
				// xy plane, while φ is the clockwise angle from the positive
				// z-axis onto our location vector (x, y, z).

				φ = map(j, 0, globe[i].length - 1, 0, PI);

				// I have some old proofs using trigonometry and 3D
				// coordinates that prove this. It's far less messy than
				// Wikipedia's version, which seems to involve heavy matrix
				// math.
				x = r * sin(φ) * cos(θ);
				y = r * sin(φ) * sin(θ);
				z = r * cos(φ);
				globe[i][j] = new PVector(x, y, z);
				strokeWeight(5);
				stroke(0, 0, 100);
				push();
				rotateX(PI/2);
				point(x, y, z);
				pop();
				strokeWeight(2);
			}
		}
	}

	@Override
	public void draw() {
		background(210, 100, 30, 100);

		// the setup of this dialog system
		drawBlenderAxes();
		renderTextFrame();

		// Adam in the background, from my month of playing Metroid Dread
		populateGlobe();

		// the real dialog part!
		cam.beginHUD();
		dialog.render();
		cam.endHUD();
	}
}