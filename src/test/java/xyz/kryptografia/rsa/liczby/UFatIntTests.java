package xyz.kryptografia.rsa.liczby;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class UFatIntTests {

	int n = 5000;
	int len = 100;

	public String toLiteral(byte[] bytes) {

		StringBuilder sb = new StringBuilder();
		for (byte b : bytes)
			sb.append(String.format("%02X ", b));

		return sb.toString();
	}

	@Test
	public void comparatorTest() {

		Num l1, l2;
		UFatInt i1, i2;
		long lo1, lo2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			System.out.println("Num1 = " + toLiteral(l1.getBytes()) + "\nufi1 = " + i1.toHex());
			System.out.println("\nnum2 = " + toLiteral(l2.getBytes()) + "\nufi2 = " + i2.toHex());

			Assertions.assertEquals(l1.compareTo(l2), i1.compareTo(i2));
			Assertions.assertEquals(toLiteral(l1.getBytes()), i1.toHex());
			Assertions.assertEquals(l1.toString(), i1.toString());
			Assertions.assertEquals(toLiteral(l2.getBytes()), i1.toHex());
			Assertions.assertEquals(l2.toString(), i2.toString());
			System.out.println("Test " + i + " compare()");
		}

		for (int i = 0; i < this.n; i++) {
			lo1 = rand.nextInt(20091);
			lo2 = rand.nextInt(298);

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
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

			i1 = new UFatInt(l1);
			i2 = new UFatInt(l2);

			UFatInt tmp = UFatInt.add(i1, i2);

			Assertions.assertEquals(Num.add(l1, l2).toString(), tmp.toString());
			Assertions.assertEquals(i1.toString(), l1.toString());
			Assertions.assertEquals(i2.toString(), l2.toString());
			System.out.println("Test " + i + " add()");
		}

		long liczba;
		for (int i = 0; i < this.n; i++) {
			liczba = rand.nextInt(Integer.MAX_VALUE);
			l1 = Num.generateOdd(this.len);
			i1 = new UFatInt(l1);

			UFatInt tmp = UFatInt.add(i1, liczba);

			Assertions.assertEquals(Num.add(l1, liczba).toString(), tmp.toString());
			Assertions.assertEquals(l1.toString(), i1.toString());
			System.out.println("Test " + i + " add()");
		}
	}

	@Test
	public void subTest() {
		Num l1, l2;
		UFatInt i1, i2;
		Random rand = new Random();

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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
			l1 = Num.generateOdd(this.len);
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
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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
			l1 = Num.generateOdd(this.len);
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
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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
			l1 = Num.generateOdd(this.len);
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

			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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

		for (int i = 0; i < this.n; i++) {
			l1 = Num.generateOdd(this.len);
			l2 = Num.generateOdd(this.len);

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
	public void algoTest() {

		byte a = (byte) 0x0B;
		byte b = (byte) 0x0C;

		int d = Byte.compareUnsigned(a, b);
		if (d < 0)
			System.out.println("-1");
		if (d > 0)
			System.out.println("1");
		System.out.println("0");
	}


}
