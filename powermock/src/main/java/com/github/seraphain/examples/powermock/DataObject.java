package com.github.seraphain.examples.powermock;

/**
 * 
 * @author
 */
public class DataObject {

    private String name;

    private long number;

    public DataObject() {
    }

    public DataObject(String name, long number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataObject)) {
            return false;
        }
        DataObject object = (DataObject) obj;
        if (!equals(name, object.getName())) {
            return false;
        }
        if (number != object.getNumber()) {
            return false;
        }
        return true;
    }

    private boolean equals(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        } else {
            return s1.equals(s2);
        }
    }

}
