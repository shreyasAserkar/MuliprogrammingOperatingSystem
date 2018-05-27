import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Class - SCHEDULER
 * 
 * Description - This method schedules the job process by sending the first job
 * from ready queue to CPU and sending the process from CPU to Blocked queue
 * whenever any I/O operation occurs, or else sending it to READY queue when the
 * page referred is in MEMORY.
 * 
 */
public class SCHEDULER {

	static ArrayList<PCB> READYQUEUE = new ArrayList<PCB>();
	static ArrayList<PCB> BLOCKEDQUEUE = new ArrayList<PCB>();
	PCB temp;
	static PCB temp1;
	

	public void SCHEDULING() throws FileNotFoundException {

		if (READYQUEUE.isEmpty()) {
			System.out.println("Ready Queue is Empty");
			for (int i = 0; i < SYSTEM.jobPCB.size(); i++) {
				temp = SYSTEM.jobPCB.get(i);
				READYQUEUE.add(temp);
			}
			CPU.INSTRUCTIONS(SCHEDULER.READYQUEUE.get(0).getPc());
		}

	}

	// This method adds the process at the last of READY queue if the time
	// quantum increases 20 VTU
	public static void PUT_PROCESS_IN_READY_QUEUE() throws FileNotFoundException {

		temp1 = SCHEDULER.READYQUEUE.get(0);
		SCHEDULER.READYQUEUE.remove(0);
		SCHEDULER.READYQUEUE.add(temp1);
		System.out.println("Blocked : " + SCHEDULER.BLOCKEDQUEUE.size());
		System.out.println("Ready : " + SCHEDULER.READYQUEUE.size());
		if (SCHEDULER.READYQUEUE.size() == 0) {
			System.out.println("No more Jobs");
			// System.exit(0);
		} else {
			CPU.INSTRUCTIONS(SCHEDULER.READYQUEUE.get(0).getPc());
		}

	}// End of PUT_PROCESS_IN_READY_QUEUE

	public static void PROCESS_ENDED() throws FileNotFoundException {

		SCHEDULER.READYQUEUE.remove(0);
		if (SCHEDULER.READYQUEUE.size() == 0) {
			System.out.println("No more Jobs");
			System.exit(0);
		} else {
			PUT_PROCESS_IN_READY_QUEUE();
		}

	}// End of PROCESS_ENDED
	
	public static void PUT_PROCESS_IN_BLOCK_QUEUE() throws FileNotFoundException {
		PCB temp2;
		PCB temp3;
		
		temp2 = SCHEDULER.READYQUEUE.get(0);
		SCHEDULER.READYQUEUE.remove(0);
		SCHEDULER.BLOCKEDQUEUE.add(temp2);
		
		System.out.println("BQ:Blocked : " + SCHEDULER.BLOCKEDQUEUE.size());
		System.out.println("BQ:Ready : " + SCHEDULER.READYQUEUE.size());
		
		SCHEDULER.BLOCKEDQUEUE.get(0).setClock(20);
		
		temp3 = SCHEDULER.BLOCKEDQUEUE.get(0);
		SCHEDULER.BLOCKEDQUEUE.remove(0);
		SCHEDULER.READYQUEUE.add(temp3);
		
		if (SCHEDULER.READYQUEUE.size() == 0) {
			System.out.println("No more Jobs");
			// System.exit(0);
		} else {
			CPU.INSTRUCTIONS(SCHEDULER.READYQUEUE.get(0).getPc());
		}

	}// End of PUT_PROCESS_IN_BLOCK_QUEUE
	
	public static void NEXT_JOB() throws FileNotFoundException {
		
		SCHEDULER.READYQUEUE.remove(0);
		
		
		if (SCHEDULER.READYQUEUE.size() == 0) {
			System.out.println("No more Jobs");
			System.exit(0);
		} else {
			CPU.INSTRUCTIONS(SCHEDULER.READYQUEUE.get(0).getPc());
		}

	}// End of NEXT_JOB
}
