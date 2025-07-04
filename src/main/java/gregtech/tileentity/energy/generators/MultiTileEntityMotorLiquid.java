/**
 * Copyright (c) 2025 GregTech-6 Team
 *
 * This file is part of GregTech.
 *
 * GregTech is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GregTech is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GregTech. If not, see <http://www.gnu.org/licenses/>.
 */

package gregtech.tileentity.energy.generators;

import gregapi.block.multitileentity.IWailaTile;
import gregapi.code.TagData;
import gregapi.data.FL;
import gregapi.data.FM;
import gregapi.data.LH;
import gregapi.data.LH.Chat;
import gregapi.data.TD;
import gregapi.fluid.FluidTankGT;
import gregapi.old.Textures;
import gregapi.recipes.Recipe;
import gregapi.recipes.Recipe.RecipeMap;
import gregapi.render.BlockTextureDefault;
import gregapi.render.BlockTextureMulti;
import gregapi.render.IIconContainer;
import gregapi.render.ITexture;
import gregapi.tileentity.ITileEntityFunnelAccessible;
import gregapi.tileentity.ITileEntityTapAccessible;
import gregapi.tileentity.base.TileEntityBase09FacingSingle;
import gregapi.tileentity.behavior.TE_Behavior_Active_Trinary;
import gregapi.tileentity.energy.ITileEntityEnergy;
import gregapi.tileentity.machines.ITileEntityAdjacentOnOff;
import gregapi.tileentity.machines.ITileEntityRunningActively;
import gregapi.util.UT;
import gregapi.util.WD;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static gregapi.data.CS.*;

/**
 * @author Gregorius Techneticies
 */
public class MultiTileEntityMotorLiquid extends TileEntityBase09FacingSingle implements IFluidHandler, ITileEntityFunnelAccessible, ITileEntityTapAccessible, ITileEntityEnergy, ITileEntityRunningActively, ITileEntityAdjacentOnOff, IWailaTile {
	public boolean mStopped = F;
	public short mEfficiency = 10000;
	public long mEnergy = 0, mRate = 32;
	public TagData mEnergyTypeEmitted = TD.Energy.RU;
	public RecipeMap mRecipes = FM.Engine;
	public Recipe mLastRecipe = null;
	public FluidTankGT[] mTanks = {new FluidTankGT(1000), new FluidTankGT(1000), new FluidTankGT(1000), new FluidTankGT(1000)}, mTanksOut = {mTanks[1], mTanks[2], mTanks[3]};
	public TE_Behavior_Active_Trinary mActivity = null;
	
	@Override
	public void readFromNBT2(NBTTagCompound aNBT) {
		super.readFromNBT2(aNBT);
		mEnergy = aNBT.getLong(NBT_ENERGY);
		mActivity = new TE_Behavior_Active_Trinary(this, aNBT);
		if (aNBT.hasKey(NBT_STOPPED)) mStopped = aNBT.getBoolean(NBT_STOPPED);
		if (aNBT.hasKey(NBT_OUTPUT)) mRate = aNBT.getLong(NBT_OUTPUT);
		if (aNBT.hasKey(NBT_FUELMAP)) mRecipes = RecipeMap.RECIPE_MAPS.get(aNBT.getString(NBT_FUELMAP));
		if (aNBT.hasKey(NBT_EFFICIENCY)) mEfficiency = (short)UT.Code.bind_(0, 10000, aNBT.getShort(NBT_EFFICIENCY));
		if (aNBT.hasKey(NBT_ENERGY_EMITTED)) mEnergyTypeEmitted = TagData.createTagData(aNBT.getString(NBT_ENERGY_EMITTED));
		mTanks[0].readFromNBT(aNBT, NBT_TANK+".0").setCapacity(mRate * 16);
		mTanks[1].readFromNBT(aNBT, NBT_TANK+".1").setCapacity(mRate * 128);
		mTanks[2].readFromNBT(aNBT, NBT_TANK+".2").setCapacity(mRate * 128);
		mTanks[3].readFromNBT(aNBT, NBT_TANK+".3").setCapacity(mRate * 128);
	}
	
