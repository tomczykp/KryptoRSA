package xyz.kryptografia.rsa;

import xyz.kryptografia.rsa.liczby.UFatInt;

public class AlgorytmRSA implements Szyfr {

	public AlgorytmRSA() {
		super();
	}

	@Override
	public UFatInt[] encrypt(UFatInt[] plainText, UFatInt[] pubKey) {
		return this.crypt(plainText, pubKey);
	}

	@Override
	public UFatInt[] decrypt(UFatInt[] cipherText, UFatInt[] privKey) {
		return this.crypt(cipherText, privKey);
	}

	@Override
	public UFatInt[] crypt(UFatInt[] text, UFatInt[] keys) {
		int l = text.length;
		UFatInt[] wynik = new UFatInt[l];

		for (int i = 0; i < l; i++)
			wynik[i] = UFatInt.modPow(text[i], keys[0], keys[1]);

		return wynik;
	}

	@Override
	public UFatInt[][] genKey(int len) {

		UFatInt one = new UFatInt(1);
		UFatInt p, q;

		do {
			p = UFatInt.randOdd(len / 2);
			System.out.println("Trying p");
		} while (!p.isPrime());

		do {
			q = UFatInt.randOdd(len / 2);
			System.out.println("Trying q");
		} while (!q.isPrime());

		while (q.equals(p)) {
			do {
				p = UFatInt.randOdd(len / 2);
			} while (!p.isPrime());
		}

		UFatInt n = UFatInt.mulKaratsuba(p, q);
		UFatInt fi = UFatInt.mulKaratsuba(
				UFatInt.subOneOdd(p),
				UFatInt.subOneOdd(q)
		);


		UFatInt e;
		do {
			e = UFatInt.generateUpTo(fi);
		} while (!UFatInt.gcd(e, fi).equals(one));

		UFatInt d = UFatInt.modInverse(e, fi);

		System.out.println("\tn = " + n + "\n\td = " + d + "\n\te = " + e);


		return new UFatInt[][]{{e, n}, {d, n}};
	}


}


