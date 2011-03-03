package at.ac.univie.philo.mmr.shared.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import at.ac.univie.philo.mmr.shared.exceptions.IndividuumDoesNotExistExcetion;
import at.ac.univie.philo.mmr.shared.exceptions.PredicateNotExistsException;
import at.ac.univie.philo.mmr.shared.expressions.Constant;
import at.ac.univie.philo.mmr.shared.expressions.ConstantExpression;
import at.ac.univie.philo.mmr.shared.expressions.FunctionExpression;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;
import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.visitors.CommentPrinter;
import at.ac.univie.philo.mmr.shared.visitors.EvaluationResult;

// TODO: Auto-generated Javadoc
/**
 * The Class World. It stores every information of one (possible) world.
 */
public class World {

	/** The name of the world. */
	private String name;
	
	/** The extension map maps to each Predicate a set of Individuals. extensionMap stores every extension of a predicate - but only in THIS world. */
	private HashMap<Predicate, HashSet<ArrayList<Individual>>> extensionMap;
	
	/** The inventory stores every individual, which EXISTS in this world.  */
	private HashSet<Individual> inventory;
	
	/** Link to the corresponding Universe */
	private Universe universe = null;
	
	/**
	 * Instantiates a new world.
	 *
	 * @param name the name of the world.
	 */
	private World (String name) {
		if (name != null) {
			setName(name);
			inventory = new HashSet<Individual>();
		}
		else {
			throw new IllegalArgumentException("name is null.");
		}
	}
	
	/**
	 * Instantiates a new world.
	 *
	 * @param name the name of the world
	 * @param extensionMap the extension map, which maps to every Predicate P a set of individuals, which is the extension of P in this world.
	 */
	public World (String name, HashMap<Predicate, HashSet<ArrayList<Individual>>> extensionMap) {
		this(name);
		if (extensionMap != null) {
			//validity check for extensionMap
			for (Entry<Predicate, HashSet<ArrayList<Individual>>> entry: extensionMap.entrySet()) {
				if(entry.getValue() == null) {
					throw new IllegalArgumentException("Input-Check failed, because at least one extension was null");
				}
				if(entry.getKey() == null) {
					throw new IllegalArgumentException("input-check fails because there is a Null-key-Predicate.");
				}
			}//end of validity-Check for extensionMap
		inventory = new HashSet<Individual>();	
		setExtensionMap(extensionMap);		
		} else {
			throw new IllegalArgumentException("name or extensionMap is null.");
		}
	}
	
	/**
	 * 
	 * @param worldName
	 * @param predicates A list of global predicates from the Universe which are used to create an empty Extension table
	 */
	public World(String worldName, HashSet<Predicate> predicates) {
		this(worldName);
		extensionMap = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		inventory = new HashSet<Individual>();
		addPredicates(predicates);
	}

	/**
	 * Adds some individuals to the extension of Predicate p in this world.
	 *
	 * @param p the predicate, whose extension will be extended.
	 * @param indisequences the individuals, which are added to the extension of predicate p.
	 * @throws IndividuumDoesNotExistExcetion 
	 */
	public void extendExtension(Predicate p, ArrayList<Individual> ...indisequences ) throws IndividuumDoesNotExistExcetion {
		if (p != null && indisequences != null) {
			//Is there such a Predicate?
			int predarity = p.getArity();
			HashSet<ArrayList<Individual>> extension = null;
			try {
			 extension = getExtension(p);
			} catch (PredicateNotExistsException e) {
				extension = new HashSet<ArrayList<Individual>>();
			}
			
			//Fill the Extension with the given Individuals
			for (ArrayList<Individual> indisequence: indisequences) {
				int indisequenceArity = indisequence.size();
//				if (!hasExistingIndividuals(indisequence)) {
//					throw new IndividuumDoesNotExistExcetion("At least one given Individual dos not exist in this world.");
//				}
				if (indisequenceArity == predarity) {
					extension.add(indisequence);
					inventory.addAll(indisequence);
				} else {
					throw new IllegalArgumentException("At least one sequence of individuals has different size ("+indisequenceArity+"instead of "+predarity+") than needed by the arity of the given Predicate");
				}
			}
			
			//Put the Set back into the ExtensionMap
			extensionMap.put(p, extension);			
		} else {
			throw new IllegalArgumentException("Some of the inputs is null.");
		}
	}
	
	/**
	 * Provide one Individual a with some new predicates. Technically, it is like adding Individuals to an extension, but here we have only one Individual and some predicates.
	 *
	 * @param indilist the individual which will predicates.
	 * @param predicates Every's predicate-extension will be enriched with the individual a.
	 * @throws IndividuumDoesNotExistExcetion when the individuum a does not exist in this world.
	 */
	public void extendExtension(ArrayList<Individual> indilist, Predicate...predicates ) throws IndividuumDoesNotExistExcetion {
		if (predicates != null) {
//			if (!hasExistingIndividuals(indilist)) throw new IndividuumDoesNotExistExcetion("At least one Individual in the given Sequence does not exist in this World "+this.getName()+".");
			for (Predicate p: predicates) {
				extendExtension(p, indilist);
			}
		} else {
			throw new IllegalArgumentException("there is no given predicate.");
		}


	}
	
