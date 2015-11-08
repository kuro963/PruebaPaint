/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paintprueba;

import static java.lang.Math.abs;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 *
 * @author hugo
 */
public class FXMLDocumentController implements Initializable {
    
     @FXML
    private ColorPicker colorPicker; 
    
    @FXML
    private Button btnBox, btnRect, btnCylinder,btnBorrar, btnLine;
   
    @FXML
    private SubScene sub1;
    
    Group root = new Group(); 
    private String figura = "";
    boolean inDrag = false;
    double startX = -1, startY = -1;
    boolean borrar = false;
    //Contadores de las figuras
    int contadorLine = 0,contadorB = 0,contadorC = 0;
    
    
     @FXML
    private void setRec(ActionEvent event) {
        figura = "rec";
    }
     @FXML
    private void setB(ActionEvent event) {
        figura = "box";
    }
    @FXML private void borrar(){
        borrar = true;
    //    cursorBorrador();
    }
     @FXML
    private void setC(ActionEvent event){
        figura = "cylinder";
    }
     @FXML
    private void setLine(ActionEvent event){
        figura = "line";
    }
   private double posX(double x){
        if(x<=370){
            return -370+x;
        }else{
            return (x - 371) + 1;
        }
    }
    //Función para acomodar las coordenadas
    private double posY(double y){
        if(y<=315){
            return -315+y;
        }else{ 
           return (y - 316) * (315) / (314) + 1;
        }
    }
 @FXML
    private void onMousePressedListener(MouseEvent e){
        this.startX = e.getX();
        this.startY = e.getY();
        //Funcion para activar la lectura del drag en la funcion de drag
        inDrag = true;
        System.err.println("mousePressed at" + startX + ", "+ startY);
    }  
    @FXML
    private void drag(MouseEvent e){
         double curX = e.getX();
         double curY = e.getY();
         String id ="";        
         if (inDrag == true) {
             sub1.setCursor(Cursor.CROSSHAIR);
             switch(figura){
                 case "box": 
                     //Contador para quitar anterior
                     contadorB =contadorB-1;
                     id = "#"+figura+contadorB;
                     root.getChildren().remove(root.lookup(id));
                     addBox(posX(startX),posY(startY),abs((curX-startX))*2);                   
                     break;
                 case "cylinder": 
                     contadorC = contadorC-1;
                     id = "#"+figura+contadorC;
                     root.getChildren().remove(root.lookup(id));
                     addCylinder(posX(startX),posY(startY),abs((curX-startX)));
                     break;
                
                 case "line":
                     contadorLine = contadorLine-1;
                     id = "#"+figura+contadorLine;
                     root.getChildren().remove(root.lookup(id));
                     addLine(posX(startX),posY(startY),posX(curX),posY(curY));
                     break;
             }
             //Elimina todas las figuras
            //root.getChildren().clear();
             
        }
    }
     
