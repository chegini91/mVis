/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility.math;

import org.ejml.data.DMatrixRMaj;

/**
 * This class contains additional utility methods for checking certain characteristics of Matrices
 * before adding methods here, check the EJML Javadoc for org.ejml.ops (and maybe also their implementation) to see whether this is actually necessary and whether it would provide any benefit
 * 
 * 
 * most of the methods here either add new 'features' or are faster than their counterparts in the EJML ops
 * 
 * anyhow, unless you know what you are doing, avoid using them before invoking methods from the ops package, as they often do check their parameters themselves
 * 
 * It is HIGHLY ENCOURAGED to use them in assertion in your code (and also enable assertions for the JVM) when testing your implementation
 * 
 * when not running in Debug mode, the static methods of this class are highly likely to be inlined efficiently by the JVM's JIT compiler
 * 
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
public final class MatrixUtils {
    
    private MatrixUtils() {
        //throw new UnsupportedOperationException("this is not going to happen!");
    }
    
    public static final boolean isRowVector(final DMatrixRMaj m) {
        return m.numRows == 1;
    }
    
    public static final DMatrixRMaj[] splitToRowVectors(final DMatrixRMaj m) {
        final DMatrixRMaj[] r = new DMatrixRMaj[m.numRows];
        for(int ir=0;ir<m.numRows;++ir) {
            final int startIdx = ir*m.numCols;
            r[ir]=new DMatrixRMaj(1,m.numCols);
            System.arraycopy(m.data, startIdx, r[ir].data, 0, m.numCols);
        }
        return r;
    }
    
    public static final boolean isNonScalarRowVector(final DMatrixRMaj m) {
        return isRowVector(m) && m.numCols > 1;
    }
    
    public static final boolean isColVector(final DMatrixRMaj m) {
        return m.numCols == 1;
    }
    
    public static final boolean isNonScalarColVector(final DMatrixRMaj m) {
        return isColVector(m) && m.numRows > 1;
    }
    
    public static final boolean isScalar(final DMatrixRMaj m) {
        return isRowVector(m) && isColVector(m);
    }
    
     /**
     * if you operate individually on multiple (dense) vectors that are jointly stored in a matrix, it is in most cases somewhat inefficient if they are organized as Column Vectors in a row major storage format
     * the larger the matrix, the worse things get
     * 
     * you could e.g. use this method in assertions or in other parts of your code that critically rely on the storage format to check whether this is the case 
     * @param m
     * @param vectorDim checks whether the 
     * @return 
     */
    public static final boolean isRowVectorsInRowMajor(final DMatrixRMaj m, final int vectorDim) {
        return m.numRows > 1 && m.numCols == vectorDim;
    }
}
