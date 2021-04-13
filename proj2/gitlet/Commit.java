package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private final Date date;
    private final String message;
    private HashMap<File, String> nameToBlob;
    private final String parentID;
    private final String secondParentID;

    public Commit() {
        date = new Date(0);
        message = "initial commit";
        nameToBlob = new HashMap<File, String>();
        parentID = null;
        secondParentID = null;
    }

    public Commit(String messageToSave, HashMap<File, String> saveMap, String parent1) {
        date = new Date();
        message = messageToSave;
        nameToBlob = saveMap;
        parentID = parent1;
        secondParentID = null;
    }

    public Commit(String messageToSave, HashMap<File, String> saveMap, String parent1, String parent2) {
        date = new Date();
        message = messageToSave;
        nameToBlob = saveMap;
        parentID = parent1;
        secondParentID = parent2;
    }

    public String getMessage() {
        return  message;
    }

    public Map<File, String> getMap() {
        return Collections.unmodifiableMap(nameToBlob);
    }

    public boolean isIDexist(String Uid) {
        return nameToBlob.containsValue(Uid);
    }

     public String thisID() {
        return Utils.sha1(Utils.serialize(this));
     }

     public String getParentID() {
        return parentID;
     }

    public String saveFile() {
        String fileUID = this.thisID();
        File saveFile = join(COMMIT_DIR, fileUID);
        Utils.writeObject(saveFile, this);
        /**Reread to avoid reading different ID of saved Commit
        File thisFile = Utils.join(COMMIT_DIR, fileUID);
        String id = Utils.readObject(thisFile, Commit.class).thisID();
        if(!id.equals(fileUID)) {
            File newFile = Utils.join(COMMIT_DIR, fileUID);
        }*/
        return fileUID;
    }

    @Override
    public String toString() {
        String result = "===\n";
        result += "commit " +  this.thisID() + "\n";
        if(secondParentID != null) {
            result += "Merge: " + parentID.substring(0,7) + " " + secondParentID.substring(0,7) + "\n";
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM d hh:mm:ss yyyy Z");
        result += "Date: " + dateFormatter.format(date) + "\n";
        result += message + "\n\n";
        return result;
    }
}
