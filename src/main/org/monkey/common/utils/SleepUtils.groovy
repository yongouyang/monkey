package org.monkey.common.utils;

public class SleepUtils {

    public static void waitFor(long timeoutInMillis, long interval, Closure condition) {
        def start = System.currentTimeMillis()
        while (!condition() && (System.currentTimeMillis() - start) < timeoutInMillis){
            println "sleeping for ${interval} milliseconds"
            Thread.sleep(interval)
        }
    }


}
