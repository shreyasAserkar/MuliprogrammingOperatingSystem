import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

/**
 * Class - CPU
 *
 * Description - This routine acts as a stack machine, where the instructions
 * are fetched,decoded and executed and are stored back into MEMORY The class
 * works in a way that it firstly categorizes the fetched binary instruction
 * from MEMORY into zero address or one address and then find the opcode and
 * EA(if one address instruction) Upon finding the opcode and EA , it does the
 * computations as mentioned and stores the result back into the memory and
 * later writes the data to the output file. Along with doing these computations
 * ,it also keeps a count of the Clock keeping, while being constantly
 * monitoring the size of the stack. All the results are output to output file
 * and store the (PC), (BR), (IR), (TOS) & (S[TOS]) before Execution, EA and
 * (EA) before Execution(if applicable), (TOS) & (S[TOS]) After Execution, EA
 * and (EA) After Execution(if applicable) to the TRACE file.
 */

public class CPU {

	static SCHEDULER schObj = new SCHEDULER();
	
	// This method categorizes if instruction is zero or one address
	public static void INSTRUCTIONS(int prgcounter) throws FileNotFoundException {

		// pc = prgcounter;
		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		System.out.println("Opcode"+pcb.jobid+" : "+pcb.opcode);
		pcb.setPc(prgcounter);
			
		String binaryStringbr = Integer.toBinaryString(pcb.getLoadAddress());
		while (binaryStringbr.length() < 4) {
			binaryStringbr = "0" + binaryStringbr;
		}
		// brList.add(binaryStringbr);
		pcb.setBrList(binaryStringbr);																//MIGHT GIVE A PROBLEM IN TRACE pcb.remove(size-1)

		if (pcb.getPc() > 127) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR006");
		}

		pcb.setNewPage(Math.floorDiv(pcb.getPc(), 8));
		pcb.setDisplacement(Math.floorMod(pcb.getPc(), 8));
		// LOADER.ref[newPage] = 1;
		pcb.setRef(pcb.getNewPage(), 1);

