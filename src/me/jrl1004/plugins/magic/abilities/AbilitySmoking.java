package me.jrl1004.plugins.magic.abilities;

import java.util.Random;

import me.jrl1004.plugins.magic.managers.ChatManager;
import me.jrl1004.plugins.magic.particles.ParticleEffect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class AbilitySmoking extends AbstractAbility {

	private final Random random;

	public AbilitySmoking() {
		super("Smoking");
		setPerm("magic.smoking");
		random = new Random();
	}

	@Override
	public void castSpell(Player player) {
		if (!canUse(player)) {
			ChatManager.messageBad(player, "You are not capable of performing this spell.");
			return;
		}
		renderPlayerPartciles(player.getLocation().add(.5, .75, .5));
		Vector savedDirection = player.getLocation().getDirection();
		BlockIterator bi = new BlockIterator(player, 15);
		Block landingBlock = null;
		while (bi.hasNext()) {
			Block current = bi.next();
			landingBlock = current;
			if (current.getType().isBlock() && current.getType().isSolid()) {
				landingBlock = current;
				break;
			}
			//ParticleEffect.SMOKE_LARGE.display(savedDirection, 0.1f, current.getLocation().add(0.5, 0.5, 0.5), Bukkit.getOnlinePlayers());
		}
		if (landingBlock == null) {
			landingBlock = player.getLocation().getBlock();
		}
		Location landingLocation = landingBlock.getLocation().add(0.5, 1, 0.5);
		landingLocation.setDirection(savedDirection);
		player.teleport(landingLocation);
		player.getLocation().setDirection(savedDirection);
		renderPlayerPartciles(landingBlock.getLocation().add(.5, 1.5, .5));
		player.setFallDistance(0f); // Turn off teleport falling damage
	}

	private void renderPlayerPartciles(Location location) {
		for (int i = 0; i < 250; i++) {
			ParticleEffect.SMOKE_LARGE.display(getTrueRandom().multiply(1.5), 0.01f, location.clone().add(getTrueRandom()), Bukkit.getOnlinePlayers());
		}
		location.getWorld().playSound(location, Sound.ENTITY_WITHER_SHOOT, 1, 1);
	}

	private Vector getTrueRandom() {
		return new Vector((random.nextDouble() * 3) - 1, (random.nextDouble() * 3) - 1, (random.nextDouble() * 3) - 1);
	}
}
