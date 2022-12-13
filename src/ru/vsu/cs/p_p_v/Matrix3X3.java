package ru.vsu.cs.p_p_v;

public class Matrix3X3 {
    int columns, rows;
    double[][] matrix;

    public Matrix3X3(double[][] matrix) {
        this.matrix = matrix;
    }

    public Vector multiply(Vector vector) {
        double[] result = new double[] {0, 0, 0};

        for (var i = 0; i < 3; i++) {
            result[i] += vector.getX() * matrix[i][0];
            result[i] += vector.getY() * matrix[i][1];
            result[i] += vector.getZ() * matrix[i][2];
        }

        return new Vector(result[0], result[1], result[2]);
    }
}
