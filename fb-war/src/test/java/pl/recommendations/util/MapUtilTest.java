package pl.recommendations.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static pl.recommendations.util.MapUtil.sortByValue;


public class MapUtilTest {
    @Test
    public void testSortByValue() {
        Random random = new Random(System.currentTimeMillis());
        Map<String, Integer> testMap = new HashMap<String, Integer>(1000);
        for (int i = 0; i < 1000; ++i) {
            String key;
            do {
                key = "SomeString" + random.nextInt();
            } while (testMap.containsKey(key));

            testMap.put(key, random.nextInt());
        }

        testMap = sortByValue(testMap);

        Assert.assertEquals(1000, testMap.size());

        Integer previous = null;
        for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
            Assert.assertNotNull(entry.getValue());
            if (previous != null) {
                Assert.assertTrue(entry.getValue() >= previous);
            }
            previous = entry.getValue();
        }
    }
}
