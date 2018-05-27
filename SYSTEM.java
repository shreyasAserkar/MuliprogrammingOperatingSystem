import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.*;

/**
 *@author : Shreyas Aserkar
 * 
 *Course number : CS-5323
 * 
 *Programming Assignment - Phase 2
 * 
 *Date : 05/01/2018
 * 
 *Class : SYSTEM
 *
 *The global variables used in phase 2 are :
 *output,traceFile,outputFileName,traceFileName - output and Trace file
 *objects formatChecker- To check if the format is correct or not
 *tempList,wordsInDisk,DISK - To store used job in DISK in page format
 * 
 *Description : This class acts as the driver to the program, it calls
 *the LOADER class to load the user input file to MEMORY, store the
 *initial information of current job to PCB and later gives a call to
 *CPU to compute the result by fetching, decoding and executing the
 *instructions until a HLT operation occurs. The input to the system is
 *a hexadecimal file which is converted into binary and stored into
 *memory by LOADER, and the output is the screen on which the resultant
 *value of the input is displayed.
 * 
 *
 */
public class SYSTEM {

	// Declaration for creation of output file and trace file
	
	static ArrayList<PCB> jobPCB = new ArrayList<PCB>();

	// Driver method, start the program by invoking DISK method,
	// calling LOADER and further initializing CPU routine for further
	// computations
	public static void main(String[] args) throws IOException {

		

		File userJob = new File(args[0]);
		if (!userJob.exists()) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR011");
		} else {

			EXECUTE_JOB(userJob);

		}

		SCHEDULER.READYQUEUE.get(0).getOutput().close();  

	} // end of main class

	//This method performs initiates the loading in DISK and calls loader to put the job in memory 
	private static void EXECUTE_JOB(File userJob) throws IOException {

		DISK diskObj = new DISK();
		SCHEDULER schObj = new SCHEDULER();
		
		// Calling method in DISK to put as many jobs in disk that can come in
		// MEMORY
		diskObj.LOAD_IN_DISK(userJob);

		PUT_JOBS_IN_MEMORY(diskObj.MAIN_DISK);
		
		schObj.SCHEDULING();
		
		

	}

	// Takes the jobs from the SPOOLED DISK and pass them to LOADER
	public static void PUT_JOBS_IN_MEMORY(ArrayList<String> diskJobs) throws IOException {

		LOADER loaderObject = new LOADER();
		DISK diskObj = new DISK();

		loaderObject.LOADERMANAGER(diskObj.MAIN_DISK);

	}

} // end of SYSTEM class
