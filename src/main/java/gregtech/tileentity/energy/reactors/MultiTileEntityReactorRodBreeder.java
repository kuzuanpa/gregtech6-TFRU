/**
 * Copyright (c) 2021 GregTech-6 Team
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

import gregapi.data.LH;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

import static gregapi.data.CS.*;

/**
 * @author Gregorius Techneticies
 */
public class MultiTileEntityReactorRodBreeder extends MultiTileEntityReactorRodBase {
	public long mDurability = 0;
	public short mProduct = -1;
	public int mNeutronLoss = 0;
	public String mProductName = "";

	@Override
	public void readFromNBT2(NBTTagCompound aNBT) {
		super.readFromNBT2(aNBT);
		mDurability = aNBT.getLong(aNBT.hasKey(NBT_DURABILITY) ? NBT_DURABILITY : NBT_MAXDURABILITY);
		if (aNBT.hasKey(NBT_NUCLEAR_LOSS)) mNeutronLoss = aNBT.getInteger(NBT_NUCLEAR_LOSS);
		if (aNBT.hasKey(NBT_VALUE)) mProduct = aNBT.getShort(NBT_VALUE);
	}

	@Override
	public void writeToNBT2(NBTTagCompound aNBT) {
		super.writeToNBT2(aNBT);
		UT.NBT.setNumber(aNBT, NBT_DURABILITY, mDurability);
	}

	@Override
	public NBTTagCompound writeItemNBT2(NBTTagCompound aNBT) {
		UT.NBT.setNumber(aNBT, NBT_DURABILITY, mDurability);
		return super.writeItemNBT2(aNBT);
	}

	@Override
	public void addToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
		aList.add(LH.Chat.DGRAY + LH.get(LH.TOOLTIP_NUCLEAR_ROD));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.breeder.0"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.breeder.1"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.breeder.2"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.breeder.3"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.breeder.4"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.breeder.5"));
		if (mProductName.equals("")) mProductName = ST.meta(aStack.copy(), mProduct).getDisplayName();
		aList.add(LH.Chat.GREEN + LH.get("gt.tooltip.nuclear.rod.breeder.6") + LH.Chat.WHITE + mProductName);
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.breeder.7") + LH.Chat.WHITE + mDurability + LH.Chat.PURPLE + " " + LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS));
		aList.add(LH.Chat.YELLOW + LH.get("gt.tooltip.nuclear.rod.breeder.8") + LH.Chat.WHITE + mNeutronLoss + LH.Chat.PURPLE + " "+ LH.get(LH.TOOLTIP_NUCLEAR_NEUTRONS));
	}

	static {
		LH.add("gt.tooltip.nuclear.rod.breeder.0", "Absorbs Neutrons to breed into an " + LH.Chat.WHITE + "Enriched Rod");
		LH.add("gt.tooltip.nuclear.rod.breeder.1", "Emits half the Heat per Neutron on this Rod");
		LH.add("gt.tooltip.nuclear.rod.breeder.2", "Can't breed with Neutrons from " + LH.Chat.RED + "Moderated" + LH.Chat.CYAN + " Fuel Rods");
		LH.add("gt.tooltip.nuclear.rod.breeder.3", "The " + LH.Chat.YELLOW + "Loss" + LH.Chat.CYAN + " value gets subtracted from Neutrons entering this Rod");
		LH.add("gt.tooltip.nuclear.rod.breeder.4", "This applies to each side where Neutrons enter, not to the total of all sides");
		LH.add("gt.tooltip.nuclear.rod.breeder.5", "Remaining Neutrons on this Rod get added to the breeding process");
		LH.add("gt.tooltip.nuclear.rod.breeder.6", "Turns into: ");
		LH.add("gt.tooltip.nuclear.rod.breeder.7", "Needed: ");
		LH.add("gt.tooltip.nuclear.rod.breeder.8", "Loss: ");
	}
	@Override
	public int getReactorRodNeutronEmission(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack) {
		return 0;
	}
	
	@Override
	public boolean getReactorRodNeutronReaction(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack) {
		aReactor.mEnergy += aReactor.oNeutronCounts[aSlot] / 2;
		mDurability -= aReactor.oNeutronCounts[aSlot];
		if (mDurability <= 0) {
			ST.meta(aStack, mProduct);
			ST.nbt(aStack, null);
			mDurability = 0;
			aReactor.updateClientData();
		}
		UT.NBT.set(aStack, writeItemNBT(aStack.hasTagCompound() ? aStack.getTagCompound() : UT.NBT.make()));
		return T;
	}
	
	@Override
	public int getReactorRodNeutronReflection(MultiTileEntityReactorCore aReactor, int aSlot, ItemStack aStack, int aNeutrons, boolean aModerated) {
		if (!aModerated && aNeutrons > mNeutronLoss) aReactor.mNeutronCounts[aSlot] += aNeutrons - mNeutronLoss;
		return 0;
	}
	
	@Override public String getTileEntityName() {return "gt.multitileentity.generator.reactor.rods.breeder";}
}
