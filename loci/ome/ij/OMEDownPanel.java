import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import org.openmicroscopy.ds.dto.*;

/**
 * OMEDownPanel is the class that handles
 * the window used to enter image search
 * criteria.
 * @author Philip Huettl pmhuettl@wisc.edu
 * @author Melissa Linkert linkert at cs.wisc.edu
 */

public class OMEDownPanel implements ActionListener {
  //Fields
  private JButton search, cancel;
  private JDialog dia;
  private JComboBox cproj, ctype, cowner;
  private JTextField id, name;
  public boolean cancelPlugin;
  
  //Constructor, sets up the dialog box
  public OMEDownPanel(Frame frame, Project[] projects, String[][] owners) {
    cancelPlugin = false;
    //creates the dialog box for searching for images
    dia = new JDialog(frame, "OME Download Search", true);
    JPanel pane = new JPanel(), panel = new JPanel();
    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    //labels
    JLabel enter = new JLabel("Specify search criteria");
    enter.setAlignmentX(Component.CENTER_ALIGNMENT);
    JLabel enter1 = new JLabel("of the image you wish to");
    enter1.setAlignmentX(Component.CENTER_ALIGNMENT);
    JLabel enter2 = new JLabel("upload into ImageJ.");
    enter2.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.setMinimumSize(new Dimension(500, 500));
    pane.add(panel);
    pane.setMinimumSize(new Dimension(500, 500));
    panel.add(enter);
    panel.add(enter1);
    panel.add(enter2);
    pane.setBorder(new EmptyBorder(5,5,5,5));
    //panels
    JPanel paneL = new JPanel();
    JPanel paneR = new JPanel();
    JPanel paneInfo = new JPanel();
    JPanel paneButtons = new JPanel();
    paneL.setLayout(new BoxLayout(paneL, BoxLayout.Y_AXIS));
    paneR.setLayout(new BoxLayout(paneR, BoxLayout.Y_AXIS));
    paneInfo.setLayout(new BoxLayout(paneInfo, BoxLayout.X_AXIS));
    paneInfo.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED),
      new EmptyBorder(5,5,5,5)));
    paneButtons.setLayout(new BoxLayout(paneButtons, BoxLayout.X_AXIS));
    paneButtons.setBorder(new EmptyBorder(5,5,5,5));
    paneInfo.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    paneButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    pane.add(paneInfo);
    paneInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
    paneButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
    pane.add(paneButtons);
    paneInfo.add(paneL);
    paneInfo.add(paneR);
    dia.setContentPane(pane);
    //borders
    EmptyBorder bordCombo = new EmptyBorder(1,0,4,0);
    EmptyBorder bordText = new EmptyBorder(3,0,2,0);
    //more components
    JLabel lproj = new JLabel("Project:");
    lproj.setBorder(bordCombo);
    lproj.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
    paneL.add(lproj);
    String[] projectS = new String[projects.length+1];
    projectS[0]="All";
    for (int i=0; i<projects.length; i++) {
      projectS[i+1] = projects[i].getName();
    }
    cproj = new JComboBox(projectS);
    cproj.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    cproj.setMinimumSize(new Dimension(50, 10));
    paneR.add(cproj);
    JLabel lowner = new JLabel("Owner:");
    lowner.setBorder(bordCombo);
    lowner.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
    paneL.add(lowner);
    String[] ownerS = new String[owners[0].length+1];
    ownerS[0] = "All";
    for (int i=0; i<owners[0].length; i++) {
      ownerS[i+1] = owners[0][i] + " "+owners[1][i];
    }
    cowner = new JComboBox(ownerS);
    cowner.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    cowner.setMinimumSize(new Dimension(50, 10));
    paneR.add(cowner);
  
    id = new JTextField(5);
    id.setMinimumSize(new Dimension(50, 10));
    id.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    JLabel label = new JLabel("Image ID:");
    label.setBorder(bordText);
    label.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
    paneL.add(label);
    paneL.setBorder(new EmptyBorder(5,5,5,5));
    paneR.add(id);
    
    name = new JTextField(10);
    name.setMinimumSize(new Dimension(50, 10));
    name.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    paneR.add(name);
    JLabel lname = new JLabel("Image Name:");
    lname.setBorder(bordText);
    lname.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
    paneL.add(lname);
    
    search = new JButton("Search");
    cancel = new JButton("Cancel");
    JButton resets = new JButton("Reset");
    resets.setActionCommand("reset");
    search.setActionCommand("search");
    cancel.setActionCommand("cancel");
    paneButtons.add(search);
    paneButtons.add(cancel);
    paneButtons.add(resets);
    search.addActionListener(this);
    cancel.addActionListener(this);
    resets.addActionListener(this);
    
    dia.pack();
    OMESidePanel.centerWindow(frame, dia);
  }
  
  /** shows and retrieves info from the DownPanel */
  public Object[] search() {
    cancelPlugin = true;
    dia.show();
    if (cancelPlugin) return null;
    //checks and puts results into an array
    Object[] results = new Object[4];
    results[0] = cproj.getSelectedItem();
    results[1] = cowner.getSelectedItem();
    try {
       String s = id.getText();
       if (s.equals("")) {
         s = "0";
       }
       results[2] = Integer.decode(s);
    }
    catch (NumberFormatException n) {
      error((Frame)dia.getOwner(),"The image ID field is not valid.", 
        "Input Error");
      return search();
    }
    catch (NullPointerException e) {
      results[2] = new Integer(0);
    }
    try {
       results[3] = name.getText();
    }
    catch (NullPointerException e) {
      results[3] = "";
    }
    return results;
  }
  
  /** implements the ActionListener actionPerformed method */
  public void actionPerformed(ActionEvent e){
    cancelPlugin = false;
    if ("search".equals(e.getActionCommand())) {
      dia.hide();
    }
    else if ("reset".equals(e.getActionCommand()) ) {
      reset();
    }
    else{
      cancelPlugin = true;
      dia.dispose();
    }
  }
  
  /** produces an error notification popup with the inputted text */
  public static void error(Frame frame, String s, String x){
    JOptionPane.showMessageDialog(frame,s,x,JOptionPane.ERROR_MESSAGE);
  }
  
  /** Resets the options of the search dialog box */
  public void reset(){
    try {
      cproj.setSelectedIndex(0);
      cowner.setSelectedIndex(0);
      ctype.setSelectedIndex(0);
      id.setText(null);
      name.setText(null);
    }
    catch(NullPointerException n) { }
  }
}
