package com.oracle.truffle.prepro.runtime.types;

import java.util.Arrays;

public enum VariableType {
    VEC3("vec3", PreProVector3.class),
    VEC4("vec4", PreProVector4.class),
    MAT("mat", PreProMatrix.class),
    MAT3("mat3", PreProMatrix3.class),
    MAT4("mat4", PreProMatrix4.class),
    SCAL("scal", PreProScalar.class),
    CONSTANT("const", PreProConstant.class);

    private final String text;
    private final Class clazz;

    VariableType(String text, Class clazz) {
        this.text = text;
        this.clazz = clazz;
    }

    public static VariableType getTypeForText(String text) {
        return Arrays.stream(VariableType.values())
                .filter(type -> type.getText().equals(text))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Type " + text + " not known."));
    }

    public static VariableType getTypeForClass(Class clazz) {
        return Arrays.stream(VariableType.values())
                .filter(type -> type.getVariableClass().equals(clazz))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Class " + clazz + " not known."));
    }

    public String getText() {
        return text;
    }

    public Class getVariableClass() {
        return clazz;
    }
}
