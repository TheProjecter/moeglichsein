package at.ac.univie.philo.mmr.shared.expressions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Symbol implements IsSerializable {

	protected String name;
	protected int index;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Symbol() {
		this.name="dummysymbol";
		this.index=1;
	}
	
	public Symbol(String name, int index) {
		this.name = name;
		this.index = index;
		valid();
	}

	private void valid() {
		if (index <0) {
			throw new IllegalArgumentException("Index has to be >= 0");
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String toString() {
		return index == 0 ? name : name+"_"+index;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Symbol) {
			Symbol otherConst = (Symbol) o;
			return this.name.equals(otherConst.name) && this.index == otherConst.index;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + index;
	}
	

}
