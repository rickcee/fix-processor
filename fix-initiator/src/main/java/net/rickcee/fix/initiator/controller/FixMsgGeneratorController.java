package net.rickcee.fix.initiator.controller;

import java.util.HashMap;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.fix.initiator.client.RCNetFixClient;
import net.rickcee.fix.initiator.util.FIX44;
import net.rickcee.fix.initiator.util.FIX50;
import net.rickcee.fix.initiator.util.Fix44Cracker;

@RestController
@Slf4j
public class FixMsgGeneratorController {
	@Autowired
	private FIX44 fix44;
	@Autowired
	private Fix44Cracker f44cracker;
	@Autowired
	private FIX50 fix50;
	@Autowired
	private Environment env;

	@Autowired
	private RCNetFixClient fixClient;
	
	@RequestMapping(method = RequestMethod.GET, path = "/HealthCheck", produces = { "application/json" })
	public Object healthCheck() {
		HashMap<String, String> result = new HashMap<>();
		result.put("result", "OK");
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/secured/service", produces = { "application/json" })
	public Object service1() {
		HashMap<String, String> result = new HashMap<>();
		result.put("secured", "true");
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/public/defaults", produces = { "application/json" })
	public Object servicePublic1() {
		HashMap<String, String> result = new HashMap<>();
		//result.put("senderCompId", env.getProperty("fix.defaultSenderCompId"));
		//result.put("targetCompId", env.getProperty("fix.defaultTargetCompId"));
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/support/fix/confirm/send", produces = { MediaType.APPLICATION_JSON })
	public Object sendConfirm() {
		HashMap<String, String> result = new HashMap<>();
		try {
			fixClient.sendConfirmation();
			result.put("result", "OK");
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("error", e.getMessage());
			log.error(e.getMessage(), e);
		}
		return result;
	}
	
//	@RequestMapping(method = RequestMethod.GET, path = "/public/fix/sessions", produces = { "application/json" })
//	public Object activeFixSessions() {
//		List<HashMap<String, Object>> sessions = new ArrayList<>();
//
//		threadedSocketAcceptor.getSessions().forEach(sessionObj -> {
//			HashMap<String, Object> session = new HashMap<>();
//			session.put("name", sessionObj.toString());
//			session.put("value", sessionObj);
//			sessions.add(session);
//		});
//
//		return sessions;
//	}
	
}