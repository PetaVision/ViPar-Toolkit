package dannydelott.inwlg;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class JsonItem {

	private JsonObject json;

	// ///////////////
	// CONSTRUCTORS //
	// ///////////////

	public JsonItem(String c, String[] h) {
		json = new JsonObject();
		JsonArray hyponyms = new JsonArray();

		json.add("category", c);
		for (String word : h) {
			hyponyms.add(word);
		}
		json.add("hyponyms", hyponyms);
	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public JsonObject getJsonObject() {
		return json;
	}

}
