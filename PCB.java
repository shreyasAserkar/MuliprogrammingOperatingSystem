import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
/**
Class - PCB

Description -This method captures and store all the information 
related to job like the job id , size , number of page frames
alloted to this particular program and whenever a system needs
 any information related to job ,it fetches from  PCB.

*/
public class PCB {

 //Store the Job id of the current user job
 public String jobid;

 //Segment Map Table
 private int[] SMT = new int[3];

 //ArrayList for Page Frame 
 public ArrayList < String > PF = new ArrayList < String > ();
 
 public ArrayList < String > PAGED_JOB = new ArrayList < String > ();
 
 public int externalJob = 0;
 
 public int startIndex;
 
 public int endIndex;
 
 public int preStartIndex = 0;
 
 public int loadAddress;
 
 //public int initialPC;
 
 public int size;
 
 public int traceFlag;
 
 public int ipDataSegement;
 
 public int opDataSegement;
 
 public int pageFrameCounter = 0;
 
 public int pageFrame_inPCB;
 
 public int ioSize ;
 
 public int inputSize = 0;

 public int outputSize = 0;
 
 public ArrayList < String > pageTable0;
 
 public int[] valid;
 
 public int[] ref;
 
 public ArrayList < String > pageTable1;
 
 public ArrayList < String > pageTable2;
 
 public boolean flag = false;

 public int wordCount = 0;
 
 public int inputMemorySize;
 
 public ArrayList<String> currentJob = new ArrayList<String>();
 
 public ArrayList<String> currentJobPages = new ArrayList<String>();
 
 public int segmentZeroSize = 0;
 
 public ArrayList<String> inputList = new ArrayList<String>();
	
 public ArrayList<String> outputList = new ArrayList<String>();
 
 public ArrayList<String> JOB = new ArrayList<String>();
 
 public PrintWriter output;
 
 public PrintWriter traceFile;
 
 //----CPU PCB's--------------------------------------------------------------------------------
 
 public int pc = 0;
 
 public ArrayList < String > brList = new ArrayList < String > ();
 
 public int newPage;
 
 public int newPageEA;
 
 public int displacement;
 
 public int newPageEAtoMemory;
 
 public int displacementEA;
 
 public int displacementEAtoMemory;
 
 public int DADDR = 0;
 
 public int segmentFaultTime = 0;
 
 public int pageFaultTime = 0;
 
 public int quantumClock = 0;
 
 public int clockPMT = 0;
 
 public int prevClock = 0;
 
 public int clock = 0;
 
 public int opCount = 0;
 
 public int ioClock = 0;
 
//Takes binary from mem and returns decimal
 public int EAfromMem = 0;
 
 public int count = 0;
 
 public int[] instr;
 
 public String opcode;
 
 public String op;
 
 public Stack < String > stack = new Stack < String > ();
 
 public String EAValue;
 
 public int[] EAtoMem;
 
 public int EAfromCPU = 0;
 
 public String ins;
 
 public String TRUE = Integer.toBinaryString(65535);
 
 public String FALSE = Integer.toBinaryString(0);
 
 public  ArrayList < String > pcList = new ArrayList < String > ();
 
 public  ArrayList < String > irList = new ArrayList < String > ();
 
 public ArrayList < String > tosBefore = new ArrayList < String > ();
 
 public ArrayList < String > tosAfter = new ArrayList < String > ();

 public ArrayList < String > eaBefore = new ArrayList < String > ();
 
 public ArrayList < String > eaAfter = new ArrayList < String > ();
 
 public int pc1;
 
 public int ipMoreThanOne = 0 ;
 
 public boolean jobFinished = false ;
 
 public int segmentCounter =0;
 
 public boolean zeroFlag = false;
 
 public boolean oneFlag = false;
 
 public int pt2Count = 0 ;
 
 public boolean wrFlag1 =false;
 
 public boolean wrFlag2 =false;

 public boolean wrFlag3 =false;
 
 public String getJobid() {
  return jobid;
 }
 public void setJobid(String jobid) {
  this.jobid = jobid;
 }
 public int[] getSMT() {
  return SMT;
 }
 public void setSMT(int index, int value) {
  this.SMT[index] = value;
 }
 public ArrayList < String > getPF() {
  return PF;
 }
 public void setPF(String p) {
  this.PF.add(p);
 }

