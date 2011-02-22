package at.ac.univie.philo.mmr.shared.expressions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Constant extends Symbol implements IsSerializable {

	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Constant() {
		super("dummyconst",1);
	}
	
	public Constant(String name, int index) {
		super(name, index);
		valid();
	}

	private void valid() {
		if (!name.equals("a")) {
			throw new IllegalArgumentException("Invalid Constantname.");
		}
	}
}
