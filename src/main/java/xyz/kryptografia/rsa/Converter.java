package xyz.kryptografia.rsa;

import xyz.kryptografia.rsa.liczby.UFatInt;

import java.util.Base64;

public class Converter {

	// base64_string -> nums
	public static UFatInt[] decode(String data) {

		byte[] tmp = Base64.getDecoder().decode(data);
		String[] t = new String(tmp).split("\n");
		UFatInt[] wynik = new UFatInt[t.length];

		for (int i = 0; i < t.length; i++)
			wynik[i] = new UFatInt(t[i]);

		return wynik;
	}

	// nums -> base64_string
	public static String encode(UFatInt[] nums) {
		String combine = "";
		for (UFatInt n : nums)
			combine += n.toHex() + "\n";

		return new String(Base64.getEncoder().encode(combine.getBytes()));
	}

	public static UFatInt[] splitToNums(byte[] data, int size) {
		int n = data.length / size;
		if (data.length % size != 0)
			n++;
		UFatInt[] wynik = new UFatInt[n];
		for (int i = 0; i < n; i++) {
			byte[] b = new byte[size];

			for (int j = 0; j < size; j++)
				b[j] = data[size * i + j];

			wynik[i] = new UFatInt(b);
		}

		return wynik;
	}

	public static byte[] compactNums(UFatInt[] nums) {
		byte[][] bytes = new byte[nums.length][];
		int size = 0;
		for (int i = 0; i < nums.length; i++) {
			bytes[i] = nums[i].getBytes();
			size += nums[i].getBytes().length;
		}

		byte[] wynik = new byte[size];
		size = 0;

		for (byte[] aByte : bytes)
			for (byte b : aByte)
				wynik[size++] = b;


		return wynik;
	}

}
