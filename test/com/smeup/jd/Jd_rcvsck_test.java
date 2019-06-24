package com.smeup.jd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.Value;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;

public class Jd_rcvsck_test extends Thread {

	private final String address = "localhost";
	private final String port = "8888";
	private ByteArrayOutputStream byteArrayOutputStream;
	private PrintStream printStream;
	private JavaSystemInterface javaSystemInterface;
	private Jd_rcvsck JdProgram;
	private Map<String, Value> jdProgramRequestParms;
	private List<Value> jdProgramResponseParms;

	@Test
	public void test_001() throws IOException, InterruptedException {

		Thread jd_rcvsck_test = new Jd_rcvsck_test();
		jd_rcvsck_test.start();

		Thread.sleep(3000);
		
		final String expectedFromSocket = "some socket data";
		writeToSocket(expectedFromSocket);
		System.out.println("Done");

	}

	@Override
	public void run() {

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		javaSystemInterface = new JavaSystemInterface(printStream);
		JdProgram = new Jd_rcvsck();

		jdProgramRequestParms = new LinkedHashMap<>();
		jdProgramRequestParms.put("ADDRSK", new StringValue(port));
		jdProgramRequestParms.put("BUFFER", new StringValue(""));
		jdProgramRequestParms.put("BUFLEN", new StringValue("100"));
		jdProgramRequestParms.put("IERROR", new StringValue(""));

		jdProgramResponseParms = JdProgram.execute(javaSystemInterface, jdProgramRequestParms);
		
		String contentFromSocket = "";
//		for (Value value : jdProgramResponseParms) {
//			System.out.println("Values:");
//			System.out.println(value);
//			if ("BUFFER".equals(value.asString().getValue())) {
//				contentFromSocket = value.asString().getValue();
//				break;
//			}
//		}

		System.out.println("Content readed from socket: " + contentFromSocket);
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
