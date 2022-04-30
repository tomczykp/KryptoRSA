package xyz.kryptografia.rsa.liczby;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class UFatIntTests {

	int n = 5000;
	int len = 100;

	public String toHex(byte[] bytes) {
		String sb = "";
		for (byte b : bytes)
			sb = String.format("%02X ", b) + sb;

		return sb;
	}


	@Test
	public void constructoTest() {
		Num n1, n2;
		UFatInt f1, f2;
		Random r = new Random();

		for (int i = 0; i < this.n; i++) {
			n1 = Num.generateOdd(r.nextInt(this.len));
			n2 = Num.generateOdd(r.nextInt(this.len));

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
	public void comparatorTest() {

		Num l1, l2;
		UFatInt i1, i2;
		long lo1, lo2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

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
	public void addTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.add(i1, i2);

			Assertions.assertEquals(Num.add(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(toHex(Num.add(l1, l2).getBytes()), tmp.toHex());
			Assertions.assertEquals(toHex(l1.getBytes()), i1.toHex());
			Assertions.assertEquals(toHex(l2.getBytes()), i2.toHex());
			System.out.println("Test " + i + " add()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = Num.generateOdd(this.len);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.add(i1, liczba);

			Assertions.assertEquals(Num.add(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(toHex(l1.getBytes()), i1.toHex());
			System.out.println("Test " + i + " add()");
		}

	}

	@Test
	public void subTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.subtract(i1, i2);

			Assertions.assertEquals(Num.subtract(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " subtract()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = Num.generateOdd(this.len);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.subtract(i1, liczba);

			Assertions.assertEquals(Num.subtract(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " subtract()");
		}
	}

	@Test
	public void mulTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.mul(i1, i2);

			Assertions.assertEquals(Num.mul(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " mul()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = Num.generateOdd(this.len);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.mul(i1, liczba);

			Assertions.assertEquals(Num.mul(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " mul()");
		}
	}

	@Test
	public void karatsubaTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.mulKaratsuba(i1, i2);

			Assertions.assertEquals(Num.mulKaratsuba(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " mulKaratsuba()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.mulKaratsuba(i1, liczba);

			Assertions.assertEquals(Num.mulKaratsuba(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " mulKaratsuba()");
		}
	}

	@Test
	public void divTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.divide(i1, i2);

			Assertions.assertEquals(Num.divide(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " divide()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.divide(i1, liczba);

			Assertions.assertEquals(Num.divide(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " divide()");
		}
	}

	@Test
	public void modTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.mod(i1, i2);

			Assertions.assertEquals(Num.mod(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " mod()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.mod(i1, liczba);

			Assertions.assertEquals(Num.mod(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " mod()");
		}
	}

	@Test
	public void modPowTest() {
		UFatInt i1, i2;
		Random rand = new Random();

		Num l1, l2;

		for (int i = 0; i < this.n; i++) {
			int t = rand.nextInt(Integer.MAX_VALUE);
			Num r1 = new Num(t);
			UFatInt r2 = new UFatInt(t);

			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			System.out.println("Test " + i + " modPow()\n 2 ^ " + l1 + " mod " + l2);
			Num s = Num.modPow(r1, l1, l2);
			UFatInt tmp = UFatInt.modPow(r2, i1, i2);

			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			Assertions.assertEquals(r1.toString(), r2.toString());
			Assertions.assertEquals(s.toString(), tmp.toString());
		}
	}

	@Test
	public void gcdTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.gcd(i1, i2);

			Assertions.assertEquals(Num.gcd(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " gcd()");
		}

	}

	@Test
	public void modInverseTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			l2 = Num.generateOdd(rand.nextInt(2, this.len));

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.modInverse(i1, i2);

			Assertions.assertEquals(Num.modInverse(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " modInverse()");
		}

	}

	@Test
	public void fastPowTest() {
		Num l1;
		UFatInt i1;
		long i2;
		Random rand = new Random();
		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			i2 = rand.nextLong();
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.fastPow(i1, i2);

			Assertions.assertEquals(Num.fastPow(l1, i2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			System.out.println("Test " + i + " modInverse()");
		}

	}

	@Test
	public void timeTest() {

		UFatInt x, y;
		Random r = new Random();
		long start;
		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
			byte[] b = new byte[this.len];
			r.nextBytes(b);
			x = new UFatInt(b);

			r.nextBytes(b);
			y = new UFatInt(b);
			UFatInt w = UFatInt.add(x, y);
		}
		System.out.println("Dodawanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
			byte[] b = new byte[this.len];
			r.nextBytes(b);
			x = new UFatInt(b);

			r.nextBytes(b);
			y = new UFatInt(b);
			UFatInt w = UFatInt.subtract(x, y);
		}
		System.out.println("Odejmowanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
			byte[] b = new byte[this.len];
			r.nextBytes(b);
			x = new UFatInt(b);

			r.nextBytes(b);
			y = new UFatInt(b);
			UFatInt w = UFatInt.mul(x, y);
		}
		System.out.println("Mul: " + (System.currentTimeMillis() - start));


		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
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
	public void timeTestNum() {

		Num x, y;
		long start;
		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
			x = Num.randOdd(this.len);
			y = Num.randOdd(this.len);
			Num w = Num.add(x, y);
		}
		System.out.println("Dodawanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
			x = Num.randOdd(this.len);
			y = Num.randOdd(this.len);
			Num w = Num.subtract(x, y);
		}
		System.out.println("Odejmowanie: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
			x = Num.randOdd(this.len);
			y = Num.randOdd(this.len);
			Num w = Num.mul(x, y);
		}
		System.out.println("Mul: " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < this.n; i++) {
			x = Num.randOdd(this.len);
			y = Num.randOdd(this.len);
			Num w = Num.mulKaratsuba(x, y);
		}
		System.out.println("mulKaratsuba: " + (System.currentTimeMillis() - start));

	}

	@Test
	public void shiftTest() {
		Num l1;
		UFatInt i1;
		int m;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			m = rand.nextInt(2, 100);
			i1 = new UFatInt(l1);
			Num w = Num.mulKaratsuba(l1, Num.fastPow(new Num(2), m));

			i1.shiftL(m);

			Assertions.assertEquals(w.toString(), i1.toString());
			System.out.println("Test " + i + " shiftL()");
		}

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(rand.nextInt(2, this.len));
			m = rand.nextInt(2, 100);
			i1 = new UFatInt(l1);
			Num w = Num.divide(l1, Num.fastPow(new Num(2), m));

			i1.shiftR(m);

			Assertions.assertEquals(w.toString(), i1.toString());
			System.out.println("Test " + i + " shiftR()");
		}

	}

	@Test
	public void algoTest() {

		UFatInt l1 = new UFatInt(0x991278);
		UFatInt h = new UFatInt(l1).shiftR(12);
		UFatInt l = UFatInt.subtract(l1, h.shiftL(12));
		System.out.println("h = " + h.toHex() + ", l = " + l.toHex());
//		l.shiftR();
//		System.out.println("Liczba = " + l.toHex());
//		l.shiftR();
//		System.out.println("Liczba = " + l.toHex());
//		l.shiftR();
//		System.out.println("Liczba = " + l.toHex());
//		l.shiftR();
//		System.out.println("Liczba = " + UFatInt.mulKaratsuba(l1, l2).toHex());

	}


}
