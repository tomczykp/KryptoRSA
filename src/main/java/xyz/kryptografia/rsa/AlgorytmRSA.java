package xyz.kryptografia.rsa;

import java.util.Base64;

public class AlgorytmRSA implements Szyfr {

	public AlgorytmRSA() {
		super();
	}

	@Override
	public byte[] encrypt(byte[] plainText, Num[] pubKey) {

		return plainText;
	}

	@Override
	public byte[] decrypt(byte[] cipherText, Num[] privKey) {

		return cipherText;
	}

	@Override
	public Num[][] genKey(int len) {

		Num one = new Num(1);
		Num p, q;

		do {
			p = Num.randOdd(len);
			System.out.println("Trying p");
		} while (!p.testRabinMiller());

		do {
			q = Num.randOdd(len);
			System.out.println("Trying q");
		} while (!q.testRabinMiller());

		while (q.equals(p)) {
			do {
				p = Num.randOdd(len);
			} while (!p.testRabinMiller());
		}

		Num n = Num.mulKaratsuba(p, q);
		Num fi = Num.mulKaratsuba(
				Num.subtract(p, 1),
				Num.subtract(q, 1)
		);


		Num e;
		do {
			e = Num.generateFromRange(one, n);
		} while (!Num.gcd(e, fi).equals(one));

		Num d = Num.inverse(e, n);

		System.out.println("\tn = " + n + "\n\tfi = " + fi);


		Num[][] t = {{e, n}, {d, n}};
		return t;
	}


}


