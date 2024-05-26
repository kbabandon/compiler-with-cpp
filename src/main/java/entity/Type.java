package entity;

/**
 * @author jxf
 * @create 2024-05-25 11:24
 */
public enum Type {
    /**
     *  _ID_(0),
     *  _INT_(1),
     *  _DOUBLE_(2),
     *  _OPERATOR_(3),
     *  _DELIMITER_(4),
     *  _KEYWORD_(5),
     *  _CHAR_(6),
     *  _STRING_(7),
     *  _COMMENT_(8),
     *  _SPACE_(9):表示"\n" or ' '
     */
    _ID_(0), _INT_(1), _DOUBLE_(2), _OPERATOR_(3), _DELIMITER_(4), _KEYWORD_(5),
    _CHAR_(6), _STRING_(7), _COMMENT_(8), _SPACE_(9);
    private final int value;
    Type(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
