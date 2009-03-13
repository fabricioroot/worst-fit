package manager;

import bean.MemoryCell;
import bean.Process;
import java.util.Vector;

/**
 *
 * @author Fabricio Reis
 */
public class MemoryGenerator {

    public MemoryGenerator() {
    }    
    
    /*
     * This method (createsRandomMemory) generates a vector of 'MemoryCell'
     * which represents a computer main memory.
     * As parameters, it receives 'size' which defines the memory's size
     * and 'freeSpaces' which defines how many free spaces this memory will have.
     * This method returns a Vector<MemoryCell> with that template:
     * | S1 F | S2 F | S3 F | ... | Sn F |
     * Where: | Sn F P | = memory cell 
     *        Sx = a random number (from 1 to 5) that represents the size of the cell
     *        F = a boolean that represents if the cell's free or not
     *        P = a Process class object (if F = true -> P = process, else P = null)
     * The sum of S's is equals 'size'. Thus (S1 + S2 + S3 + ... + Sn == 'size').
     */
    
    public Vector<MemoryCell> createsRandomMemory(int size, int freeSpaces){
        Vector<MemoryCell> memory = new Vector<MemoryCell>();
        MemoryCell lastCell = new MemoryCell();
        int randomNumber = 0;
        int valueSize = 0;  //This variable defines the cell's size
        int aux = 0;
        int auxSize = size;
        int processId = 0; //This variable is used to name the process of a cell that's free
        
        while(auxSize > 0){        
            MemoryCell cell = new MemoryCell();
            //That's defining the cell's size
            randomNumber = (int) (1 + Math.random()*5);
            valueSize = randomNumber;
            cell.setSize(valueSize);
            cell.setIsFree(false);
            //End
            
            //This bit limits the quantity of 'freeSpaces' in the memory
            if(aux < freeSpaces){
                // That's defining if the cell's free or not
                randomNumber = (int) (1 + Math.random()*10);
                if (randomNumber <= 5) {
                    cell.setIsFree(true);
                    aux += 1;
                }
            } //End -> if(aux < freeSpaces)
            
            auxSize = auxSize - valueSize;
            
            //This bit looks after the last memory's cell
            if((auxSize >= 1) && (auxSize <= 5)) {
                lastCell.setSize(auxSize);
                lastCell.setIsFree(true);
                lastCell.setProcess(null);
                auxSize = 0;
            }
            
            if(!cell.isIsFree()) {
                Process process = new Process();
                process.setSize(valueSize);
                //That's defining randomly the cell's process' life time 
                randomNumber = (int) (3 + Math.random()*8);
                process.setLifeTime(randomNumber);
                process.setId(processId);
                cell.setProcess(process);
                processId++;
            }
            else {
                cell.setProcess(null);
            }
            memory.add(cell);            
        }
        memory.add(lastCell);
        
        //Here is tested if the value returned is legal
        int testing = 0;        
        for(int i= 0; i <= memory.size() - 1; i++) {
            testing = testing + memory.elementAt(i).getSize();
        }
        
        if(testing == size) {
            return memory;
        }
        else {
            return null;
        }        
    }
    
    /*
     * This method cluster all free spaces in the memory (put the free spaces that are side by side, together in just one cell).
     */
    public Vector<MemoryCell> clusterFreeCells(Vector<MemoryCell> memory) {
        Vector<MemoryCell> memoryOut = new Vector<MemoryCell>();
        int j = 0;
        int k = 0;
        int total = 0;
        boolean stop = false;
        boolean avoidRepeatingError = true;
        
        //while(k <= (memory.size() - 1)) {
        for(k = 0; k <= (memory.size() - 1); k++) {
            if(memory.elementAt(k).isIsFree() == true) {
                total = 0;
                j = k;
                while((memory.elementAt(j).isIsFree() == true) && (!stop)){
                    total = total + memory.elementAt(j).getSize();
                    j++;
                    k++;
                    if(j == memory.size()) {
                        stop = true;
                        j--;
                        k--;
                        avoidRepeatingError = false;
                        MemoryCell auxCell = new MemoryCell(true, null, 0);
                        auxCell.setSize(total);
                        memoryOut.add(auxCell);
                    }
                    else {
                        if(memory.elementAt(j).isIsFree() == false) {
                            stop = true;
                            j--;
                            k--;
                        }
                    }

                }
                if(avoidRepeatingError) {
                    MemoryCell auxCell = new MemoryCell(true, null, 0);
                    auxCell.setSize(total);
                    memoryOut.add(auxCell);    
                }
            }
            else {
                memoryOut.add(memory.elementAt(k));
                stop = false;
            }
        }
        return memoryOut;
    }
    
    /*
     * This method decrease in 1 the life time of the processes associated with each cell of the memory.
     */
    public Vector<MemoryCell> decreaseProcessLifeTime(Vector<MemoryCell> memory) {
        int aux = 0;
        
        for(int i = 0; i <= (memory.size() - 1); i++) {
            if(!memory.elementAt(i).isIsFree()) {
                aux = memory.elementAt(i).getProcess().getLifeTime();
                memory.elementAt(i).getProcess().setLifeTime(aux - 1);
                
                if(memory.elementAt(i).getProcess().getLifeTime() == 0) {
                    memory.elementAt(i).setIsFree(true);
                    memory.elementAt(i).setProcess(null);
                }
            }
        }
        memory = this.clusterFreeCells(memory);
        return memory;
    }
    
    /*
     * This method counts free spaces of the memory.
     */
    public int counterOfFreeSpaces(Vector<MemoryCell> memory) {
        int quantityOfFreeSpaces = 0;        
        for(int i= 0; i <= memory.size() - 1; i++) {
            if(memory.elementAt(i).isIsFree() == true) {
                quantityOfFreeSpaces = quantityOfFreeSpaces + memory.elementAt(i).getSize();
            }
        }
        return quantityOfFreeSpaces;
    }
}