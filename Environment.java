package epidemiology;
import sim.util.Bag;
import sim.util.distribution.Normal;
import spaces.Spaces;
import sim.field.grid.SparseGrid2D;
import sweep.SimStateSweep;
import sim.util.Int2D;

public class Environment extends SimStateSweep {
	public int gridWidth = 100;
	public int gridHeight = 100;
	public int searchRadius = 1;
	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public int numAgents = 250;
	public int recoveryTime = 20;
	public int recoveryError = 5;
	public double complianceAvg = 0.5;
	public double complianceSD = 0.1;
	public int burninTime = 20;
	public double baseInfectionRate = 1;
	public double randMove = .3;
	public boolean shareSpace = false;
	public boolean charts = false;//uses charts when true
	

	  
	public int clock = 0;
	public int quarantineTime;

	    
		public Environment(long seed, Class observer) {
			super(seed, observer);
			// TODO Auto-generated constructor stub
		}
		


	  
	    public void start() {
			super.start();
			makeSpace(gridWidth,gridHeight);
			makeAgents();
			if(observer != null)
				observer.initialize(space, spaces);
			
		}

	


	    public void makeAgents() {
	        // Create 1 agent that is infected (patient zero)
	        int x = random.nextInt(gridWidth);
	        int y = random.nextInt(gridHeight);
	        int xdir = random.nextInt(3) - 1;
	        int ydir = random.nextInt(3) - 1;
	        Normal normal = new Normal(complianceAvg, complianceSD, random);
	        double compliance = normal.nextDouble();
	        Agent patientZero = new Agent(x, y, xdir, ydir, compliance, Status.INFECTED, false);
	        sparseSpace.setObjectLocation(patientZero, x, y);
	        patientZero.colorByStrategy(this);

	        // Create remaining agents
	        for (int i = 1; i < numAgents; i++) {
	            int tempx = random.nextInt(gridWidth);
	            int tempty = random.nextInt(gridHeight);

	            if (!shareSpace) {
	                Bag b = sparseSpace.getObjectsAtLocation(tempx, tempty);
	                while (b != null) {
	                    tempx = random.nextInt(gridWidth);
	                    tempty = random.nextInt(gridHeight);
	                    b = sparseSpace.getObjectsAtLocation(tempx, tempty);
	                }
	            }

	            x = tempx;
	            y = tempty;
	            xdir = random.nextInt(3) - 1;
	            ydir = random.nextInt(3) - 1;
	            Agent a = new Agent(x, y, xdir, ydir, compliance, Status.SUSCEPTIBLE, false);
	            schedule.scheduleRepeating(a);
	            sparseSpace.setObjectLocation(a, x, y);
		        a.colorByStrategy(this);
	        }
	    }

		public int getGridWidth() {
			return gridWidth;
		}

		public void setGridWidth(int gridWidth) {
			this.gridWidth = gridWidth;
		}

		public int getGridHeight() {
			return gridHeight;
		}

		public void setGridHeight(int gridHeight) {
			this.gridHeight = gridHeight;
		}

		public int getNumAgents() {
			return numAgents;
		}

		public void setNumAgents(int numAgents) {
			this.numAgents = numAgents;
		}

		public int getRecoveryTime() {
			return recoveryTime;
		}

		public void setRecoveryTime(int recoveryTime) {
			this.recoveryTime = recoveryTime;
		}

		public int getRecoveryError() {
			return recoveryError;
		}

		public void setRecoveryError(int recoveryError) {
			this.recoveryError = recoveryError;
		}

		public double getComplianceAvg() {
			return complianceAvg;
		}

		public void setComplianceAvg(double complianceAvg) {
			this.complianceAvg = complianceAvg;
		}

		public double getComplianceSD() {
			return complianceSD;
		}

		public void setComplianceSD(double complianceSD) {
			this.complianceSD = complianceSD;
		}

		public int getBurninTime() {
			return burninTime;
		}

		public void setBurninTime(int burninTime) {
			this.burninTime = burninTime;
		}

		public double getBaseInfectionRate() {
			return baseInfectionRate;
		}

		public void setBaseInfectionRate(double baseInfectionRate) {
			this.baseInfectionRate = baseInfectionRate;
		}

		public double getRandMove() {
			return randMove;
		}

		public void setRandMove(double randMove) {
			this.randMove = randMove;
		}

		public boolean isShareSpace() {
			return shareSpace;
		}

		public void setShareSpace(boolean shareSpace) {
			this.shareSpace = shareSpace;
		}

		public boolean isCharts() {
			return charts;
		}

		public void setCharts(boolean charts) {
			this.charts = charts;
		}

		public int getClock() {
			return clock;
		}

		public void setClock(int clock) {
			this.clock = clock;
		}

		public int getQuarantineTime() {
			return quarantineTime;
		}

		public void setQuarantineTime(int quarantineTime) {
			this.quarantineTime = quarantineTime;
		}

	  

	 
	}