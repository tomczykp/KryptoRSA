package xyz.kryptografia.rsa;

import java.util.Base64;

public class Converter {

	public static Num[] decodeKey(String data) {
		// hex dec albo base64 text

		byte[] tmp = Base64.getDecoder().decode(data);
		String[] t = new String(tmp).split(" ");
		Num[] wynik = {new Num(t[0]), new Num(t[1])};
		return wynik;
	}

	public static String encodeKey(Num[] nums) {
		String combine = nums[0].toString() + " " + nums[1].toString();
		return new String(Base64.getEncoder().encode(combine.getBytes()));
	}


}
