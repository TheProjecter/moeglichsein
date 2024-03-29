package at.ac.univie.philo.mmr.server;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import at.ac.univie.philo.mmr.client.Dummy;
import at.ac.univie.philo.mmr.client.ModalParsingService;
import at.ac.univie.philo.mmr.server.parsetree.ModallogicParser;
import at.ac.univie.philo.mmr.shared.evaluation.EvaluationReport;
import at.ac.univie.philo.mmr.shared.exceptions.ExpressionParsingException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.expressions.QuantorExpression;
import at.ac.univie.philo.mmr.shared.expressions.Variable;
import at.ac.univie.philo.mmr.shared.expressions.VariableExpression;
import at.ac.univie.philo.mmr.shared.operators.Operators;
import at.ac.univie.philo.mmr.shared.semantic.Universe;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ModalParsingServiceImpl extends RemoteServiceServlet implements
		ModalParsingService {

	public EvaluationReport parse(String input, Universe universe) throws ExpressionParsingException {
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
		Expression exp;
		EvaluationReport report;
		try {
			exp = parser.parse(input);

			Collection<VariableExpression> freeVars = exp.freeVariables();
			//for each free Variable add a universal quantifier with a prefix
			for (VariableExpression v :  freeVars) {
				Variable var = (Variable) v.getSymbol();
				exp = new QuantorExpression(Operators.getAllQuantorOperator(), var , exp);
			}
			report = new EvaluationReport(exp, universe);
		} catch(Exception e) {
			throw new ExpressionParsingException(e);
		}
		return report;
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

	public Dummy dummy(Dummy d) {
		return d;
	}

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
