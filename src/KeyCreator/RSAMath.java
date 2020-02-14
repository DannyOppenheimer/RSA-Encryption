package KeyCreator;

import java.math.BigInteger;
import java.util.Random;

public class RSAMath {
    
    public static String[] loadingSigns = {"\\", "|", "/", "–"};
    
    public static int find(String[] a, String target) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(target))
                return i;
        }
                

        return -1;
    }
    
    public static boolean isPrime(BigInteger number) {
        
      //check if even
        if(number.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
            return false;
        }
        
        //check via BigInteger.isProbablePrime(certainty)
        if (!number.isProbablePrime(13))
            return false;
        return true;
    }
    
    
    // find out the greatest common divisor of two numbers
    public static BigInteger gcd(BigInteger p, BigInteger q) {
        
        if (q.equals(BigInteger.valueOf(0))) {
            return p;
        }
        
        return gcd(q, p.mod(q));
    }
    
    // figure out if two numbers are coprimes of each other
    public static boolean coprime(BigInteger p, BigInteger q) {
        return (gcd(p, q).equals(BigInteger.valueOf(1)));
    }
   
    
    public static BigInteger randomPrime(BigInteger exclude) {



        BigInteger maxLimit = BigInteger.valueOf(2).pow(Main.encryptionLevel + 1).subtract(BigInteger.valueOf(1));
        BigInteger minLimit = BigInteger.valueOf(2).pow(Main.encryptionLevel - 1).subtract(BigInteger.valueOf(1));
        
        BigInteger res = new BigInteger(0, new Random());
        int loadingBar = 0;
        System.out.print("\nFinding 2nd prime number: [\\]");
        while(!isPrime(res) && !res.equals(exclude)) {
     
            BigInteger bigInteger = maxLimit.subtract(minLimit);
            Random randNum = new Random();
            int len = maxLimit.bitLength();

            res = new BigInteger(len, randNum);
            if (res.compareTo(minLimit) < 0) {
                res = res.add(minLimit);
            }

            if (res.compareTo(bigInteger) >= 0) {
                res = res.mod(bigInteger).add(minLimit);
            }
            
            
            ++loadingBar;
            System.out.print("\rFinding 2nd prime number: [" + loadingSigns[loadingBar % loadingSigns.length] + "]");

           
        }
        System.out.print("\rFinding 2nd prime number: [✓]");
        return res;
            
        
        
    }
    public static BigInteger randomPrime() {
        

        BigInteger maxLimit = BigInteger.valueOf(2).pow(Main.encryptionLevel + 1).subtract(BigInteger.valueOf(1));
        BigInteger minLimit = BigInteger.valueOf(2).pow(Main.encryptionLevel - 1).subtract(BigInteger.valueOf(1));
        
        BigInteger res = new BigInteger(0, new Random());
        int loadingBar = 0;
        System.out.print("\nFinding 1st prime number: [\\]");
        while(!isPrime(res)) {
     
            BigInteger bigInteger = maxLimit.subtract(minLimit);
            Random randNum = new Random();
            int len = maxLimit.bitLength();

            res = new BigInteger(len, randNum);
            if (res.compareTo(minLimit) < 0) {
                res = res.add(minLimit);
            }

            if (res.compareTo(bigInteger) >= 0) {
                res = res.mod(bigInteger).add(minLimit);
            }
            
            
            ++loadingBar;
            System.out.print("\rFinding 1st prime number: [" + loadingSigns[loadingBar % loadingSigns.length] + "]");

           
        }
        System.out.print("\rFinding 1st prime number: [✓]");
        return res;
        
    }
    
    public static boolean isInt(String i) {
        try {
            Integer.parseInt(i);
        } catch(NumberFormatException e1) {
            return false;
        }
        return true;
    }
    
    public static String addLeadingZeroes(int length, String string) {
        
        String[] zeros = new String[length];
        
        for(int i = 0; i < zeros.length; i++) {
            zeros[i] = "0";
        }
        
        return (String.join("", zeros) + string).substring(string.length());
    }
    
    public static String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }
    
    static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }
    
    public static BigInteger getRandom(BigInteger upperLimit) {
        Random rand = new Random();
        BigInteger result;
        do {
            result = new BigInteger(upperLimit.bitLength(), rand);
        } while(result.compareTo(upperLimit) >= 0);
        return result;
    }
 
}
