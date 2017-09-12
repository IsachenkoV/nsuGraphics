package ru.nsu.ccfit.g12201.isachenko.cg.model;

import java.math.BigDecimal;

/**
 * Created by Владимир on 21.05.2015.
 */
public class SofLE {

    double[][] m = new double[8][8];
    double[] b = new double[8];

    BigDecimal[][] msh = new BigDecimal[8][8];

    SofLE(Point[] ps)
    {
/**        m[0][0] = ps[0].x;
        m[0][1] = ps[0].y;
        m[0][2] = 1;

        m[1][3] = ps[0].x;
        m[1][4] = ps[0].y;
        m[1][5] = 1;

        m[2][0] = ps[1].x;
        m[2][1] = ps[1].y;
        m[2][2] = 1;
        m[2][6] = -ps[1].x;
        m[2][7] = -ps[1].y;

        m[3][3] = ps[1].x;
        m[3][4] = ps[1].y;
        m[3][5] = 1;

        m[4][0] = ps[3].x;
        m[4][1] = ps[3].y;
        m[4][2] = 1;

        m[5][3] = ps[3].x;
        m[5][4] = ps[3].y;
        m[5][5] = 1;
        m[5][6] = -ps[3].x;
        m[5][7] = -ps[3].y;

        m[6][0] = ps[2].x;
        m[6][1] = ps[2].y;
        m[6][2] = 1;
        m[6][6] = -ps[2].x;
        m[6][7] = -ps[2].y;

        m[7][3] = ps[2].x;
        m[7][4] = ps[2].y;
        m[7][5] = 1;
        m[7][6] = -ps[2].x;
        m[7][7] = -ps[2].y;

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                msh[i][j] = new BigDecimal(m[i][j]);

        b[2] = b[5] = b[6] = b[7] = -1;
*/

        m[0][2] = 1.0;

        m[1][5] = 1.0;

        m[2][1] = m[2][2] = 1.0;
        m[2][7] = -ps[1].x + 0.0;

        m[3][4] = m[3][5] = 1.0;
        m[3][7] = -ps[1].y + 0.0;

        m[4][0] = m[4][1] = m[4][2] = 1.0;
        m[4][6] = m[4][7] = -ps[2].x + 0.0;

        m[5][3] = m[5][4] = m[5][5] = 1.0;
        m[5][6] = m[5][7] = -ps[2].y + 0.0;

        m[6][0] = m[6][2] = 1.0;
        m[6][6] = -ps[3].x + 0.0;

        m[7][3] = m[7][5] = 1.0;
        m[7][6] = -ps[3].y + 0.0;

        for (int i = 0; i <= 3; i++)
        {
            b[2*i] = ps[i].x + 0.0;
            b[2*i + 1] = ps[i].y + 0.0;
        }

    }

    SofLE(Point a, Point b, Point c, int ver)
    {
        double px = (b.x + c.x) / 2.0;
        double py = (b.y + c.y) / 2.0;

        px = 2.0*((a.x + 0.0) - px) / 3.0;
        py = 2.0*((a.y + 0.0) - py) / 3.0;

        px = (a.x - px);
        py = (a.y - py);

        if (ver == 1)
        {
            m[0][1] = m[0][2] = 1.0;
            m[0][7] = -a.x;

            m[1][4] = m[1][5] = 1.0;
            m[1][7] = -a.y;

            m[6][0] = 1.0;
            m[6][1] = 2.0;
            m[6][2] = 3.0;
            m[6][6] = -px;
            m[6][7] = -2*px;

            m[7][3] = 1.0;
            m[7][4] = 2.0;
            m[7][5] = 3.0;
            m[7][6] = -py;
            m[7][7] = -2*py;
        }
        else
        {
            m[0][0] = m[0][2] = 1.0;
            m[0][6] = -a.x;

            m[1][3] = m[1][5] = 1.0;
            m[1][6] = -a.y;

            m[6][0] = 2.0;
            m[6][1] = 1.0;
            m[6][2] = 3.0;
            m[6][6] = -2*px;
            m[6][7] = -px;

            m[7][3] = 2.0;
            m[7][4] = 1.0;
            m[7][5] = 3.0;
            m[7][6] = -2*py;
            m[7][7] = -py;
        }
        m[2][2] = 1.0;

        m[3][5] = 1.0;

        m[4][0] = m[4][1] = m[4][2] = 1.0;
        m[4][6] = m[4][7] = -c.x;

        m[5][3] = m[5][4] = m[5][5] = 1.0;
        m[5][6] = m[5][7] = -c.y;

        this.b[0] = a.x;
        this.b[1] = a.y;
        this.b[2] = b.x;
        this.b[3] = b.y;
        this.b[4] = c.x;
        this.b[5] = c.y;
        this.b[6] = 3*px;
        this.b[7] = 3*py;
    }

    public double[] solve()
    {
        double[][] matr = new double[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                matr[i][j] = m[i][j];

        double[] res = new double[9];

        double det0 = getDeterminant(m);
        double[] det = new double[9];

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++) {
                if (i - 1 >= 0)
                {
                    matr[j][i - 1] = m[j][i - 1];
                }
                matr[j][i] = b[j];
            }
            det[i] = getDeterminant(matr);
        }

        for (int i = 0; i < 8; i++)
        {
            res[i] = det[i] / (det0 + 0.0);
        }
        res[8] = 1.0;
        double[] resInvert = new double[9];
        double[][] anotherMatrix = new double[3][3];

        anotherMatrix[0][0] = resInvert[0] = res[4]*res[8] - res[7]*res[5];
        anotherMatrix[1][0] = resInvert[1] = res[7]*res[2] - res[1]*res[8];
        anotherMatrix[2][0] = resInvert[2] = res[1]*res[5] - res[4]*res[2];

        anotherMatrix[0][1] = resInvert[3] = res[6]*res[5] - res[3]*res[8];
        anotherMatrix[1][1] = resInvert[4] = res[0]*res[8] - res[6]*res[2];
        anotherMatrix[2][1] = resInvert[5] = res[2]*res[3] - res[0]*res[5];

        anotherMatrix[0][2] = resInvert[6] = res[7]*res[3] - res[6]*res[4];
        anotherMatrix[1][2] = resInvert[7] = res[1]*res[6] - res[0]*res[7];
        anotherMatrix[2][2] = resInvert[8] = res[0]*res[4] - res[1]*res[3];

        double anotherMatrixDet = getDeterminant(anotherMatrix);

        for (int i = 0; i < 9; i++)
            resInvert[i] /= anotherMatrixDet;

        return resInvert;
    }

    private double getDeterminant(double[][] matrix){
        if (matrix.length == 1)
            return matrix[0][0];

        double sum = 0.0;
        double s;

        for (int i = 0; i < matrix.length; i++) {
            double[][] smaller = new double[matrix.length - 1][matrix.length - 1];
            for (int a = 1; a < matrix.length; a++){
                for (int b = 0; b < matrix.length; b++){
                    if(b < i){
                        smaller[a - 1][b] = matrix[a][b];
                    }
                    else if(b > i){
                        smaller[a - 1][b - 1] = matrix[a][b];
                    }
                }
            }
            if (i % 2 == 0){
                s = 1.0;
            }
            else {
                s = -1.0;
            }
            sum += s*matrix[0][i]*(getDeterminant(smaller));
        }
        return(sum);
    }
}

