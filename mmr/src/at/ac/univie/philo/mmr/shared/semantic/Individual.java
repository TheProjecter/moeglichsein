package at.ac.univie.philo.mmr.shared.semantic;

import com.google.gwt.user.client.rpc.IsSerializable;



public class Individual implements IsSerializable{

	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Individual() {
		this("fakeIndi",null);
	}
	
	/**
	 * Instantiates a new model element.
	 *
	 * @param name If we want to talk about things, 
	 * we have to give them a name. 
	 * This is the "real-life"-name of 
	 * the element, not the name in the model. 
	 * You will never find it in a formulae.
	 * @param imageUrl ... URL to the image
	 * @param symbol A shortcut for the name. 
	 */	
	public Individual(String name, String imageUrl) {
		setName(name);
		setImageUrl(imageUrl);
		valid();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	private String name;
	private String imageUrl;
//	private static final String DEFAULTICONPATH = System.getProperty("user.home")+"\test.jpg";

	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Individual) {
			Individual other = (Individual)o;
			return (name.equals(other.name) && imageUrl.equals(((Individual) o).imageUrl));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();	
	}
	
	protected void valid() {
		if (this.name == null) {
			throw new IllegalArgumentException("Name is null!");
		}
		if (this.imageUrl == null) {
				imageUrl = "http://de.kgbpeople.com/img/icon_android.png";
		}
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return  this.name;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Sets the icon.
	 *
	 * @param ico the new icon
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
