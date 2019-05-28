package com.smeup.jd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smeup.rpgparser.interpreter.Program;
import com.smeup.rpgparser.interpreter.ProgramParam;
import com.smeup.rpgparser.interpreter.SystemInterface;
import com.smeup.rpgparser.interpreter.Value;

public class Jdurl implements Program {

	public static void main(String[] args) {
		Jdurl jdurl = new Jdurl();
		String rawResponse = jdurl.urlCall("https://jsonplaceholder.typicode.com/posts/42");
		System.out.println(rawResponse);
	}

	public String urlCall(final String urlToCall) {
		URL url;
		String responseAsString = "";
		try {
			url = new URL(urlToCall);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			responseAsString = content.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseAsString;

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
