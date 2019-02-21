package neural_networks;

import java.util.ArrayList;
import java.util.Random;

public class Population<T 
	extends IPopulation<T>> 
{
	
	ArrayList<T> alive = new ArrayList<T>();
	ArrayList<T> dead = new ArrayList<T>();
	
	
	
	public Population(int popSize, IPopulation<T> fact) {
		for(int i = 0; i < popSize; i++) {
			alive.add(fact.Factory());
		}
	}
	
	public void Think() {
		for(T entity : alive) {
			entity.ThinkWrapper();
		}
	}
	
	public void Kill() {
		for(int i = 0; i < alive.size(); i++) {
			if(!alive.get(i).Active()) {
				dead.add(alive.remove(i));
			}
		}
	}
	
	public void nextGen() {
		float totalFitness = calcFitness();
		for(int i = 0; i < dead.size(); i++) {
			T child = pickOne(totalFitness);
			alive.add(child);
		}
		dead.clear();
	}
	
	public boolean isDead() {
		return alive.size() <= 0;
	}
	
	T pickOne(float totalFitness){
		Random ran = new Random();
		int index = 0;
	    float r = (float) (ran.nextDouble()*totalFitness);
	    while (r > 0) {
	      r = r - dead.get(index).Fitness();
	      index++;
	    }
	    index--;
	    T child = dead.get(index).Copy();
	    return child;
	}
	
	float calcFitness(){
		for(T entity : dead) {
			entity.CalcFitness();
		}
		
		float totalFitness = 0;
		for(T entity : dead) {
			totalFitness += entity.Fitness();
		}
		return totalFitness;
	}
	
}