public class KochSnowflake {
    private static final double SIN60 = Math.sin(Math.PI/3);
    private static final double COS60 = Math.cos(Math.PI/3);

    public static double[] calculateVecAE(double xA, double yA, double xE, double yE) {
        return new double[] {xE-xA, yE-yA};
    }

    public static double[] calculatePointB(double xA, double yA, double[] vecAE) {
        return new double[] {xA + (1/3d) * vecAE[0], yA + (1/3d) * vecAE[1]};
    }

    public static double[] calculatePointD(double xA, double yA, double[] vecAE) {
        return new double[] {xA + (2/3d) * vecAE[0], yA + (2/3d) * vecAE[1]}; 
    }

    public static double[] calculateVecBC(double[] vAE) {
        return new double[] {(1/3d) * (COS60 * vAE[0] - SIN60 * vAE[1]), 
            (1/3d) * (SIN60 * vAE[0] + COS60 * vAE[1])};
    }

    public static double[] calculatePointC(double xB, double yB, double[] vecBC) {
        return new double[] {xB + vecBC[0], yB + vecBC[1]};
    }

    public static double[] foldLine(double xA, double yA, double xE, double yE) {
        double[] vecAE = calculateVecAE(xA, yA, xE, yE);
        double[] pointB = calculatePointB(xA, yA, vecAE);
        double[] pointD = calculatePointD(xA, yA, vecAE);
        double[] vecBC = calculateVecBC(vecAE);
        double[] pointC = calculatePointC(pointB[0], pointB[1], vecBC);

        return new double[] {xA, yA, pointB[0], pointB[1], pointC[0], pointC[1], pointD[0], pointD[1], xE, yE};
    }

    public static void draw(DrawingUnit zf, double xA, double yA, double xE, double yE, int iterations) {
        if (iterations <= 0) {
            zf.drawLine(xA, yA, xE, yE);
        } else {
            double[] folded = foldLine(xA, yA, xE, yE);
            iterations--;
            draw(zf, folded[0], folded[1], folded[2], folded[3], iterations);
            draw(zf, folded[2], folded[3], folded[4], folded[5], iterations);
            draw(zf, folded[4], folded[5], folded[6], folded[7], iterations);
            draw(zf, folded[6], folded[7], folded[8], folded[9], iterations);
        }
    }

}