		// Looks into PMT at the specified page for the frame in MEMORY
		// ins = LOADER.pageTable0.get(newPage);
		pcb.setIns(pcb.getPageTable0().get(pcb.getNewPage()));
		if (pcb.getIns().equals("Nil")) {
			LOADER.PAGE_FAULT_HANDLER(pcb.getNewPage());
			// LOADER.HEX_BINARY(SYSTEM.DISK.get(newPage));
			LOADER.HEX_BINARY(pcb.getCurrentJobPages().get(pcb.getNewPage()));
			pcb.setInstr(MEMORY.GET_PHYSICALADDRESS(Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPage())),
					pcb.getDisplacement()));

		} else {

			pcb.setInstr(MEMORY.GET_PHYSICALADDRESS(Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPage())),
					pcb.getDisplacement()));

		}

		if (pcb.getStack().size() < 0) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR001");
		} else if (pcb.getStack().size() > 7) {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR002");
		}

		// if Zero address instruction
		if (pcb.getInstr()[0] == 0) {
			
			pcb.setClock();
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}/*pcb.setQuantumClock();
			if (pcb.getQuantumClock() > 20) {
			pcb.setQuantumClock(0);
			pcb.setClock(-1);
			pcb.setPc1(-1);
			schObj.PUT_PROCESS_IN_READY_QUEUE();
		}else{*/
			
			String instrString = pcb.getInstr()[0] + "" + pcb.getInstr()[1] + "" + pcb.getInstr()[2] + ""
					+ pcb.getInstr()[3] + "" + pcb.getInstr()[4] + "" + pcb.getInstr()[5] + "" + pcb.getInstr()[6] + ""
					+ pcb.getInstr()[7];
			int decimal = Integer.parseInt(instrString, 2);
			String hexStr = Integer.toHexString(decimal);

			while (hexStr.length() < 4) {
				hexStr = "0" + hexStr;
			}
			pcb.setIrList(hexStr);
			String binaryStringpc1 = Integer.toHexString(pcb.getPc());
			while (binaryStringpc1.length() < 4) {
				binaryStringpc1 = "0" + binaryStringpc1;
			}
			pcb.setPcList(binaryStringpc1);
			pcb.setEaBefore("---- " + "----");
			// SCHEDULER.READYQUEUE.get(0).setEaAfter("---- " + "----");
			pcb.setEaAfter("---- " + "----");

			pcb.setOp(pcb.getInstr()[3] + "" + pcb.getInstr()[4] + "" + pcb.getInstr()[5] + "" + pcb.getInstr()[6] + ""
					+ pcb.getInstr()[7]);
			pcb.setOpcode(INSTRUCTION_NAMES());
			pcb.setPc1(OPCODE_OPERATIONS_ZERO_ADDR(pcb.getOpcode()));
			String instrString1 = pcb.getInstr()[8] + "" + pcb.getInstr()[9] + "" + pcb.getInstr()[10] + ""
					+ pcb.getInstr()[11] + "" + pcb.getInstr()[12] + "" + pcb.getInstr()[13] + "" + pcb.getInstr()[14]
					+ "" + pcb.getInstr()[15];
			int decimal1 = Integer.parseInt(instrString1, 2);
			String hexStr1 = Integer.toHexString(decimal1);

			while (hexStr1.length() < 4) {
				hexStr1 = "0" + hexStr1;
			}
			pcb.setIrList(hexStr1);
			String binaryStringpc2 = Integer.toHexString(pcb.getPc1());
			while (binaryStringpc2.length() < 4) {
				binaryStringpc2 = "0" + binaryStringpc2;
			}
			pcb.setPcList(binaryStringpc2);
			pcb.setEaBefore("---- " + "----");
			pcb.setEaAfter("---- " + "----");

			pcb.setOp(pcb.getInstr()[11] + "" + pcb.getInstr()[12] + "" + pcb.getInstr()[13] + "" + pcb.getInstr()[14]
					+ "" + pcb.getInstr()[15]);
			pcb.setOpcode(INSTRUCTION_NAMES());
			pcb.setPc1(OPCODE_OPERATIONS_ZERO_ADDR(pcb.getOpcode()));
			
			pcb.setPc1();
			
			
			INSTRUCTIONS(pcb.getPc1());
			//}

		}	

		// if One address instruction
		else {
			String instrString = pcb.getInstr()[0] + "" + pcb.getInstr()[1] + "" + pcb.getInstr()[2] + ""
					+ pcb.getInstr()[3] + "" + pcb.getInstr()[4] + "" + pcb.getInstr()[5] + "" + pcb.getInstr()[6] + ""
					+ pcb.getInstr()[7] + "" + pcb.getInstr()[8] + "" + pcb.getInstr()[9] + "" + pcb.getInstr()[10] + ""
					+ pcb.getInstr()[11] + "" + pcb.getInstr()[12] + "" + pcb.getInstr()[13] + "" + pcb.getInstr()[14]
					+ "" + pcb.getInstr()[15];
			int decimal = Integer.parseInt(instrString, 2);
			String hexStr = Integer.toHexString(decimal);

			while (hexStr.length() < 4) {
				hexStr = "0" + hexStr;
			}
			pcb.setIrList(hexStr);
			String binaryStringpc = Integer.toHexString(pcb.getPc());
			while (binaryStringpc.length() < 4) {
				binaryStringpc = "0" + binaryStringpc;
			}
			pcb.setPcList(binaryStringpc);
			pcb.setOp(pcb.getInstr()[1] + "" + pcb.getInstr()[2] + "" + pcb.getInstr()[3] + "" + pcb.getInstr()[4] + ""
					+ pcb.getInstr()[5]);
			pcb.setEaBefore(
					String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));

			// Calculates the Effective Address by checking if the index
			// bit exists or not
			pcb.setDADDR(Integer.parseInt(pcb.getInstr()[9] + "" + pcb.getInstr()[10] + "" + pcb.getInstr()[11] + ""
					+ pcb.getInstr()[12] + "" + pcb.getInstr()[13] + "" + pcb.getInstr()[14] + "" + pcb.getInstr()[15],
					2));
			int indexBit = pcb.getInstr()[6];
			if (indexBit == 1) {
				pcb.setDADDR(pcb.getDADDR() + CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			}

			pcb.setOpcode(INSTRUCTION_NAMES());
			OPCODE_OPERATIONS_ONE_ADDR(pcb.getOpcode());
		}

	} // end of INSTRUCTIONS method

	// This method return the opcode based on the binary value in the
	// current instruction
	public static String INSTRUCTION_NAMES() throws FileNotFoundException {
		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		if (pcb.getOp().equals("00000")) {
			pcb.setOpcode("NOP");
		} else if (pcb.getOp().equals("00001")) {
			pcb.setOpcode("OR");
		} else if (pcb.getOp().equals("00010")) {
			pcb.setOpcode("AND");
		} else if (pcb.getOp().equals("00011")) {
			pcb.setOpcode("NOT");
		} else if (pcb.getOp().equals("00100")) {
			pcb.setOpcode("XOR");
		} else if (pcb.getOp().equals("00101")) {
			pcb.setOpcode("ADD");
		} else if (pcb.getOp().equals("00110")) {
			pcb.setOpcode("SUB");
		} else if (pcb.getOp().equals("00111")) {
			pcb.setOpcode("MUL");
		} else if (pcb.getOp().equals("01000")) {
			pcb.setOpcode("DIV");
		} else if (pcb.getOp().equals("01001")) {
			pcb.setOpcode("MOD");
		} else if (pcb.getOp().equals("01010")) {
			pcb.setOpcode("SL");
		} else if (pcb.getOp().equals("01011")) {
			pcb.setOpcode("SR");
		} else if (pcb.getOp().equals("01100")) {
			pcb.setOpcode("CPG");
		} else if (pcb.getOp().equals("01101")) {
			pcb.setOpcode("CPL");
		} else if (pcb.getOp().equals("01110")) {
			pcb.setOpcode("CPE");
		} else if (pcb.getOp().equals("01111")) {
			pcb.setOpcode("BR");
		} else if (pcb.getOp().equals("10000")) {
			pcb.setOpcode("BRT");
		} else if (pcb.getOp().equals("10001")) {
			pcb.setOpcode("BRF");
		} else if (pcb.getOp().equals("10010")) {
			pcb.setOpcode("CALL");
		} else if (pcb.getOp().equals("10011")) {
			pcb.setOpcode("RD");
		} else if (pcb.getOp().equals("10100")) {
			pcb.setOpcode("WR");
		} else if (pcb.getOp().equals("10101")) {
			pcb.setOpcode("RTN");
		} else if (pcb.getOp().equals("10110")) {
			pcb.setOpcode("PUSH");
		} else if (pcb.getOp().equals("10111")) {
			pcb.setOpcode("POP");
		} else if (pcb.getOp().equals("11000")) {
			pcb.setOpcode("HLT");
		}

		// If the opcode is not found
		else {
			ERROR_HANDLER error = new ERROR_HANDLER();
			ERROR_HANDLER.errorCode("ERROR007");
		}
		return pcb.getOpcode();

	} // end of INSTRUCTION_NAMES_ZERO method

	// Zero address instruction computation method
	private static int OPCODE_OPERATIONS_ZERO_ADDR(String opcodeZero) throws FileNotFoundException {
		// ArrayList < String > tosBefore = new ArrayList < String > ();

		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		if (opcodeZero.equals("NOP")) {
			
			if(pcb.isJobFinished()==true){
				System.out.println(">>>>>>>>>>>THIS JOB "+pcb.jobid+" is finished");
				SCHEDULER.PROCESS_ENDED();
				System.out.println(SCHEDULER.READYQUEUE.size());
				//System.exit(0);
			}
			
			return pcb.getPc();

		} else if (opcodeZero.equals("OR")) {

			// top element of stack
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			// second element from top of stack
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			int OR = (pcb.getEAfromCPU() | EAfromCPUSecond);
			String binaryString = Integer.toBinaryString(OR);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			// Stores TOS and the value at top of stack for trace file
			// before push-pop operations
			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			// Stores TOS and the value at top of stack for trace file
			// after push-pop operations
			// SCHEDULER.READYQUEUE.get(0).setTosAfter(
			pcb.setTosAfter((String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString())))));

			return pcb.getPc();

		} else if (opcodeZero.equals("AND")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			int AND = (pcb.getEAfromCPU() & EAfromCPUSecond);
			String binaryString = Integer.toBinaryString(AND);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("NOT")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int NOT = (65535 - pcb.getEAfromCPU());
			String binaryString = Integer.toBinaryString(NOT);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("XOR")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			int XOR = (pcb.getEAfromCPU() ^ EAfromCPUSecond);
			String binaryString = Integer.toBinaryString(XOR);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("ADD")) {

			// check for the infinite loop
			if (pcb.getClock() > 1000) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR006");
			}

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			int ADD = (pcb.getEAfromCPU() + EAfromCPUSecond);
			String binaryString = Integer.toBinaryString(ADD);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);

			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("SUB")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			int SUB = (pcb.getEAfromCPU() - EAfromCPUSecond);
			String binaryString = Integer.toBinaryString(SUB);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("MUL")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			int MUL = (pcb.getEAfromCPU() * EAfromCPUSecond);
			String binaryString = Integer.toBinaryString(MUL);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("DIV")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			// Check for divide by zero
			if (pcb.getEAfromCPU() == 0) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR008");
			}

			int DIV = (EAfromCPUSecond / pcb.getEAfromCPU());
			String binaryString = Integer.toBinaryString(DIV);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("MOD")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			int MOD = (pcb.getEAfromCPU() % EAfromCPUSecond);
			String binaryString = Integer.toBinaryString(MOD);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("SL")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int SL = (pcb.getEAfromCPU() << 1);
			String binaryString = Integer.toBinaryString(SL);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("SR")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int SR = (pcb.getEAfromCPU() >> 1);
			String binaryString = Integer.toBinaryString(SR);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("CPG")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());
			if (EAfromCPUSecond > pcb.getEAfromCPU()) {
				String binaryString = pcb.getTRUE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				return pcb.getPc();
			} else {

				String binaryString = pcb.getFALSE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();
			}

		} else if (opcodeZero.equals("CPL")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());

			if (EAfromCPUSecond < pcb.getEAfromCPU()) {
				String binaryString = pcb.getTRUE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				return pcb.getPc();
			} else {
				String binaryString = pcb.getFALSE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();
			}

		} else if (opcodeZero.equals("CPE")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int EAfromCPUSecond = CONVERT_CPU_TO_DECIMAL(pcb.getStack().get(pcb.getStack().size() - 2).toString());
			if (EAfromCPUSecond == pcb.getEAfromCPU()) {
				String binaryString = pcb.getTRUE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();
			} else {
				String binaryString = pcb.getFALSE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();
			}

		} else if (opcodeZero.equals("BR")) {

			// Takes the pc counter to The Effective address
			pcb.setPc(pcb.getDADDR());
			return pcb.getPc();

		} else if (opcodeZero.equals("BRT")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			if (pcb.getEAfromCPU() == 65535) {
				pcb.setPc(pcb.getDADDR());
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek(

				).toString()))));
				pcb.getStack().pop();
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();
			} else {
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().pop();
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();
			}

		} else if (opcodeZero.equals("BRF")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			if (pcb.getEAfromCPU() == 0) {
				pcb.setPc(pcb.getDADDR());
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().pop();
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();
			} else {
				pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().pop();
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				return pcb.getPc();

			}

		} else if (opcodeZero.equals("CALL")) {

			if (pcb.getClock() > 1000) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR006");
			}

			String binaryString = Integer.toBinaryString(pcb.getPc());
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().push(binaryString);
			pcb.setPc(pcb.getDADDR());
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("RD")) {

			if(pcb.isZeroFlag()==false){
			pcb.setClock(15);
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				// SCHEDULER.READYQUEUE.get(0).setPrevClock(SCHEDULER.READYQUEUE.get(0).getClock());
				pcb.setPrevClock(pcb.getClock());
			}

			// I/O clock
			pcb.setIoClock(15);
			pcb.setZeroFlag(true);
			LOADER.SEGEMENT_FAULT_HANDLER(1);
			
			}else{
				pcb.setZeroFlag(false);
			int[] inputArray = MEMORY.GET_PHYSICALADDRESS(Integer.parseInt(pcb.getPageTable1().get(pcb.getSegmentCounter())), 0);
			pcb.setSegmentCounter();
			String ip = "";
			for (int i = 0; i < inputArray.length; i++) {
				int temp = inputArray[i];
				ip = ip + temp;
			}
			int number = Integer.parseInt(ip, 2);
			if (ip.substring(0, 1).equals("1")) {
				number = number - 65536;
			}
			if (number < -8192 | number > 8191) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR003");
			}
			String binaryString = Integer.toBinaryString(number);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " " + "0000");
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();}

		} else if (opcodeZero.equals("WR")) {

			if(pcb.isWrFlag1()==false){
			pcb.setClock(15);
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setIoClock(15);

			// Count to maintain if the WR instruction is fetched for
			// second time
			pcb.setCount();
			pcb.setWrFlag2(true);
			}
			if ((pcb.getCount() == 1|| pcb.getCount()==2 ) && pcb.isWrFlag2()==true) {
				pcb.setWrFlag1(true);
				if(pcb.isWrFlag3()==false){
					pcb.setWrFlag3(true);
				LOADER.SEGEMENT_FAULT_HANDLER(2);}
				pcb.setOpCount();
				pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
				pcb.getOutput().println();
				pcb.getOutput().println("Job ID (DEC): " + pcb.getJobid() + " Input (DEC): " + pcb.getEAfromCPU());
				pcb.getOutput().flush();
				pcb.setWrFlag1(false);
				pcb.setWrFlag2(false);
				pcb.setWrFlag3(false);
			} else {
				pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
				MEMORY.OUTPUT_TO_DISK(Integer.parseInt(pcb.getPageTable2().get(pcb.getPt2Count()-1)), pcb.getEAfromCPU());
				pcb.setPt2Count();
				pcb.setOpCount();
				pcb.getOutput().println();
				if (ERROR_HANDLER.errorCount > 0) {
					pcb.getOutput().println("Abnormal Termination :" + ERROR_HANDLER.errorMessage);
				} else {
					pcb.getOutput().println("Normal Termination.");
				}
				pcb.getOutput().println();
				pcb.getOutput().println("CLOCK (HEX): " + Integer.toHexString(pcb.getClock()));
				pcb.getOutput().println();
				pcb.getOutput().println("Runtime(DEC): "
						+ (pcb.getClock() - pcb.getIoClock() - pcb.getSegmentFaultTime() - pcb.getPageFaultTime())
						+ " vtu");
				pcb.getOutput().println("Execution time(DEC): " + pcb.getClock() + " vtu");
				pcb.getOutput().println("I/O time(DEC): " + pcb.getIoClock() + " vtu");
				pcb.getOutput().println("Segment Fault Time(DEC): " + pcb.getSegmentFaultTime() + " vtu");
				pcb.getOutput().println("Page Fault Time(DEC): " + pcb.getPageFaultTime() + " vtu");
				float wordCount = pcb.getWordCount();
				float totalMemoryWords = pcb.getSegmentZeroSize() * 4 + pcb.getIpDataSegement()
						+ pcb.getOpDataSegement();
				float memUsage = wordCount * 100 / totalMemoryWords;
				float pageFrameSize = MEMORY.mem.length * 8;
				float memUsage1 = wordCount * 100 / pageFrameSize;
				pcb.getOutput().println("\nMemory Utilization (DEC) : " + (int) wordCount + ":" + (int) pageFrameSize
						+ " or " + memUsage1 + " %");
				pcb.getOutput().println("\nDisk Utilization (DEC): " + pcb.getCurrentJob().size() + ":" + "2048 or "
						+ (float) (pcb.getCurrentJob().size() * 100 / 2048) + " %");
				float unusedSegment1 = 8 - pcb.getInputMemorySize();
				float unusedSegment2 = 8 - pcb.getOpCount();
				;
				float unusedSegment3 = ((pcb.getPF().get(pcb.getPageFrameCounter() - 1)).length() - 1);
				float total1 = (unusedSegment1 + unusedSegment2 + unusedSegment3);
				float avg1 = (unusedSegment1 / 8 + unusedSegment2 / 8 + unusedSegment3 / 8);
				pcb.getOutput().println("\nMemory Fragmentation (DEC): " + (int) total1 + ":" + "24 or "
						+ (float) (avg1 * 100 / 3) + " %");
				float unusedSegment4 = pcb.getCurrentJobPages().size();
				float total2 = ((pcb.getCurrentJobPages().size() * 2) - unusedSegment4) * 4
						+ (16 - pcb.getIpDataSegement() - pcb.getOpDataSegement());
				float avg2 = total2 / 24;
				pcb.getOutput().println(
						"\nDISK Fragmentation (DEC): " + (int) (total2) + ":" + "24 or " + (float) (avg2 * 100) + " %");

				pcb.getOutput().flush();
				if (pcb.getTraceFlag() != 0) {
					// TRACE();
					// SYSTEM.traceFile.flush();
					// SYSTEM.traceFile.close();
				}
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			try{
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));}
			catch(Exception E){
				pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " ----");
			}

			return pcb.getPc();

		} else if (opcodeZero.equals("RTN")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			pcb.setPc(pcb.getEAfromCPU() - 1);
			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("PUSH")) {

			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			String binaryString = Integer.toBinaryString(pcb.getEAfromMem());
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setTosBefore(pcb.getStack().size() + " " + "0000");
			pcb.getStack().push(binaryString);
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			return pcb.getPc();

		} else if (opcodeZero.equals("POP")) {

			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			String binaryString = Integer.toBinaryString(pcb.getEAfromCPU());
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			String[] stringsplit = binaryString.split("");
			pcb.setEAtoMem(stringsplit.length);
			for (int i = 0; i < pcb.getEAtoMem().length; i++) {
				pcb.getEAtoMem()[i] = Integer.parseInt(stringsplit[i]);
			}

			pcb.setNewPageEAtoMemory(Math.floorDiv(pcb.getDADDR(), 8));
			pcb.setDisplacementEAtoMemory(Math.floorMod(pcb.getDADDR(), 8));
			try {
				// LOADER.ref[newPageEAtoMemory] = 1;
				pcb.setRef(pcb.getNewPageEAtoMemory(), 1);
			} catch (Exception e) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR007");
			}
			// ins = LOADER.pageTable0.get(newPageEAtoMemory);
			pcb.setIns(pcb.getPageTable0().get(pcb.getNewPageEAtoMemory()));
			if (pcb.getIns().equals("Nil")) {
				LOADER.PAGE_FAULT_HANDLER(pcb.getNewPageEAtoMemory());
				try {
					LOADER.HEX_BINARY(pcb.getCurrentJobPages().get(pcb.getNewPageEAtoMemory()));
				} catch (Exception e) {
					ERROR_HANDLER error = new ERROR_HANDLER();
					ERROR_HANDLER.errorCode("ERROR019");
				}
				int[] inputArray = MEMORY.GET_PHYSICALADDRESS(
						Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPageEAtoMemory())),
						pcb.getDisplacementEAtoMemory());
				MEMORY.DIRTYBIT_UPDATE_EA(pcb.getNewPageEAtoMemory(), pcb.getEAtoMem(),
						pcb.getPageTable0().get(pcb.getNewPageEAtoMemory()), pcb.getDisplacementEAtoMemory());
			} else {

				int[] inputArray = MEMORY.GET_PHYSICALADDRESS(
						Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPageEAtoMemory())),
						pcb.getDisplacementEAtoMemory());
				MEMORY.DIRTYBIT_UPDATE_EA(pcb.getNewPageEAtoMemory(), pcb.getEAtoMem(),
						pcb.getPageTable0().get(pcb.getNewPageEAtoMemory()), pcb.getDisplacementEAtoMemory());
			}

			pcb.setTosBefore(String.format("%1$04x", pcb.getStack().size() + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString())))));
			pcb.getStack().pop();
			pcb.setTosAfter(String.format("%1$04x", pcb.getStack().size()) + " "
					+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			return pcb.getPc();

		} else if (opcodeZero.equals("HLT")) {
			
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			try{
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));}
			catch(Exception E){
				pcb.setTosBefore("---- " + " " + "----");
			}
			try{
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			}
			catch(Exception e){
				pcb.setTosAfter("---- " + " " + "----");
			}

			SCHEDULER.NEXT_JOB();
			pcb.setJobFinished(true);
			
		}

		return pcb.getPc();

	} // End of OPCODE_OPERATIONS_ZERO_ADDR method

	// One address instruction computation method
	private static void OPCODE_OPERATIONS_ONE_ADDR(String opcodeOne) throws FileNotFoundException {

		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		if (opcodeOne.equals("NOP")) {

			pcb.setClock(4);
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setPc();
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("OR")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{

			// Converts the value at DADDR to Decimal
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			// Converts the value from TOS of CPU to Decimal
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int OR = (pcb.getEAfromCPU() | pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(OR);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());

			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("AND")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int AND = (pcb.getEAfromCPU() & pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(AND);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("NOT")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int NOT = (65535 - pcb.getEAfromCPU());
			String binaryString = Integer.toBinaryString(NOT);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			// EA not applicable
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("XOR")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int XOR = (pcb.getEAfromCPU() ^ pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(XOR);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("ADD")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			if (pcb.getClock() > 1000) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR006");
			}
			// Converts the value at DADDR to Decimal
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			// Converts the value from TOS of CPU to Decimal
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int ADD = (pcb.getEAfromCPU() + pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(ADD);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("SUB")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int SUB = (pcb.getEAfromCPU() - pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(SUB);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("MUL")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int MUL = (pcb.getEAfromCPU() * pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(MUL);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("DIV")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			if (pcb.getEAfromCPU() == 0) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR008");
			}

			int DIV = (pcb.getEAfromCPU() / pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(DIV);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("MOD")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int MOD = (pcb.getEAfromCPU() % pcb.getEAfromMem());
			String binaryString = Integer.toBinaryString(MOD);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("SL")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			int SL = (pcb.getEAfromCPU() << 1);
			String binaryString = Integer.toBinaryString(SL);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			// EA not applicable
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("SR")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			int SR = (pcb.getEAfromCPU() >> 1);
			String binaryString = Integer.toBinaryString(SR);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("CPG")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			if (pcb.getEAfromCPU() > pcb.getEAfromMem()) {
				String binaryString = pcb.getTRUE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			} else {

				String binaryString = pcb.getFALSE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			}

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("CPL")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			if (pcb.getEAfromCPU() < pcb.getEAfromMem()) {
				String binaryString = pcb.getTRUE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				INSTRUCTIONS(pcb.getPc());

			} else {

				String binaryString = pcb.getFALSE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				INSTRUCTIONS(pcb.getPc());}
			}

		} else if (opcodeOne.equals("CPE")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));

			if (pcb.getEAfromCPU() == pcb.getEAfromMem()) {
				String binaryString = pcb.getTRUE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			} else {

				String binaryString = pcb.getFALSE();
				while (binaryString.length() < 16) {
					binaryString = "0" + binaryString;
				}
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().push(binaryString);
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			}

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("BR")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setPc(pcb.getDADDR());
			pcb.setEaAfter("----" + " " + String.format("%1$04x", pcb.getDADDR()));
			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("BRT")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			// If TOS is TRUE
			if (pcb.getEAfromCPU() == 65535) {
				pcb.setPc(pcb.getDADDR());
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromCPU()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().pop();
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				INSTRUCTIONS(pcb.getPc());

			} else {

				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromCPU()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().pop();
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				INSTRUCTIONS(pcb.getPc());}
			}

		} else if (opcodeOne.equals("BRF")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			// If TOS is FALSE
			if (pcb.getEAfromCPU() == 0) {
				pcb.setPc(pcb.getDADDR());
				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromCPU()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().pop();
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				INSTRUCTIONS(pcb.getPc());

			} else {

				pcb.setEaAfter(
						String.format("%1$04x", pcb.getEAfromCPU()) + " " + String.format("%1$04x", pcb.getDADDR()));
				String TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosBefore(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
				pcb.getStack().pop();
				TOS = String.format("%1$04x", pcb.getStack().size());
				pcb.setTosAfter(TOS + " "
						+ String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

				INSTRUCTIONS(pcb.getPc());}
			}

		} else if (opcodeOne.equals("CALL")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			if (pcb.getClock() > 1000) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR006");
			}

			String binaryString = Integer.toBinaryString(pcb.getPc());
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}

			pcb.setEaAfter("----" + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().push(binaryString);
			pcb.setPc(pcb.getDADDR());
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("RD")) {

			if(pcb.isOneFlag()==false){
			pcb.setPc();
			pcb.setClock(19);
			pcb.setQuantumClock(19);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-19);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setIoClock(15);
			pcb.setOneFlag(true);
			pcb.setPc(-1);
			LOADER.SEGEMENT_FAULT_HANDLER(1);}
			int[] inputArray = MEMORY.GET_PHYSICALADDRESS(Integer.parseInt(pcb.getPageTable1().get(pcb.getSegmentCounter())), 0);
			String ip = "";
			for (int i = 0; i < inputArray.length; i++) {
				int temp = inputArray[i];
				ip = ip + temp;
			}
			int number = Integer.parseInt(ip, 2);
			if (ip.substring(0, 1).equals("1")) {
				number = number - 65536;
			}
			if (number < -8192 | number > 8191) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR003");
			}
			String binaryString = Integer.toBinaryString(number);
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(TOS + " " + "0000");
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("WR")) {

			if(pcb.isWrFlag1()==false){
			pcb.setPc();
			pcb.setClock(19);
			pcb.setQuantumClock(19);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-19);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setIoClock(15);

			// Count to maintain if the WR instruction is fetched
			// for second time
			pcb.setCount();
			pcb.setWrFlag2(true);
			}
			if ((pcb.getCount() == 1 || pcb.getCount() == 2) || pcb.isWrFlag2()==true) {
				pcb.setWrFlag1(true);
				if(pcb.isWrFlag3()==false){
					pcb.setWrFlag3(true);
				LOADER.SEGEMENT_FAULT_HANDLER(2);}
				pcb.setOpCount();
				pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
				pcb.getOutput().println();
				pcb.getOutput().println("Job ID (DEC): " + pcb.getJobid() + " Input (DEC): " + pcb.getEAfromCPU());
				pcb.getOutput().flush();
				pcb.setWrFlag1(false);
				pcb.setWrFlag2(false);
				pcb.setWrFlag3(false);

			} else {
				pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
				MEMORY.OUTPUT_TO_DISK(Integer.parseInt(pcb.getPageTable2().get(pcb.getPt2Count()-1)), pcb.getEAfromCPU());
				pcb.setPt2Count();
				pcb.setOpCount();
				pcb.getOutput().println();
				if (ERROR_HANDLER.errorCount > 0) {
					pcb.getOutput().println("Abnormal Termination :" + ERROR_HANDLER.errorMessage);
				} else {
					pcb.getOutput().println("Normal Termination.");
				}
				pcb.getOutput().println();
				pcb.getOutput().println("CLOCK (HEX): " + Integer.toHexString(pcb.getClock()));
				pcb.getOutput().println("Runtime (DEC): "
						+ (pcb.getClock() - pcb.getIoClock() - pcb.getSegmentFaultTime() - pcb.getPageFaultTime())
						+ " vtu");
				pcb.getOutput().println("Execution time(DEC): " + pcb.getClock() + " vtu");
				pcb.getOutput().println("Segment Fault Time(DEC): " + pcb.getSegmentFaultTime() + " vtu");
				pcb.getOutput().println("Page Fault Time(DEC): " + pcb.getPageFaultTime() + " vtu");
				float wordCount = pcb.getWordCount();
				float totalMemoryWords = pcb.getSegmentZeroSize() * 4 + pcb.getIpDataSegement()
						+ pcb.getOpDataSegement();
				float memUsage = wordCount * 100 / totalMemoryWords;
				float pageFrameSize = MEMORY.mem.length * 8;
				float memUsage1 = wordCount * 100 / pageFrameSize;
				pcb.getOutput().println("\nMemory Utilization (DEC) : " + (int) wordCount + ":" + (int) pageFrameSize
						+ " or " + memUsage1 + " %");
				pcb.getOutput().println("\nDisk Utilization (DEC): " + pcb.getCurrentJob().size() + ":" + "2048 or "
						+ (float) (pcb.getCurrentJob().size() * 100 / 2048) + " %");
				float unusedSegment1 = 8 - pcb.getInputMemorySize();
				float unusedSegment2 = 8 - pcb.getOpCount();
				;
				float unusedSegment3 = ((pcb.getPF().get(pcb.getPageFrameCounter() - 1)).length() - 1);
				float total1 = (unusedSegment1 + unusedSegment2 + unusedSegment3);
				float avg1 = (unusedSegment1 / 8 + unusedSegment2 / 8 + unusedSegment3 / 8);
				pcb.getOutput().println("\nMemory Fragmentation (DEC): " + (int) total1 + ":" + "24 or "
						+ (float) (avg1 * 100 / 3) + " %");
				float unusedSegment4 = pcb.getCurrentJobPages().size();
				float total2 = ((pcb.getCurrentJobPages().size() * 2) - unusedSegment4) * 4
						+ (16 - pcb.getIpDataSegement() - pcb.getOpDataSegement());
				float avg2 = total2 / 24;
				pcb.getOutput().println(
						"\nDISK Fragmentation (DEC): " + (int) (total2) + ":" + "24 or " + (float) (avg2 * 100) + " %");

				pcb.getOutput().flush();
				if (pcb.getTraceFlag() != 0) {
					// TRACE();
					// SYSTEM.traceFile.flush();
					// SYSTEM.traceFile.close();
				}
			}
			// EA no applicable
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("RTN")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			pcb.setPc(pcb.getEAfromCPU() - 1);
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("PUSH")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromMem(CONVERT_MEM_TO_DECIMAL(pcb.getDADDR()));
			String binaryString = Integer.toBinaryString(pcb.getEAfromMem());
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromMem()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(TOS + " " + "0000");
			pcb.getStack().push(binaryString);
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("POP")) {

			pcb.setPc();
			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEAfromCPU(CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()));
			String binaryString = Integer.toBinaryString(pcb.getEAfromCPU());
			if (binaryString.length() > 16) {
				binaryString = binaryString.substring(binaryString.length() - 16);
			}
			while (binaryString.length() < 16) {
				binaryString = "0" + binaryString;
			}
			String[] stringsplit = binaryString.split("");
			pcb.setEAtoMem(stringsplit.length);
			for (int i = 0; i < pcb.getEAtoMem().length; i++) {
				pcb.getEAtoMem()[i] = Integer.parseInt(stringsplit[i]);
			}

			pcb.setNewPageEAtoMemory(Math.floorDiv(pcb.getDADDR(), 8));
			pcb.setDisplacementEAtoMemory(Math.floorMod(pcb.getDADDR(), 8));
			try {
				// LOADER.ref[newPageEAtoMemory] = 1;
				pcb.setRef(pcb.getNewPageEAtoMemory(), 1);
			} catch (Exception e) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR007");
			}
			// ins = LOADER.pageTable0.get(newPageEAtoMemory);
			pcb.setIns(pcb.getPageTable0().get(pcb.getNewPageEAtoMemory()));
			if (pcb.getIns().equals("Nil")) {
				LOADER.PAGE_FAULT_HANDLER(pcb.getNewPageEAtoMemory());
				try {
					LOADER.HEX_BINARY(pcb.getCurrentJobPages().get(pcb.getNewPageEAtoMemory()));
				} catch (Exception e) {
					ERROR_HANDLER error = new ERROR_HANDLER();
					ERROR_HANDLER.errorCode("ERROR019");
				}
				int[] inputArray = MEMORY.GET_PHYSICALADDRESS(
						Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPageEAtoMemory())),
						pcb.getDisplacementEAtoMemory());
				MEMORY.DIRTYBIT_UPDATE_EA(pcb.getNewPageEAtoMemory(), pcb.getEAtoMem(),
						pcb.getPageTable0().get(pcb.getNewPageEAtoMemory()), pcb.getDisplacementEAtoMemory());
			} else {

				int[] inputArray = MEMORY.GET_PHYSICALADDRESS(
						Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPageEAtoMemory())),
						pcb.getDisplacementEAtoMemory());
				MEMORY.DIRTYBIT_UPDATE_EA(pcb.getNewPageEAtoMemory(), pcb.getEAtoMem(),
						pcb.getPageTable0().get(pcb.getNewPageEAtoMemory()), pcb.getDisplacementEAtoMemory());
			}

			pcb.setEaAfter(String.format("%1$04x", pcb.getEAfromCPU()) + " " + String.format("%1$04x", pcb.getDADDR()));
			String TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			pcb.getStack().pop();
			TOS = String.format("%1$04x", pcb.getStack().size());
			pcb.setTosAfter(TOS + " " + String.format("%1$04x", pcb.getEAfromCPU()));

			INSTRUCTIONS(pcb.getPc());}

		} else if (opcodeOne.equals("HLT")) {

			pcb.setClock(4);
			pcb.setQuantumClock(4);
			if (pcb.getQuantumClock() > 20) {
				pcb.setQuantumClock(0);
				pcb.setClock(-4);
				pcb.setPc(-1);
				schObj.PUT_PROCESS_IN_READY_QUEUE();
			}else{
			pcb.setClockPMT(pcb.getClock() - pcb.getPrevClock());
			if (pcb.getClockPMT() > 15) {
				PRINT_PMT();
				pcb.setClockPMT(0);
				pcb.setPrevClock(pcb.getClock());
			}
			pcb.setEaAfter("---- " + " " + "----");
			String TOS = String.format("%1$04x", pcb.getStack().size());
			try{
			pcb.setTosBefore(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));}
			catch(Exception E){
				pcb.setTosBefore("---- " + " " + "----");
			}
			try{
			pcb.setTosAfter(
					TOS + " " + String.format("%1$04x", (CONVERT_CPU_TO_DECIMAL(pcb.getStack().peek().toString()))));
			}
			catch(Exception e){
				pcb.setTosAfter("---- " + " " + "----");
			};
			SCHEDULER.NEXT_JOB();
			//System.exit(0);
			}
		}

	} // End of OPCODE_OPERATIONS_ONE_ADDR method

	// This method returns the top value of the
	// SCHEDULER.READYQUEUE.get(0).getStack() into integer
	private static int CONVERT_CPU_TO_DECIMAL(String string) {

		// Top value of Stack goes to EAValue
		SCHEDULER.READYQUEUE.get(0).setEAValue(SCHEDULER.READYQUEUE.get(0).getStack().peek().toString());
		// Conversion of String EAValue to to int[] for memory insertion
		String[] ab = SCHEDULER.READYQUEUE.get(0).getEAValue().split("");
		String b = "";
		int intvalue;
		for (int i = 0; i < ab.length; i++) {
			b += ab[i];
		}

		if (b.substring(0, 1).equals("1") & (!b.equals("1111111111111111"))) {

			int in = Integer.parseInt(b, 2);
			intvalue = convertToTwoscompliment(in);
			return intvalue;
		} else {
			intvalue = Integer.parseInt(b, 2);
			return intvalue;
		}
	}

	// This method returns the value at memory location of
	// Effective address in integer
	private static int CONVERT_MEM_TO_DECIMAL(int daddrInbin) throws FileNotFoundException {

		int number;
		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		pcb.setNewPageEA(Math.floorDiv(daddrInbin, 8));
		pcb.setDisplacementEA(Math.floorMod(daddrInbin, 8));
		// LOADER.ref[newPageEA] = 1;
		pcb.setRef(pcb.getNewPageEA(), 1);
		// Looking into the PMT at the specified page for the frame in memory
		pcb.setIns(pcb.getPageTable0().get(pcb.getNewPageEA()));
		if (pcb.getIns().equals("Nil")) {
			LOADER.PAGE_FAULT_HANDLER(pcb.getNewPageEA());
			try {
				LOADER.HEX_BINARY(pcb.getCurrentJobPages().get(pcb.getNewPageEA()));
			} catch (Exception e) {
				ERROR_HANDLER error = new ERROR_HANDLER();
				ERROR_HANDLER.errorCode("ERROR019");
			}
			int[] inputArray = MEMORY.GET_PHYSICALADDRESS(Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPageEA())),
					pcb.getDisplacementEA());
			String ip = "";
			for (int i = 0; i < inputArray.length; i++) {
				int temp = inputArray[i];
				ip = ip + temp;
			}
			number = Integer.parseInt(ip, 2);
		} else {

			int[] inputArray = MEMORY.GET_PHYSICALADDRESS(Integer.parseInt(pcb.getPageTable0().get(pcb.getNewPageEA())),
					pcb.getDisplacementEA());
			String ip = "";
			for (int i = 0; i < inputArray.length; i++) {
				int temp = inputArray[i];
				ip = ip + temp;
			}
			number = Integer.parseInt(ip, 2);
		}

		return number;

	} // End of CONVERT_MEM_TO_DECIMAL method

	// This method Prints all 3 PMT
	public static void PRINT_PMT() {
		PCB pcb = SCHEDULER.READYQUEUE.get(0);
		pcb.getOutput().println("Program Segment");
		System.out.println("Program Segment");
		for (int i = 0; i < pcb.getPageTable0().size(); i++) {
			pcb.getOutput().println("Page(DEC)- " + i + " : Frame(DEC)- " + pcb.getPageTable0().get(i));
			System.out.println("Page(DEC)- " + i + " : Frame(DEC)- " + pcb.getPageTable0().get(i));
		}

		pcb.getOutput().println("Input Segment");
		System.out.println("Input Segment");
		for (int i = 0; i < pcb.getPageTable1().size(); i++) {
			pcb.getOutput().println("Page(DEC)- " + i + " : Frame(DEC)- " + pcb.getPageTable1().get(i));
			System.out.println("Page(DEC)- " + i + " : Frame(DEC)- " + pcb.getPageTable1().get(i));
		}

		pcb.getOutput().println("Output Segment");
		System.out.println("Output Segment");
		for (int i = 0; i < pcb.getPageTable2().size(); i++) {
			pcb.getOutput().println("Page(DEC)- " + i + " : Frame(DEC)- " + pcb.getPageTable2().get(i));
			System.out.println("Page(DEC)- " + i + " : Frame(DEC)- " + pcb.getPageTable2().get(i));
		}
		pcb.getOutput().println("----------------------");
	}

	// This method convert's to Two's compliment
	public static int convertToTwoscompliment(int dec) {

		int temp1 = dec - 1;
		String temp2 = Integer.toBinaryString(temp1);
		String temp3 = reverseDigits(temp2);
		int tempo = Integer.parseInt(temp3, 2);
		int temp4 = 0 - tempo;

		return temp4;
	} // end of convertToTwoscompliment method

	// This method reveres digits
	public static String reverseDigits(String binaryInt) {
		String result = binaryInt;
		result = result.replace("0", " ");
		result = result.replace("1", "0");
		result = result.replace(" ", "1");
		return result;
	} // end of reverseDigits method

	// This method returns the two's compliment to the object calling it.
	// This method prints the values for (PC), (BR), (IR),
	// (TOS) & (S[TOS]) before Execution, EA and (EA) before
	// Execution(if applicable), (TOS) & (S[TOS]) After Execution,
	// EA and (EA) After Execution(if applicable) to the TRACE file.
	/*
	 * public static void TRACE() {
	 * 
	 * SYSTEM.traceFile .println("----------------------------------" +
	 * "-------------" + "--------------------------------------");
	 * SYSTEM.traceFile.printf("%-6s %-8s %-8s %-8s %-8s %-10s %-11s " + " \r\n"
	 * , "|(PC)" + "|", "|" + "(BR)" + "|", "|" + "(IR)" + "|", "|" +
	 * "t&s[t]Bef" + "|", "|" + "EA&(EA)Bef" + "|", "|" + "t&s[t]Af" + "|", "|"
	 * + "EA&(EA)Af|"); SYSTEM.traceFile.printf(
	 * "%-6s %-7s %-8s %-10s %-13s %-12s %-11s" + "  \r\n", "|(HEX)" + "|", "|"
	 * + "(HEX)" + "|", "|" + "(HEX)" + "|", "|" + "(HEX)" + "|", "|" + "(HEX)"
	 * + "|", "|" + "(HEX)" + "|", "|" + "(HEX)|"); SYSTEM.traceFile.println(
	 * "--------------------------" + "----------------------------------" +
	 * "----------" + "---------------"); try { for (int i = 0; i < pc; i++) {
	 * String pcList1 =pcList.get(i); String brList1 =brList.get(i); String
	 * irList1 =irList.get(i); String tosBefore1 =tosBefore.get(i); String
	 * eaBefore1 =eaBefore.get(i); String tosAfter1 =tosAfter.get(i); String
	 * eaAfter1 =eaAfter.get(i);
	 * 
	 * if(pcList1.length()>4||brList1.length()>4||irList1.length()>4||
	 * tosBefore1.length()>4||eaBefore1.length()>4||tosAfter1.
	 * length()>4||eaAfter1.length()>4){
	 * 
	 * pcList1 = pcList1.substring(pcList1.length()-4, pcList1.length());
	 * brList1 = brList1.substring(brList1.length()-4, brList1.length());
	 * irList1 = irList1.substring(irList1.length()-4, irList1.length());
	 * tosBefore1 = tosBefore1.substring(0, 4)+" "+tosBefore1.substring
	 * (tosBefore1.length()-4, tosBefore1.length()); eaBefore1 =
	 * eaBefore1.substring(0, 4)+" "+eaBefore1.substring (eaBefore1.length()-4,
	 * eaBefore1.length()); tosAfter1 = tosAfter1.substring(0, 4)+" "
	 * +tosAfter1.substring (tosAfter1.length()-4, tosAfter1.length()); eaAfter1
	 * = eaAfter1.substring(0, 4)+" "+eaAfter1.substring(eaAfter1 .length()-4,
	 * eaAfter1.length()); } SYSTEM.traceFile.printf(
	 * "%-6s %-8s %-8s %-8s %-9s %-11s %-11s" + " \r\n", "|" + pcList1 + "|",
	 * "|" + brList1 + "|", "|" + irList1 + "|", "|" + tosBefore1 + "|", "|" +
	 * eaBefore1 + "|", "|" + tosAfter1 + "|", "|" + eaAfter1 + "|"); } } catch
	 * (Exception e) { //System.out.println(); } }
	 */

}
