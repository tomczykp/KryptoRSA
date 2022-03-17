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
        return null;
    }

}
