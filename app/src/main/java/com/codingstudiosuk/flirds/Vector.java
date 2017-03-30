package com.codingstudiosuk.flirds;

class Vector {

    float x, y;
    private float z = 0;

    Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Set x, y, and z coordinates from a Vector3D object.
     *
     * @param v the Vector object to be copied
     */
    public void set(Vector v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }


    /**
     * Set the x, y (and maybe z) coordinates using a float[] array as the source.
     * @param source array to copy from
     */
    public void set(float[] source) {
        if (source.length >= 2) {
            x = source[0];
            y = source[1];
        }
        if (source.length >= 3) {
            z = source[2];
        }
    }


    /**
     * Get a copy of this vector.
     */
    public Vector get() {
        return new Vector(x, y);
    }


    public float[] get(float[] target) {
        if (target == null) {
            return new float[] { x, y, z };
        }
        if (target.length >= 2) {
            target[0] = x;
            target[1] = y;
        }
        if (target.length >= 3) {
            target[2] = z;
        }
        return target;
    }


    /**
     * Calculate the magnitude (length) of the vector
     * @return the magnitude of the vector
     */
    public float mag() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }


    /**
     * Add a vector to this vector
     * @param v the vector to be added
     */
    public void add(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }


    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }


    /**
     * Add two vectors
     * @param v1 a vector
     * @param v2 another vector
     * @return a new vector that is the sum of v1 and v2
     */
    static public Vector add(Vector v1, Vector v2) {
        return add(v1, v2, null);
    }


    /**
     * Add two vectors into a target vector
     * @param v1 a vector
     * @param v2 another vector
     * @param target the target vector (if null, a new vector will be created)
     * @return a new vector that is the sum of v1 and v2
     */
    static public Vector add(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x + v2.x,v1.y + v2.y);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }
        return target;
    }


    /**
     * Subtract a vector from this vector
     * @param v the vector to be subtracted
     */
    public void sub(Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }


    public void sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }


    /**
     * Subtract one vector from another
     * @param v1 a vector
     * @param v2 another vector
     * @return a new vector that is v1 - v2
     */
    static public Vector sub(Vector v1, Vector v2) {
        return sub(v1, v2, null);
    }


    static public Vector sub(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x - v2.x, v1.y - v2.y);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }
        return target;
    }


    /**
     * Multiply this vector by a scalar
     * @param n the value to multiply by
     */
    public void mult(float n) {
        x *= n;
        y *= n;
        z *= n;
    }


    /**
     * Multiply a vector by a scalar
     * @param v a vector
     * @param n scalar
     * @return a new vector that is v1 * n
     */
    static public Vector mult(Vector v, float n) {
        return mult(v, n, null);
    }


    /**
     * Multiply a vector by a scalar, and write the result into a target Vector.
     * @param v a vector
     * @param n scalar
     * @param target Vector to store the result
     * @return the target vector, now set to v1 * n
     */
    static public Vector mult(Vector v, float n, Vector target) {
        if (target == null) {
            target = new Vector(v.x*n, v.y*n);
        } else {
            target.set(v.x*n, v.y*n, v.z*n);
        }
        return target;
    }


    /**
     * Multiply each element of one vector by the elements of another vector.
     * @param v the vector to multiply by
     */
    public void mult(Vector v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
    }


    /**
     * Multiply each element of one vector by the individual elements of another
     * vector, and return the result as a new Vector.
     */
    static public Vector mult(Vector v1, Vector v2) {
        return mult(v1, v2, null);
    }


    /**
     * Multiply each element of one vector by the individual elements of another
     * vector, and write the result into a target vector.
     * @param v1 the first vector
     * @param v2 the second vector
     * @param target Vector to store the result
     */
    static public Vector mult(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x * v2.x, v1.y * v2.y);
        } else {
            target.set(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
        }
        return target;
    }


    /**
     * Divide this vector by a scalar
     * @param n the value to divide by
     */
    public void div(float n) {
        x /= n;
        y /= n;
        z /= n;
    }


    /**
     * Divide a vector by a scalar and return the result in a new vector.
     * @param v a vector
     * @param n scalar
     * @return a new vector that is v1 / n
     */
    static public Vector div(Vector v, float n) {
        return div(v, n, null);
    }


    static public Vector div(Vector v, float n, Vector target) {
        if (target == null) {
            target = new Vector(v.x/n, v.y/n);
        } else {
            target.set(v.x/n, v.y/n, v.z/n);
        }
        return target;
    }


    /**
     * Divide each element of one vector by the elements of another vector.
     */
    public void div(Vector v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
    }


    /**
     * Multiply each element of one vector by the individual elements of another
     * vector, and return the result as a new Vector.
     */
    static public Vector div(Vector v1, Vector v2) {
        return div(v1, v2, null);
    }


    /**
     * Divide each element of one vector by the individual elements of another
     * vector, and write the result into a target vector.
     * @param v1 the first vector
     * @param v2 the second vector
     * @param target Vector to store the result
     */
    static public Vector div(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x/v2.x, v1.y/v2.y);
        } else {
            target.set(v1.x/v2.x, v1.y/v2.y, v1.z/v2.z);
        }
        return target;
    }


    /**
     * Calculate the Euclidean distance between two points (considering a point as a vector object)
     * @param v another vector
     * @return the Euclidean distance between
     */
    public float dist(Vector v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
    }


    /**
     * Calculate the Euclidean distance between two points (considering a point as a vector object)
     * @param v1 a vector
     * @param v2 another vector
     * @return the Euclidean distance between v1 and v2
     */
    static public float dist(Vector v1, Vector v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        float dz = v1.z - v2.z;
        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
    }


    /**
     * Calculate the dot product with another vector
     * @return the dot product
     */
    public float dot(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }


    public float dot(float x, float y, float z) {
        return this.x*x + this.y*y + this.z*z;
    }


    static public float dot(Vector v1, Vector v2) {
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }


    /**
     * Return a vector composed of the cross product between this and another.
     */
    public Vector cross(Vector v) {
        return cross(v, null);
    }


    /**
     * Perform cross product between this and another vector, and store the
     * result in 'target'. If target is null, a new vector is created.
     */
    public Vector cross(Vector v, Vector target) {
        float crossX = y * v.z - v.y * z;
        float crossY = z * v.x - v.z * x;
        float crossZ = x * v.y - v.x * y;

        if (target == null) {
            target = new Vector(crossX, crossY);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }


    static public Vector cross(Vector v1, Vector v2, Vector target) {
        float crossX = v1.y * v2.z - v2.y * v1.z;
        float crossY = v1.z * v2.x - v2.z * v1.x;
        float crossZ = v1.x * v2.y - v2.x * v1.y;

        if (target == null) {
            target = new Vector(crossX, crossY);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }


    /**
     * Rotate the vector to a different angle (added by us).
     */
    public void rotate(float theta){
        theta = (float)((theta/360)*(Math.PI*2));
        float temp = x;
        // Might need to check for rounding errors like with angleBetween function?
        x = (float)(x*Math.cos(theta) - y*Math.sin(theta));
        y = (float)(temp*Math.sin(theta) + y*Math.cos(theta));
    }

    /**
     * Normalize the vector to length 1 (make it a unit vector)
     */
    public void normalize() {
        float m = mag();
        if (m != 0 && m != 1) {
            div(m);
        }
    }


    /**
     * Normalize this vector, storing the result in another vector.
     * @param target Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    public Vector normalize(Vector target) {
        if (target == null) {
            target = new Vector(0,0);
        }
        float m = mag();
        if (m > 0) {
            target.set(x/m, y/m, z/m);
        } else {
            target.set(x, y, z);
        }
        return target;
    }


    /**
     * Limit the magnitude of this vector
     * @param max the maximum length to limit this vector
     */
    public void limit(float max) {
        if (mag() > max) {
            normalize();
            mult(max);
        }
    }


    /**
     * Calculate the angle of rotation for this vector (only 2D vectors)
     * @return the angle of rotation
     */
    public float heading2D() {
        float angle = (float) Math.atan2(-y, x);
        return -1*angle;
    }


    /**
     * Calculate the angle between two vectors, using the dot product
     * @param v1 a vector
     * @param v2 another vector
     * @return the angle between the vectors
     */
    static public float angleBetween(Vector v1, Vector v2) {
        double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
        double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
        return (float) Math.acos(dot / (v1mag * v2mag));
    }


    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }

}