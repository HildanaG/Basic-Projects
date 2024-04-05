package epidemiology;
import sim.util.Bag;
import sim.engine.SimState;
import sim.engine.Steppable;



public class Agent implements Steppable {
    public int x;
    public int y;
    public int xdir;
    public int ydir;
    public double compliance;
    public Status status;
    public boolean inQuarantine;
    public int sickTime = 0;
	//private MersenneTwisterFast random;

    public Agent(int x, int y, int xdir, int ydir, double compliance, epidemiology.Status status, boolean inQuarantine) {
        this.x = x;
        this.y = y;
        this.xdir = xdir;
        this.ydir = ydir;
        this.compliance = compliance;
        this.status = status;
        this.inQuarantine = inQuarantine;
    }

    @Override
    public void step(SimState state) {
        Environment environment = (Environment) state;
        environment.clock++;

        if (this.status == Status.EXPOSED) {
            this.status = Status.INFECTED;
           return; // [return I guess?]
        }

        if (this.status == Status.INFECTED) {
            this.sickTime++;
            if (environment.clock >= environment.burninTime) {
                if (this.inQuarantine) {
                    checkRecover(environment);
                    if (this.sickTime > environment.quarantineTime) {
                        this.inQuarantine = false;
                        return; // end step
                    }
                } else move(environment);
            }
            checkQuarantine(environment);
            checkRecover(environment);
            if (this.inQuarantine) {
                return;
            }
        } else move(environment);
        if (this.status == Status.SUSCEPTIBLE) {
            Bag neighbors = findSickNeighbors(environment);
            interact(environment, neighbors);
        }
        colorByStrategy(environment);
    }
    
    public void colorByStrategy(Environment state) {
    	switch(this.status) {
    	case SUSCEPTIBLE:
    		state.gui.setOvalPortrayal2DColor(this, (float)0,  (float)0,  (float)1,  (float)1);//BLUE
    		break;
    	case EXPOSED:
    		state.gui.setOvalPortrayal2DColor(this, (float)0.7,  (float)0,  (float)1,  (float)1);//PURPLE
    		break;
    	case INFECTED:
    		if(this.inQuarantine) {
    			state.gui.setOvalPortrayal2DColor(this, (float)1,  (float)0.1,  (float)0.1,  (float)0.5);//LIGHT
    		break;
    	}
			state.gui.setOvalPortrayal2DColor(this, (float)1,  (float)0,  (float)0,  (float)1);//RED

    	case RECOVERED:
    		state.gui.setOvalPortrayal2DColor(this, (float)0.5,  (float)0.5,  (float)0.5,  (float)1);//GREY
    		break;
    		default:
    			state.gui.setOvalPortrayal2DColor(this, (float)1,  (float)1,  (float)1,  (float)1);//BLACK
    			break;

    	}
    }
    
    public void move(Environment state) {
      	 if (!state.random.nextBoolean(state.randMove)) {
      		 return;
      	 }
      	 if(state.random.nextBoolean(state.randMove)) {
               xdir = state.random.nextInt(3)-1;
               ydir = state.random.nextInt(3)-1;
           }
           placeAgent(state);

       }

       public void placeAgent(Environment state) {
   		if(!state.shareSpace) {
   			int tempx = state.sparseSpace.stx(x + xdir);
   			int tempy = state.sparseSpace.sty(y + ydir);
   			Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
   			if(b == null || b.numObjs == 0){
   				x = tempx;
   				y = tempy;
   				state.sparseSpace.setObjectLocation(this, x, y);
   			}
   		}
   		else {
   			x = state.sparseSpace.stx(x + xdir);
   			y = state.sparseSpace.sty(y + ydir);
   			state.sparseSpace.setObjectLocation(this, x, y);
   		}
   	}

    public void checkQuarantine(Environment state) {
        if (state.random.nextBoolean(compliance)) {
            this.inQuarantine = true;
        }
    }

    public void checkRecover(Environment state) {
        int recoveryTime = state.recoveryTime + state.random.nextInt(2 * state.recoveryError) - state.recoveryError;
        if (this.sickTime == recoveryTime) {
            this.status = Status.RECOVERED;
            this.inQuarantine = false;
        }
    }

    public Bag findSickNeighbors(Environment state) {
        Bag neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.getSearchRadius(), state.sparseSpace.TOROIDAL, false);
        Bag sickNeighbors = new Bag();
        for (int i = 0; i < neighbors.size(); i++) {
            Agent a = (Agent) neighbors.get(i);
            if (!a.inQuarantine && a.status == Status.INFECTED) {
                sickNeighbors.add(a);
            }
        }
        return sickNeighbors;
    }

    public void interact(Environment state, Bag neighbors) {
        if (neighbors.isEmpty()) {
            return;
        }
        for (int i = 0; i < neighbors.size(); i++) {
            boolean infected = state.random.nextBoolean(state.baseInfectionRate);
            if (infected) {
                this.status = Status.EXPOSED;
                return; // Break out of method and return to step
            }
        }
        // If lucky, they aren't exposed
    }
}
