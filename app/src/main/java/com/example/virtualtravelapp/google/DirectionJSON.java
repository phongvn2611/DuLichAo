package com.example.virtualtravelapp.google;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionJSON {
	public List<List<HashMap<String, String>>> parse(JSONObject jsonObject) {
		List<List<HashMap<String, String>>> routes = new ArrayList<>();
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;
		// Time va do dai
		JSONObject jDistance = null;
		JSONObject jDuration = null;

		try {
			jRoutes = jsonObject.getJSONArray("routes");

			//all routes
			for (int i = 0; i < jRoutes.length(); i++) {
				JSONObject object = (JSONObject) jRoutes.get(i);
				jLegs = object.getJSONArray("legs");

				List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

				//Legs
				for (int j = 0; j < jLegs.length(); j++) {
					//get distance khoang cach
					JSONObject objectLeg = (JSONObject) jLegs.get(j);
					jDistance = objectLeg.getJSONObject("distance");
					HashMap<String,String> distance = new HashMap<>();
					distance.put("distance", jDistance.getString("text"));

					//get thoi gian duration
					jDuration = objectLeg.getJSONObject("duration");
					HashMap<String,String> duration = new HashMap<>();
					duration.put("duration", jDuration.getString("text"));

					//add distance
					path.add(distance);
					//add duration
					path.add(duration);

					//stepppppppppppppp
					jSteps = objectLeg.getJSONArray("steps");
					//Step
					for (int k = 0; k < jSteps.length(); k++) {
						String polyline = "";
						JSONObject objectSteps = (JSONObject) jSteps.get(k);
						JSONObject objectPoly = (JSONObject) objectSteps.get("polyline");
						polyline = objectPoly.getString("points");

						List<LatLng> list = decodePoly(polyline);

						//all point
						for (int l = 0; l < list.size(); l++) {
							HashMap<String, String> hm = new HashMap<>();
							hm.put("lat", Double.toString(list.get(l).latitude));
							hm.put("lng", Double.toString(list.get(l).longitude));
							path.add(hm);
						}
					}
					routes.add(path);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return routes;
	}

	/**
	 * Method to decode polyline points
	 * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
	 */
	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}
}
