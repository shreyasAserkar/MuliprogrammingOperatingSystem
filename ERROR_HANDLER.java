import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
Class - ERROR_HANDLER

Description - This class maintains all the Errors that have been 
mentioned in the CPU routine, The CPU send the error code to this
 class, upon which it displays the respective error and also print 
 them to the output file for further reference.
The Errors are categorized into FATAL,Error,Warning. While the FATAL
 error stops the program, warning doesn't stop the program but
  displays the message on the screen and writes to the output file. 

*/
public class ERROR_HANDLER {

 //Integer to keep a count of the number of errors or warnings.
 static int errorCount;																			//CHANGE TO PCB OBJ
 static String errorMessage;																	//CHANGE TO PCB OBJ

 //This method return the error message to be displayed on the 
 //screen and also write it to output file.
 public static void errorCode(String error) throws
 FileNotFoundException {
	 			
  if (error.equals("ERROR001")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println();
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR001: EmptyStackException.");
   errorMessage = "EmptyStackException ";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;

  } else if (error.equals("ERROR002")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println();
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR002: StackOverflowError.");
   errorMessage = " StackOverflowError ";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;

  } else if (error.equals("ERROR003")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR003: Illegal input entered.");
   errorMessage = "Illegal input entered ";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   SCHEDULER.NEXT_JOB();
   errorCount++;

  } else if (error.equals("ERROR004")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println();
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Warning: Trace flag is not set. \n");
   errorMessage = "Trace flag is not set";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;

  } else if (error.equals("ERROR005")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR005: OutOfMemoryError Exception.");
   OUTPUT_DATA();
   errorMessage = "OutOfMemoryError Exception ";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   SCHEDULER.NEXT_JOB();
   errorCount++;

  } else if (error.equals("ERROR006")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Warning: Suspected infinite loop.");
   errorMessage = " Suspected infinite loop.";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   SCHEDULER.NEXT_JOB();
   errorCount++;

  } else if (error.equals("ERROR007")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR007: FATAL Error - Address "
   		+ "out of range. Address:" + SCHEDULER.READYQUEUE.get(0).getDADDR() + "(DEC)");
   OUTPUT_DATA();
   errorMessage = "Address out of range.";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR008")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR008: FATAL Error -Illegal "
   		+ "Argument" + " exception - divide by zero error");
   errorMessage = "divide by zero error";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR009")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR009: FATAL Error - Number"
   		+ " format Exception.");
   OUTPUT_DATA();
   errorMessage = "Number format Exception.";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR010")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR010: FATAL Error - "
   		+ "Inappropriate Loader format, Length of word too short.");
   errorMessage = "Inappropriate Loader format, Length of word too short.";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR011")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR011: FATAL Error - "
   		+ "System is expecting an " + "input.");
   OUTPUT_DATA();
   errorMessage = "System is expecting an input.";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR012")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR012: FATAL Error - Invalid"
   		+ " Loader format");
   OUTPUT_DATA();
   errorMessage = "Invalid Loader format";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR014")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR014: FATAL Error - DISK size "
   		+ "exceeded.");
   errorMessage = " DISK size exceeded.";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR015")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR015: FATAL Error - **JOB "
   		+ "missing exception,no JOB id found");
   OUTPUT_DATA();
   errorMessage = "**JOB missing";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR016")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR016: FATAL Error - Loader"
   		+ " size mismatch error");
   OUTPUT_DATA();
   errorMessage = "Loader size mismatch error, Loader size is diferent "
   		+ "from the size mentioned.";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR017")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR017: FATAL Error - Multiple "
   		+ "**JOB found");
   OUTPUT_DATA();
   errorMessage = "**JOB Format mismatch,more than one job id found";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR020")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR017: FATAL Error - missing "
   		+ "**INPUT,no input found. ");
   OUTPUT_DATA();
   errorMessage = "**INPUT mismatch error,no Input found";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR018")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR018: FATAL Error - Multiple "
   		+ "**FIN found ");
   OUTPUT_DATA();
   errorMessage = "**FIN mismatch error";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR019")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR019: FATAL Error - Loader "
   		+ "Format wrong");
   OUTPUT_DATA();
   errorMessage = "Loader Format wrong";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR021")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR021: FATAL Error - Multiple "
   		+ "**INPUT found");
   OUTPUT_DATA();
   errorMessage = "**INPUT mismatch error";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR022")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR022: FATAL Error -**FIN"
   		+ " missing, no **FIN found ");
   OUTPUT_DATA();
   errorMessage = "**FIN misssing, no **FIN found";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR023")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR023: FATAL Error -Multiple"
   		+ " **FIN found ");
   OUTPUT_DATA();
   errorMessage = "Multiple **FIN found ";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();

  } else if (error.equals("ERROR024")) {
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("Abnormal Termination \n");
	  SCHEDULER.READYQUEUE.get(0).getOutput().println("ErrorCode ERROR024: FATAL Error -Input"
   		+ " conflict error.");
   OUTPUT_DATA();
   errorMessage = "FATAL Error -Input conflict error";
   SCHEDULER.READYQUEUE.get(0).getOutput().flush();
   errorCount++;
   SCHEDULER.NEXT_JOB();
  }


 } //end of errorCode Method

 //This method output the execution times before the program error occurred
 public static void OUTPUT_DATA() {

  /*SYSTEM.output.println();
  try {
   SYSTEM.output.println("CLOCK (HEX): " + Integer.toHexString(CPU.clock));
   SYSTEM.output.println();
  } catch (Exception e) {
   SYSTEM.output.println("CLOCK (HEX): 0");
   SYSTEM.output.println();
  }
  try {
   SYSTEM.output.println("Runtime(DEC): " + (CPU.clock - CPU.ioClock -
    CPU.segmentFaultTime - CPU.pageFaultTime) + " vtu");
   SYSTEM.output.println("Execution time(DEC): " + CPU.clock + " vtu");
   SYSTEM.output.println("I/O time(DEC): " + CPU.ioClock + " vtu");
   SYSTEM.output.println("Segment Fault Time(DEC): " + CPU.
		   segmentFaultTime + " vtu");
   SYSTEM.output.println("Page Fault Time(DEC): " + 
		   CPU.pageFaultTime + " vtu");
  } catch (Exception e) {
   SYSTEM.output.println("Runtime(DEC): NA vtu");
   SYSTEM.output.println("Execution time(DEC): NA vtu");
   SYSTEM.output.println("I/O time(DEC): NA vtu");
   SYSTEM.output.println("Segment Fault Time(DEC): NA vtu");
   SYSTEM.output.println("Page Fault Time(DEC): NA vtu");
   SYSTEM.output.println();
  }

  try {
   float wordCount = LOADER.wordCount;
   float totalMemoryWords = SYSTEM.segmentZeroSize * 4 + LOADER.
   ipDataSegement + LOADER.opDataSegement;
   float memUsage = wordCount * 100 / totalMemoryWords;
   float pageFrameSize = MEMORY.mem.length * 8;
   float memUsage1 = wordCount * 100 / pageFrameSize;
   SYSTEM.output.println("\nMemory Utilization (DEC) : " +
    (int) wordCount + ":" + (int) pageFrameSize + " or " +
    memUsage1 + " %");
   SYSTEM.output.println("\nDisk Utilization (DEC): " + 
    wordsInDisk.size() + ":" + "2048 or " + (float)
    (wordsInDisk.size() * 100 / 2048) + " %");									// NEED TO BE DONE
   float unusedSegment1 = 8 - LOADER.inputMemorySize;
   float unusedSegment2 = 8 - CPU.opCount;
   float unusedSegment3 = ((LOADER.pcbObj.getPF().get(LOADER
		   .pageFrameCounter - 1)).length() - 1);
   float total1 = (unusedSegment1 + unusedSegment2 + unusedSegment3);
   float avg1 = (unusedSegment1 / 8 + unusedSegment2 / 8 +
    unusedSegment3 / 8);
   SYSTEM.output.println("\nMemory Fragmentation (DEC): " +
    (int) total1 + ":" + "24 or " + (float)(avg1 * 100 / 3) + " %");
   float unusedSegment4 = SYSTEM.tempList.size();
   SYSTEM.output.println("\nDISK Fragmentation (DEC): " +
    (int)(18 - unusedSegment4) + ":" + "2 or " + (float)((18 -
     unusedSegment4) * 100 / 2) + " %");
  } catch (Exception e) {
   SYSTEM.output.println("\nMemory Utilization (DEC) : NA: or NA %");
   SYSTEM.output.println("\nDisk Utilization (DEC): NA or NA %");
   SYSTEM.output.println("\nMemory Fragmentation (DEC): NA or NA %");
   SYSTEM.output.println("\nDISK Fragmentation (DEC):NA or NA %");
  }*/
 }
} //end of ERROR_HANDLER class