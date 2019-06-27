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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.Value;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;

public class Jd_rcvsck_test {

	private final String address = "localhost";
	private final String port = "8888";
	private ByteArrayOutputStream byteArrayOutputStream;
	private PrintStream printStream;
	private JavaSystemInterface javaSystemInterface;
	private JD_RCVSCK JdProgram;
	private LinkedHashMap<String, Value> jdProgramRequestParms;
	private List<Value> jdProgramResponseParms;
	private String dataToSocket = "SOME DATA ON SOCKET";

	@Test
	public void test_001() throws InterruptedException, ExecutionException {
		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() {

				String readFromSocket = "";
				byteArrayOutputStream = new ByteArrayOutputStream();
				printStream = new PrintStream(byteArrayOutputStream);
				javaSystemInterface = new JavaSystemInterface(printStream);
				JdProgram = new JD_RCVSCK();

				jdProgramRequestParms = new LinkedHashMap<>();
				
				jdProgramRequestParms.put("ADDRSK", new StringValue(port));
				jdProgramRequestParms.put("BUFFER", new StringValue(""));
				jdProgramRequestParms.put("BUFLEN", new StringValue("100"));
				jdProgramRequestParms.put("IERROR",  new StringValue(""));		
				
				jdProgramResponseParms = JdProgram.execute(javaSystemInterface, jdProgramRequestParms);

				readFromSocket = jdProgramResponseParms.get(1).asString().getValue();
				System.out.println("Content readed from socket: " + readFromSocket);

				return readFromSocket;
			}
		};

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> future = executor.submit(callable);

		Thread.sleep(3000);
		
		writeToSocket(dataToSocket);

		assertEquals(dataToSocket, future.get());
		executor.shutdown();
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
