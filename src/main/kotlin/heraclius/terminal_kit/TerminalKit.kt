package heraclius.terminal_kit

import heraclius.tools.TerminalAdapter

class TerminalKit(val adapter: TerminalAdapter) {
    suspend fun getSize(): Pair<Int, Int> {
        adapter.write(ANSI.reportCursorPosition())
        while (true) {
            val str = adapter.input()
            val size = ANSI.decodeCursorPosition(str)
            if (size != null) return size
        }
    }
}
