package heraclius.terminal_kit

import heraclius.tools.TerminalAdapter

class TerminalKit(val adapter: TerminalAdapter) {
    // 获取终端大小
    suspend fun getSize(): Pair<Int, Int> {
        adapter.write(ANSI.reportSize())
        while (true) {
            val str = adapter.input()
            val size = ANSI.decodeSize(str)
            if (size != null) return size
        }
    }

    // 获取光标位置
    suspend fun getCursorPosition(): Pair<Int, Int> {
        adapter.write(ANSI.reportCursorPosition())
        while (true) {
            val str = adapter.input()
            val size = ANSI.decodeCursorPosition(str)
            if (size != null) return size
        }
    }

    suspend fun inputLine(): String {
        return handleInputLine { adapter.write(it) }
    }

    suspend fun inputPassword(): String {
        return handleInputLine { adapter.write("*") }
    }

    private suspend fun handleInputLine(writeChar: suspend (String) -> Unit): String {
        var line = ""
        while (true) {
            val str = adapter.input()
            if (str == "\n" || str == "\r") {
                adapter.write("\n\r")
                return line
            }
            line += str
            writeChar(str)
        }
    }
}
