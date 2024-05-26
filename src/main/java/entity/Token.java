package entity;

/**
 * @author jxf
 * @create 2024-05-25 10:56
 */
public class Token {
    private Integer type;
    private String value;
    private String category;

    public Token(Integer type, String value, String category) {
        this.type = type;
        this.value = value;
        this.category = category;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
