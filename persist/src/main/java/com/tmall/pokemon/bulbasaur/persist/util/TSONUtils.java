package com.tmall.pokemon.bulbasaur.persist.util;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目对于上下文的特殊处理
 * Created by IntelliJ IDEA.
 * User: guichen - anson
 * Date: 12-11-24
 */
public class TSONUtils {
    public static String toTSONwithFilter(Map<String, Object> params) {
        Map<String, Object> resultParams = new HashMap<String, Object>();
        for (String key : params.keySet()) {
            if (key.startsWith("_")) {
                resultParams.put(key, params.get(key));
            }
        }
        return toTSON(resultParams);
    }

    public static String toTSON(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder s = new StringBuilder("{");
        boolean hasValue = false;
        String[] keys = params.keySet().toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        Arrays.sort(keys);
        for (String key : keys) {
            Object value = params.get(key);
            char typeToken = typeToken(value);
            if (typeToken == 'E') { continue; }
            hasValue = true;
            s.append(typeToken);
            s.append('"').append(key.replace("\"", "\\\"")).append('"');
            s.append(':');
            s.append('"').append(String.valueOf(value).replace("\"", "\\\"")).append('"');
            s.append(',');
        }
        if (hasValue) {
            s.deleteCharAt(s.length() - 1);
        }
        s.append('}');
        return s.toString();
    }

    public static Map<String, Object> fromTSON(String tson) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (tson == null || tson.isEmpty()) {
            return result;
        }
        char token = 't';
        char tToken = 'I';
        String key = "";
        int index = 1;
        while (index < (tson.length() - 1)) {
            char c = tson.charAt(index);
            switch (token) {
                case 't':
                    tToken = c;
                    index++;
                    token = 'k';
                    break;
                case 'k':
                    ContentValue contentValue = firstQuoteContent(tson.substring(index));
                    key = contentValue.valueStr;
                    index += (contentValue.realSize + 3);
                    token = 'v';
                    break;
                case 'v':
                    ContentValue contentValue2 = firstQuoteContent(tson.substring(index));
                    index += (contentValue2.realSize + 3);
                    result.put(key, getObject(tToken, contentValue2.valueStr));
                    token = 't';
                    break;
                default:
                    throw new RuntimeException("解析失败！tson：" + tson);
            }
        }
        return result;
    }

    public static boolean isTSONSupport(Object o) {
        return typeToken(o) != 'E';
    }

    private static ContentValue firstQuoteContent(String tson) {
        StringBuilder result = new StringBuilder("");
        boolean escape = false;
        boolean end = false;
        int index = 1;
        int escapeSize = 0;
        while (!end) {
            char c = tson.charAt(index);
            index++;
            switch (c) {
                case '\\':
                    if (escape) {
                        escape = false;
                        result.append(c);
                    } else {
                        escape = true;
                        escapeSize++;
                    }
                    break;
                case '"':
                    if (escape) {
                        escape = false;
                        result.append(c);
                    } else {
                        end = true;
                    }
                    break;
                default:
                    escape = false;
                    result.append(c);
            }
        }
        return new ContentValue(result.length() + escapeSize, result.toString());
    }

    private static char typeToken(Object o) {
        if (o instanceof Integer) {
            return 'I';
        }
        if (o instanceof Long) {
            return 'L';
        }
        if (o instanceof String) {
            return 'S';
        }
        if (o instanceof Character) {
            return 'C';
        }
        if (o instanceof Byte) {
            return 'B';
        }
        if (o instanceof Float) {
            return 'F';
        }
        if (o instanceof Double) {
            return 'D';
        }
        if (o instanceof Boolean) {
            return 'O';
        }
        if (o == null) {
            return 'N';
        }
        return 'E';
    }

    private static Object getObject(char typeT, String valueStr) {
        switch (typeT) {
            case 'I':
                return Integer.valueOf(valueStr);
            case 'L':
                return Long.valueOf(valueStr);
            case 'S':
                return valueStr;
            case 'C':
                return valueStr.charAt(0);
            case 'B':
                return Byte.valueOf(valueStr);
            case 'F':
                return Float.valueOf(valueStr);
            case 'D':
                return Double.valueOf(valueStr);
            case 'O':
                return Boolean.valueOf(valueStr);
            case 'N':
                return null;
        }
        throw new IllegalArgumentException("unrecognized type:" + typeT);
    }

    private static class ContentValue {
        private ContentValue(int realSize, String valueStr) {
            this.realSize = realSize;
            this.valueStr = valueStr;
        }

        private int realSize;
        private String valueStr;
    }
}
