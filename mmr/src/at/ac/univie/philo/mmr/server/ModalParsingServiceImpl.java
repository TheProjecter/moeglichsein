package at.ac.univie.philo.mmr.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import at.ac.univie.philo.mmr.client.Dummy;
import at.ac.univie.philo.mmr.client.ModalParsingService;
import at.ac.univie.philo.mmr.server.parsetree.ModallogicParser;
import at.ac.univie.philo.mmr.shared.FieldVerifier;
import at.ac.univie.philo.mmr.shared.exceptions.ExpressionParsingException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

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
