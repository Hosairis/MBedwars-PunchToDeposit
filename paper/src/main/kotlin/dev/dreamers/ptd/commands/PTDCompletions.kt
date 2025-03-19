package dev.dreamers.ptd.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class PTDCompletions: TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (args.size == 1) {
            val completions = mutableListOf<String>()
            if (sender.hasPermission("ptd.commands.reload")) {
                completions.add("reload")
            }
            return completions.filter { it.startsWith(args[0], ignoreCase = true) }.toMutableList()
        }
        return mutableListOf()
    }
}