package routines.system;

import java.security.SecureRandom;

public class RandomUtils {

	//lazy init, but no meaning now as only one method
	private static final class RandomNumberGeneratorHolder {
		static final SecureRandom randomNumberGenerator = new SecureRandom();
	}

	public static double random() {
		return RandomNumberGeneratorHolder.randomNumberGenerator.nextDouble();
	}
}
