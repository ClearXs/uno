package cc.allio.uno.component.flink.task;

import cc.allio.uno.test.BaseTestCase;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.FromElementsFunction;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.FluxSink;

import java.util.Arrays;
import java.util.List;

class FlinkAccumulatedCountTaskTest extends BaseTestCase {

    FluxSink<Tuple3<String, Long, Integer>> emit;

    @Override
    protected void onInit() throws Throwable {

    }

    @Test
    void testCountBuilder() {
        assertThrows(IllegalArgumentException.class, () -> {
            FlinkTaskBuilder.<Integer, Integer>shelves()
                    .count()
                    .build();
        });

    }

    @Test
    void testSum() throws Exception {
        Tuple3<String, Long, Integer> num1 = Tuple3.of("k1", 100L, 1);
        Tuple3<String, Long, Integer> num2 = Tuple3.of("k1", 200L, 2);
        Tuple3<String, Long, Integer> num3 = Tuple3.of("k1", 300L, 3);
        AccumulatedCountTask<Integer, Integer> task = FlinkTaskBuilder.<Integer, Integer>shelves()
                .buildName("count")
                .buildInputType(TypeInformation.<Tuple3<String, Long, Integer>>of(new TypeHint<Tuple3<String, Long, Integer>>() {
                }))
                .buildOutputType(TypeInformation.<Integer>of(new TypeHint<Integer>() {
                }))
                .buildCalculator((context, obj) -> {
                    if (obj instanceof List) {
                        return ((List<Tuple3<String, Long, Integer>>) obj).stream()
                                .map(t -> t.f2)
                                .reduce(Integer::sum)
                                .get();
                    }
                    return null;
                })
                .buildSource(new FromElementsFunction<>(Arrays.asList(num1, num2, num3)))
                .buildSink(new PrintSinkFunction<>())
                .count()
                .buildThreshold(2)
                .buildOverlap(1)
                .build();
        task.run();
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
