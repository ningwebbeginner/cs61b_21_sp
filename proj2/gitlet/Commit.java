package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.TreeMap;

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

    /** The message of this Commit. */
    private Date date;
    private String message;
    private TreeMap<String, String> nameToBlob;
    private String parentID;
    private String secondParentID;

    public Commit() {
        date = new Date(0);
        message = "initial commit";
        nameToBlob = null;
        parentID = null;
        secondParentID = null;
    }


    /* TODO: fill in the rest of this class. */
}
