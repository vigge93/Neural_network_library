package neural_networks;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Victor
 *
 * @param <T> Entity with brain
 */
public class Population<T extends IPopulation<T>> 
{
	ArrayList<T> alive = new ArrayList<T>();
	ArrayList<T> dead = new ArrayList<T>();
	
	/**
	 * Get all entities that are still alive
	 * @return All entities that are still alive
	 */
	public ArrayList<T> getAlive(){
		return alive;
	}
	
	/**
	 * Constructor
	 * @param popSize Size of the population
	 * @param fact Constructor for the entity
	 */
	public Population(int popSize, IPopulation<T> fact, Object... args) {
		for(int i = 0; i < popSize; i++) {
			alive.add(fact.factory(args));
		}
	}
	
	/**
	 * Method for thinking
	 */
	public void think(Object... args) {
		for(T entity : alive) {
			entity.think(args);
		}
	}
	
	/**
	 * Kill all entities that should be killed
	 */
	public void kill(Object... args) {
		for(int i = alive.size()-1; i >= 0; i--) {
			if(!alive.get(i).isAlive(args)) {
				dead.add(alive.remove(i));
			}
		}
	}
	
	/**
	 * Breeds the next generation based on the previous one
	 */
	public void nextGen(Object... args) {
		float totalFitness = calcFitness(args);
		for(int i = 0; i < dead.size(); i++) {
			T child = pickOne(totalFitness);
			alive.add(child);
		}
		dead.clear();
	}
	
	/**
	 * Check if generation is dead
	 * @return True if all entities in the generation are dead
	 */
	public boolean isDead() {
		return alive.size() <= 0;
	}
	
	T pickOne(float totalFitness){
		Random ran = new Random();
		int index = 0;
	    float r = (float) (ran.nextDouble()*totalFitness);

	    while (r >= 0 && index < dead.size()) {
	      r = r - dead.get(index).fitness();
	      index++;
	    }
	    index--;
	    if(index >= dead.size()) index = dead.size()-1;
	    T child = dead.get(index).Copy();
	    return child;
	}
	
	float calcFitness(Object... args){
		for(T entity : dead) {
			entity.calcFitness(args);
		}
		
		float totalFitness = 0;
		for(T entity : dead) {
			totalFitness += entity.fitness();
		}
		return totalFitness;
	}
	
}