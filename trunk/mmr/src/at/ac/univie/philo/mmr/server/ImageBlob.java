package at.ac.univie.philo.mmr.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ImageBlob {

	@PrimaryKey     
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;  
	
	@Persistent
	private String name;
	@Persistent
	private Blob blob;
	
    
	
	public ImageBlob(String name, Blob blob) {
		this.name = name;
		this.blob = blob;
	}



	public Key getKey() {
		return key;
	}



	public void setKey(Key key) {
		this.key = key;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Blob getBlob() {
		return blob;
	}



	public void setBlob(Blob blob) {
		this.blob = blob;
	}
}
