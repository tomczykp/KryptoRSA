package xyz.kryptografia.rsa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnsignedBigInt implements Comparable<UnsignedBigInt> {

    private List<Integer> liczba;

    public UnsignedBigInt() {
        this.setZero();
    }

    public UnsignedBigInt(UnsignedBigInt l) {
        this.liczba = new ArrayList<>();
        for (Integer i: l.liczba)
            this.liczba.add(i.intValue());
    }

    public UnsignedBigInt(long l) {
        this();
        this.add(l);
    }

    @Override
    public String toString() {
        String tmp = "";
        for (int i=this.liczba.size()-1; i >= 0; i--) {
            tmp += this.get(i);
        }
        return tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnsignedBigInt that)) return false;

        return Objects.equals(that.toString(), this.toString());
    }

    @Override
    public int compareTo(UnsignedBigInt l2) {
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

    public UnsignedBigInt add(UnsignedBigInt l) {
        if (l == null)
            return this;

        int d;
        boolean overload = false;
        for (int i=0; i < l.liczba.size() || overload; i ++) {

            if (this.liczba.size() <= i)
                this.liczba.add(0);

            if (l.liczba.size() <= i)
                d = this.get(i);
            else
                d = this.get(i) + l.get(i);

            if (overload)
                d ++;

            overload = (d > 9);
            d = d%10;
            this.liczba.set(i, d);
        }

        return this;
    }

    public UnsignedBigInt add(long l) {
        if (l < 0)
            return this;

        int i = 0;
        int d;
        boolean overload = false;
        while (l != 0 || overload){
            if (this.liczba.size() <= i)
                this.liczba.add(0);

            d = (int) (this.get(i) + (l%10));
            if (overload)
                d++;

            overload = (d > 9);

            d = d%10;
            this.liczba.set(i, d);
            l = (l - (l%10))/10;
            i++;
        }

        return this;
    }

    public UnsignedBigInt subtract(UnsignedBigInt l) {
        if (l == null)
            return this;

        if (this.liczba.size() < l.liczba.size()) {
            this.setZero();
            return this;
        }

        int d;
        boolean borrow = false;
        for (int i = 0; i < this.liczba.size(); i++) {

            if (l.liczba.size() <= i)
                if (borrow)
                    d = this.get(i);
                else
                    break;
            else
                d = this.get(i) - l.get(i);

            if (borrow)
                d--;

            if (d < 0) {
                borrow = true;
                this.liczba.set(i, 10 + d);
            } else {
                borrow = false;
                this.liczba.set(i, d);
            }

            if (borrow && (i+1) == this.liczba.size()) {
                this.setZero();
                return this;
            }
        }

        this.removeLeadingZeros();
        return this;
    }

    public UnsignedBigInt subtract(long l) {
        return  this.subtract(new UnsignedBigInt(l));
    }

    public UnsignedBigInt multiply(UnsignedBigInt l) {

        if (l.liczba.size() == 1 && l.get(0) == 0) {
            this.setZero();
            return this;
        }

        UnsignedBigInt sum = new UnsignedBigInt();
        UnsignedBigInt tmp;
        int d;
        for (int i = 0; i < l.liczba.size(); i++) {

            d = l.get(i);
            if (d != 0) {
                tmp = new UnsignedBigInt(this);
                tmp.multiply(d);
                tmp.tenPow(i);
                sum.add(tmp);
            }
        }

        this.liczba = sum.liczba;
        return this;
    }

    private void tenPow(int n) {
        for (int i = 0; i < n; i++) {
            this.liczba.add(0, 0);
        }
        this.removeLeadingZeros();
    }

    public UnsignedBigInt multiply(long l) {
        if (l < 0)
            return this;
        else if (l == 0) {
            this.setZero();
            return this;
        }

        int d;
        int j = 0;
        UnsignedBigInt tmp = new UnsignedBigInt(1);
        UnsignedBigInt suma = new UnsignedBigInt();
        while (l != 0) {

            d = (int) (l%10);
            l /= 10;

            for (int i = 0; i < this.liczba.size(); i++) {
                tmp.setZero();
                tmp.add(d * this.get(i));
                tmp.tenPow(j + i);
                suma.add(tmp);
            }
            j++;
        }

        this.liczba = suma.liczba;
        return this;
    }

    public UnsignedBigInt pow(long n) {
        UnsignedBigInt tmp = new UnsignedBigInt(this);
        for (int i = 0; i < n; i++) {
            this.multiply(tmp);
        }
        return this;
    }

    public UnsignedBigInt pow(UnsignedBigInt n) {
        UnsignedBigInt tmp = new UnsignedBigInt(this);
        for (UnsignedBigInt i = new UnsignedBigInt(0); i.compareTo(n) < 0; i.add(1)) {
            this.multiply(tmp);
        }
        return this;
    }

    public void divide(long l) {
        if ( l == 0)
            return;

        int n = 0;
        long tmp = l;
        while(tmp != 0) {
            n++;
            tmp /= 10;
        }

        if (n > this.liczba.size()) {
            this.setZero();
            return;
        }



    }

    public void divide(UnsignedBigInt l) {

        if (l.get(0) == 0 && l.liczba.size() == 1)
            throw new ArithmeticException();

        if (l.liczba.size() > this.liczba.size()) {
            this.setZero();
            return;
        }

        UnsignedBigInt suma = new UnsignedBigInt();
        UnsignedBigInt tmp = new UnsignedBigInt();

        for (int i = this.liczba.size()-1; i >= 0; i--) {

            tmp.tenPow(1);
            tmp.add(this.get(i));

            if (tmp.compareTo(l) < 0 && i == 0) {



            } else {
                int n = 0;
                UnsignedBigInt l2 = new UnsignedBigInt(l);

                while (tmp.compareTo(l2.multiply(n)) > 0) {
                    n++;
                    l2 = new UnsignedBigInt(l);
                }

                l2 = new UnsignedBigInt(l);
                if (tmp.compareTo(l2.multiply(n)) == 0) {
                    n++;
                    l2 = new UnsignedBigInt(l);
                    l2.multiply(n);
                }

                suma.liczba.add(0, n);
                tmp.subtract(l2);
            }

        }


    }

}
