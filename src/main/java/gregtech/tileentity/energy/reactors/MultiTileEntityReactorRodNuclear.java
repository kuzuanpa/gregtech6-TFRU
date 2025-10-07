/**
 * Copyright (c) 2022 GregTech-6 Team
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

package gregtech.tileentity.energy.reactors;

import gregapi.data.FL;
import gregapi.data.LH;
import gregapi.data.MT;
import gregapi.render.BlockTextureDefault;
import gregapi.render.BlockTextureMulti;
import gregapi.render.ITexture;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

import static gregapi.data.CS.*;
import static gregapi.data.LH.TOOLTIP_NUCLEAR_ROD;

/**
 * @author Gregorius Techneticies
 */
public class MultiTileEntityReactorRodNuclear extends MultiTileEntityReactorRodBase {
	public long mDurability = 0;
	public int mNeutronSelf = 128, mNeutronOther = 128, mNeutronDiv = 8, mNeutronMax = 128;
	public short mDepleted = -1;
	public boolean mModerated = F, oModerated = F;
	
	@Override
	public void readFromNBT2(NBTTagCompound aNBT) {
		super.readFromNBT2(aNBT);
		mDurability = aNBT.getLong(aNBT.hasKey(NBT_DURABILITY) ? NBT_DURABILITY : NBT_MAXDURABILITY);
		if (aNBT.hasKey(NBT_NUCLEAR_SELF    )) mNeutronSelf  = aNBT.getInteger(NBT_NUCLEAR_SELF );
		if (aNBT.hasKey(NBT_NUCLEAR_OTHER   )) mNeutronOther = aNBT.getInteger(NBT_NUCLEAR_OTHER);
		if (aNBT.hasKey(NBT_NUCLEAR_DIV     )) mNeutronDiv   = aNBT.getInteger(NBT_NUCLEAR_DIV  );
		if (aNBT.hasKey(NBT_NUCLEAR_MAX     )) mNeutronMax   = aNBT.getInteger(NBT_NUCLEAR_MAX);
		if (aNBT.hasKey(NBT_NUCLEAR_MOD     )) mModerated    = aNBT.getBoolean(NBT_NUCLEAR_MOD);
		if (aNBT.hasKey(NBT_NUCLEAR_MOD+".o")) oModerated    = aNBT.getBoolean(NBT_NUCLEAR_MOD+".o");
		if (aNBT.hasKey(NBT_VALUE           )) mDepleted     = aNBT.getShort(NBT_VALUE);
	}
	
	@Override
	public void writeToNBT2(NBTTagCompound aNBT) {
		super.writeToNBT2(aNBT);
		UT.NBT.setNumber(aNBT, NBT_DURABILITY, mDurability);
		UT.NBT.setBoolean(aNBT, NBT_NUCLEAR_MOD, mModerated);
		UT.NBT.setBoolean(aNBT, NBT_NUCLEAR_MOD+".o", oModerated);
	}
	
	@Override
	public NBTTagCompound writeItemNBT2(NBTTagCompound aNBT) {
		UT.NBT.setNumber(aNBT, NBT_DURABILITY, mDurability);
		UT.NBT.setBoolean(aNBT, NBT_NUCLEAR_MOD, mModerated);
		UT.NBT.setBoolean(aNBT, NBT_NUCLEAR_MOD+".o", oModerated);
		return super.writeItemNBT2(aNBT);
	}
	
