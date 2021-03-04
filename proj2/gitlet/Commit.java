package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");

    /** The message of this Commit. */
    private Date date;
    private String message;
    private HashMap<String, String> nameToBlob;
    private String parentID;
    private String secondParentID;

    public Commit() {
        date = new Date(0);
        message = "initial commit";
        nameToBlob = null;
        parentID = null;
        secondParentID = null;
    }

    public Commit(String messageToSave, HashMap<String, String> saveMap, String parent1) {
        date = new Date();
        message = messageToSave;
        nameToBlob = saveMap;
        parentID = parent1;
        secondParentID = null;
    }

    public Commit(String messageToSave, HashMap<String, String> saveMap, String parent1, String parent2) {
        date = new Date();
        message = messageToSave;
        nameToBlob = saveMap;
        parentID = parent1;
        secondParentID = parent2;
    }

    public HashMap<String, String> getMap() {
        return nameToBlob;
    }

    public String saveFile() {
        String tempStringFile = Utils.sha1(String.valueOf(0));
        File tempFile = join(COMMIT_DIR, tempStringFile);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(tempFile, this);
        String fileUID = Utils.sha1(Utils.readContents(tempFile));
        File saveFile = join(COMMIT_DIR, fileUID);
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(saveFile, this);
        tempFile.delete();
        return fileUID;
    }


    /* TODO: fill in the rest of this class. */
}
