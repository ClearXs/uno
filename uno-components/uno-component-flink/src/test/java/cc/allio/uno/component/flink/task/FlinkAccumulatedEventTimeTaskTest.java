package cc.allio.uno.component.flink.task;

import cc.allio.uno.test.BaseTestCase;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.FromElementsFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class FlinkAccumulatedEventTimeTaskTest extends BaseTestCase {
    @Override
    protected void onInit() throws Throwable {

    }

    @Test
    void testSum() throws Exception {
        Tuple3<String, Long, Integer> num1 = Tuple3.of("k1", 100L, 1);
        Tuple3<String, Long, Integer> num2 = Tuple3.of("k1", 120L, 2);
        Tuple3<String, Long, Integer> num3 = Tuple3.of("k1", 160L, 3);
        TypeInformation<Tuple3<String, Long, Integer>> inputType = TypeInformation.<Tuple3<String, Long, Integer>>of(new TypeHint<Tuple3<String, Long, Integer>>() {
        });
        AccumulatedEventTimeTask<Integer, Integer> task = FlinkTaskBuilder.<Integer, Integer>shelves()
                .buildName("time_count")
                .buildInputType(inputType)
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
                .eventTime()
                .buildSize(Time.milliseconds(100L))
                .buildSlide(Time.milliseconds(50L))
                .buildTolerance(0L)
                .build();
        task.run();
        Thread.sleep(20000L);
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
