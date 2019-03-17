package de.kodierer.kraytracer.material

import de.kodierer.kraytracer.base.*
import kotlin.random.Random

class Dielectric(val ref_idx: Float) : Material {
    override fun scatter(ray: Ray, hit: Hit): Scatter? {
        val reflected = reflect(ray.direction.unitVector(), hit.normal)
        val niOverNt: Float
        val refracted: Vector?
        val outwardNormal: Vector
        val reflectProb: Float
        val scattered: Ray
        val cosine: Float
        if (dot(ray.direction, hit.normal) > 0) {
            outwardNormal = -hit.normal
            niOverNt = ref_idx
            cosine = ref_idx * dot(ray.direction, hit.normal) / ray.direction.length()
        } else {
            outwardNormal = hit.normal
            niOverNt = 1.0f / ref_idx
            cosine = -dot(ray.direction, hit.normal) / ray.direction.length()
        }
        refracted = refract(ray.direction, outwardNormal, niOverNt)
        reflectProb = if (refracted != null) {
            schlick(cosine, ref_idx)
        } else {
            1.0f
        }
        scattered = if (Random.nextFloat() < reflectProb) {
            Ray(hit.position, reflected)
        } else {
            Ray(hit.position, refracted!!)
        }
        return Scatter(Vector(1.0f, 1.0f, 1.0f), scattered)

    }

}