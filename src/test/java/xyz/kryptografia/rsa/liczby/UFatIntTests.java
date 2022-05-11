package xyz.kryptografia.rsa.liczby;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class UFatIntTests {

	int n = 2000;
	int len = 100;

	public static boolean isPrime (int n) {
		// Corner case
		if (n <= 1)
			return false;

		// Check from 2 to n-1
		for (int i = 2; i < n; i++)
			if (n % i == 0)
				return false;

		return true;
	}


	public String toHex (byte[] bytes) {
		String sb = "";
		for (byte b : bytes)
			sb = String.format("%02X ", b) + sb;

		return sb;
	}


	@Test
	public void constructoTest () {
		NumList n1, n2;
		UFatInt f1, f2;
		Random r = new Random();

		for (int i = 0; i < this.n; i++) {
			n1 = NumList.generateOdd(r.nextInt(this.len));
			n2 = NumList.generateOdd(r.nextInt(this.len));

			f1 = new UFatInt(n1);
			f2 = new UFatInt(n2);

			Assertions.assertEquals(toHex(n1.getBytes()), f1.toHex());
			Assertions.assertEquals(toHex(n2.getBytes()), f2.toHex());

			Assertions.assertEquals(n1.toString(), f1.toString());
			Assertions.assertEquals(n2.toString(), f2.toString());
			System.out.println("Test " + i + " const()");
		}

	}

	@Test
	public void comparatorTest () {

		NumList l1, l2;
		UFatInt i1, i2;
		long lo1, lo2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			Assertions.assertEquals(l1.compareTo(l2), i1.compareTo(i2));
			Assertions.assertEquals(toHex(l1.getBytes()), i1.toHex());
			Assertions.assertEquals(toHex(l2.getBytes()), i2.toHex());
			System.out.println("Test " + i + " compare()");
		}

		for (int i = 0; i < this.n; i++) {
			lo1 = rand.nextInt(Integer.MAX_VALUE);
			lo2 = rand.nextInt(Integer.MAX_VALUE);

			i1 = new UFatInt(lo1);
			i2 = new UFatInt(lo2);
			int t = 0;
			if (lo1 > lo2)
				t = 1;
			else if (lo1 < lo2)
				t = -1;

			Assertions.assertEquals(t, i1.compareTo(i2));
			Assertions.assertEquals(String.valueOf(lo1), i1.toString());
			Assertions.assertEquals(String.valueOf(lo2), i2.toString());
			System.out.println("Test " + i + " compare()");
		}

	}

	@Test
	public void addTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.add(i1, i2);

			Assertions.assertEquals(NumList.add(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(toHex(NumList.add(l1, l2).getBytes()), tmp.toHex());
			Assertions.assertEquals(toHex(l1.getBytes()), i1.toHex());
			Assertions.assertEquals(toHex(l2.getBytes()), i2.toHex());
			System.out.println("Test " + i + " add()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = NumList.generateOdd(this.len);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.add(i1, liczba);

			Assertions.assertEquals(NumList.add(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(toHex(l1.getBytes()), i1.toHex());
			System.out.println("Test " + i + " add()");
		}

	}

	@Test
	public void subTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.subtract(i1, i2);

			Assertions.assertEquals(NumList.subtract(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " subtract()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = NumList.generateOdd(this.len);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.subtract(i1, liczba);

			Assertions.assertEquals(NumList.subtract(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " subtract()");
		}
	}

	@Test
	public void mulTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.mul(i1, i2);

			Assertions.assertEquals(NumList.mul(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " mul()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = NumList.generateOdd(this.len);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.mul(i1, liczba);

			Assertions.assertEquals(NumList.mul(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " mul()");
		}
	}

	@Test
	public void karatsubaTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.mulKaratsuba(i1, i2);

			Assertions.assertEquals(NumList.mulKaratsuba(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " mulKaratsuba()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.mulKaratsuba(i1, liczba);

			Assertions.assertEquals(NumList.mulKaratsuba(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " mulKaratsuba()");
		}
	}

	@Test
	public void divTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.divide(i1, i2);

			Assertions.assertEquals(NumList.divide(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " divide()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.divide(i1, liczba);

			Assertions.assertEquals(NumList.divide(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " divide()");
		}
	}

	@Test
	public void divShiftTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.divShift(i1, i2);

			Assertions.assertEquals(NumList.divide(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " divShift()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.divShift(i1, liczba);

			Assertions.assertEquals(NumList.divide(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " divShift()");
		}
	}

	@Test
	public void modTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.mod(i1, i2);

			Assertions.assertEquals(NumList.mod(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " mod()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.mod(i1, liczba);

			Assertions.assertEquals(NumList.mod(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " mod()");
		}
	}

	@Test
	public void modPowTest () {
		UFatInt i1, i2;
		Random rand = new Random();

		NumList l1, l2;

		for (int i = 0; i < this.n; i++) {
			int t = rand.nextInt(200);
			NumList r1 = new NumList(t);
			UFatInt r2 = new UFatInt(t);

			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			System.out.println("Test " + i + " modPow()\n 2 ^ " + l1 + " mod " + l2);
			NumList s = NumList.modPow(r1, l1, l2);
			UFatInt tmp = UFatInt.modPow(r2, i1, i2);

			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			Assertions.assertEquals(r1.toString(), r2.toString());
			Assertions.assertEquals(s.toString(), tmp.toString());
		}
	}

	@Test
	public void gcdTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.gcd(i1, i2);

			Assertions.assertEquals(NumList.gcd(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " gcd()");
		}

	}

	@Test
	public void modInverseTest () {
		NumList l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();
		NumList one = new NumList(1);
		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			l2 = NumList.generateOdd(rand.nextInt(2, this.len));
			if (!NumList.gcd(l1, l2).equals(one)) {
				i--;
				continue;
			}

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);


			UFatInt tmp = UFatInt.modInverse(i1, i2);

			Assertions.assertEquals(NumList.modInverse(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " modInverse()");
		}

	}

	@Test
	public void primeTestLow () {
		UFatInt l1;
		int i1;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(3, Integer.MAX_VALUE);

			l1 = new UFatInt(i1);
			System.out.println("Test " + i + " prime(): " + i1);
			Assertions.assertEquals(isPrime(i1), l1.isPrime());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
		}

	}

	@Test
	public void fastPowTest () {
		NumList l1;
		UFatInt i1;
		long i2;
		Random rand = new Random();
		for (int i = 0; i < this.n / 100; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			i2 = rand.nextLong(1, 30);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.fastPow(i1, i2);

			Assertions.assertEquals(NumList.fastPow(l1, i2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			System.out.println("Test " + i + " fastPow()");
		}

	}

	@Test
	public void timeTest () {

		UFatInt x, y;
		Random r = new Random();
		long start;
		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			byte[] b = new byte[this.len];
			r.nextBytes(b);
			x = new UFatInt(b);

			r.nextBytes(b);
			y = new UFatInt(b);
			UFatInt w = UFatInt.add(x, y);
		}
		System.out.println("Dodawanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			byte[] b = new byte[this.len];
			r.nextBytes(b);
			x = new UFatInt(b);

			r.nextBytes(b);
			y = new UFatInt(b);
			UFatInt w = UFatInt.subtract(x, y);
		}
		System.out.println("Odejmowanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			byte[] b = new byte[this.len];
			r.nextBytes(b);
			x = new UFatInt(b);

			r.nextBytes(b);
			y = new UFatInt(b);
			UFatInt w = UFatInt.mul(x, y);
		}
		System.out.println("Mul: " + (System.currentTimeMillis() - start));


		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			byte[] b = new byte[this.len];
			r.nextBytes(b);
			x = new UFatInt(b);
			r.nextBytes(b);
			y = new UFatInt(b);
			UFatInt w = UFatInt.mulKaratsuba(x, y);
		}
		System.out.println("mulKaratsuba: " + (System.currentTimeMillis() - start));

	}

	@Test
	public void timeTestNum () {

		NumList x, y;
		long start;
		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			x = NumList.randOdd(this.len);
			y = NumList.randOdd(this.len);
			NumList w = NumList.add(x, y);
		}
		System.out.println("Dodawanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			x = NumList.randOdd(this.len);
			y = NumList.randOdd(this.len);
			NumList w = NumList.subtract(x, y);
		}
		System.out.println("Odejmowanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			x = NumList.randOdd(this.len);
			y = NumList.randOdd(this.len);
			NumList w = NumList.mul(x, y);
		}
		System.out.println("Mul: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n / 10000; i++) {
			x = NumList.randOdd(this.len);
			y = NumList.randOdd(this.len);
			NumList w = NumList.mulKaratsuba(x, y);
		}
		System.out.println("mulKaratsuba: " + (System.currentTimeMillis() - start));

	}

	@Test
	public void shiftTest () {
		NumList l1;
		UFatInt i1;
		int m;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			m = rand.nextInt(2, 100);
			i1 = new UFatInt(l1);
			NumList w = NumList.mulKaratsuba(l1, NumList.fastPow(new NumList(2), m));

			i1.shiftL(m);

			Assertions.assertEquals(w.toString(), i1.toString());
			System.out.println("Test " + i + " shiftL()");
		}

		for (int i = 0; i < this.n; i++) {
			l1 = NumList.generateOdd(rand.nextInt(2, this.len));
			m = rand.nextInt(2, 100);
			i1 = new UFatInt(l1);
			NumList w = NumList.divide(l1, NumList.fastPow(new NumList(2), m));

			i1.shiftR(m);

			Assertions.assertEquals(w.toString(), i1.toString());
			System.out.println("Test " + i + " shiftR()");
		}

	}

	@Test
	public void algoTest () {

		UFatInt e = new UFatInt(new NumList("9749209442300188085"));
		UFatInt d = new UFatInt(new NumList("147415365464702919742613307226325408573"));
		UFatInt n = new UFatInt(new NumList("273523274130512590397975405396105494153"));

		System.out.println("priv = " + d);
		System.out.println("pub = " + e);
		System.out.println("n = " + n);

		UFatInt msg = new UFatInt(0x90);
		UFatInt cipherText = UFatInt.modPow(msg, e, n);
		System.out.println("msg = " + msg);
		System.out.println("Decrypted = " + UFatInt.modPow(cipherText, d, n));
		System.out.println("encrypted = " + cipherText);

	}


}
