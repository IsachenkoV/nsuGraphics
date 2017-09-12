package ru.nsu.ccfit.g12201.isachenko.cg.model;

public class Matrix {

    private double a[][] = new double[4][4];

    Matrix()
    {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                a[i][j] = 0;
    }

    Matrix(double m[][])
    {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                a[i][j] = m[i][j];
    }

    static Matrix matrixProduct(Matrix a, Matrix b)
    {
        Matrix res = new Matrix();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                for (int k = 0; k < 4; k++)
                    res.a[i][j] += a.a[i][k] * b.a[k][j];
        return res;
    }

    static Matrix matrixSum(Matrix a, Matrix b)
    {
        Matrix res = new Matrix();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                res.a[i][j] = a.a[i][j] + b.a[i][j];
        return res;
    }

    static Matrix matrixScalarProduct(double scalar, Matrix a)
    {
        Matrix res = new Matrix();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                res.a[i][j] = a.a[i][j] * scalar;
        return res;
    }

    static double[] matrixVectorProduct(Matrix a, double[] b)
    {
        double[] res = new double[4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                res[i] += a.a[i][j] * b[j];
        return res;
    }

    static double[] vectorMatrixProduct(double[] b, Matrix a)
    {
        double[] res = new double[4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                res[i] += b[j]*a.a[j][i];
        return res;
    }

    static double vectorVectorProduct(double[] a, double[] b)
    {
        double result = 0;
        for (int i = 0; i < 4; i++)
        {
            result += a[i] * b[i];
        }
        return result;
    }
}
