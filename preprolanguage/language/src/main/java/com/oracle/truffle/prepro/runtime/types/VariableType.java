package com.oracle.truffle.prepro.runtime.types;

public enum VariableType {
    VEC3("vec3", PreProVector3.class),
    VEC4("vec4", PreProVector4.class),
    MAT("mat", PreProMatrix.class),
    MAT3("mat3", PreProMatrix3.class),
    MAT4("mat4", PreProMatrix4.class),
    SCAL("scal", PreProScalar.class),
    CONSTANT("const", PreProConstant.class);

    private final String text;
    private final Class<?> clazz;

    VariableType(String text, Class<?> clazz) {
        this.text = text;
        this.clazz = clazz;
    }

    
    public static VariableType getTypeForText(String text) {
        for (VariableType value : VariableType.values()) {
            if (value.getText().equals(text)) {
                return value;
            }
        }
        throw new RuntimeException("Type " + text + " not known.");
    }

    public static VariableType getTypeForClass(Class<?> clazz) {
        for (VariableType type : VariableType.values()) {
            if(type.getVariableClass().equals(clazz)){
                return type;
            }
        }
        
        throw new RuntimeException("Class " + clazz + " not known.");
    }

    public String getText() {
        return text;
    }

    public Class<?> getVariableClass() {
        return clazz;
    }
}
