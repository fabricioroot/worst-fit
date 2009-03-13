package thread;

import gui.MainScreen;
import bean.Process;
import bean.MemoryCell;
import manager.WorstFitAlgorithm;
import manager.MemoryGenerator;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import javax.swing.JDialog;

/**
 *
 * @author Fabrício Reis
 */
public class AlgorithmStepsThread implements Runnable {

    JButton jButtonAlgorithmSteps;
    Vector<MemoryCell> finalMainMemory;
    MemoryGenerator memoryGenerator;
    Vector<Process> processesQueue;
    JPanel jPanelAnimation;
    JLabel jLabelNextStep;
    MainScreen mainScreen;
    boolean isJButtonOkClicked = false;
    JDialog jDialogNextStep;
    JButton jButtonOkNextStep;
    JLabel jLabelAtDialogNextStep;
    
    public AlgorithmStepsThread(MainScreen mainScreen, JButton jButtonAlgorithmSteps, Vector<MemoryCell> finalMainMemory, MemoryGenerator memoryGenerator, Vector<Process> processesQueue, JPanel jPanelAnimation) {
        this.mainScreen = mainScreen ;
        this.jButtonAlgorithmSteps = jButtonAlgorithmSteps;
        this.finalMainMemory = finalMainMemory;
        this.memoryGenerator = memoryGenerator;
        this.processesQueue = processesQueue;
        this.jPanelAnimation = jPanelAnimation;
    }
    
    public Vector<MemoryCell> getFinalMainMemory() {
        return finalMainMemory;
    }
    
    public JDialog getJDialogNextStep() {
        return jDialogNextStep;
    }

    public void setJDialogNextStep(JDialog jDialogNextStep) {
        this.jDialogNextStep = jDialogNextStep;
    }

