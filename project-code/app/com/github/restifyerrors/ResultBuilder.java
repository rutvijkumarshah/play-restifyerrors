package com.github.restifyerrors;

public interface ResultBuilder<E extends Throwable> {
	public play.mvc.Result getResult(E exception);
}
