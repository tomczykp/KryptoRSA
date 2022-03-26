package xyz.kryptografia.rsa;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumTests {

	static boolean isPrime(int n) {
		// Corner case
		if (n <= 1)
			return false;

		// Check from 2 to n-1
		for (int i = 2; i < n; i++)
			if (n % i == 0)
				return false;

		return true;
	}

	static long power(int x, int y, int p) {
		int res = 1; // Initialize result

		x = x % p; // Update x if it is more than or
		// equal to p

		if (x == 0)
			return 0; // In case x is divisible by p;

		while (y > 0) {

			// If y is odd, multiply x with result
			if ((y & 1) != 0)
				res = (res * x) % p;

			// y must be even now
			y = y >> 1; // y = y/2
			x = (x * x) % p;
		}
		return res;
	}


	@Test
	public void addTest() {
		Num l1;
		Num l2;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new Num(i1);
			l2 = new Num(i2);

			Num tmp = Num.add(l1, l2);
			Assertions.assertEquals(String.valueOf(i1 + i2), tmp.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " add()");
		}

		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new Num(i1);

			Num tmp = Num.add(l1, i2);

			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i1 + i2), tmp.toString());
			System.out.println("Test " + i + " add()");
		}
	}

	@Test
	public void subTest() {
		Num l1;
		Num l2;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new Num(i1);
			l2 = new Num(i2);

			Num s = Num.subtract(l1, l2);

			long tmp = i1 - i2;
			if (tmp < 0)
				tmp = 0;
			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " subtract()");
		}

		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new Num(i1);

			Num s = Num.subtract(l1, i2);
			long tmp = i1 - i2;
			if (tmp < 0)
				tmp = 0;
			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			System.out.println("Test " + i + " subtract()");
		}
	}

	@Test
	public void mul1Test() {
		Num l1;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new Num(i1);

			Num s = Num.mul(l1, i2);
			long tmp = i1 * i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			System.out.println("Test " + i + " mul()");
		}

		long tmp = 100000;
		l1 = new Num(1);

		for (int i = 0; i < 40; i++)
			l1 = Num.mul(l1, tmp);

		String s = "1" + "0".repeat(200);
		Assertions.assertEquals(s, l1.toString());
	}

	@Test
	public void mul2Test() {
		Num l1;
		Num l2;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new Num(i1);
			l2 = new Num(i2);

			Num s = Num.mul(l1, l2);
			long tmp = i1 * i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " mul()");
		}

		long tmp = 100000;
		l2 = new Num(tmp);
		l1 = new Num(1);

		for (int i = 0; i < 40; i++)
			l1 = Num.mul(l1, l2);

		String s = "1" + "0".repeat(200);
		Assertions.assertEquals(s, l1.toString());

		tmp = 999999999;
		l1 = Num.mul(new Num(tmp), tmp);

		s = "999999998000000001";
		Assertions.assertEquals(s, l1.toString());

	}

	@Test
	public void karatsubaTest() {
		Num l1;
		Num l2;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new Num(i1);
			l2 = new Num(i2);

			Num s = Num.mulKaratsuba(l1, l2);
			long tmp = i1 * i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " mulKaratsuba()");
		}

		String s;
		s = "1" + "9".repeat(100);
		l2 = new Num(s);
		l1 = new Num(s);
		l1 = Num.mulKaratsuba(l1, l2);

		s =
				"399999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999960000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001";
		Assertions.assertEquals(s, l1.toString());

		long tmp = 999999999;
		l1 = Num.mulKaratsuba(new Num(tmp), tmp);

		s = "999999998000000001";
		Assertions.assertEquals(s, l1.toString());

	}

	@Test
	public void div1Test() {
		Num l1;
		Num l2;

		long i1, i2;
		Random rand = new Random();

		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new Num(i1);
			l2 = new Num(i2);

			Num s = Num.divide(l1, l2);
			long tmp = i1 / i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " divide()");
		}
	}

	@Test
	public void div2Test() {
		Num l1;

		long i1, i2;
		Random rand = new Random();

		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new Num(i1);

			Num s = Num.divide(l1, i2);
			long tmp = i1 / i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			System.out.println("Test " + i + " divide()");
		}
	}

	@Test
	public void modTest() {
		Num l1;
		Num l2;

		long i1, i2;
		Random rand = new Random();

		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new Num(i1);
			l2 = new Num(i2);

			Num s = Num.mod(l1, l2);
			long tmp = i1 % i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " mod()");
		}
	}

	@Test
	public void modPowTest() {
		Num l1;
		Num l2;
		Num two = new Num(2);

		int i1, i2;
		Random rand = new Random();

		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(1, Short.MAX_VALUE);
			i2 = rand.nextInt(1, Short.MAX_VALUE);

			l1 = new Num(i1);
			l2 = new Num(i2);

			System.out.println("Test " + i + " modPow()\n 2 ^ " + l1 + " mod " + l2);
			Num s = Num.modPow(two, l1, l2);
			long tmp = power(2, i1, i2);

			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			Assertions.assertEquals(String.valueOf(tmp), s.toString());
		}
	}

	@Test
	public void binTest() {
		Num l1;
		int i1;
		Random rand = new Random();

		for (int i = 0; i < 5000; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new Num(i1);

			Assertions.assertEquals(Integer.toBinaryString(i1), l1.bin());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
		}
	}

	@Test
	public void genRangeTest() {

		Num p, k;

		for (int i = 0; i < 100; i++) {
			p = Num.generateOdd(18);
			k = Num.generateOdd(20);
			System.out.println("Nowa para: " + p + " " + k);

			for (int j = 0; j < 5000; j++) {
				Num tmp = Num.generateFromRange(p, k);
				System.out.println("Test dla " + tmp);
				Assertions.assertTrue(tmp.compareTo(p) > 0);
				Assertions.assertTrue(tmp.compareTo(k) < 0);
			}
		}

	}

	@Test
	public void prime1Test() {
		Num l1;
//        int i1;
//        Random rand = new Random();

		for (int i = 100; i < 5000; i++) {
//            i1 = rand.nextInt(i);

			l1 = new Num(i);
			System.out.println("Następna liczba: " + i);
			Assertions.assertEquals(isPrime(i), l1.testRabinMiller());
			Assertions.assertEquals(String.valueOf(i), l1.toString());
		}

	}

	@Test
	public void mainTest() {

//		Num a = Num.generateOdd(70);
//		Num b = Num.generateOdd(70);
//		Num n = Num.generateOdd(70);
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(n);

//		Num.modPow(a, b, n);

		System.out.println(new Num(Num.randOdd(64)).testRabinMiller());

	}

}

