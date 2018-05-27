import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class - LOADER
 * 
 * Description- This routine takes the input job from system and sends the words
 * to memory via memory buffer in form of pages. This class also keeps a track
 * of the Job id,Load address, initial PC , size and trace flag This method also
 * contains the FMBV , which will keep the count of number of frames that are
 * alloted to the job. This class also sends the information about the current
 * job to PCB Along with that, this class contains the method for SEGMENT FAULT
 * and PAGE FAULT to assign segments and pages when and where necessary.
 * 
 */

public class LOADER {

	static int count = 0; // NOT SURE WHAT IT IS FOR?
	static int[] FMBV = new int[32];
	static int jobStartIndex = 0;
	static int jobEndIndex = 0;
	static boolean firstPageFlag = false;
	
	// Reads each job line by line and send them to LOADERINPUTREADER for PCB
	// info
	public void LOADERMANAGER(ArrayList<String> spooledJobs) throws IOException {

		/*
		 * for(String s : SYSTEM.jobPCB.get(2).JOB){ System.out.println(s);
		 * //JUST A JOB INFO FETCH }
		 */
		// System.out.println(DISK.jobCount);

		for (int i = 0; i < DISK.jobCount; i++) {
			LOADERINPUTREADER(SYSTEM.jobPCB.get(i));
		}
		FIRSTPAGE_TO_MEMORY(SYSTEM.jobPCB.get(0).getPc());
		// CALL CPU for the rest of the Stuff

		/*
		 * for(int i = 0; i< MEMORY.mem.length;i++){ System.out.println(); //
		 * PRINTING MEMORY for(int j=0 ;j<MEMORY.mem[i].length;j++){
		 * System.out.print(MEMORY.mem[i][j]); } }
		 */
	}

