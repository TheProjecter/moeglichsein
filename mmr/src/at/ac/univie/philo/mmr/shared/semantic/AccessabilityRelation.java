package at.ac.univie.philo.mmr.shared.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import at.ac.univie.philo.mmr.shared.exceptions.ConstraintViolationException;


/**
 * The Class AccessabilityRelation uses a Graph implementation to represent the accessability relation between possible worlds.
 */
public class AccessabilityRelation {

	/** The graph. A world represents a node. */
	private HashMap<World, HashSet<World>> accessMap;
	private HashSet<World> worlds;
	
	/** The constraint is a guarantee, that certain properties hold for a universe of possible worlds. For example, if the constraint is set to S5, you can be sure, that every world can see every other world. The class guarantees that no method changes  */
	private AccessabilityConstraint constraint;
	
	/**
	 * Instantiates a new and empty accessability relation.
	 */
	public AccessabilityRelation() {
		accessMap = new HashMap<World, HashSet<World>>();
		worlds = new HashSet<World>();
		this.constraint = AccessabilityConstraint.NONE;
	}
	
	/**
	 * Instantiates a new accessability relation with a given Graph.
	 *
	 * @param g the graph whose nodes are worlds and represent the accessability relation from every world to another.
	 */
	public AccessabilityRelation (HashMap<World, HashSet<World>> g) {
		this.accessMap = g;
		this.worlds = new HashSet<World>();
		this.worlds.addAll(extractContainingWorlds(g));
		this.constraint = AccessabilityConstraint.NONE;
	}

	private Collection<World> extractContainingWorlds(
			HashMap<World, HashSet<World>> g) {
		
		HashSet<World> hsW = new HashSet<World>();
		if (g != null) {
			hsW.addAll(g.keySet());
			for (HashSet<World> onehsW : g.values()) {
				hsW.addAll(onehsW);
			}
		}
		return hsW;
	}

	//Achtung, der übergebene Graph wird nicht entsprechend des angegebenen Constraints angepasst!
	//Wenn eine Exception geworfen wird, muss man mit enforceConstraint nachhelfen und den übergebenen Graph nachbessern.
	/**
	 * Instantiates a new accessability relation. The best way to deal with {@link ConstraintViolationException} is to use {@link #enforceConstraint(AccessabilityConstraint)}, which modifies the graph until it fits the constraints.
	 *
	 * @param g the graph whose nodes are worlds and represent the accessability relation from every world to another.
	 * @param c the {@link AccessabilityConstraint}, which determines, if special properties should hold. In case of doubt, use {@link AccessabilityConstraint.NONE}.
	 * @throws ConstraintViolationException iff the constraints do not fit the given Constraints c.
	 */
	public AccessabilityRelation (HashMap<World, HashSet<World>> g, AccessabilityConstraint c) throws ConstraintViolationException {
		this(g);
		changeConstraint(c);
	}
	
	/**
	 * Gets the {@link AccessabilityConstraint}.
	 *
	 * @return the constraint
	 */
	public AccessabilityConstraint getConstraint() {
		return this.constraint;
	}
	
	
	public Set<World> getWorlds() {
		return this.worlds;
	}
	
	/**
	 * Change {@link AccessabilityConstraint} for the universe. The best way to deal with {@link ConstraintViolationException} is to use {@link #enforceConstraint(AccessabilityConstraint)}, which modifies the graph until it fits the constraints.
	 *
	 * @param c the {@link AccessabilityConstraint} which should apply for the Accessability-Relation.
	 * @throws ConstraintViolationException iff the constraints do not fit the given Constraints c.
	 */
	public void changeConstraint(AccessabilityConstraint c) throws ConstraintViolationException {
		if (!validateConstraint(c)) throw new ConstraintViolationException();
		else this.constraint = c;
	}
	
