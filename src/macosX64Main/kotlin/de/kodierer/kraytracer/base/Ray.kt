package de.kodierer.kraytracer.base

data class Ray (val origin: Vector, val direction: Vector) {
    fun pointAtParameter(t: Float) = origin + direction * t
}