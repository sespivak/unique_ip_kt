import java.io.File
import java.util.Arrays
import kotlin.math.roundToInt
import kotlin.system.exitProcess

const val IP_BATCH_SIZE = 10000

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        val f = File(args[0])
        if (!f.exists()) {
            println("File '" + args[0] + "' does not exist!")
            exitProcess(1)
        }
    } else {
        println("IP address filename required in argument!")
        exitProcess(1)
    }
    val startTimestamp = System.currentTimeMillis()
    var monitorTimestamp = startTimestamp
    var timeElapsed: Long
    var ipReadCount: Long = 0
    var uniqueIpCount: Long = 0
    val ipSource = IpSource(args[0])
    val ipUniqueChecker = IpUniqueChecker()
    var ipArray = arrayOfNulls<String>(IP_BATCH_SIZE)
    var ip: String? = null
    while (ipArray.size == IP_BATCH_SIZE) {
        for (i in ipArray.indices) {
            ip = ipSource.ip
            if (ip == null) {
                // last non-null ip value
                ip = if (i > 0) ipArray[i - 1] else ipArray[IP_BATCH_SIZE - 1]
                ipArray = ipArray.copyOf(i)
                break
            }
            ipReadCount++
            ipArray[i] = ip
        }
        uniqueIpCount += Arrays.stream(ipArray).parallel().reduce(
            0.toLong(),
            { partialSum: Long, ipString: String? ->
                partialSum + if (ipUniqueChecker.addNewIp(ipString)) 1 else 0
            }
        ) { a: Long, b: Long -> java.lang.Long.sum(a, b) }
        if (System.currentTimeMillis() - monitorTimestamp >= 10000) {
            monitorTimestamp = System.currentTimeMillis()
            timeElapsed = monitorTimestamp - startTimestamp
            printStatistics(timeElapsed, ipReadCount, uniqueIpCount, ip)
        }
    }
    timeElapsed = System.currentTimeMillis() - startTimestamp
    printStatistics(timeElapsed, ipReadCount, uniqueIpCount, ip)
}

fun printStatistics(timeElapsed: Long, ipReadCount: Long, uniqueIpCount: Long, lastIp: String?) {
    print("Time elapsed, sec.: ")
    print((timeElapsed * 0.001).roundToInt())
    print(" Total IPs read: ")
    print(ipReadCount)
    print(" Unique IPs: ")
    print(uniqueIpCount)
    print(" Unique percent: ")
    print((100.0 * uniqueIpCount / ipReadCount).roundToInt())
    print(" Last IP: ")
    print(lastIp)
    println()
}