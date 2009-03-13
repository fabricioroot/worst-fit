package manager;

import java.util.Vector;
import bean.MemoryCell;
import bean.Process;
import java.util.Arrays;


/**
 *
 * @author Fabricio Reis
 */
public class WorstFitAlgorithm {

    public WorstFitAlgorithm() {
    }

    /* This method implements the algorithm WORST-FIT of memory management, which
     * looks for the worst free space to put the process.
     * It receives two parameters: memory (Vector<MemoryCell>) and process (Process).
     * Its out is a vector object that contains the position (starts counting from 0) where the process can be put
     * and a number (leftover) that is how much space rested.
     * If the out is null, means none big enough free space has been found in the memory to put the process.
     */
    
     public Vector<Integer> toExecute_A(Vector<MemoryCell> memory, Process process) {
        int leftover = -1; //This variable stores the leftover (Cell's size - Process's size).
        int positionWorstFit = -1; //This variable stores the position of the cell which has the best size to put the process
        int aux = 0; //This variable is used to find the best fit cell
        Vector<Integer> out = null; //This is the method's return. A vector where its first position stores the position where the process was put
                                    // and its second position stores the leftover (Cell's size - Process's size).
        MemoryCell cell = new MemoryCell();
        
        for(int i = 0; i <= (memory.size() - 1); i++) {
            cell = memory.elementAt(i);
            if((process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                if( aux < (cell.getSize() - process.getSize()) ) {
                    aux = cell.getSize() - process.getSize();
                    positionWorstFit = i;
                    leftover = cell.getSize() - process.getSize();
                }
            }
        }
        
        //If the algorithm doesn't find a space bigger than the process' size, it'll look for a space with the same size
        if(positionWorstFit == -1) {
            for(int i = 0; i <= (memory.size() - 1); i++) {
                cell = memory.elementAt(i);
                if((process.getSize() == cell.getSize()) && (cell.isIsFree())) {
                    positionWorstFit = i;
                    i = memory.size();
                }
            }    
        }
        
        //If positionWorstFit's value is NOT -1, that means the algorithm found a valid position to put the process
        if (positionWorstFit != -1) {
            out = new Vector<Integer>();
            out.add(positionWorstFit);
            out.add(leftover);
        }        
        return out;
    }
     
    /* This method implements the algorithm WORST-FIT of memory management, which
     * looks for the worst free space to put the process.
     * It receives two parameters: memory (Vector<MemoryCell>) and process (Process).
     * Its out is the modified memory (includes the process in the memory).
     */
     
     public Vector<MemoryCell> toExecute_B(Vector<MemoryCell> memory, Process process) {
        int leftover = -1; //This variable stores the leftover (Cell's size - Process's size).
        int positionWorstFit = -1; //This variable stores the position of the cell which has the best size to put the process
        int aux = 0; //This variable is used to find the best fit cell        
        Vector<MemoryCell> out = memory; //This is the method's return. The new memory!
        MemoryCell cell = new MemoryCell();

        
        for(int i = 0; i <= (memory.size() - 1); i++) {
            cell = memory.elementAt(i);
            if( (process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                if( aux < (cell.getSize() - process.getSize()) ) {
                    aux = cell.getSize() - process.getSize();
                    positionWorstFit = i;
                    leftover = cell.getSize() - process.getSize();
                }
            }
        }
        
        //If the algorithm doesn't find a space bigger than the process' size, it'll look for a space with the same size
        if(positionWorstFit == -1) {
            for(int i = 0; i <= (memory.size() - 1); i++) {
                cell = memory.elementAt(i);
                if((process.getSize() == cell.getSize()) && (cell.isIsFree())) {
                    positionWorstFit = i;
                    i = memory.size();
                }
            }    
        }
        
        if(positionWorstFit != -1) {
            MemoryCell auxCell = new MemoryCell(false, process, process.getSize());
            out.set(positionWorstFit, auxCell);
            if(leftover > 0){
                auxCell = new MemoryCell(true, null, leftover);
                out.add(positionWorstFit + 1, auxCell);    
            }
        }
        return out;
    }
     
     /*
     * This method finds and stores in a Vector<Integer>, the positions which the process (parameter) fits in the memory.
     * The method stores the worse positions only!
     * It starts looking for these positions from the first position of the memory and goes until its end.
     */
    public Vector<Integer> findWorsePositions(Vector<MemoryCell> memory, Process process) {
        Vector<Integer> positions = new Vector<Integer>();
        Vector<Integer> auxSizes = new Vector<Integer>();
        MemoryCell cell = new MemoryCell();
        int aux = 0;
        
        for(int i = 0; i <= (memory.size() - 1); i++) {
            cell = memory.elementAt(i);
            if( (process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                if( aux <= (cell.getSize() - process.getSize()) ) {
                    aux = cell.getSize() - process.getSize();
                    if(!auxSizes.contains(aux)) {
                        positions.add(i);
                        auxSizes.add(aux);    
                    }                    
                }
            }
        }
        return positions;
    }
}
