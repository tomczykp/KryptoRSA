package xyz.kryptografia.rsa;

public class AlgorytmRSA implements Szyfr {

    public AlgorytmRSA() {
        super();
    }

    @Override
    public byte[] encrypt(byte[] plainText, byte[] pubKey) {

        return plainText;
    }

    @Override
    public byte[] decrypt(byte[] cipherText, byte[] privKey) {

        return cipherText;
    }

    @Override
    public byte[][] genKey(int len) {

        // find p i q (large primes)
        // n = p * q
        // fi = (p-1)*(q-1)
        // e = rand(2^(size-1), 2^size) ? gcd(e, fi) == 1
        // mod inverse d = (e, fi)
        //
        // pubKey = (n, e)
        // privKey = (n, d)

        return null;
    }

}
