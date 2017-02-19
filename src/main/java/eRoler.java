import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.user.UserGameUpdateEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Bot that handles roles for Ethereal Network.
 */
public class eRoler extends ListenerAdapter {

    /**
     * Starts the bot with command line args or the bot.properties file.
     * @param args Command line args.
     */
    public static void main(String[] args) {
        System.out.println("eRoler - Discord Permissions Bot");

        String token = "";

        // Initialize bot via the command line.
        if (args.length >= 1) {
            token = args[0];
            if (args.length >= 2) {
                // Looking for: (r,g,b)
                String[] parts = args[1].split(",");
                try {
                    int r = Integer.parseInt(parts[0].substring(1));
                    int g = Integer.parseInt(parts[1]);
                    int b = Integer.parseInt(parts[2].substring(0, parts[2].length() - 1));
                    gameRoleColor = new Color(r,g,b);
                } catch (Exception e) {System.out.println("Invalid RGB values!");}
            }
            if(args.length >= 3)
                mentionable = (args[2].equalsIgnoreCase("true"));
            if(args.length >= 4)
                gameRolePermissions = convertStringArrayToPermissionList(args[3].split(", "));
        }
        try {
            if (token.equals("")) {
                BufferedReader br = Files.newBufferedReader(Paths.get("./bot.properties"));

                String properties = br.readLine();
                if (properties != null)
                    token = properties;

                properties = br.readLine();
                if (properties != null) {
                    // Looking for: (r,g,b)
                    int r = properties.charAt(1);
                    int g = properties.charAt(3);
                    int b = properties.charAt(5);
                    gameRoleColor = new Color(r,g,b);
                }

                properties = br.readLine();
                if (properties != null)
                    gameRolePermissions = convertStringArrayToPermissionList(properties.split(", "));

                br.close();
            }

            new JDABuilder(AccountType.BOT)
                    .setBulkDeleteSplittingEnabled(false)
                    .setToken(token)
                    .addListener(new eRoler())
                    .buildBlocking()
                    .setAutoReconnect(true);
        }
        catch (IllegalArgumentException e) {
            System.out.println("The config was not populated. Please make sure all arguments were given.");
        }
        catch (LoginException e) {
            System.out.println("The provided bot token was incorrect. Please provide a valid token.");
        }
        catch (InterruptedException | RateLimitedException e) {
            System.out.println("A thread interruption occurred. Check Stack Trace below for source.");
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not find Bot Token file!");
        }
        catch (IOException e) {
            System.out.println("Could not read Bot Token file!");
        }
        catch (Exception e) {
            System.out.println("A general exception was caught. Exception: " + e.getCause());
        }
    }

    /**
     * Color the {@link Game} {@link Role} will be.
     */
    private static Color gameRoleColor = new Color(40,80,120);

    /**
     * Whether the {@link Game} {@link Role} can be mentioned.
     */
    private static boolean mentionable = true;

    /**
     * All {@link Permission}s the {@link Game} {@link Role} will have.
     */
    private static List<Permission> gameRolePermissions = new ArrayList<>();

    /**
     * Gets a {@link List} of {@link Permission}s that are converted from a {@link String} array.
     * @param permissions {@link String} array of permissions.
     * @return A {@link List} of {@link Permission}s the {@link Game} {@link Role} will have.
     */
    private static List<Permission> convertStringArrayToPermissionList(String[] permissions) {
        List<Permission> permissionList = new ArrayList<>();
        for (String permission : permissions)
            permissionList.add(stringPermissionToPermission(permission));
        return permissionList;
    }

    /**
     * Converts the {@link String} version of a {@link Permission} into a {@link Permission}.
     * @param permission {@link String} form of a {@link Permission}.
     * @return The converted {@link String} {@link Permission}.
     */
    private static Permission stringPermissionToPermission(String permission) {
        switch (permission) {
            case "Administrator":
                return Permission.ADMINISTRATOR;
            case "Manage Server":
                return Permission.MANAGE_SERVER;
            case "Manage Roles":
                return Permission.MANAGE_ROLES;
            case "Manage Channels":
                return Permission.MANAGE_CHANNEL;
            case "Kick Members":
                return Permission.KICK_MEMBERS;
            case "Ban Members":
                return Permission.BAN_MEMBERS;
            case "Create Instant Invite":
                return Permission.CREATE_INSTANT_INVITE;
            case "Change Nickname":
                return Permission.NICKNAME_CHANGE;
            case "Manage Nicknames":
                return Permission.NICKNAME_MANAGE;
            case "Manage Emojis":
                return Permission.MANAGE_EMOTES;
            case "Manage Webhooks":
                return Permission.MANAGE_WEBHOOKS;
            case "Read Messages":
                return Permission.MESSAGE_READ;
            case "Send Messages":
                return Permission.MESSAGE_WRITE;
            case "Send TTS Messages":
                return Permission.MESSAGE_TTS;
            case "Manage Messages":
                return Permission.MESSAGE_MANAGE;
            case "Embed Links":
                return Permission.MESSAGE_EMBED_LINKS;
            case "Attach Files":
                return Permission.MESSAGE_ATTACH_FILES;
            case "Read Message History":
                return Permission.MESSAGE_HISTORY;
            case "Mention Everyone":
                return Permission.MESSAGE_MENTION_EVERYONE;
            case "Use External Emojis":
                return Permission.MESSAGE_EXT_EMOJI;
            case "Add Reactions":
                return Permission.MESSAGE_ADD_REACTION;
            case "Connect":
                return Permission.VOICE_CONNECT;
            case "Speak":
                return Permission.VOICE_SPEAK;
            case "Mute Members":
                return Permission.VOICE_MUTE_OTHERS;
            case "Deafen Members":
                return Permission.VOICE_DEAF_OTHERS;
            case "Move Members":
                return Permission.VOICE_MOVE_OTHERS;
            case "Use Voice Activity":
                return Permission.VOICE_USE_VAD;
            default:
                return Permission.UNKNOWN;
        }
    }

