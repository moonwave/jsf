package org.moonwave.util;

import java.security.SecureRandom;

public class RandomUtil {

    static SecureRandom random = new SecureRandom();

    public static int nextInt() {
          return random.nextInt();
    }

    public static int nextPositiveInt() {
        int ret = 0; 
        while (true) {
            ret = random.nextInt();
            if (ret > 0) {
                break;
            }
        }
        return ret;
    }

    /**
     * https://javadigest.wordpress.com/tag/securerandom-example/
     * http://stackoverflow.com/questions/12249235/securerandom-safe-seed-in-java
     * http://www.programcreek.com/java-api-examples/java.security.SecureRandom
     *
     * @param args
     */
    public static void test() {
    
        // Get the instance of SecureRandom class with specified PRNG algorithm
        SecureRandom secureRandom = new SecureRandom();

        // You can use the getInstance() of the Secure Random class to create an object of SecureRandam
        // where you would need to specify the algorithm name.
        // SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        
        // Display the algorithm name
        System.out.println("Usedrithm: " + secureRandom.getAlgorithm());

        // You also specify the algorithm provider in the getInstance() method
        // SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        
        // Display the Provider
        System.out.println("Provider secureRandom.getProvider()");

        // A call to the setSeed() method will seed the SecureRandom object.
        // If a call is not made to setSeed(),
        // The first call to nextBytes method will force the SecureRandom object to seed itself.
        
        // Get 10 random numbers
        System.out.println("Randomgers generated using SecureRandom");
            for (int i = 0; i < 10; i++) {
              System.out.println(secureRandom.nextInt());
        }
    }

    /**
     * https://javadigest.wordpress.com/tag/securerandom-example/
     * http://stackoverflow.com/questions/12249235/securerandom-safe-seed-in-java
     * http://www.programcreek.com/java-api-examples/java.security.SecureRandom
     *
     * @param args
     */
    public static void main(String[] args) {
        test();
        System.out.println("");
        for (int i = 0; i < 10; i++) {
            System.out.println(nextInt());
        }
        System.out.println("");
        for (int i = 0; i < 10; i++) {
            System.out.println(nextPositiveInt());
        }
    }
}
