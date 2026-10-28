package emanondev.displayeditor.utility;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Paper and Folia scheduler helpers. Use entity or location overloads when accessing that context.
 */
public final class SchedulerUtils {

    private SchedulerUtils() {
    }

    /**
     * Runs a task asynchronously. The task must not access Bukkit world or entity state.
     */
    public static void runAsync(@NotNull Plugin plugin, @NotNull Runnable task) {
        plugin.getServer().getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
    }

    /**
     * Runs a task on the global region.
     */
    public static void run(@NotNull Plugin plugin, @NotNull Runnable task) {
        plugin.getServer().getGlobalRegionScheduler().run(plugin, scheduledTask -> task.run());
    }

    /**
     * Schedules a task on the global region after the given number of ticks.
     */
    public static void runLater(@NotNull Plugin plugin,
                                @Range(from = 1L, to = Long.MAX_VALUE) long delayTicks,
                                @NotNull Runnable task) {
        plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin,
                scheduledTask -> task.run(), delayTicks);
    }

    /**
     * Runs a task on the region that owns the location.
     */
    public static void run(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task) {
        plugin.getServer().getRegionScheduler().run(plugin, location, scheduledTask -> task.run());
    }

    /**
     * Schedules a task on the region that owns the location after the given number of ticks.
     */
    public static void runLater(@NotNull Plugin plugin,
                                @NotNull Location location,
                                @Range(from = 1L, to = Long.MAX_VALUE) long delayTicks,
                                @NotNull Runnable task) {
        plugin.getServer().getRegionScheduler().runDelayed(plugin, location,
                scheduledTask -> task.run(), delayTicks);
    }

    /**
     * Runs a task on the scheduler owned by the player.
     */
    public static void run(@NotNull Plugin plugin, @NotNull Player player, @NotNull Runnable task) {
        run(plugin, (Entity) player, task);
    }

    /**
     * Runs a task on the scheduler owned by the entity.
     */
    public static void run(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable task) {
        run(plugin, entity, task, null);
    }

    /**
     * Runs a task on the entity scheduler, or invokes retired if the entity can no longer be scheduled.
     */
    public static void run(@NotNull Plugin plugin,
                           @NotNull Entity entity,
                           @NotNull Runnable task,
                           @Nullable Runnable retired) {
        entity.getScheduler().run(plugin, scheduledTask -> task.run(), retired);
    }

    /**
     * Schedules a task on the scheduler owned by the player.
     */
    public static void runLater(@NotNull Plugin plugin,
                                @NotNull Player player,
                                @Range(from = 1L, to = Long.MAX_VALUE) long delayTicks,
                                @NotNull Runnable task) {
        runLater(plugin, (Entity) player, delayTicks, task);
    }

    /**
     * Schedules a task on the scheduler owned by the entity.
     */
    public static void runLater(@NotNull Plugin plugin,
                                @NotNull Entity entity,
                                @Range(from = 1L, to = Long.MAX_VALUE) long delayTicks,
                                @NotNull Runnable task) {
        entity.getScheduler().runDelayed(plugin, scheduledTask -> task.run(), null, delayTicks);
    }
}
