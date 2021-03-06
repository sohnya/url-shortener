package com.sonja.url;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Contains information about URLs and their corresponding 
 * short URLs, in a field urlShortenersMap.
 * 
 * Contains methods to shorten a URL and expand a short URL.
 * */
public class UrlShortener implements Serializable {

	private static final long serialVersionUID = 8484695048918260523L;
	private Map<String, String> urlShortenersMap;
	private final int SEQUENCELENGHT = 10;
	private final String SHORTURL = "http://cl.ip/";

	public Map<String, String> getUrlShortenersMap() {
		return urlShortenersMap;
	}

	public void setUrlShortenersMap(Map<String, String> urlShortenersMap) {
		this.urlShortenersMap = urlShortenersMap;
	}

	/**
	 * Constructor.
	 * Creates a new UrlShortener object and initializes
	 * its field urlShortenersMap
	 * */
	public UrlShortener() {
		this.urlShortenersMap = new HashMap<String, String>();
	}
	
	/**
	 * Performs a specified action on the input address.
	 * 
	 * @param Action an action from the Enum Action.
	 * 
	 * @param address the URL or short URL to which the action should be
	 * performed
	 * 
	 * @return A String with the result of the action. For shorten() it is the
	 * shortened URL. For expand() it is the original URL.
	 */

	public String doAction(Enum<?> Action, String address) throws Exception {

		if (Action.name().equals("shorten")) {

			String shortenedUrl = shorten(address);
			return shortenedUrl;

		} else if (Action.name().equals("expand")) {

			String originalUrl = expand(address);
			return originalUrl;

		} else {
			return "";
		}

	}

	/**
	 * Sets and returns a short URL given a full, absolute URL. Two identical
	 * URLs result always in the same short URL being generated. All short URLs
	 * generated begin with: http://cl.ip/ and are followed by a series of 10
	 * alphanumerical characters.
	 * 
	 * @param urlString the URL to be shortened
	 * 
	 * @return the shortened URL as a String
	 */

	private String shorten(String urlString) {

		// First check that it is a valid URL.
		if (validateUrl(urlString)) {

			// When an url is sent in, check if it is a key in the HashMap.
			String shortenedUrl;

			if (urlShortenersMap.containsKey(urlString)) {

				// Get the existing value
				shortenedUrl = urlShortenersMap.get(urlString);
				System.out.println("The shortened url is '" + shortenedUrl + "'\n");

			} else {

				// Generate a new short url with a random sequence
				String shortenedUrlSequence = RandomStringUtils.randomAlphanumeric(SEQUENCELENGHT);
				shortenedUrl = new String(SHORTURL + shortenedUrlSequence);

				// Make sure that it's an unique value
				while (urlShortenersMap.containsValue(shortenedUrl)) {

					shortenedUrlSequence = RandomStringUtils.randomAlphanumeric(SEQUENCELENGHT);
					shortenedUrl = new String(SHORTURL + shortenedUrlSequence);
				}

				// Put the url and shortenedUrl to the Map
				urlShortenersMap.put(urlString, shortenedUrl);
				System.out.println("The new shortened url is '" + shortenedUrl + "'\n");
				return shortenedUrl;
			}
		}

		return "";
	}

	private String expand(String shortenedUrl) throws Exception {

		/**
		 * Returns an original non-shortened URL given a short URL previously
		 * generated with the program Validates the format of the short URL. If
		 * the short URL provided has not been previously generated by the
		 * program, it throws an Exception.
		 */

		// Check correct format of shortenedUrl
		if (!validateShortUrl(shortenedUrl)) {

		} else {

			// Check the Map value to see if the URL specified is there
			if (urlShortenersMap.containsValue(shortenedUrl)) {

				Set<String> urls = new HashSet<String>();

				// Get all urls that match this shortenedUrl
				for (Entry<String, String> entry : urlShortenersMap.entrySet()) {
					if (Objects.equals(shortenedUrl, entry.getValue())) {
						urls.add(entry.getKey());
					}
				}

				// Programming error : Duplicate shortened urls
				if (urls.size() != 1) {
					throw new Exception("Duplicate shortened URLs found");
				}

				// return the only url that matches the value;
				Iterator<String> iter = urls.iterator();
				String originalUrl = iter.next().toString();
				System.out.println("The original URL is " + originalUrl);
				return originalUrl;
			} else {
				throw new Exception("No URL defined for short URL '" + shortenedUrl + "'");
			}
		}

		return "";

	}

	/**
	 * Validates the provided URL by checking its connection
	 * 
	 * @param urlString the URL to be validated
	 * 
	 * @return true if the URL is valid, otherwise false
	 */
	private boolean validateUrl(String urlString) {
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			System.err.println("Invalid URL. Should be in the form http://www.example.com \n");
			return false;
		} catch (IOException e) {
			System.err.println("Connection could not be established to the URL");
			return false;
		}
	}

	/**
	 * Validates the provided shortenedUrl by checking that it starts with a
	 * given String, and finishes with a fixed number of alphanumeric characters
	 * 
	 * @param shortenedUrl the url to be validated
	 * 
	 * @return true if the url provided meets the requirements, otherwise false
	 */
	private boolean validateShortUrl(String shortenedUrl) {

		boolean startsCorrectly = shortenedUrl.startsWith(SHORTURL);

		String sequence = shortenedUrl.replace(SHORTURL, "");

		// and ends with exactly 10 characters
		boolean isCorrectLength = (shortenedUrl.replace(SHORTURL, "")).length() == SEQUENCELENGHT;

		// That are alphanumeric
		boolean isAlphaNumeric = sequence.matches("[A-Za-z0-9]+");

		boolean isValidShortUrl = startsCorrectly && isCorrectLength && isAlphaNumeric;

		if (isValidShortUrl) {
			return true;
		} else {
			System.err.println("Invalid format for short URL. Should be ..." + SHORTURL
					+ " followed by 10 alphanumeric characters. Returning. \n");
			return false;
		}
	}

}
