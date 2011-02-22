package at.ac.univie.philo.mmr.client;

import at.ac.univie.philo.mmr.shared.expressions.Expression;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ModalParsingServiceAsync {
	void parse(String input, AsyncCallback<Expression> callback);
	void dummy(Dummy d, AsyncCallback< Dummy> callback);
}
