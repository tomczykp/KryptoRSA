package xyz.kryptografia.rsa;

import xyz.kryptografia.rsa.liczby.UFatInt;

public class AlgorytmRSA implements Szyfr {

	public AlgorytmRSA() {}

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
		int pSize = len / 16;

		do {
			p = UFatInt.randOdd(pSize);
			System.out.println("Trying p: " + p.toHex());
		} while (!p.isPrime());

		do {
			q = UFatInt.randOdd(pSize);
			System.out.println("Trying q: " + q.toHex());
		} while (!q.isPrime());

		while (q.equals(p)) {
			do {
				p = UFatInt.randOdd(pSize);
				System.out.println("Got the same nums, trying new p: " + p.toHex());
			} while (!p.isPrime());
		}

		UFatInt n = UFatInt.mulKaratsuba(p, q);
		UFatInt fi = UFatInt.mulKaratsuba(
				UFatInt.subOneOdd(p),
				UFatInt.subOneOdd(q)
		);


		UFatInt e;
		do {
			e = UFatInt.randOdd(fi.len());
		} while (!UFatInt.gcd(e, fi).equals(one));

		UFatInt d = UFatInt.modInverse(e, fi);

		System.out.println("\tn = " + n + "\n\td = " + d + "\n\te = " + e);
		System.out.println("\tn = " + n.len() + "\n\td = " + d.len() + "\n\te = " + e.len());


		return new UFatInt[][]{{d, n}, {e, n}};
	}


}


