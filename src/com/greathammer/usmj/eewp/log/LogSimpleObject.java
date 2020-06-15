package com.greathammer.usmj.eewp.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.greathammer.usmj.eewp.math.MathPair;

/**
 * 日志对象对. <br>
 * 继承MathPair. First-contentTag, Second-content <br>
 * 显示为[contentTag] content
 * 
 * @author Zheng Chao
 * @param <T>
 *            contentTag
 * @param <S>
 *            content
 * @version 0.1.3
 */
public class LogSimpleObject<T, S> extends MathPair<T, S> {

	public LogSimpleObject(T contentTag, S content) {
		super(contentTag, content);
	}

	public T getContentTag() {
		return getFirst();
	}

	public S getContent() {
		return getSecond();
	}

	@Override
	public String toString() {
		S second = getSecond();
		if (getSecond() instanceof Exception) {
			StringWriter out = new StringWriter();
			((Exception) second).printStackTrace(new PrintWriter(out));
			String description = out.toString();
			return "[" + getContentTag() + "] " + description;
		}
		return "[" + getContentTag() + "] " + getContent();
	}

	public static void main(String[] args) {
		String contentTag = "contentTag";
		String content = "content";
		System.out.println(new LogSimpleObject<String, String>(contentTag, content));
	}

}
