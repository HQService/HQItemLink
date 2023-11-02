package kr.cosine.itemlink

import kr.cosine.itemlink.command.ItemLinkAdminCommand
import kr.cosine.itemlink.config.SettingConfig
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule

@Component
class HQItemLinkModule(
    private val plugin: HQBukkitPlugin,
    private val settingConfig: SettingConfig,
    private val itemLinkAdminCommand: ItemLinkAdminCommand
) : HQModule {

    override fun onEnable() {
        settingConfig.load()
        plugin.getCommand("링크관리")?.setExecutor(itemLinkAdminCommand)
    }
}