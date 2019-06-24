package com.smeup.jd;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.Value;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;

public class Jd_rcvsck_thread extends Thread {

	private ByteArrayOutputStream byteArrayOutputStream;
	private PrintStream printStream;
	private JavaSystemInterface javaSystemInterface;
	private Jd_rcvsck JdProgram;
	private Map<String, Value> JdProgramRequestParms;
	private List<Value> JdProgramResponseParms;
	private final String port = "8888";

	@Override
	public void run() {

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		javaSystemInterface = new JavaSystemInterface(printStream);
		JdProgram = new Jd_rcvsck();
		JdProgramRequestParms = new HashMap<>();

		final String expectedFromSocket = "some socket data";
		JdProgramRequestParms.put("ADDRSK", new StringValue(port));
		JdProgramRequestParms.put("BUFFER", new StringValue(expectedFromSocket));
		JdProgramRequestParms.put("BUFLEN", new StringValue("100"));
		JdProgramRequestParms.put("IERROR", new StringValue(""));

		JdProgramResponseParms = JdProgram.execute(javaSystemInterface, JdProgramRequestParms);
		String contentFromSocket = JdProgramResponseParms.get(1).asString().getValue();
		System.out.println("Content readed from socket: " + contentFromSocket);
	}

}
