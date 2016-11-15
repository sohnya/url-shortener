package com.sonja.url.test;

import java.util.Map;

import org.junit.Test;

import com.sonja.url.Action;
import com.sonja.url.UrlShortener;

public class UrlShortenerTest{

	@Test
	public void ShorteningAnUrlUpdatesUrlMapWithSentUrl() throws Exception  {
		UrlShortener urlShortener = new UrlShortener();
		
		String url = "http://www.google.com";
		
		urlShortener.doAction(Action.valueOf("shorten"),url);
		
		Map<String, String> shortenersMap = urlShortener.getUrlShortenersMap();
		
		assert(shortenersMap.containsKey(url));
	}

	@Test
	public void ShorteningAnInvalidUrlIsNotAddedToMap() throws Exception {
		
		UrlShortener urlShortener = new UrlShortener();
		
		String url = "www.google.com";
		
		urlShortener.doAction(Action.valueOf("shorten"),url);
		
		Map<String, String> shortenersMap = urlShortener.getUrlShortenersMap();

		for(String key : shortenersMap.keySet()){
			System.out.println(key);
		}
		assert(!shortenersMap.containsKey(url));
		
	}

	@Test
	public void ShorteningTheSameUrlTwiceGivesTheSameShortUrl() throws Exception {

		UrlShortener urlShortener = new UrlShortener();
		
		String url = "http://www.google.com";
		
		urlShortener.doAction(Action.valueOf("shorten"),url);
		
		Map<String, String> shortenersMap = urlShortener.getUrlShortenersMap();
		String firstShortUrl = shortenersMap.get(url);

		urlShortener.doAction(Action.valueOf("shorten"),url);
		Map<String, String> shortenersMapTwo = urlShortener.getUrlShortenersMap();
		String secondShortUrl = shortenersMapTwo.get(url);
		
		assert(secondShortUrl.equals(firstShortUrl));
		
	}

	@Test
	public void DoingShortenAndExpandReturnsTheOriginalUrl() throws Exception {
		
		UrlShortener urlShortener = new UrlShortener();
		
		String url = "http://www.google.com";
		
		urlShortener.doAction(Action.valueOf("shorten"),url);
		
		Map<String, String> shortenersMap = urlShortener.getUrlShortenersMap();
		String shortUrl = shortenersMap.get(url);

		String expandedUrl = urlShortener.doAction(Action.valueOf("expand"),shortUrl);
		
		assert(expandedUrl.equals(url));
		
	}

	@Test (expected = Exception.class)
	public void ExpandingANonExistingShortUrlGivesError() throws Exception {
		
		UrlShortener urlShortener = new UrlShortener();
		
		String nonExistantShortUrl = "http://cl.ip/2khJDXjTac";
		
		urlShortener.doAction(Action.valueOf("expand"),nonExistantShortUrl);
	}
	
}