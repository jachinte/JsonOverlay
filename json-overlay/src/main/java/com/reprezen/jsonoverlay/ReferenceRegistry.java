package com.reprezen.jsonoverlay;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;

public class ReferenceRegistry {

	private Map<String, ReferenceManager> managers = new HashMap<>();
	private JsonLoader loader = new JsonLoader();
	private Map<Pair<String, String>, JsonOverlay<?>> overlaysByRef = new HashMap<>();
	// can't use Pair here because we need to index by JsonNode identity, not
	// using
	// its equals impl
	private Map<JsonNode, Map<String, JsonOverlay<?>>> overlaysByJson = new IdentityHashMap<>();

	public ReferenceManager getManager(URL baseUrl) {
		return managers.get(baseUrl.toString());
	}

	public void registerManager(URL baseUrl, ReferenceManager manager) {
		managers.put(baseUrl.toString(), manager);
	}

	public JsonNode loadDoc(URL url) throws IOException {
		return loader.load(url);
	}

	public JsonOverlay<?> getOverlay(String normalizedRef, String factorySig) {
		return overlaysByRef.get(Pair.of(normalizedRef, factorySig));
	}

	public void register(String normalizedRef, String factorySig, JsonOverlay<?> overlay) {
		overlaysByRef.put(Pair.of(normalizedRef, factorySig), overlay);
	}

	public JsonOverlay<?> getOverlay(JsonNode json, String factorySig) {
		Map<String, JsonOverlay<?>> overlaysBySig = overlaysByJson.get(json);
		return overlaysBySig != null ? overlaysBySig.get(factorySig) : null;
	}

	public void register(JsonNode json, String factorySig, JsonOverlay<?> overlay) {
		// can't share boolean or nulls because they don't have a public
		// constructor,
		// and factory uses shared instances
		if (!json.isMissingNode() && !json.isBoolean() && !json.isNull()) {
			if (!overlaysByJson.containsKey(json)) {
				overlaysByJson.put(json, new HashMap<>());
			}
			overlaysByJson.get(json).put(factorySig, overlay);
		}
	}

	public Optional<PositionInfo> getPositionInfo(String docUrl, JsonPointer pointer) {
		return loader.getPositionInfo(docUrl, pointer);
	}
}