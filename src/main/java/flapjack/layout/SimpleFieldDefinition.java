package flapjack.layout;

/**
 * Basic implementation of a FieldDefinition
 */
public class SimpleFieldDefinition implements FieldDefinition {
    private int position;
    private int length;
    private String name;

    public SimpleFieldDefinition() {
    }

    public SimpleFieldDefinition(String name, int position, int length) {
        this.position = position;
        this.length = length;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName() + "@" + getPosition() + ",length=" + getLength();
    }
}
