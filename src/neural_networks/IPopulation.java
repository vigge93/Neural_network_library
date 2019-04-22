/**
 * 
 */
package neural_networks;

/**
 * Interface for the Population class
 * @author Victor
 * 
 */
public interface IPopulation<T> {
	/**
	 * Returns the state of the entity (Alive or dead)
	 * @param args Optional parameters required to check condition
	 * @return true if entity is alive, otherwise false
	 */
	boolean isAlive(Object... args);
	
	/**
	 * A factory to instantiate the entity
	 * @param args Optional parameters to instantiate entity
	 * @return A new instance of the entity object
	 */
	T factory(Object... args);
	
	/**
	 * Copies entity
	 * @return A deep copy of the entity
	 */
	T Copy();
	
	/**
	 * Get current fitness for entity
	 * @return Entity's current fitness
	 */
	float fitness();
	
	/**
	 * Calculate enity's fitness
	 * @param args Optional arguments required to calculate entity's fitness
	 */
	void calcFitness(Object... args);
	
	/**
	 * Makes a decision for the entity
	 * @param args Optional arguments required to make a decision
	 */
	void think(Object... args);
	
	/**
	 * Returns the entity's brain
	 * @return The entity's brain
	 */
	ENN getBrain();
	
}
