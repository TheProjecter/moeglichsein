package at.ac.univie.philo.mmr.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import at.ac.univie.philo.mmr.client.Dummy;
import at.ac.univie.philo.mmr.client.ModalParsingService;
import at.ac.univie.philo.mmr.server.parsetree.ModallogicParser;
import at.ac.univie.philo.mmr.shared.exceptions.ExpressionParsingException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ModalParsingServiceImpl extends RemoteServiceServlet implements
		ModalParsingService {

	public Expression parse(String input) throws ExpressionParsingException {
		// Verify that the input is valid. 
//		if (!FieldVerifier.isValidName(input)) {
//			// If the input is not valid, throw an IllegalArgumentException back to
//			// the client.
//			throw new IllegalArgumentException(
//					"Name must be at least 4 characters long");
//		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		ModallogicParser parser = new ModallogicParser();
		Expression exp = parser.parse(input);
		return exp;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public Dummy dummy(Dummy d) {
		return d;
	}

	@Override
	public Void sendReport(String category, String question, String contact) throws RuntimeException {
		        Properties props = new Properties();
		        Session session = Session.getDefaultInstance(props, null);

		        String msgBody = question;
		            Message msg = new MimeMessage(session);
		            try {
						msg.setFrom(new InternetAddress("akalypse@gmail.com", "Moeglichsein Feedback"));
						msg.addRecipient(Message.RecipientType.TO,
		                             new InternetAddress("akalypse+moeglichsein@gmail.com", "AKAlypse"));
						msg.setSubject("Moeglichsein: Feedback ("+category+")");
						msg.setText(msgBody+"\n\n ---- \nsent by: "+contact);
						Transport.send(msg);
						return null;
		            } catch (MessagingException e) {
						throw new RuntimeException(e);
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
		



	}

//	@Override
//	public List<Image> retrieveImages() {
//		PersistenceManager pm = PersistenceMgrFactory.get().getPersistenceManager();     
//		String query = "select from " + ImageBlob.class.getName();     
//		List<ImageBlob> imageblobs = (List<ImageBlob>) pm.newQuery(query).execute();
//		List<Image> images = new ArrayList<Image>();
//		for (ImageBlob ib : imageblobs) {
//			Image i = new Image
//		}
//	}
}
