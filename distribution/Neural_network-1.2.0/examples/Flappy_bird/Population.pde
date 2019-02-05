import java.util.Arrays;
class Population {

  ArrayList<Bird> alive = new ArrayList<Bird>();
  ArrayList<Bird> dead = new ArrayList<Bird>();

  Bird best = new Bird();

  Population(int Popsize) {
    for (int i = 0; i < Popsize; i++) {
      alive.add(new Bird());
    }
  }

  void show() {
    for (Bird b : alive) {
      b.show();
    }
  }

  void update(ArrayList<Pipe> pipes) {
    for (Bird b : alive) {
      b.update();
    }
    for (Pipe p : pipes) {
      for (int i = alive.size()-1; i >= 0; i--) {
        if (p.hits(alive.get(i))) {
          dead.add(alive.remove(i));
        }
      }
    }
  }    

  void think(ArrayList<Pipe> pipes) {
    for (Bird b : alive) {
      b.think(pipes);
    }
  }

  void nextGen() {
    calcFitness();
    calcBest();
    Bird[] pop = dead.toArray(new Bird[0]);
    ENN[] popBrains = new ENN[pop.length];
    for(int i = 0; i < pop.length; i++){
      popBrains[i] = pop[i].brain;
    }
    ENN[] offspring = ENN.crossover(popBrains);
    for(int i = 0; i < offspring.length; i++){
      Bird b = new Bird();
      b.brain = offspring[i];
      alive.add(b);
    }
    dead.clear();
  }

  void calcBest() {
    float maxFitness = best.brain.fitness;
    for (Bird b : dead) {
      if (b.brain.fitness > maxFitness) {
        best = new Bird();
        best.brain = b.brain.copy();
        best.brain.fitness = b.brain.fitness;
        maxFitness = best.brain.fitness;
      }
    }
    for (Bird b : alive) {
      if (pow(b.brain.fitness, 2) > maxFitness) {
        best = new Bird();
        best.brain = b.brain.copy();
        best.brain.fitness = pow(b.brain.fitness, 2);
        maxFitness = best.brain.fitness;
      }
    }
  }

  void calcFitness() {
    for (Bird b : dead) {
      b.brain.fitness = pow(b.brain.fitness, 2);
    }
  }
}