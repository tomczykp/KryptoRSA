package xyz.kryptografia.rsa;

import xyz.kryptografia.rsa.liczby.UFatInt;

import java.util.Base64;

public class Converter {

	public static UFatInt[] splitToNums(byte[] data, int size) {
		size /= 8;
		int n = data.length / size;

		if (data.length % size != 0)
			n++;

		System.out.println("nums = " + n);
		System.out.println("bytes to encode = " + data.length);
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

	public static byte[] compactNums(UFatInt[] nums, int n) {
		n /= 8;
		byte[][] bytes = new byte[nums.length][];
		int size = 0;
		for (int i = 0; i < nums.length; i++) {
//			int j = 0;
//			for (byte b : nums[i].getBytes())
//				bytes[i][j++] = b;
			bytes[i] = nums[i].getBytes();
			size += bytes[i].length;

			if (bytes[i].length != n) {
				System.out.println("Pobrano z liczby: " + bytes[i].length);
//				bytes[i][j] = (byte) 0;
			}
		}

		byte[] wynik = new byte[size];
		size = 0;
		for (byte[] aByte : bytes)
			for (byte b : aByte)
				wynik[size++] = b;


		return wynik;
	}

}