	@Override
	public void addToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
		aList.add(LH.Chat.DGRAY + LH.get(TOOLTIP_NUCLEAR_ROD));
		aList.add(LH.Chat.GREEN + " " + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION)+ LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.emission"));
		aList.add(LH.Chat.GREEN + " " + LH.get(LH.TOOLTIP_NUCLEAR_SELF)+ LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.self"));
		aList.add(LH.Chat.GREEN + " " + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM)+ LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.maximum"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.factor.0") + LH.Chat.YELLOW +  LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.factor.1"));
		if (mModerated || oModerated) aList.add(LH.Chat.DBLUE + LH.get("gt.tooltip.nuclear.rod.nuclear.moderated"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.remain") + LH.Chat.WHITE + (mDurability / 120000) + LH.Chat.PURPLE + " NU");
		switch ((int) ((CLIENT_TIME / 100) % 10)) {
			case 0:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.HDO.mLiquid.getLocalizedName() +"/"+  FL.DistW.make(0).getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + mNeutronOther + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + mNeutronMax + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + mNeutronDiv);
				aList.add(LH.Chat.GREEN + LH.get("gt.tooltip.nuclear.rod.nuclear.moderated.coolant"));
				if (mNeutronDiv <= 4) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 1:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.D2O.mLiquid.getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + mNeutronOther + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + UT.Code.divup(mNeutronMax, 8) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + mNeutronDiv);
				aList.add(LH.Chat.GREEN + LH.get("gt.tooltip.nuclear.rod.nuclear.moderated.coolant"));
				if (mNeutronDiv <= 4) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 2:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.T2O.mLiquid.getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + mNeutronOther + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + UT.Code.divup(mNeutronMax, 16) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + mNeutronDiv);
				aList.add(LH.Chat.GREEN + LH.get("gt.tooltip.nuclear.rod.nuclear.moderated.coolant"));
				if (mNeutronDiv <= 4) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 3:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.Sn.mLiquid.getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + mNeutronOther + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + mNeutronMax + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + (mNeutronDiv - 1));
				aList.add(LH.Chat.GREEN + "1/3 "+LH.get("gt.tooltip.nuclear.rod.nuclear.heat"));
				if (mNeutronDiv <= 5) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 4:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.Na.mLiquid.getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + mNeutronOther + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + mNeutronMax + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + (mNeutronDiv - 1));
				aList.add(LH.Chat.GREEN + "1/6 "+LH.get("gt.tooltip.nuclear.rod.nuclear.heat"));
				if (mNeutronDiv <= 5) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 5:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + FL.Coolant_IC2.make(0).getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + mNeutronOther * 4 + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf * 4 + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + mNeutronMax + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + mNeutronDiv * 2);
				if (mNeutronDiv <= 2) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 6:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.LiCl.mLiquid.getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + (mNeutronOther - UT.Code.divup(mNeutronOther, 2)) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + (mNeutronSelf * 5) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + (mNeutronMax + UT.Code.divup(mNeutronMax, 4)) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + mNeutronDiv);
				if (mNeutronDiv <= 4) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 7:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + FL.Thorium_Salt.make(0).getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + (mNeutronOther - UT.Code.divup(mNeutronOther, 2)) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + (mNeutronSelf * 0) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + (mNeutronMax * 4) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + (mNeutronDiv - 1));
				if (mNeutronDiv <= 5) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 8:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.CO2.mGas.getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + mNeutronOther + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf * 3 + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + mNeutronMax + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + mNeutronDiv);
				if (mNeutronDiv <= 4) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
			case 9:
				aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.nuclear.used_with") + MT.He.mGas.getLocalizedName());
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_EMISSION) + ": " + LH.Chat.WHITE + (mNeutronOther - UT.Code.divup(mNeutronOther, 2)) + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_SELF) + ": " + LH.Chat.WHITE + mNeutronSelf + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.GREEN + LH.get(LH.TOOLTIP_NUCLEAR_MAXIMUM) + ": " + LH.Chat.WHITE + mNeutronMax + LH.Chat.PURPLE + " "+LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS)+"/t");
				aList.add(LH.Chat.YELLOW + LH.get(LH.TOOLTIP_NUCLEAR_FACTOR) + ": " + LH.Chat.WHITE + "1/" + mNeutronDiv);
				if (mNeutronDiv <= 4) aList.add(LH.Chat.RED + LH.get("gt.tooltip.nuclear.rod.nuclear.critical") + LH.Chat.BLINKING_RED + " "+ LH.get(LH.TOOLTIP_NUCLEAR_CRITICAL));
				break;
		}
	}


	static {
		LH.add("gt.tooltip.nuclear.rod.nuclear.emission",  " describes how many Neutrons are emitted to adjacent Rods");
		LH.add("gt.tooltip.nuclear.rod.nuclear.self",  " describes how many Neutrons naturally onto this Rod");
		LH.add("gt.tooltip.nuclear.rod.nuclear.maximum",  " describes how many Neutrons can be on this Rod while lasting the advertised duration");
		LH.add("gt.tooltip.nuclear.rod.nuclear.factor.0",  "A greater ");
		LH.add("gt.tooltip.nuclear.rod.nuclear.factor.1", " means the Rod emits more extra Neutrons for the amount of Neutrons on it" );
		LH.add("gt.tooltip.nuclear.rod.nuclear.moderated", "This Fuel is " + LH.Chat.WHITE + "Moderated");
		LH.add("gt.tooltip.nuclear.rod.nuclear.moderated.coolant", "Fuel rods will be " + LH.Chat.WHITE + "Moderated");
		LH.add("gt.tooltip.nuclear.rod.nuclear.remain", "Remaining: ");
		LH.add("gt.tooltip.nuclear.rod.nuclear.used_with", "When used with ");
		LH.add("gt.tooltip.nuclear.rod.nuclear.critical", "This Fuel is");
		LH.add("gt.tooltip.nuclear.rod.nuclear.heat", "the Heat per Neutron");
	}
	@Override
	// Gets called every 20 Ticks.
	public int getReactorRodNeutronEmission(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack) {
		int tNeutronOther = mNeutronOther;
		int tNeutronSelf = mNeutronSelf;
		int tNeutronDiv = mNeutronDiv;
		if (FL.Coolant_IC2.is(aReactor.mTanks[0])) {
			tNeutronOther *= 4;
			tNeutronSelf *= 4;
			tNeutronDiv *= 2;
		} else if (MT.CO2.mGas.isFluidEqual(aReactor.mTanks[0].getFluid())) {
			tNeutronSelf *= 3;
		} else if (MT.He.mGas.isFluidEqual(aReactor.mTanks[0].getFluid())) {
			tNeutronOther -= UT.Code.divup(mNeutronOther, 2);
		} else if (MT.LiCl.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid())) {
			tNeutronOther -= UT.Code.divup(mNeutronOther, 2);
			tNeutronSelf *= 5;
		} else if (FL.Thorium_Salt.is(aReactor.mTanks[0])) {
			tNeutronOther -= UT.Code.divup(mNeutronOther, 2);
			tNeutronSelf = 0;
			tNeutronDiv -= 1;
		} else if (MT.Sn.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid()) || MT.Na.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid())) {
			tNeutronDiv -= 1;
		}
		aReactor.mNeutronCounts[aSlot] += tNeutronSelf;
		long tEmission = tNeutronOther + UT.Code.divup(Math.max(aReactor.oNeutronCounts[aSlot]-tNeutronSelf, 0), tNeutronDiv);
		return UT.Code.bindInt(tEmission);
	}
	
	@Override
	// Gets called every Tick.
	public boolean getReactorRodNeutronReaction(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack) {
		aReactor.mEnergy += aReactor.oNeutronCounts[aSlot];
		int tNeutronMax = getReactorRodNeutronMaximum(aReactor, aSlot, aStack);

		if (FL.distw(aReactor.mTanks[0]) ||
			MT.HDO.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid()) ||
			MT.D2O.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid()) ||
			MT.T2O.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid()))
		{
			mModerated = oModerated = T;
		}
		long tDurabilityLoss = aReactor.oNeutronCounts[aSlot] <= tNeutronMax ? 100 : UT.Code.divup(400 * aReactor.oNeutronCounts[aSlot], tNeutronMax);
		if (oModerated) tDurabilityLoss *= 4;
		mDurability = tDurabilityLoss > mDurability ? -1 : mDurability - tDurabilityLoss;
		UT.NBT.set(aStack, writeItemNBT(aStack.hasTagCompound() ? aStack.getTagCompound() : UT.NBT.make()));

		if (mDurability <= 0) {
			ST.meta(aStack, mDepleted);
			ST.nbt(aStack, null);
			aReactor.updateClientData();
		}
		return T;
	}
	
	@Override
	// Gets called every 20 Ticks.
	public int getReactorRodNeutronReflection(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack, int aNeutrons, boolean aModerated) {
		if (aModerated) {
			mModerated = T;
			UT.NBT.set(aStack, writeItemNBT(aStack.hasTagCompound() ? aStack.getTagCompound() : UT.NBT.make()));
		}
		aReactor.mNeutronCounts[aSlot] += aNeutrons;
		return 0;
	}

	@Override
	public int getReactorRodNeutronMaximum(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack) {
		if (MT.LiCl.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid())) {
			return mNeutronMax + (int) UT.Code.divup(mNeutronMax, 4);
		} else if (FL.Thorium_Salt.is(aReactor.mTanks[0])) {
			return mNeutronMax * 4;
		} else if (MT.D2O.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid())) {
			return (int) UT.Code.divup(mNeutronMax, 8);
		} else if (MT.T2O.mLiquid.isFluidEqual(aReactor.mTanks[0].getFluid())) {
			return (int) UT.Code.divup(mNeutronMax, 16);
		} else {
			return mNeutronMax;
		}
	}

	@Override
	public boolean isModerated(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack) {
		return oModerated;
	}

	@Override
	public void updateModeration(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack) {
		oModerated = mModerated;
		mModerated = F;
		UT.NBT.set(aStack, writeItemNBT(aStack.hasTagCompound() ? aStack.getTagCompound() : UT.NBT.make()));
	}

	@Override public ITexture getReactorRodTextureSides(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack, boolean aActive) {return BlockTextureMulti.get(BlockTextureDefault.get(sColoreds[1], mRGBa, T), BlockTextureDefault.get(sOverlays[1], aActive ? UNCOLOURED : MT.Pb.fRGBaSolid));}
	@Override public ITexture getReactorRodTextureTop  (MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack, boolean aActive) {return BlockTextureMulti.get(BlockTextureDefault.get(sColoreds[0], mRGBa, T), BlockTextureDefault.get(sOverlays[0], aActive ? UNCOLOURED : MT.Pb.fRGBaSolid));}
	
	@Override public String getTileEntityName() {return "gt.multitileentity.generator.reactor.rods.nuclear";}
}
