package com.oracle.truffle.prepro.runtime.types;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import java.util.Arrays;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.string.NDArrayStrings;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
public final class PreProConstant implements TruffleObject {

    private final INDArray ndArray;

    // PrePro has no Boolean type, this is the replacement
    public static final PreProConstant TRUE = new PreProConstant(Nd4j.create(new int[]{1, 1}, new double[]{1}));
    public static final PreProConstant FALSE = new PreProConstant(Nd4j.create(new int[]{1, 1}, new double[]{0}));

    public PreProConstant(INDArray ndArray) {
        this.ndArray = ndArray;
        if (ndArray.length() != 1) {
            throw new RuntimeException("Can only add Constant with double value.");
        }
    }

    public PreProConstant(double value) {
        this(Nd4j.create(new double[]{value}, new int[]{1, 1}));
    }

    public PreProConstant(String value) {
        this(Nd4j.create(new double[]{Double.parseDouble(value)}, new int[]{1, 1}));
    }

    public INDArray timeSeries() {
        return ndArray;
    }

    public int amountTimeElements() {
        return ndArray.shape()[0];
    }
    
    @TruffleBoundary
    public double getDoubleValue() {
        return timeSeries().getDouble(0);
    }

    @TruffleBoundary
    public PreProConstant add(PreProConstant right) {
        return new PreProConstant(timeSeries().add(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProConstant sub(PreProConstant right) {
        return new PreProConstant(timeSeries().sub(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProConstant mul(PreProConstant right) {
        return new PreProConstant(timeSeries().mul(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProConstant div(PreProConstant right) {
        return new PreProConstant(timeSeries().div(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProVector3 add(PreProVector3 right) {
        return right.add(this);
    }

    @TruffleBoundary
    public PreProVector3 sub(PreProVector3 right) {
        return right.sub(this);
    }

    @TruffleBoundary
    public PreProVector3 mul(PreProVector3 right) {
        return right.mul(this);
    }

    @TruffleBoundary
    public PreProVector3 div(PreProVector3 right) {
        return right.div(this);
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return new NDArrayStrings().format(timeSeries());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreProConstant)) return false;
        PreProConstant that = (PreProConstant) o;
        return ndArray.equals(that.ndArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ndArray);
    }

    @SuppressWarnings("static-method")
    @ExportMessage
    boolean isNumber() {
        return true;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInByte() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInShort() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInFloat() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInLong() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInInt() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInDouble() {
        return true;
    }

    @ExportMessage
    @TruffleBoundary
    double asDouble() throws UnsupportedMessageException {
        if (fitsInDouble()) {
            return getDoubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    @TruffleBoundary
    long asLong() throws UnsupportedMessageException {
        if (fitsInLong()) {
            return (long) getDoubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    @TruffleBoundary
    byte asByte() throws UnsupportedMessageException {
        if (fitsInByte()) {
            return (byte) getDoubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    @TruffleBoundary
    int asInt() throws UnsupportedMessageException {
        if (fitsInInt()) {
            return (int) getDoubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    @TruffleBoundary
    float asFloat() throws UnsupportedMessageException {
        if (fitsInFloat()) {
            return (float) getDoubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    @TruffleBoundary
    short asShort() throws UnsupportedMessageException {
        if (fitsInShort()) {
            return (short) getDoubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }
}
