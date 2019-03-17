package de.kodierer.kraytracer.base

class ObjectList : Object {

    private val objects = mutableListOf<Object>()

    fun add(o: Object) { objects.add(o) }
    fun remove(o: Object) { objects.remove(o) }

    override fun hit(ray: Ray, tMin: Float, tMax: Float): Hit? {
        var bestHit : Hit? = null
        var closest = tMax
        objects.forEach { o ->
            var potentialHit = o.hit(ray, tMin, closest)
            if (potentialHit != null) {
                bestHit = potentialHit
                closest = potentialHit.t
            }
        }
        return bestHit
    }
}