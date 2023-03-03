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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
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

  double cameraRotation = 90;

  double cameraElevation = 90;

  Sphere s1 = new Sphere(100, new Vector(0,0,0),new Vector(1,0,0));
  Sphere s2 = new Sphere(100, new Vector(-160,160,160),new Vector(0,1,0));
  Sphere s3 = new Sphere(100, new Vector(160,-160,320),new Vector(0,0,1));


  @Override
  public void start(Stage stage) {

    GridPane root = new GridPane();
    root.setVgap(15);
    root.setHgap(15);

    ArrayList<Slider> sliderArray = new ArrayList<>();
    stage.setTitle("Ray Tracing");

    //We need 3 things to see an image
    //1. We create an image we can write to
    WritableImage image = new WritableImage(Width, Height);
    //2. We create a view of that image
    ImageView view = new ImageView(image);
    //3. Add to the pane (below)

    //XYZ sliders
    Slider x_slider = new Slider(-320, 320, 0);
    x_slider.setPrefWidth(400);
    Slider y_slider = new Slider(-320, 320, 0);
    y_slider.setPrefWidth(400);
    Slider z_slider = new Slider(-320, 320, 0);
    z_slider.setPrefWidth(400);

    //Colour sliders
    Slider r_slider = new Slider(0, 255, red*255);
    Slider g_slider = new Slider(0, 255, green*255);
    Slider b_slider = new Slider(0, 255, blue*255);

    //radius slider
    Slider radius_slider = new Slider(10, 200, radius);

    //Camera rotation slider
    Slider camera_slider = new Slider(0, 360, cameraRotation);

    //Camera elevation slider
    Slider elevation_slider = new Slider(0, 180, cameraElevation);

    sliderArray.add(x_slider);
    sliderArray.add(y_slider);
    sliderArray.add(z_slider);
    sliderArray.add(r_slider);
    sliderArray.add(g_slider);
    sliderArray.add(b_slider);
    sliderArray.add(radius_slider);
    sliderArray.add(camera_slider);
    sliderArray.add(elevation_slider);

    ToggleGroup toggleSpheres = new ToggleGroup();

    RadioButton s1button = new RadioButton();
    s1button.setPrefWidth(120);
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

    for (int i = 0; i<sliderArray.size(); i++) {
      Slider currentSlider = sliderArray.get(i);
      currentSlider.setShowTickMarks(true);
      currentSlider.setShowTickLabels(true);
      elevation_slider.setPrefWidth(400);
      switch (i) {
        case 0 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          x = newValue.doubleValue();
          Render(image);
        });
        case 1 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          y = newValue.doubleValue();
          Render(image);
        });
        case 2 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          z = newValue.doubleValue();
          Render(image);
        });
        case 3 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          red = newValue.doubleValue();
          Render(image);
        });
        case 4 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          green = newValue.doubleValue();
          Render(image);
        });
        case 5 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          blue = newValue.doubleValue();
          Render(image);
        });
        case 6 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          radius = newValue.doubleValue();
          Render(image);
        });
        case 7 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          cameraRotation = newValue.doubleValue();
          Render(image);
        });
        case 8 -> currentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
          cameraElevation = newValue.doubleValue();
          Render(image);
        });
      }
      root.add(currentSlider, 1, i + 1);
    }

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
              int i = 0;
              sliderArray.get(i++).setValue(selectedSphere.getCentPos().x);
              sliderArray.get(i++).setValue(selectedSphere.getCentPos().y);
              sliderArray.get(i++).setValue(selectedSphere.getCentPos().z);
              sliderArray.get(i++).setValue(selectedSphere.getRed() * 255);
              sliderArray.get(i++).setValue(selectedSphere.getGreen() * 255);
              sliderArray.get(i++).setValue(selectedSphere.getBlue() * 255);
              sliderArray.get(i++).setValue(selectedSphere.getRadius());
            });

    //The following is in case you want to interact with the image in any way
    //e.g., for user interaction, or you can find out the pixel position for debugging
    view.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
      System.out.println(event.getX() + " " + event.getY());
      event.consume();
    });

    Render(image);

    ScrollPane scroll = new ScrollPane();
    scroll.setContent(root);


    Label xSliderLabel = new Label("X Slider");
    xSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    xSliderLabel.setPrefWidth(100);
    Label ySliderLabel = new Label("Y Slider");
    ySliderLabel.setPrefWidth(100);
    ySliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    Label zSliderLabel = new Label("Z Slider");
    zSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    zSliderLabel.setPrefWidth(100);
    Label rSliderLabel = new Label("Red Slider");
    rSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    rSliderLabel.setPrefWidth(100);
    Label gSliderLabel = new Label("Green Slider");
    gSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    gSliderLabel.setPrefWidth(100);
    Label bSliderLabel = new Label("Blue Slider");
    bSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    bSliderLabel.setPrefWidth(100);
    Label radiusSliderLabel = new Label("Radius Slider");
    radiusSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    radiusSliderLabel.setPrefWidth(100);
    Label cameraSliderLabel = new Label("Rotation Slider");
    cameraSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    cameraSliderLabel.setPrefWidth(100);
    Label elevationSliderLabel = new Label("Elevation Slider");
    elevationSliderLabel.setAlignment(Pos.BASELINE_RIGHT);
    elevationSliderLabel.setPrefWidth(100);

    root.add(view,0,0);
    GridPane.setColumnSpan(view, 3);
    root.add(xSliderLabel, 0, 1);
    root.add(ySliderLabel, 0, 2);
    root.add(zSliderLabel, 0, 3);
    root.add(rSliderLabel, 0, 4);
    root.add(gSliderLabel, 0, 5);
    root.add(bSliderLabel, 0, 6);
    root.add(radiusSliderLabel, 0, 7);
    root.add(cameraSliderLabel, 0, 8);
    root.add(elevationSliderLabel, 0, 9);

    //3. (referring to the 3 things we need to display an image)
    //we need to add it to the pane

    root.add(s1button, 2, 1);
    root.add(s2button, 2, 2);
    root.add(s3button, 2, 3);

    //Display to user
    Scene scene = new Scene(scroll, 768, 768);
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

    Vector light = new Vector(0,300,-1000);

    double vrpRadius = 400;
    double originRadius = 1000;

    if (cameraElevation == 0) cameraElevation = 0.1;

    //Spherical points calculations
    double vrpX = vrpRadius*Math.sin(Math.toRadians(cameraElevation))*Math.cos(Math.toRadians(cameraRotation));
    double vrpY = vrpRadius*Math.cos(Math.toRadians(cameraElevation));
    double vrpZ = vrpRadius*Math.sin(Math.toRadians(cameraElevation))*Math.sin(Math.toRadians(cameraRotation));

    double originX = -originRadius*Math.sin(Math.toRadians(cameraElevation))*Math.cos(Math.toRadians(cameraRotation));
    double originY = -originRadius*Math.cos(Math.toRadians(cameraElevation));
    double originZ = -originRadius*Math.sin(Math.toRadians(cameraElevation))*Math.sin(Math.toRadians(cameraRotation));

    Vector origin =  new Vector(originX,originY,originZ);

    //Perspective camera
    Vector VRP = new Vector(vrpX,vrpY,vrpZ); //Centre of the image plane
    Vector VUV = new Vector(0,1,0); //Approx up direction of the camera
    Vector lookAt = new Vector(0,0,0); //Point defining where the camera is pointing
    Vector VPN = lookAt.sub(VRP); //Direction the camera is looking
    VPN.normalise();
    Vector VRV = VPN.cross(VUV); //Right direction of the camera
    VRV.normalise();
    VUV = VRV.cross(VPN); //Accurate up direction of the camera
    VUV.normalise();
    double scale = 0.45; //Field of view

    //Background colour
    Vector bg_col = new Vector(0,0,0);

    //Light colour
    Vector lightColour = new Vector(1,1,1);

    //Object shininess
    double shininess = 1;

    //Looping through each pixel
    for (j = 0; j < h; j++) {
      for (i = 0; i < w; i++) {
        double u = (i-(w/2.0))*scale;
        double v = ((h-j)-h/2.0)*scale;
        Vector direction = VRP.add(VRV.mul(u)).add(VUV.mul(v));
        boolean hasIntersected = false;
        double lowestT = 999999999;
        Sphere closestSphere = null;

        //Finding the closest point of intersection.
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

        if (!hasIntersected) { //No sphere hit
          image_writer.setColor(i,j,Color.color(bg_col.x,bg_col.y,bg_col.z, 1.0));
        } else { //Sphere hit
          Vector pointOfIntersection = origin.add(direction.mul(closestSphere.findIntersection(origin,direction)));
          Vector lightVector = light.sub(pointOfIntersection);
          lightVector.normalise();
          Vector normal = pointOfIntersection.sub(closestSphere.getCentPos());
          normal.normalise();
          double dp = lightVector.dot(normal);
          Vector lightReflection = normal.mul(2*dp).sub(lightVector);
          lightReflection.normalise();
          double dp2 = lightReflection.dot(direction);
          if (dp2<0) dp2 = 0;
          if (dp2>1) dp2 = 1;
          if (dp<0) dp = 0;
          if (dp>1) dp = 1;
          Vector col = closestSphere.getColour().mul(dp*0.7).add(closestSphere.getColour().mul(0.3));//.add(closestSphere.getColour().mul(Math.pow(dp2, shininess)));

          image_writer.setColor(i, j, Color.color(col.x, col.y, col.z, 1.0));
        }
      } // column loop (x-axis loop)
    } // row loop (y-axis loop)
  }

  public static void main(String[] args) {
    launch();
  }
}