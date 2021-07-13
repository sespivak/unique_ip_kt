import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class IpUniqueChecker {
    private val ipMostStorage: Array<IpLeastStorage?> = arrayOfNulls(256 * 256)
    private val lock: Lock
    fun addNewIp(ip: String?): Boolean {
        if (ip == null) return false
        val ipNum = parse(ip)
        return addNewIp(ipNum)
    }

    private fun getIpLeastStorage(mostIpBytes: Int): IpLeastStorage? {
        if (ipMostStorage[mostIpBytes] == null) {
            lock.lock()
            try {
                if (ipMostStorage[mostIpBytes] == null) {
                    ipMostStorage[mostIpBytes] = IpLeastStorage()
                }
            } finally {
                lock.unlock()
            }
        }
        return ipMostStorage[mostIpBytes]
    }

    private fun addNewIp(ipNum: Long): Boolean {
        if (ipNum == 0L) return false
        val mostIpBytes = (ipNum shr 16).toInt()
        val leastIpBytes = (ipNum and 0xffff).toInt()
        return getIpLeastStorage(mostIpBytes)!!.addNewIp(leastIpBytes)
    }

    init {
        lock = ReentrantLock()
    }
}