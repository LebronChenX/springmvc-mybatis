package com.lebron.springmvc.web.coverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * @Desc trim
 * @author tvu
 * @Date 2017年7月24日 上午10:08:35
 * @version 1.0.0
 */
public class StringTrimConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		if (StringUtils.isNotEmpty(source)) {
			String trim = source.trim();
			if ("".equals(trim)) {
				return null;
			}
			return trim;
		}
		return null;
	}
}
