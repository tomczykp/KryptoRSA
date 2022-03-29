package xyz.kryptografia.rsa;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Num implements Comparable<Num> {

	private List<Integer> liczba;

	public Num() {
		this.setZero();
	}

	public Num(Num l) {
		this.liczba = new ArrayList<>();
		for (Integer i : l.liczba)
			this.liczba.add(i.intValue());
	}

	public Num(long l) {
		this.liczba = Num.add(new Num(), l).liczba;
	}

	private Num(List<Integer> l) {
		this.liczba = l;
	}

	public Num(String s) {
		this.liczba = new ArrayList<>();
		for (int i = 0; i < s.length(); i++) {
			this.liczba.add(0, s.charAt(i) - '0');
		}
	}

	public Num(byte[] d) {
		Num t = new Num();
		Num shift = new Num(256);

		for (byte b : d) {
			t = Num.mulKaratsuba(t, shift);
			t = Num.add(t, Byte.toUnsignedInt(b));
		}
		this.liczba = t.liczba;
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder();
		for (int i = this.liczba.size() - 1; i >= 0; i--) {
			tmp.append(this.get(i));
		}
		return tmp.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Num that)) return false;

		return Objects.equals(that.toString(), this.toString());
	}

	@Override
	public int compareTo(Num l2) {
		int d = this.liczba.size() - l2.liczba.size();
		if (d > 0)
			return 1;
		else if (d < 0)
			return -1;
		else
			for (int i = this.liczba.size() - 1; i >= 0; i--) {
				d = this.get(i) - l2.get(i);
				if (d > 0)
					return 1;
				else if (d < 0)
					return -1;
			}
		return 0;
	}

	private Integer get(int i) {
		return this.liczba.get(i);
	}

	private void setZero() {
		this.liczba = new ArrayList<>();
		this.liczba.add(0);
	}

	private boolean isZero() {
		return (this.liczba.size() == 1 && this.get(0) == 0);
	}

	private Num splitNum(int start, int stop) {

		int s = this.liczba.size();
		List<Integer> lista = new ArrayList<>();
		boolean zero = true;
		for (; start < stop; start++) {
			if (start >= s)
				break;
			zero = false;
			lista.add(this.get(start).intValue());
		}
		if (zero)
			lista.add(0);

		return new Num(lista);
	}

	private void removeLeadingZeros() {
		for (int i = this.liczba.size() - 1; i >= 0; i--) {
			if (this.get(i) != 0)
				break;
			this.liczba.remove(i);
		}

		if (this.liczba.size() == 0)
			this.liczba.add(0);
	}

	private boolean isOdd() {
		return (this.get(0) % 2 == 1);
	}

	public String bin() {

		Num copy = new Num(this);
		String wynik = "";

		while (!copy.isZero()) {
			String k = Num.mod(copy, 2).toString();
			wynik = k + wynik;
			copy = Num.divide(copy, 2);
		}


		return wynik;
	}

	public Num tenPow(int n) {
		for (int i = 0; i < n; i++)
			this.liczba.add(0, 0);

		this.removeLeadingZeros();
		return this;
	}

	public long toLong() {
		Num longNum = new Num(String.valueOf(Long.MAX_VALUE));
		if (this.compareTo(longNum) > 0)
			return 0;
		return Long.parseLong(this.toString());
	}

	public byte[] getBytes() {
		int n = 0;
		Num copy = new Num(this);
		Num tmp = new Num(this);
		Num b = new Num("256");
		while (!tmp.isZero()) {
			n++;
			tmp = Num.divide(tmp, b);
		}

		int size = n / 8;
		if (n % 8 != 0)
			size++;

		System.out.println("Powinno być " + n + " byte");
		byte[] buffer = new byte[n];

		return buffer;
	}

	public static Num add(Num x, Num y) {
		if (y == null && x == null)
			return new Num();
		else if (x == null)
			return new Num(y);
		else if (y == null)
			return new Num(x);

		int d;
		boolean overload = false;
		Num tmp = new Num();
		int sx = x.liczba.size();
		int sy = y.liczba.size();

		for (int i = 0; i < sx || i < sy || overload; i++) {

			if (tmp.liczba.size() <= i)
				tmp.liczba.add(0);
			d = 0;

			if (sx > i)
				d += x.get(i);
			if (sy > i)
				d += y.get(i);

			if (overload)
				d++;

			overload = (d > 9);
			d = d % 10;
			tmp.liczba.set(i, d);
		}

		return tmp;
	}

	public static Num add(Num x, long y) {
		if (y < 0 && x == null)
			return new Num();
		else if (y < 0)
			return new Num(x);
		if (x == null)
			return new Num(y);

		int i = 0;
		int d;
		boolean overload = false;
		Num tmp = new Num();

		int sx = x.liczba.size();

		while (y != 0 || overload || sx > i) {

			if (tmp.liczba.size() <= i)
				tmp.liczba.add(0);

			d = 0;
			if (sx > i)
				d += x.get(i);
			if (y != 0)
				d += (int) (y % 10);
			if (overload)
				d++;

			overload = (d > 9);

			d = d % 10;
			tmp.liczba.set(i, d);
			y = (y - (y % 10)) / 10;
			i++;
		}

		return tmp;
	}

	public static Num subtract(Num x, Num y) {
		if (y == null && x == null)
			return new Num();
		else if (x == null)
			return new Num(y);
		else if (y == null)
			return new Num(x);

		int sx = x.liczba.size();
		int sy = y.liczba.size();

		if (sx < sy) {
			return new Num();
		}

		int d;
		boolean borrow = false;
		Num tmp = new Num();

		for (int i = 0; i < sx || borrow; i++) {

			if (tmp.liczba.size() <= i)
				tmp.liczba.add(0);

			d = 0;
			if (sx <= i && borrow) // już nic nie ma w X, a trzeba pobrać
				return new Num();

			if (borrow)
				d--;

			if (sx > i)
				d += x.get(i);
			if (sy > i)
				d -= y.get(i);

			if (d < 0) {
				borrow = true;
				tmp.liczba.set(i, 10 + d);
			} else {
				borrow = false;
				tmp.liczba.set(i, d);
			}

		}

		tmp.removeLeadingZeros();
		return tmp;
	}

	public static Num subtract(Num x, long y) {
		return Num.subtract(x, new Num(y));
	}

	public static Num mul(Num x, Num y) {

		if ((y.liczba.size() == 1 && y.get(0) == 0)
				|| (x.liczba.size() == 1 && x.get(0) == 0)) {
			return new Num();
		}

		Num suma = new Num();
		int d;
		for (int i = 0; i < y.liczba.size(); i++) {

			d = y.get(i);
			if (d != 0)
				suma = Num.add(suma, Num.mul(x, d).tenPow(i));

		}

		return suma;
	}

	public static Num mul(Num x, long y) {
		if (y < 0 || (x.liczba.size() == 1 && x.get(0) == 0))
			return new Num();

		int d;
		int j = 0;
		Num suma = new Num();

		while (y != 0) {

			d = (int) (y % 10);
			y /= 10;

			for (int i = 0; i < x.liczba.size(); i++) {
				Num tmp = Num.add(new Num(), (long) d * x.get(i));
				tmp.tenPow(j + i);
				suma = Num.add(suma, tmp);
			}
			j++;
		}

		return suma;
	}

	public static Num mulKaratsuba(Num x, Num y) {

		int sx = x.liczba.size();
		int sy = y.liczba.size();
		int N = Math.min(sx, sy);

		/** for small values directly multiply **/
		if (N < 5)
			return Num.mul(x, y);

		/** max length divided, rounded up **/
		N = (N / 2);


		/** compute sub expressions **/
		Num ax = x.splitNum(N, sx);
		Num bx = x.splitNum(0, N);

		Num ay = y.splitNum(N, sy);
		Num by = y.splitNum(0, N);

		/** compute sub expressions **/
		Num z0 = Num.mulKaratsuba(bx, by);  // z0
		Num z1 = Num.mulKaratsuba(           // z1
				Num.add(ax, bx),
				Num.add(ay, by));
		Num z2 = Num.mulKaratsuba(ax, ay);  // z2

		z1 = Num.subtract(z1, z2);
		z1 = Num.subtract(z1, z0);

		z2.tenPow(2 * N);
		z1.tenPow(N);

		return Num.add(z2, Num.add(z0, z1));
	}

	public static Num mulKaratsuba(Num x, long y) {
		return Num.mulKaratsuba(x, new Num(y));
	}

	public static Num fastPow(Num a, long n) {
		if (n == 0)
			return new Num(1);

		Num tmp = Num.fastPow(a, n / 2);
		if (n % 2 == 0)
			return Num.mulKaratsuba(tmp, tmp);
		else
			return Num.mulKaratsuba(a, Num.mulKaratsuba(tmp, tmp));
	}

	public static Num divide(Num x, Num y) {
		if (y.isZero())
			throw new ArithmeticException();

		if (x.isZero())
			return new Num();

		int sx = x.liczba.size();
		int sy = y.liczba.size();
		if (sy > sx) // dzielenie przez większą liczbę
			return new Num();

		Num wynik = new Num();
		Num tmp = new Num();
		int n;
		boolean d;

		for (int i = sx - 1; i >= 0; i--) {

			d = true;
			tmp = Num.add(tmp, x.get(i));

			if (tmp.compareTo(y) >= 0) {
				d = false;
				n = 1;
				while (true) {
					Num s = Num.mulKaratsuba(y, n);
					int t = s.compareTo(tmp);

					if (t == 0)
						break;
					else if (t > 0) {
						n--;
						break;
					} else
						n++;
				}

				wynik.liczba.add(0, n);
				tmp = Num.subtract(tmp, Num.mulKaratsuba(y, n));
			}

			tmp.tenPow(1);
			if (d)
				wynik.tenPow(1);
		}

		wynik.removeLeadingZeros();
		return wynik;
	}

	public static Num divide(Num x, long y) {
		return Num.divide(x, new Num(y));
	}

	public static Num mod(Num x, Num y) {

		Num d = Num.divide(x, y);
		d = Num.mulKaratsuba(y, d);
		d = Num.subtract(x, d);
		return d;
	}

	public static Num mod(Num x, long y) {
		return Num.mod(x, new Num(y));
	}

	public static Num randOdd(int bytes) {
		Num t = Num.fastPow(new Num(2), bytes);
		return Num.generateOdd(t.liczba.size());
	}

	public static Num generateOdd(int size) {
		Random r = new Random();
		List<Integer> s = new ArrayList<>();

		for (int i = 1; i < size - 1; i++) {
			s.add(r.nextInt(10));
		}

		if (s.size() > 1)
			s.add(r.nextInt(9) + 1);
		int[] t = {1, 3, 7, 9};

		s.add(0, t[r.nextInt(4)]);

		return new Num(s);
	}

	public static Num generateFromRange(Num a0, Num a1) {

		Num wynik;
		Random r = new Random();

		int s1 = a0.liczba.size();
		int s2 = a1.liczba.size();
		if (s1 == s2)
			s2++;

		do {
			wynik = Num.generateOdd(r.nextInt(s1, s2));
			wynik = Num.mod(wynik, a1);
		} while (wynik.compareTo(a0) < 0);

		return wynik;
	}

	public static Num modPow(Num a, Num b, Num n) {
		Num res = new Num(1);

		Num x = Num.mod(a, n);
		Num y = new Num(b);

		if (x.isZero())
			return x;

		while (!y.isZero()) {
			if (y.isOdd())
				res = Num.mod(Num.mulKaratsuba(res, x), n);

			y = Num.divide(y, 2);
			x = Num.mod(Num.mulKaratsuba(x, x), n);
		}

		return res;
	}

	public boolean testRabinMiller() {

		if (this.compareTo(new Num(3)) < 0)
			return true;

		Num one = new Num(1);
		Num two = new Num(2);

		Num p_1 = Num.subtract(this, 1);
		String bin = p_1.bin();
		int size = bin.length();

		int k = 0;
		while (bin.charAt(size - k - 1) == '0')
			k++;


//		Num a = Num.generateFromRange(two, p_1);
		Num a = two;
		Num m = Num.divide(p_1, Num.fastPow(two, k));
		Num x = Num.modPow(a, m, this);

		if (x.equals(p_1) || x.equals(one))
			return true;

		for (int i = 0; i < k - 1; i++) {
			x = Num.modPow(x, two, this);

			if (x.equals(one))
				return false;
			else if (x.equals(p_1))
				return true;
		}
		return false;
	}

	public static Num gcd(Num a, Num b) {

		Num cA = new Num(a);
		Num cB = new Num(b);

		while (!cB.isZero()) {
			Num tmp = cB;
			cB = Num.mod(cA, cB);
			cA = tmp;
		}

		return cA;
	}

	public static Num modInverse(Num a0, Num m0) {

		Num one = new Num(1);
		Num x = new Num(1), y = new Num();
		Num m = new Num(m0);
		Num a = new Num(a0);
		boolean xNeg = false, yNeg = false, tNeg;

		if (m.equals(one))
			return new Num();

		while (a.compareTo(one) > 0) {

			Num q = Num.divide(a, m);
			Num t = new Num(m);

			m = Num.mod(a, m);
			a = new Num(t);

			tNeg = yNeg;
			t = new Num(y);

			Num tmp = Num.mulKaratsuba(q, y);

			if (yNeg) {
				if (xNeg) {
					//	y = q*y - x
					if (tmp.compareTo(x) > 0) {
						y = Num.subtract(tmp, x);
						yNeg = false;
					} else {
						y = Num.subtract(x, tmp);
						yNeg = true;
					}
				} else {
					// y = q*y + x
					y = Num.add(tmp, x);
					yNeg = false;
				}
			} else {
				if (xNeg) {
					// y = - q*y - x
					y = Num.add(tmp, x);
					yNeg = true;
				} else {
					// y = -q*y + x
					if (tmp.compareTo(x) > 0) {
						y = Num.subtract(tmp, x);
						yNeg = true;
					} else {
						y = Num.subtract(x, tmp);
						yNeg = false;
					}
				}
			}

			xNeg = tNeg;
			x = new Num(t);
		}

		if (xNeg)
			x = Num.subtract(m0, x);

		return x;
	}


}


