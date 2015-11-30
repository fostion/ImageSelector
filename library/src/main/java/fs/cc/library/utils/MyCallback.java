package fs.cc.library.utils;

public class MyCallback {
	public interface Callback0 {
		Object run();
	}

	public interface Callback1<T> {
		Object run(T param);
	}

	public interface Callback2<T, E> {
		Object run(T param, E param2);
	}

	public interface Callback3<T, E, Q> {
		Object run(T param, E param2, Q param3);
	}
}

