package ubiquitaku.block_drop;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class Block_drop extends JavaPlugin {
    FileConfiguration config;
    int kakuritu;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            kakuritu = config.getInt("kaku");
        } catch (NumberFormatException kaku){
            kakuritu = 10000;
            config.set("kaku",10000);
            System.out.println("Brock_drop configの値を取得できなかったためデフォルトの10000に設定されました");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("bd")) {
            if (!(sender.isOp())) {
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage("Brock_drop plugin");
                sender.sendMessage("現在のドロップ率 : 1/" + config.getInt("kaku"));
                sender.sendMessage("/bd <数字> : ドロップ率の分母の値を変更します");
                return true;
            }
            try {
                int k = Integer.parseInt(args[0]);
            } catch (NumberFormatException k) {
                sender.sendMessage("/bd でコマンドのヘルプを確認することができます");
                return true;
            }
            config.set("kaku", args[0]);
            System.out.println("bd確率変更" + sender + " " + args[0]);
            saveConfig();
            reloadConfig();
            sender.sendMessage("確率を修正しました");
            return true;
        }
        return true;
    }

    @EventHandler
    void BlockBreakEvent(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getBlock().getType() != Material.STONE) {
            return;
        }
        Random rnd = new Random();
        int x = rnd.nextInt(kakuritu);
        if (x == 1) {
            e.setDropItems(false);
            ItemStack item = new ItemStack(Material.DIAMOND);
            e.getPlayer().sendMessage("&lあっ、ダイヤモンド!");
            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
        }
    }
}
