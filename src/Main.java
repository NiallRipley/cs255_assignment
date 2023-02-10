/*
CS-255 Getting started code for the assignment
I do not give you permission to post this code online
Do not post your solution online
Do not copy code
Do not use JavaFX functions or other libraries to do the main parts of the assignment (i.e. ray tracing steps 1-7)
All of those functions must be written by yourself
You may use libraries to achieve a better GUI
*/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.*;
import java.lang.Math.*;
import javafx.geometry.HPos;

public class Main extends Application {
  int Width = 640;
  int Height = 640;

  int green_col = 255; //just for the test example

  int xaxis = 0;
  int yaxis = 320;

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

    Slider g_slider = new Slider(5, 255, green_col);

    Slider x_slider = new Slider(-320, 320, xaxis);

    //Add all the event handlers
    g_slider.valueProperty().addListener(
      new ChangeListener < Number > () {
        public void changed(ObservableValue < ? extends Number >
          observable, Number oldValue, Number newValue) {
          green_col = newValue.intValue();
          Render(image);
        }
      });

    x_slider.valueProperty().addListener(
            new ChangeListener < Number > () {
        public void changed(ObservableValue < ? extends Number >
            observable, Number oldValue, Number newValue) {
            xaxis = newValue.intValue();
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
    root.add(g_slider, 0, 2);

    //Display to user
    Scene scene = new Scene(root, 1024, 768);
    stage.setScene(scene);
    stage.show();
  }

  public void Render(WritableImage image) {
    //Get image dimensions, and declare loop variables
    int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
    PixelWriter image_writer = image.getPixelWriter();

    double c = green_col / 255.0;
    Vector col = new Vector(0.5, c, 0.5);

    Vector light = new Vector(250,250,-200);

    Sphere s = new Sphere(100, new Vector(xaxis ,0,0));

    Vector o = new Vector(0,0,0); //origin of ray
    Vector d = new Vector(0,0,1); //direction of ray
    //Vector cs = new Vector(0,0,0); //centre of sphere
    //double r = 100; //radius of sphere
    Vector p; //3D points
    double t; // solution, where on the line it intersects the sphere
    double a, b;
    Vector v;

    for (j = 0; j < h; j++) {
      for (i = 0; i < w; i++) {
        o.x = i-320;
        o.y = j-320;
        o.z = -200;
        v = o.sub(s.getCentPos());
        a = d.dot(d);
        b = 2*v.dot(d);
        c = v.dot(v) - s.getRadius()*s.getRadius();

        double disc = b*b-4*a*c; //discriminant
        if (disc < 0) {
          col.x = 0.0;
          col.y = 0.0;
          col.z = 0.0;
        }
        else {
          col.x = 1.0;
          col.y = 1.0;
          col.z = 1.0;
        }
        t = (-b-Math.sqrt(disc))/(2*a); // ray sphere intersection
        p = o.add(d.mul(t)); //line
        Vector lv = light.sub(p);
        lv.normalise();
        Vector n = p.sub(s.getCentPos());
        n.normalise();
        double dp = lv.dot(n);
        if (dp<0) {
          col = new Vector(0,0,0);
        } else {
          if (dp>1) dp = 1;
          col = new Vector(dp,dp,dp);
        }

        image_writer.setColor(i, j, Color.color(col.x, col.y, col.z, 1.0));

      } // column loop
    } // row loop
  }

  public static void main(String[] args) {
    launch();
  }

}