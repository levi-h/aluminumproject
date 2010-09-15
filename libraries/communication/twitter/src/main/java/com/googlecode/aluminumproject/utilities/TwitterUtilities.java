/*
 * Copyright 2010 Levi Hoogenberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;
import com.googlecode.aluminumproject.utilities.environment.Request;
import com.googlecode.aluminumproject.utilities.environment.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Provides Twitter-related utility methods.
 *
 * @author levi_h
 */
public class TwitterUtilities {
	private TwitterUtilities() {}

	/**
	 * Sends a signed request.
	 *
	 * @param method the request method to use
	 * @param url the URL of the resource to request
	 * @param token the token to use
	 * @param secret the secret to use
	 * @param requestParameters the request parameters to use
	 * @return the server's response to the request
	 * @throws UtilityException when the request can't be created or when it can't be sent
	 */
	public static Response sendRequest(String method, String url,
			String token, String secret, Map<String, String> requestParameters) throws UtilityException {
		Request request = EnvironmentUtilities.getWebClient().createRequest(method, url);

		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("oauth_timestamp", Long.toString(System.currentTimeMillis() / 1000L));
		parameters.put("oauth_signature_method", "HMAC-SHA1");
		parameters.put("oauth_version", "1.0");
		parameters.put("oauth_nonce", "TODO");
		parameters.put("oauth_consumer_key", "aBiTOncsoeqgE21wpg");

		if (!token.equals("")) {
			parameters.put("oauth_token", token);
		}

		for (Map.Entry<String, String> requestParameter: requestParameters.entrySet()) {
			String name = requestParameter.getKey();
			String value = requestParameter.getValue();

			parameters.put(name, value);

			request.addParameter(name, value);
		}

		parameters.put("oauth_signature", getSignature(method, url, secret, parameters));

		request.addHeader("Authorization", getAuthorisationHeader(parameters));

		return request.getResponse();
	}

	private static String getSignature(
			String method, String url, String secret, SortedMap<String, String> parameters) throws UtilityException {
		StringBuilder signatureBuilder = new StringBuilder();

		for (String key: parameters.keySet()) {
			if (signatureBuilder.length() > 0) {
				signatureBuilder.append("&");
			}

			signatureBuilder.append(key);
			signatureBuilder.append("=");
			signatureBuilder.append(encodeUrlComponent(parameters.get(key)));
		}

		byte[] key;
		byte[] body;

		try {
			key = String.format("%s&%s", "oMheEHqYotrh7pa6kw2S6OxTZA4hSGUVUJA4U44RQ4", secret).getBytes("utf-8");

			String encodedUrl = encodeUrlComponent(url);
			String encodedSignature = encodeUrlComponent(signatureBuilder.toString());

			body = String.format("%s&%s&%s", method, encodedUrl, encodedSignature).getBytes("utf-8");
		} catch (UnsupportedEncodingException exception) {
			throw new UtilityException(exception, "can't get bytes in utf-8");
		}

		Mac mac;

		try {
			mac = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException exception) {
			throw new UtilityException(exception, "can't create signer");
		}

		try {
			mac.init(new SecretKeySpec(key, mac.getAlgorithm()));
		} catch (InvalidKeyException exception) {
			throw new UtilityException(exception, "can't create key for signing");
		}

		mac.update(body);

		return encodeBase64(mac.doFinal());
	}

	private static String getAuthorisationHeader(SortedMap<String, String> parameters) throws UtilityException {
		StringBuilder headerBuilder = new StringBuilder("OAuth ");

		for (String key: parameters.keySet()) {
			if (key.startsWith("oauth_")) {
				if (headerBuilder.length() > 6) {
					headerBuilder.append(", ");
				}

				headerBuilder.append(key);
				headerBuilder.append("=");
				headerBuilder.append("\"");
				headerBuilder.append(encodeUrlComponent(parameters.get(key)));
				headerBuilder.append("\"");
			}
		}

		return headerBuilder.toString();
	}

	private static String encodeBase64(byte[] bytes) {
		StringBuilder builder = new StringBuilder();

		int m = bytes.length * 8 % 6;
		int n = bytes.length * 8 + ((m == 0) ? m : (6 - m));

		BitSet set = new BitSet(n);

		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];

			for (int j = 1; j <= 8; j++) {
				set.set((i + 1) * 8 - j, (b & 1) == 1);

				b >>>= 1;
			}
		}

		for (int i = 0; i < n / 6; i++) {
			int s = 0;
			int t = 32;

			for (int j = 0; j < 6; j++) {
				if (set.get(i * 6 + j)) {
					s += t;
				}

				t >>= 1;
			}

			builder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(s));
		}

		builder.append((m == 2) ? "==" : (m == 4) ? "=" : "");

		return builder.toString();
	}

	private static String encodeUrlComponent(String text) throws UtilityException {
		try {
			return URLEncoder.encode(text, "utf-8").replace("+", "%20");
		} catch (UnsupportedEncodingException exception) {
			throw new UtilityException(exception, "can't encode '", text, "' in utf-8");
		}
	}
}