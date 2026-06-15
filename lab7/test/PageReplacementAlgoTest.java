import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import PageReplacementAlgo.FIFO;
import PageReplacementAlgo.LRU;
import PageReplacementAlgo.OPT;
public class PageReplacementAlgoTest {
    int[] ref = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
    int frames = 3;
    @Test
    public void FIFOTest() {
        FIFO fifo = new FIFO(ref, frames);
        assertEquals(15,fifo.pageFaultsIncured());
    }
    @Test
    public void LRUTest(){
        LRU lru = new LRU(ref, frames);
        assertEquals(12, lru.pageFaultsIncured());
    }
    @Test
    public void OPTTest(){
        OPT opt = new OPT(ref, frames);
        assertEquals(9, opt.pageFaultsIncured());
    }
}
