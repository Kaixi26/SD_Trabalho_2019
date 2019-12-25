package Client.Interface;

public class HelpText {
    static final public String help = "help\n" +
            "register\n" +
            "authenticate\n" +
            "download\n" +
            "upload\n" +
            "search";

    static final public String help_help = "usage:\n" +
            "\thelp <command>\n" +
            "example:\n" +
            "\thelp register";

    static final public String help_register = "usage:\n" +
            "\tregister <username> <password>\n" +
            "example:\n" +
            "\tregister test 12345";

    static final public String help_search = "usage:\n" +
            "\tsearch <tag> <list_offset>\n" +
            "example:\n" +
            "\tsearch rock 3";

    static final public String help_authenticate = "usage:\n" +
            "\tauthenticate <username> <password>\n" +
            "example:\n" +
            "\tauthenticate admin admin";

    static final public String help_upload = "usage:\n" +
            "\tupload <path> <title> <author> <year> <tags>\n" +
            "example:\n" +
            "\tupload /home/user/Midas.mp3 Midas Skott 2019 skott pop";
    static final public String help_download = "usage:\n" +
            "\tdownload <path> <id>\n" +
            "example:\n" +
            "\tdownload ./ 1";
}
