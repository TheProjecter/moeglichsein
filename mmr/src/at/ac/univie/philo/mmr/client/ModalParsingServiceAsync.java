package at.ac.univie.philo.mmr.client;

import java.util.List;

import at.ac.univie.philo.mmr.shared.evaluation.EvaluationReport;
import at.ac.univie.philo.mmr.shared.expressions.Expression;
import at.ac.univie.philo.mmr.shared.semantic.Universe;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ModalParsingServiceAsync {
	void parse(String input, Universe universe, AsyncCallback<EvaluationReport> callback);
	void dummy(Dummy d, AsyncCallback< Dummy> callback);
	void sendReport(String category, String question, String contact, AsyncCallback<Void> callback);
//	void retrieveImages(AsyncCallback<List<Image>> callback);
}
