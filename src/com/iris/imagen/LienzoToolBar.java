/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.iris.reproductorsm.ReproductorSM;

/**
 * Clase encargada de construir un <code>JPanel</code> con las herramientas necesarias
 * para el dibujado sobre lienzos.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>.
 */
public class LienzoToolBar extends JPanel {
    public static final int SINRELLENO = 0, LISO = 1, DEGRADADO = 2;
    public static final int HORIZONTAL = 1, VERTICAL = 2;
    public static final int CONTINUA = 0, DISCONTINUA = 1;
    
    private String forma;
    private Color fgcolor, bgcolor;
    private boolean relleno, editable, seleccion;
    private int grosor;
    private int tiporelleno, dirrelleno;
    private int trazo;
    private final JPanel shapesPanel, colorsPanel, trazoPanel;
    private MyButtonGroup shapesGroup;
    private JButton fgcolorBtn, bgcolorBtn;
    private JComboBox trazoBtn;
    private JComboBox rellenoBtn;
    private JSpinner grosorBtn;
    private ReproductorSM repSM;
    private MyShapes selectedShape = null;
    
    /**
     * Constructor común.
     * @param rep <code>ReproductorSM</code> asociado.
     */
    public LienzoToolBar(ReproductorSM rep){
        repSM = rep;
        trazo = tiporelleno = dirrelleno = 0;
        grosor = 2;
        relleno = editable = seleccion = false;
        forma = "Punto";
        fgcolor = Color.black;
        bgcolor = Color.white;
        
        GridBagConstraints c;
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(300, 200));
        this.setMaximumSize(new Dimension(300, 300));
        this.setBorder(BorderFactory.createTitledBorder("Dibujo:"));
        
        // Crear Panel de las formas
        shapesPanel = createShapesPanel();
        // Crear Panel para los colores
        colorsPanel = createColorsPanel();
        // Crear Panel de atributos (grosor, trazo, relleno)
        trazoPanel = createTrazoPanel();

        // Restricciones del layout.
        // Panel de formas row 0, col 0
        //c.fill = GridBagConstraints.BOTH;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1;
        c.weightx = 1;
        this.add(shapesPanel, c);
        
        // Panel de colores row 0, col 1;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        //c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 0;
        this.add(colorsPanel, c);
        
