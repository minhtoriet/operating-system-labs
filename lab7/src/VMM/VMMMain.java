package VMM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VMMMain {

    public static void main(String[] args) throws IOException {
        String bsFileName = "BACKING_STORE.bin";
        String addressFileName = "addresses.txt";

        VMM vmm = null;
        BufferedReader addressReader = null;
        try {
            vmm = new VMM(65535, 8192, 256, 16, bsFileName);
            byte[] backingStore = Files.readAllBytes(Paths.get(bsFileName));
            addressReader = new BufferedReader(new FileReader(addressFileName));
            String addressLine;
            int lineCount = 0;
            addressLine = addressReader.readLine();
            lineCount++;
            while (addressLine != null) {
                int logicalAddress = Integer.parseInt(addressLine);
                byte actual = vmm.read(logicalAddress);
                if (backingStore[logicalAddress] != actual) {
                    System.out.println("Error - Line " + lineCount + " - Logical address " + logicalAddress + 
                    ", backing store: " + backingStore[logicalAddress] + ", actual: " + actual);
                    break;
                } 
                addressLine = addressReader.readLine();
                lineCount++;
            }
            System.out.println("TLB miss count: " + vmm.getTLBMissCount());
            System.out.println("Page Fault count: " + vmm.getPageFaultCount());
        } finally {
            if (addressReader != null)
                addressReader.close();
            if (vmm != null)
                vmm.shutdown();            
        }
    }
}
