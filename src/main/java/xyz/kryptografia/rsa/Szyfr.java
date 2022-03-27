package xyz.kryptografia.rsa;

public interface Szyfr {

	byte[] encrypt(byte[] plainText, Num[] key);

	byte[] decrypt(byte[] cipherText, Num[] key);

	Num[][] genKey(int len);
}
