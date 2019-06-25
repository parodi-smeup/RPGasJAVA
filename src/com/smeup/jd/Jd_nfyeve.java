package com.smeup.jd;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.smeup.iotspi.jd003.VegaANPRDataDocumentEvent;
import com.smeup.iotspi.jd003.VegaANPRDataDocumentListenerInterface;
import com.smeup.iotspi.jd003.VegaANPRDocumentCreator;
import com.smeup.iotspi.jd003.VegaANPRLogEvent;
import com.smeup.iotspi.jd003.VegaANPRLogEventListenerInterface;
import com.smeup.rpgparser.interpreter.Program;
import com.smeup.rpgparser.interpreter.ProgramParam;
import com.smeup.rpgparser.interpreter.StringType;
import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.SystemInterface;
import com.smeup.rpgparser.interpreter.Value;

public class Jd_nfyeve implements Program, VegaANPRDataDocumentListenerInterface, VegaANPRLogEventListenerInterface{

	private List<ProgramParam> parms;

	public Jd_nfyeve() {
		parms = new ArrayList<ProgramParam>();
		// Sme.UP Function
		parms.add(new ProgramParam("§§FUNZ", new StringType(15)));
		// Sme.UP Method
		parms.add(new ProgramParam("§§METO", new StringType(30000)));
		// XML from camera
		parms.add(new ProgramParam("§§SVAR", new StringType(210000)));
	}

	private String notifyEvent(final String xml) {
		String responseAsString = "";
		VegaANPRDocumentCreator reader = new VegaANPRDocumentCreator(xml);
		reader.addLogEventListener(this);
		reader.addDataDocumentEventListener(this);
		reader.start();
		return responseAsString;
	}

	@Override
	public List<ProgramParam> params() {
		return parms;
	}

	@Override
	public List<Value> execute(SystemInterface arg0, LinkedHashMap<String, Value> arg1) {
		ArrayList<Value> arrayListResponse = new ArrayList<Value>();
		
		String response = "";
		
		for (Map.Entry<String, ? extends Value> entry : arg1.entrySet()) {
			
			String parmName = entry.getKey().toString();
			
			switch(parmName) {
			case "§§FUNZ":
				arrayListResponse.add(entry.getValue());
				break;
			case "§§METO":
				arrayListResponse.add(entry.getValue());
				break;
			case "§§SVAR":
				final String xml = entry.getValue().asString().getValue();
				response = notifyEvent(xml);
				arrayListResponse.add(new StringValue(response.trim()));
				break;
			}
		}
		return arrayListResponse;
	}

	@Override
	public void menageLogEvent(VegaANPRLogEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menageEventDataDocument(VegaANPRDataDocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
