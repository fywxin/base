package org.whale.system.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 王金绍 on 2017/1/18.
 */
public final class CastValUtil {

    public static final Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue() > 0;
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }
            if ("true".equalsIgnoreCase(strVal)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(strVal)) {
                return Boolean.FALSE;
            }
            if ("1".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("0".equals(strVal)) {
                return Boolean.FALSE;
            }
            if ("yes".equalsIgnoreCase(strVal)) {
                return Boolean.TRUE;
            }
            if ("no".equalsIgnoreCase(strVal)) {
                return Boolean.TRUE;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.trim().length() == 0) {
                return null;
            }
        }

        throw new IllegalArgumentException("can not cast to boolean, value : " + value);
    }


    public static final Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.trim().length() == 0) {
                return null;
            }

            return Byte.parseByte(strVal);
        }

        throw new NumberFormatException("can not cast to byte, value : " + value);
    }

    public static final Character castToChar(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Character) {
            return (Character) value;
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }

            if (strVal.length() != 1) {
                throw new NumberFormatException("can not cast to byte, value : " + value);
            }

            return strVal.charAt(0);
        }

        throw new NumberFormatException("can not cast to byte, value : " + value);
    }

    public static final Short castToShort(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.trim().length() == 0) {
                return null;
            }

            return Short.parseShort(strVal);
        }

        throw new NumberFormatException("can not cast to short, value : " + value);
    }


    public static final Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.trim().length() == 0) {
                return null;
            }

            return Integer.parseInt(strVal);
        }

        throw new NumberFormatException("can not cast to int, value : " + value);
    }

    public static final Long castToLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            return Long.parseLong(strVal);
        }
        throw new NumberFormatException("can not cast to long, value : " + value);
    }



    public static final Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.trim().length() == 0) {
                return null;
            }

            return Double.parseDouble(strVal);
        }

        throw new NumberFormatException("can not cast to double, value : " + value);
    }

    public static final Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }

        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.trim().length() == 0) {
                return null;
            }

            return Float.parseFloat(strVal);
        }

        throw new NumberFormatException("can not cast to float, value : " + value);
    }

    public static final BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }

        if (value instanceof Float || value instanceof Double) {
            return BigInteger.valueOf(((Number) value).longValue());
        }

        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }

        return new BigInteger(strVal);
    }

    public static final BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }

        String strVal = value.toString();
        if (strVal.length() == 0 || strVal.trim().length() == 0) {
            return null;
        }

        return new BigDecimal(strVal);
    }

    public static final Date castToDate(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }

        if (value instanceof Date) {
            return (Date) value;
        }

        long longValue = -1;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
            return new Date(longValue);
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.indexOf('-') != -1) {
                String format;
                if (strVal.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                    format = "yyyy-MM-dd HH:mm:ss";
                } else if (strVal.length() == 10) {
                    format = "yyyy-MM-dd";
                } else {
                    format = "yyyy-MM-dd HH:mm:ss.SSS";
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                try {
                    return (Date) dateFormat.parse(strVal);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("can not cast to Date, value : " + strVal);
                }
            }

            if (strVal.length() == 0) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue < 0) {
            throw new IllegalArgumentException("can not cast to Date, value : " + value);
        }

        return new Date(longValue);
    }

    public static final java.sql.Date castToSqlDate(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }

        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }

        if (value instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date) value).getTime());
        }

        long longValue = 0;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue <= 0) {
            throw new IllegalArgumentException("can not cast to Date, value : " + value);
        }

        return new java.sql.Date(longValue);
    }

    public static final java.sql.Timestamp castToTimestamp(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return new java.sql.Timestamp(((Calendar) value).getTimeInMillis());
        }

        if (value instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) value;
        }

        if (value instanceof java.util.Date) {
            return new java.sql.Timestamp(((java.util.Date) value).getTime());
        }

        long longValue = 0;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue <= 0) {
            throw new IllegalArgumentException("can not cast to Date, value : " + value);
        }

        return new java.sql.Timestamp(longValue);
    }
}
