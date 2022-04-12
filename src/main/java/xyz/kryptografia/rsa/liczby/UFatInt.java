package xyz.kryptografia.rsa.liczby;

import java.nio.ByteBuffer;
import java.util.*;

public class UFatInt implements Comparable<UFatInt> {

	private List<Byte> liczba;

	// ##########################
	// #      KONSTRUKTORY      #
	// ##########################
	public UFatInt(String s) {
		if (s.matches("[0-9]*")) {

			UFatInt wynik = new UFatInt();
			for (int i = s.length() - 1; i >= 0; i--) {
				wynik = UFatInt.add(wynik, s.charAt(i) - '0');
				wynik = UFatInt.mulKaratsuba(wynik, 10);
			}

			this.liczba = wynik.liczba;
		}

		throw new ArithmeticException();
	}

	public UFatInt(Num n) {
		this(n.getBytes());
	}

	public UFatInt(byte[] bytes) {
		List<Byte> tmp = new ArrayList<>();

		for (int i = 0; i < bytes.length; i++)
			tmp.add(bytes[i]);

		this.liczba = tmp;
	}

	public UFatInt(List<Byte> bytes) {
		List<Byte> tmp = new ArrayList<>();

		for (int i = 0; i < bytes.size(); i++)
			tmp.add(bytes.get(i).byteValue());

		this.liczba = tmp;
	}

	public UFatInt(long l) {
		byte[] bytes = ByteBuffer.allocate(Long.BYTES).putLong(l).array();
		List<Byte> tmp = new ArrayList<>();

		for (int i = 0; i < bytes.length; i++)
			tmp.add(0, bytes[i]);

		this.liczba = tmp;
		this.removeLeadingZeros();
	}

	public UFatInt() {
		this.setZero();
	}

	public UFatInt(UFatInt l) {
		int s = l.liczba.size();
		List<Byte> tmp = new ArrayList<>();

		for (int i = 0; i < s; i++)
			tmp.add(l.get(i));

		this.liczba = tmp;
	}


	// ##########################
	// #   METODY MATEMATYCZNE  #
	// ##########################
	public static UFatInt add(UFatInt x, UFatInt y) {
		if (y == null && x == null)
			return new UFatInt();
		else if (x == null)
			return new UFatInt(y);
		else if (y == null)
			return new UFatInt(x);

		UFatInt wynik = new UFatInt();
		int sx = x.liczba.size();
		int sy = y.liczba.size();

		int overflow = 0, sum;

		for (int i = 0; i < sx || i < sy; i++) {
			if (wynik.liczba.size() <= i)
				wynik.liczba.add((byte) 0);

			sum = 0;
			if (i < sx)
				sum += Byte.toUnsignedInt(x.get(i));
			if (i < sy)
				sum += Byte.toUnsignedInt(y.get(i));

			sum += overflow;

			wynik.liczba.set(i, (byte) sum);
			overflow = (sum >> 8);
		}

		if (overflow != 0) {
			wynik.liczba.add((byte) overflow);
		}

		return wynik;
	}

	public static UFatInt add(UFatInt x, long y) {
		if (x == null)
			return new UFatInt(y);
		else if (y == 0)
			return new UFatInt(x);

		return UFatInt.add(x, new UFatInt(y));
	}

	public static UFatInt subtract(UFatInt x, UFatInt y) {
		if (y == null && x == null)
			return new UFatInt();
		else if (x == null)
			return new UFatInt(y);
		else if (y == null)
			return new UFatInt(x);

		int sx = x.liczba.size();
		int sy = y.liczba.size();

		if (sx < sy) {
			return new UFatInt();
		}

		int sum;
		boolean borrow = false;
		UFatInt wynik = new UFatInt();

		for (int i = 0; i < sx || borrow; i++) {

			if (wynik.liczba.size() <= i)
				wynik.liczba.add((byte) 0);

			sum = 0;
			if (sx <= i && borrow) // już nic nie ma w X, a trzeba pobrać
				return new UFatInt();

			if (sx > i)
				sum += Byte.toUnsignedInt(x.get(i));

			if (sy > i)
				sum -= Byte.toUnsignedInt(y.get(i));

			if (borrow)
				sum--;

			if (sum < 0) {
				borrow = true;
				wynik.liczba.set(i, (byte) (256 + sum));
			} else {
				borrow = false;
				wynik.liczba.set(i, (byte) sum);
			}

		}

		wynik.removeLeadingZeros();
		return wynik;
	}

	public static UFatInt subtract(UFatInt l1, long l2) {
		return UFatInt.subtract(l1, new UFatInt(l2));
	}

	public static UFatInt mul(UFatInt x, UFatInt y) {
		if ((y.liczba.size() == 1 && y.get(0) == 0)
				|| (x.liczba.size() == 1 && x.get(0) == 0))
			return new UFatInt();

		UFatInt suma = new UFatInt();
		UFatInt smallSuma;

		for (int i = y.liczba.size() - 1; i >= 0; i--) {

			int yb = Byte.toUnsignedInt(y.get(i));
			suma.shiftL(8);

			if (yb == 0)
				continue;

			smallSuma = new UFatInt();
			for (int j = x.liczba.size() - 1; j >= 0; j--) {
				smallSuma.shiftL(8);

				int xb = Byte.toUnsignedInt(x.get(j));
				if (xb == 0)
					continue;

				smallSuma = UFatInt.add(smallSuma, xb * yb);
			}

			suma = UFatInt.add(suma, smallSuma);
		}

		return suma;
	}