	/**
	 * Add predicates just to synchronize with other Worlds. The initial Extension is empty
	 * @param predicates the set of predicates to add if not already available in this world
	 */
	public void addPredicates(HashSet<Predicate> predicates) {
		if (predicates != null) {
			for (Predicate p : predicates) {
				HashSet<ArrayList<Individual>> hai = extensionMap.get(p);
				if (hai == null) {
					extensionMap.put(p, new HashSet<ArrayList<Individual>>());
				}
			}
		}
	}
	
	/**
	 * Overwrite the last extension of Predicate p and give an alternative extension.
	 *
	 * @param p the predicate p, whose extension will be overwritten.
	 * @param the sequence of individuals who form the new extension for Predicate p. If the arity is 1, it is a set of sequences with the length 1.
	 * @throws IllegalArgumentException if the arity does not match with the given sequence of individuals. 
	 * @throws IndividuumDoesNotExistExcetion when the individual does not exist.
	 */
	public void overwriteExtension(Predicate p, HashSet<ArrayList<Individual>> ...extensions ) throws IndividuumDoesNotExistExcetion {
		if (p != null && extensions != null) {
			int predarity = p.getArity();
			HashSet<ArrayList<Individual>> extension = new HashSet<ArrayList<Individual>>();
			//Fill the Extension with the given Individuals
			for (HashSet<ArrayList<Individual>> indilist : extensions) {
				for (ArrayList<Individual> ai : indilist) {
					if (predarity != ai.size()) {
						throw new IllegalArgumentException("At least one sequence of individuals has different size ("+indilist.size()+"instead of "+predarity+") than needed by the arity of the given Predicate");
					}
	//				if (!hasExistingIndividuals(indilist)) {
	//					throw new IndividuumDoesNotExistExcetion("At least one given Individual dos not exist in this world.");
	//				}
					extension.add(ai);
					inventory.addAll(ai);
				}
			}
			
			//Put the Set into the ExtensionMap, instead of the old one
			extensionMap.put(p, extension);
		} else {
			throw new IllegalArgumentException("InputCheck failed because some parameter is null.");
		}
	}
	
/**
 * Overwrite the extension for every predicat in the list. The result is, that every predicate of the given predicates has only one element in his extension: a.
 *
 * @param the sequence of individuals, which form the new extension for every predicate.
 * @param predicates the predicates, whose extension is overwritten.
 * @throws IndividuumDoesNotExistExcetion when the individuum does not exist.
 * @throws IllegalArgumentException if the arity does not match with the given sequence of individuals.
 */
@SuppressWarnings("unchecked")
void overwriteExtension(HashSet<ArrayList<Individual>> indilist, Predicate...predicates ) throws IndividuumDoesNotExistExcetion {
	if (indilist != null && predicates != null) {

//		if (!hasExistingIndividuals(indilist)) {
//				throw new IndividuumDoesNotExistExcetion("At least one given Individual dos not exist in this world.");
//		}
			for (Predicate p: predicates) {
					overwriteExtension(p, indilist);
			}
	} else {
		throw new IllegalArgumentException("Input check failed because at least one parameter is null.");
	}
	}

	
	public boolean hasExistingIndividuals(Collection<Individual> individuals) {
		if (individuals != null) {
			return inventory.containsAll(individuals);
		} else {
			throw new IllegalArgumentException("input check failed because individual is null.");
		}
	}

	/**
	 * Checks for existing individual.
	 *
	 * @param a the individual, whose existence in this world is checked.
	 * @return true, iff it exists. false, otherwise. 
	 */
	public boolean hasExistingIndividual(Individual a) {
		if (a != null) {
			return (inventory.contains(a));
		} else {
			throw new IllegalArgumentException("input check failed because individual is null.");
		}
	}

	/**
	 * Gets the extension of a certain predicate.
	 *
	 * @param p the predicate, for which you search the extension.
	 * @return the extension for predicate p.
	 * @throws PredicateNotExistsException iff the predicate does not exist.
	 */
	public HashSet<ArrayList<Individual>> getExtension(Predicate p) throws PredicateNotExistsException {
		if (p != null) {
			if (!extensionMap.containsKey(p)) throw new PredicateNotExistsException(p.getName()+" has no incarnation in this world.");
			return extensionMap.get(p);
		} else {
			throw new IllegalArgumentException("Input-Check failed because Predicate is null.");
		}
	}
	
