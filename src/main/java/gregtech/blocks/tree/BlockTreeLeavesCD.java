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

package gregtech.blocks.tree;

import gregapi.block.tree.BlockBaseLeaves;
import gregapi.data.CS.BlocksGT;
import gregapi.data.LH;
import gregapi.data.OP;
import gregapi.old.Textures;
import gregapi.util.OM;
import gregapi.util.ST;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockTreeLeavesCD extends BlockBaseLeaves {
	public BlockTreeLeavesCD(String aUnlocalised, Block aSaplings) {
		super(null, aUnlocalised, Material.leaves, soundTypeGrass, 1, Textures.BlockIcons.LEAVES_CD, aSaplings, new Block[] {BlocksGT.LogC, BlocksGT.LogC, BlocksGT.LogC, BlocksGT.LogC, BlocksGT.LogD, BlocksGT.LogD, BlocksGT.LogD, BlocksGT.LogD}, new byte[] {0, 1, 2, 3, 0, 1, 2, 3});
		LH.add(getUnlocalizedName()+ ".0.name", "Blue Spruce Leaves");
	//  LH.add(getUnlocalizedName()+ ".1.name", " Leaves");
	//  LH.add(getUnlocalizedName()+ ".2.name", " Leaves");
	//  LH.add(getUnlocalizedName()+ ".3.name", " Leaves");
	//  LH.add(getUnlocalizedName()+ ".4.name", " Leaves");
	//  LH.add(getUnlocalizedName()+ ".5.name", " Leaves");
	//  LH.add(getUnlocalizedName()+ ".6.name", " Leaves");
	//  LH.add(getUnlocalizedName()+ ".7.name", " Leaves");
		LH.add(getUnlocalizedName()+ ".8.name", "Blue Spruce Leaves");
	//  LH.add(getUnlocalizedName()+ ".9.name", " Leaves");
	//  LH.add(getUnlocalizedName()+".10.name", " Leaves");
	//  LH.add(getUnlocalizedName()+".11.name", " Leaves");
	//  LH.add(getUnlocalizedName()+".12.name", " Leaves");
	//  LH.add(getUnlocalizedName()+".13.name", " Leaves");
	//  LH.add(getUnlocalizedName()+".14.name", " Leaves");
	//  LH.add(getUnlocalizedName()+".15.name", " Leaves");
		
		for (int i = 0; i < 16; i++) OM.reg(ST.make(this, 1, i), OP.treeLeaves);
	}
	
	@Override
	public int getLeavesRangeSide(byte aMeta) {
		switch(aMeta) {
		case  0: case  8: return 5;
		default:          return 3;
		}
	}
	
	@Override
	public int getLeavesRangeYNeg(byte aMeta) {
		switch(aMeta) {
		default:          return 2;
		}
	}
	
	@Override public int getLeavesRangeYPos(byte aMeta) {return 0;} // There is no instance where Leaves are below the Logs for these Trees.
}
