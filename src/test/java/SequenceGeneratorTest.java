import com.ppluobo.jtools.id.SequenceGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SequenceGeneratorTest {

    @Test
    public void testThreadsafe() {
        int min = 1000000;
        Set<Long> sets = Collections.synchronizedSet(new HashSet<>());

        new Thread(new ThreadRun("A", sets, min)).run();
        new Thread(new ThreadRun("B", sets, min)).run();

        Assert.assertEquals(sets.size(), min * 2);
    }

    @Test
    public void testSpeed() {

        int times = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            SequenceGenerator.nextId();
        }
        long end = System.currentTimeMillis();

        System.out.print("pre second : ");
        System.out.println(times / (end - start) * 1000);

    }

    public static class ThreadRun implements Runnable {
        private String name;
        private Set<Long> sets;
        private int times;

        public ThreadRun(String name, Set<Long> sets, int times) {
            this.name = name;
            this.sets = sets;
            this.times = times;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; i++) {
                sets.add(SequenceGenerator.nextId());
            }
        }
    }
}
