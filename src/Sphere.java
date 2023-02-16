public class Sphere {

    private double radius;
    private Vector centPos;
    private Vector colour;

    public Sphere(double r, Vector cs, Vector c) {
        radius = r;
        centPos = cs;
        colour = c;
    }

    public Vector getCentPos() {
        return centPos;
    }

    public Double getRadius() {
        return radius;
    }
}
