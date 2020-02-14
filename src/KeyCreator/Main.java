package KeyCreator;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Main {
    
    public String[] asciiArray = "~!\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} œ∑´®†¥¨ˆøπåß∂ƒ©˙∆˚¬Ω≈ç√∫˜µ≤≥÷…æ“‘«≠–ºª•¶§∞¢£™¡`áÁéÉíÍóÓñÑüÜîÎôÔ𝜙𝑛".split("(?!^)");
    public int bitLength;
    public static int encryptionLevel;
    public Main() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("How many bits should the key be? (Large numbers will return large encryption strings, and take longer)");

        String rsaLength = userInput.nextLine();
        
        if(!RSAMath.isInt(rsaLength)) {
            System.out.println("That was not an integer.");
            new Main();
            userInput.close();
            return;
        } else {
            encryptionLevel = Integer.parseInt(rsaLength);
        }
        
        BigInteger[] publicPair = new BigInteger[2];
        BigInteger[] privatePair = new BigInteger[2];
        
        BigInteger p = RSAMath.randomPrime();
        BigInteger q = RSAMath.randomPrime(p);
       
        BigInteger N = p.multiply(q);
        // (p - 1)(q - 1)
        BigInteger ΦN = (p.subtract(BigInteger.valueOf(1))).multiply(q.subtract(BigInteger.valueOf(1)));
        // e is such that in range (1 - ΦN), excluding 1 and ΦN. It is also coprime to both N and ΦN.

        BigInteger e = getLockbox(N, ΦN);
        
        // Got our pubic keys!!
        publicPair[0] = e;
        publicPair[1] = N;
        // d is such that (d * e) % ΦN = 1
        BigInteger d = getKey(e, ΦN);
        
        // Got our private keys!
        privatePair[0] = d;
        privatePair[1] = N;
        
        System.out.println("\n\n*************************************");
        System.out.println("Your public (encrypting) keypair is:");
        System.out.println(publicPair[0] + " and \n" + publicPair[1]);
        System.out.println("\nYour private (decrypting) keypair is:");
        System.out.println(privatePair[0] + " and \n" + privatePair[1]);
        System.out.println("*************************************");
 
        bitLength = N.toString().length();
  
        System.out.println("\nWhat is your secret message?");
        String initialMessage = userInput.nextLine();
        userInput.close();
        
        System.out.println("\n*************************************");
        String encryptedMessage = encrypt(publicPair[0], publicPair[1], initialMessage);
        System.out.println("Your message encrypted is: " + encryptedMessage + "\n");

        String decryptedMessage = decrypt(privatePair[0], privatePair[1], encryptedMessage);
        System.out.println("\nYour message decrypted is: " + decryptedMessage);
        System.out.println("*************************************");
        
    }
    
    public static void main(String[] args) throws InterruptedException {
        new Main();
    }
    
    
    
    // encrypt a message with the keys
    public String encrypt(BigInteger lock1, BigInteger lock2, String message) {
        
        String[] initialMessageArray = message.split("(?!^)");
        
        String[] preppedMessageArray = new String[initialMessageArray.length];
        
        int loadingBar = 0;
        System.out.print("Encrypting: [\\]");
        
        // takes the letters and converts them to numbers to be encrypted
        for(int e = 0; e < initialMessageArray.length; e++) {
            preppedMessageArray[e] = Integer.toString(RSAMath.find(asciiArray, initialMessageArray[e]));
            ++loadingBar;
            System.out.print("\rEncrypting: [" + RSAMath.loadingSigns[loadingBar % RSAMath.loadingSigns.length] + "]");
          
        }
        
        // multiplies the digits by the public values
        for(String item : preppedMessageArray) {
            BigInteger newBigInt = BigInteger.valueOf(Integer.parseInt(item));

            newBigInt = newBigInt.modPow(lock1, lock2);
            
            String bigIntString = newBigInt.toString();
            preppedMessageArray[RSAMath.find(preppedMessageArray, item)] = RSAMath.addLeadingZeroes(bitLength, bigIntString);
            
            ++loadingBar;
            System.out.print("\rEncrypting: [" + RSAMath.loadingSigns[loadingBar % RSAMath.loadingSigns.length] + "]");
        }
        
        String encryptedMessage = String.join("", preppedMessageArray);
        
        System.out.print("\rEncrypting: [✓]\n");
        
        return encryptedMessage;
        
    }
    
    
    public String decrypt(BigInteger key1, BigInteger key2, String message) {
        
        String[] preppedDecodeMessage = RSAMath.splitStringEvery(message, bitLength);
        
        
        int loadingBar = 0;
        //System.out.print("\rDecrypting: [\\]");
        
        for(int i = 0; i < preppedDecodeMessage.length; i++) {
            BigInteger newBigInt = new BigInteger(preppedDecodeMessage[i]);
            
            newBigInt = newBigInt.modPow(key1, key2);
            ++loadingBar;
            System.out.print("\rDecrypting: [" + RSAMath.loadingSigns[loadingBar % RSAMath.loadingSigns.length] + "]");
            
            preppedDecodeMessage[i] = asciiArray[newBigInt.intValueExact()];
         
        }
        
        System.out.print("\rDecrypting: [✓]");
        return String.join("", preppedDecodeMessage);
        
    }
    
    
    public BigInteger getKey(BigInteger e, BigInteger ΦN) {
        // d is such that (d * e) % ΦN = 1
        /*!(d.multiply(e)).mod(ΦN).equals(BigInteger.valueOf(1))*/
        
        //quick way: 𝑑=𝑒^−1mod(𝜙(𝑛))
       

        System.out.print("\nCreating key: [|]");
        BigInteger d = e.modInverse(ΦN);
        System.out.print("\rCreating key: [✓]");
 
        return d;
        
    }
    
    // get the first part of the public key
    private  BigInteger getLockbox(BigInteger N, BigInteger ΦN) {
      //if(RSAMath.coprime(ΦN, i) && RSAMath.coprime(N, i))
        
        System.out.print("\nCreating lock: [");

            
       
        BigInteger maxLimit = ΦN;
        BigInteger minLimit = BigInteger.valueOf(1);
        
        int loadingBar = 0;
        
        BigInteger res = new BigInteger(0, new Random());
        
        while(!(RSAMath.coprime(ΦN, res) && RSAMath.coprime(N, res))) {
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
            System.out.print("\rCreating lock: [" + RSAMath.loadingSigns[loadingBar % RSAMath.loadingSigns.length] + "]");
        }
                
        System.out.print("\rCreating lock: [✓]");
        return res;
        
    }

}
