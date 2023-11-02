package kr.cosine.itemlink.listener

import kr.cosine.itemlink.config.SettingConfig
import kr.cosine.itemlink.extension.applyComma
import kr.cosine.itemlink.service.EconomyService
import kr.hqservice.framework.bukkit.core.listener.HandleOrder
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import kr.hqservice.framework.global.core.component.Qualifier
import kr.hqservice.framework.nms.Version
import kr.hqservice.framework.nms.extension.getDisplayName
import kr.hqservice.framework.nms.service.item.NmsItemStackService
import kr.hqservice.framework.nms.service.item.NmsNBTTagCompoundService
import kr.hqservice.framework.nms.wrapper.NmsReflectionWrapper
import kr.hqservice.framework.nms.wrapper.getFunction
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Server
import org.bukkit.event.player.AsyncPlayerChatEvent

@Listener
class ItemLinkListener(
    @Qualifier("itemStack") private val nmsItemStackService: NmsItemStackService,
    @Qualifier("tag") private val nmsNBTTagCompoundService: NmsNBTTagCompoundService,
    nmsReflectionWrapper: NmsReflectionWrapper,
    private val server: Server,
    private val settingConfig: SettingConfig,
    private val economyService: EconomyService
) {

    private val fromNBTTagCompoundFunction = nmsReflectionWrapper.getFunction(
        nmsItemStackService.getTargetClass(),
        "save",
        listOf(nmsNBTTagCompoundService.getTargetClass()),
        Version.V_17.handleFunction("b") { setParameterClasses(nmsNBTTagCompoundService.getTargetClass()) }
    )

    private val regex = Regex("\\[(링크|link|fldzm)]")

    @Subscribe(HandleOrder.LAST, ignoreCancelled = true)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message

        if (!regex.containsMatchIn(message)) return
        event.isCancelled = true

        val itemStack = player.inventory.itemInMainHand
        if (itemStack.type.isAir) {
            player.sendMessage(settingConfig.requiredItemMessage)
            return
        }
        val maxLink = settingConfig.maxLink
        if (regex.findAll(message).count() > maxLink) {
            player.sendMessage(settingConfig.tooManyLinkMessage.replace("%max%", maxLink.toString()))
            return
        }
        val displayName = settingConfig.linkFormat.replace("%display%", itemStack.getDisplayName())
        val itemComponent = TextComponent(displayName).legacyToNewComponentStyle()
        val nmsItemStack = nmsItemStackService.wrap(itemStack).getUnwrappedInstance()
        val emptyNBTTagCompound = nmsNBTTagCompoundService.wrap(null).getUnwrappedInstance()
        val json = fromNBTTagCompoundFunction.call(nmsItemStack, emptyNBTTagCompound)?.toString()
        val jsonComponent = TextComponent(json)
        itemComponent.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_ITEM, arrayOf(jsonComponent))

        var linkCount = 0
        val finalMessage = String.format(event.format, player.displayName, message)
        val component = TextComponent()
        val iterator = finalMessage.split("[링크]", "[link]", "[fldzm]", limit = maxLink + 1).iterator()
        while (iterator.hasNext()) {
            component.addExtra(TextComponent(iterator.next()))
            if (iterator.hasNext()) {
                linkCount++
                component.addExtra(itemComponent)
            }
        }
        if (settingConfig.isVaultEnabled) {
            val vaultCost = settingConfig.vaultCost * linkCount
            if (!economyService.hasMoney(player, vaultCost)) {
                player.sendMessage(settingConfig.requiredMoneyMessage.replace("%money%", vaultCost.applyComma()))
                return
            }
            economyService.subtractMoney(player, vaultCost)
        }
        server.onlinePlayers.forEach { onlinePlayer ->
            onlinePlayer.spigot().sendMessage(component)
        }
    }

    private fun TextComponent.legacyToNewComponentStyle(): TextComponent {
        val extra = this.extra
        val newComponent = TextComponent()
        newComponent.hoverEvent = this.hoverEvent
        newComponent.clickEvent = this.clickEvent

        if (extra != null && extra.isNotEmpty()) {
            extra.forEach {
                val child = TextComponent()
                val legacy = it.toLegacyText()
                TextComponent.fromLegacyText(legacy).forEach { newText ->
                    child.addExtra(newText)
                }
                child.hoverEvent = it.hoverEvent
                child.clickEvent = it.clickEvent
                newComponent.addExtra(child)
            }
        } else {
            val child = TextComponent()
            val legacy = this.toLegacyText()
            TextComponent.fromLegacyText(legacy).forEach { newText ->
                child.addExtra(newText)
            }
            newComponent.addExtra(child)
        }
        return newComponent
    }
}