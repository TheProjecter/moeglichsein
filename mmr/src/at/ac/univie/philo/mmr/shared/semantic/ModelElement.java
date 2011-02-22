package at.ac.univie.philo.mmr.shared.semantic;

import java.io.File;

import com.google.gwt.user.client.ui.Image;

import at.ac.univie.philo.mmr.shared.exceptions.NotAnIconException;

/**
 * The abstract class ModelElement encapsulates common fields of a REAL Modal-Logic Element.
 * REAL means, that every instantiation is thought as being an element in the (possible) world and not only a symbolic representation of it.
 *  
 */
public abstract class ModelElement {

	protected String name;
	private Image icon;
//	private static final String DEFAULTICONPATH = System.getProperty("user.home")+"\test.jpg";
	
	/**
	 * Instantiates a new model element.
	 *
	 * @param name If we want to talk about things, 
	 * we have to give them a name. 
	 * This is the "real-life"-name of 
	 * the element, not the name in the model. 
	 * You will never find it in a formulae.
	 * @param icon An icon visualizes the element. Every formulae will finally talk about icons, which are thought to capture the properties and entities of possible worlds.
	 * @param symbol A shortcut for the name. 
	 */	
	public ModelElement(String name, Image icon) {
		setName(name);
		setIcon(icon);
		valid();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ModelElement) {
			ModelElement other = (ModelElement)o;
			return (name.equals(other.name));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();	
	}
	
	private void valid() {
		if (this.name == null) {
			throw new IllegalArgumentException("Name is null!");
		}
		if (this.icon == null) {
				icon = new Image("http://de.kgbpeople.com/img/icon_android.png");
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
	public Image getIcon() {
		return this.icon;
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
	public void setIcon(Image ico) {
		this.icon = ico;
	}
	
//	/**
//	 * Gets the symbol.
//	 *
//	 * @return symbol
//	 */
//	
//	public String getSymbol(){
//		return this.symbol;
//	}
//	
//	/**
//	 * Sets the symbol.
//	 *
//	 * @param symbol the new symbol
//	 */
//	public void setSymbol(String symbol) {
//		this.symbol = symbol;
//	}

	
}
