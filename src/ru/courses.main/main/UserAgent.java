package ru.courses.main.main;


public class UserAgent {

    final String typeBrowser;
    final String typeOs;
    final boolean isBot;

    public UserAgent(String userAgent) {
        String s = "OTHER";
        String s2 = "";

        if (userAgent.equals("-")) {
            this.typeBrowser = "-";
        } else {
            for (TypeBrowse typeBrowse : TypeBrowse.values()) {
                if (userAgent.contains(typeBrowse.name)) {
                    s = typeBrowse.name;
                    break;
                }
            }
            this.typeBrowser = String.valueOf(TypeBrowse.forValue(s));
        }

        if (getTypeBrowser().equals("-")) {
            this.typeOs = "-";
        } else {
            for (TypeOs typeOs1 : TypeOs.values()) {
                int osIndex = userAgent.indexOf(typeOs1.name);
                if (osIndex != -1) {
                    int osEndIndex = osIndex + typeOs1.name.length();
                    s2 = userAgent.substring(osIndex, osEndIndex);
                    break;
                }
            }
            this.typeOs = String.valueOf(TypeOs.forValue(s2));
        }

        this.isBot = isBot(userAgent);
    }

    public String getTypeOs() {
        return typeOs;
    }

    public String getTypeBrowser() {
        return typeBrowser;
    }

    @Override
    public String toString() {
        return typeBrowser + "/" + typeOs;
    }

    public enum TypeBrowse {
        EDGE("Edge"), MOZILLA("Mozilla"), OPERA("Opera"), CHROME("Chrome"), OTHER("Other");

        final String name;

        TypeBrowse(String name) {
            this.name = name;
        }

        public static String forValue(String nameBrowser) {
            for (TypeBrowse typeBrowse : TypeBrowse.values()) {
                if (typeBrowse.name.equals(nameBrowser)) {
                    return typeBrowse.name;
                }
            }
            return TypeBrowse.OTHER.name;
        }
    }

    public enum TypeOs {
        WINDOWS("Windows"), MACOS("Mac OS"), LINUX("Linux");

        final String name;

        TypeOs(String name) {
            this.name = name;
        }
        public static String forValue(String nameOs) {
            for (TypeOs typeOs1 : TypeOs.values()) {
                if (typeOs1.name.equals(nameOs)) {
                    return typeOs1.name;
                }
            }
            return "-";
        }
    }
    private boolean isBot(String userAgent) {
        if (getTypeBrowser().equals("-")) {
            return false;
        } else return userAgent.contains("bot");
    }

    public boolean isBot() {
        return isBot;
    }
}