//package at.ac.univie.philo.mmr.server;
//
//import gwtupload.server.exceptions.UploadActionException; 
//import gwtupload.server.gae.AppEngineUploadAction; 
//
//
//import java.io.InputStream; 
//import java.util.List; 
//
//
//import javax.jdo.PersistenceManager; 
//import javax.jdo.Transaction; 
//import javax.servlet.http.HttpServletRequest; 
//
//import org.apache.commons.fileupload.FileItem; 
//import org.apache.commons.io.IOUtils; 
//
//
//import com.google.appengine.api.datastore.Blob; 
//
//
//public class DataStoreUploadAction extends AppEngineUploadAction { 
//
//	private static final long serialVersionUID = 1L;
//
//		@Override 
//        public String executeAction(HttpServletRequest request, 
//List<FileItem> sessionFiles) throws UploadActionException { 
//                String out = super.executeAction(request, sessionFiles); 
//
//
//                for(FileItem imgItem : sessionFiles) { 
//                        PersistenceManager pm = PersistenceMgrFactory.get().getPersistenceManager(); 
//                        Transaction tx = pm.currentTransaction(); 
//                        try { 
//                            // Start the transaction 
//                            tx.begin(); 
//
//
//                            InputStream imgStream = imgItem.getInputStream(); 
//                                Blob blob = new Blob(IOUtils.toByteArray(imgStream)); 
//                                ImageBlob imageBlob = new ImageBlob(imgItem.getName(), blob); 
//
//
//                            pm.makePersistent(imageBlob); 
//
//
//                            // Commit the transaction, flushing the object to the datastore 
//                            tx.commit(); 
//                        } 
//                        catch(Exception e) { 
//                                e.printStackTrace(); 
//                        } 
//                        finally { 
//                            if(tx.isActive()) { 
//                                tx.rollback(); 
//                            } 
//                            pm.close(); 
//                        } 
//                } 
//            return out; 
//        } 
//
//
//
//} 

