def run = { sender, label, args ->
    try {
        sender.sendMessage("Your number is ${Integer.valueOf(args[0])}")
    } catch (NumberFormatException ignored) {

    }
}