	public boolean isAccessible(World fromWorld, World toWorld) {
		if (fromWorld != null && toWorld != null) {
			if (worlds.contains(fromWorld) && worlds.contains(toWorld)) {
				HashSet<World> accessList = accessMap.get(fromWorld);
				return accessList.contains(toWorld);
			}
		}
		return false;
	}
	
	
	/**
	 * You can use this method before changing the {@link AccessabilityConstraint} of the {@link Universe} with {@link #changeConstraint(AccessabilityConstraint)} and if you want to avoid {@link ConstraintViolationException}.
	 * It answers the question, whether the given Constraint is applyable to the current graph without any changes.
	 * 
	 * @param c the {@link AccessabilityConstraint}.
	 * @return true, iff the current graph fits the given AccessabilityConstraint. You can use {@link #changeConstraint(AccessabilityConstraint)} without any exception and without the need to call {@link #enforceConstraint(AccessabilityConstraint)}. False, otherwise.
	 */
	boolean validateConstraint(AccessabilityConstraint c) {
	
		switch(c) {
	
		case NONE:
			return true;				
		
		case REFLEXIVE:
			for (World w: worlds) {
				if (!isAccessible(w, w)) return false;
			};
			return true;
		case S5:
			for (World w: worlds) {
				HashSet<World> wList = accessMap.get(w);
				if (!wList.containsAll(worlds)) return false;
			}
			return true;
		
		default: 
			return false;	
		}
	}
	
	 /**
	 * Validate the current constraint. Only for internal use. It checks, if the graph is corrupted by changes, which were made.
	 *
	 * @return true, iff the constraints are compatible with the current graph. False, otherwise.
	 */
	 private boolean validateConstraint() {
		 return validateConstraint(this.constraint);
	 }
	
		/**
		 * Adds a new access between two {@link World}s. Adding a new edge will not modify constraint-compability.
		 * If the worlds are not yet represented in the graph, they will be added. 
		 * 
		 * @param fromWorld the {@link World}, FROM where a new access will be added.
		 * @param toWorlds the {@link World}s, TO whom a new access will be added.
		 */
	 public void addAccess(World fromWorld, World... toWorlds) {
		 if (fromWorld != null && toWorlds != null) {
			HashSet<World> fromWorldList = accessMap.get(fromWorld);
			if (fromWorldList == null) {
				fromWorldList = new HashSet<World>();
			}
			for (World worldToAdd : toWorlds) {
				fromWorldList.add(worldToAdd);
				worlds.add(worldToAdd);
			}
			worlds.add(fromWorld);
			accessMap.put(fromWorld, fromWorldList);

		 } else {
			 throw new IllegalArgumentException("One of the Worlds was null.");
		 }
	 }
	 
		/**
		 * Overrides the access of a {@link World}.
		 * If the worlds are not yet represented in the graph, they will be added. 
		 * 
		 * @param fromWorld the {@link World}, FROM where a new access will be added.
		 * @param toWorlds the {@link World}s, TO whom a new access will be added.
		 * @param force sets if it should also update the AccessRelation Constraints
		 */
	 public AccessabilityConstraint overrideAccess(World fromWorld, HashSet<World> toWorlds, boolean force) {
		 return updateExistingWorldsAndDetectConstraint(fromWorld, toWorlds, force);
	 }
	
	private AccessabilityConstraint updateExistingWorldsAndDetectConstraint(
				World fromWorld, HashSet<World> toWorlds, boolean force) {

		 	HashSet<World> oldAccessOfReferenceWorld = accessMap.get(fromWorld);
		 	accessMap.put(fromWorld, toWorlds);
		
			//start with the current constraint as maxConstraint-Level
			AccessabilityConstraint retrievedConstraint = this.constraint;
		
			//retrieve all Worlds
			HashSet<World> newWorlds = new HashSet<World>();
			for(HashSet<World> hsw : accessMap.values()) {
				newWorlds.addAll(hsw);
			}
			newWorlds.addAll(accessMap.keySet());
			
			//Every entry should be 
			for(Entry<World,HashSet<World>> entry : accessMap.entrySet()) {
				AccessabilityConstraint newConstraint = determineMaxConstraint(entry.getKey(), entry.getValue(), newWorlds);
				retrievedConstraint = minConstraint(newConstraint,retrievedConstraint);
			}
			
			if (force || retrievedConstraint.equals(this.constraint)) {
				//apply changes
				this.worlds = newWorlds;
				this.constraint = retrievedConstraint;
			} else {
				//reverse changes
				accessMap.put(fromWorld, oldAccessOfReferenceWorld);
			}
			
			return retrievedConstraint;
		}
	
