package spacefiller;

public class Parameter<T> {
  T value;
  T min;
  T max;
  String name;

  public Parameter(T value) {
    this.value = value;
  }

  public Parameter(T value, T min, T max) {
    this.value = value;
    this.min = min;
    this.max = max;
  }

  public Parameter(T value, T min, T max, String name) {
    this.value = value;
    this.min = min;
    this.max = max;
  }

  public void set(T value) {
    this.value = value;
  }

  public T get() {
    return value;
  }

  public String getName() {
    return name;
  }

  public T getMin() {
    return min;
  }

  public T getMax() {
    return max;
  }
}
