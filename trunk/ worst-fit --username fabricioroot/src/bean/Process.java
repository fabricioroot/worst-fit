/*
 * Process.java
 *
 * Created on January 26th by Fabricio Reis.
 */

package bean;

/**
 *
 * @author Fabricio Reis
 */
public class Process {
    private int fathersId;
    private int id;
    private int lifeTime;
    private int priority;
    private int state;
    private int size;
    
    public Process() {        
    }

    public Process(int fathersId, int id,int lifeTime, int priority, int state, int size) {
        this.fathersId = fathersId;
        this.id = id;
        this.lifeTime = lifeTime;
        this.priority = priority;
        this.state = state;
        this.size = size;        
    }

    public int getFathersId() {
        return fathersId;
    }

    public void setFathersId(int fathersId) {
        this.fathersId = fathersId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int likeTime) {
        this.lifeTime = likeTime;
    }    

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }    
}