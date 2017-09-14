package com.wday.search.api.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
// Dummy class
public class App {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String encodedUserCredentials = new String(Base64.encodeBase64(String.format("%s:%s", URLEncoder.encode("EWZaZdKxTKCm79pBmhP9acpfKDkZDoakUFsRksT","UTF-8"),URLEncoder.encode("lblM8oQGYDA8ecHbsY8vhodtBxcwBbIFMHO3zRJagqsBg","UTF-8")).getBytes()));
		System.out.println(encodedUserCredentials);
		
		// AAAAAAAAAAAAAAAAAAAAAPsG2QAAAAAASpupJSX4kBQHL0c1xxydr2aDCcI%3D06zNsVzup40gNTS1hjtvJ5eUaampVr0jEzAJ5qepj1ShrQ11YA
	}

}