 public void setPF(int index, String p) {
  this.PF.set(index, p);
 }
public ArrayList<String> getPAGED_JOB() {
	return PAGED_JOB;
}
public void setPAGED_JOB(ArrayList<String> dISK_JOB) {
	PAGED_JOB = dISK_JOB;
}
public int getStartIndex() {
	return startIndex;
}
public void setStartIndex(int startIndex) {
	this.startIndex = startIndex;
}
public int getEndIndex() {
	return endIndex;
}
public void setEndIndex(int endIndex) {
	this.endIndex = endIndex;
}
public int getPreStartIndex() {
	return preStartIndex;
}
public void setPreStartIndex(int preStartIndex) {
	this.preStartIndex = preStartIndex;
}
public int getLoadAddress() {
	return loadAddress;
}
public void setLoadAddress(int loadAddress) {
	this.loadAddress = loadAddress;
}
/*public int getInitialPC() {
	return initialPC;
}
public void setInitialPC(int initialPC) {
	this.initialPC = initialPC;
}*/
public int getSize() {
	return size;
}
public void setSize(int size) {
	this.size = size;
}
public int getTraceFlag() {
	return traceFlag;
}
public void setTraceFlag(int traceFlag) {
	this.traceFlag = traceFlag;
}
public int getIpDataSegement() {
	return ipDataSegement;
}
public void setIpDataSegement(int ipDataSegement) {
	this.ipDataSegement = ipDataSegement;
}
public int getOpDataSegement() {
	return opDataSegement;
}
public void setOpDataSegement(int opDataSegement) {
	this.opDataSegement = opDataSegement;
}
public int getPageFrameCounter() {
	return pageFrameCounter;
}
public void setPageFrameCounter(int pageFrameCounter) {
	this.pageFrameCounter = pageFrameCounter;
}
public void setPageFrameCounter() {
	this.pageFrameCounter = pageFrameCounter+1;
}
public int getPageFrame_inPCB() {
	return pageFrame_inPCB;
}
public void setPageFrame_inPCB(int pageFrame_inPCB) {
	this.pageFrame_inPCB = pageFrame_inPCB;
}
public int getIoSize() {
	return ioSize;
}
public void setIoSize(int ioSize) {
	this.ioSize = ioSize;
}
public ArrayList<String> getPageTable0() {
	return pageTable0;
}
public void setPageTable0(ArrayList<String> pageTable0) {
	this.pageTable0 = pageTable0;
}
public void setPageTable0(int index, String value) {
	this.pageTable0.set(index, value);
	 }
public int[] getValid() {
	return valid;
}
public void setValid(int pageSize) {
	this.valid = new int[pageSize];
}
public void setValid(int index, int value) {
	  this.valid[index] = value;
	 }
public int[] getRef() {
	return ref;
}
public void setRef(int pageSize) {
	this.ref = new int[pageSize];
}
public void setRef(int index, int value) {
	this.ref[index] = value;
}
public ArrayList<String> getPageTable1() {
	return pageTable1;
}
public void setPageTable1(int pageTable1Size) {
	this.pageTable1 = new ArrayList<String>(pageTable1Size);
}
public void setPageTable1(String pageTable1) {
	this.pageTable1.add(pageTable1);
}
public void setPageTable1(int index, String pageTable1) {
	this.pageTable1.set(index, pageTable1);
}
public ArrayList<String> getPageTable2() {
	return pageTable2;
}
public void setPageTable2(int pageTable2Size) {
	this.pageTable2 = new ArrayList<String>(pageTable2Size);
}
public void setPageTable2(String pageTable2) {
	this.pageTable2.add(pageTable2);
}
public void setPageTable2(int index, String pageTable2) {
	this.pageTable2.set(index, pageTable2);
}
public boolean isFlag() {
	return flag;
}
public void setFlag(boolean flag) {
	this.flag = flag;
}
public int getWordCount() {
	return wordCount;
}
public void setWordCount() {
	this.wordCount = wordCount+1;
}
public int getInputMemorySize() {
	return inputMemorySize;
}
public void setInputMemorySize(int inputMemorySize) {
	this.inputMemorySize = inputMemorySize;
}
public ArrayList<String> getCurrentJob() {
	return currentJob;
}
public void setCurrentJob(ArrayList<String> currentJob) {
	this.currentJob = currentJob;
}
public ArrayList<String> getCurrentJobPages() {
	return currentJobPages;
}
public void setCurrentJobPages(ArrayList<String> currentJobPages) {
	this.currentJobPages = currentJobPages;
}
public int getSegmentZeroSize() {
	return segmentZeroSize;
}
public void setSegmentZeroSize(int segmentZeroSize) {
	this.segmentZeroSize = segmentZeroSize;
}
public int getInputSize() {
	return inputSize;
}
public void setInputSize(int inputSize) {
	this.inputSize = inputSize;
}
public int getOutputSize() {
	return outputSize;
}
public void setOutputSize(int outputSize) {
	this.outputSize = outputSize;
}
public ArrayList<String> getInputList() {
	return inputList;
}
public void setInputList(ArrayList<String> inputList) {
	this.inputList = inputList;
}
public ArrayList<String> getOutputList() {
	return outputList;
}
public void setOutputList(ArrayList<String> outputList) {
	this.outputList = outputList;
}
public ArrayList<String> getJOB() {
	return JOB;
}
public void setJOB(ArrayList<String> jOB) {
	JOB = jOB;
}
public PrintWriter getOutput() {
	return output;
}
public void setOutput(PrintWriter output) {
	this.output = output;
}
public PrintWriter getTraceFile() {
	return traceFile;
}
public void setTraceFile(PrintWriter traceFile) {
	this.traceFile = traceFile;
}
public int getPc() {
	return pc;
}
public void setPc(int pc1) {
	if(pc1 == -1){
		this.pc =pc -1;
	}else{
	this.pc = pc1;
	}
}
public void setPc() {
	this.pc = pc+1;
}
public ArrayList<String> getBrList() {
	return brList;
}
public void setBrList(String brList) {
	this.brList.add(brList);
}
public int getNewPage() {
	return newPage;
}
public void setNewPage(int newPage) {
	this.newPage = newPage;
}
public int getNewPageEA() {
	return newPageEA;
}
public void setNewPageEA(int newPageEA) {
	this.newPageEA = newPageEA;
}
public int getDisplacement() {
	return displacement;
}
public void setDisplacement(int displacement) {
	this.displacement = displacement;
}
public int getNewPageEAtoMemory() {
	return newPageEAtoMemory;
}
public void setNewPageEAtoMemory(int newPageEAtoMemory) {
	this.newPageEAtoMemory = newPageEAtoMemory;
}
public int getDisplacementEA() {
	return displacementEA;
}
public void setDisplacementEA(int displacementEA) {
	this.displacementEA = displacementEA;
}
public int getDisplacementEAtoMemory() {
	return displacementEAtoMemory;
}
public void setDisplacementEAtoMemory(int displacementEAtoMemory) {
	this.displacementEAtoMemory = displacementEAtoMemory;
}
public int getDADDR() {
	return DADDR;
}
public void setDADDR(int dADDR) {
	DADDR = dADDR;
}
public int getSegmentFaultTime() {
	return segmentFaultTime;
}
public void setSegmentFaultTime() {
	this.segmentFaultTime = segmentFaultTime+20;
}
public int getPageFaultTime() {
	return pageFaultTime;
}
public void setPageFaultTime() {
	this.pageFaultTime = pageFaultTime+20;
}
public int getClockPMT() {
	return clockPMT;
}
public void setClockPMT(int clockPMT) {
	this.clockPMT = clockPMT;
}
public int getQuantumClock() {
	return quantumClock;
}
public void setQuantumClock(int clockPMT) {
	if(clockPMT==0){
		this.quantumClock = 0;
	}else{
	this.quantumClock = quantumClock	+ clockPMT;
	}
}
public void setQuantumClock() {
	this.quantumClock = quantumClock+1;
}
public int getPrevClock() {
	return prevClock;
}
public void setPrevClock(int prevClock) {
	this.prevClock = prevClock;
}
public int getClock() {
	return clock;
}
public void setClock(int clocktime) {
	this.clock = clock + clocktime;
}
public void setClock() {
	this.clock = clock+1;
}
public int getOpCount() {
	return opCount;
}
public void setOpCount() {
	this.opCount = opCount+1;
}
public int getEAfromMem() {
	return EAfromMem;
}
public void setEAfromMem(int eAfromMem) {
	EAfromMem = eAfromMem;
}
public int getIoClock() {
	return ioClock;
}
public void setIoClock(int ioClocktime) {
	this.ioClock = ioClock+ioClocktime;
}
public int getCount() {
	return count;
}
public void setCount() {
	this.count = count+1;
}
public int[] getInstr() {
	return instr;
}
public void setInstr(int[] instr) {
	this.instr = instr;
}
public String getOpcode() {
	return opcode;
}
public void setOpcode(String opcode) {
	this.opcode = opcode;
}
public String getOp() {
	return op;
}
public void setOp(String op) {
	this.op = op;
}
public Stack<String> getStack() {
	return stack;
}
public void setStack(Stack<String> stack) {
	this.stack = stack;
}
public String getEAValue() {
	return EAValue;
}
public void setEAValue(String eAValue) {
	EAValue = eAValue;
}
public int[] getEAtoMem() {
	return EAtoMem;
}
public void setEAtoMem(int[] eAtoMem) {
	EAtoMem = eAtoMem;
}
public void setEAtoMem(int eAtoMem) {
	EAtoMem = new int[eAtoMem];
}
public int getEAfromCPU() {
	return EAfromCPU;
}
public void setEAfromCPU(int eAfromCPU) {
	EAfromCPU = eAfromCPU;
}
public String getIns() {
	return ins;
}
public void setIns(String ins) {
	this.ins = ins;
}
public String getTRUE() {
	return TRUE;
}
public void setTRUE(String tRUE) {
	TRUE = tRUE;
}
public String getFALSE() {
	return FALSE;
}
public void setFALSE(String fALSE) {
	FALSE = fALSE;
}
public ArrayList<String> getPcList() {
	return pcList;
}
public void setPcList(String pcList) {
	this.pcList.add(pcList);
}
public ArrayList<String> getIrList() {
	return irList;
}
public void setIrList(String irList) {
	this.irList.add(irList);
}
public ArrayList<String> getTosBefore() {
	return tosBefore;
}
public void setTosBefore(String tosBefore) {
	this.tosBefore.add(tosBefore);
}
public ArrayList<String> getTosAfter() {
	return tosAfter;
}
public void setTosAfter(String tosAfter) {
	this.tosAfter.add(tosAfter);
}
public ArrayList<String> getEaBefore() {
	return eaBefore;
}
public void setEaBefore(String eaBefore) {
	this.eaBefore.add(eaBefore);
}
public ArrayList<String> getEaAfter() {
	return eaAfter;
}
public void setEaAfter(String eaAfter) {
	this.eaAfter.add(eaAfter);
}
public int getPc1() {
	return pc1;
}
public void setPc1(int pc1) {
	if(pc1 == -1){
		this.pc =pc -1;
	}else{
	this.pc1 = pc1;
	}
}
public void setPc1() {
	this.pc1 = pc1 +1;
}
public int getExternalJob() {
	return externalJob;
}
public void setExternalJob(int i) {
	this.externalJob = externalJob+1;
}
public boolean isJobFinished() {
	return jobFinished;
}
public void setJobFinished(boolean jobFinished) {
	this.jobFinished = jobFinished;
}
public int getSegmentCounter() {
	return segmentCounter;
}
public void setSegmentCounter() {
	this.segmentCounter = segmentCounter+1;
}
public boolean isZeroFlag() {
	return zeroFlag;
}
public void setZeroFlag(boolean zeroFlag) {
	this.zeroFlag = zeroFlag;
}
public boolean isOneFlag() {
	return oneFlag;
}
public void setOneFlag(boolean oneFlag) {
	this.oneFlag = oneFlag;
}
public int getIpMoreThanOne() {
	return ipMoreThanOne;
}
public void setIpMoreThanOne() {
	this.ipMoreThanOne = ipMoreThanOne+1;
}
public int getPt2Count() {
	return pt2Count;
}
public void setPt2Count() {
	this.pt2Count = pt2Count+1;
}
public void setPt2Count(int p) {
	this.pt2Count = p;
}
public boolean isWrFlag1() {
	return wrFlag1;
}
public void setWrFlag1(boolean wrFlag1) {
	this.wrFlag1 = wrFlag1;
}
public boolean isWrFlag2() {
	return wrFlag2;
}
public void setWrFlag2(boolean wrFlag2) {
	this.wrFlag2 = wrFlag2;
}
public boolean isWrFlag3() {
	return wrFlag3;
}
public void setWrFlag3(boolean wrFlag3) {
	this.wrFlag3 = wrFlag3;
}
} //End of PCB method