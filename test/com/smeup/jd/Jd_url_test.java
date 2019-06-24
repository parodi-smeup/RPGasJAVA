package com.smeup.jd;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.Value;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;

public class Jd_url_test {

	private ByteArrayOutputStream byteArrayOutputStream;
	private PrintStream printStream;
	private JavaSystemInterface javaSystemInterface;
	private Jd_url JdProgram;
	private Map<String, Value> JdProgramRequestParms;
	private List<Value> JdProgramResponseParms;

	@Test
	public void test_001() {
		// This test is the proof of it works correctly
		final String EXPECTED_RESPONSE = "{\"hello\": \"world\"}";
		JdProgramRequestParms.put("U$FUNZ", new StringValue("URL"));
		JdProgramRequestParms.put("U$METO", new StringValue("HTTP"));
		JdProgramRequestParms.put("U$SVARSK", new StringValue("http://www.mocky.io/v2/5185415ba171ea3a00704eed"));

		List<Value> responseParms = JdProgram.execute(javaSystemInterface, JdProgramRequestParms);
		assertTrue(responseParms.get(2).asString().getValue().equals(EXPECTED_RESPONSE));
	}

	@Test
	public void test_002() {
		// This test is the proof of an expected error response
		JdProgramRequestParms.put("U$FUNZ", new StringValue("URL"));
		JdProgramRequestParms.put("U$METO", new StringValue("HTTP"));
		JdProgramRequestParms.put("U$SVARSK", new StringValue("malformed_url"));

		JdProgramResponseParms = JdProgram.execute(javaSystemInterface, JdProgramRequestParms);
		assertTrue(JdProgramResponseParms.get(2).asString().getValue().startsWith("*ERROR"));
		assertTrue(JdProgramResponseParms.get(2).asString().getValue().contains("malformed_url"));
	}

	@Test
	public void test_003() {
		// This test is the proof of an expected error response
		JdProgramRequestParms.put("U$FUNZ", new StringValue("URL"));
		JdProgramRequestParms.put("U$METO", new StringValue("HTTP"));
		JdProgramRequestParms.put("U$SVARSK", new StringValue("http://www.mocky.io/v2/5185415ba171ea3a00704eedxxx"));

		JdProgramResponseParms = JdProgram.execute(javaSystemInterface, JdProgramRequestParms);
		assertTrue(JdProgramResponseParms.get(2).asString().getValue().startsWith("*ERROR"));
		assertTrue(JdProgramResponseParms.get(2).asString().getValue().contains("HTTP response code: 500"));
	}

	@Before
	public void init() {
		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		javaSystemInterface = new JavaSystemInterface(printStream);
		JdProgram = new Jd_url();
		JdProgramRequestParms = new HashMap<>();
	}

}