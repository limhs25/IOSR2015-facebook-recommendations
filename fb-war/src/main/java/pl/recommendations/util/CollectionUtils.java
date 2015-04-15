package pl.recommendations.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by marekmagik on 2015-04-14.
 */
public class CollectionUtils {

    public static void trimCollection(Collection collection, int numberOfElements){
        Iterator it = collection.iterator();
        int counter = 0;

        while(it.hasNext()){
            if(counter <= numberOfElements){
                it.next();
                counter++;
            }
            it.remove();
        }
    }

    public static void trimMap(Map map, int numberOfElements) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        int counter = 0;

        while(it.hasNext()){
            if(counter <= numberOfElements){
                it.next();
                counter++;
            }
            it.remove();
        }
    }

}
