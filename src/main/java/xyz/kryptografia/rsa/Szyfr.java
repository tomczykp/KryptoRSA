package xyz.kryptografia.rsa;

public interface Szyfr {

	byte[] encrypt(byte[] plainText, byte[] key);

	byte[] decrypt(byte[] cipherText, byte[] key);

	Num[] genKey(int len);
}