    public void run() {
        this.jDialogNextStep = new JDialog();
        this.jDialogNextStep.setModalityType(ModalityType.MODELESS);
        this.jDialogNextStep.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        //this.jDialogNextStep.setAlwaysOnTop(true);
        this.jDialogNextStep.setResizable(false);
        this.jDialogNextStep.setBounds(750, 520, 231, 118);
        this.jDialogNextStep.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.jDialogNextStep.setLayout(null);

        this.jButtonOkNextStep = new JButton("OK");
        this.jButtonOkNextStep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        this.jButtonOkNextStep.setBorderPainted(true);
        this.jButtonOkNextStep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.jButtonOkNextStep.setBounds(80, 35, 60, 30);

        this.jButtonOkNextStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isJButtonOkClicked = true;
            }
        });
        
        this.jLabelAtDialogNextStep = new JLabel("Clique em 'OK' para o próximo passo");
        this.jLabelAtDialogNextStep.setBounds(5, 3, 500, 30);
        
        this.jDialogNextStep.add(this.jLabelAtDialogNextStep);
        this.jDialogNextStep.add(this.jButtonOkNextStep);

        this.jButtonAlgorithmSteps.setEnabled(false);        
        
        this.finalMainMemory = this.memoryGenerator.decreaseProcessLifeTime(this.finalMainMemory);
        this.mainScreen.paintMainMemory(this.finalMainMemory);
        
        Process process = new Process();
        process.setSize(this.processesQueue.firstElement().getSize());
        process.setLifeTime(this.processesQueue.firstElement().getLifeTime());
        process.setId(this.processesQueue.firstElement().getId());
        this.processesQueue.remove(0);
        this.mainScreen.paintProcessesQueue(this.processesQueue);
        
        this.jDialogNextStep.setTitle("INSERÇÃO DE P" + String.valueOf(process.getId()) + " ...");
        
        WorstFitAlgorithm algorithm = new WorstFitAlgorithm();

        //Semantically this object 'algorithmResult' determines if the algorithm found a solution
        //If the solution is found this object has the position where the process goes in and the leftover (Cell's size - Process's size)
        //See the corresponding method at the 'WorstFitAlgorithm' class for more information.
        Vector<Integer> algorithmResult = algorithm.toExecute_A(this.finalMainMemory, process);
        
        if(algorithmResult != null) {
        
            int orientationAxisY = 25;
            Vector<Integer> worsePositions = algorithm.findWorsePositions(this.finalMainMemory, process);
            
            int initialPositionBlocks = 0;
            int finalPositionBlocks = 0;
            
            //It finds the 'finalPositionBlocks' and walk until it reaches the first position of the Vector<Integer> 'worsePositions'
            for(int i = 0; i <= worsePositions.get(0); i++){
                finalPositionBlocks = finalPositionBlocks + this.finalMainMemory.elementAt(i).getSize();
            }
            
            JTextField block = new JTextField();
            block.setText(String.valueOf(process.getSize()));
            block.setBackground(new java.awt.Color(51, 255, 255));
            block.setForeground(new java.awt.Color(0, 0, 0));
            block.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            block.setEditable(false);
            block.setToolTipText("Tamanho de P" + String.valueOf(process.getId()) + " = " +  String.valueOf(process.getSize()));
            this.jPanelAnimation.add(block);
            
            int j = 0;
            if(finalPositionBlocks <= 15) {
                this.jDialogNextStep.setVisible(true);
                block.setBounds(20, orientationAxisY, 30, 30);
                while (j <= (finalPositionBlocks - 1)) {
                    if (this.isJButtonOkClicked) {
                        this.isJButtonOkClicked = false;
                        j++;
                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    }
                }
                this.jDialogNextStep.setVisible(false);
                j--;
                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
            }
            else {
                if((finalPositionBlocks > 15) && (finalPositionBlocks <= 30)) {
                    this.jDialogNextStep.setVisible(true);

                    // First row
                    block.setBounds(20, orientationAxisY, 30, 30);    
                    j = 0;
                    while (j <= 14) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                    }
                        
                    // Second row
                    block.setBounds(20, (orientationAxisY + 60), 30, 30);
                    j = 0;
                    while (j <= (finalPositionBlocks - 16)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                    }
                    this.jDialogNextStep.setVisible(false);
                    j--;
                    block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                }
                else {
                    if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                        this.jDialogNextStep.setVisible(true);

                        // First row
                        block.setBounds(20, orientationAxisY, 30, 30);
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }

                        // Second row
                        block.setBounds(20, (orientationAxisY + 60), 30, 30);
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        }

                        // third row
                        block.setBounds(20, orientationAxisY + 120, 30, 30);
                        j = 0;
                        while (j <= (finalPositionBlocks - 31)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20 + (j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                        j--;
                        block.setBounds(20 + (j*35), (orientationAxisY + 120), 30, 30);
                    }
                }
            }
            
            block.setText("j");
            block.setBackground(new java.awt.Color(255, 255, 102));
            block.setToolTipText("Possível posição");            
            this.jPanelAnimation.removeAll();
            this.jPanelAnimation.repaint();
            this.mainScreen.paintMainMemory(this.finalMainMemory);
            this.jPanelAnimation.add(block);
            this.jDialogNextStep.setVisible(true);
            do {
                if (this.isJButtonOkClicked) {
                    this.jDialogNextStep.setVisible(false);
                }
            } while (!this.isJButtonOkClicked);
            this.isJButtonOkClicked = false;

            if(worsePositions.size() > 1) {

                if(worsePositions.size() == 2) {
                    JTextField block1 = new JTextField();
                    block1.setText(String.valueOf(process.getSize()));
                    block1.setBackground(new java.awt.Color(51, 255, 255));
                    block1.setForeground(new java.awt.Color(0, 0, 0));
                    block1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                    block1.setEditable(false);
                    block1.setToolTipText("Tamanho de P" + String.valueOf(process.getId()) + " = " +  String.valueOf(process.getSize()));
                    this.jPanelAnimation.add(block1);
                    
                    initialPositionBlocks = finalPositionBlocks;
                    finalPositionBlocks = 0;

                    //It finds the 'finalPositionBlocks'
                    for(int i = 0; i <= worsePositions.elementAt(1); i++){
                        finalPositionBlocks = finalPositionBlocks + this.finalMainMemory.elementAt(i).getSize();
                    }

                    //The initial position to paint the blocks is on the first row...
                    if((initialPositionBlocks <= 15) && (finalPositionBlocks >= initialPositionBlocks)){
                        if(finalPositionBlocks <= 15) {
                            this.jDialogNextStep.setVisible(true);
                            j = (initialPositionBlocks - 1);
                            block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            while (j <= (finalPositionBlocks - 1)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            j--;
                            block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                        else {
                            if((finalPositionBlocks > 15) && (finalPositionBlocks <= 30)) {
                                this.jDialogNextStep.setVisible(true);

                                // First row
                                j = (initialPositionBlocks - 1);
                                block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                }

                                // Second row
                                block1.setBounds(20, (orientationAxisY + 60), 30, 30);
                                j = 0;
                                while (j <= (finalPositionBlocks - 16)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }
                                this.jDialogNextStep.setVisible(false);
                                j--;
                                block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                            else {
                                if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                                    this.jDialogNextStep.setVisible(true);

                                    // First row
                                    j = (initialPositionBlocks - 1);
                                    block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        }
                                    }
                                    
                                    // Second row
                                    block1.setBounds(20, (orientationAxisY + 60), 30, 30);
                                    j = 0;
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }
                                    }
                                    
                                    // third row
                                    block1.setBounds(20, (orientationAxisY + 120), 30, 30);
                                    j = 0;
                                    while (j <= (finalPositionBlocks - 31)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    }
                                    this.jDialogNextStep.setVisible(false);
                                    j--;
                                    block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }
                        }
                    }
                    else {
                        if((initialPositionBlocks <= 15) && (finalPositionBlocks < initialPositionBlocks)){
                            this.jDialogNextStep.setVisible(true);

                            // First row
                            j = (initialPositionBlocks - 1);
                            block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }
                            
                            // Second row
                            block1.setBounds(20, (orientationAxisY + 60), 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }
                            
                            // third row
                            block1.setBounds(20, (orientationAxisY + 120), 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }

                            //Going back... First Row
                            block1.setBounds(20, orientationAxisY, 30, 30);
                            j = 0;
                            while (j <= (finalPositionBlocks - 1)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            j--;
                            block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                    }

                    //The initial position to paint the blocks is on the second row
                    if((initialPositionBlocks > 15) && (initialPositionBlocks <= 30) && (finalPositionBlocks >= initialPositionBlocks)){
                        if(finalPositionBlocks <= 30) {
                            this.jDialogNextStep.setVisible(true);
                            // Second row
                            j = (initialPositionBlocks - 16);
                            block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            while (j <= (finalPositionBlocks - 16)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            j--;
                            block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                        else {
                            if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                                this.jDialogNextStep.setVisible(true);
                                
                                // Second row
                                j = (initialPositionBlocks - 16);
                                block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }
                                
                                // third row
                                block1.setBounds(20, (orientationAxisY + 120), 30, 30);
                                j = 0;
                                while (j <= (finalPositionBlocks - 31)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }
                                this.jDialogNextStep.setVisible(false);
                                j--;
                                block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }
                    }
                    else {
                        if((initialPositionBlocks > 15) && (initialPositionBlocks <= 30) && (finalPositionBlocks < initialPositionBlocks)){
                            if(finalPositionBlocks <= 15) {
                                this.jDialogNextStep.setVisible(true);
                                
                                // Second row
                                j = (initialPositionBlocks - 16);
                                block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }
                                
                                // third row
                                block1.setBounds(20, (orientationAxisY + 120), 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }
                                
                                //Going back... First Row
                                block1.setBounds(20, orientationAxisY, 30, 30);
                                j = 0;
                                while (j <= (finalPositionBlocks - 1)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                }
                                this.jDialogNextStep.setVisible(false);
                                j--;
                                block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                            else {
                                if(finalPositionBlocks > 15) {
                                    this.jDialogNextStep.setVisible(true);
                                    
                                    // Second row
                                    j = (initialPositionBlocks - 16);
                                    block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }
                                    }

                                    // third row
                                    block1.setBounds(20, (orientationAxisY + 120), 30, 30);
                                    j = 0;
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    }
                                    
                                    //Going back... First Row
                                    block1.setBounds(20, orientationAxisY, 30, 30);
                                    j = 0;
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        }
                                    }
                                    
                                    //Going back... Second Row
                                    block1.setBounds(20, (orientationAxisY + 60), 30, 30);
                                    j = 0;
                                    while (j <= (finalPositionBlocks - 16)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }
                                    }
                                    this.jDialogNextStep.setVisible(false);
                                    j--;
                                    block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }
                        }
                    }

                    //The initial position to paint the blocks is on the third row
                    if((initialPositionBlocks > 30) && (initialPositionBlocks <= 45) && (finalPositionBlocks >= initialPositionBlocks)){
                        this.jDialogNextStep.setVisible(true);
                        j = (initialPositionBlocks - 31);
                        block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                        while (j <= (finalPositionBlocks - 31)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                        j--;
                        block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                    }
                    else {
                        if((initialPositionBlocks > 30) && (initialPositionBlocks <= 45) && (finalPositionBlocks < initialPositionBlocks)){
                            if(finalPositionBlocks <= 15) {
                                this.jDialogNextStep.setVisible(true);
                                
                                // Third Row
                                j = (initialPositionBlocks - 31);
                                block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }
                                
                                // Going back ... First row
                                block1.setBounds(20, orientationAxisY, 30, 30);
                                j = 0;
                                while (j <= (finalPositionBlocks - 1)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                }
                                this.jDialogNextStep.setVisible(false);
                                j--;
                                block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                            else {
                                if((finalPositionBlocks > 15) && (finalPositionBlocks <= 30)){
                                    this.jDialogNextStep.setVisible(true);

                                    // Third row
                                    j = (initialPositionBlocks - 31);
                                    block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    }
                                    
                                    // Going back... First row
                                    block1.setBounds(20, orientationAxisY, 30, 30);
                                    j = 0;
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        }
                                    }
                                    
                                    // Going back... Second row
                                    block1.setBounds(20, (orientationAxisY + 60), 30, 30);
                                    j = 0;
                                    while (j <= (finalPositionBlocks - 16)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }
                                    }
                                    this.jDialogNextStep.setVisible(false);
                                    j--;
                                    block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                                else {
                                    if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                                        this.jDialogNextStep.setVisible(true);
                                        
                                        // Third row
                                        j = (initialPositionBlocks - 31);
                                        block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                            }
                                        }

                                        // Going back... First row
                                        block1.setBounds(20, orientationAxisY, 30, 30);
                                        j = 0;
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block1.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                            }                                        
                                        }
                                        
                                        // Going back... Second row
                                        block1.setBounds(20, (orientationAxisY + 60), 30, 30);
                                        j = 0;
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block1.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                            }                                        
                                        }

                                        // Going back... Third row
                                        block1.setBounds(20, (orientationAxisY + 120), 30, 30);
                                        j = 0;
                                        while (j <= (finalPositionBlocks - 31)) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                            }
                                        }
                                        this.jDialogNextStep.setVisible(false);
                                        j--;
                                        block1.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }
                            }
                        }
                    }
                    block1.setText("j");
                    block1.setBackground(new java.awt.Color(255, 255, 102));
                    block1.setToolTipText("Possível Posição");
                    this.jPanelAnimation.removeAll();
                    this.jPanelAnimation.repaint();
                    this.mainScreen.paintMainMemory(this.finalMainMemory);
                    this.jPanelAnimation.add(block1);
                    this.jDialogNextStep.setVisible(true);
                    do {
                        if (this.isJButtonOkClicked) {
                            this.jDialogNextStep.setVisible(false);
                        }
                    } while (!this.isJButtonOkClicked);
                    this.isJButtonOkClicked = false;
                }

                // 'worsePositions.size() > 2', thus there are more than 2 possible "worse positions"
                else {
                    int t = 0;
                    do {
                        initialPositionBlocks = finalPositionBlocks;
                        finalPositionBlocks = 0;

                        //It finds the 'finalPositionBlocks'
                        for(int i = 0; i <= worsePositions.elementAt(t+1); i++){
                            finalPositionBlocks = finalPositionBlocks + this.finalMainMemory.elementAt(i).getSize();
                        }

                        JTextField block2 = new JTextField();
                        block2.setText(String.valueOf(process.getSize()));
                        block2.setBackground(new java.awt.Color(51, 255, 255));
                        block2.setForeground(new java.awt.Color(0, 0, 0));
                        block2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                        block2.setEditable(false);
                        block2.setToolTipText("Tamanho de P" + String.valueOf(process.getId()) + " = " +  String.valueOf(process.getSize()));
                        this.jPanelAnimation.add(block2);

                        //The initial position to paint the blocks is on the first row...
                        if((initialPositionBlocks <= 15) && (finalPositionBlocks >= initialPositionBlocks)){
                            if(finalPositionBlocks <= 15) {
                                this.jDialogNextStep.setVisible(true);
                                j = (initialPositionBlocks - 1);
                                block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                while (j <= (finalPositionBlocks - 1)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                } 
                                this.jDialogNextStep.setVisible(false);
                                j--;
                                block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                            else {
                                if((finalPositionBlocks > 15) && (finalPositionBlocks <= 30)) {
                                    this.jDialogNextStep.setVisible(true);

                                    // First row
                                    j = (initialPositionBlocks - 1);
                                    block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        }
                                    } 

                                    // Second row
                                    block2.setBounds(20, (orientationAxisY + 60), 30, 30);
                                    j = 0;
                                    while (j <= (finalPositionBlocks - 16)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }
                                    } 
                                    this.jDialogNextStep.setVisible(false);
                                    j--;
                                    block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                                else {
                                    if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                                        this.jDialogNextStep.setVisible(true);

                                        // First row
                                        j = (initialPositionBlocks - 1);
                                        block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                            }
                                        } 
                                        
                                        // Second row
                                        block2.setBounds(20, (orientationAxisY + 60), 30, 30);
                                        j = 0;
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                            }
                                        } 
                                        
                                        // third row
                                        block2.setBounds(20, (orientationAxisY + 120), 30, 30);
                                        j = 0;
                                        while (j <= (finalPositionBlocks - 31)) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                            }
                                        } 
                                        this.jDialogNextStep.setVisible(false);
                                        j--;
                                        block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }
                            }
                        }
                        else {
                            if((initialPositionBlocks <= 15) && (finalPositionBlocks < initialPositionBlocks)){
                                this.jDialogNextStep.setVisible(true);

                                // First row
                                j = (initialPositionBlocks - 1);
                                block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                } 

                                // Second row
                                block2.setBounds(20, (orientationAxisY + 60), 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                } 

                                // third row
                                block2.setBounds(20, (orientationAxisY + 120), 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                } 
                                
                                //Going back... First Row
                                block2.setBounds(20, orientationAxisY, 30, 30);
                                j = 0;
                                while (j <= (finalPositionBlocks - 1)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                } 
                                this.jDialogNextStep.setVisible(false);
                                j--;
                                block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }

                        //The initial position to paint the blocks is on the second row
                        if((initialPositionBlocks > 15) && (initialPositionBlocks <= 30) && (finalPositionBlocks >= initialPositionBlocks)){
                            if(finalPositionBlocks <= 30) {
                                this.jDialogNextStep.setVisible(true);

                                // Second row
                                j = (initialPositionBlocks - 16);
                                block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                while (j <= (finalPositionBlocks - 16)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                } 
                                this.jDialogNextStep.setVisible(false);
                                j--;
                                block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                            else {
                                if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                                    this.jDialogNextStep.setVisible(true);

                                    // Second row
                                    j = (initialPositionBlocks - 16);
                                    block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }
                                    } 
                                    
                                    // third row
                                    block2.setBounds(20, (orientationAxisY + 120), 30, 30);
                                    j = 0;
                                    while (j <= (finalPositionBlocks - 31)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    } 
                                    this.jDialogNextStep.setVisible(false);
                                    j--;
                                    block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }
                        }
                        else {
                            if((initialPositionBlocks > 15) && (initialPositionBlocks <= 30) && (finalPositionBlocks < initialPositionBlocks)){
                                if(finalPositionBlocks <= 15) {
                                    this.jDialogNextStep.setVisible(true);

                                    // Second row
                                    j = (initialPositionBlocks - 16);
                                    block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }
                                    } 
                                    
                                    // third row
                                    block2.setBounds(20, (orientationAxisY + 120), 30, 30);
                                    j = 0;
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    } 

                                    //Going back... First Row
                                    block2.setBounds(20, orientationAxisY, 30, 30);
                                    j = 0;
                                    while (j <= (finalPositionBlocks - 1)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        }
                                    } 
                                    this.jDialogNextStep.setVisible(false);
                                    j--;
                                    block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }                    
                                else {
                                    if(finalPositionBlocks > 15) {
                                        this.jDialogNextStep.setVisible(true);

                                        // Second row
                                        j = (initialPositionBlocks - 16);
                                        block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                            }
                                        } 

                                        // third row
                                        block2.setBounds(20, (orientationAxisY + 120), 30, 30);
                                        j = 0;
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                            }
                                        } 

                                        //Going back... First Row
                                        block2.setBounds(20, orientationAxisY, 30, 30);
                                        j = 0;
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                            }
                                        } 

                                        //Going back... Second Row
                                        block2.setBounds(20, (orientationAxisY + 60), 30, 30);
                                        j = 0;
                                        while (j <= (finalPositionBlocks - 16)) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                            }
                                        } 
                                        this.jDialogNextStep.setVisible(false);
                                        j--;
                                        block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }
                            }
                        }

                        //The initial position to paint the blocks is on the third row
                        if((initialPositionBlocks > 30) && (initialPositionBlocks <= 45) && (finalPositionBlocks >= initialPositionBlocks)){
                            this.jDialogNextStep.setVisible(true);
                            j = (initialPositionBlocks - 31);
                            block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            while (j <= (finalPositionBlocks - 31)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            } 
                            this.jDialogNextStep.setVisible(false);
                            j--;
                            block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                        }
                        else {
                            if((initialPositionBlocks > 30) && (initialPositionBlocks <= 45) && (finalPositionBlocks < initialPositionBlocks)){
                                if(finalPositionBlocks <= 15) {
                                    this.jDialogNextStep.setVisible(true);
                                
                                    // Third Row
                                    j = (initialPositionBlocks - 31);
                                    block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    } 

                                    // Going back ... First row
                                    block2.setBounds(20, orientationAxisY, 30, 30);
                                    j = 0;
                                    while (j <= (finalPositionBlocks - 1)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        }
                                    } 
                                    this.jDialogNextStep.setVisible(false);
                                    j--;
                                    block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                                else {
                                    if((finalPositionBlocks > 15) && (finalPositionBlocks <= 30)){
                                        this.jDialogNextStep.setVisible(true);

                                        // Third row
                                        j = (initialPositionBlocks - 31);
                                        block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                            }
                                        } 

                                        // Going back... First row
                                        block2.setBounds(20, orientationAxisY, 30, 30);
                                        j = 0;
                                        while (j <= 14) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                            }
                                        } 

                                        // Going back... Second row
                                        block2.setBounds(20, (orientationAxisY + 60), 30, 30);
                                        j = 0;
                                        while (j <= (finalPositionBlocks - 16)) {
                                            if (this.isJButtonOkClicked) {
                                                this.isJButtonOkClicked = false;
                                                j++;
                                                block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                            }
                                        }
                                        this.jDialogNextStep.setVisible(false);
                                        j--;
                                        block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                    else {
                                        if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                                            this.jDialogNextStep.setVisible(true);
                                        
                                            // Third row
                                            j = (initialPositionBlocks - 31);
                                            block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                            while (j <= 14) {
                                                if (this.isJButtonOkClicked) {
                                                    this.isJButtonOkClicked = false;
                                                    j++;
                                                    block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                                }
                                            } 

                                            // Going back... First row
                                            block2.setBounds(20, orientationAxisY, 30, 30);
                                            j = 0;
                                            while (j <= 14) {
                                                if (this.isJButtonOkClicked) {
                                                    this.isJButtonOkClicked = false;
                                                    j++;
                                                    block2.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                                }
                                            } 

                                            // Going back... Second row
                                            block2.setBounds(20, (orientationAxisY + 60), 30, 30);
                                            j = 0;
                                            while (j <= 14) {
                                                if (this.isJButtonOkClicked) {
                                                    this.isJButtonOkClicked = false;
                                                    j++;
                                                    block2.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                                }
                                            } 

                                            // Going back... Third row
                                            block2.setBounds(20, (orientationAxisY + 120), 30, 30);
                                            j = 0;
                                            while (j <= (finalPositionBlocks - 31)) {
                                                if (this.isJButtonOkClicked) {
                                                    this.isJButtonOkClicked = false;
                                                    j++;
                                                    block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                                }
                                            }
                                            this.jDialogNextStep.setVisible(false);
                                            j--;
                                            block2.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    }
                                }
                            }
                        }
                        block2.setText("j");
                        block2.setBackground(new java.awt.Color(255, 255, 102));
                        block2.setToolTipText("Possível Posição");
                        this.jPanelAnimation.removeAll();
                        this.jPanelAnimation.repaint();
                        this.mainScreen.paintMainMemory(this.finalMainMemory);
                        this.jPanelAnimation.add(block2);
                        this.jDialogNextStep.setVisible(true);
                        do {
                            if (this.isJButtonOkClicked) {
                                this.jDialogNextStep.setVisible(false);
                            }
                        } while (!this.isJButtonOkClicked);
                        this.isJButtonOkClicked = false;
                        t++;
                    } while (t <= worsePositions.size() - 2);
                }
            }
            
            initialPositionBlocks = finalPositionBlocks;
            finalPositionBlocks = 45;

            JTextField block3 = new JTextField();
            block3.setText(String.valueOf(process.getSize()));
            block3.setBackground(new java.awt.Color(51, 255, 255));
            block3.setForeground(new java.awt.Color(0, 0, 0));
            block3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            block3.setEditable(false);
            block3.setToolTipText("Tamanho de P" + String.valueOf(process.getId()) + " = " +  String.valueOf(process.getSize()));
            this.jPanelAnimation.add(block3);

            //The initial position to paint the blocks is on the first row...
            if((initialPositionBlocks <= 15) && (finalPositionBlocks >= initialPositionBlocks)){
                if(finalPositionBlocks <= 15) {
                    this.jDialogNextStep.setVisible(true);
                    j = (initialPositionBlocks - 1);
                    block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    while (j <= (finalPositionBlocks - 1)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                    }
                    this.jDialogNextStep.setVisible(false);
                    j--;
                    block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                }
                else {
                    if((finalPositionBlocks > 15) && (finalPositionBlocks <= 30)) {
                        this.jDialogNextStep.setVisible(true);

                        // First row
                        j = (initialPositionBlocks - 1);
                        block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        } 

                        // Second row
                        block3.setBounds(20, (orientationAxisY + 60), 30, 30);
                        j = 0;
                        while (j <= (finalPositionBlocks - 16)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                        j--;
                        block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                    }
                    else {
                        if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                            this.jDialogNextStep.setVisible(true);

                            // First row
                            j = (initialPositionBlocks - 1);
                            block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            while (j <= 14) { 
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            } 

                            // Second row
                            block3.setBounds(20, (orientationAxisY + 60), 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            } 

                            // third row
                            block3.setBounds(20, (orientationAxisY + 120), 30, 30);
                            j = 0;
                            while (j <= (finalPositionBlocks - 31)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            j--;
                            block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                        }
                    }
                }
            }
            else {
                if((initialPositionBlocks <= 15) && (finalPositionBlocks < initialPositionBlocks)){
                    this.jDialogNextStep.setVisible(true);

                    // First row
                    j = (initialPositionBlocks - 1);
                    block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    while (j <= 14) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                    } 

                    // Second row
                    block3.setBounds(20, (orientationAxisY + 60), 30, 30);
                    j = 0;
                    while (j <= 14) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                    } 

                    // third row
                    block3.setBounds(20, (orientationAxisY + 120), 30, 30);
                    j = 0;
                    while (j <= 14) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                        }
                    } 

                    //Going back... First Row
                    block3.setBounds(20, orientationAxisY, 30, 30);
                    j = 0;
                    while (j <= (finalPositionBlocks - 1)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                    }
                    this.jDialogNextStep.setVisible(false);
                    j--;
                    block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                }
            }

            //The initial position to paint the blocks is on the second row
            if((initialPositionBlocks > 15) && (initialPositionBlocks <= 30) && (finalPositionBlocks >= initialPositionBlocks)){
                if(finalPositionBlocks <= 30) {
                    this.jDialogNextStep.setVisible(true);
                    // Second row
                    j = (initialPositionBlocks - 16);
                    block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                    while (j <= (finalPositionBlocks - 16)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                    }
                    this.jDialogNextStep.setVisible(false);
                    j--;
                    block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                }
                else {
                    if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                        this.jDialogNextStep.setVisible(true);

                        // Second row
                        j = (initialPositionBlocks - 16);
                        block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        } 

                        // third row
                        block3.setBounds(20, (orientationAxisY + 120), 30, 30);
                        j = 0;
                        while (j <= (finalPositionBlocks - 31)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                        j--;
                        block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                    }
                }
            }
            else {
                if((initialPositionBlocks > 15) && (initialPositionBlocks <= 30) && (finalPositionBlocks < initialPositionBlocks)){
                    if(finalPositionBlocks <= 15) {
                        this.jDialogNextStep.setVisible(true);

                        // Second row
                        j = (initialPositionBlocks - 16);
                        block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        } 

                        // third row
                        block3.setBounds(20, (orientationAxisY + 120), 30, 30);
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            }
                        } 

                        //Going back... First Row
                        block3.setBounds(20, orientationAxisY, 30, 30);
                        j = 0;
                        while (j <= (finalPositionBlocks - 1)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                        j--;
                        block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    }
                    else {
                        if(finalPositionBlocks > 15) {
                            this.jDialogNextStep.setVisible(true);

                            // Second row
                            j = (initialPositionBlocks - 16);
                            block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            } 

                            // third row
                            block3.setBounds(20, (orientationAxisY + 120), 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            } 

                            //Going back... First Row
                            block3.setBounds(20, orientationAxisY, 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }

                            //Going back... Second Row
                            block3.setBounds(20, (orientationAxisY + 60), 30, 30);
                            j = 0;
                            while (j <= (finalPositionBlocks - 16)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            j--;
                            block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                    }
                }
            }

            //The initial position to paint the blocks is on the third row
            if((initialPositionBlocks > 30) && (initialPositionBlocks <= 45) && (finalPositionBlocks >= initialPositionBlocks)){
                this.jDialogNextStep.setVisible(true);
                j = (initialPositionBlocks - 31);
                block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                while (j <= (finalPositionBlocks - 31)) {
                    if (this.isJButtonOkClicked) {
                        this.isJButtonOkClicked = false;
                        j++;
                        block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                    }
                }
                this.jDialogNextStep.setVisible(false);
                j--;
                block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
            }
            else {
                if((initialPositionBlocks > 30) && (initialPositionBlocks <= 45) && (finalPositionBlocks < initialPositionBlocks)){
                    if(finalPositionBlocks <= 15) {
                        this.jDialogNextStep.setVisible(true);
                                
                        // Third Row
                        j = (initialPositionBlocks - 31);
                        block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }

                        // Going back ... First row
                        block3.setBounds(20, orientationAxisY, 30, 30);
                        j = 0;
                        while (j <= (finalPositionBlocks - 1)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                        j--;
                        block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    }
                    else {
                        if((finalPositionBlocks > 15) && (finalPositionBlocks <= 30)){
                            this.jDialogNextStep.setVisible(true);

                            // Third row
                            j = (initialPositionBlocks - 31);
                            block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }

                            // Going back... First row
                            block3.setBounds(20, orientationAxisY, 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }

                            // Going back... Second row
                            block3.setBounds(20, (orientationAxisY + 60), 30, 30);
                            j = 0;
                            while (j <= (finalPositionBlocks - 16)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            j--;
                            block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                        else {
                            if((finalPositionBlocks > 30) && (finalPositionBlocks <= 45)){
                                this.jDialogNextStep.setVisible(true);
                                        
                                // Third row
                                j = (initialPositionBlocks - 31);
                                block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }

                                // Going back... First row
                                block3.setBounds(20, orientationAxisY, 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block3.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                }

                                // Going back... Second row
                                block3.setBounds(20, (orientationAxisY + 60), 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block3.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }

                                // Going back... Third row
                                block3.setBounds(20, (orientationAxisY + 120), 30, 30);
                                j = 0;
                                while (j <= (finalPositionBlocks - 31)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block3.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }
                                this.jDialogNextStep.setVisible(false);
                            }
                        }
                    }
                }
            }
            
            this.finalMainMemory = algorithm.toExecute_B(this.finalMainMemory, process);
            this.jPanelAnimation.removeAll();
            this.jPanelAnimation.repaint();
            this.mainScreen.paintMainMemory(this.finalMainMemory);

            if(this.processesQueue.size() > 0) {
                this.jButtonAlgorithmSteps.setEnabled(true);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Não há espaço contínuo na memória grande suficiente para armazenar o processo!\n" +
                            "Por isso, ele será inserido novamente na fila (última posição).", "ATENÇÃO", JOptionPane.WARNING_MESSAGE);
            this.processesQueue.add(process);
            this.mainScreen.paintProcessesQueue(this.processesQueue);
            this.jButtonAlgorithmSteps.setEnabled(true);
        }
    }
}