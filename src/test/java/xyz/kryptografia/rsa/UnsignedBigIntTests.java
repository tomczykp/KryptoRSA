package xyz.kryptografia.rsa;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnsignedBigIntTests {

    @Test
    public void addTest() {
        UnsignedBigInt l1;
        UnsignedBigInt l2;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new UnsignedBigInt(i1);
            l2 = new UnsignedBigInt(i2);
            l1.add(l2);
            Assertions.assertEquals(String.valueOf(i1 + i2), l1.toString());
        }

        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new UnsignedBigInt(i1);
            l1.add(i2);
            Assertions.assertEquals(String.valueOf(i1 + i2), l1.toString());
        }
    }

    @Test
    public void subTest() {
        UnsignedBigInt l1;
        UnsignedBigInt l2;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new UnsignedBigInt(i1);
            l2 = new UnsignedBigInt(i2);
            l1.subtract(l2);
            long tmp = i1 - i2;
            if (tmp < 0)
                tmp = 0;
            Assertions.assertEquals(String.valueOf(tmp), l1.toString());
        }

        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new UnsignedBigInt(i1);
            l1.subtract(i2);
            long tmp = i1 - i2;
            if (tmp < 0)
                tmp = 0;
            Assertions.assertEquals(String.valueOf(tmp), l1.toString());
        }
    }

    @Test
    public void modTest() {

    }

    @Test
    public void mulitIntTest() {
        UnsignedBigInt l1;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new UnsignedBigInt(i1);
            l1.multiply(i2);

            long tmp = i1 * i2;
            Assertions.assertEquals(String.valueOf(tmp), l1.toString());
        }
        long tmp = 100000;
        l1 = new UnsignedBigInt(1);

        for (int i = 0; i < 40; i++) {
            l1.multiply(tmp);
        }

        String s = "1" + "0".repeat(200);
        Assertions.assertEquals(s, l1.toString());
    }

    @Test
    public void mulitTest() {
        UnsignedBigInt l1;
        UnsignedBigInt l2;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new UnsignedBigInt(i1);
            l2 = new UnsignedBigInt(i2);
            l1.multiply(l2);

            long tmp = i1 * i2;
            Assertions.assertEquals(String.valueOf(tmp), l1.toString());
        }


        long tmp = 100000;
        l2 = new UnsignedBigInt(tmp);
        l1 = new UnsignedBigInt(1);

        for (int i = 0; i < 40; i++) {
            l1.multiply(l2);
        }

        String s = "1" + "0".repeat(200);
        Assertions.assertEquals(s, l1.toString());

        tmp = 999999999;
        l1 = new UnsignedBigInt(tmp);
        l1.multiply(tmp);
        s = "999999998000000001";
        Assertions.assertEquals(s, l1.toString());

    }
}
