package xyz.kryptografia.rsa;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumTests {

    @Test
    public void addTest() {
        Num l1;
        Num l2;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new Num(i1);
            l2 = new Num(i2);

            Num tmp = Num.add(l1, l2);
            Assertions.assertEquals(String.valueOf(i1 + i2), tmp.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
            Assertions.assertEquals(String.valueOf(i2), l2.toString());
        }

        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new Num(i1);

            Num tmp = Num.add(l1, i2);

            Assertions.assertEquals(String.valueOf(i1), l1.toString());
            Assertions.assertEquals(String.valueOf(i1 + i2), tmp.toString());
        }
    }

    @Test
    public void subTest() {
        Num l1;
        Num l2;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new Num(i1);
            l2 = new Num(i2);

            Num s = Num.subtract(l1, l2);

            long tmp = i1 - i2;
            if (tmp < 0)
                tmp = 0;
            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
            Assertions.assertEquals(String.valueOf(i2), l2.toString());
        }

        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);
            l1 = new Num(i1);

            Num s = Num.subtract(l1, i2);
            long tmp = i1 - i2;
            if (tmp < 0)
                tmp = 0;
            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
        }
    }

    @Test
    public void mul1Test() {
        Num l1;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new Num(i1);

            Num s = Num.mul(l1, i2);
            long tmp = i1 * i2;

            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
        }

        long tmp = 100000;
        l1 = new Num(1);

        for (int i = 0; i < 40; i++)
            l1 = Num.mul(l1, tmp);

        String s = "1" + "0".repeat(200);
        Assertions.assertEquals(s, l1.toString());
    }

    @Test
    public void mul2Test() {
        Num l1;
        Num l2;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new Num(i1);
            l2 = new Num(i2);

            Num s = Num.mul(l1, l2);
            long tmp = i1 * i2;

            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
            Assertions.assertEquals(String.valueOf(i2), l2.toString());
        }

        long tmp = 100000;
        l2 = new Num(tmp);
        l1 = new Num(1);

        for (int i = 0; i < 40; i++)
            l1 = Num.mul(l1 ,l2);

        String s = "1" + "0".repeat(200);
        Assertions.assertEquals(s, l1.toString());

        tmp = 999999999;
        l1 = Num.mul(new Num(tmp), tmp);

        s = "999999998000000001";
        Assertions.assertEquals(s, l1.toString());

    }

    @Test
    public void karatsubaTest() {
        Num l1;
        Num l2;

        long i1, i2;
        Random rand = new Random();
        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new Num(i1);
            l2 = new Num(i2);

            Num s = Num.mulKaratsuba(l1, l2);
            long tmp = i1 * i2;

            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
            Assertions.assertEquals(String.valueOf(i2), l2.toString());
        }

        String s;
        s = "1" + "9".repeat(100);
        l2 = new Num(s);
        l1 = new Num(s);
        l1 = Num.mulKaratsuba(l1 ,l2);

        s = "399999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999960000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001";
        Assertions.assertEquals(s, l1.toString());

        long tmp = 999999999;
        l1 = Num.mulKaratsuba(new Num(tmp), tmp);

        s = "999999998000000001";
        Assertions.assertEquals(s, l1.toString());

    }

    @Test
    public void div1Test() {
        Num l1;
        Num l2;

        long i1, i2;
        Random rand = new Random();

        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new Num(i1);
            l2 = new Num(i2);

            Num s = Num.divide(l1, l2);
            long tmp = i1 / i2;

            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
            Assertions.assertEquals(String.valueOf(i2), l2.toString());
        }
    }

    @Test
    public void div2Test() {
        Num l1;

        long i1, i2;
        Random rand = new Random();

        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new Num(i1);

            Num s = Num.divide(l1, i2);
            long tmp = i1 / i2;

            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
        }
    }

    @Test
    public void modTest() {
        Num l1;
        Num l2;

        long i1, i2;
        Random rand = new Random();

        for (int i = 0; i < 5000; i++) {
            i1 = rand.nextInt(Integer.MAX_VALUE);
            i2 = rand.nextInt(Integer.MAX_VALUE);

            l1 = new Num(i1);
            l2 = new Num(i2);

            Num s = Num.mod(l1, l2);
            long tmp = i1 % i2;

            Assertions.assertEquals(String.valueOf(tmp), s.toString());
            Assertions.assertEquals(String.valueOf(i1), l1.toString());
            Assertions.assertEquals(String.valueOf(i2), l2.toString());
        }
    }

}