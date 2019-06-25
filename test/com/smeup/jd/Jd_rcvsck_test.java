package com.smeup.jd;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;

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
	private LinkedHashMap<String, Value> jdProgramRequestParms;
	private List<Value> jdProgramResponseParms;
	private final String writtenToSocket = "SOME DATA ON SOCKET";
	private String readFromSocket = "";

	@Test
	public void test_001() throws IOException, InterruptedException {

		Thread jd_rcvsck_test = new Jd_rcvsck_test();
		jd_rcvsck_test.start();

		Thread.sleep(3000);

		writeToSocket(writtenToSocket);

		assertEquals(readFromSocket, writtenToSocket);
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

		// TODO get ONLY BUFFER parm, NOT ALL parms
		readFromSocket = "\n parm0=" + jdProgramResponseParms.get(0).asString().getValue() +
				"\n parm1=" + jdProgramResponseParms.get(1).asString().getValue() +
				"\n parm2=" +jdProgramResponseParms.get(2).asString().getValue() +
				"\n parm3=" +jdProgramResponseParms.get(3).asString().getValue();

		System.out.println("Content readed from socket: " + readFromSocket);
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
