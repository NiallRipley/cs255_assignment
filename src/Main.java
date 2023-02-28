/*
CS-255 Getting started code for the assignment
I do not give you permission to post this code online
Do not post your solution online
Do not copy code
Do not use JavaFX functions or other libraries to do the main parts of the assignment (i.e. ray tracing steps 1-7)
All of those functions must be written by yourself
You may use libraries to achieve a better GUI
*/
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
  int Width = 640;
  int Height = 640;

  int selected = 0;
  Sphere selectedSphere;

  double x = 0;
  double y = 0;
  double z = 0;

  double red = 0.3;
  double green = 0.5;
  double blue = 0;

  double radius = 100;

  Sphere s1 = new Sphere(100, new Vector(0,0,0),new Vector(0.5,0.3,1));
  Sphere s2 = new Sphere(100, new Vector(-160,160,160),new Vector(0.5,0.3,1));
  Sphere s3 = new Sphere(100, new Vector(160,-160,320),new Vector(0.5,0.3,1));


  @Override
  public void start(Stage stage) {
    stage.setTitle("Ray Tracing");

    //We need 3 things to see an image
    //1. We create an image we can write to
    WritableImage image = new WritableImage(Width, Height);
    //2. We create a view of that image
    ImageView view = new ImageView(image);
    //3. Add to the pane (below)

    //all values are related to middle sphere (s1)
    Slider x_slider = new Slider(-320, 320, 0);
    Slider y_slider = new Slider(-320, 320, 0);
    Slider z_slider = new Slider(-320, 320, 0);

    //Create the simple GUI
    Slider r_slider = new Slider(0, 255, red*255);
    Slider g_slider = new Slider(0, 255, green*255);
    Slider b_slider = new Slider(0, 255, blue*255);

    //radius slider
    Slider radius_slider = new Slider(10, 200, radius);

    ToggleGroup toggleSpheres = new ToggleGroup();

    RadioButton s1button = new RadioButton();
    s1button.setToggleGroup(toggleSpheres);
    s1button.setSelected(true);
    s1button.setUserData("Sphere 1");

    RadioButton s2button = new RadioButton();
    s2button.setToggleGroup(toggleSpheres);
    s2button.setUserData("Sphere 2");

    RadioButton s3button = new RadioButton();
    s3button.setToggleGroup(toggleSpheres);
    s3button.setUserData("Sphere 3");

    s1button.setText("Sphere 1");
    s2button.setText("Sphere 2");
    s3button.setText("Sphere 3");

    ScrollPane scroll = new ScrollPane();
    scroll.setContent(view);

    x_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              x = newValue.doubleValue();
              Render(image);
            });

    y_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              y = newValue.doubleValue();
              Render(image);
            });

    z_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              z = newValue.doubleValue();
              Render(image);
            });

    r_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              red = newValue.doubleValue() / 255;
              Render(image);
            });

    g_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              green = newValue.doubleValue() / 255;
              Render(image);
            });

    b_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              blue = newValue.doubleValue() / 255;
              Render(image);
            });

    radius_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              radius = newValue.doubleValue();
              Render(image);
            });

    //for x, y and z sliders
    toggleSpheres.selectedToggleProperty().addListener(
            (ov, old_toggle, new_toggle) -> {
              if (toggleSpheres.getSelectedToggle().getUserData() == "Sphere 1") {
                selectedSphere = s1;
                selected = 0;
              } else if (toggleSpheres.getSelectedToggle().getUserData() == "Sphere 2") {
                selectedSphere = s2;
                selected = 1;
              } else if (toggleSpheres.getSelectedToggle().getUserData() == "Sphere 3") {
                selectedSphere = s3;
                selected = 2;
              } else {
                System.out.println("Error while changing spheres");
              }
              x_slider.setValue(selectedSphere.getCentPos().getX());
              y_slider.setValue(selectedSphere.getCentPos().getY());
              z_slider.setValue(selectedSphere.getCentPos().getZ());
              r_slider.setValue(selectedSphere.getRed() * 255);
              g_slider.setValue(selectedSphere.getGreen() * 255);
              b_slider.setValue(selectedSphere.getBlue() * 255);
              radius_slider.setValue(selectedSphere.getRadius());
            });

    //The following is in case you want to interact with the image in any way
    //e.g., for user interaction, or you can find out the pixel position for debugging
    view.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
      System.out.println(event.getX() + " " + event.getY());
      event.consume();
    });

    Render(image);

    GridPane root = new GridPane();
    root.setVgap(12);
    root.setHgap(12);

    root.setAlignment(Pos.CENTER);

    //needs tweaking
    Label label = new Label("X Slider");
    HBox hbox = new HBox(label, x_slider);
    hbox.setMinWidth(Width);
    hbox.setMaxWidth(Width);

    //3. (referring to the 3 things we need to display an image)
    //we need to add it to the pane

    root.add(view, 0, 0);;
    root.add(hbox, 0, 1);
    //root.add(x_slider, 0, 1);
    root.add(y_slider, 0, 2);
    root.add(z_slider, 0, 3);

    root.add(r_slider, 0, 4);
    root.add(g_slider, 0, 5);
    root.add(b_slider, 0, 6);

    root.add(radius_slider, 0, 7);

    root.getChildren().addAll(scroll);

    root.add(s1button, 2, 1);
    root.add(s2button, 2, 2);
    root.add(s3button, 2, 3);

    x_slider.setShowTickMarks(true);
    x_slider.setShowTickLabels(true);
    y_slider.setShowTickMarks(true);
    y_slider.setShowTickLabels(true);
    z_slider.setShowTickMarks(true);
    z_slider.setShowTickLabels(true);

    r_slider.setShowTickMarks(true);
    r_slider.setShowTickLabels(true);
    g_slider.setShowTickMarks(true);
    g_slider.setShowTickLabels(true);
    b_slider.setShowTickMarks(true);
    b_slider.setShowTickLabels(true);

    radius_slider.setShowTickMarks(true);
    radius_slider.setShowTickLabels(true);

    //Display to user
    Scene scene = new Scene(root, 1024, 768);
    stage.setScene(scene);
    stage.show();
  }

  public void Render(WritableImage image) {
    //Get image dimensions, and declare loop variables
    int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
    PixelWriter image_writer = image.getPixelWriter();

    ArrayList<Sphere> sphereArray = new ArrayList<>();

    switch (selected) {
      case 0 ->
              s1 = new Sphere(radius, new Vector(x, y, z), new Vector(red, green, blue));
      case 1 ->
              s2 = new Sphere(radius, new Vector(x, y, z), new Vector(red, green, blue));
      case 2 ->
              s3 = new Sphere(radius, new Vector(x, y, z), new Vector(red, green, blue));
    }

    sphereArray.add(s1);
    sphereArray.add(s2);
    sphereArray.add(s3);

    Vector light = new Vector(0,300,-400);


    Vector origin;
    Vector direction = new Vector(0,0,1); //direction of ray
    Vector p; //3D points

    Vector VRP = new Vector(0,0,400); //Centre of the image plane
    Vector VUV = new Vector(0,1,0); //Up direction of the camera
    Vector lookAt = new Vector(0,0,0); //Point defining where the camera is pointing
    Vector VPN = lookAt.sub(VRP); //Direction the camera is looking
    VPN.normalise();
    Vector VRV = VPN.cross(VUV); //Right direction of the camera
    VRV.normalise();
    VUV = VRV.cross(VPN);
    VUV.normalise();

    double scale = 1;

    //col
    Vector bg_col = new Vector(0.5,0.5,0.5);

    for (j = 0; j < h; j++) {
      for (i = 0; i < w; i++) {
        double u = (i-(w/2))*scale;
        double v = ((h-j)-h/2)*scale;
        origin =  new Vector(0, 0, -400);
        direction = VRP.add(VRV.mul(u)).add(VUV.mul(v));
        //direction.print();
        //origin =  new Vector(i-w/2.0, j-h/2.0, -600);
        boolean hasIntersected = false;
        double lowestT = 999999999;
        Sphere closestSphere = null;

        for (Sphere currentSp : sphereArray) {
          if (currentSp.checkIntersection(origin, direction)) {
            double intersectionPoint = currentSp.findIntersection(origin, direction);
            if (intersectionPoint >= 0 && intersectionPoint < lowestT) {
              closestSphere = currentSp;
              lowestT = intersectionPoint;
              hasIntersected = true;
            }
          }
        }

        if (!hasIntersected) {
          image_writer.setColor(i,j,Color.color(bg_col.x,bg_col.y,bg_col.z, 1.0));
        } else {
          p = origin.add(direction.mul(closestSphere.findIntersection(origin,direction))); //line
          Vector lv = light.sub(p);
          lv.normalise();
          Vector n = p.sub(closestSphere.getCentPos());
          n.normalise();
          double dp = lv.dot(n);
          if (dp<0) dp = 0;
          if (dp>1) dp = 1;
          Vector col = closestSphere.getColour().mul(dp*0.7).add(closestSphere.getColour().mul(0.3));
          image_writer.setColor(i, j, Color.color(col.x, col.y, col.z, 1.0));
        }
      } // column loop (x-axis loop)
    } // row loop (y-axis loop)
  }

  public static void main(String[] args) {
    launch();
  }
}