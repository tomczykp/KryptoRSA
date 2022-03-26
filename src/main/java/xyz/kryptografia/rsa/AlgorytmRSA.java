package xyz.kryptografia.rsa;

public class AlgorytmRSA implements Szyfr {

	public AlgorytmRSA() {
		super();
	}

	@Override
	public byte[] encrypt(byte[] plainText, byte[] pubKey) {

		return plainText;
	}

	@Override
	public byte[] decrypt(byte[] cipherText, byte[] privKey) {

		return cipherText;
	}

	@Override
	public byte[][] genKey(int len) {

		// find p i q (large primes)
		// n = p * q
		// fi = (p-1)*(q-1)
		// e = rand(2^(size-1), 2^size) ? gcd(e, fi) == 1
		// mod inverse d = (e, fi)
		//
		// pubKey = (n, e)
		// privKey = (n, d)
		Num p, q;

		do {
			p = Num.generateOdd(len);
			System.out.println("Checking p = " + p);
		} while (!p.testRabinMiller());

		do {
			q = Num.generateOdd(len);
			System.out.println("Checking q = " + q);
		} while (!q.testRabinMiller());

		while (q.equals(p)) {
			do {
				p = Num.generateOdd(len);
			} while (!p.testRabinMiller());
		}

		Num n = Num.mulKaratsuba(p, q);
		Num fi = Num.mulKaratsuba(
				Num.subtract(p, 1),
				Num.subtract(q, 1)
		);

		System.out.println("\tn = " + n + "\n\tfi = " + fi);

		return null;
	}

}
