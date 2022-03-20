package xyz.kryptografia.rsa;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Num implements Comparable<Num> {

    private List<Integer> liczba;

    public Num() {
        this.setZero();
    }

    public Num(Num l) {
        this.liczba = new ArrayList<>();
        for (Integer i: l.liczba)
            this.liczba.add(i.intValue());
    }

    public Num(long l) {
        this.liczba = Num.add(new Num(), l).liczba;
    }

    private Num(List<Integer> l) {
        this.liczba = l;
    }

    public Num(String s) {
        this.liczba = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            this.liczba.add(0, s.charAt(i) - '0' );
        }
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for (int i=this.liczba.size()-1; i >= 0; i--) {
            tmp.append(this.get(i));
        }
        return tmp.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Num that)) return false;

        return Objects.equals(that.toString(), this.toString());
    }

    @Override
    public int compareTo(Num l2) {
        int d = this.liczba.size() - l2.liczba.size();
        if (d > 0)
            return 1;
        else if (d < 0)
            return -1;
        else
            for (int i=this.liczba.size()-1; i >= 0 ; i--) {
                d = this.get(i) - l2.get(i);
                if (d > 0)
                    return 1;
                else if (d < 0)
                    return -1;
            }
        return 0;
    }

    private void setZero() {
        this.liczba = new ArrayList<>();
        this.liczba.add(0);
    }

    private boolean isZero() {
        return (this.liczba.size() == 1 && this.get(0) == 0);
    }

    private Num splitNum(int start, int stop) {

        int s = this.liczba.size();
        List<Integer> lista = new ArrayList<>();
        boolean zero = true;
        for (; start < stop; start++) {
            if (start >= s)
                break;
            zero = false;
            lista.add(this.get(start).intValue());
        }
        if (zero)
            lista.add(0);

        return new Num(lista);
    }

    public Num tenPow(int n) {
        for (int i = 0; i < n; i++)
            this.liczba.add(0, 0);

        this.removeLeadingZeros();
        return this;
    }

    private Integer get(int i) {
        return this.liczba.get(i);
    }

    private void removeLeadingZeros() {
        for (int i = this.liczba.size()-1; i >= 0 ; i--) {
            if (this.get(i) == 0)
                this.liczba.remove(i);
            else if (this.get(i) != 0)
                break;
        }
        if (this.liczba.size() == 0)
            this.liczba.add(0);
    }

    public static Num add(Num x, Num y) {
        if (y == null && x == null)
            return new Num();
        else if (x == null)
            return new Num(y);
        else if (y == null)
            return new Num(x);

        int d;
        boolean overload = false;
        Num tmp = new Num();
        int sx = x.liczba.size();
        int sy = y.liczba.size();

        for (int i=0; i < sx || i < sy || overload; i ++) {

            if (tmp.liczba.size() <= i)
                tmp.liczba.add(0);
            d = 0;

            if (sx > i)
                d += x.get(i);
            if (sy > i)
                d += y.get(i);

            if (overload)
                d ++;

            overload = (d > 9);
            d = d%10;
            tmp.liczba.set(i, d);
        }

        return tmp;
    }

    public static Num add(Num x, long y) {
        if (y < 0 && x == null)
            return new Num();
        else if (y < 0)
            return new Num(x);
        if (x == null)
            return new Num(y);

        int i = 0;
        int d;
        boolean overload = false;
        Num tmp = new Num();

        int sx = x.liczba.size();

        while (y != 0 || overload || sx > i) {

            if (tmp.liczba.size() <= i)
                tmp.liczba.add(0);

            d = 0;
            if (sx > i)
                d += x.get(i);
            if (y != 0)
                d += (int)(y%10);
            if (overload)
                d++;

            overload = (d > 9);

            d = d%10;
            tmp.liczba.set(i, d);
            y = (y - (y%10))/10;
            i++;
        }

        return tmp;
    }

    public static Num subtract(Num x, Num y) {
        if (y == null && x == null)
            return new Num();
        else if (x == null)
            return new Num(y);
        else if (y == null)
            return new Num(x);

        int sx = x.liczba.size();
        int sy = y.liczba.size();

        if (sx < sy) {
            return new Num();
        }

        int d;
        boolean borrow = false;
        Num tmp = new Num();

        for (int i = 0; i < sx || borrow; i++) {

            if (tmp.liczba.size() <= i)
                tmp.liczba.add(0);

            d = 0;
            if (sx <= i && borrow) // już nic nie ma w X, a trzeba pobrać
                return new Num();

            if (borrow)
                d--;

            if (sx > i)
                d += x.get(i);
            if (sy > i)
                d -= y.get(i);

            if (d < 0) {
                borrow = true;
                tmp.liczba.set(i, 10 + d);
            } else {
                borrow = false;
                tmp.liczba.set(i, d);
            }

        }

        tmp.removeLeadingZeros();
        return tmp;
    }

    public static Num subtract(Num x, long y) {
        return Num.subtract(x, new Num(y));
    }

    public static Num mul(Num x, Num y) {

        if ((y.liczba.size() == 1 && y.get(0) == 0)
                || (x.liczba.size() == 1 && x.get(0) == 0)) {
            return new Num();
        }

        Num suma = new Num();
        int d;
        for (int i = 0; i < y.liczba.size(); i++) {

            d = y.get(i);
            if (d != 0)
                suma = Num.add(suma, Num.mul(x, d).tenPow(i));

        }

        return suma;
    }

    public static Num mul(Num x, long y) {
        if (y < 0 || (x.liczba.size() == 1 && x.get(0) == 0))
            return new Num();

        int d;
        int j = 0;
        Num suma = new Num();

        while (y != 0) {

            d = (int) (y%10);
            y /= 10;

            for (int i = 0; i < x.liczba.size(); i++) {
                Num tmp = Num.add(new Num(), (long) d * x.get(i));
                tmp.tenPow(j + i);
                suma = Num.add(suma, tmp);
            }
            j++;
        }

        return suma;
    }

    public static Num mulKaratsuba(Num x, Num y) {

        int sx = x.liczba.size();
        int sy = y.liczba.size();
        int N = Math.min(sx, sy);

        /** for small values directly multiply **/
        if (N < 5)
            return Num.mul(x, y);

        /** max length divided, rounded up **/
        N = (N / 2);


        /** compute sub expressions **/
        Num ax = x.splitNum(N, sx);
        Num bx = x.splitNum(0, N);

        Num ay = y.splitNum(N, sy);
        Num by = y.splitNum(0, N);

        /** compute sub expressions **/
        Num z0 = Num.mulKaratsuba(bx, by);  // z0
        Num z1 = Num.mulKaratsuba(           // z1
                Num.add(ax, bx),
                Num.add(ay, by));
        Num z2 = Num.mulKaratsuba(ax, ay);  // z2

        z1 = Num.subtract(z1, z2);
        z1 = Num.subtract(z1, z0);

        z2.tenPow(2*N);
        z1.tenPow(N);

        return Num.add(z2, Num.add(z0, z1));
    }

    public static Num mulKaratsuba(Num x, long y) {
        return Num.mulKaratsuba(x, new Num(y));
    }

    public static Num pow(Num a, long n) {
        Num tmp = new Num(a);

        for (int i = 0; i < n; i++)
            tmp = Num.mulKaratsuba(tmp, a);

        return tmp;
    }

    public static Num pow(Num a, Num n) {
        Num tmp = new Num(a);

        for (int i = 0; !n.isZero(); n = Num.subtract(n, 1))
            tmp = Num.mulKaratsuba(tmp, a);

        return tmp;
    }

}
