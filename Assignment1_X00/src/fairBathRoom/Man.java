package fairBathRoom;
import java.util.concurrent.ThreadLocalRandom;

import se.his.iit.it325g.common.AndrewsProcess;

public class Man implements Runnable {

	boolean TooManyMen = false;
	
	@Override
	public void run() {
		
		GlobalState.mutex.P();
		GlobalState.menQ.add(this);
		GlobalState.mutex.V();
		
		while (true) {
			// fill functionality here
			
			if (GlobalState.menQ.peek() == this && MEN TURN){
				GlobalState.mutex.P();
			
				if(GlobalState.numberOfWomenInCS > 0){
					GlobalState.numberOfDelayedMen++;
					if(!GlobalState.menQ.contains(this))
						GlobalState.menQ.add(this);
					GlobalState.mutex.V();
					GlobalState.menSem.P();
				}
				
				if(GlobalState.numberOfMenInCS < 4){ // l�gg till om f�rst i listan && om det �r ditt k�ns tur
					if (TooManyMen){
						TooManyMen = false;
						GlobalState.numberOfWaitingMen--;
					}
					GlobalState.numberOfMenInCS++;
					GlobalState.signal();
					
					doThings();
					System.out.println(getState());
					GlobalState.mutex.P();
					GlobalState.numberOfMenInCS--;
					GlobalState.signal();	
				
					doThings();
				} else {
					if (TooManyMen == false){
						GlobalState.numberOfWaitingMen++;
						TooManyMen = true;
					}
					GlobalState.mutex.V();
				}
			}
		}
	}

	public static String getState(){ // printout on the global state
		return "W " + AndrewsProcess.currentAndrewsProcessId()
				+ "  in CS, state: nm:" + GlobalState.numberOfMenInCS +
				" nw:" + GlobalState.numberOfWomenInCS +
				" dm:" + GlobalState.numberOfDelayedMen +
				" wm:" + GlobalState.numberOfWaitingMen +
				" dw:" + GlobalState.numberOfDelayedWomen +
				" ww:" + GlobalState.numberOfWaitingWomen;
	}

	// represents that processes are staying in a state for a while
	private void doThings() {
		AndrewsProcess.uninterruptibleMinimumDelay(ThreadLocalRandom.current()
				.nextInt(100, 500));
	}
}