package kr.cosine.itemlink.config

import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration

@Bean
class SettingConfig(
    private val config: HQYamlConfiguration
) {

    var maxLink = 1
        private set

    var linkFormat = ""
        private set

    var isVaultEnabled = true
        private set

    var vaultCost = 500L
        private set

    var requiredItemMessage = ""
        private set

    var tooManyLinkMessage = ""
        private set

    var requiredMoneyMessage = ""
        private set

    fun load() {
        maxLink = config.getInt("link.max")
        linkFormat = config.getString("link.format").colorize()
        isVaultEnabled = config.getBoolean("vault.enabled")
        vaultCost = config.getLong("vault.cost")
        requiredItemMessage = config.getString(
            "message.required-item",
            "§c아이템을 링크하기 위해선, 손에 아이템을 들어주세요."
        ).colorize()
        tooManyLinkMessage = config.getString(
            "message.too-many-link",
            "§c링크 메시지는 최대 한개만 보낼 수 있습니다."
        ).colorize()
        requiredMoneyMessage = config.getString(
            "message.required-money",
            "&c%money%원이 부족합니다."
        ).colorize()
    }

    fun reload() {
        config.reload()
        load()
    }
}