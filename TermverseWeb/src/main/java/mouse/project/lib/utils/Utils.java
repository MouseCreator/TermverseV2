package mouse.project.lib.utils;

public class Utils {

    public static Object validatePrimitive(Class<?> clazz, Object toTest) {
        if (clazz == int.class) {
            if (toTest instanceof Integer) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not an integer");
            }
        } else if (clazz == byte.class) {
            if (toTest instanceof Byte) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not a byte");
            }
        } else if (clazz == short.class) {
            if (toTest instanceof Short) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not a short");
            }
        } else if (clazz == long.class) {
            if (toTest instanceof Long) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not a long");
            }
        } else if (clazz == float.class) {
            if (toTest instanceof Float) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not a float");
            }
        } else if (clazz == double.class) {
            if (toTest instanceof Double) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not a double");
            }
        } else if (clazz == char.class) {
            if (toTest instanceof Character) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not a char");
            }
        } else if (clazz == boolean.class) {
            if (toTest instanceof Boolean) {
                return toTest;
            } else {
                throw new IllegalArgumentException(toTest + " is not a boolean");
            }
        } else {
            throw new IllegalArgumentException(clazz + " is not a primitive type");
        }
    }

    public static Class<?> fromPrimitive(Class<?> clazz) {
        if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == byte.class) {
           return Byte.class;
        } else if (clazz == short.class) {
            return Short.class;
        } else if (clazz == long.class) {
            return Long.class;
        } else if (clazz == float.class) {
            return Float.class;
        } else if (clazz == double.class) {
            return Double.class;
        } else if (clazz == char.class) {
            return Character.class;
        } else if (clazz == boolean.class) {
            return Boolean.class;
        } else {
            return clazz;
        }
    }
}
