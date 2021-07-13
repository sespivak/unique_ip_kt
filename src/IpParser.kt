import java.lang.NumberFormatException

fun parse(ipString: String): Long {
    var beginIndex = 0
    var endIndex: Int
    var res: Long = 0
    var segment: Long
    for (i in 0..3) {
        if (i < 3) {
            endIndex = ipString.indexOf('.', beginIndex + 1)
            if (endIndex == -1) {
                println(ipString)
                return 0
            }
        } else {
            endIndex = ipString.length
        }
        try {
            segment = Integer.parseUnsignedInt(ipString.substring(beginIndex, endIndex)).toLong()
            beginIndex = endIndex + 1
        } catch (e: NumberFormatException) {
            println(ipString)
            return 0
        }
        if (segment > 255 || segment < 0) {
            println(ipString)
            return 0
        }
        res += segment shl (3 - i) * 8
    }
    return res
}