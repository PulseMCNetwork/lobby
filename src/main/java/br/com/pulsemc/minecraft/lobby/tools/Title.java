package br.com.pulsemc.minecraft.lobby.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
@author: syncwrld
@website: https://github.com/syncwrld
 */
@Getter
@Setter
public class Title {
    private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<>();
    private static Class<?> packetTitle;
    private static Class<?> packetActions;
    private static Class<?> nmsChatSerializer;
    private static Class<?> chatBaseComponent;
    private String title = "";
    private ChatColor titleColor;
    private String subtitle;
    private ChatColor subtitleColor;
    private int fadeInTime;
    private int stayTime;
    private int fadeOutTime;
    private boolean ticks;

    public Title() {
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.loadClasses();
    }

    public Title(String title) {
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title;
        this.loadClasses();
    }

    public Title(String title, String subtitle) {
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title;
        this.subtitle = subtitle;
        this.loadClasses();
    }

    public Title(Title title) {
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title.getTitle();
        this.subtitle = title.getSubtitle();
        this.titleColor = title.getTitleColor();
        this.subtitleColor = title.getSubtitleColor();
        this.fadeInTime = title.getFadeInTime();
        this.fadeOutTime = title.getFadeOutTime();
        this.stayTime = title.getStayTime();
        this.ticks = title.isTicks();
        this.loadClasses();
    }

    public Title(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title;
        this.subtitle = subtitle;
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTime = fadeOutTime;
        this.loadClasses();
    }

    private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length) {
            return false;
        } else {
            for(int i = 0; i < a.length; ++i) {
                if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i])) {
                    return false;
                }
            }

            return true;
        }
    }

    private void loadClasses() {
        if (packetTitle == null) {
            packetTitle = this.getNMSClass("PacketPlayOutTitle");
            packetActions = this.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
            chatBaseComponent = this.getNMSClass("IChatBaseComponent");
            nmsChatSerializer = this.getNMSClass("ChatComponentText");
        }

    }

    public void setTimingsToTicks() {
        this.ticks = true;
    }

    public void setTimingsToSeconds() {
        this.ticks = false;
    }

    public void send(Player player) {
        if (packetTitle != null) {
            this.resetTitle(player);

            try {
                Object handle = this.getHandle(player);
                Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
                Object[] actions = packetActions.getEnumConstants();
                Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
                Object packet = packetTitle.getConstructor(packetActions, chatBaseComponent, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(actions[2], null, this.fadeInTime * (this.ticks ? 1 : 20), this.stayTime * (this.ticks ? 1 : 20), this.fadeOutTime * (this.ticks ? 1 : 20));
                if (this.fadeInTime != -1 && this.fadeOutTime != -1 && this.stayTime != -1) {
                    assert sendPacket != null;
                    sendPacket.invoke(connection, packet);
                }

                Object serialized = nmsChatSerializer.getConstructor(String.class).newInstance(ChatColor.translateAlternateColorCodes('&', this.title));
                packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[0], serialized);
                assert sendPacket != null;
                sendPacket.invoke(connection, packet);
                if (!Objects.equals(this.subtitle, "")) {
                    serialized = nmsChatSerializer.getConstructor(String.class).newInstance(ChatColor.translateAlternateColorCodes('&', this.subtitle));
                    packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[1], serialized);
                    sendPacket.invoke(connection, packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void updateTimes(Player player) {
        if (packetTitle != null) {
            try {
                Object handle = this.getHandle(player);
                Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
                Object[] actions = packetActions.getEnumConstants();
                Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
                Object packet = packetTitle.getConstructor(packetActions, chatBaseComponent, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(actions[2], 0, this.fadeInTime * (this.ticks ? 1 : 20), this.stayTime * (this.ticks ? 1 : 20), this.fadeOutTime * (this.ticks ? 1 : 20));
                if (this.fadeInTime != -1 && this.fadeOutTime != -1 && this.stayTime != -1) {
                    assert sendPacket != null;
                    sendPacket.invoke(connection, packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void updateTitle(Player player) {
        if (packetTitle != null) {
            try {
                Object handle = this.getHandle(player);
                Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
                Object[] actions = packetActions.getEnumConstants();
                Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
                Object serialized = nmsChatSerializer.getConstructor(String.class).newInstance(ChatColor.translateAlternateColorCodes('&', this.title));
                Object packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[0], serialized);
                assert sendPacket != null;
                sendPacket.invoke(connection, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void updateSubtitle(Player player) {
        if (packetTitle != null) {
            try {
                Object handle = this.getHandle(player);
                Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
                Object[] actions = packetActions.getEnumConstants();
                Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
                Object serialized = nmsChatSerializer.getConstructor(String.class).newInstance(ChatColor.translateAlternateColorCodes('&', this.subtitle));
                Object packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[1], serialized);
                assert sendPacket != null;
                sendPacket.invoke(connection, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void broadcast() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            this.send(p);
        }

    }

    public void clearTitle(Player player) {
        try {
            Object handle = this.getHandle(player);
            Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
            Object[] actions = packetActions.getEnumConstants();
            Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
            Object packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[3], null);
            assert sendPacket != null;
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetTitle(Player player) {
        try {
            Object handle = this.getHandle(player);
            Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
            Object[] actions = packetActions.getEnumConstants();
            Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
            Object packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[4], null);
            sendPacket.invoke(connection, packet);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    private Class<?> getPrimitiveType(Class<?> clazz) {
        return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES.get(clazz) : clazz;
    }

    private Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class[] types = new Class[a];

        for(int i = 0; i < a; ++i) {
            types[i] = this.getPrimitiveType(classes[i]);
        }

        return types;
    }

    private Object getHandle(Object obj) {
        try {
            return this.getMethod("getHandle", obj.getClass()).invoke(obj);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
        Class[] t = this.toPrimitiveTypeArray(paramTypes);
        Method[] arrayOfMethod;
        int j = (arrayOfMethod = clazz.getMethods()).length;

        for(int i = 0; i < j; ++i) {
            Method m = arrayOfMethod[i];
            Class[] types = this.toPrimitiveTypeArray(m.getParameterTypes());
            if (m.getName().equals(name) && equalsTypeArray(types, t)) {
                return m;
            }
        }

        return null;
    }

    private String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf(46) + 1) + ".";
    }

    private Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + this.getVersion() + className;
        Class clazz = null;

        try {
            clazz = Class.forName(fullName);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return clazz;
    }

    private Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        Method[] arrayOfMethod;
        int j = (arrayOfMethod = clazz.getMethods()).length;

        for(int i = 0; i < j; ++i) {
            Method m = arrayOfMethod[i];
            if (m.getName().equals(name) && (args.length == 0 || this.ClassListEqual(args, m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        }

        return null;
    }

    private boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        } else {
            for(int i = 0; i < l1.length; ++i) {
                if (l1[i] != l2[i]) {
                    equal = false;
                    break;
                }
            }

            return equal;
        }
    }

}