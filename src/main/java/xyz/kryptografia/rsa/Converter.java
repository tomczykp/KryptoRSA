package xyz.kryptografia.rsa;

public class Converter {

	public static Num[] decodeKey(String data) {
		// hex dec albo base64 text

		String[] t = data.split(" ");
		Num[] wynik = {new Num(t[0]), new Num(t[1])};
		return wynik;
	}

	public static String encodeKey(Num[] nums) {
		return nums[0].toString() + " " + nums[1].toString();
	}


}
