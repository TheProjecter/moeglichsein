package at.ac.univie.philo.mmr.client;

import at.ac.univie.philo.mmr.shared.exceptions.ExpressionParsingException;
import at.ac.univie.philo.mmr.shared.expressions.Expression;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ModalParsingService extends RemoteService {
	Expression parse(String expression) throws ExpressionParsingException;
	Dummy dummy(Dummy d);
	Void sendReport(String category, String question, String contact) throws RuntimeException;
//	List<Image> retrieveImages();
}
