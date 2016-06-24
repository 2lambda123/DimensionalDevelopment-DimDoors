package com.zixiken.dimdoors.items;

import com.zixiken.dimdoors.DimDoors;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemGoldDoor extends ItemDoor {
	public static final String ID = "itemGoldDoor";

	public ItemGoldDoor() {
		super(DimDoors.goldenDoor);
		setMaxStackSize(16);
        setUnlocalizedName(ID);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(DimDoors.modid + ":" + this.getUnlocalizedName().replace("item.", ""));
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (par7 != 1)
		{
			return false;
		}
		else
		{
			++par5;
			Block block = DimDoors.goldenDoor;

			if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack))
			{
				if (!block.canPlaceBlockAt(par3World, par4, par5, par6))
				{
					return false;
				}
				else
				{
					int i1 = MathHelper.floor_double((par2EntityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
					placeDoorBlock(par3World, par4, par5, par6, i1, block);
					--par1ItemStack.stackSize;
					return true;
				}
			}
			else
			{
				return false;
			}
		}
	}
}