package com.ooyala.playback.utils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.ooyala.playback.utils.StorageCredentials.S3Host;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.http.Response;
import com.ooyala.qe.common.util.PropertyReader;

public class APIUtils {

	private static Logger logger = Logger.getLogger(APIUtils.class);

	private PropertyReader properties;
	protected String backlot_api_endPoint;
	protected String version;
	protected String assets;
	protected String publishing_rules;
	protected String publishing_rule;
	protected String rl_api_endpoint;
	protected String entitlements;
	protected String providers;
	protected String accounts;
	protected String content;
	protected String external_products;
	protected String accountId;
	protected int httpStatus;
	protected String api_key;
	protected String signature;
	protected String internal;
	protected String device_management;
	protected String pcode;
	protected String account_id;
	protected String devices;
	protected String device_limit;
	protected String storage_credentials;
	protected String helios_api_endPoint_prod;
	protected String process;
	protected String json_processing_profiles;
	protected String profiles;
	NeoRequest neoRequest;

	public APIUtils() {
		try {
			neoRequest = new NeoRequest();
			properties = PropertyReader.getInstance("api.properties");
			String sasStaging = System.getProperty(CommandLineParameters.runSASStaging);

			backlot_api_endPoint = sasStaging == null || sasStaging.isEmpty() || sasStaging.equalsIgnoreCase("false")
					? properties.getProperty("backlot_api_endPoint")
					: properties.getProperty("backlot_api_endPoint_staging");
			storage_credentials = properties.getProperty("storage_credentials");
			helios_api_endPoint_prod = properties.getProperty("helios_api_endPoint_prod");

			version = properties.getProperty("version");
			assets = properties.getProperty("assets");
			publishing_rules = properties.getProperty("publishing_rules");
			publishing_rule = properties.getProperty("publishing_rule");

			rl_api_endpoint = sasStaging == null || sasStaging.isEmpty() || sasStaging.equalsIgnoreCase("false")
					? properties.getProperty("rl_api_endpoint") : properties.getProperty("rl_api_endpoint_staging");
			entitlements = properties.getProperty("entitlements");
			providers = properties.getProperty("providers");
			accounts = properties.getProperty("accounts");
			content = properties.getProperty("content");
			external_products = properties.getProperty("external_products");
			accountId = properties.getProperty("accountId");
			api_key = properties.getProperty("api_key");
			signature = properties.getProperty("signature");
			internal = properties.getProperty("internal");
			device_management = properties.getProperty("device_management");
			pcode = properties.getProperty("pcode");
			account_id = properties.getProperty("account_id");
			devices = properties.getProperty("devices");
			device_limit = properties.getProperty("device_limit");
			process = properties.getProperty("process");
			json_processing_profiles = properties.getProperty("json_processing_profiles");
			profiles = properties.getProperty("profiles");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (OoyalaException e) {
			e.printStackTrace();
		}
	}

	public boolean isEntitlementAvailable(String pcode, String embedCode) throws Exception {

		Response response = neoRequest.makeRequest(rl_api_endpoint, "", null, "GET", null, null, null, version,
				entitlements, providers, pcode, accounts, accountId, content, assets, embedCode, external_products,
				"default");

		return response.getResponseCode() == 200;
	}

	public boolean deleteEntitlement(String pcode, String embedCode) throws Exception {

		Response response = neoRequest.makeRequest(rl_api_endpoint, "", null, "DELETE", null, null, null, version,
				entitlements, providers, pcode, accounts, accountId, content, assets, embedCode, external_products,
				"default");

		return response.getResponseCode() == 200;
	}

	public boolean addEntitlement(String pcode, String embedCode, int deviceCount) throws Exception {

		JSONObject json = new JSONObject();
		json.put("content_id", embedCode);
		json.put("external_product_id", "default");
		json.put("num_devices_to_bind", deviceCount);
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(json);
		JSONObject requestBody = new JSONObject();
		requestBody.put("assets", jsonArray);

		Response response = neoRequest.makeRequest(rl_api_endpoint, "", null, "POST", requestBody.toString(), null,
				null, version, entitlements, providers, pcode, accounts, accountId, content);

		return response.getResponseCode() == 200;
	}

	public HashMap<String, String> getDevices(String pcode) throws IOException {

		Response response = neoRequest.makeRequest(rl_api_endpoint, "", null, "GET", null, null, null,
				device_management, this.pcode, pcode, account_id, accountId, devices);

		if (response.getResponseCode() == 200) {
			HashMap<String, String> devices = new HashMap<>();
			JSONObject json = new JSONObject(response.getResponse());
			JSONArray jsonArray = json.getJSONArray("devices");
			for (int i = 0; i < jsonArray.length(); i++) {
				devices.put(jsonArray.getJSONObject(i).getString("user_agent"),
						jsonArray.getJSONObject(i).getString("public_device_id"));
			}
			return devices;
		}
		return null;

	}

	public boolean deleteAllDevices(String pcode) throws IOException {

		JSONObject json = new JSONObject();
		json.put("actor", "sasport");
		json.put("actor_type", "admin");

		Response response = neoRequest.makeRequest(rl_api_endpoint, "", null, "DELETE", json.toString(), null, null,
				device_management, this.pcode, pcode, account_id, accountId, devices);

		return response.getResponseCode() == 200 ? true
				: (response.getResponseCode() == 404 && response.getResponse().contains("device does not exist"));

	}

	public boolean deleteDevice(String pcode, String deviceId) throws IOException {

		JSONObject json = new JSONObject();
		json.put("actor", "sasport");
		json.put("actor_type", "admin");

		Response response = neoRequest.makeRequest(rl_api_endpoint, "", null, "DELETE", json.toString(), null, null,
				device_management, this.pcode, pcode, account_id, accountId, devices, deviceId);

		return response.getResponseCode() == 200 ? true
				: (response.getResponseCode() == 404 && response.getResponse().contains("device does not exist"));
	}

	public boolean updateDeviceLimit(String pcode, int count) {
		JSONObject json = new JSONObject();
		json.put("device_limit", count);

		Response response = neoRequest.makeRequest(rl_api_endpoint, "", null, "PUT", json.toString(), null, null,
				device_management, this.pcode, pcode, account_id, accountId, device_limit);

		return response.getResponseCode() == 200;

	}

	public boolean updatePublishingRule(String embedCode, String publishingRuleId, String api_key) throws Exception {

		Map<String, String> queryString = new HashMap<>();
		queryString.put("api_key", api_key);

		Response response = neoRequest.makeRequest(backlot_api_endPoint, "", api_key, "PUT", null, null, queryString,
				version, assets, embedCode, publishing_rule, publishingRuleId);

		return response.getResponseCode() == 200;
	}

	public boolean getPublishingRule(String embedCode, String publishingRuleId, String api_key) throws Exception {

		Map<String, String> queryString = new HashMap<>();
		queryString.put("api_key", api_key);

		Response response = neoRequest.makeRequest(backlot_api_endPoint, "", api_key, "GET", null, null, queryString,
				version, assets, embedCode, publishing_rule);

		return response.getResponseCode() == 200 && response.getResponse().contains(publishingRuleId);
	}

	public HashMap<String, String> getPublishingRuleIds(String api_key) throws Exception {

		Map<String, String> queryString = new HashMap<>();
		queryString.put("api_key", api_key);

		Response response = neoRequest.makeRequest(backlot_api_endPoint, "", api_key, "GET", null, null, queryString,
				version, publishing_rules);

		if (response.getResponse() != null && !response.getResponse().isEmpty()) {
			JSONObject json = new JSONObject(response.getResponse());
			HashMap<String, String> rules = new HashMap<>();

			JSONArray items = json.getJSONArray("items");

			for (int i = 0; i < items.length(); i++) {
				JSONObject j = items.getJSONObject(i);
				if (j.getString("name").equals("Default Group")) {
					rules.put("default", j.getString("id"));
				} else if (j.getString("name").equals("Flight Time") || j.getString("name").equals("Geo Restriction")) {
					rules.put("specific", j.getString("id"));
				}
			}

			return rules;
		}

		return null;

	}

	public String getPromoImageUrl(String embedCode, String pCode) {
		logger.info("Inside getPromoImageUrl method");
		String promoImageUrl = null;
		try {
			Response response = neoRequest.makeRequest("http://player.ooyala.com",
					"/player_api/v1/content_tree/embed_code/", null, "GET", null, null, null, pCode, embedCode);
			JSONObject json = new JSONObject(response.getResponse());
			promoImageUrl = json.getJSONObject("content_tree").getJSONObject(embedCode).getString("promo_image");
			logger.info("Promo Image URL : " + promoImageUrl);
		} catch (Exception e) {
			logger.error("Issue in getPromoImageUrl method : " + e.getMessage());
		}
		return promoImageUrl;
	}

	public String getCountry() {
		Response response = neoRequest.makeRequest("http://ip-api.com/json", "", null, "GET", null, null, null);
		return response.getResponse();
	}

	public String createAsset(String desc, String providerId) throws UnirestException {

		Map<String, String> headers = new HashMap<>();
		headers.put("x_provider_id", providerId);

		String key = "regression_testing/SourceFiles/BigBunny/BigBunny.mp4";

		JSONObject request = new JSONObject();
		request.put("name", desc + LocalTime.now().getNano() + new Random().nextInt(5000));
		request.put("file_name", key + LocalTime.now().getNano() + new Random().nextInt(5000));
		request.put("asset_type", "video");
		request.put("file_size", 1053651);
		request.put("host_id", 657);
		request.put("host_type", "s3_host");
		request.put("file_path", key);

		String body = request.toString();
		Response response = neoRequest.makeRequest(helios_api_endPoint_prod, "", null, "POST", body, null, headers,
				assets);
		if (response.getResponseCode() == 200) {
			JSONObject json = new JSONObject(response.getResponse());
			return json.getString("embed_code");
		}

		return null;
	}

	public String createProfile(String profile, String providerId) {
		Map<String, String> headers = new HashMap<>();
		headers.put("x_provider_id", providerId);

		Response response = neoRequest.makeRequest(helios_api_endPoint_prod, "", null, "POST", profile, null, headers,
				json_processing_profiles);
		if (response.getResponseCode() == 200) {
			JSONObject json = new JSONObject(response.getResponse());
			return json.getString("id");
		}
		return null;
	}

	public boolean transcodeAsset(String embedCode, String profileId, String providerId) throws UnirestException {
		Map<String, String> headers = new HashMap<>();
		headers.put("x_provider_id", providerId);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("initiation_type", "original_ingest");
		jsonObject.put("processing_profile_id", profileId);
		String body = jsonObject.toString();
		Response response = neoRequest.makeRequest(helios_api_endPoint_prod, "", null, "POST", body, null, headers,
				assets, embedCode, process);
		return response.getResponseCode() == 200;
	}

	public boolean isAssetLive(String embedCode, String providerId) {
		Map<String, String> headers = new HashMap<>();
		headers.put("x_provider_id", providerId);
		Response response = neoRequest.makeRequest(helios_api_endPoint_prod, "", null, "GET", null, null, headers,
				assets, embedCode);
		JSONObject json = new JSONObject(response.getResponse());
		return json.getString("status") != null && json.getString("status").equalsIgnoreCase("live");
	}

	public boolean deleteProfile(String profileId, String providerId) {
		Map<String, String> headers = new HashMap<>();
		headers.put("x_provider_id", providerId);
		Response response = neoRequest.makeRequest(helios_api_endPoint_prod, "", null, "DELETE", null, null, headers,
				profiles, profileId);
		return response.getResponseCode() == 200;
	}

}
