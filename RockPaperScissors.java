import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacUtils;
import static org.apache.commons.codec.digest.HmacAlgorithms.*;

public class RockPaperScissors {

	public static void main(String[] args) {
		
		if(args.length < 3 || args.length % 2 == 0 || args.length != new HashSet<>(Arrays.asList(args)).size()) {
			System.out.println("Invalid code execution. Please input an odd number (at least 3) of unrepeated game choices."
					+ "\nExample: java -jar gamr.jar rock paper scissors lizard Spock");
			System.exit(0);
		}
		
		Scanner in = new Scanner(System.in);
		SecureRandom random = new SecureRandom();

		byte[] keyBytes = new byte[16];
		random.nextBytes(keyBytes);
		String key = Hex.encodeHexString(keyBytes);
		
		int computer = random.nextInt(args.length) + 1, user = -1;

		String hmac = new HmacUtils(HMAC_SHA_256, key).hmacHex(String.valueOf(computer));
		System.out.println("HMAC:\n" + hmac);

		do {
			System.out.println("Available moves:");
			for(int i = 0; i < args.length; i++)
				System.out.println(i + 1 + " - " + args[i]);
			System.out.print("0 - exit\nEnter your move: ");
			try {
				user = Integer.parseInt(in.next());
			}catch(NumberFormatException e) {
				System.out.println("Your input is not valid. Please try again");
			}
			if(user < 0 || user > args.length) System.out.println("Please enter number from the list");
		}while(user < 0 || user > args.length);
		
		if(user == 0) System.exit(0);
		
		System.out.println("Your move: " + args[user - 1]);
		System.out.println("Computer move: " + args[computer - 1]);
		
		if(user == computer) System.out.println("Tie!");
		else {
			if(doesUserWin(user, computer, args.length)) System.out.println("You win!");
			else System.out.println("Computer wins");
		}
		System.out.println("HMAC key" + key);
	}

	public static boolean doesUserWin(int user, int computer, int vars) {
		for(int i = 0; i < vars / 2; i++) {
			if(user + i  + 1 > vars) {
				if(user + i + 1 - vars == computer) return false;
			}else if(user + i + 1 == computer) return false;
		}	
		return true;
	}
}
