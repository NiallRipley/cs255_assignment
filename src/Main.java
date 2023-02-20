/*
CS-255 Getting started code for the assignment
I do not give you permission to post this code online
Do not post your solution online
Do not copy code
Do not use JavaFX functions or other libraries to do the main parts of the assignment (i.e. ray tracing steps 1-7)
All of those functions must be written by yourself
You may use libraries to achieve a better GUI
*/
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
  int Width = 640;
  int Height = 640;

  int red_col = 255;
  int green_col = 255; //just for the test example
  int blue_col = 255;

  //sphere 1
  int s1xaxis = 0;
  int s1yaxis = 0;
  int s1zaxis = 0;
//spehere 2
  int s2axis = 0;
  int s2xaxis = -160;
  int s2yaxis = 160;
  int s2zaxis = 160;
  //sphere 3
  int s3xaxis = 160;
  int s3yaxis = -160;
  int s3zaxis = -160;
  @Override
  public void start(Stage stage) throws FileNotFoundException {
    stage.setTitle("Ray Tracing");

    //We need 3 things to see an image
    //1. We create an image we can write to
    WritableImage image = new WritableImage(Width, Height);
    //2. We create a view of that image
    ImageView view = new ImageView(image);
    //3. Add to the pane (below)

    //Create the simple GUI
    Slider r_slider = new Slider(0, 255, red_col);
    Slider g_slider = new Slider(0, 255, green_col);
    Slider b_slider = new Slider(0, 255, blue_col);

    Slider x_slider = new Slider(-320, 320, s1xaxis);

    //Add all the event handlers
    x_slider.valueProperty().addListener(
            new ChangeListener < Number > () {
              public void changed(ObservableValue < ? extends Number >
                                          observable, Number oldValue, Number newValue) {
                s1xaxis = newValue.intValue();
                Render(image);
              }
            });

    r_slider.valueProperty().addListener(
            new ChangeListener < Number > () {
              public void changed(ObservableValue < ? extends Number >
                                          observable, Number oldValue, Number newValue) {
                red_col = newValue.intValue();
                Render(image);
              }
            });


    g_slider.valueProperty().addListener(
            new ChangeListener < Number > () {
              public void changed(ObservableValue < ? extends Number >
                                          observable, Number oldValue, Number newValue) {
                green_col = newValue.intValue();
                Render(image);
              }
            });

    b_slider.valueProperty().addListener(
            new ChangeListener < Number > () {
              public void changed(ObservableValue < ? extends Number >
                                          observable, Number oldValue, Number newValue) {
                blue_col = newValue.intValue();
                Render(image);
              }
            });


    //The following is in case you want to interact with the image in any way
    //e.g., for user interaction, or you can find out the pixel position for debugging
    view.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
      System.out.println(event.getX() + " " + event.getY());
      event.consume();
    });

    Render(image);

    GridPane root = new GridPane();
    root.setVgap(12);
    root.setHgap(12);

    //3. (referring to the 3 things we need to display an image)
    //we need to add it to the pane
    root.add(view, 0, 0);
    root.add(x_slider, 0, 1);
    root.add(r_slider, 0, 2);
    root.add(g_slider, 0, 3);
    root.add(b_slider, 0, 4);

    //Display to user
    Scene scene = new Scene(root, 1024, 768);
    stage.setScene(scene);
    stage.show();
  }

  public double Discriminant(double a, double b, double c) {
    return b*b-4*a*c;
  }

  public double Quadratic(double a,double b,double c, Boolean close) {
    double disc = Discriminant(a,b,c);
    if (close) return (-b-Math.sqrt(disc))/(2*a);
    else return (-b+Math.sqrt(disc))/(2*a);
  }

  public void Render(WritableImage image) {
    //Get image dimensions, and declare loop variables
    int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
    PixelWriter image_writer = image.getPixelWriter();

    double rc = red_col / 255.0;
    double gc = green_col / 255.0;
    double bc = blue_col / 255.0;


    Vector light = new Vector(0,150,-300);

    Vector sphere_col = new Vector(rc,gc,bc);
    Sphere s1 = new Sphere(100, new Vector(s1xaxis,s1yaxis,s1zaxis),sphere_col);
    Sphere s2 = new Sphere(100, new Vector(s2xaxis,s2yaxis,s2zaxis),sphere_col);
    Sphere s3 = new Sphere(100, new Vector(s3xaxis,s3yaxis,s3zaxis),sphere_col);



    Vector o = new Vector(0,0,0); //origin of ray
    Vector d = new Vector(0,0,1); //direction of ray
    //Vector cs = new Vector(0,0,0); //centre of sphere
    //double r = 100; //radius of sphere
    Vector p; //3D points
    double t; // solution, where on the line it intersects the sphere
    double a, b;
    Vector v;

    //col
    Vector bg_col = new Vector(0.5,0.5,0.5);

    for (j = 0; j < h; j++) {
      for (i = 0; i < w; i++) {
        o.x = i-320;
        o.y = j-320;
        o.z = -200;
        v = o.sub(s1.getCentPos());
        a = d.dot(d);
        b = 2*v.dot(d);
        double c = v.dot(v) - s1.getRadius()*s1.getRadius();
        double disc = Discriminant(a,b,c);

        if (disc < 0) {
          image_writer.setColor(i,j,Color.color(bg_col.x,bg_col.y,bg_col.z, 1.0));
        } else { //hit sphere
          image_writer.setColor(i,j,Color.color(sphere_col.x,sphere_col.y,sphere_col.z, 1.0));
          t = Quadratic(a,b,c,true); // ray sphere intersection, how far along
          if (t<0) t = Quadratic(a,b,c,false);
          if (t<0)
            image_writer.setColor(i,j,Color.color(bg_col.x,bg_col.y,bg_col.z, 1.0));
          else {
            p = o.add(d.mul(t)); //line
            Vector lv = light.sub(p);
            lv.normalise();
            Vector n = p.sub(s1.getCentPos());
            n.normalise();
            double dp = lv.dot(n);
            if (dp<0) dp = 0;
            if (dp>1) dp = 1;
            image_writer.setColor(i, j, Color.color(dp*sphere_col.x, dp*sphere_col.y, dp*sphere_col.z, 1.0));
          }
        }
      } // column loop (x-axis loop)
    } // row loop (y-axis loop)
  }

  public static void main(String[] args) {
    launch();
  }
}