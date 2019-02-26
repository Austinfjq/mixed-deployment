package cn.harmonycloud.schedulingalgorithm.dataobject;

import java.math.BigInteger;

public class Quantity {
    private Int64Amount i;
    private InfDecAmount d;
    private String s;
    private String format;

    public boolean isZero() {
        if (i != null) {
            return i.value == 0L;
        }
        if (d != null) {
            return new BigInteger("0").compareTo(d.unscaled) == 0;
        }
        return "0".equals(s);
    }

    public Int64Amount getI() {
        return i;
    }

    public void setI(Int64Amount i) {
        this.i = i;
    }

    public InfDecAmount getD() {
        return d;
    }

    public void setD(InfDecAmount d) {
        this.d = d;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int compareTo(Quantity other) {
        if (this.d == null && other.d == null) {
            return this.i.cmp(other.i);
        }
        return this.asDec().compareTo(other.asDec());
    }

    private InfDecAmount asDec() {
        if (this.d != null) {
            return this.d;
        }
        this.d = this.i.asDec();
        this.i = null;
        return this.d;
    }

    public class Int64Amount {
        long value;
        int scale;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        InfDecAmount asDec() {
            return new InfDecAmount(BigInteger.valueOf(this.value), this.scale);
        }

        int cmp(Int64Amount o) {
            if (this.scale > o.scale) {
                if (this.scale - o.scale >= 18) {
                    return this.asDec().compareTo(o.asDec());
                }
                long result = o.value / (long) (Math.pow(10, o.scale));
                long reminder = o.value % (long) (Math.pow(10, o.scale));
                if (result == this.value) {
                    if (reminder == 0) {
                        return 0;
                    } else if (reminder > 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
                o.value = result;
            } else if (this.scale < o.scale) {
                if (o.scale - this.scale >= 18) {
                    return this.asDec().compareTo(o.asDec());
                }
                long result = this.value / (long) (Math.pow(10, this.scale));
                long reminder = this.value % (long) (Math.pow(10, this.scale));
                if (result == o.value) {
                    if (reminder == 0) {
                        return 0;
                    } else if (reminder > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
                this.value = result;
            }
            return Long.compare(this.value, o.value);
        }
    }

    public class InfDecAmount {
        BigInteger unscaled;
        int scale;

        public BigInteger getUnscaled() {
            return unscaled;
        }

        public void setUnscaled(BigInteger unscaled) {
            this.unscaled = unscaled;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        InfDecAmount(BigInteger unscaled, int scale) {
            this.unscaled = unscaled;
            this.scale = scale;
        }

        int compareTo(InfDecAmount o) {
            // upscale (this, o) to (x, y)
            InfDecAmount x, y;
            if (this.scale == o.scale) {
                x = this;
                y = o;
            } else if (this.scale > o.scale) {
                x = this;
                y = o.rescale(this.scale);
            } else {
                x = this.rescale(o.scale);
                y = o;
            }
            return x.unscaled.compareTo(y.unscaled);
        }

        InfDecAmount rescale(int newScale) {
            int shift = newScale - this.scale;
            if (shift < 0) {
                BigInteger e = BigInteger.valueOf(10).pow(-shift);
                return new InfDecAmount(unscaled.divide(e), newScale);
            } else if (shift > 0) {
                BigInteger e = BigInteger.valueOf(10).pow(shift);
                return new InfDecAmount(unscaled.multiply(e), newScale);
            } else {
                return this;
            }
        }
    }
}
