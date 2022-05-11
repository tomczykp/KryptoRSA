package xyz.kryptografia.rsa;

import xyz.kryptografia.rsa.liczby.UFatInt;

public class Converter {

	public static UFatInt[] splitToNums (byte[] data, int size) {
		size /= 8;
		int n = data.length / size;

		if (data.length % size != 0)
			n++;

		System.out.println("nums:" + n);
		System.out.println("bytes: " + data.length);
		UFatInt[] wynik = new UFatInt[n];
		for (int i = 0; i < n; i++) {
			byte[] b = new byte[size];

			for (int j = 0; j < size; j++)
				if (size * i + j < data.length)
					b[j] = data[size * i + j];
				else
//					b[j] = 0;
					break;

			wynik[i] = new UFatInt(b);
		}

		return wynik;
	}

	public static byte[] compactNums (UFatInt[] nums, int size) {
		size /= 8;
		byte[] wynik = new byte[nums.length * (size)];

		for (int i = 0; i < nums.length; i++) {
			byte[] b = nums[i].getBytes();
			for (int j = 0; j < size && j < b.length; j++)
				wynik[i * size + j] = b[j];
		}


		// usuwanie trailing zeros
		int len = 0;
		for (int i = wynik.length - 1; i >= 0; i--) {
			if (wynik[i] == 0)
				len++;
			else
				break;
		}

		if (len == 0)
			return wynik;

		System.out.println("Trailing 0 to remove: " + len);
		byte[] w = new byte[wynik.length - len];
		if (wynik.length - len >= 0)
			System.arraycopy(wynik, 0, w, 0, wynik.length - len);

		return w;
	}

}
