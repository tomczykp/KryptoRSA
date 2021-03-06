package xyz.kryptografia.rsa.liczby;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class NumListTests {

	int n = 20000;
	int nP = 10000;

	public static boolean isPrime(int n) {
		// Corner case
		if (n <= 1)
			return false;

		// Check from 2 to n-1
		for (int i = 2; i < n; i++)
			if (n % i == 0)
				return false;

		return true;
	}

	public static long modPow(int x, int y, int p) {
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

	public static long GCD(long a, long b) {
		while (b != 0) {
			long tmp = b;
			b = a % b;
			a = tmp;
		}

		return a;
	}

	public static int modInverse(int a, int m) {
		int m0 = m;
		int y = 0, x = 1;

		if (m == 1)
			return 0;

		while (a > 1) {
			// q is quotient
			int q = a / m;

			int t = m;

			// m is remainder now, process
			// same as Euclid's algo
			m = a % m;
			a = t;
			t = y;

			// Update x and y
			y = x - q * y;
			x = t;
		}

		// Make x positive
		if (x < 0)
			x += m0;

		return x;
	}

	public static long convertToLong(byte[] bytes) {
		long value = 0L;

		// Iterating through for loop
		for (byte b : bytes) {
			// Shifting previous value 8 bits to right and
			// add it with next value

			value = (value << 8) + Byte.toUnsignedInt(b);
		}

		return value;
	}

	@Test
	public void addTest() {
		NumList l1;
		NumList l2;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new NumList(i1);
			l2 = new NumList(i2);

			NumList tmp = NumList.add(l1, l2);
			Assertions.assertEquals(String.valueOf(i1 + i2), tmp.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " add()");
		}

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new NumList(i1);

			NumList tmp = NumList.add(l1, i2);

			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i1 + i2), tmp.toString());
			System.out.println("Test " + i + " add()");
		}
	}

	@Test
	public void subTest() {
		NumList l1;
		NumList l2;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new NumList(i1);
			l2 = new NumList(i2);

			NumList s = NumList.subtract(l1, l2);

			long tmp = i1 - i2;
			if (tmp < 0)
				tmp = 0;
			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " subtract()");
		}

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);
			l1 = new NumList(i1);

			NumList s = NumList.subtract(l1, i2);
			long tmp = i1 - i2;
			if (tmp < 0)
				tmp = 0;
			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			System.out.println("Test " + i + " subtract()");
		}
	}

	@Test
	public void mulTest() {
		NumList l1, l2, s;

		long i1, i2, tmp;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new NumList(i1);

			s = NumList.mul(l1, i2);
			tmp = i1 * i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			System.out.println("Test " + i + " mul()");
		}

		tmp = 100000;
		l1 = new NumList(1);

		for (int i = 0; i < 40; i++)
			l1 = NumList.mul(l1, tmp);

		String str = "1" + "0".repeat(200);
		Assertions.assertEquals(str, l1.toString());

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new NumList(i1);
			l2 = new NumList(i2);

			s = NumList.mul(l1, l2);
			tmp = i1 * i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " mul()");
		}

		tmp = 100000;
		l2 = new NumList(tmp);
		l1 = new NumList(1);

		for (int i = 0; i < 40; i++)
			l1 = NumList.mul(l1, l2);

		str = "1" + "0".repeat(200);
		Assertions.assertEquals(str, l1.toString());

		tmp = 999999999;
		l1 = NumList.mul(new NumList(tmp), tmp);

		str = "999999998000000001";
		Assertions.assertEquals(str, l1.toString());
	}

	@Test
	public void karatsubaTest() {
		NumList l1;
		NumList l2;

		long i1, i2;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new NumList(i1);
			l2 = new NumList(i2);

			NumList s = NumList.mulKaratsuba(l1, l2);
			long tmp = i1 * i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " mulKaratsuba()");
		}

		String s;
		s = "1" + "9".repeat(100);
		System.out.println(s);
		l2 = new NumList(s);
		l1 = new NumList(s);
		l1 = NumList.mulKaratsuba(l1, l2);

		s =
				"399999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999960000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001";
		Assertions.assertEquals(s, l1.toString());

		long tmp = 999999999;
		l1 = NumList.mulKaratsuba(new NumList(tmp), tmp);

		s = "999999998000000001";
		Assertions.assertEquals(s, l1.toString());

	}

	@Test
	public void divTest() {
		NumList l1, l2, s;

		long i1, i2, tmp;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(1, Integer.MAX_VALUE);

			l1 = new NumList(i1);
			l2 = new NumList(i2);

			s = NumList.divide(l1, l2);
			tmp = i1 / i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " divide()");
		}

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(1, Integer.MAX_VALUE);

			l1 = new NumList(i1);

			s = NumList.divide(l1, i2);
			tmp = i1 / i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			System.out.println("Test " + i + " divide()");
		}
	}

	@Test
	public void modTest() {
		NumList l1;
		NumList l2;

		long i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(1, Integer.MAX_VALUE);

			l1 = new NumList(i1);
			l2 = new NumList(i2);

			NumList s = NumList.mod(l1, l2);
			long tmp = i1 % i2;

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " mod()");
		}
	}

	@Test
	public void modPowTest() {
		NumList l1;
		NumList l2;
		NumList two = new NumList(2);

		int i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(1, Short.MAX_VALUE);
			i2 = rand.nextInt(1, Short.MAX_VALUE);

			l1 = new NumList(i1);
			l2 = new NumList(i2);

			System.out.println("Test " + i + " modPow()\n 2 ^ " + l1 + " mod " + l2);
			NumList s = NumList.modPow(two, l1, l2);
			long tmp = modPow(2, i1, i2);

			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			Assertions.assertEquals(String.valueOf(tmp), s.toString());
		}
	}

	@Test
	public void binTest() {
		NumList l1;
		int i1;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			System.out.println("Test " + i + " binTest()");
			i1 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new NumList(i1);

			Assertions.assertEquals(Integer.toBinaryString(i1), l1.bin());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
		}
	}

	@Test
	public void genRangeTest() {

		NumList p, k;

		for (int i = 0; i < 100; i++) {
			System.out.println("Test " + i + " genRangeTest()");
			p = NumList.generateOdd(18);
			k = NumList.generateOdd(20);

			for (int j = 0; j < this.n; j++) {
				NumList tmp = NumList.generateFromRange(p, k);
				Assertions.assertTrue(tmp.compareTo(p) > 0);
				Assertions.assertTrue(tmp.compareTo(k) < 0);
			}
		}

	}

	@Test
	public void primeTest() {
		NumList l1;
		int i1;
		Random rand = new Random();

		for (int i = 0; i < this.nP; i++) {
			i1 = rand.nextInt(3, Integer.MAX_VALUE);

			l1 = new NumList(i1);
			System.out.println("Test " + i + " prime(): " + i1);
			Assertions.assertEquals(isPrime(i1), l1.isPrime());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
		}

	}

	@Test
	public void gcdTest() {

		NumList l1;
		NumList l2;

		long i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(Integer.MAX_VALUE);
			i2 = rand.nextInt(Integer.MAX_VALUE);

			l1 = new NumList(i1);
			l2 = new NumList(i2);

			NumList s = NumList.gcd(l1, l2);
			long tmp = GCD(i1, i2);

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " gcd()");
		}
	}

	@Test
	public void modInverseTest() {

		NumList l1;
		NumList l2;

		int i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextInt(1, Integer.MAX_VALUE);
			i2 = rand.nextInt(1, Integer.MAX_VALUE);

			l1 = new NumList(i1);
			l2 = new NumList(i2);
			if (GCD(i1, i2) != 1)
				continue;

			long tmp = modInverse(i1, i2);
			NumList s = NumList.modInverse(l1, l2);

			Assertions.assertEquals(String.valueOf(tmp), s.toString());
			Assertions.assertEquals(String.valueOf(i1), l1.toString());
			Assertions.assertEquals(String.valueOf(i2), l2.toString());
			System.out.println("Test " + i + " modInverse()");
		}
	}

	@Test
	public void toLongTest() {

		NumList l1;
		long i1;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			i1 = rand.nextLong(Long.MAX_VALUE);
			l1 = new NumList(i1);

			Assertions.assertEquals(i1, l1.toLong());
			System.out.println("Test " + i + " toLong()");
		}
	}

	@Test
	public void algoTest() {

		NumList msg = new NumList("8675472634406074469");
		NumList n = new NumList("6876277378425123763");
		NumList d = new NumList("3175897683356795561");
		NumList e = new NumList("854204321");

		NumList x = NumList.modPow(msg, e, n);
		NumList m = NumList.modPow(x, d, n);

		System.out.println("X = " + x + ", msg = " + m);

	}

}

