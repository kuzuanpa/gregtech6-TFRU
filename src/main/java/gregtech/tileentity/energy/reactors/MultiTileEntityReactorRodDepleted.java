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
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Gregorius Techneticies
 */
public class MultiTileEntityReactorRodDepleted extends MultiTileEntityReactorRodBase {
	@Override
	public void addToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
		aList.add(LH.Chat.DGRAY + LH.get(LH.TOOLTIP_NUCLEAR_ROD));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.depleted.0"));
		aList.add(LH.Chat.CYAN + LH.get("gt.tooltip.nuclear.rod.depleted.1"));
	}

	static {
		LH.add("gt.tooltip.nuclear.rod.depleted.0", "This Rod is " + LH.Chat.RED + "Depleted" + LH.Chat.CYAN + " and will not output or accept any Neutrons");
		LH.add("gt.tooltip.nuclear.rod.depleted.1", "Can be centrifuged to get valuable materials");
	}
	
	@Override public String getTileEntityName() {return "gt.multitileentity.generator.reactor.rods.depleted";}
}
