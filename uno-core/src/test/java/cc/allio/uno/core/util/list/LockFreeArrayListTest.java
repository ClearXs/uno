package cc.allio.uno.core.util.list;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LockFreeArrayListTest extends BaseTestCase {

    @Test
    void testGrow() {

        LockFreeArrayList<Integer> list = new LockFreeArrayList<>(1);

        list.add(1);
        list.add(2);

        assertEquals(2, list.size());

    }

    @Test
    void testEliminate1() {
        LockFreeArrayList<Integer> list = new LockFreeArrayList<>(1);

        list.add(1);
        list.add(2);

        list.add(3);

        list.remove(1);

        assertEquals(2, list.size());

        assertEquals(3, list.get(1));
    }

    @Test
    void testEliminate2() {
        LockFreeArrayList<Integer> list = new LockFreeArrayList<>(1);
        list.add(1);
        list.add(2);

        list.add(3);

        list.remove(2);
        assertEquals(2, list.size());

        assertEquals(2, list.get(1));
    }

    @Test
    void testEliminate3() {
        LockFreeArrayList<Integer> list = new LockFreeArrayList<>(1);
        list.add(1);
        list.add(2);

        list.add(3);

        list.removeFirst();
        assertEquals(2, list.size());

        assertEquals(3, list.get(1));
    }

    @Test
    void testParallelAdd() throws InterruptedException {
        LockFreeArrayList<Integer> list = new LockFreeArrayList<>(1);


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Runnable r1 = () -> {
            list.add(1);
        };

        Runnable r2 = () -> {
            list.add(2);
        };

        Runnable r3 = () -> {
            list.add(3);
        };

        executorService.execute(r1);
        executorService.execute(r2);
        executorService.execute(r3);


        Thread.sleep(100);
        assertEquals(3, list.size());
    }
}
