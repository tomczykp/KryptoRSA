package xyz.kryptografia.rsa;

import xyz.kryptografia.rsa.liczby.Num;

public class AlgorytmRSA implements Szyfr {

	public AlgorytmRSA() {
		super();
	}

	@Override
	public Num[] encrypt(Num[] plainText, Num[] pubKey) {
		return this.crypt(plainText, pubKey);
	}

	@Override
	public Num[] decrypt(Num[] cipherText, Num[] privKey) {
		return this.crypt(cipherText, privKey);
	}

	@Override
	public Num[] crypt(Num[] text, Num[] keys) {
		int l = text.length;
		Num[] wynik = new Num[l];

		for (int i = 0; i < l; i++)
			wynik[i] = Num.modPow(text[i], keys[0], keys[1]);

		return wynik;
	}

	@Override
	public Num[][] genKey(int len) {

		Num one = new Num(1);
		Num p, q;

		do {
			p = Num.randOdd(len / 2);
			System.out.println("Trying p");
		} while (!p.isPrime());

		do {
			q = Num.randOdd(len / 2);
			System.out.println("Trying q");
		} while (!q.isPrime());

		while (q.equals(p)) {
			do {
				p = Num.randOdd(len / 2);
			} while (!p.isPrime());
		}

		Num n = Num.mulKaratsuba(p, q);
		Num fi = Num.mulKaratsuba(
				Num.subtract(p, 1),
				Num.subtract(q, 1)
		);


		Num e;
		do {
			e = Num.generateFromRange(one, fi);
		} while (!Num.gcd(e, fi).equals(one));

		Num d = Num.modInverse(e, fi);

		System.out.println("\tn = " + n + "\n\td = " + d + "\n\te = " + e);


		return new Num[][]{{e, n}, {d, n}};
	}


}


