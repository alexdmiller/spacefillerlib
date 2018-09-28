package spacefiller;

public interface FloatField3 {
  float get(float x, float y, float z);

  FloatField3 ONE = (x, y, z) -> 1;
  FloatField3 ZERO = (x, y, z) -> 0;
}