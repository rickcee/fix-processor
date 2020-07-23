package net.rickcee.fix.initiator.client;

import java.io.InputStream;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.AllocAccount;
import quickfix.field.AllocQty;
import quickfix.field.AllocStatus;
import quickfix.field.AvgPx;
import quickfix.field.ConfirmID;
import quickfix.field.ConfirmStatus;
import quickfix.field.ConfirmTransType;
import quickfix.field.ConfirmType;
import quickfix.field.GrossTradeAmt;
import quickfix.field.NetMoney;
import quickfix.field.NoCapacities;
import quickfix.field.NoLegs;
import quickfix.field.NoUnderlyings;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TradeDate;
import quickfix.field.TransactTime;
import quickfix.fix44.AllocationInstruction;
import quickfix.fix44.AllocationInstructionAck;
import quickfix.fix44.Confirmation;
import quickfix.fix44.ConfirmationAck;

/**
 * @author rickcee
 *
 */
@Slf4j
@Component
public class RCNetFixClient extends quickfix.MessageCracker implements quickfix.Application {
	private SocketInitiator initiator;

	@PostConstruct
	public void init() {
		try {
			InputStream inputStream = RCNetFixClient.class.getClassLoader()
					.getResourceAsStream("fix-rcnet-client1.cfg");
			SessionSettings settings = new SessionSettings(inputStream);
			inputStream.close();

			MessageFactory messageFactory = new DefaultMessageFactory();
			MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
			LogFactory logFactory = new ScreenLogFactory();

			initiator = new SocketInitiator(this, messageStoreFactory, settings, logFactory, messageFactory);
			initiator.start();

			SessionID sessionId = initiator.getSessions().get(0);
			Session.lookupSession(sessionId).logon();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	@Override
	public void onCreate(SessionID sessionId) {
		log.info("--------- onCreate ---------");
	}

	@Override
	public void onLogon(SessionID sessionId) {
		log.info("--------- onLogon ---------");
	}

	@Override
	public void onLogout(SessionID sessionId) {
		log.info("--------- onLogout ---------");
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		log.info("--------- toAdmin ---------");
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		log.info("--------- fromAdmin ---------");
	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		log.info("--------- toApp ---------");
	}

	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		log.info(" - fromApp: [" + message.toRawString() + "]");
		try {
			crack(message, sessionId);
		} catch (Exception e) {
			log.error(" Error processing Msg: [" + message + "]: " + e.getMessage(), e);
		}

	}

	public void onMessage(AllocationInstruction ai, SessionID sessionId) throws FieldNotFound {
		// log.info("onMessage(" + ai + ", " + sessionId + ")");
		AllocationInstructionAck aiAck = new AllocationInstructionAck(ai.getAllocID(), new TransactTime(),
				new AllocStatus(AllocStatus.ACCEPTED));
		try {
			Session.lookupSession(sessionId).send(aiAck);
		} catch (Exception e) {
			log.error(" Error sending Msg: [" + aiAck + "]: " + e.getMessage(), e);
		}
	}

	public void onMessage(ConfirmationAck cAck, SessionID sessionId) throws FieldNotFound {
		// log.info("onMessage(" + cAck + ", " + sessionId + ")");
	}

	public void sendConfirmation() {
		Confirmation conf = new Confirmation();
		conf.set(new ConfirmID("CID-000001"));
		conf.set(new ConfirmTransType(ConfirmTransType.NEW));
		conf.set(new ConfirmStatus(ConfirmStatus.RECEIVED));
		conf.set(new TransactTime(LocalDateTime.now()));
		conf.set(new ConfirmType(ConfirmType.CONFIRMATION));
		conf.set(new TradeDate("20200723"));
		conf.set(new NoUnderlyings(0));
		conf.set(new NoLegs(0));
		conf.set(new AllocQty(150000));
		conf.set(new AllocAccount("ALLOC-ACCT-1"));
		conf.set(new Symbol("MSFT"));
		conf.set(new Side(Side.BUY));
		conf.set(new NoCapacities(0));
		conf.set(new AvgPx(99.863));
		conf.set(new GrossTradeAmt(149794.5));
		conf.set(new NetMoney(149794.5));

		SessionID session = null;
		for (SessionID sess : initiator.getSessions()) {
			if (sess.toString().equals("FIX.4.4:RCNET-CLIENT1->RCNET")) {
				session = sess;
				break;
			}
		}

		Session.lookupSession(session).send(conf);

	}

}
