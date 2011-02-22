package at.ac.univie.philo.mmr.shared.expressions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Variable extends Symbol implements IsSerializable{

	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Variable() {
		super("dummyvar",1);
	}
	
	public Variable(String name, int index) {
		super(name, index);
		valid();
	}

	private void valid() {
		if(!name.matches("^(x|y|z)$")) {
			throw new IllegalArgumentException("Invalid Variablename.");
		}
	}
}
