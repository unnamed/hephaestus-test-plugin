package team.unnamed.hephaestus.plugin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.v1_18_R2.MinecraftModelEntity;

public class CustomModelEntity extends MinecraftModelEntity {

    public CustomModelEntity(Level world, Model model) {
        super(EntityType.SLIME, world, model);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

}
