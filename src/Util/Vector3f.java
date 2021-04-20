package Util;

import java.io.Serializable;

public class Vector3f implements Serializable {
  static final long serialVersionUID = -7031930069184524614L;
  
  public float x;
  
  public float y;
  
  public float z;
  
  public Vector3f(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Vector3f(float[] v) {
    this.x = v[0];
    this.y = v[1];
    this.z = v[2];
  }
  
  public Vector3f(Vector3f v1) {
    this.x = v1.x;
    this.y = v1.y;
    this.z = v1.z;
  }
  
  public Vector3f() {}
  
  public final float lengthSquared() {
    return this.x * this.x + this.y * this.y + this.z * this.z;
  }
  
  public final float length() {
    return 
      (float)Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z));
  }
  
  public final void cross(Vector3f v1, Vector3f v2) {
    float x = v1.y * v2.z - v1.z * v2.y;
    float y = v2.x * v1.z - v2.z * v1.x;
    this.z = v1.x * v2.y - v1.y * v2.x;
    this.x = x;
    this.y = y;
  }
  
  public final float dot(Vector3f v1) {
    return this.x * v1.x + this.y * v1.y + this.z * v1.z;
  }
  
  public final void normalize(Vector3f v1) {
    float norm = (float)(1.0D / Math.sqrt((v1.x * v1.x + v1.y * v1.y + v1.z * v1.z)));
    v1.x *= norm;
    v1.y *= norm;
    v1.z *= norm;
  }
  
  public final void normalize() {
    float norm = 
      (float)(1.0D / Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z)));
    this.x *= norm;
    this.y *= norm;
    this.z *= norm;
  }
  
  public final float angle(Vector3f v1) {
    double vDot = (dot(v1) / length() * v1.length());
    if (vDot < -1.0D)
      vDot = -1.0D; 
    if (vDot > 1.0D)
      vDot = 1.0D; 
    return (float)Math.acos(vDot);
  }
}
