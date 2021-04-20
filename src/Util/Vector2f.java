package Util;

import java.io.Serializable;

public class Vector2f implements Serializable {
  static final long serialVersionUID = -2168194326883512320L;
  
  public float x;
  
  public float y;
  
  public Vector2f(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  public Vector2f(float[] v) {
    this.x = v[0];
    this.y = v[1];
  }
  
  public Vector2f(Vector2f v1) {
    this.x = v1.x;
    this.y = v1.y;
  }
  
  public Vector2f() {}
  
  public final float dot(Vector2f v1) {
    return this.x * v1.x + this.y * v1.y;
  }
  
  public final float length() {
    return (float)Math.sqrt((this.x * this.x + this.y * this.y));
  }
  
  public final float lengthSquared() {
    return this.x * this.x + this.y * this.y;
  }
  
  public final void normalize(Vector2f v1) {
    float norm = (float)(1.0D / Math.sqrt((v1.x * v1.x + v1.y * v1.y)));
    v1.x *= norm;
    v1.y *= norm;
  }
  
  public final void normalize() {
    float norm = 
      (float)(1.0D / Math.sqrt((this.x * this.x + this.y * this.y)));
    this.x *= norm;
    this.y *= norm;
  }
  
  public final float angle(Vector2f v1) {
    double vDot = (dot(v1) / length() * v1.length());
    if (vDot < -1.0D)
      vDot = -1.0D; 
    if (vDot > 1.0D)
      vDot = 1.0D; 
    return (float)Math.acos(vDot);
  }
}
