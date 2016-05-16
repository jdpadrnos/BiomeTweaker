package me.superckl.biometweaker.server.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.primitives.Ints;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class CommandSetBiome implements ICommand{

	private final List<String> aliases = Arrays.asList("btsetbiome", "biometweakersetbiome", "bts", "biometweakers");

	@Override
	public int compareTo(final ICommand c) {
		return this.getCommandName().compareTo(c.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTSetBiome";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return "biometweaker.msg.setbiome.usage.text";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_) {
		return false;
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		final BlockPos coord = sender.getPosition();
		final World world = sender.getEntityWorld();
		if((coord != null) && (world != null)){
			if((args.length < 2) || (args.length > 3)){
				sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new Style().setColor(TextFormatting.RED)));
				return;
			}
			BiomeGenBase gen = null;
			Integer i = Ints.tryParse(args[0]);
			if(i != null)
				gen = BiomeGenBase.getBiome(i);
			else{
				final Iterator<BiomeGenBase> it = BiomeGenBase.biomeRegistry.iterator();
				while(it.hasNext()){
					final BiomeGenBase biome = it.next();
					if((biome != null) && biome.getBiomeName().equals(args[0])){
						gen = biome;
						break;
					}
				}
			}
			if(gen == null){
				sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new Style().setColor(TextFormatting.RED)));
				return;
			}
			final int id = BiomeGenBase.getIdForBiome(gen);
			i = Ints.tryParse(args[1]);
			if(i == null){
				sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new Style().setColor(TextFormatting.RED)));
				return;
			}
			boolean blocks = true;
			if(args.length == 3)
				if(args[2].equalsIgnoreCase("block"))
					blocks = true;
				else if(args[2].equalsIgnoreCase("chunk"))
					blocks = false;
				else{
					sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new Style().setColor(TextFormatting.RED)));
					return;
				}
			int count = 0;
			if(blocks){
				for(int x = coord.getX()-i; x <= (coord.getX()+i); x++)
					for(int z = coord.getZ()-i; z <= (coord.getZ()+i); z++){
						final int realX = x & 15;
						final int realZ = z & 15;
						/*if(x < 0)
							realX = 15-realX;
						if(z < 0)
							realZ = 15-realZ;*/
						final Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
						chunk.getBiomeArray()[(realZ*16)+realX] = (byte) id;
						chunk.setChunkModified();
						count++;
					}
				sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.setbiome.blocksuccess.text", count, gen.getBiomeName()).setChatStyle(new Style().setColor(TextFormatting.GOLD)));
			}else{
				final byte[] biomeArray = new byte[256];
				Arrays.fill(biomeArray, (byte) id);
				final int chunkX = coord.getX() >> 4;
					final int chunkZ = coord.getZ() >> 4;
					for(int x = chunkX-i; x <= (chunkX+i); x++)
						for(int z = chunkZ-i; z <= (chunkZ+i); z++){
							final Chunk chunk = world.getChunkFromChunkCoords(x, z);
							chunk.setBiomeArray(Arrays.copyOf(biomeArray, biomeArray.length));
							chunk.setChunkModified();
							count++;
						}
					sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.setbiome.chunksuccess.text", count, gen.getBiomeName()).setChatStyle(new Style().setColor(TextFormatting.GOLD)));
			}
		}else
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.info.invalsender.text").setChatStyle(new Style().setColor(TextFormatting.RED)));
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return sender.canCommandSenderUseCommand(server.getOpPermissionLevel(), this.getCommandName());
	}

	@Override
	public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args,
			final BlockPos pos) {
		return null;
	}

}