	public static UFatInt mul(UFatInt l1, long l2) {
		return UFatInt.mul(l1, new UFatInt(l2));
	}

	public static UFatInt mulKaratsuba(UFatInt x, UFatInt y) {
		int sx = x.liczba.size();
		int sy = y.liczba.size();
		int N = Math.min(sx, sy);

		/** for small values directly multiply **/
		if (N < 10)
			return UFatInt.mul(x, y);

		/** max length divided, rounded up **/
		N = (N / 2) + (N % 2);


		/** compute sub expressions **/
		UFatInt ax = x.split(N, sx);
		UFatInt bx = x.split(0, N);

		UFatInt ay = y.split(N, sy);
		UFatInt by = y.split(0, N);

		/** compute sub expressions **/
		UFatInt z0 = UFatInt.mulKaratsuba(bx, by);  // z0
		UFatInt z1 = UFatInt.mulKaratsuba(           // z1
				UFatInt.add(ax, bx),
				UFatInt.add(ay, by));
		UFatInt z2 = UFatInt.mulKaratsuba(ax, ay);  // z2

		z1 = UFatInt.subtract(z1, z2);
		z1 = UFatInt.subtract(z1, z0);

		z2.shiftL(2 * N * 8);
		z1.shiftL(N * 8);

		return UFatInt.add(z2, UFatInt.add(z0, z1));
	}

	public static UFatInt mulKaratsuba(UFatInt l1, long l2) {
		return UFatInt.mulKaratsuba(l1, new UFatInt(l2));
	}

	public static UFatInt divide(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}

	public static UFatInt divide(UFatInt l1, long l2) {
		return new UFatInt();
	}

	public static UFatInt modInverse(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}

	public static UFatInt gcd(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}

	public static UFatInt modPow(UFatInt a, UFatInt b, UFatInt n) {
		return new UFatInt();
	}

	public static UFatInt mod(UFatInt l1, long l2) {
		return new UFatInt();
	}

	public static UFatInt mod(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}


	// ##########################
	// #   METODY NARZĘDZIOWE   #
	// ##########################
	@Override
	public int compareTo(UFatInt x) {

		int d = this.liczba.size() - x.liczba.size();
		if (d > 0)
			return 1;
		else if (d < 0)
			return -1;
		else
			for (int i = this.liczba.size() - 1; i >= 0; i--) {
				d = Byte.compareUnsigned(this.get(i), x.get(i));
				if (d < 0)
					return -1;
				if (d > 0)
					return 1;
			}
		return 0;
	}

	public String toHex() {
		String sb = "";

		for (byte b : this.liczba)
			sb = String.format("%02X ", b) + sb;

		return sb;
	}

	@Override
	public String toString() {
//		return this.toHex();
		return new Num(this.getBytes()).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof UFatInt))
			return false;

		UFatInt that = (UFatInt) o;

		return Objects.equals(that.liczba, this.liczba);
	}

	// ##########################
	// #   METODY POMOCNICZE    #
	// ##########################
	public void shiftL(int n) {

		int full = n / 8;
		int rem = n % 8;

		for (int i = 0; i < rem; i++)
			this.shiftL();

		for (int i = 0; i < full; i++)
			this.liczba.add(0, (byte) 0);

	}

	public void shiftL() {
		byte r = 0;

		for (int i = 0; i < this.liczba.size(); i++) {

			byte t = (byte) (this.get(i) << 1);
			t = (byte) (t | r);
			r = (byte) (this.get(i) >> 7);
			this.liczba.set(i, t);
		}

		if (r != 0)
			this.liczba.add(r);

	}

	public void shiftR() {
		byte r;

		for (int i = this.liczba.size() - 1; i >= 0; i--) {

			r = (byte) (this.get(i) & 0x01);
			byte t = (byte) (this.get(i) >> 1);
			t = (byte) (t & (r << 7));
			this.liczba.set(i, t);
		}

		this.removeLeadingZeros();
	}

	public void shiftR(int n) {

		int full = n / 8;
		int rem = n % 8;

		for (int i = 0; i < full; i++)
			this.liczba.remove(0);

		for (int i = 0; i < rem; i++)
			this.shiftR();
	}

	public void setZero() {
		this.liczba = new ArrayList<>();
		this.liczba.add((byte) 0);
	}

	public void removeLeadingZeros() {
		for (int i = this.liczba.size() - 1; i >= 0; i--) {
			if (this.liczba.get(i) != 0)
				break;
			this.liczba.remove(i);
		}

		if (this.liczba.size() == 0)
			this.liczba.add((byte) 0);
	}

	public byte[] getBytes() {
		byte[] t = new byte[this.liczba.size()];
		for (int i = 0; i < this.liczba.size(); i++)
			t[i] = this.liczba.get(i);

		return t;
	}

	public byte get(int i) {
		return this.liczba.get(i);
	}

	private UFatInt split(int start, int stop) {
		return new UFatInt(this.liczba.subList(start, stop));
	}

}
