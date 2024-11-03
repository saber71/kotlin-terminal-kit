package heraclius.terminal_kit

import java.awt.Color

object ANSI {
    const val INSERT = "\u001b[2~"
    const val DELETE = "\u001b[3~"
    const val PAGE_UP = "\u001b[5~"
    const val PAGE_DOWN = "\u001b[6~"
    const val UP = "\u001b[A"
    const val DOWN = "\u001b[B"
    const val LEFT = "\u001b[D"
    const val RIGHT = "\u001b[C"
    const val HOME = "\u001b[H"
    const val END = "\u001b[F"
    const val F1 = "\u001bOP"
    const val F2 = "\u001bOQ"
    const val F3 = "\u001bOR"
    const val F4 = "\u001bOS"
    const val F5 = "\u001b[15~"
    const val F6 = "\u001b[17~"
    const val F7 = "\u001b[18~"
    const val F8 = "\u001b[19~"
    const val F9 = "\u001b[20~"
    const val F10 = "\u001b[21~"
    const val F11 = "\u001b[23~"
    const val F12 = "\u001b[24~"
    const val CTRL_UP = "\u001b[1;5A"
    const val CTRL_DOWN = "\u001b[1;5B"
    const val CTRL_LEFT = "\u001b[1;5D"
    const val CTRL_RIGHT = "\u001b[1;5C"

    data class Unit(val prefix: String, val content: Any = "", val suffix: String = "") {
        private val lazyOriginString: Lazy<String> = lazy { getOriginString(content) }

        fun originString(): String {
            return lazyOriginString.value
        }

        override fun toString(): String {
            return prefix + content.toString() + suffix
        }

        private fun getOriginString(arg: Any?): String {
            if (arg == null) return ""
            return when (arg) {
                is Unit -> arg.originString()
                is List<*> -> arg.joinToString("") { getOriginString(it) }
                is Array<*> -> arg.joinToString("") { getOriginString(it) }
                else -> arg.toString()
            }
        }
    }

    fun unit(arg: Any): Unit {
        return Unit("", arg)
    }

    fun cursorUp(rows: Int = 1): Unit {
        return Unit("\u001b[${rows}A")
    }

    fun cursorDown(rows: Int = 1): Unit {
        return Unit("\u001b[${rows}B")
    }

    fun cursorForward(cols: Int = 1): Unit {
        return Unit("\u001b[${cols}C")
    }

    fun cursorBack(cols: Int = 1): Unit {
        return Unit("\u001b[${cols}D")
    }

    fun cursorNextLine(rows: Int = 1): Unit {
        return Unit("\u001b[${rows}E")
    }

    fun cursorPreviousLine(rows: Int = 1): Unit {
        return Unit("\u001b[${rows}F")
    }

    // 光标移动到第 n（默认1）列
    fun cursorHorizontalAbsolute(cols: Int = 1): Unit {
        return Unit("\u001b[${cols}G")
    }

    fun cursorPosition(rows: Int, cols: Int = 1): Unit {
        return Unit("\u001b[${rows};${cols}H")
    }

    /*
    * 清除屏幕的部分区域。
    * 如果n是0（或缺失），则清除从光标位置到屏幕末尾的部分。
    * 如果n是1，则清除从光标位置到屏幕开头的部分。
    * 如果n是2，则清除整个屏幕（在DOS ANSI.SYS中，光标还会向左上方移动）。
    * 如果n是3，则清除整个屏幕，并删除回滚缓存区中的所有行
    * */
    fun eraseInDisplay(n: Int = 0): Unit {
        return Unit("\u001b[${n}J")
    }

    /*
    * 清除行内的部分区域。
    * 如果n是0（或缺失），清除从光标位置到该行末尾的部分。
    * 如果n是1，清除从光标位置到该行开头的部分。
    * 如果n是2，清除整行。光标位置不变。
    * */
    fun eraseInLine(n: Int = 0): Unit {
        return Unit("\u001b[${n}K")
    }

    fun scrollUp(rows: Int = 0): Unit {
        return Unit("\u001b[${rows}S")
    }

    fun scrollDown(rows: Int = 0): Unit {
        return Unit("\u001b[${rows}T")
    }

    fun saveCursorPosition(): Unit {
        return Unit("\u001b[s")
    }

    fun restoreCursorPosition(): Unit {
        return Unit("\u001b[u")
    }

    fun reportSize(): Unit {
        return Unit("\u001b[18t")
    }
    
    fun decodeSize(str: String): Pair<Int, Int>? {
        val regex = """\u001b\[\d+;(\d+);(\d+)t""".toRegex()
        val matchResult = regex.find(str)
        if (matchResult != null) {
            val (row, column) = matchResult.destructured
            return Pair(row.toInt(), column.toInt())
        } else {
            return null
        }
    }

    fun reportCursorPosition(): Unit {
        return Unit("\u001b[6n")
    }

    fun decodeCursorPosition(str: String): Pair<Int, Int>? {
        val regex = """\u001b\[(\d+);(\d+)R""".toRegex()
        val matchResult = regex.find(str)
        if (matchResult != null) {
            val (row, column) = matchResult.destructured
            return Pair(row.toInt(), column.toInt())
        } else {
            return null
        }
    }

    fun bold(arg: Any): Unit {
        return Unit("\u001b[1m", arg, "\u001b[22m")
    }

    fun thin(arg: Any): Unit {
        return Unit("\u001b[2m", arg, "\u001b[22m")
    }

    fun italic(arg: Any): Unit {
        return Unit("\u001b[3m", arg, "\u001b[23m")
    }

    fun underline(arg: Any): Unit {
        return Unit("\u001b[4m", arg, "\u001b[24m")
    }

    fun slowBlink(arg: Any): Unit {
        return Unit("\u001b[5m", arg, "\u001b[25m")
    }

    fun fastBlink(arg: Any): Unit {
        return Unit("\u001b[6m", arg, "\u001b[25m")
    }

    fun reverse(arg: Any): Unit {
        return Unit("\u001b[7m", arg, "\u001b[27m")
    }

    fun hidden(arg: Any): Unit {
        return Unit("\u001b[8m", arg, "\u001b[28m")
    }

    fun crossedOut(arg: Any): Unit {
        return Unit("\u001b[9m", arg, "\u001b[29m")
    }

    fun reset(): Unit {
        return Unit("\u001b[0m")
    }

    fun foreColor(color: Color, content: Any): Unit {
        return Unit("\u001b[38;2;${color.red};${color.green};${color.blue}m", content, "\u001B[39m")
    }

    fun backColor(color: Color, content: Any): Unit {
        return Unit("\u001b[48;2;${color.red};${color.green};${color.blue}m", content, "\u001B[49m")
    }

    fun color(arg: Any, foreColor: Color? = null, backColor: Color? = null): Unit {
        var unit: Unit? = null
        if (foreColor != null) unit = foreColor(foreColor, arg)
        if (backColor != null) unit = backColor(backColor, if (unit == null) arg else unit)
        return if (unit == null) unit(arg) else unit
    }
}
