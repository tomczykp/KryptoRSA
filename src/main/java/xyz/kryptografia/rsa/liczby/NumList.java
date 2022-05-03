package xyz.kryptografia.rsa.liczby;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NumList implements Comparable<NumList> {

	private List<Integer> liczba;

	public NumList() {
		this.setZero();
	}

	public NumList(NumList l) {
		this.liczba = new ArrayList<>();
		for (Integer i : l.liczba)
			this.liczba.add(i.intValue());
	}

	public NumList(long l) {
		this.liczba = NumList.add(new NumList(), l).liczba;
	}

	private NumList(List<Integer> l) {
		this.liczba = l;
	}

	public NumList(String s) {
		this.liczba = new ArrayList<>();
		for (int i = 0; i < s.length(); i++) {
			this.liczba.add(0, s.charAt(i) - '0');
		}
	}

	public NumList(byte[] d) {
		NumList t = new NumList();
		NumList shift = new NumList(256);

		for (int i = d.length - 1; i >= 0; i--) {
			byte b = d[i];
			t = NumList.mulKaratsuba(t, shift);
			t = NumList.add(t, Byte.toUnsignedInt(b));
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
		if (!(o instanceof NumList that)) return false;

		return Objects.equals(that.toString(), this.toString());
	}

	@Override
	public int compareTo(NumList l2) {
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

	public Integer get(int i) {
		return this.liczba.get(i);
	}

	public int size() {
		return this.liczba.size();
	}

	private void setZero() {
		this.liczba = new ArrayList<>();
		this.liczba.add(0);
	}

	public boolean isZero() {
		return (this.liczba.size() == 1 && this.get(0) == 0);
	}

	private NumList splitNum(int start, int stop) {

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

		return new NumList(lista);
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

		NumList copy = new NumList(this);
		String wynik = "";

		while (!copy.isZero()) {
			String k = NumList.mod(copy, 2).toString();
			wynik = k + wynik;
			copy = NumList.divide(copy, 2);
		}


		return wynik;
	}

	public NumList tenPow(int n) {
		for (int i = 0; i < n; i++)
			this.liczba.add(0, 0);

		this.removeLeadingZeros();
		return this;
	}

	public long toLong() {
		NumList longNum = new NumList(String.valueOf(Long.MAX_VALUE));
		if (this.compareTo(longNum) > 0)
			return 0;
		return Long.parseLong(this.toString());
	}

	public byte[] getBytes() {

		int n = 0;
		NumList copy = new NumList(this);
		NumList b = new NumList("256");

		while (!copy.isZero()) {
			copy = NumList.divide(copy, b);
			n++;
		}

		copy = new NumList(this);
		byte[] buffer = new byte[n];

		for (int i = 0; !copy.isZero(); i++) {
			buffer[i] = (byte) (NumList.mod(copy, b).toLong());
			copy = NumList.divide(copy, b);
		}

		return buffer;
	}

	public static NumList add(NumList x, NumList y) {
		if (y == null && x == null)
			return new NumList();
		else if (x == null)
			return new NumList(y);
		else if (y == null)
			return new NumList(x);

		int d;
		boolean overload = false;
		NumList tmp = new NumList();
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

	public static NumList add(NumList x, long y) {
		if (y < 0 && x == null)
			return new NumList();
		else if (y < 0)
			return new NumList(x);
		if (x == null)
			return new NumList(y);

		int i = 0;
		int d;
		boolean overload = false;
		NumList tmp = new NumList();

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

	public static NumList subtract(NumList x, NumList y) {
		if (y == null && x == null)
			return new NumList();
		else if (x == null)
			return new NumList(y);
		else if (y == null)
			return new NumList(x);

		int sx = x.liczba.size();
		int sy = y.liczba.size();

		if (sx < sy) {
			return new NumList();
		}

		int d;
		boolean borrow = false;
		NumList tmp = new NumList();

		for (int i = 0; i < sx || borrow; i++) {

			if (tmp.liczba.size() <= i)
				tmp.liczba.add(0);

			d = 0;
			if (sx <= i && borrow) // już nic nie ma w X, a trzeba pobrać
				return new NumList();

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

	public static NumList subtract(NumList x, long y) {
		return NumList.subtract(x, new NumList(y));
	}

	public static NumList mul(NumList x, NumList y) {

		if ((y.liczba.size() == 1 && y.get(0) == 0)
				|| (x.liczba.size() == 1 && x.get(0) == 0)) {
			return new NumList();
		}

		NumList suma = new NumList();
		int d;
		for (int i = 0; i < y.liczba.size(); i++) {

			d = y.get(i);
			if (d != 0)
				suma = NumList.add(suma, NumList.mul(x, d).tenPow(i));

		}

		return suma;
	}

	public static NumList mul(NumList x, long y) {
		if (y < 0 || (x.liczba.size() == 1 && x.get(0) == 0))
			return new NumList();

		int d;
		int j = 0;
		NumList suma = new NumList();

		while (y != 0) {

			d = (int) (y % 10);
			y /= 10;

			for (int i = 0; i < x.liczba.size(); i++) {
				NumList tmp = NumList.add(new NumList(), (long) d * x.get(i));
				tmp.tenPow(j + i);
				suma = NumList.add(suma, tmp);
			}
			j++;
		}

		return suma;
	}

	public static NumList mulKaratsuba(NumList x, NumList y) {

		int sx = x.liczba.size();
		int sy = y.liczba.size();
		int N = Math.min(sx, sy);

		/** for small values directly multiply **/
		if (N < 5)
			return NumList.mul(x, y);

		/** max length divided, rounded up **/
		N = (N / 2);


		/** compute sub expressions **/
		NumList ax = x.splitNum(N, sx);
		NumList bx = x.splitNum(0, N);

		NumList ay = y.splitNum(N, sy);
		NumList by = y.splitNum(0, N);

		/** compute sub expressions **/
		NumList z0 = NumList.mulKaratsuba(bx, by);  // z0
		NumList z1 = NumList.mulKaratsuba(           // z1
				NumList.add(ax, bx),
				NumList.add(ay, by));
		NumList z2 = NumList.mulKaratsuba(ax, ay);  // z2

		z1 = NumList.subtract(z1, z2);
		z1 = NumList.subtract(z1, z0);

		z2.tenPow(2 * N);
		z1.tenPow(N);

		return NumList.add(z2, NumList.add(z0, z1));
	}

	public static NumList mulKaratsuba(NumList x, long y) {
		return NumList.mulKaratsuba(x, new NumList(y));
	}

	public static NumList fastPow(NumList a, long n) {
		if (n == 0)
			return new NumList(1);

		NumList tmp = NumList.fastPow(a, n / 2);
		if (n % 2 == 0)
			return NumList.mulKaratsuba(tmp, tmp);
		else
			return NumList.mulKaratsuba(a, NumList.mulKaratsuba(tmp, tmp));
	}

	public static NumList divide(NumList x, NumList y) {
		if (y.isZero())
			throw new ArithmeticException();

		if (x.isZero())
			return new NumList();

		int sx = x.liczba.size();
		int sy = y.liczba.size();
		if (sy > sx) // dzielenie przez większą liczbę
			return new NumList();

		NumList wynik = new NumList();
		NumList tmp = new NumList();
		int n;
		boolean d;

		for (int i = sx - 1; i >= 0; i--) {

			d = true;
			tmp = NumList.add(tmp, x.get(i));

			if (tmp.compareTo(y) >= 0) {
				d = false;
				n = 1;
				while (true) {
					NumList s = NumList.mulKaratsuba(y, n);
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
				tmp = NumList.subtract(tmp, NumList.mulKaratsuba(y, n));
			}

			tmp.tenPow(1);
			if (d)
				wynik.tenPow(1);
		}

		wynik.removeLeadingZeros();
		return wynik;
	}

	public static NumList divide(NumList x, long y) {
		return NumList.divide(x, new NumList(y));
	}

	public static NumList mod(NumList x, NumList y) {

		NumList d = NumList.divide(x, y);
		d = NumList.mulKaratsuba(y, d);
		d = NumList.subtract(x, d);
		return d;
	}

	public static NumList mod(NumList x, long y) {
		return NumList.mod(x, new NumList(y));
	}

	public static NumList randOdd(int bytes) {
		NumList t = NumList.fastPow(new NumList(2), bytes);
		return NumList.generateOdd(t.liczba.size());
	}

	public static NumList generateOdd(int size) {
		Random r = new Random();
		List<Integer> s = new ArrayList<>();

		for (int i = 1; i < size - 2; i++)
			s.add(r.nextInt(10));

		if (size > 1)
			s.add(r.nextInt(9) + 1);

		int[] t = {1, 3, 7, 9};
		s.add(0, t[r.nextInt(4)]);

		return new NumList(s);
	}

	public static NumList generateFromRange(NumList a0, NumList a1) {

		NumList wynik;
		Random r = new Random();

		int s1 = a0.liczba.size();
		int s2 = a1.liczba.size();
		if (s1 == s2)
			s2++;

		do {
			wynik = NumList.generateOdd(r.nextInt(s1, s2));
			wynik = NumList.mod(wynik, a1);
		} while (wynik.compareTo(a0) < 0);

		return wynik;
	}

	public static NumList modPow(NumList a, NumList b, NumList n) {
		NumList res = new NumList(1);

		NumList x = NumList.mod(a, n);
		NumList y = new NumList(b);

		if (x.isZero())
			return x;

		while (!y.isZero()) {
			if (y.isOdd())
				res = NumList.mod(NumList.mulKaratsuba(res, x), n);

			y = NumList.divide(y, 2);
			x = NumList.mod(NumList.mulKaratsuba(x, x), n);
		}

		return res;
	}

	public boolean testRabinMiller() {

		if (this.compareTo(new NumList(3)) < 0)
			return true;

		NumList one = new NumList(1);
		NumList two = new NumList(2);

		NumList p_1 = NumList.subtract(this, 1);
		String bin = p_1.bin();
		int size = bin.length();

		int k = 0;
		while (bin.charAt(size - k - 1) == '0')
			k++;


		NumList a = NumList.generateFromRange(two, p_1);
		NumList m = NumList.divide(p_1, NumList.fastPow(two, k));
		NumList x = NumList.modPow(a, m, this);

		if (x.equals(p_1) || x.equals(one))
			return true;

		for (int i = 0; i < k - 1; i++) {
			x = NumList.modPow(x, two, this);

			if (x.equals(one))
				return false;
			else if (x.equals(p_1))
				return true;
		}
		return false;
	}

	public boolean isPrime() {

		for (int i = 0; i < 6; i++)
			if (!this.testRabinMiller())
				return false;

		return true;
	}

	public static NumList gcd(NumList a, NumList b) {

		NumList cA = new NumList(a);
		NumList cB = new NumList(b);

		while (!cB.isZero()) {
			NumList tmp = cB;
			cB = NumList.mod(cA, cB);
			cA = tmp;
		}

		return cA;
	}

	public static NumList modInverse(NumList a0, NumList m0) {

		NumList one = new NumList(1);
		NumList x = new NumList(1), y = new NumList();
		NumList m = new NumList(m0);
		NumList a = new NumList(a0);
		boolean xNeg = false, yNeg = false, tNeg;

		if (m.equals(one))
			return new NumList();

		while (a.compareTo(one) > 0) {

			NumList q = NumList.divide(a, m);
			NumList t = new NumList(m);

			m = NumList.mod(a, m);
			a = new NumList(t);

			tNeg = yNeg;
			t = new NumList(y);

			NumList tmp = NumList.mulKaratsuba(q, y);

			if (yNeg) {
				if (xNeg) {
					//	y = q*y - x
					if (tmp.compareTo(x) > 0) {
						y = NumList.subtract(tmp, x);
						yNeg = false;
					} else {
						y = NumList.subtract(x, tmp);
						yNeg = true;
					}
				} else {
					// y = q*y + x
					y = NumList.add(tmp, x);
					yNeg = false;
				}
			} else {
				if (xNeg) {
					// y = - q*y - x
					y = NumList.add(tmp, x);
					yNeg = true;
				} else {
					// y = -q*y + x
					if (tmp.compareTo(x) > 0) {
						y = NumList.subtract(tmp, x);
						yNeg = true;
					} else {
						y = NumList.subtract(x, tmp);
						yNeg = false;
					}
				}
			}

			xNeg = tNeg;
			x = new NumList(t);
		}

		if (xNeg)
			x = NumList.subtract(m0, x);

		return x;
	}


}


