import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Thanks to our hero Abe Pazos at https://vimeo.com/channels/p5idea, who
 * teaches us how to use Processing inside IDEA
 */
public class NetworkStation extends PApplet {
	JSONArray json;

	//	String[] passages;
	//	int[][] highlightIndices;

	List<String> passages;
	List<Integer> durations;
	List<List<Integer>> highlightIndices;

	public static void main(String[] args) {
		PApplet.main(new String[]{NetworkStation.class.getName()});
	}

	@Override
	public void settings() {
		size(640, 360);
	}

	@Override
	public void setup() {
		rectMode(RADIUS);
		colorMode(HSB, 360f, 100f, 100f, 100f);

		loadData();
	}

	private void loadData() {
		passages = new ArrayList<>();
		durations = new ArrayList<>();

		// creating the json object
		json = loadJSONArray("data/passages.json");
		// my attempts to try to understand java json manipulation...
		// the first thing we can see is the entire json, then we just need
		// the array that comes with it. then we need to make sure we extract
		// the json object at a certain index that we can control so now extract
		// the json array at the highlight index.

		// TODO turn this into a loop later, i'm just not sure how to do it
		// System.out.println(json.getJSONArray("passages").getJSONObject(0)
		// .getJSONArray("highlightIndices"));

		for (int i = 0; i < json.size(); i++) {
			passages.add(json.getJSONObject(i).getString("text"));
			durations.add(json.getJSONObject(i).getInt("ms"));
		}

		System.out.println(passages);
		System.out.println(durations);
	}

	@Override
	public void draw() {
		background(210, 100, 30, 100);
		rect(mouseX, mouseY, 20, 20);
	}

	@Override
	public void mousePressed() {
		System.out.println(mouseX);
	}
}