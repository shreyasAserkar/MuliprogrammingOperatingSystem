import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ Class: DISK
 * 
 * This class reads the userJob and spools as many programs as can be
 * accommodated in MEMORY. This approach works as, the Loader format is divided
 * into pages and it compares the no of pages allowed to be allocated to current
 * program to the available memory. If there is enough space in the MEMORY, that
 * particular Job is spooled onto the DISK for further use.
 */

public class DISK {

	// This Disk contains the Jobs as much as can come in Memory
	static ArrayList<String> MAIN_DISK = new ArrayList<String>();
	static SYSTEM sysObj = new SYSTEM();
	static int segmentZeroSize = 0;
	static int cntStartIndex = 0;
	static int cntEndIndex = 0;
	static int finCount = 10000;
	static int jobCount = 0;

	public void LOAD_IN_DISK(File userJob) throws IOException {

		ArrayList<String> currentJob = new ArrayList<String>();
		ArrayList<String> currentJobPages = new ArrayList<String>();
		ArrayList<String> tempList = new ArrayList<String>();
		ArrayList<String> inputList = new ArrayList<String>();
		ArrayList<String> outputList = new ArrayList<String>();
		
		LOADER loaderObject = new LOADER();
		PCB pcbObj = new PCB();
		int ioSize ;
		jobCount++;
		SYSTEM.jobPCB.add(pcbObj);
		pcbObj.setExternalJob(jobCount);

		DISK(userJob, currentJob,pcbObj);									//UNPAGED SINGLE JOB
		
		if(!currentJob.isEmpty()){
			pcbObj.setCurrentJob(currentJob);
			ioSize = ZERO_LINE_INFO(currentJob,pcbObj);
			pcbObj.setIoSize(ioSize);
			HEX_TO_BIN_FIRST_LINE(currentJob,pcbObj);
			
			PrintWriter output;
			PrintWriter traceFile;
			String outputFileName = "output_" + jobCount + "_" + pcbObj.getJobid() + ".txt";
			String traceFileName = "trace_file_" + DISK.jobCount + "_" + pcbObj.getJobid() + ".txt";
			output = new PrintWriter(new FileWriter(outputFileName));

			pcbObj.setOutput(output);
			
		for (int i = 2; i < currentJob.size() - (pcbObj.getInputSize()+2); i++) {					    
			tempList.add(currentJob.get(i)); 									// CONTAINS ONLY LOADER UNPAGED
		}
		currentJobPages = PAGING(tempList);
		pcbObj.setCurrentJobPages(currentJobPages);
		
		NUMBER_OF_INPUTS(pcbObj,inputList);
		
		JOB(pcbObj);
		
		
		
		// Checks if the Memory has enough space to spool that many jobs in DISK
		float temp = tempList.size();
		float temp2 = (temp / 2);
		int pagesInMemory = (int) Math.ceil(temp2);

		//System.out.println("Size: " + tempList.size() + " Pages: " + pagesInMemory);

		int pagesToMemory = Math.min(6, pagesInMemory + 2);

		if (pagesToMemory < MEMORY.isAvailable) { 	// CONDITION TO CHECK IF THERE IS SPACE IN MEMORY  DO THE NECESSARY WORK OF ADDING userJob TO DISK FOR FURTHER REFERENC
			DISK_SPOOL(pcbObj.getJOB());
			MEMORY.isAvailable = MEMORY.isAvailable - pagesToMemory;
			//System.out.println("Now available Pages " + MEMORY.isAvailable);
			LOAD_IN_DISK(userJob);
		}

		}
	}

	private void JOB(PCB pcbObj) {
		
		ArrayList<String> JOB = new ArrayList<String>();
		
		JOB.addAll(pcbObj.getCurrentJobPages());
		JOB.addAll(pcbObj.getInputList());
		for(int i=0;i<pcbObj.getOutputSize();i++){
			JOB.add("NULL");
		}
		
		pcbObj.setJOB(JOB);
	}

