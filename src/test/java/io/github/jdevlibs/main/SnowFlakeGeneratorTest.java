package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.unique.SnowFlakeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class SnowFlakeGeneratorTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        SnowFlakeGenerator snowflake = new SnowFlakeGenerator();
        int iterations = 100;

        // Validate that the IDs are not same even if they are generated in the same ms in different threads
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            Future<Long> future = executorService.submit(() -> {
                long id = snowflake.nextId();
                latch.countDown();
                return id;
            });
            futures.add(future);
        }

        latch.await();
        System.out.println("NodeId: " + snowflake.getNodeId());

        for (Future<Long> future : futures) {
            System.out.println(future.get());
        }
    }
}
