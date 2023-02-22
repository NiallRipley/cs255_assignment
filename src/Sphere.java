public class Sphere {

    private double radius;
    private Vector centPos;
    private Vector colour;

    public Sphere(double r, Vector cs, Vector c) {
        centPos = cs;
        radius = r;
        colour = c;
    }

    public Vector getCentPos() {
        return centPos;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getRed() {return colour.x;}

    public Double getGreen() {return colour.y;}

    public Double getBlue() {return colour.z;}

    public Vector getColour() {return colour;}
}
