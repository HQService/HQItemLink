package kr.cosine.itemlink.service

import kr.hqservice.framework.global.core.component.Service
import net.milkbowl.vault.economy.Economy
import org.bukkit.Server
import org.bukkit.entity.Player

@Service
class EconomyService(
    private val server: Server
) {

    private val economy by lazy {
        server.servicesManager.getRegistration(Economy::class.java)!!.provider
    }

    fun hasMoney(player: Player, amount: Long): Boolean {
        return getMoney(player) >= amount
    }

    private fun getMoney(player: Player): Long = economy.getBalance(player).toLong()

    fun subtractMoney(player: Player, amount: Long) {
        economy.withdrawPlayer(player, amount.toDouble())
    }
}