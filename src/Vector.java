import java.lang.Math.*;

public class Vector {
  double x, y, z;
  public Vector() {}
  public Vector(double i, double j, double k) {
    x = i;
    y = j;
    z = k;
  }
  public double magnitude() {
    return Math.sqrt(x * x + y * y + z * z);
  }
  public void normalise() {
    double mag = magnitude();
    if (mag != 0) {
      x /= mag;
      y /= mag;
      z /= mag;
    }
  }
  public double dot(Vector a) {
    return x * a.x + y * a.y + z * a.z;
  }
  public Vector sub(Vector a) {
    return new Vector(x - a.x, y - a.y, z - a.z);
  }
  public Vector add(Vector a) {
    return new Vector(x + a.x, y + a.y, z + a.z);
  }
  public Vector mul(double d) {
    return new Vector(d * x, d * y, d * z);
  }
  public void print() {
    System.out.println("x=" + x + ", y=" + y + ", z=" + z);
  }

  public double getX() {
    return x;
  }
  public double getY() {
    return y;
  }
  public double getZ() {
    return z;
  }

  public Vector cross(Vector v2) {
    return new Vector(y*v2.z-z*v2.y, z*v2.x-x*v2.z, x*v2.y-y*v2.x);
  }

}