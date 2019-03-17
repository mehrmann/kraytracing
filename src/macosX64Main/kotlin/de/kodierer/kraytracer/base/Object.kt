package de.kodierer.kraytracer.base

data class Hit(val t: Float, val position: Vector, val normal: Vector, val material: Material)

interface Object {
    fun hit(ray : Ray, tMin : Float, tMax: Float) : Hit?
}