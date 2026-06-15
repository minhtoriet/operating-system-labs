package VMM;

import java.io.IOException;
import java.io.RandomAccessFile;

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
        this.pageNum = vmSize / pageSize;
        this.pageSize = pageSize;
        this.frameNum = pmSize / pageSize;

        this.tlb = new TLB(tlbEntryNum);
        this.pageTable = new PageTable(this.frameNum);
        this.memory = new byte[pmSize];
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
            return this.memory[frame * this.pageSize + offset];
        }

        // Page fault
        this.pageFaultCount++;
        frame = this.pageTable.replace(page);
        this.swapin(page, frame);

        // ???? update TLB
        
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
        int entry = this.policy.access(page);
        if (entry == -1)
            return -1;  // TLB miss

        // TLB hit
        return this.tlb[entry][1];
    }

    public void replace(int page, int frame) {
        // ???
    }
}

class PageTable {
    private int frameNum;
    private ReplacementPolicy policy;

    public PageTable(int frameNum) {
        this.frameNum = frameNum;
        this.policy = new FIFOPolicy(this.frameNum);
    }

    public  int getFrame(int page) {
        return this.policy.access(page);
    }

    public int replace(int page) {
        return this.policy.replace(page);
    }
}

