package xyz.kryptografia.rsa.liczby;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

	public UFatInt(NumList n) {
		UFatInt w = new UFatInt();
		for (int i = n.size() - 1; i >= 0; i--) {
			w = UFatInt.add(w.tenPow(), n.get(i));
		}

		this.liczba = w.liczba;
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
				sum += (x.get(i) & 0xFF);
			if (i < sy)
				sum += (y.get(i) & 0xFF);

			sum += overflow;

			wynik.liczba.set(i, (byte) sum);
			overflow = (sum >> 8);
		}

		if (overflow != 0)
			wynik.liczba.add((byte) overflow);

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
				sum += (x.get(i) & 0xFF);

			if (sy > i)
				sum -= (y.get(i) & 0xFF);

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

		return wynik.removeLeadingZeros();
	}

	public static UFatInt subtract(UFatInt l1, long l2) {
		return UFatInt.subtract(l1, new UFatInt(l2));
	}

	public static UFatInt mul(UFatInt x, UFatInt y) {
		if ((y.liczba.size() == 1 && y.get(0) == 0)
				|| (x.liczba.size() == 1 && x.get(0) == 0))
			return new UFatInt();

		int d;
		UFatInt suma = new UFatInt();
		for (int i = y.liczba.size() - 1; i >= 0; i--) {

			d = y.get(i) & 0xFF;

			suma.liczba.add(0, (byte) 0);
			if (d != 0)
				suma = UFatInt.add(suma, UFatInt.mul(x, d));
		}

		return suma.removeLeadingZeros();
	}

	public static UFatInt mul(UFatInt x, long y) {
		if (y < 0 || (x.liczba.size() == 1 && x.get(0) == 0))
			return new UFatInt();

		int s = x.liczba.size();
		UFatInt suma = new UFatInt();

		for (int i = s - 1; i >= 0; i--) {
			suma.liczba.add(0, (byte) 0);
			suma = UFatInt.add(suma, (x.get(i) & 0xFF) * y);
		}

		return suma.removeLeadingZeros();
	}

	public static UFatInt mulKaratsuba(UFatInt x, UFatInt y) {
		int sx = x.getBitSize();
		int sy = y.getBitSize();
		int N = Math.min(sx, sy);

		if (N < 8)
			return UFatInt.mul(x, y);

		N = (N / 2);


		/** compute sub expressions **/
		UFatInt[] t = x.split(N);
		UFatInt ax = t[0];
		UFatInt bx = t[1];

		t = y.split(N);
		UFatInt ay = t[0];
		UFatInt by = t[1];

		/** compute sub expressions **/
		UFatInt z0 = UFatInt.mulKaratsuba(bx, by);  // z0
		UFatInt z1 = UFatInt.mulKaratsuba(           // z1
				UFatInt.add(ax, bx),
				UFatInt.add(ay, by));
		UFatInt z2 = UFatInt.mulKaratsuba(ax, ay);  // z2

		z1 = UFatInt.subtract(UFatInt.subtract(z1, z2), z0);

		z2.shiftL(2 * N);
		z1.shiftL(N);

		return UFatInt.add(z2, UFatInt.add(z0, z1));
	}

	public static UFatInt mulKaratsuba(UFatInt l1, long l2) {
		return UFatInt.mulKaratsuba(l1, new UFatInt(l2));
	}

	public static UFatInt divide(UFatInt x, UFatInt y) {
		if (y.isZero())
			throw new ArithmeticException();

		if (x.isZero())
			return new UFatInt();

		int sx = x.liczba.size();
		int sy = y.liczba.size();
		if (sy > sx) // dzielenie przez większą liczbę
			return new UFatInt();

		UFatInt wynik = new UFatInt();
		UFatInt subsum = new UFatInt();
		int n;
		boolean d;

		for (int i = sx - 1; i >= 0; i--) {

			d = true;
			subsum = UFatInt.add(subsum, (x.get(i) & 0xff));

			if (subsum.compareTo(y) >= 0) {
				d = false;
				n = 1;
				while (true) {
					UFatInt s = UFatInt.mulKaratsuba(y, n);
					int t = s.compareTo(subsum);

					if (t == 0)
						break;
					else if (t > 0) {
						n--;
						break;
					} else
						n++;
				}

				wynik.liczba.add(0, (byte) n);
				subsum = UFatInt.subtract(subsum, UFatInt.mulKaratsuba(y, n));
			}

			subsum.shiftL(8);
			if (d)
				wynik.shiftL(8);
		}

		return wynik.removeLeadingZeros();
	}

	public static UFatInt divide(UFatInt l1, long l2) {
		return UFatInt.divide(l1, new UFatInt(l2));
	}

	public static UFatInt divShift(UFatInt x, UFatInt y) {
		if (y.isZero())
			throw new ArithmeticException();

		UFatInt tmp = new UFatInt();
		UFatInt quot = new UFatInt();

		for (int i = x.getBitSize(); i >= 0; i--) {

			UFatInt t = UFatInt.add(tmp, new UFatInt(y).shiftL(i));

			if (x.compareTo(t) >= 0) {
				tmp = t;
				quot = UFatInt.setBit(quot, i);
			}

		}
		return quot.removeLeadingZeros();
	}

	public static UFatInt divShift(UFatInt x, long y) {
		if (y == 0)
			throw new ArithmeticException();

		return UFatInt.divShift(x, new UFatInt(y));
	}

	public static UFatInt setBit(UFatInt x, int i) {
		int c = i / 8;
		int r = i % 8;
		UFatInt w = new UFatInt(x);
		for (int j = 0; j < c + 1 - x.liczba.size(); j++)
			w.liczba.add((byte) 0);

		w.liczba.set(c, (byte) (w.get(c) | (1 << r)));

		return w;
	}

	public static UFatInt mod(UFatInt l1, long l2) {
		return UFatInt.mod(l1, new UFatInt(l2));
	}

	public static UFatInt mod(UFatInt x, UFatInt y) {
		UFatInt d = UFatInt.divShift(x, y);
		d = UFatInt.mulKaratsuba(y, d);
		return UFatInt.subtract(x, d);
	}

	public static UFatInt fastPow(UFatInt a, long n) {
		if (n == 0)
			return new UFatInt(1);

		UFatInt tmp = UFatInt.fastPow(a, n / 2);
		if (n % 2 == 0)
			return UFatInt.mulKaratsuba(tmp, tmp);
		else
			return UFatInt.mulKaratsuba(a, UFatInt.mulKaratsuba(tmp, tmp));
	}

	public static UFatInt modInverse(UFatInt a0, UFatInt m0) {
		UFatInt one = new UFatInt(1);
		UFatInt x = new UFatInt(1), y = new UFatInt();
		UFatInt m = new UFatInt(m0);
		UFatInt a = new UFatInt(a0);
		boolean xNeg = false, yNeg = false, tNeg;

		if (m.equals(one))
			return new UFatInt();

		while (a.compareTo(one) > 0) {

			UFatInt q = UFatInt.divShift(a, m);
			UFatInt t = new UFatInt(m);

			m = UFatInt.mod(a, m);
			a = new UFatInt(t);

			tNeg = yNeg;
			t = new UFatInt(y);

			UFatInt tmp = UFatInt.mulKaratsuba(q, y);

			if (yNeg) {
				if (xNeg) {
					//	y = q*y - x
					if (tmp.compareTo(x) > 0) {
						y = UFatInt.subtract(tmp, x);
						yNeg = false;
					} else {
						y = UFatInt.subtract(x, tmp);
						yNeg = true;
					}
				} else {
					// y = q*y + x
					y = UFatInt.add(tmp, x);
					yNeg = false;
				}
			} else {
				if (xNeg) {
					// y = - q*y - x
					y = UFatInt.add(tmp, x);
					yNeg = true;
				} else {
					// y = -q*y + x
					if (tmp.compareTo(x) > 0) {
						y = UFatInt.subtract(tmp, x);
						yNeg = true;
					} else {
						y = UFatInt.subtract(x, tmp);
						yNeg = false;
					}
				}
			}

			xNeg = tNeg;
			x = new UFatInt(t);
		}

		if (xNeg)
			x = UFatInt.subtract(m0, x);

		return x;
	}

	public static UFatInt gcd(UFatInt a, UFatInt b) {

		UFatInt cA = new UFatInt(a);
		UFatInt cB = new UFatInt(b);

		while (!cB.isZero()) {
			UFatInt tmp = cB;
			cB = UFatInt.mod(cA, cB);
			cA = tmp;
		}

		return cA;
	}

	public static UFatInt modPow(UFatInt a, UFatInt b, UFatInt n) {
		UFatInt res = new UFatInt(1);

		UFatInt x = UFatInt.mod(a, n);
		UFatInt y = new UFatInt(b);

		if (x.isZero())
			return x;

		while (!y.isZero()) {
			if (y.isOdd())
				res = UFatInt.mod(UFatInt.mulKaratsuba(res, x), n);

			y.shiftR();
			x = UFatInt.mod(UFatInt.mulKaratsuba(x, x), n);
		}

		return res;
	}

	public static UFatInt subOneOdd(UFatInt x) {
		UFatInt l = new UFatInt(x);
		l.liczba.set(0, (byte) (l.liczba.get(0) & 0xfe));

		return l;
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
		return this.toHex();
//		return new NumList(this.getBytes()).toString();
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
	public UFatInt shiftL(int n) {

		int full = n / 8;
		int rem = n % 8;

		for (int i = 0; i < rem; i++)
			this.shiftL();

		for (int i = 0; i < full; i++)
			this.liczba.add(0, (byte) 0);

		return this.removeLeadingZeros();
	}

	private UFatInt shiftL() {
		byte overFlow = 0;

		for (int i = 0; i < this.liczba.size(); i++) {

			byte t = (byte) (this.get(i) << 1);
			t = (byte) (t | overFlow);
			overFlow = (byte) ((this.get(i) & 0x80) >>> 7);
			this.liczba.set(i, t);
		}

		if (overFlow == 0x01)
			this.liczba.add(overFlow);

		return this;
	}

	private UFatInt shiftR() {
		byte borrow = 0;

		for (int i = this.liczba.size() - 1; i >= 0; i--) {

			byte t = (byte) (this.get(i) >>> 1);
			if (borrow == 1)
				t = (byte) (t | (borrow << 7));
			else
				t = (byte) (t & ~(1 << 7));
			borrow = (byte) (this.get(i) & 0x01);
			this.liczba.set(i, t);
		}

		return this.removeLeadingZeros();
	}

	public UFatInt shiftR(int n) {

		int full = n / 8;
		int rem = n % 8;

		for (int i = 0; i < full; i++)
			if (this.liczba.size() != 0)
				this.liczba.remove(0);

		for (int i = 0; i < rem; i++)
			this.shiftR();
		return this;
	}

	private UFatInt setZero() {
		this.liczba = new ArrayList<>();
		this.liczba.add((byte) 0);
		return this;
	}

	private UFatInt tenPow() {
		UFatInt w = new UFatInt(this).shiftL(3);
		this.liczba = UFatInt.add(w, new UFatInt(this).shiftL(1)).liczba;
		return this;
	}

	private boolean isOdd() {
		return ((this.liczba.get(0) & 0x01) == 1);
	}

	private boolean isZero() {
		return (this.liczba.size() == 1 && this.get(0) == 0);
	}

	private UFatInt removeLeadingZeros() {
		for (int i = this.liczba.size() - 1; i >= 0; i--) {
			if (this.liczba.get(i) != 0)
				break;
			this.liczba.remove(i);
		}

		if (this.liczba.size() == 0)
			this.liczba.add((byte) 0);
		return this;
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

	private UFatInt[] split(int n) {
		if (n > this.getBitSize())
			return new UFatInt[]{new UFatInt(), new UFatInt(this)};

		UFatInt higher = new UFatInt(this).shiftR(n);
		return new UFatInt[]{
				new UFatInt(higher),
				UFatInt.subtract(this, higher.shiftL(n))
		};
	}

	private int getBitSize() {
		if (this.liczba.size() == 0)
			return 0;

		int s = (this.liczba.size() - 1) * 8;
		int i;
		byte t, b = this.liczba.get(this.liczba.size() - 1);

		for (i = 7; i >= 0; i--) {
			t = (byte) ((b >>> i) & 0x01);
			if (t == 1)
				break;
		}

		return s + i + 1;
	}

	public int len() {
		return this.removeLeadingZeros().liczba.size();
	}

	public static UFatInt randOdd(int n) {
		Random rand = new Random();
		byte[] bytes = new byte[n];
		rand.nextBytes(bytes);

		bytes[0] = (byte) (bytes[0] | 0x01);
		bytes[bytes.length - 1] = (byte) (bytes[bytes.length - 1] | 0x80);

		return new UFatInt(bytes);
	}

	public static UFatInt generateUpTo(UFatInt l) {
		return UFatInt.randOdd(l.getBitSize() / 8);
	}

	public boolean testRabinMiller() {

		if (this.compareTo(new UFatInt(3)) < 0)
			return true;

		UFatInt one = new UFatInt(1);
		UFatInt two = new UFatInt(2);

		UFatInt p_1 = UFatInt.subOneOdd(this);
		UFatInt cp = new UFatInt(p_1);

		int k = 0;
		while ((cp.get(k / 8) & (1 << k % 8)) == 0)
			k++;

		UFatInt a = UFatInt.generateUpTo(p_1);
		UFatInt m = new UFatInt(p_1).shiftR(k);
		UFatInt x = UFatInt.modPow(a, m, this);

		if (x.equals(p_1) || x.equals(one))
			return true;

		for (int i = 0; i < k - 1; i++) {
			x = UFatInt.modPow(x, two, this);
			if (x.equals(one))
				return false;

			else if (x.equals(p_1))
				return true;
		}

		return false;
	}

	public boolean isPrime() {

		for (int i = 0; i < 5; i++)
			if (!this.testRabinMiller())
				return false;

		return true;
	}

}
