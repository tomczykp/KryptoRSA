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
			tmp.add(0, bytes[i]);

		this.liczba = tmp;
		this.removeLeadingZeros();
	}

	public UFatInt(long l) {
		this(ByteBuffer.allocate(Long.BYTES).putLong(l).array());
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

		int overflow = 0, sum, i;

		for (i = 0; i < sx || i < sy; i++) {

			sum = 0;
			if (i < sx)
				sum += Byte.toUnsignedInt(x.get(i));
			if (i < sy)
				sum += Byte.toUnsignedInt(y.get(i));

			sum += overflow;

			wynik.liczba.add((byte) sum);
			overflow = (sum >> 8);
		}

		return wynik;
	}

	public static UFatInt add(UFatInt x, long yL) {
		if (x == null)
			return new UFatInt(yL);
		else if (yL == 0)
			return new UFatInt(x);

		UFatInt wynik = new UFatInt();
		byte[] y = ByteBuffer.allocate(Long.BYTES).putLong(yL).array();

		int sx = x.liczba.size();
		int sy = y.length;

		int overflow = 0, sum, i;

		for (i = 0; i < sx || i < sy; i++) {

			sum = 0;
			if (i < sx)
				sum += Byte.toUnsignedInt(x.get(i));
			if (i < sy)
				sum += Byte.toUnsignedInt(y[i]);

			sum += overflow;

			wynik.liczba.add((byte) sum);
			overflow = (sum >> 8);
		}

		return wynik;
	}

	public static UFatInt subtract(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}

	public static UFatInt subtract(UFatInt l1, long l2) {
		return new UFatInt();
	}

	public static UFatInt divide(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}

	public static UFatInt divide(UFatInt l1, long l2) {
		return new UFatInt();
	}

	public static UFatInt mul(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}

	public static UFatInt mul(UFatInt l1, long l2) {
		return new UFatInt();
	}

	public static UFatInt mulKaratsuba(UFatInt l1, UFatInt l2) {
		return new UFatInt();
	}

	public static UFatInt mulKaratsuba(UFatInt l1, long l2) {
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
		if (d > 0) {
			System.out.println("Size 1");
			return 1;
		} else if (d < 0) {
			System.out.println("Size -1");
			return -1;
		} else
			for (int i = this.liczba.size() - 1; i >= 0; i--) {
				d = Byte.compareUnsigned(this.get(i), x.get(i));
				if (d < 0) {
					System.out.println("WTF -1 value");
					return -1;
				}
				if (d > 0) {
					System.out.println("WTF -1 value");
					return 1;
				}
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

			r = (byte) ((this.get(i) & 0x80) >> 7);
			byte t = (byte) (this.get(i) << 1);
			t = (byte) (t & r);
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

}