	// This method loads the first page of the first job in the memory
	public void FIRSTPAGE_TO_MEMORY(int prgcounter) throws FileNotFoundException {

		int pc = prgcounter;
		int newPage;
		int displacement;

		firstPageFlag = true;
		
		if (pc > 127) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR006");
		}
		newPage = Math.floorDiv(pc, 8);
		displacement = Math.floorMod(pc, 8);
		SYSTEM.jobPCB.get(0).setRef(newPage, 1);
		SYSTEM.jobPCB.get(0).setPageTable0(newPage,
				SYSTEM.jobPCB.get(0).getPF().get(SYSTEM.jobPCB.get(0).getPageFrameCounter()));
		SYSTEM.jobPCB.get(0).setValid(newPage, 1);
		SYSTEM.jobPCB.get(0).setPageFrameCounter();
		LOADER.HEX_BINARY(SYSTEM.jobPCB.get(0).getCurrentJobPages().get(newPage));
		firstPageFlag = false;

	}// End of FIRSTPAGE_TO_MEMORY method

	// This method reads the input file from the system
	public void LOADERINPUTREADER(PCB pcbObj) throws IOException {

		int fmbvCounter = 0;
		/*PrintWriter output;
		PrintWriter traceFile;
		String outputFileName = "output_" + DISK.jobCount + "_" + pcbObj.getJobid() + ".txt";
		String traceFileName = "trace_file_" + DISK.jobCount + "_" + pcbObj.getJobid() + ".txt";
		output = new PrintWriter(new FileWriter(outputFileName));

		pcbObj.setOutput(output);*/
		// Program Segment PMT
		ArrayList<String> pageTable0 = new ArrayList<String>(pcbObj.getCurrentJobPages().size());

		pcbObj.setRef(pcbObj.getCurrentJobPages().size());
		pcbObj.setValid(pcbObj.getCurrentJobPages().size());

		for (int i = 0; i < pcbObj.getCurrentJobPages().size(); i++) {
			pageTable0.add("0");
		}
		pcbObj.setPageTable0(pageTable0);
		pcbObj.setPageTable1(pcbObj.getIpDataSegement());
		pcbObj.setPageTable2(pcbObj.getOpDataSegement());
		for (int i = 0; i < pcbObj.getInputSize(); i++) {
			pcbObj.setPageTable1("Nil");
		}
		for (int i = 0; i < pcbObj.getOutputSize(); i++) {
			pcbObj.setPageTable2("Nil");
		}
		pcbObj.setPageFrame_inPCB(Math.min(6, pcbObj.getCurrentJobPages().size() + 2));

		PCB(pcbObj.getCurrentJobPages(), pcbObj);

		INITIALIZE_PMT(pcbObj);

		for (int i = 0; i < FMBV.length; i++) {
			if (FMBV[i] == 0 && fmbvCounter < pcbObj.getPageFrame_inPCB()) {
				FMBV[i] = 1;
				pcbObj.setPF(fmbvCounter, Integer.toString(i));
				fmbvCounter++;
			}
		}

	} // end of LOADER method

	// This method implements loader buffer of 4 words and sent to
	// memory manager
	public static void HEX_BINARY(String s) throws FileNotFoundException {

		int length = s.length();
		if (!(length % 4 == 0)) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR010");
		}

		// Convert 8 words to 16 bits binary each and storing in memory
		String[] word = new String[8];
		int[] hexValue = new int[8];
		String[] hexTobinary = new String[8];
		String binaryString = "";
		String temp;
		int index = 0;

		for (int i = 0; i < word.length; i++) {
			word[i] = s.substring(index, index + 4);
			// wordCount++;
			SYSTEM.jobPCB.get(0).setWordCount();
			index += 4;

			try {
				hexValue[i] = (Integer.parseInt(word[i], 16));

				hexTobinary[i] = Integer.toBinaryString(hexValue[i]);
				while (hexTobinary[i].length() < 16) {
					hexTobinary[i] = "0" + hexTobinary[i];
				}

			} catch (Exception E) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR009");
			}
		}
		for (int i = 0; i < word.length; i++) {
			temp = hexTobinary[i];
			binaryString = binaryString + temp;
		}
		if(firstPageFlag ==true){
		MEMORY.MEMORY_MANAGER(binaryString,
				Integer.parseInt(SYSTEM.jobPCB.get(0).getPF().get(SYSTEM.jobPCB.get(0).getPageFrameCounter() - 1)));
		}
		else{
			MEMORY.MEMORY_MANAGER(binaryString,
					Integer.parseInt(SCHEDULER.READYQUEUE.get(0).getPF().get(SCHEDULER.READYQUEUE.get(0).getPageFrameCounter() - 1)));
		}
		count++;

	} // end of hexToBin method

	// This method takes the input of the job from the Input Segment
	// of the job
	public static void HEX_BINARY_INPUT(String s) throws FileNotFoundException {

		int length = s.length();
		if (!(length % 4 == 0)) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR010");
		}

		String[] word = new String[1];
		int[] hexValue = new int[1];
		String[] hexTobinary = new String[1];
		String binaryString = "";
		String temp;
		int index = 0;

		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		for (int i = 0; i < word.length; i++) {
			word[i] = s.substring(index, index + 4);
			// wordCount++;
			pcb.setWordCount();
			index += 4;

			try {
				hexValue[i] = (Integer.parseInt(word[i], 16));

				hexTobinary[i] = Integer.toBinaryString(hexValue[i]);
				while (hexTobinary[i].length() < 16) {
					hexTobinary[i] = "0" + hexTobinary[i];
				}

			} catch (Exception E) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR009");
			}
		}
		for (int i = 0; i < word.length; i++) {
			temp = hexTobinary[i];
			binaryString = binaryString + temp;
		}
		MEMORY.MEMORY_MANAGER(binaryString, Integer.parseInt(pcb.getPF().get(pcb.getPageFrameCounter() - 1)));

	} // end of hexToBin_Input method

	// This method sets the PCB information in the PCB class
	// for the program to use it later
	public static void PCB(ArrayList<String> pages, PCB pcbObj) {

		pcbObj.setSMT(0, Integer.parseInt(pcbObj.getPageTable0().get(0)));
		pcbObj.setSMT(1, pages.size());
		pcbObj.setSMT(2, pages.size() + pcbObj.getInputSize());
		for (int i = 0; i < pcbObj.getPageFrame_inPCB(); i++) {
			pcbObj.setPF("Nil");
		}

		// adds each pcb object to SCHEDULER's ArrayList
		// SCHEDULER.data.add(pcbObj);
		// System.out.println(SCHEDULER.data.get(0).jobid);
	} // end of PCB method

	// This method initializes PMT and replaces all zeros with NIL
	public static void INITIALIZE_PMT(PCB pcbObj) {

		for (int i = 0; i < pcbObj.getPageTable0().size(); i++) {
			pcbObj.getPageTable0().set(i, "Nil");
		}

	} // end of PMT method

	// This method handles the Segment Fault for Input and
	// Output Segment
	public static void SEGEMENT_FAULT_HANDLER(int index) throws FileNotFoundException {
		int inputMemorySize;
		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		pcb.setClock(20);
		pcb.setSegmentFaultTime();
		if (index == 1) {
			
			if (pcb.getPageFrameCounter() == pcb.PF.size()) {
				// pageFrameCounter = 0;
				pcb.setPageFrameCounter(0);
			}
			
			for (int i = 0; i < pcb.getPageTable0().size(); i++) {
				String temp = pcb.getPageTable0().get(i);
				if (temp.equals(pcb.getPF().get(pcb.getPageFrameCounter()))) {
					// if (ref[i] == 0) {
					if (pcb.getRef()[i] == 0) {
						// valid[i] = 0;
						pcb.setValid(i, 0);
					} else {
						// pageFrameCounter++;
						pcb.setPageFrameCounter();
						// ref[i] = 0;
						pcb.getRef()[i] = 0;

					}

				}
			}
			//If the page is not in memory
			pcb.setPageTable1(pcb.getSegmentCounter(), pcb.getPF().get(pcb.getPageFrameCounter()));
			pcb.setPageFrameCounter();
			String input = pcb.getInputList().get(pcb.getSegmentCounter());
			if(input.length()==4){
			
			inputMemorySize = input.length() / 4;
			
			HEX_BINARY_INPUT(input);
			
			//Put the process in BLOCKED QUEUE if Input Segment Fault is occurring first time 
			SCHEDULER.PUT_PROCESS_IN_BLOCK_QUEUE();
			}else{
				//If the page already exists 
				if(input.length() % 4 ==0){
					
					String[] parts = input.split("(?<=\\G....)");
					HEX_BINARY_INPUT(parts[pcb.getIpMoreThanOne()]);
					pcb.setIpMoreThanOne();
					
				}else{
					
					//Input mismatch (Not of length 4 or not the same number as mentioned in job info)
					 SCHEDULER.READYQUEUE.get(0).setInputMemorySize(pcb.getInputMemorySize());
					 if (!(SCHEDULER.READYQUEUE.get(0).getInputMemorySize() ==
					 SCHEDULER.READYQUEUE.get(0) .getIpDataSegement())) {
					 ERROR_HANDLER error = new ERROR_HANDLER();
					 ERROR_HANDLER.errorCode("ERROR024"); }
					 
				}
			}
			
			
		} else if (index == 2) {
			if (pcb.getPageFrameCounter() == pcb.PF.size()) {
				// pageFrameCounter = 0;
				pcb.setPageFrameCounter(0);
			}
			
			for (int i = 0; i < pcb.getPageTable0().size(); i++) {
				String temp = pcb.getPageTable0().get(i);
				if (temp.equals(pcb.getPF().get(pcb.getPageFrameCounter()))) {
					// if (ref[i] == 0) {
					if (pcb.getRef()[i] == 0) {
						// valid[i] = 0;
						pcb.setValid(i, 0);
					} else {
						// pageFrameCounter++;
						pcb.setPageFrameCounter();
						// ref[i] = 0;
						pcb.getRef()[i] = 0;

					}

				}
			}
			
			pcb.setPageTable2(pcb.getPt2Count(), pcb.getPF().get(pcb.getPageFrameCounter()));
			// pageFrameCounter++;
			pcb.setPageFrameCounter();
			pcb.setPt2Count();
			if(pcb.getPt2Count()>2){
				pcb.setPt2Count(0);
			}
			//Put the process in BLOCKED QUEUE if output Segment Fault is occurring first time 
			SCHEDULER.PUT_PROCESS_IN_BLOCK_QUEUE();
		}

	} // end of segment fault method

	// This method handles the PAGE FAULT and replaces NIL with
	// the next available pageframe from PCB
	public static void PAGE_FAULT_HANDLER(int newPage) {

		// CPU.clock = CPU.clock + 20;
		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		pcb.setClock(20);
		pcb.setPageFaultTime();

		// if (flag == true) {
		if (pcb.isFlag() == true) {
			if (pcb.getPageFrameCounter() == pcb.PF.size()) {
				// pageFrameCounter = 0;
				pcb.setPageFrameCounter(0);
			}
			for (int i = 0; i < pcb.getPageTable0().size(); i++) {
				String temp = pcb.getPageTable0().get(i);
				if (temp.equals(pcb.getPF().get(pcb.getPageFrameCounter()))) {
					// if (ref[i] == 0) {
					if (pcb.getRef()[i] == 0) {
						// valid[i] = 0;
						pcb.setValid(i, 0);
					} else {
						// pageFrameCounter++;
						pcb.setPageFrameCounter();
						// ref[i] = 0;
						pcb.getRef()[i] = 0;

					}

				}
			}
			pcb.setPageTable0(newPage, pcb.getPF().get(pcb.getPageFrameCounter()));
			// valid[newPage] = 1;
			pcb.setValid(newPage, 1);
			// pageFrameCounter++;
			pcb.setPageFrameCounter();

		} else {
			pcb.setPageTable0(newPage, pcb.getPF().get(pcb.getPageFrameCounter()));
			// valid[newPage] = 1;
			pcb.setValid(newPage, 1);
			// pageFrameCounter++;
			pcb.setPageFrameCounter();
			if (pcb.getPageFrameCounter() == pcb.PF.size()) {
				// flag = true;
				pcb.setFlag(true);
			}

		}

	} // end of page Fault method

} // end of LOADER main class