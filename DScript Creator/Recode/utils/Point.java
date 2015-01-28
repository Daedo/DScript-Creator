package utils;

public class Point<T extends Number> {
	public T x;
	public T y;
	
	public Point(T xVal,T yVal) {
		this.x = xVal;
		this.y = yVal;
	}
}
