package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * * .gitlet/ -- top level folder for all persistent data
 *    - commit/ -- folder containing all of the persistent data for commits
 *    - blob/ -- folder containing all of the persistent data for blobs
 *    - branch/ -- folder containing all of the persistent data for branch
 *    - HEAD -- file containing the current HEAD
 *    - (index) -- file containing the current staging area, file is created
 *                 depending on commands
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");
    public static final File HEAD = join(GITLET_DIR, "HEAD");


    /* TODO: fill in the rest of this class. */
    
    public static void init() {
        if(!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOB_DIR.mkdir();
            String master_UID = new Commit().saveFile();
            addBranchFile("master", master_UID);
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.writeContents(HEAD, master_UID);
        }
        else {
            Utils.error("A Gitlet version-control system already exists in the current directory.");
        }
    }

    private static void addBranchFile(String branch, String branch_uid) {
        File masterFile = join(BRANCH_DIR, branch);
        try {
            masterFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(masterFile, branch_uid);
    }

    private static void checkInit() {
        if (!GITLET_DIR.exists() || !GITLET_DIR.isDirectory()) {
            throw new RuntimeException("Not in an initialized Gitlet directory.");
        }
    }


    public static void add(String arg) {
        checkInit();
        File addFile = join(CWD, arg);
        if(!addFile.exists() || addFile.isDirectory()) {
            Utils.error("File does not exist.");
        }

        String fileContentUID = Utils.sha1(Utils.readContents(addFile));

    }

    public static void commit(String arg) {
    }

    public static void checkoutBranch(String arg) {
    }

    public static void checkoutFilename(String arg, String arg1) {
    }

    public static void checkoutIdWithFilename(String arg, String arg1, String arg2) {
    }

    public static void log() {
    }
}
