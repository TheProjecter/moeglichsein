package at.ac.univie.philo.mmr.shared.semantic;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Image;

public class Individual extends ModelElement implements IsSerializable {

	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Individual() {
		super("name", null);
	}
	public Individual(String name, Image icon) {
		super(name, icon);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
