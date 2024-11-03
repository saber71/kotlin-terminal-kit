import heraclius.terminal_kit.ANSI

fun main() {
    print(ANSI.decodeCursorPosition("\u001b[2;20R"))
}
