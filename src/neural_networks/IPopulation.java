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
	boolean Active();
	T Factory();
	T Copy();
	float Fitness();
	void CalcFitness();
	void ThinkWrapper();
	ENN GetBrain();
}