	@Override
	public void writeToNBT2(NBTTagCompound aNBT) {
		super.writeToNBT2(aNBT);
		UT.NBT.setNumber(aNBT, NBT_ENERGY, mEnergy);
		UT.NBT.setBoolean(aNBT, NBT_STOPPED, mStopped);
		mActivity.save(aNBT);
		mTanks[0].writeToNBT(aNBT, NBT_TANK+".0");
		mTanks[1].writeToNBT(aNBT, NBT_TANK+".1");
		mTanks[2].writeToNBT(aNBT, NBT_TANK+".2");
		mTanks[3].writeToNBT(aNBT, NBT_TANK+".3");
	}
	
	@Override
	public void addToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
		aList.add(Chat.CYAN     + LH.get(LH.RECIPES) + ": " + Chat.WHITE + LH.get(mRecipes.mNameInternal));
		aList.add(LH.getToolTipEfficiency(mEfficiency));
		LH.addEnergyToolTips(this, aList, null, mEnergyTypeEmitted, null, LH.get(LH.FACE_FRONT));
		aList.add(Chat.ORANGE   + LH.get(LH.NO_GUI_FUNNEL_TAP_TO_TANK));
		aList.add(Chat.DGRAY    + LH.get(LH.TOOL_TO_DETAIL_MAGNIFYINGGLASS));
		super.addToolTips(aList, aStack, aF3_H);
	}
	
	@Override
	public void onTick2(long aTimer, boolean aIsServerSide) {
		if (aIsServerSide) {
			if (mEnergy >= mRate) {
				ITileEntityEnergy.Util.emitEnergyToNetwork(mEnergyTypeEmitted, mRate, 1, this);
				mEnergy -= mRate;
			}
			if (mEnergy < mRate * 2 && !mStopped) {
				mActivity.mActive = F;
				Recipe tRecipe = mRecipes.findRecipe(this, mLastRecipe, T, Long.MAX_VALUE, NI, mTanks[0].AS_ARRAY, ZL_IS);
				if (tRecipe != null) {
					if (canOutputFill(tRecipe.mFluidOutputs, mTanksOut)) {
						if (tRecipe.isRecipeInputEqual(T, F, mTanks[0].AS_ARRAY, ZL_IS)) {
							mActivity.mActive = T;
							mLastRecipe = tRecipe;
							mEnergy += UT.Code.units(tRecipe.getAbsoluteTotalPower(), 10000, mEfficiency, F);
							fillOutput(tRecipe.mFluidOutputs, mTanksOut);
							while (mEnergy < mRate * 2 && (canOutputFill(tRecipe.mFluidOutputs, mTanksOut)) && tRecipe.isRecipeInputEqual(T, F, mTanks[0].AS_ARRAY, ZL_IS)) {
								mEnergy += UT.Code.units(tRecipe.getAbsoluteTotalPower(), 10000, mEfficiency, F);
								fillOutput(tRecipe.mFluidOutputs, mTanksOut);
								if (mTanks[0].isEmpty()) break;
							}
						} else {
							// set remaining Fluid to null, in case the Fuel Type needs to be swapped out. But only if it was inactive for 64 ticks.
							if (mActivity.mData == 0) mTanks[0].setEmpty();
						}
					}
				} else {
					// set remaining Fluid to null, because it is not valid Fuel anymore for whatever reason. MineTweaker happens to live Modpacks too sometimes. ;)
					mTanks[0].setEmpty();
				}
			}
			if (mEnergy < 0) mEnergy = 0;

            for (FluidTankGT tank : mTanksOut) {
                FL.move(tank, getAdjacentTank(OPOS[mFacing]));
                if (FL.gas(tank) && !WD.hasCollide(worldObj, getOffset(OPOS[mFacing], 1))) {
					tank.setEmpty();
                }
            }
        } else {
            if (mActivity.mState != 0 && WD.random(this, 20, CLIENT_TIME)) UT.Sounds.play(SFX.MC_MINECART, 1, 0.5F, getCoords());
        }
	}

	public boolean canOutputFill(FluidStack[] outputs, FluidTankGT[] tanks){
		return Arrays.stream(outputs).allMatch(output -> Arrays.stream(tanks).anyMatch(tank -> tank.canFillAll(output)));
	}


	public void fillOutput(FluidStack[] outputs, FluidTankGT[] tanks){
		Arrays.stream(outputs).forEach(output -> Arrays.stream(tanks).anyMatch(tank -> tank.canFillAll(output) && tank.fill(output) > 0));
    }

	@Override
	public long onToolClick2(String aTool, long aRemainingDurability, long aQuality, Entity aPlayer, List<String> aChatReturn, IInventory aPlayerInventory, boolean aSneaking, ItemStack aStack, byte aSide, float aHitX, float aHitY, float aHitZ) {
		long rReturn = super.onToolClick2(aTool, aRemainingDurability, aQuality, aPlayer, aChatReturn, aPlayerInventory, aSneaking, aStack, aSide, aHitX, aHitY, aHitZ);
		if (rReturn > 0) return rReturn;
		
		if (isClientSide()) return 0;
		
		if (aTool.equals(TOOL_plunger)) {
			if (mTanks[1].has()) return GarbageGT.trash(mTanks[1]);
			return GarbageGT.trash(mTanks[0]);
		}
		
		if (aTool.equals(TOOL_magnifyingglass)) {
			if (aChatReturn != null) {
				aChatReturn.add("Input: "  + mTanks[0].content());
				aChatReturn.add("Output: " + mTanks[1].content());
			}
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean onTickCheck(long aTimer) {
		return mActivity.check(mStopped) || super.onTickCheck(aTimer);
	}
	
	@Override
	public void setVisualData(byte aData) {
		mActivity.mState = (byte)(aData & 127);
	}
	
	@Override public byte getVisualData() {return mActivity.mState;}
	@Override public byte getDefaultSide() {return SIDE_FRONT;}
	@Override public boolean[] getValidSides() {return SIDES_VALID;}
	
	@Override
	protected IFluidTank getFluidTankFillable2(byte aSide, FluidStack aFluidToFill) {
		return mRecipes.containsInput(aFluidToFill, this, NI) ? mTanks[0] : null;
	}
	
	@Override
	protected IFluidTank getFluidTankDrainable2(byte aSide, FluidStack aFluidToDrain) {
		return mTanks[1];
	}
	
	@Override
	protected IFluidTank[] getFluidTanks2(byte aSide) {
		return mTanks;
	}
	
	@Override
	public int funnelFill(byte aSide, FluidStack aFluid, boolean aDoFill) {
		if (!mRecipes.containsInput(aFluid, this, NI)) return 0;
		updateInventory();
		return mTanks[0].fill(aFluid, aDoFill);
	}
	
	@Override
	public FluidStack tapDrain(byte aSide, int aMaxDrain, boolean aDoDrain) {
		updateInventory();
		return mTanks[mTanks[1].has() ? 1 : 0].drain(aMaxDrain, aDoDrain);
	}
	
	@Override
	public ITexture getTexture2(Block aBlock, int aRenderPass, byte aSide, boolean[] aShouldSideBeRendered) {
		if (!aShouldSideBeRendered[aSide]) return null;
		if (aSide == mFacing)              return BlockTextureMulti.get(BlockTextureDefault.get(sColoreds[0], mRGBa), BlockTextureDefault.get((mActivity.mState>0?sOverlaysActive:sOverlays)[0]));
		if (aSide == OPOS[mFacing])   return BlockTextureMulti.get(BlockTextureDefault.get(sColoreds[1], mRGBa), BlockTextureDefault.get((mActivity.mState>0?sOverlaysActive:sOverlays)[1]));
										   return BlockTextureMulti.get(BlockTextureDefault.get(sColoreds[2], mRGBa), BlockTextureDefault.get((mActivity.mState>0?sOverlaysActive:sOverlays)[2]));
	}
	
	@Override public ItemStack[] getDefaultInventory(NBTTagCompound aNBT) {return ZL_IS;}
	@Override public boolean canDrop(int aInventorySlot) {return T;}
	
	@Override public boolean isEnergyType(TagData aEnergyType, byte aSide, boolean aEmitting) {return aEmitting && aEnergyType == mEnergyTypeEmitted;}
	@Override public boolean isEnergyEmittingTo(TagData aEnergyType, byte aSide, boolean aTheoretical) {return aSide == mFacing && super.isEnergyEmittingTo(aEnergyType, aSide, aTheoretical);}
	@Override public long getEnergyOffered(TagData aEnergyType, byte aSide, long aSize) {return Math.min(mRate, mEnergy);}
	@Override public long getEnergySizeOutputRecommended(TagData aEnergyType, byte aSide) {return mRate;}
	@Override public long getEnergySizeOutputMin(TagData aEnergyType, byte aSide) {return mRate;}
	@Override public long getEnergySizeOutputMax(TagData aEnergyType, byte aSide) {return mRate;}
	@Override public Collection<TagData> getEnergyTypes(byte aSide) {return mEnergyTypeEmitted.AS_LIST;}
	
	@Override public boolean getStateRunningPassively() {return mActivity.mActive;}
	@Override public boolean getStateRunningPossible() {return mActivity.mActive || (mTanks[0].has() && !mTanks[1].isFull());}
	@Override public boolean getStateRunningActively() {return mActivity.mActive;}
	@Override public boolean setAdjacentOnOff(boolean aOnOff) {mStopped = !aOnOff; return !mStopped;}
	@Override public boolean setStateOnOff(boolean aOnOff) {mStopped = !aOnOff; return !mStopped;}
	@Override public boolean getStateOnOff() {return !mStopped;}
	
	// Icons
	public static IIconContainer[] sColoreds = new IIconContainer[] {
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/colored/front"),
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/colored/back"),
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/colored/sides"),
	}, sOverlays = new IIconContainer[] {
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/overlay/front"),
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/overlay/back"),
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/overlay/sides"),
	}, sOverlaysActive = new IIconContainer[] {
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/overlay_active/front"),
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/overlay_active/back"),
		new Textures.BlockIcons.CustomIcon("machines/generators/motor_liquid/overlay_active/sides"),
	};

	@Override
	public IWailaInfoProvider[] getWailaInfos() {
		return new IWailaInfoProvider[] {IWailaTile.instanceInfoState, IWailaTile.instanceInfoEnergyIORange};
	}

	@Override
	public NBTTagCompound getWailaNBT(TileEntity te, NBTTagCompound aNBT) {
		IWailaTile.super.getWailaNBT(te, aNBT);
		mTanks[0].writeToNBT(aNBT, NBT_TANK+".0");
		mTanks[1].writeToNBT(aNBT, NBT_TANK+".1");
		mTanks[2].writeToNBT(aNBT, NBT_TANK+".2");
		mTanks[3].writeToNBT(aNBT, NBT_TANK+".3");
		return aNBT;
	}
	@Override
	public List<String> getWailaBody(List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		IWailaTile.super.getWailaBody(currentTip, accessor, config);
		NBTTagCompound aNBT = accessor.getNBTData();
		mTanks[0].readFromNBT(aNBT, NBT_TANK+".0").setCapacity(mRate * 16);
		mTanks[1].readFromNBT(aNBT, NBT_TANK+".1").setCapacity(mRate * 128);
		mTanks[2].readFromNBT(aNBT, NBT_TANK+".2").setCapacity(mRate * 128);
		mTanks[3].readFromNBT(aNBT, NBT_TANK+".3").setCapacity(mRate * 128);
		for (int i = 0; i < mTanks.length; i++) IWailaTile.addTankDesc(currentTip,LH.get(LH.CONTENT)+(i+1)+" ",mTanks[i],"");
		return currentTip;
	}

	@Override public String getTileEntityName() {return "gt.multitileentity.generator.motor_liquid";}
}
