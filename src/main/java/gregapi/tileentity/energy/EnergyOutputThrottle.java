/**
 * Copyright (c) 2026 GregTech-6 Team
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

package gregapi.tileentity.energy;

import gregapi.data.LH;
import gregapi.data.LH.Chat;
import gregapi.util.UT;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

import static gregapi.data.CS.NBT_OUTPUT_MAX;
import static gregapi.data.CS.NBT_OUTPUT_MIN;

public final class EnergyOutputThrottle {
	public static final int STEPS = 8;
	
	private EnergyOutputThrottle() {
		//
	}
	
	public static long minimum(long aMaximum) {
		return aMaximum <= 1 ? aMaximum : Math.max(1L, aMaximum / STEPS);
	}
	
	public static long read(NBTTagCompound aNBT, long aMaximum) {
		if (!aNBT.hasKey(NBT_OUTPUT_MAX)) return aMaximum;
		return UT.Code.bind_(readMinimum(aNBT, aMaximum), aMaximum, aNBT.getLong(NBT_OUTPUT_MAX));
	}
	
	public static long readMinimum(NBTTagCompound aNBT, long aMaximum) {
		if (!aNBT.hasKey(NBT_OUTPUT_MIN)) return minimum(aMaximum);
		return UT.Code.bind_(0, aMaximum, aNBT.getLong(NBT_OUTPUT_MIN));
	}
	
	public static void write(NBTTagCompound aNBT, long aMinimum, long aCurrent) {
		UT.NBT.setNumber(aNBT, NBT_OUTPUT_MIN, aMinimum);
		UT.NBT.setNumber(aNBT, NBT_OUTPUT_MAX, aCurrent);
	}
	
	public static long adjust(long aMinimum, long aCurrent, long aMaximum, boolean aSneaking) {
		if (aMaximum <= aMinimum) return aMaximum;
		int tStep = (int)Math.round((aCurrent - aMinimum) * (double)(STEPS - 1) / (aMaximum - aMinimum));
		tStep = (int)UT.Code.bind(0, STEPS - 1, tStep + (aSneaking ? 1 : -1));
		return aMinimum + (aMaximum - aMinimum) * tStep / (STEPS - 1);
	}
	
	public static void addToolTip(List<String> aList) {
		aList.add(Chat.DGRAY + LH.get(LH.TOOL_TO_ADJUST_THROTTLE));
	}
}
