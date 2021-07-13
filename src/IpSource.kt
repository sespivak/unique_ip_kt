import java.io.BufferedReader
import java.io.IOException
import java.io.FileReader
import kotlin.system.exitProcess

class IpSource(ipFileName: String) {
    private var bufferedReader: BufferedReader? = null
    val ip: String?
        get() {
            try {
                return bufferedReader!!.readLine()
            } catch (ex: IOException) {
                println(ex.message)
                exitProcess(1)
            }
        }

    init {
        try {
            val fileReader = FileReader(ipFileName)
            bufferedReader = BufferedReader(fileReader)
        } catch (ex: IOException) {
            println(ex.message)
            exitProcess(1)
        }
    }
}