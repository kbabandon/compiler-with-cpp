package entity;

/**
 * @author jxf
 * @create 2024-05-29 15:11
 */
public class Production {
    private char origin;
    private String array;
    private Integer length;

    public Production(char origin, String array) {
        this.origin = origin;
        this.array = array;
        this.length = array.length();
    }

    public Production(Production production){
        this(production.getOrigin(), production.getArray());
//        this.origin = production.getOrigin();
//        this.array = production.getArray();
//        this.length = production.getLength();
    }

    public char getOrigin() {
        return origin;
    }

    public void setOrigin(char origin) {
        this.origin = origin;
    }

    public String getArray() {
        return array;
    }

    public void setArray(String array) {
        this.array = array;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Production{" +
                "origin=" + origin +
                ", array='" + array + '\'' +
                ", length=" + length +
                '}';
    }
}
