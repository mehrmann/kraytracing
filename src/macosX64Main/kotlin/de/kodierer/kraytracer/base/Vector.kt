package de.kodierer.kraytracer.base

import platform.posix.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class Vector(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    companion object {
        fun randomUnitInDisc(): Vector {
            var p: Vector
            do {
                p = 2.0f * Vector(Random.nextFloat(), Random.nextFloat(), 0f) - Vector(1f, 1f, 0f)
            } while (dot(p, p) >= 1.0)
            return p
        }

        fun randomInUnitSphere(): Vector {
            var p: Vector
            do {
                p = 2.0f * Vector(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) - Vector(1f, 1f, 1f)
            } while (p.squaredLength() >= 1.0)
            return p
        }

    }

    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector) = Vector(x - other.x, y - other.y, z - other.z)
    operator fun times(other: Vector) = Vector(x * other.x, y * other.y, z * other.z)
    operator fun div(other: Vector) = Vector(x / other.x, y / other.y, z / other.z)
    operator fun unaryMinus() = Vector(-x, -y, -z)

    operator fun plusAssign(other: Vector) {
        x += other.x
        y += other.y
        z += other.z
    }

    operator fun minusAssign(other: Vector) {
        x -= other.x
        y -= other.y
        z -= other.z
    }

    operator fun timesAssign(other: Vector) {
        x *= other.x
        y *= other.y
        z *= other.z
    }

    operator fun divAssign(other: Vector) {
        x /= other.x
        y /= other.y
        z /= other.z
    }

    operator fun times(factor: Float) = Vector(x * factor, y * factor, z * factor)
    operator fun div(factor: Float) = Vector(x / factor, y / factor, z / factor)

    operator fun timesAssign(factor: Float) {
        x *= factor
        y *= factor
        z *= factor
    }

    operator fun divAssign(factor: Float) {
        x /= factor
        y /= factor
        z /= factor
    }

    operator fun inc(): Vector {
        x += 1f
        y += 1f
        z += 1f
        return this
    }

    operator fun dec(): Vector {
        x -= 1f
        y -= 1f
        z -= 1f
        return this
    }

    fun length() = sqrt(x * x + y * y + z * z)
    fun squaredLength() = x * x + y * y + z * z

    fun unitVector(): Vector {
        return this / this.length()
    }

    fun makeUnitVector(): Vector {
        this /= this.length()
        return this
    }

    fun toIntString() = "${x.toInt()} ${y.toInt()} ${z.toInt()}"

    fun applyToAll(lambda: (Float) -> Float) {
        x = lambda(x)
        y = lambda(y)
        z = lambda(z)
    }

}

/**
 * dot product aka scalar product aka inner product
 *
 * If the dot product of two non zero vectors is zero, then these vectors are orthogonal
 */
fun dot(a: Vector, b: Vector) = a.x * b.x + a.y * b.y + a.z * b.z

/**
 * Cross product aka vector product
 *
 * Given two linearly independent vectors the cross product is a vector that is
 * perpendicular to both vectors and thus normal to the plane containing them.
 *
 * If two vectors have the same direction, are exact opposite, or one of them has zero length
 * then their cross product is zero.
 */
fun cross(a: Vector, b: Vector) = Vector((a.y * b.z - a.z * b.y), -(a.x * b.z - a.z * b.x), (a.x * b.y - a.y * b.x))

fun lerp(x1: Vector, x2: Vector, t: Float): Vector = (1f - t) * x1 + t * x2

operator fun Float.plus(v: Vector) = Vector(this + v.x, this + v.y, this + v.z)
operator fun Float.minus(v: Vector) = Vector(this - v.x, this - v.y, this - v.z)
operator fun Float.times(v: Vector) = Vector(this * v.x, this * v.y, this * v.z)
operator fun Float.div(v: Vector) = Vector(this / v.x, this / v.y, this / v.z)

/**
 * \     /|
 *  \v r/ | B
 *   \ /  |
 *  --*-----
 *     \  |
 *      \v| B
 *       \|
 *
 * length of B = v dot n
 * direction of B is n
 */
fun reflect(v: Vector, n: Vector) = v - 2.0f * dot(v, n) * n


fun refract(v: Vector, n: Vector, niOverNt: Float): Vector? {
    val uv = v.unitVector()
    val dt = dot(uv, n)
    val discriminant = 1.0f - niOverNt * niOverNt * (1 - dt * dt)
    if (discriminant > 0) {
        return niOverNt * (uv - n * dt) - n * sqrt(discriminant)
    }
    return null
}

fun schlick(cosine: Float, ref_idx: Float): Float {
    var r0 = (1 - ref_idx) / (1 + ref_idx)
    r0 *= r0
    return r0 + (1f - r0) * pow((1.0 - cosine), 5.0).toFloat()
}