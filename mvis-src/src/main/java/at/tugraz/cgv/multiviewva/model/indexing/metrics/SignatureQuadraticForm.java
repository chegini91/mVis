/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing.metrics;

import at.tugraz.cgv.multiviewva.model.indexing.SimilarityMeasure;
import org.ejml.data.DMatrixRMaj;

/**
 * TODO: Implement
 * 
 * Implements the Signature Quadratic Form Distance (SQFD)
 * Note that the number of row vectors in both arguments can be arbitrary (>=1)
 * 
 * 
 * each N dimensional row vector is to be structured as follows:
 * dimensions 0 to N-2 contain the actual feature vector whereas N-1 contains it's weight
 * 
 * the number of columns must be equal in both arguments
 * 
 *
 * ATTENTION: make sure that the vectors are stored in row major format (i.e. as
 * row vectors)
 *
 * @see https://pdfs.semanticscholar.org/b81a/d9c60add0b0101ebe5c34473fc8f0fa91724.pdf
 * 
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 * distances
 *
 */
public class SignatureQuadraticForm implements SimilarityMeasure {

    public SignatureQuadraticForm() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Double apply(final DMatrixRMaj a, final DMatrixRMaj b) {
       throw new UnsupportedOperationException("Not implemented yet!");
    }

}
