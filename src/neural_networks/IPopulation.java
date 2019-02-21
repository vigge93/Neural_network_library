/**
 * 
 */
package neural_networks;

/**
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