        // Panel de atributos row 1, col 0;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 0;
        this.add(trazoPanel, c);       
    }
    
    /**
     * Ajusta el valor de todos los atributos de la barra de herramientas
     * con los valores de la forma <code>MyShapes</code> recibida como 
     * argumento.
     * 
     * @param shape <code>MyShapes</code> a leer.
     */
    public void readSelectedShape(MyShapes shape){
        if (selectedShape != null)
            selectedShape.setSelected(false);
        
        selectedShape = shape;
        fgcolor = shape.getfgColor();
        bgcolor = shape.getbgColor();
        grosor = shape.getGrosor();
        grosorBtn.setValue(grosor);
        fgcolorBtn.setBackground(fgcolor);
        bgcolorBtn.setBackground(bgcolor);
        trazo = shape.getTrazo();
        trazoBtn.setSelectedIndex(trazo);
        relleno = shape.getFilled();
        tiporelleno = shape.getFilledType();
        dirrelleno = shape.getFilledDirection();

        // Actualizar combobox de relleno
        if (!relleno){
            rellenoBtn.setSelectedIndex(SINRELLENO);
        }else{
            if (tiporelleno == LISO)
                rellenoBtn.setSelectedIndex(LISO);
            if (tiporelleno == DEGRADADO && dirrelleno == HORIZONTAL)
                rellenoBtn.setSelectedIndex(2);
            if (tiporelleno == DEGRADADO && dirrelleno == VERTICAL)
                rellenoBtn.setSelectedIndex(3);
        }
    }

    /**
     * Crea un <code>JPanel</code> con las <code>MyShapes</code> disponibles
     * para el dibujado.
     * 
     * @return <code>JPanel</code> de <code>MyShapes</code>.
     */
    private JPanel createShapesPanel(){
        JPanel panel = new JPanel();
        shapesGroup = new MyButtonGroup();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Formas:"));
        panel.setPreferredSize(new Dimension(150,100));
        panel.setMinimumSize(new Dimension(150,100));
        
        JToggleButton punto = new JToggleButton();
        JToggleButton linea = new JToggleButton();
        JToggleButton rectangulo = new JToggleButton();
        JToggleButton elipse = new JToggleButton();
        JToggleButton mover = new JToggleButton();
        JToggleButton seleccionar = new JToggleButton();
        JToggleButton curva = new JToggleButton();

        ImageIcon iconLapiz = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Lapiz.gif"));
        ImageIcon iconLinea = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Linea.gif"));
        ImageIcon iconRectangulo = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Rectangulo.gif"));
        ImageIcon iconOvalo = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Ovalo.gif"));
        ImageIcon iconSel = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Hand.png"));
        ImageIcon iconMover = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Move.png"));
        ImageIcon iconCurva = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Curva.png"));

        // Properties of seleccionar
        seleccionar.setIcon(iconSel);
        seleccionar.setName("Seleccionar");
        seleccionar.setSelectedIcon(iconSel);
        seleccionar.setBorderPainted(false);
        seleccionar.setBorder(null);
        seleccionar.setMargin(new Insets(0, 0, 0, 0));
        seleccionar.setToolTipText("Seleccionar forma");

        // Properties of mover
        mover.setIcon(iconMover);
        mover.setName("Mover");
        mover.setSelectedIcon(iconMover);
        mover.setBorderPainted(false);
        mover.setBorder(null);
        mover.setMargin(new Insets(0, 0, 0, 0));
        mover.setToolTipText("Mover forma");
        
        
        // Properties of punto
        punto.setIcon(iconLapiz);
        punto.setName("Punto");
        punto.setSelectedIcon(iconLapiz);
        punto.setBorderPainted(false);
        punto.setBorder(null);
        punto.setMargin(new Insets(0, 0, 0, 0));
        punto.setSelected(true);
        punto.setToolTipText("Punto");

        // Properties of linea
        linea.setIcon(iconLinea);
        linea.setName("Linea");
        linea.setSelectedIcon(iconLinea);
        linea.setBorderPainted(false);
        linea.setBorder(null);
        linea.setMargin(new Insets(0, 0, 0, 0));
        linea.setToolTipText("Linea");

        // Properties of rectangulo
        rectangulo.setIcon(iconRectangulo);
        rectangulo.setName("Rectangulo");
        rectangulo.setSelectedIcon(iconRectangulo);
        rectangulo.setBorderPainted(false);
        rectangulo.setBorder(null);
        rectangulo.setMargin(new Insets(0, 0, 0, 0));
        rectangulo.setToolTipText("Rectángulo");

        // Properties of elipse
        elipse.setIcon(iconOvalo);
        elipse.setName("Elipse");
        elipse.setSelectedIcon(iconOvalo);
        elipse.setBorderPainted(false);
        elipse.setBorder(null);
        elipse.setMargin(new Insets(0, 0, 0, 0));
        elipse.setToolTipText("Elipse");

        // Properties of curva
        curva.setIcon(iconCurva);
        curva.setName("Curva");
        curva.setSelectedIcon(iconCurva);
        curva.setBorderPainted(false);
        curva.setBorder(null);
        curva.setMargin(new Insets(0, 0, 0, 0));
        curva.setToolTipText("Curva bezier");

        // Add items to the toolbar
        panel.add(seleccionar);
        panel.add(mover);
        panel.add(punto);
        panel.add(linea);
        panel.add(curva);
        panel.add(rectangulo);
        panel.add(elipse);
        
        // Add buttons to a Group object
        shapesGroup.add(seleccionar);
        shapesGroup.add(mover);
        shapesGroup.add(punto);
        shapesGroup.add(linea);
        shapesGroup.add(curva);
        shapesGroup.add(rectangulo);
        shapesGroup.add(elipse);

        // Listener
        for (Enumeration<AbstractButton> shapes = shapesGroup.getElements();
         shapes.hasMoreElements();){
          final AbstractButton shape = shapes.nextElement();

          shape.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent evt){
                   forma = shape.getName();
                   seleccion = editable = false;
                   if (selectedShape != null)
                       selectedShape.setSelected(false);
                   selectedShape = null;
                   getRootPane().repaint();
                   if (forma.equals("Seleccionar"))
                       seleccion = true;
                   if (forma.equals("Mover"))
                       editable = true;
                }
                @Override
                public void mouseEntered(MouseEvent evt){
                    repSM.setEstado(shape.getName());
                }
                @Override
                public void mouseExited(MouseEvent evt){
                    repSM.setEstado("");
                }   
             });
        }

        return panel;
    }
    
    /**
     * Crea un <code>JPanel</code> para la selección de colores.
     * Permite seleccionar un color primario y un secundario.
     * 
     * click izquierdo: Ajusta el color primario.
     * click derecho: Ajusta el color secundario.
     * 
     * @return <code>JPanel</code> de colores.
     */
    private JPanel createColorsPanel(){
      JPanel wrapper = new JPanel();
      JPanel colorsGrid = new JPanel();
      MyButtonGroup colorsGroup = new MyButtonGroup();

      // Proporties of panels
      wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.PAGE_AXIS));;
      wrapper.setPreferredSize(new Dimension(150, 200));
      wrapper.setMinimumSize(new Dimension(150, 150));
      wrapper.add(colorsGrid);
      colorsGrid.setAlignmentX(0.0F);
      colorsGrid.setBorder(BorderFactory.createTitledBorder("Colores:"));
      colorsGrid.setLayout(new GridLayout(4,3,1,1));

      // Colors button
      JToggleButton blackBtn = new JToggleButton();
      JToggleButton redBtn = new JToggleButton();
      JToggleButton blueBtn = new JToggleButton();
      JToggleButton whiteBtn = new JToggleButton();
      JToggleButton yellowBtn = new JToggleButton();
      JToggleButton greenBtn = new JToggleButton();

      // Set background color
      blackBtn.setBackground(Color.black);
      redBtn.setBackground(Color.red);
      blueBtn.setBackground(Color.blue);
      whiteBtn.setBackground(Color.white);
      yellowBtn.setBackground(Color.yellow);
      greenBtn.setBackground(Color.green);

      // Set name to each color button
      blackBtn.setName("black");
      redBtn.setName("red");
      blueBtn.setName("blue");
      whiteBtn.setName("white");
      yellowBtn.setName("yellow");
      greenBtn.setName("green");

      blackBtn.setPreferredSize(new Dimension(24,24));
      blackBtn.setSelected(true);

      // Add buttons to a Group object
      colorsGroup.add(blackBtn);
      colorsGroup.add(redBtn);
      colorsGroup.add(blueBtn);
      colorsGroup.add(whiteBtn);
      colorsGroup.add(yellowBtn);
      colorsGroup.add(greenBtn);

      // Add buttons to Grid Panel
      colorsGrid.add(blackBtn);
      colorsGrid.add(redBtn);
      colorsGrid.add(blueBtn);
      colorsGrid.add(whiteBtn);
      colorsGrid.add(yellowBtn);
      colorsGrid.add(greenBtn);
      
      
      // Selección de colores
      fgcolorBtn = new JButton();
      bgcolorBtn = new JButton();
      fgcolorBtn.setName("fgcolor");
      bgcolorBtn.setName("bgcolor");
      fgcolorBtn.setToolTipText("Color primario");
      bgcolorBtn.setToolTipText("Color secundario");
      fgcolorBtn.setBackground(fgcolor);
      bgcolorBtn.setBackground(bgcolor);
      
      colorsGrid.add(new JLabel(" FG"));
      colorsGrid.add(new JLabel(" BG"));
      colorsGrid.add(new JLabel(""));
      colorsGrid.add(fgcolorBtn);
      colorsGrid.add(bgcolorBtn);

      // Set colors listener
      final JRootPane mainFrame = this.getRootPane();
      fgcolorBtn.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            Color fgnewcol = JColorChooser.showDialog(mainFrame,
               "Seleccione color principal", fgcolor);
            
            if (fgnewcol != null){
                ((JButton) evt.getSource()).setBackground(fgnewcol);                    
                fgcolor = fgnewcol;
                if (selectedShape != null){
                    selectedShape.setfgColor(fgcolor);
                    getRootPane().repaint();
                }
            }
        }
      });

      bgcolorBtn.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            Color bgnewcol = JColorChooser.showDialog(mainFrame,
               "Seleccione color de fondo", bgcolor);
            
            if (bgnewcol != null){
                ((JButton) evt.getSource()).setBackground(bgnewcol);
                bgcolor = bgnewcol;
                
                if (selectedShape != null){
                    selectedShape.setbgColor(bgcolor);
                    getRootPane().repaint();
                }
            }
        }
      });

      for (Enumeration<AbstractButton> buttons = colorsGroup.getElements();
           buttons.hasMoreElements();){
            final AbstractButton button = buttons.nextElement();

            button.addMouseListener(new MouseAdapter(){
                  public void mousePressed(MouseEvent evt){
                     JToggleButton btn = ((JToggleButton) evt.getSource());
                     if (evt.getButton() == MouseEvent.BUTTON1){
                        fgcolor = Lienzo.toColor(btn.getName());
                        fgcolorBtn.setBackground(fgcolor);
                        if (selectedShape != null)
                            selectedShape.setfgColor(fgcolor);
                     }
                     if (evt.getButton() == MouseEvent.BUTTON3){
                        bgcolor = Lienzo.toColor(btn.getName()); 
                        bgcolorBtn.setBackground(bgcolor);
                        if (!btn.isSelected())
                            btn.setSelected(true);
                        if (selectedShape != null)
                            selectedShape.setbgColor(bgcolor);
                     }
                     getRootPane().repaint();
                  }
               });
        }
      
      return wrapper;
    }    
    /**
     * Crea un <code>JPanel</code> con herramientas para el ajuste
     * del trazo.
     * 
     * @return <code>JPanel</code> de trazo.
     */
    private JPanel createTrazoPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Atributos:"));
        panel.getBorder().getBorderInsets(panel);
        panel.setPreferredSize(new Dimension(150,140));
        Object [] trazos = {new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Continua.png")), 
                            new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Discontinua.png"))};
        Object [] rellenos = {new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Sinrelleno.png")), 
                              new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/Solido.png")),
                              new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/DegradadoHor.png")),
                              new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/DegradadoVer.png"))};
        rellenoBtn = new JComboBox(rellenos);
        trazoBtn = new JComboBox(trazos);
        trazoBtn.setPreferredSize(new Dimension(80, 20));        
        rellenoBtn.setPreferredSize(new Dimension(80, 20));        
        
        GridBagConstraints c;
        // Trazo label
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(new JLabel("Trazo"), c);

        // Trazo botón
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(trazoBtn, c);
        
        // Grosor label
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(new JLabel("Grosor"), c);
        
        // Create a spinner and formatted text field not editable.
        SpinnerNumberModel grosorModel = new SpinnerNumberModel(grosor, 1, 100, 1);
        grosorBtn = new JSpinner(grosorModel);
        JFormattedTextField tf = ((JSpinner.DefaultEditor) grosorBtn.getEditor()).getTextField();
        tf.setEditable(false);
        grosorBtn.setPreferredSize(new Dimension(50,20));
        
        // Grosor botón
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHEAST;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(grosorBtn, c);

        // Relleno label
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(new JLabel("Relleno"), c);

        // relleno botón
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 1;
        c.gridy = 2;
        panel.add(rellenoBtn, c);
        
        // Listeners
        grosorBtn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
               grosor = (int) grosorBtn.getValue();
               if (selectedShape != null){
                   selectedShape.setGrosor(grosor);
                   getRootPane().repaint();
               }
            }
         });
        
        trazoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();
                trazo = cb.getSelectedIndex();
                if (selectedShape != null)
                    selectedShape.setTrazo(trazo);
                getRootPane().repaint();
            }
        });
        
        rellenoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();
                int idx = cb.getSelectedIndex();
                switch(idx){
                    case 0:
                        relleno = false;
                        tiporelleno = SINRELLENO;
                    break;
                    case 1:
                        relleno = true;
                        tiporelleno = LISO;
                    break;
                    case 2:
                        relleno = true;
                        tiporelleno = DEGRADADO;
                        dirrelleno = HORIZONTAL;
                    break;
                    case 3:
                        relleno = true;
                        tiporelleno = DEGRADADO;
                        dirrelleno = VERTICAL;
                    break;
                }
                
                // Actualizar forma seleccionada
                if (selectedShape != null){
                    selectedShape.setFilled(relleno);
                    selectedShape.setFilledType(tiporelleno);
                    if (idx > 1)
                        selectedShape.setFilledDirection(dirrelleno);
                    getRootPane().repaint();
                }
            }
        });
        
        return panel;
    }
    
    // Getters
    /**
     * Devuelve el color primario seleccionado.
     * @return <code>Color</code> primario.
     */
    public Color fgcolorSelected(){ return this.fgcolor; }
    /**
     * Devuelve el color secundario seleccionado.
     * @return <code>Color</code> secundario.
     */
    public Color bgcolorSelected(){ return this.bgcolor; }
    /**
     * Devuelve el string asociado del <code>MyShapes</code> seleccionado.
     * @return <code>String</code> forma seleccionada.
     */
    public String formaSelected(){ return this.forma; }
    /**
     * Devuelve el grosor seleccionado.
     * @return <code>Integer</code> grosor.
     */
    public int getGrosor(){ return this.grosor; }
    /**
     * Permite consultar si la opción relleno está activada o no.
     * 
     * @return <code>boolean</code> <code>true</code> relleno
     * <code>false</code> sin relleno.
     */
    public boolean relleno(){ return this.relleno; }

    /**
     * Devuelve el tipo de relleno seleccionado.
     * <ul>
     * <li>0- Sin relleno</li>
     * <li>1- Relleno liso</li>
     * <li>2- Relleno degradado</li>
     * </ul>
     * 
     * @return <code>Integer</code> relleno.
     */
    public int getTipoRelleno(){ return this.tiporelleno; }
    /**
     * Devuelve la dirección de relleno
     * <ul>
     * <li>1- Horizontal</li>
     * <li>2- Vertical</li>
     * </ul>
     * 
     * @return <code>Integer</code> relleno.
     */
    public int getDirRelleno(){ return this.dirrelleno; }
    /**
     * Devuelve el valor del trazo seleccionado.
     * 
     * @return <code>Integer</code> trazo.
     */
    public int getTrazo(){ return this.trazo; }
    /**
     * Permite consultar si la opción editar está activada
     * o no.
     * 
     * @return <code>boolean</code> <code>true</code> editable
     * <code>false</code> no editable.
     */
    public boolean editable(){ return this.editable; }
    /**
     * Permite consultar si la opción selección está activada
     * o no.
     * 
     * @return <code>boolean</code> <code>true</code> selección
     * <code>false</code> sin selección.
     */
    public boolean seleccion(){ return this.seleccion; }
    /**
     * Devuelve el <code>MyShapes</code> seleccionado.
     * @return <code>MyShapes</code> seleccionado.
     */
    public MyShapes getSelectedShape(){ return this.selectedShape; }
    

    // Setters
    /**
     * Actualiza el valor del grosor.
     * 
     * @param grosor <code>Intenger</code> grosor.
     */
    public void setGrosor(int grosor){ this.grosor = grosor; }
    /**
     * Ajusta el <code>MyShapes</code> seleccionado con el recibido
     * como argumento.
     * 
     * @param s <code>MyShapes</code> seleccionado.
     */
    public void setSelectedShape(MyShapes s){
        this.selectedShape = s;
    }
}
