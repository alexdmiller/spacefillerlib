package spacefiller;

public interface FloatField2 {
  float get(float x, float y);

  FloatField2 ONE = (x, y) -> 1;

  class Constant implements FloatField2 {
    private float value;

    public Constant(float value) {
      this.value = value;
    }

    @Override
    public float get(float x, float y) {
      return value;
    }
  }

}


