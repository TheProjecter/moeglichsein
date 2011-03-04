package at.ac.univie.philo.mmr.shared.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import org.junit.Assert;

import at.ac.univie.philo.mmr.shared.exceptions.ConstraintViolationException;
import at.ac.univie.philo.mmr.shared.exceptions.IndividuumDoesNotExistExcetion;
import at.ac.univie.philo.mmr.shared.expressions.Constant;
import at.ac.univie.philo.mmr.shared.expressions.Predicate;

/**
 * The Class Universe is the main class for working with this API. Nearly every other Entitiy-Class is used here.
 */
public class Universe {

	/** The name. */
	private String name;
	
	/** The relations. */
	private AccessabilityRelation relations;
	
	private HashMap<Constant,Individual> constantMap;
	
	private HashSet<Predicate> predicates;
	
	private HashSet<Individual> individuals;
	
	/**
	 * Instantiates a new universe with a certain name.
	 *
	 * @param name the name of the universe.
	 */
	public Universe(String name, HashMap<Constant, Individual> constantMap) {
		if (name != null) {
			setName(name);
			setConstantMap(constantMap);
		} else {
			throw new IllegalArgumentException("name is null.");
		}
		predicates = new HashSet<Predicate>();
	}
	
	/**
	 * Instantiates a new universe with a certain name and a certain {@link AccessabilityRelation}.
	 *
	 * @param name the name of the universe
	 * @param r the accessability relation, which determines the connection between possible {@link World}s.
	 */
	public Universe(String name, AccessabilityRelation r, HashMap<Constant,Individual> constantMap) {
		this(name,constantMap);
		if (r != null && constantMap != null) {
			setRelations(r);
		} else {
			throw new IllegalArgumentException("Accessability Relation is null.");
		}
	}
	
	/**
	 * Instantiates a new universe with a certain name and a set of worlds and with no specific {@link AccessabilityConstraint}s.
	 *
	 * @param name the name of the universe.
	 * @param worlds the worlds of this universe, which won't be connected to each other. They even won't be connected to themselves. So they see nothing and you may want to add accesses to it later by modifying the {@link AccessabilityRelation}.
	 * @throws ConstraintViolationException the constraint violation exception
	 */
	public Universe(String name, HashMap<Constant,Individual> constantMap, World... worlds) throws ConstraintViolationException {
		this(name,AccessabilityConstraint.NONE, true, constantMap, worlds);
	}
	
	/**
	 * Instantiates a new universe with a certain name and a set of worlds and a specific {@link AccessabilityConstraint}.
	 * Every added World is modified to have all Predicates of each other World (if not existent with an empty Extension).
	 * @param name the name of the universe
	 * @param c the {@link AccessabilityConstraint} which should hold for the Relation between the Worlds.
	 * @param enforceConstraints Set to true, iff you want the graph to be modified in order to fulfill the constraints. Set to false, if you want to get an Exception, if the constraints do not fulfill. In this case, the constraints are set to {@link AccessabilityConstraint.NONE}
	 * @param worlds the worlds which will be in this Universe.
	 * @throws ConstraintViolationException iff enforceConstraints is false and the constraints do not fit with a set of unconnected worlds. In this case, the universe is created but with {@link AccessabilityConstraint.NONE}.
	 */
	public Universe(String name, AccessabilityConstraint c, boolean enforceConstraints, HashMap<Constant,Individual> constantMap, World ... worlds) throws ConstraintViolationException {
		this(name, constantMap);
		if (c != null && worlds != null) {
			this.relations = new AccessabilityRelation();

			//first find all predicates and individuals
			for (World w : worlds){
				predicates.addAll(w.getPredicates());
				individuals.addAll(w.getInventory());
			}
			
			//then connect the worlds to this universe
			//and synchronize the availability of predicates in each World
			for (World w : worlds){
				w.addPredicates(predicates);
				relations.addWorld(w);
				w.setUniverse(this);
			}
			
			if (enforceConstraints)
				this.relations.enforceConstraint(c);
			
			// when Validation fails
			if (!this.relations.validateConstraint(c)) {
//				//set constraints to NONE
//				this.relations.changeConstraint(AccessabilityConstraint.NONE);
//				//and throw exception
				throw new ConstraintViolationException();	
			} else {
				this.relations.changeConstraint(c);
			}
		} else {
			throw new IllegalArgumentException("Input-Check failed because one of the inputs was null.");	
		}
	}
	
	private void setConstantMap(HashMap<Constant, Individual> constantMap) {
		if (constantMap != null) {
			individuals = new HashSet<Individual>();
		//validity check for constantMapping
		for (Entry<Constant, Individual> entry : constantMap.entrySet()) {
			individuals.add(entry.getValue());
			if(entry.getValue() == null) {
				throw new IllegalArgumentException("There is a null-constant.");
			}
			if (entry.getKey() == null){
				throw new IllegalArgumentException("There is a constant which maps to null.");
			}
		}
		this.constantMap = new HashMap<Constant, Individual>(constantMap);
		} else {
			throw new IllegalArgumentException("constantMapping is null.");
		}
	}
	