	private void NUMBER_OF_INPUTS(PCB pcbObj, ArrayList<String> inputList) {
		
		int size = pcbObj.getSize();
		if(size %2==0){
		for (int i = 3+(pcbObj.getSize()/4) ; i < (3+(pcbObj.getSize()/4)+pcbObj.getInputSize()); i++) {					    
			inputList.add(pcbObj.getCurrentJob().get(i)); 									
		}
		}else{
			for (int i = 4+(pcbObj.getSize()/4) ; i < (4+(pcbObj.getSize()/4)+pcbObj.getInputSize()); i++) {					    
				inputList.add(pcbObj.getCurrentJob().get(i)); 	
		}
		}
		pcbObj.setInputList(inputList);
		
	}

	public int ZERO_LINE_INFO(ArrayList<String> currentJob, PCB pcbObj) throws FileNotFoundException {
	String[] zeroLine;
	 	
	 	int inputSize = 0;
	 	int outputSize = 0;
	 	
	 	zeroLine = (currentJob.get(0)).split(" ");
	   if (!zeroLine[0].equals("**JOB")) {
	    ERROR_HANDLER error = new ERROR_HANDLER();
	    ERROR_HANDLER.errorCode("ERROR015");
	   } 
	  
	   inputSize = Integer.parseInt(zeroLine[1]);
	   pcbObj.setInputSize(inputSize);
	   outputSize = Integer.parseInt(zeroLine[2]);
	   pcbObj.setOutputSize(outputSize);
	   
	 return inputSize+outputSize;
	}

	
	// This method keeps a count of the firstline of the user job
		public void HEX_TO_BIN_FIRST_LINE(ArrayList<String> currentJob, PCB pcbObj) throws IOException {
			String value;
			int jobId;
			int hexValue;
			String hexValueInt;
			String[] firstLine =  (currentJob.get(1)).split(" ");

			try {
				value = firstLine[0];
				hexValue = (Integer.parseInt(value, 16));
				hexValueInt = Integer.toBinaryString(hexValue);
				jobId = Integer.parseInt(hexValueInt, 2);
				pcbObj.setJobid(Integer.toString(jobId));
				//SYSTEM.output.println("Job ID: " + jobId + " (DEC)");

				value = firstLine[1];
				hexValue = (Integer.parseInt(value, 16));
				hexValueInt = Integer.toBinaryString(hexValue);
				pcbObj.setLoadAddress(Integer.parseInt(hexValueInt));

				value = firstLine[2];
				hexValue = (Integer.parseInt(value, 16));
				hexValueInt = Integer.toString(hexValue);
				pcbObj.setPc(Integer.parseInt(hexValueInt));

				value = firstLine[3];
				hexValue = (Integer.parseInt(value, 16));
				hexValueInt = Integer.toBinaryString(hexValue);
				pcbObj.setSize(Integer.parseInt(hexValueInt, 2));
				int size = pcbObj.getSize();
				if (size > 128) {
					ERROR_HANDLER error = new ERROR_HANDLER();
					ERROR_HANDLER.errorCode("ERROR005");
				}
				
				try {
					value = firstLine[4];
					hexValue = (Integer.parseInt(value, 16));
					hexValueInt = Integer.toBinaryString(hexValue);
					pcbObj.setTraceFlag(Integer.parseInt(hexValueInt));
					int traceFlag = pcbObj.getTraceFlag();
					if (traceFlag == 1) {
						//SYSTEM.traceFile = new PrintWriter(new FileWriter(SYSTEM.traceFileName));
					}
				} catch (Exception e) {
					ERROR_HANDLER error = new ERROR_HANDLER();
					ERROR_HANDLER.errorCode("ERROR004");
				}

			} catch (Exception e) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR012");
			}

		} // end of HEX_TO_BIN_FIRST_LINE class
	
	
	
	
	
	
	//SPOOLS the job to the DISK 
	private void DISK_SPOOL(ArrayList<String> wordsInDisk) {

		MAIN_DISK.addAll(wordsInDisk);
	}

	private static void DISK(File userJob, ArrayList<String> currentJob, PCB pcbObj) throws IOException {

		ArrayList<String> formatChecker = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(userJob));
		// To read the line of the userJob which are already read

		//pcbObj.setPreStartIndex(cntStartIndex);
		
		try {
			for (int i = 0; i < cntStartIndex; i++) {
				br.readLine();
			}
		} catch (Exception e) {

		}
		String line = br.readLine();

		if (line != null) {

			//sysObj.PUT_JOBS_IN_MEMORY(MAIN_DISK); // WHEN THERE ARE NO MORE JOBS
												// IN THE DISK
			
		// Read the specific program when provided with start and end index
		while (line != null && cntStartIndex >= 0 && cntEndIndex < finCount) {
			if (currentJob.size() < 512) {

				if (line.contains("**JOB") || line.contains("**INPUT") || line.contains("**FIN")) {
					if (line.contains("**JOB")) {
						formatChecker.add(line.substring(0, 5));
					} else {
						formatChecker.add(line);
					}
				}
				cntStartIndex++;
				cntEndIndex++; // TO COUNT NO OF LINES IN I/P FILE
				if (line.contains("**FIN")) {
					finCount = cntEndIndex; //
				}
				currentJob.add(line); // FULL UNPAGED JOB
				line = br.readLine();
			} else {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR014");
			}
		}
		finCount = 10000;
		// Checks for the correct format of the user job
		CHECK_INPUT_FORMAT(formatChecker);
		
		if(!currentJob.isEmpty()){
			pcbObj.setCurrentJob(currentJob);
		}
		}else{
			jobCount--;
			SYSTEM.jobPCB.remove(jobCount);
		}

	} // end of DISK method

	// This method check if the format of the user job is correct
	public static void CHECK_INPUT_FORMAT(ArrayList<String> formatChecker) throws FileNotFoundException {

		String str = "";
		for (int i = 0; i < formatChecker.size(); i++) {
			str = str + formatChecker.get(i);
		}

		String checkJob = "**JOB";
		String checkInput = "**INPUT";
		String checkFin = "**FIN";

		int checkJobCount = COUNT(checkJob, str);
		int checkInputCount = COUNT(checkInput, str);
		int checkFinCount = COUNT(checkFin, str);

		if (checkJobCount == 0) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR015");
		} else if (checkJobCount > 1) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR017");
		} else if (checkInputCount == 0) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR020");
		} else if (checkInputCount > 1) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR021");
		} else if (checkFinCount == 0) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR022");
		} else if (checkFinCount > 1) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR023");
		}

	} // End of CHECK_INPUT_FORMAT method

	// This method counts the occurrence of the **JOB, **INPUT, **FIN
	public static int COUNT(String findStr, String str2) {
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {
			lastIndex = str2.indexOf(findStr, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	} // End of the COUNT method

	// Performs paging of Job
		public ArrayList<String> PAGING(ArrayList<String> currentJob) {

			ArrayList<String> tempPaging = new ArrayList<String>();
			int count = 0;
			String strngToAdd = "";
			int length = 0;
			if (currentJob.size() % 2 == 0) {
				length = currentJob.size() / 2;
			} else {
				length = currentJob.size() / 2 + 1;
			}
			for (int i = 0; i < length; i++) {
				if ((count + 1) == currentJob.size()) {
					strngToAdd = currentJob.get(count);
				} else {
					strngToAdd = currentJob.get(count) + currentJob.get(count + 1);
				}

				tempPaging.add(strngToAdd); // CONTAINS PAGES INFORMATION

				count = count + 2;
			}
			return tempPaging;

		}
		
}