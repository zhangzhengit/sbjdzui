package com.example.demo.s;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 接口统一返回数据
 *
 * @param <T>
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> implements Serializable {

	private static final long serialVersionUID = -8734600502839849231L;

	private int code = 0;
	private T data;
	private String message;

	public static <T> R<T> ok() {
		return R.ok(null);
	}

	public static <T> R<T> ok(final T data) {
		final R<T> r = new R<>();
		r.setData(data);
		return r;
	}

	public static <T> R<T> error(final String message, final int code) {
		final R<T> r = new R<>();
		r.setCode(code);
		r.setMessage(message);
		return r;
	}
}
