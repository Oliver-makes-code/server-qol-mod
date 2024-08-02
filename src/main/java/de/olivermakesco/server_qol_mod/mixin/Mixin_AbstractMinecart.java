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

import java.util.List;

@Mixin(AbstractMinecart.class)
public abstract class Mixin_AbstractMinecart extends VehicleEntity {
    @Unique private static final int COUNT = 2;

    @Shadow private int lerpSteps;

    @Shadow private double lerpX;
    @Shadow private double lerpY;
    @Shadow private double lerpZ;

    @Shadow private double lerpYRot;

    @Shadow private double lerpXRot;

    @Shadow private boolean onRails;

    @Shadow protected abstract void moveAlongTrack(BlockPos blockPos, BlockState blockState);

    @Shadow public abstract void activateMinecart(int i, int j, int k, boolean bl);

    @Shadow protected abstract void comeOffTrack();

    @Shadow private boolean flipped;

    @Shadow public abstract AbstractMinecart.Type getMinecartType();

    public Mixin_AbstractMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
        throw new IllegalCallerException("h");
    }

    /**
     * @author Octal
     * @reason I'm hacking it together for the server. This should not be included if I release on mr.
     */
    @Overwrite
    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        this.checkBelowWorld();
        this.handlePortal();
        if (this.level().isClientSide) {
            if (this.lerpSteps > 0) {
                this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
                this.lerpSteps--;
            } else {
                this.reapplyPosition();
                this.setRot(this.getYRot(), this.getXRot());
            }
        } else {
            this.applyGravity();

            // CHANGED CODE {
                for (int n = 0;  n < COUNT; n++) {
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
            // }

            this.checkInsideBlocks();
            this.setXRot(0.0F);
            double d = this.xo - this.getX();
            double e = this.zo - this.getZ();
            if (d * d + e * e > 0.001) {
                this.setYRot((float)(Mth.atan2(e, d) * 180.0 / Math.PI));
                if (this.flipped) {
                    this.setYRot(this.getYRot() + 180.0F);
                }
            }

            double f = (double)Mth.wrapDegrees(this.getYRot() - this.yRotO);
            if (f < -170.0 || f >= 170.0) {
                this.setYRot(this.getYRot() + 180.0F);
                this.flipped = !this.flipped;
            }

            this.setRot(this.getYRot(), this.getXRot());
            if (this.getMinecartType() == AbstractMinecart.Type.RIDEABLE && this.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
                List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2F, 0.0, 0.2F), EntitySelector.pushableBy(this));
                if (!list.isEmpty()) {
                    for (Entity entity : list) {
                        if (!(entity instanceof Player) && !(entity instanceof IronGolem) && !(entity instanceof AbstractMinecart) && !this.isVehicle() && !entity.isPassenger()) {
                            entity.startRiding(this);
                        } else {
                            entity.push(this);
                        }
                    }
                }
            } else {
                for (Entity entity2 : this.level().getEntities(this, this.getBoundingBox().inflate(0.2F, 0.0, 0.2F))) {
                    if (!this.hasPassenger(entity2) && entity2.isPushable() && entity2 instanceof AbstractMinecart) {
                        entity2.push(this);
                    }
                }
            }

            this.updateInWaterStateAndDoFluidPushing();
            if (this.isInLava()) {
                this.lavaHurt();
                this.fallDistance *= 0.5F;
            }

            this.firstTick = false;
        }
    }
}
