package epidemiology;


import observer.Observer;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer implements Steppable{

	public int numAgents = 0;
	public int infectedAgents=0;
	public int susceptibleAgents=0;
	public int exposedAgents=0;
    public int recoveredAgents=0;
    public int nQuarantined=0;


	/**
	 * 
	 * @param state
	 */
	
	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);

	}
	
	public void step(SimState state) {
		super.step(state);
//		if(step % this.state.dataSamplingInterval==0) {
//			countStatus((Environment)state);
//		}
		//nextInterval()
		
		Environment estate = (Environment)state;
		upDatePopulation(estate);
		if(estate.paramSweeps && getdata) {
			reset(state); // reset variables
			countStatus(estate);
			nextInterval(); // saves data to a file
			
		}
		
	}
	
	public void countStatus(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		for(int i=0;i<agents.numObjs;i++) {
			Agent a =(Agent)agents.objs[i];
			switch(a.status) {
			case INFECTED:
				infectedAgents ++;
				break;
			case SUSCEPTIBLE:
				susceptibleAgents ++;
				break;
			case RECOVERED:
				recoveredAgents ++;
				break;
			case EXPOSED:
				exposedAgents ++;
				break;	
			default:
				break;
			
				
			}
		}
	}
	
	
public void stop(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		if(agents == null || agents.numObjs == 0 || infectedAgents==numAgents) {
			event.stop();
		}
	}
	
	/**
	 * 
	 * @param state
	 */


	/**
	 * 
	 * @param state
	 * @return
	 */
	public boolean reset(SimState state) {
		super.reset();
		infectedAgents =1;
		susceptibleAgents=0;
	    exposedAgents=0;
	    recoveredAgents=0;
	    nQuarantined=0;
		return true;
		
	}

	/**
	 * 
	 * @return
	 */
	
	public boolean nextInterval() {
		double total = numAgents;
		data.add(total);
		data.add(infectedAgents/total);
		data.add(susceptibleAgents/total);
		data.add(exposedAgents/total);
		data.add(recoveredAgents/total);
		return false;
	}


	/**
	 * 
	 * @param fileName
	 * @param folderName
	 * @param state
	 * @param sweeper
	 * @param precision
	 * @param headers
	 */
	
	
	/**
	 * 
	 * @param state
	 */
public void upDatePopulation(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		for(int i=0;i<agents.numObjs;i++) {
			Agent a = (Agent)agents.objs[i];
			a.inQuarantine=false;
		}
	}
}
	