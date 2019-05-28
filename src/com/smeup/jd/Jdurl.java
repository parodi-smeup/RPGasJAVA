package com.smeup.jd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.smeup.rpgparser.interpreter.Program;
import com.smeup.rpgparser.interpreter.ProgramParam;
import com.smeup.rpgparser.interpreter.SystemInterface;
import com.smeup.rpgparser.interpreter.Value;

public class Jdurl implements Program {

	private HttpRequestFactory requestFactory;
	private HttpRequest request;

	public static void main(String[] args) {
		Jdurl jdurl = new Jdurl();
		String rawResponse = jdurl.urlCall("https://jsonplaceholder.typicode.com/posts/42"); 
		System.out.println(rawResponse);
	}
	
	
	private String urlCall(final String url){
		requestFactory = new NetHttpTransport().createRequestFactory();
		String rawResponse = "";
		try {
			request = requestFactory.buildGetRequest(new GenericUrl(url));
			rawResponse = request.execute().parseAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rawResponse;
	}


	@Override
	public List<Value> execute(SystemInterface arg0, Map<String, ? extends Value> arg1) {
		System.out.println("RPG as a Java program");
		for (Map.Entry<String, ? extends Value> entry : arg1.entrySet()) {
			System.out.println("Name: " + entry.getKey() + " - Value: " + entry.getValue());
		}
		return new ArrayList<Value>();
	}


	@Override
	public List<ProgramParam> params() {
		// TODO Auto-generated method stub
		return null;
	}



}
