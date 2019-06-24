package com.smeup.jd;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class Jd_rcvsck_test {

	private final String address = "localhost";
	private final String port = "8888";

	@Test
	public void test_001() throws IOException {

		// start to listen
		Jd_rcvsck_thread jd_rcvsck_thread = new Jd_rcvsck_thread();
		jd_rcvsck_thread.start();

		// write to socket
		final String expectedFromSocket = "Some data to socket to program Jd_rcvsck";
		writeToSocket(expectedFromSocket);

		File temp = File.createTempFile("Jd_rcvsck_thread", ".tmp");
        if (temp.exists() && temp.canWrite()) { 
        	
        }
		// read from temp file written by jd_rcvsck with socket content
		final String fromSocket = FileUtils.readFileToString(temp, "UTF-8");

		assertTrue(fromSocket.equals(expectedFromSocket));

	}

	private String writeToSocket(final String message) {
		try (Socket socket = new Socket(address, Integer.valueOf(port))) {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(message);
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
		return message;
	}

}