	/**
	 * 
	 * @param newConstraint
	 * @param oldConstraint
	 * @return the maximum possible constraint (in fact that means we have to chose the actual constraint is the minimal constraint
	 */
	private AccessabilityConstraint minConstraint(
			AccessabilityConstraint newConstraint, AccessabilityConstraint oldConstraint) {
		if (newConstraint.compareTo(oldConstraint) <= 0) {
			return newConstraint;
		}
		return oldConstraint;
		
	}

	public AccessabilityConstraint determineMaxConstraint(World fromWorld, HashSet<World> toWorlds, HashSet<World> allWorlds) {
		if(toWorlds.containsAll(allWorlds)) return AccessabilityConstraint.S5;
		if(toWorlds.contains(fromWorld)) return AccessabilityConstraint.REFLEXIVE;
		return AccessabilityConstraint.NONE;
	}
	

	/**
	 * Enforce constraint. Modifies the graph in order to fulfill the given {@link AccessabilityConstraint}. It won't remove edges between worlds, but it may add them.
	 *
	 * @param c the {@link AccessabilityConstraint}.
	 * @return true, iff the Constraint is known and was enforced successfully. False, otherwise.
	 */
	public boolean enforceConstraint(AccessabilityConstraint c) {
		boolean ret = false;
		switch(c) {
	
		case NONE:
			ret = true;
		case REFLEXIVE:
			for (World w: worlds) {
				addAccess(w, w);
			};
			ret= true;
			break;
		case S5:
			for (World w: worlds) {
				addAccess(w, worlds.toArray(new World[worlds.size()]));
			}
			ret = true;
			break;
		default: 
			ret= false;	
		}
		
		try {
			changeConstraint(c);
		} catch (ConstraintViolationException e) {
			// this should not happen
			e.printStackTrace();
			ret = false;
		}
		return ret;	
	}
	
	/**
	 * Adds a new {@link World} to the accessability graph. The according edges will be created corresponding to the current {@link AccessabilityConstraint}.
	 *
	 * @param newWorld {@link World}, which will become a new node in the AccessabilityRelation.
	 */
	public void addWorld(World newWorld) {
		if (newWorld != null) {
			HashSet<World> emptyAccessList = accessMap.get(newWorld);
			if (emptyAccessList == null) {
				emptyAccessList = new HashSet<World>();
				accessMap.put(newWorld, emptyAccessList);
				worlds.add(newWorld);
			}
			if (!validateConstraint()) enforceConstraint(this.constraint);
		} else {
			throw new IllegalArgumentException("World is null.");
		}
	}
	
	/**
	 * Removes the access from one {@link World} to the other.
	 *
	 * @param fromWorld the world, whose access to the other world should be removed.
	 * @param toWorld the world, TO which an access should be removed. 
	 * @throws ConstraintViolationException iff constraints are violated by this change. So the change is reversed and the accessabilty graph does not change. If you still want to do this change, you have to lover the constraints with {@link #changeConstraint(AccessabilityConstraint)}.
	 */
//	void removeAccess(World fromWorld, World toWorld) throws ConstraintViolationException {
//		if (fromWorld != null && toWorld != null) {
//			graph.removeEdge(fromWorld, toWorld);
//			if (!this.validateConstraint()) {
//				//reverse the change by re-adding
//				this.addAccess(fromWorld, toWorld);
//				throw new ConstraintViolationException();
//			}			
//		} else {
//			throw new IllegalArgumentException("From or ToWorld is null.");
//		}
//
//	}
	
	/**
	 * Removes a certain {@link World} and updates the accesslists of the other Worlds.
	 *
	 * @param w the World, which will be removed.
	 */
	public void removeWorld(World w) {
		if (w != null) {
			worlds.remove(w);
			accessMap.remove(w);
			for (HashSet<World> accessList : accessMap.values()) {
				accessList.remove(w);
			}
			if (!validateConstraint()) {
				throw new RuntimeException("Accessability Constraint Violation due to World removement. Should not happen.");
			}
		} else {
			throw new IllegalArgumentException("World is null.");
		}
	}
	
	public Collection<World> getAdjacentWorlds(World referenceWorld) {
		HashSet<World> retSet = new HashSet<World>();
		retSet.addAll(accessMap.get(referenceWorld));
		return retSet;
	}
	
}