	/**
	 * Gets the predicates.
	 *
	 * @param indilist the sequence of individuals a (that means one extension-sequence), for which you want all predicates of them.
	 * @return the predicates, which apply to the sequence of individuals a (can also be an empty set).
	 */
	HashSet<Predicate> getPredicates(ArrayList<Individual> indilist) throws IndividuumDoesNotExistExcetion {
		if (indilist == null) {
			throw new IllegalArgumentException("Input-Check failed because input is null.");
		}
		if (!hasExistingIndividuals(indilist)) {
			return new HashSet<Predicate>();
		}
		
		HashSet<Predicate> resultset = new HashSet<Predicate>();
		Set<Predicate> predicates = extensionMap.keySet();
			
		for (Predicate p: predicates) {
			try {
				if (getExtension(p).contains(indilist)) resultset.add(p);
			} catch (PredicateNotExistsException e) {
				// should not happen
				e.printStackTrace();
			}
		}
		return resultset;
	}
	
	/**
	 * 
	 * @return a set of Predicates which have a defined Extension in this World (including Predicates with empty extension)
	 */
	public HashSet<Predicate> getPredicates() {
		return new HashSet<Predicate>(extensionMap.keySet());
	}
	
	/**
	 * Gets the name of the world.
	 *
	 * @return the name of the world
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name of the world.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the extension map, which assigns an extension to every predicate.
	 *
	 * @return the extension map
	 */
	public HashMap<Predicate,HashSet<ArrayList<Individual>>> getExtensionMap() {
		return this.extensionMap;
	}
	
	/**
	 * Sets the extension map, which assigns an extension to every predicate.
	 *
	 * @param extensionMap the extension map
	 */
	private void setExtensionMap (HashMap<Predicate,HashSet<ArrayList<Individual>>> extensionMap) {
		//make a copy
		this.extensionMap = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		for(Entry<Predicate,HashSet<ArrayList<Individual>>> entry : extensionMap.entrySet()) {
			Predicate p = entry.getKey();
			HashSet<ArrayList<Individual>> hai = new HashSet<ArrayList<Individual>>();
			for(ArrayList<Individual> ai : entry.getValue()) {
				ArrayList<Individual> newAi = new ArrayList<Individual>(ai);
				hai.add(newAi);
				//also add the individuals:
				inventory.addAll(ai);
			}
			this.extensionMap.put(p, hai);
		}	
	}
	
	
	/**
	 * Gets a Collection, which contains every existing individual in this world. Existing only means that it is member of at least one extension of a predicate in this world.
	 *
	 * @return the inventory
	 */
	public Collection<Individual> getInventory() {
		return new HashSet<Individual>(inventory);
	}
	
	/**
	 * Sets the inventory, which contains every existing individual in this world.
	 *
	 * @param inv the new inventory
	 */
	private void setInventory(HashSet<Individual> inv) {
		this.inventory=inv;
	}
	
	public Universe getUniverse() {
		return universe;
	}

	public void setUniverse(Universe universe) {
		if(universe == null) {
			throw new IllegalArgumentException("Universe is null");
		}
		this.universe = universe;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof World) {
			World otherWorld = (World)o;
			return 	this.name.equals(otherWorld.name) && 
					this.extensionMap.equals(otherWorld.extensionMap) && 
					this.inventory.equals(otherWorld.inventory);
		}
		return false;
	}
	
	public String getString() {
		StringBuffer result = new StringBuffer();
		result.append("World "+this.getName()+" content:\n\n Existing Individuals:\n");
		for (Individual i : inventory) {
			result.append(i.getName() + "\n");
		}
		result.append("\nExtensions of Predicates:\n");
		for (Entry<Predicate,HashSet<ArrayList<Individual>>> entry : extensionMap.entrySet()) {
			Predicate p = entry.getKey();
			result.append("*** "+p.getName() + "/"+p.getArity()+"\n");
			HashSet<ArrayList<Individual>> ext = entry.getValue();
			int countPerSolution = 0;
			result.append(CommentPrinter.printExtension(ext));
			result.append("\n");
		}
		return result.toString();	
	}

	/**
	 * 
	 * @param i
	 */
	public void removeIndividual(Individual i) {
		HashMap<Predicate, HashSet<ArrayList<Individual>>> newExtension = new HashMap<Predicate, HashSet<ArrayList<Individual>>>();
		for (Entry<Predicate, HashSet<ArrayList<Individual>>> haientry : extensionMap.entrySet()) {
			HashSet<ArrayList<Individual>> hainew = new HashSet<ArrayList<Individual>>();
			for (ArrayList<Individual> ai : haientry.getValue()) {
				//List of Individuals in an Extension which contain the to-be-removed Individual are not added in the new Extension
				if(!ai.contains(i)) {
					hainew.add(ai);
				}
			}
			newExtension.put(haientry.getKey(), hainew);
		}
		inventory.remove(i);
		setExtensionMap(newExtension);
	}

	public void removePredicate(Predicate object) {
		extensionMap.remove(object);
		
	}
	
}