    @FXML void clear(ActionEvent event){
        root.getChildren().clear();
    }
      @FXML
    private void onReleased(MouseEvent e){
        inDrag = false;
        sub1.setCursor(Cursor.DEFAULT);
        //Este switch es para aumentar los contadores para el id
        //porque si no se aumentan se elmina la figura dibujada anterior siempre
        switch(figura){
            case "box": 
                contadorB++;
                break;
            case "cylinder": 
                contadorC++;
                break;
            case "line":
                contadorLine++;
                break;
             }
    }
      public void addLine(double x, double y, double x2, double y2){
       Line l= new Line();
       l.setId("line"+contadorLine++);
       l.setStartX(x);
       l.setStartY(y);
       l.setEndX(x2);
       l.setEndY(y2);
       l.setStrokeWidth(5);
       l.setStroke(colorPicker.getValue());
        l.setOnMousePressed((e) ->{
            l.setStroke(colorPicker.getValue());
            if(borrar){
                 sub1.setCursor(Cursor.DEFAULT);
                borrar = false;
                root.getChildren().remove(l);
            }   
        });
       root.getChildren().add(l);
    }
       public void cursorBorrador(){
       Image borrador = new Image(getClass().getResourceAsStream("borrador.png"));
       sub1.setCursor(new ImageCursor(borrador,
       borrador.getWidth()/2,
       borrador.getHeight()/2));
   }
        public void addBox(double x, double y, double tam){
        Box box = new Box(tam, tam,tam);
        PhongMaterial b = new PhongMaterial();
        b.setDiffuseColor(colorPicker.getValue());
        box.setMaterial(b);
        box.setOnMousePressed((e) ->{
            b.setDiffuseColor(colorPicker.getValue());
            if(borrar){
                 sub1.setCursor(Cursor.DEFAULT);
                borrar = false;
                root.getChildren().remove(box);
            }   
        });
        //Se asigna un id a la esfera para poder elminarla facilmente
        box.setId("box"+contadorB++);
        box.setCullFace(CullFace.BACK);
        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(-100);
        root.getChildren().add(box);
        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(30);
        ryBox.setAngle(40);
        rzBox.setAngle(0);
        box.getTransforms().addAll(rxBox, ryBox, rzBox);
    }
    public void addCylinder(double x, double y, double radio){
        Cylinder cylinder = new Cylinder(radio,radio*2); 
        PhongMaterial c = new PhongMaterial();
        c.setDiffuseColor(colorPicker.getValue());
        cylinder.setMaterial(c);
        cylinder.setId("cylinder"+contadorC++);
        root.getChildren().add(cylinder);
        cylinder.setTranslateX(x); 
        cylinder.setTranslateY(y); 
        cylinder.setTranslateZ(300);
        cylinder.setOnMousePressed((e) ->{
            c.setDiffuseColor(colorPicker.getValue());
            if(borrar){
                 sub1.setCursor(Cursor.DEFAULT);
                borrar = false;
                root.getChildren().remove(cylinder);
                
            }   
        });
        Rotate rxC = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryC = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzC = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxC.setAngle(30);
        ryC.setAngle(40);
        rzC.setAngle(0);
        cylinder.getTransforms().addAll(rxC, ryC, rzC);
        
    }
    @FXML
    private void closeButtonAction(){
        System.exit(0);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(20000.0);
        camera.setTranslateZ(-1000);
        colorPicker.setValue(Color.BLUEVIOLET);
        
        camera.setFieldOfView(35);
        sub1.setCamera(camera);
        sub1.setRoot(root); 
       
        Image imageBox = new Image(getClass().getResourceAsStream("cubo.png"));
        ImageView iv = new ImageView(imageBox);
        iv.setFitWidth(30);
        iv.setFitHeight(30);
        btnBox.setGraphic(iv);
        
        Image imageCylinder = new Image(getClass().getResourceAsStream("cilindro.png"));
        ImageView ic = new ImageView(imageCylinder);
        ic.setFitWidth(30);
        ic.setFitHeight(30);
        btnCylinder.setGraphic(ic);
        
        
        Image imageLine = new Image(getClass().getResourceAsStream("line.png"));
        ImageView iL = new ImageView(imageLine);
        iL.setFitHeight(20);
        iL.setFitWidth(30);
        btnLine.setGraphic(iL);
        
        Image imageBorrador = new Image(getClass().getResourceAsStream("borrador.png"));
        ImageView iB = new ImageView(imageBorrador);
        iB.setFitHeight(25);
        iB.setFitWidth(35);
        btnBorrar.setGraphic(iB);
        
        
        
     
    } 
    @FXML
    private void AcercaDe(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Créditos");
        alert.setHeaderText("Universidad Politécnica de Chiapas");
        alert.setContentText("Josseline Juliane Arreola Cruz | Matricula: 143471\n Javier de Jesús Flores Herrera | Matricula: 143372 \n Hugo Sarmiento Toledo | Matricula: 143359 \n Dr. Juan Carlos López Pimentel \n Programación Visual");
        alert.showAndWait();
    }
   
    }    
    

