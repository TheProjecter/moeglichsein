package at.ac.univie.philo.mmr.server;

import javax.jdo.JDOHelper; 
import javax.jdo.PersistenceManagerFactory;

public class PersistenceMgrFactory {

	private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");      
	private PersistenceMgrFactory() {
		
	}      
	public static PersistenceManagerFactory get() {
		return pmfInstance;     
	}
}