	public HashMap<Constant, Individual> getConstantMap() {
		return this.constantMap;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name of the universe.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name of the universe
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the {@link AccessabilityRelation}.
	 *
	 * @return the relations
	 */
	public AccessabilityRelation getRelations() {
		return this.relations;
	}
	
	public Set<World> getWorlds() {
		return relations.getWorlds();
	}
	
	/**
	 * Sets the {@link AccessabilityRelation}.
	 *
	 * @param rels the new relations
	 */
	private void setRelations(AccessabilityRelation rels) {
		this.relations = rels;
		for (World w: rels.getWorlds()) {
			w.setUniverse(this);
		}
	}
	
	/**
	 * Removes a given Individual from the Universe (and also recursively in all Worlds)
	 * @param i Individual which gets removed in each World and in the whole Universe
	 */
	public void removeIndividual(Individual i) {
		//remove from constantMap
		Collection<Individual> col = constantMap.values();
		individuals.remove(i);
		
		//remove constants which map to the individual
		boolean more = true;
		while(more) {
			more = col.remove(i);
		}
		//remove from each World by removing from the ExtensionMaps
		for (World w : getWorlds()) {
			w.removeIndividual(i);
		}
		
	}
	
	/**
	 * 
	 * @param win, ther world where we start to traverse
	 * @return a list of all worlds which are reachable from input-world
	 */
	public HashSet<World> getAccessibleWorlds(World win) {
		if (win == null ){
			throw new IllegalArgumentException("Input-Check failed because Input-World is null.");
		}
		return new HashSet<World>(relations.getAdjacentWorlds(win));
//		//resultSet
//		HashSet<World> isVisited = new HashSet<World>();
//
//		//inputSet for one while-loop
//		HashSet<World> inputWorlds = new HashSet<World>();
//		//we begin to traverse in "our" world
//		inputWorlds.add(win);
//		
//		//as long as there are unvisited worlds which are adjacent to visited worlds
//		while (!inputWorlds.isEmpty()) {
//			HashSet<World> currentAdjacentList = new HashSet<World>();
//			for(World x: inputWorlds) {
//				inputWorlds.remove(x);
//				if (isVisited.contains(x)) continue; 
//				currentAdjacentList.addAll(relations.getAdjacentWorlds(x));
//				isVisited.add(x);
//			}
//			inputWorlds.addAll(currentAdjacentList);
//		}
//		return isVisited;
	}

	public boolean addNewWorld(String worldName) {
		for(World w : this.getWorlds()) {
			if (worldName.equals(w.getName())) {
				return false;
			}
		}

		World w = new World(worldName, predicates);
		w.setUniverse(this);
		relations.addWorld(w);
		return true;
	}

	/**
	 * 
	 * @return set of individuals which is backed, so if you modify the set you modify the set of global individuals of this universe
	 */
	public Set<Individual> getIndividuals() {
		return individuals;
	}

	public int getFreeConstantIndex() {
		//TODO: maybe cache the value
		return computeNextFreeConstantIndex();
	}

	private int computeNextFreeConstantIndex() {
		ArrayList<Constant> clist = new ArrayList<Constant>(constantMap.keySet());
		Collections.sort(clist, new Comparator<Constant>() {

			@Override
			public int compare(Constant o1, Constant o2) {
				Integer o1index = o1.getIndex();
				Integer o2index = o2.getIndex();
				return o1index.compareTo(o2index);
			}
		});
		Constant highestC = clist.get(clist.size()-1);
		// TODO Auto-generated method stub
		return highestC.getIndex()+1;
	}
	
	public boolean isFreeIndividualName(String indiName) {
		for (Individual i : getIndividuals()) {
			if (i.getName().equals(indiName)) {
				return false;
			}
		}
		return true;
	}

	public void addIndividual(Individual individual) {
		if (individual != null) {
			this.individuals.add(individual);
		}
	}

	public boolean isFreeWorldName(String value) {
		for (World w : this.getWorlds()) {
			if (w.getName().equals(value)) {
				return false;
			}
		}
		return true;
	}

	public boolean isFreePredicateName(String value) {
		for (Predicate p : this.predicates) {
			if (p.getName().equals(value)) {
				return false;
			}
		}
		return true;
	}

	public void removePredicate(Predicate object) {
		predicates.remove(object);
		for(World w : getWorlds()) {
			w.removePredicate(object);
		}
		
	}
	
	public HashSet<Predicate> getPredicates() {
		return new HashSet<Predicate>(predicates);
	}

	public void addPredicate(Predicate predicate) {

		if (predicate != null) {
			HashSet<Predicate> tempPreds = new HashSet<Predicate>();
			tempPreds.add(predicate);
			predicates.add(predicate);
			for (World w : getWorlds()) {
				w.addPredicates(tempPreds);
			}
		}
		
	}

	/**
	 * 
	 * @param arity
	 * @return a set of predicates which have an arity of the given int-parameter
	 */
	public HashSet<Predicate> getPredicates(int arity) {
		HashSet<Predicate> predicates = new HashSet<Predicate>();
		for (Predicate p : getPredicates()) {
			if (p.getArity() == arity) {
				predicates.add(p);
			}
		}
		return predicates;
	}
	
//	public void getRequiredConstraintForAccessibleWorlds(World win, HashSet<World> world) {
//		if (win == null ){
//			throw new IllegalArgumentException("Input-Check failed because Input-World is null.");
//		}
//		relations
//	}
	
//	/**
//	 * Gets a set of worlds, which can be accessed from the given world.
//	 *
//	 * @param myWorld the actual world, for which you want to know the accessible worlds.
//	 * @return the accessible worlds
//	 * @throws WorldDoesNotExistException the world does not exist exception
//	 */
//	public HashSet<World> getAccessibleWorlds(World myWorld) throws WorldDoesNotExistException {
//		if (myWorld == null) {
//			throw new IllegalArgumentException("Input-Check failed because Input-World is null.");
//		}
//
//		HashSet<World> result = new HashSet<World>();
//		
//		//search  myWorld in relations
//		if (!this.relations.getGraph().containsVertex(myWorld))
//			throw new WorldDoesNotExistException();
//		
//		for(World w : this.relations.getGraph().vertexSet()) {
//			System.out.println("Foreach: World "+w.getName());
//			//if myWorld can see this world...
//			//TODO: DANGER! this above statement only holds if there is a single edge between these two worlds.
//			
//			if (relations.getGraph().containsEdge(myWorld, w)) {
//				//add it to the result set
//				result.add(w);
//				//TODO: If I want to implement counterpart-function, I need to modify something here.
//			}
//		}
//		return result;
//		
//	}
	
//	/**
//	 * Checks if a simple statement is necessary in this Universe (i.e.: If for every world which is accessible from myWorld the statement is true)
//	 * But note, that you can give only a specific Individual and not some individual.
//	 * @param a the {@link Individual} for the simple statement, we want to know, if it is necessary in this Universe.
//	 * @param p the {@link Predicate} for the simple statement, we want to know, if it is necessary in this Universe.
//	 * @param myWorld the actual (real) {@link World}, from which we start to discover the possible worlds. We only check Worlds, which are reachable from this world.
//	 * @return true, iff this statement is necessary and false otherwise.
//	 * @throws WorldDoesNotExistException iff myWorld does not exist in this universe.
//	 */
//	boolean isNecessary(Individual a, Predicate p, World myWorld) throws WorldDoesNotExistException {
//		if (a != null && p != null && myWorld != null) {
//			for(World w : this.getAccessibleWorlds(myWorld)) {
//					if (!w.evaluate(a, p)) return false;
//					else System.out.println("In World "+myWorld.getName()+" "+a.getName()+" ("+a.getSymbol()+") is-a "+p.getName()+" ( "+p.getSymbol()+" ).");
//					//TODO: If I want to implement counterpart-function, I need to modify something here. because then it is not the same individual, but similar individual.	
//			}
//			return true;
//		} else {
//			throw new IllegalArgumentException("Input-Check failed because one of the inputs is null.");
//		}
//		
//	}
//	
//	/**
//	 * Checks if a certain simple statement is possible (i.e.: There is at least one world, for which the statement a is-a p holds).
//	 *
//	 * @param a the {@link Individual} for the simple statement, we want to know, if it is possible in this Universe.
//	 * @param p the {@link Predicate} for the simple statement, we want to know, if it is possible in this Universe.
//	 * @param myWorld the actual (real) {@link World}, from which we start to discover the possible worlds. We only check Worlds, which are reachable from this world.
//	 * @return a set of worlds, for which the statements hold. If there is no world, for which the statement holds, it returns null.
//	 * @throws WorldDoesNotExistException iff myWorld does not exist in this universe.
//	 */
//	HashSet<World> isPossible(Individual a, Predicate p, World myWorld) throws WorldDoesNotExistException {
//		if (a != null && p != null && myWorld != null) {
//			HashSet<World> result = new HashSet<World>();
//			for(World w : this.getAccessibleWorlds(myWorld)) {
//				if (!w.evaluate(a, p)) 
//					continue;
//				else { 
//					System.out.println("In World "+myWorld.getName()+" "+a.getName()+" ("+a.getSymbol()+") is-a "+p.getName()+" ( "+p.getSymbol()+" ).");
//					result.add(w);
//				}
//			}
//			//TODO: If I want to implement counterpart-function, 
//			//I need to modify something here. because then it is not the same individual, 
//			//but similar individual.	
//			if (result.size()==0) return null;
//			return result;
//		} else {
//			throw new IllegalArgumentException("Input-Check failed because some of the input-parameters are null.");
//		}
//	}
}
