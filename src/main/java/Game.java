import org.apache.commons.lang3.ArrayUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Arrays;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public class Game {
    public static void main(String[] args) throws IOException {
        try {
            if (args.length < 3) {
                throw new Exception1();
            }
            if (args.length % 2 == 0) {
                throw new Exception2();
            }
            for (int i = 0; i < args.length; i++) {
                for (int j = i + 1; j < args.length; j++) {
                    if (args[i].equals(args[j])) {
                        throw new Exception3(i, j);
                    }
                }
            }
        } catch (Exception1 | Exception2 | Exception3 o) {
            o.printStackTrace();
            return;
        }

        SecureRandom random = new SecureRandom();
        byte secretKey[] = new byte[16];
        random.nextBytes(secretKey);

        int computerMoveIndex = random.nextInt(args.length);
        String computerMove = args[computerMoveIndex];
        String hmacSha256 = hmac256(computerMove, secretKey);
        System.out.println("HMAC: " + hmacSha256);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Available moves:");
        for (int i = 0; i < args.length; i++) {
            System.out.println((i + 1) + " - " + args[i]);
        }
        System.out.println("0 - exit");
        System.out.print("Enter your move: ");

        String humanMoveNumber = br.readLine();
        br.close();

        int humanMoveIndex = Integer.parseInt(humanMoveNumber);
        String humanMoveString;

        if (humanMoveNumber.equals("0")) {
            System.out.println("Exiting");
            return;
        } else {
            humanMoveString = args[Integer.parseInt(humanMoveNumber) - 1];
        }

        System.out.println("Your move: " + humanMoveString);
        System.out.println("Computer move: " + computerMove);

        if (computerMoveIndex == humanMoveIndex - 1) {
            System.out.println("DRAW!");
            System.out.println(Arrays.toString(secretKey));
            return;
        }

        String[] combinedArg = ArrayUtils.addAll(args, args);

        for (int i = computerMoveIndex + 1; i <= computerMoveIndex + args.length / 2; i++) {
            if (humanMoveString.equals(combinedArg[i])) {
                System.out.println("YOU WIN!");
                System.out.println(Arrays.toString(secretKey));
                return;
            }
        }
        System.out.println("YOU LOSE!");
        System.out.println(Arrays.toString(secretKey));
    }

    public static String hmac256(String message, byte[] secretKey) {
        HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey);
        return hm256.hmacHex(message);
    }

}