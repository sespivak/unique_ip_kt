import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class IpLeastStorage {
    private val ipArray: LongArray = LongArray(1024)
    private val lock: Lock
    fun addNewIp(leastIpBytes: Int): Boolean {
        val leastIpIndex = leastIpBytes shr 6
        val leastIpBitmask = 1.toLong() shl (leastIpBytes and 63)
        if (ipArray[leastIpIndex] and leastIpBitmask != 0L) return false
        lock.lock()
        try {
            if (ipArray[leastIpIndex] and leastIpBitmask == 0L) {
                ipArray[leastIpIndex] = ipArray[leastIpIndex] or leastIpBitmask
                return true
            }
        } finally {
            lock.unlock()
        }
        return false
    }

    init {
        lock = ReentrantLock()
    }
}