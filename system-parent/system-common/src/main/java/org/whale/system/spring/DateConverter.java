package org.whale.system.spring;

import org.springframework.core.convert.converter.Converter;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;

import java.util.Date;

/**
 * 时间转换
 */
public class DateConverter implements Converter<String, Date> {
	
    /*** 短类型日期长度 */
    public static final int SHORT_DATE_LENGTH = 10;

    @Override
    public Date convert(String source) {
        if (Strings.isBlank(source)) {
            return null;
        }

        if (Strings.isNumeric(source)) {
            return new Date(Long.parseLong(source));
        }

        return TimeUtil.parseTime(source);
    }
}  