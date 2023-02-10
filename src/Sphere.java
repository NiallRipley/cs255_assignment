public class Sphere {

    private double radius;
    private Vector centPos;


    public Sphere(double r, Vector cs) {
        radius = r;
        centPos = cs;
    }

    public Vector getCentPos() {
        return centPos;
    }

    public Double getRadius() {
        return radius;
    }

}
