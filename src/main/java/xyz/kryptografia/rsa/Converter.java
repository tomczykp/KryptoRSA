package xyz.kryptografia.rsa;

import java.util.Arrays;
import java.util.Base64;

public class Converter {

	public static Num[] decode(String data) {
		// hex dec albo base64 text

		byte[] tmp = Base64.getDecoder().decode(data);

		String[] t = new String(tmp).split(" ");

		Num[] wynik = new Num[t.length];

		for (int i = 0; i < t.length; i++) {
			wynik[i] = new Num(t[i]);
		}

		return wynik;
	}

	public static String encode(Num[] nums) {
		String combine = "";
		for (Num n : nums)
			combine += n.toString() + " ";

		return new String(Base64.getEncoder().encode(combine.getBytes()));
	}

	public static byte[] numsToBytes(Num[] nums) {
		// konwertuje tablice wielkich liczb na ciąg byte'ów

		int s = nums.length;
		int suma = 0;
		int[] lengths = new int[s];
		byte[][] byteBytes = new byte[s][];

		for (int i = 0; i < s; i++) {

			byteBytes[i] = nums[i].getBytes();
			System.out.println(i + " liczba na bytes: \n\t" + Arrays.toString(byteBytes[i]));

			lengths[i] = byteBytes[i].length;
			suma += lengths[i];
		}

		byte[] wynik = new byte[suma];

		suma = 0;
		for (int i = 0; i < s; i++) {
			for (int j = 0; j < lengths[i]; j++)
				wynik[suma + j] = byteBytes[i][j];
			suma += lengths[i];
		}

		return wynik;
	}

	public static Num[] bytesToNums(byte[] bytes) {
		System.out.println("Bytów odczytano: " + bytes.length);
		int n = bytes.length / 8;
		int reminder = bytes.length % 8;
		int size = n;
		if (reminder != 0)
			size++;

		Num[] wynik = new Num[size];

		for (int i = 0; i < n; i++) {
			byte[] t = new byte[8];
			for (int j = 0; j < 8; j++)
				t[j] = bytes[i * 8 + j];

			wynik[i] = new Num(t);
		}

		if (reminder != 0) {
			byte[] t = new byte[reminder];
			for (int i = 0; i < reminder; i++)
				t[i] = bytes[i + n * 8];

			wynik[size - 1] = new Num(t);
		}
		System.out.println("Wyszło: " + size + " liczb\n\t" + Arrays.toString(wynik));
		return wynik;
	}

}