    /**
     * Checks the current game of all {@link Member}s in the passed in {@link Guild}. It then assigns the {@link Game}'s
     * {@link Role} to that {@link Member}.
     * @param event Contains the {@link Guild} used to get {@link Member}s and {@link Role}s.
     */
    public void onUserGameUpdate(UserGameUpdateEvent event) {
        Guild guild = event.getGuild();
        List<Member> members = guild.getMembers();
        for (Member member : members) {
            Game game = member.getGame();
            if (game != null && !game.getType().name().equalsIgnoreCase("twitch"))
                assignGameRoleToMember(guild.getController(), member, member.getGame());
        }
    }

    /**
     * Checks if a {@link Guild} contains a {@link Game} {@link Role} using {@link #guildHasGameRole(Guild, Game)}, and
     * if the {@link Guild} has the {@link Role}, assign it to the {@link Member}. If the {@link Role} does not exist,
     * the method creates the {@link Role} and then assigns it to the {@link Member}. (If the {@link Member}
     * already has the {@link Role}, the method does not assign it.)
     *
     * @param controller Used to create the {@link Role}.
     * @param member The {@link Member} for the {@link Role} to be assigned to.
     * @param game The {@link Game} whose name is used in searching/creating the {@link Role}.
     */
    private void assignGameRoleToMember(GuildController controller, Member member, Game game) {
        // If the guild has a role for the passed in game continue, otherwise go to else.
        if(guildHasGameRole(member.getGuild(), game)) {
            Role role = getGameRoleFromGuild(member.getGuild(), game);

            if(role != null) {
                // Member already has the role.
                for(Role r : member.getRoles())
                    if(r.getName().equalsIgnoreCase(role.getName()))
                        return;

                // Member didn't have role, so assign it to the member if the role is not null.
                controller.addRolesToMember(member, role).queue();
            }
        }
        // Game role did not exist in the guild so create it and assign it to the member.
        else {
            Role role = createRoleFromGame(controller, game);
            controller.addRolesToMember(member, role).queue();
        }
    }

    /**
     * Checks if the passed in {@link Guild} contains a {@link Role} with the {@link Game}'s name.
     * @param guild {@link Guild} from which to check {@link Role}s.
     * @param game Used to get the name of the {@link Role}.
     * @return Whether the {@link Guild} contains a {@link Role} with the {@link Game}'s name.
     */
    private boolean guildHasGameRole(Guild guild, Game game) {
        for (Role role : guild.getRoles())
            if (role.getName().equalsIgnoreCase(game.getName()))
                return true;
        return false;
    }

    /**
     * Gets the game role from the passed in guild using the passed in game's name.
     * @param guild Used to get the {@link Role}s.
     * @param game Used to get the {@link Role}'s name.
     * @return The {@link Role} of the {@link Game}, or null if it doesn't exist.
     */
    private Role getGameRoleFromGuild(Guild guild, Game game) {
        for(Role role : guild.getRoles())
            if (role.getName().equalsIgnoreCase(game.getName()))
                return role;
        return null;
    }

    /**
     * Creates a {@link Role} with: the permissions from {@link #gameRolePermissions}, the {@link Color} of
     * {@link #gameRoleColor}, and the passed in {@link Game}'s name.
     * @param controller {@link GuildController} used to create the game {@link Role}.
     * @param game {@link Game} used for it's name in the {@link Role} creation.
     * @return A new {@link Role} with the same name as the {@link Game}'s name.
     */
    private Role createRoleFromGame(GuildController controller, Game game) {
        // Create the role.
        Role role = controller.createRole()
                .setName(game.getName())
                .setPermissions()
                .setColor(gameRoleColor)
        .complete();

        // Set the role's permissions.
        setRolePermissions(role);

        return role;
    }

    /**
     * Sets the permissions of a {@link Game} {@link Role} with the {@link #gameRolePermissions} variable.
     * @param role The {@link Role} whose permissions will be set.
     */
    private void setRolePermissions(Role role) {
        // Get rid of the all permissions.
        role.getManager().revokePermissions(Permission.getPermissions(Permission.ALL_PERMISSIONS)).complete();

        // Add permissions from specified gameRolePermissions list.
        if(gameRolePermissions.size() > 0)
            role.getManager().givePermissions(gameRolePermissions);

        // Set the game role as mentionable.
        role.getManager().setMentionable(mentionable).complete();
    }
}
