package at.ac.univie.philo.mmr.client.gui;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;

public interface Ressources extends ClientBundle {

	  @Source("world.png")
	  ImageResource world();
	  
	  @Source("individual.jpg")
	  ImageResource individual();
	  
	  @Source("logo.jpg")
	  ImageResource logo();
}
