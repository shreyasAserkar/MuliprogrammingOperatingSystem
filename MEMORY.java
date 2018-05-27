import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
/**
 *Class - MEMORY
 *
 *Description - This routine is the Memory for the system, 
 * It stores the input job in binary format of pages of 8 words
 * with 16 bit each and the size of the memory can hold 32 pages.
 *All the instructions that are store here are later fetched 
 * and stored again by CPU.
 *
 */
public class MEMORY {

 //The size of the memory with 8 words each
 static int[][] mem = new int[32][128];
 //static PCB pcb = new PCB();
 static HashSet < Integer > set = new HashSet < Integer > ();													  //NOT SURE WHAT IT IS FOR?
 static int isAvailable = 32;


 /*This method accepts the values from the LOADER buffer and
 store them into memory array*/
 public static void MEMORY_MANAGER(String binaryWord, int index)
 throws FileNotFoundException {
  String[] ary = binaryWord.split("");
  int arr[] = new int[128];

  for (int i = 0; i < ary.length; i++) {
   arr[i] = Integer.parseInt(ary[i]);
  }
  MEMORY1(arr, index);
 } // end of MEMORY_MANAGER method


 /*This method store each instruction into the DISK line
 by line*/
 public static void MEMORY1(int[] arr, int index) throws
 FileNotFoundException {
  try {
   mem[index] = arr;
  } catch (Exception e) {
   ERROR_HANDLER error = new ERROR_HANDLER();
   ERROR_HANDLER.errorCode("ERROR005");
  }

 } // End of MEMORY1 method

 //This method gives the Physical address if instruction from 
 //PC which is divided into Page and displacement
 public static int[] GET_PHYSICALADDRESS(int frameNumber, int 
		 displacement) throws FileNotFoundException {
   int[] instruction = new int[16];
   int count = 0;
   if (displacement > 7) {
    ERROR_HANDLER error = new ERROR_HANDLER();
    ERROR_HANDLER.errorCode("ERROR012");
   } else {

    for (int j = displacement * 16; j < (displacement * 16) + 16; j++) {
     instruction[count] = mem[frameNumber][j];
     count++;
    }
   }
   set.add(frameNumber);
   return instruction;

  } // End of GET_PHYSICALADDRESS method

 //This method updates the Effective address in the memory at 
 //the particular location
 @SuppressWarnings("null")
 public static void DIRTYBIT_UPDATE_EA(int newPageEAtoMemory,
		 int[] newArray, String string, int displacementToValue) {


   int size = Integer.parseInt(string);
   int count = 0;
   String toDisk = "";
   int index = 0;
   String temp;
   for (int j = displacementToValue * 16; j < (displacementToValue
		   * 16) + 16; j++) {
    mem[size][j] = newArray[count];
    count++;
   }

   //Writing the DIRTY BIT changes in DISK
   StringBuilder strNum = new StringBuilder();

   for (int num: mem[size]) {
    strNum.append(num);
   }
   String finalStr = strNum.toString();

   for (int i = 0; i < (finalStr.length() / 16); i++) {
    temp = finalStr.substring(index, index + 16);
    index += 16;
    int a = Integer.parseInt(temp, 2);
    String b = Integer.toHexString(a);
    while (b.length() < 4) {
     b = "0" + b;
    }
    toDisk = toDisk + b;

   }
  // SYSTEM.DISK.set(newPageEAtoMemory, toDisk);
   DISK.MAIN_DISK.set(newPageEAtoMemory, toDisk);														//NOT SURE 
  } //end of Update EA method 

 //This method writes the output written in page to the memory and
 //to DISK
 public static void OUTPUT_TO_DISK(int pageNumber, int output) {

   String binaryString = "";
   String temp = Integer.toBinaryString(output);
   if (temp.length() > 16) {
    temp = temp.substring(temp.length() - 16);
   }
   int[] outputArray = new int[temp.length()];
   for (int i = 0; i < temp.length(); i++) {
    outputArray[i] = temp.charAt(i) - '0';
   }
   for (int j = 0; j < outputArray.length; j++) {
    mem[pageNumber][j] = outputArray[j];
   }

   for (int j = 0; j < outputArray.length; j++) {
    binaryString = binaryString + mem[pageNumber][j];
   }

   int answer = Integer.parseInt(binaryString, 2);
   if (output < 0) {
    answer = answer - 65536;
   }

   PCB pcb = SCHEDULER.READYQUEUE.get(0);
   pcb.getOutput().print("Job ID (DEC): " + pcb.getJobid() + " Output (DEC): ");
   pcb.getOutput().print(answer);
   pcb.getOutput().println();

   //Writes the output in the DISK
   //wordsInDisk.add(temp);																					YET TO WRITE >>>>>IMP<<<<<<<

  } //end of OUTPUT_TO_DISK method 

} // end of MEMORY class