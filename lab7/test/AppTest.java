import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AppTest {
    App app = new App();
    @Test
    public void testAdd(){
        assertEquals(11,app.add(5, 6));
        assertEquals(1,app.add(-5, 6));
    }
    @Test
    public void testMul(){
        assertEquals(20, app.mul(10, 2));
        assertEquals(2, app.mul(1, 2));
    }
}
