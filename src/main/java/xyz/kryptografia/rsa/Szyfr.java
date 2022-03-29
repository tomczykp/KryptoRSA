package xyz.kryptografia.rsa;

public interface Szyfr {

	Num[] encrypt(Num[] plainText, Num[] key);

	Num[] decrypt(Num[] cipherText, Num[] key);

	Num[] crypt(Num[] text, Num[] key);


	Num[][] genKey(int len);
}
