package kr.cosine.itemlink.command

import kr.cosine.itemlink.config.SettingConfig
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

@Bean
class ItemLinkAdminCommand(
    private val settingConfig: SettingConfig
) : CommandExecutor, TabCompleter {

    private val commandTabList = listOf("리로드")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            printHelp(sender)
            return true
        }
        when (args[0]) {
            "리로드" -> {
                settingConfig.reload()
                sender.sendMessage("§aconfig.yml을 리로드하였습니다.")
            }
            else -> printHelp(sender)
        }
        return true
    }

    private fun printHelp(sender: CommandSender) {
        sender.sendMessage("§6[ /링크관리 리로드 ] §8- §fconfig.yml을 리로드합니다.")
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        if (args.size <= 1) {
            return commandTabList
        }
        return emptyList()
    }
}