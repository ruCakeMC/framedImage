/*
 *  Copyright (C) 2022  cakemc-ru
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.cakemc.framedimage.command.fi;

import net.elytrium.java.commons.config.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cakemc.framedimage.FrameDisplay;
import ru.cakemc.framedimage.FramedImage;
import ru.cakemc.framedimage.command.SubCommand;
import ru.cakemc.framedimage.config.Messages;
import ru.cakemc.framedimage.util.BlockUtil;
import ru.cakemc.framedimage.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class CreateSubcommand implements SubCommand {

  private final FramedImage plugin;

  public CreateSubcommand(FramedImage plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean execute(CommandSender commandSender, List<String> args) {
    if (!commandSender.hasPermission("framedimage.subcommand.create")) {
      commandSender.sendMessage(ChatColor.RED + Messages.IMP.MESSAGES.COMMAND.NOT_ENOUGH_PERMISSIONS);
      return true;
    }

    if (args.size() < 3) {
      commandSender.sendMessage(ChatColor.RED + Messages.IMP.MESSAGES.COMMAND.CREATE.TOO_FEW_ARGUMENTS);
      return false;
    }

    if (!(commandSender instanceof Player)) {
      commandSender.sendMessage(ChatColor.RED + Messages.IMP.MESSAGES.COMMAND.CREATE.ONLY_PLAYERS_CAN_USE);
      return true;
    }

    Player player = (Player) commandSender;

    int width = Integer.parseInt(args.get(0));
    int height = Integer.parseInt(args.get(1));
    String urlString = args.get(2);

    if (width <= 0 || height <= 0) {
      commandSender.sendMessage(ChatColor.RED + Messages.IMP.MESSAGES.COMMAND.CREATE.INVALID_ARGUMENTS);
      return false;
    }

    BlockFace blockFace = BlockUtil.getBlockFace(player.getLocation().getYaw());

    Block block = player.getTargetBlock(null, 10);
    if (block == null || block.isEmpty()) {
      commandSender.sendMessage(ChatColor.RED + Messages.IMP.MESSAGES.COMMAND.CREATE.BLOCK_NOT_FOUND);
      return true;
    }

    Location location = BlockUtil.getNextBlockLocation(block.getLocation(), blockFace);

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      try {
        List<BufferedImage> frames = ImageUtil.readFrames(urlString);
        FrameDisplay frameDisplay = new FrameDisplay(plugin, location, blockFace, width, height, frames);
        plugin.add(frameDisplay);
        plugin.saveFrames();
      } catch(IOException e) {
        commandSender.sendMessage(ChatColor.RED + e.getClass().getName() + ": " + e.getMessage());
      }
    });

    return true;
  }

  @Override
  public String help(CommandSender commandSender) {
    return Messages.IMP.MESSAGES.COMMAND.CREATE.HELP;
  }

  @Override
  public String usage(CommandSender commandSender, String name) {
    return Placeholders.replace(Messages.IMP.MESSAGES.COMMAND.CREATE.USAGE, name);
  }
}
