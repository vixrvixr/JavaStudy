package academy.kovalevskyi.javadeepdive.week2.day0;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/*
only for String, String[], Integer, Integer[] and Classes.
Don't use with Classes[]
*/
public class JsonHelper {

    public static <T> String toJsonString(T target) throws IllegalAccessException {
        if (target == null) {
            return "null";
        }
        if (target instanceof Integer || target instanceof String) {
            return oneValueToJson(target);
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (target.getClass().isArray()) {
            stringBuilder.append(arrayToJson(target));
            return stringBuilder.toString();
        }
        stringBuilder.append("{");

        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            /**
             * Increase accessibility need for tests
             */
            field.setAccessible(true);
            // add field
            stringBuilder.append("\"" + field.getName() + "\" : ");
            // add value
            if (field.get(target).getClass().isArray()) {
                stringBuilder.append(arrayToJson(field.get(target)));
            } else {
                stringBuilder.append(oneValueToJson(field.get(target)));
            }
            stringBuilder.append(",");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
     }

    public static <T> T fromJsonString(String json, Class<T> cls)
            throws IllegalAccessException, InvocationTargetException, InstantiationException,
            NoSuchFieldException, NoSuchMethodException {
        if (json == null || json.equals("null") || json.equals("")) {
            return null;
        }
        if (json.equals("[]") || json.equals("{}")) {
            if (cls.isArray()) {
                return (T) Array.newInstance(cls.getComponentType(), 0);
            }
            return null;
        }
        /**
         * delete '{' and '}'
         */
        T obj = null;
        json = json.substring(1, json.length() - 1);
        String[] values = json.split(",");
        if (cls.isArray()) {
            obj = (T) Array.newInstance(cls.getComponentType(), values.length);
            int i = 0;
            for (String s : values) {
                if (cls.getComponentType().equals(String.class)) {
                    s = s.replace("\"", "");
                    Array.set(obj, i++, s);
                } else if (cls.getComponentType().equals(Integer.class) ||
                        cls.getComponentType().equals(int.class)) {
                    Array.set(obj, i++, Integer.parseInt(s));
                }
            }
        } else {
            obj = cls.getDeclaredConstructor().newInstance();
            for (String s : values) {
                s = s.replace("\"", "");

                String[] stringField  = s.split(":");
                try {
                        stringField[0] = stringField[0].strip();
                        stringField[1] = stringField[1].strip();

                        Field field = cls.getDeclaredField(stringField[0]);
                        /**
                         * Increase accessibility need for tests
                         */
                        field.setAccessible(true);

                        Class<?> type = field.getType();
                        String typeName = type.getSimpleName();
                        if (typeName.equals("Integer") || typeName.equals("int")) {// is Digit or isNumber() // compare types
                            field.setInt(obj, Integer.parseInt(stringField[1]));
                        } else {
                            field.set(obj, stringField[1]);
                        }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    private static String oneValueToJson(final Object obj) {
        if (obj.getClass().equals(String.class)) {
            return String.format("\"%s\"", obj);
        } else {
            return obj.toString();
        }
    }

    private static String arrayToJson(final Object obj) {
        final int length = Array.getLength(obj);
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        if (length > 0) {
            for (int index = 0; index < length; index++) {
                builder.append(oneValueToJson(Array.get(obj, index))).append(',');
            }
            builder.setCharAt(builder.length() - 1, ']');
        } else {
            builder.append(']');
        }
        return builder.toString();
    }
}