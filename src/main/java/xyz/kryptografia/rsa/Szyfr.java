package xyz.kryptografia.rsa;

import xyz.kryptografia.rsa.liczby.UFatInt;

public interface Szyfr {

	UFatInt[] encrypt(UFatInt[] plainText, UFatInt[] key);

	UFatInt[] decrypt(UFatInt[] cipherText, UFatInt[] key);

	UFatInt[] crypt(UFatInt[] text, UFatInt[] key);


	UFatInt[][] genKey(int len);
}
