package de.olivermakesco.server_qol_mod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class Mixin_AbstractMinecart extends VehicleEntity {
    @Unique private static final int COUNT = 2;

    @Shadow private boolean onRails;

    @Shadow protected abstract void moveAlongTrack(BlockPos blockPos, BlockState blockState);

    @Shadow public abstract void activateMinecart(int i, int j, int k, boolean bl);

    @Shadow protected abstract void comeOffTrack();

    public Mixin_AbstractMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
        throw new IllegalCallerException("h");
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;checkInsideBlocks()V",
                    shift = At.Shift.BEFORE
            )
    )
    private void moveAgain(CallbackInfo ci) {
        for (int n = 0;  n < COUNT-1; n++) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY());
            int k = Mth.floor(this.getZ());
            if (this.level().getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS)) {
                j--;
            }

            BlockPos blockPos = new BlockPos(i, j, k);
            BlockState blockState = this.level().getBlockState(blockPos);
            this.onRails = BaseRailBlock.isRail(blockState);
            if (this.onRails) {
                this.moveAlongTrack(blockPos, blockState);
                if (blockState.is(Blocks.ACTIVATOR_RAIL)) {
                    this.activateMinecart(i, j, k, (Boolean)blockState.getValue(PoweredRailBlock.POWERED));
                }
            } else {
                this.comeOffTrack();
            }
        }
    }
}
