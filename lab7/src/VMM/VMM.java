package VMM;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;


public class VMM {
    private int pageNum;
    private int pageSize;
    private int frameNum;
    private TLB tlb;
    private PageTable pageTable;
    private byte[] memory;
    private RandomAccessFile backingFile = null;
    
    private int tlbMissCount = 0;
    private int pageFaultCount = 0;

    public VMM(int vmSize, int pmSize, int pageSize, int tlbEntryNum, String bsFileName) throws IOException {
        this.pageNum = vmSize / pageSize;   // 65536 / 256 = 256 pages
        this.pageSize = pageSize;           // 256 bytes
        this.frameNum = pmSize / pageSize;  // 8192 / 256 = 32 frames

        this.tlb = new TLB(tlbEntryNum);    // 16 entries translation lookaside buffer
        this.pageTable = new PageTable(this.frameNum, this.pageNum);  // should be 256 entries tho?
        this.memory = new byte[pmSize];     // 8192 bytes physical memory
        this.backingFile = new RandomAccessFile(bsFileName, "r");
        if (this.pageNum * this.pageSize != this.backingFile.length())
            throw new IllegalArgumentException("Backing file lenth is invalid.");
    }

    public byte read(int logicalAddress) throws IOException {
        int offset = logicalAddress % this.pageSize;
        int page = logicalAddress / this.pageSize;
        int frame;

        frame = this.tlb.getFrame(page);
        if (frame >= 0)  // TBL hit
            return this.memory[frame * this.pageSize + offset];

        // TLB miss
        this.tlbMissCount++;
        frame = this.pageTable.getFrame(page);
        if (frame >= 0) {
            // ??? Update TLB
            tlb.replace(page, frame);
            return this.memory[frame * this.pageSize + offset];
        }

        // Page fault
        this.pageFaultCount++;
        frame = this.pageTable.replace(page);   // replace this invalid page with whatever frame there is,
        this.swapin(page, frame);               // delete the duplicate frame anywhere else on the page table

        // ???? update TLB
        tlb.invalidateFrame(frame);
        tlb.replace(page, frame);               // simple update
        return this.memory[frame * this.pageSize + offset];
    }

    private void swapin(int page, int frame) throws IOException {
        this.backingFile.seek(page * this.pageSize);
        this.backingFile.read(this.memory, frame * this.pageSize, this.pageSize);
    }

    public int getTLBMissCount() {
        return this.tlbMissCount;
    }

    public int getPageFaultCount() {
        return this.pageFaultCount;
    }

    public void shutdown() {
        if (this.backingFile != null) {
            try {
                this.backingFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class TLB {
    private int entryNum;
    private int[][] tlb;
    private ReplacementPolicy policy;

    public TLB(int entryNum) {
        this.entryNum = entryNum;
        this.tlb = new int[entryNum][2];
        for (int i = 0; i < this.entryNum; i++) {
            this.tlb[i][0] = -1;
            this.tlb[i][1] = -1;
        }
        this.policy = new FIFOPolicy(this.entryNum);
    }

    public int getFrame(int page) {
        for (int i = 0; i < entryNum; i++) {
            if (tlb[i][0] == page) {
                this.policy.access(page); 
                return tlb[i][1];
            }
        }
        return -1;
    }

    public void replace(int page, int frame) {
        // ???
        int victim = policy.replace(page);
        tlb[victim][0] = page;
        tlb[victim][1] = frame;
    }
    public void invalidateFrame(int frame){
        for (int i = 0; i < tlb.length; i++) {
            if (tlb[i][1] == frame) {
                tlb[i][1] = -1;
                break;
            }
        }
    }
}

class PageTable {
    private int frameNum;
    private ReplacementPolicy policy;
    private int[] table;
    public PageTable(int frameNum, int pageNum) {
        this.frameNum = frameNum;
        this.policy = new FIFOPolicy(this.frameNum);
        table = new int[pageNum];
        Arrays.fill(table, -1);
    }

    public int getFrame(int page) {
        int frame = table[page];
        if (frame != -1) policy.access(page);
        return frame;
    }

    public int replace(int page) { // returns frame idx wtf
        int frameIdx = this.policy.replace(page);
        for (int i = 0; i < table.length; i++) {
            if (table[i] == frameIdx){
                table[i] = -1;
                break;
            }
        }
        table[page] = frameIdx;
        return frameIdx;
    }
}

class FIFOPolicy implements ReplacementPolicy {
    private int pointer;
    private int maxSize;
    public FIFOPolicy(int num) {
        maxSize = num;
    }
    public int replace(int page){ // if full then remove first, add to last, return idx of last add
        int victim = pointer;
        pointer = (pointer + 1) % maxSize;
        return victim;
    }
    public void access(int page){
    }
}

interface ReplacementPolicy {
    public int replace(int page);
    public void access(int page);
}
