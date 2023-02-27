public class Sphere {

    private double radius;
    private Vector centPos;
    private Vector colour;

    public Sphere(double r, Vector cs, Vector c) {
        centPos = cs;
        radius = r;
        colour = c;
    }

    private double Discriminant(double a, double b, double c) {
        return b*b-4*a*c;
    }

    private double Quadratic(double a,double b,double c, Boolean close) {
        double disc = Discriminant(a,b,c);
        if (close) return (-b-Math.sqrt(disc))/(2*a);
        else return (-b+Math.sqrt(disc))/(2*a);
    }

    public boolean checkIntersection(Vector origin, Vector direction) {
        Vector v = origin.sub(centPos);
        double a = direction.dot(direction);
        double b = 2*v.dot(direction);
        double c = v.dot(v) - radius*radius;
        double disc = Discriminant(a,b,c);
        return disc >= 0;
    }

    public Double findIntersection(Vector origin, Vector direction) {
        Vector v = origin.sub(centPos);
        double a = direction.dot(direction);
        double b = 2*v.dot(direction);
        double c = v.dot(v) - radius*radius;

        double t = Quadratic(a,b,c,true); // ray sphere intersection, how far along
        if (t<0) t = Quadratic(a,b,c,false);
        return t;
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
