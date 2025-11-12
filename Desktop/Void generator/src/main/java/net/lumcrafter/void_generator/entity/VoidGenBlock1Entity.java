package net.lumcrafter.void_generator.entity;

import net.lumcrafter.void_generator.network.ModMessages;
import net.lumcrafter.void_generator.network.StopSoundPacket;
import net.lumcrafter.void_generator.screen.VoidGenMenu;
import net.lumcrafter.void_generator.util.ModUtil;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.lumcrafter.void_generator.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class VoidGenBlock1Entity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private boolean markedfordeath = false;
    private static final int OUTPUT_SLOT = 0;
    private static final ArrayList<ChunkPos> chunklist = new ArrayList<>();
    private boolean firstplaced;
    protected final ContainerData data;
    private int explodingprogress = 0;
    private int maxexplodingprogress = 560;
    private int progress = 0;
    private int maxProgress = 1200;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    public VoidGenBlock1Entity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.VOID_GEN_I.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> VoidGenBlock1Entity.this.progress;
                    case 1 -> VoidGenBlock1Entity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i){
                    case 0 -> VoidGenBlock1Entity.this.progress = i1;
                    case 1 -> VoidGenBlock1Entity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.void_generator.void_generator_block");
    }
    public ChunkPos getchunkpos(){
        int posx = getBlockPos().getX() >> 4;
        int posz = getBlockPos().getZ() >> 4;
        ChunkPos pos = new ChunkPos(posx, posz);
        return pos;
    }
    public void addchunktolist(){
        ChunkPos pos = getchunkpos();
        if(this.chunklist.isEmpty() == false){
            for(int i = 0; i <= chunklist.size()-1; i++){
            System.out.println("Chunklist: " + chunklist.get(i));
            System.out.println("pos: " + pos);
            System.out.println("Listlength: "+ chunklist.size());
            if(chunklist.get(i).equals(pos)){
                markedfordeath = true;
            }
            }
                chunklist.add(pos);
                System.out.println("added pos");
        }
        else if (markedfordeath == false) {
            chunklist.add(pos);
            System.out.println("added first pos");
            System.out.println("pos: " + pos);
        }

    }

    public void eventhorizon(){
        if(explodingprogress >= maxexplodingprogress){
            ChunkPos pos = getchunkpos();
            boolean firstok = false;
            for(int i = 0; i <= chunklist.size()-1; i++){
                if(chunklist.get(i).equals(pos)){
                    if(firstok == true) {
                        System.out.println("secondok");
                        break;
                    }
                    firstok = true;
                    System.out.println("firstok");
                }
                if((chunklist.size()-1) == i){
                    markedfordeath = false;
                    explodingprogress = 0;
                    System.out.println("eventhorizon stopped");
                    break;
                }
                System.out.println("i: " + i);
                System.out.println("chunklist-1: " + (chunklist.size()-1));
            }
            if (!level.isClientSide && level instanceof ServerLevel serverLevel && markedfordeath == true) {
                BlockPos bpos = getBlockPos();
                serverLevel.explode(null, bpos.getX(), bpos.getY(), bpos.getZ(), 37F, true, Level.ExplosionInteraction.BLOCK);
            }
        }
        else if (explodingprogress == 0){
            explodingprogress++;
            Level level = getLevel();       // dein Level / World
            BlockPos pos = getBlockPos();   // Position des Blocks
                level.playSeededSound(
                        null,                     // für alle Spieler in der Nähe
                        pos.getX(), pos.getY(), pos.getZ(),
                        ModSounds.VOIDGEN_OVERLOADED.get(), // registrierter SoundEvent
                        SoundSource.BLOCKS,1f, 1f,0L);
        }
        else if(markedfordeath == true) explodingprogress++;
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new VoidGenMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("void_itemgeneration_progress", progress);
        pTag.putInt("explosion_progress", explodingprogress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("void_itemgeneration_progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(markedfordeath == true) eventhorizon();
        if(firstplaced == false){
            addchunktolist();
            firstplaced = true;
        }
        if(progress < maxProgress){ //if progress finished
            increaseCraftingProgress();
        }
        else{
            if(itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()){
                createItem();
                progress = 0;
            }
        }
    }
    public void onDestroy(){
        if(markedfordeath == true) {
            ModMessages.sendToClients(
                    new StopSoundPacket(ModSounds.VOIDGEN_OVERLOADED.get().getLocation())
            );
            System.out.println("Sent StopSoundPacket to clients");
        }

        ChunkPos pos = getchunkpos();
            for(int i = 0; i <= chunklist.size()-1; i++){
                if(chunklist.get(i).equals(pos)){
                    System.out.println("remove");
                    chunklist.remove(i);
                    break;
            }
        }
    }
    private void createItem() {
        Item item = ModUtil.getRandomConfiguredItem();
        ItemStack stack = new ItemStack(item);
        itemHandler.insertItem(0,stack,false);
    }

    private void increaseCraftingProgress() {
        progress++;
    }

}
