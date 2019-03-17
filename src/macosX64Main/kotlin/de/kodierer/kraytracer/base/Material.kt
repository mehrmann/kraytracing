package de.kodierer.kraytracer.base

data class Scatter(val attenuation: Vector, val scattered: Ray)

interface Material {
    fun scatter(ray: Ray, hit: Hit): Scatter?
}

