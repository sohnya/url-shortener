package com.sonja.url;

/**
 * Interface to deal with the data handling of UrlShorteners.
 * 
 */
public interface UrlShortenerData {
	
	/**
	 * Loads previously saved data about urls and their 
	 * corresponding shorteners. 
	 * */
	UrlShortener loadShorternerData() throws Exception;

	/**
	 * Saves the data collected by the program.
	 * */
	void saveShorternerData(UrlShortener urlShortener);

}