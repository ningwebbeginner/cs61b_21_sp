package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
    public static final File INDEX_File = join(GITLET_DIR, "index");

    /* TODO: fill in the rest of this class. */
    
    public static void init() {
        if(!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOB_DIR.mkdir();
            BRANCH_DIR.mkdir();
            String master_UID = new Commit().saveFile();
            addBranchFile("master", master_UID);
            initIndex();
            Utils.writeContents(HEAD, master_UID);
        }
        else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    private static void initIndex() {
        Utils.writeObject(INDEX_File, new HashMap<File, String>());
    }


    private static void addBranchFile(String branch, String branch_uid) {
        File masterFile = join(BRANCH_DIR, branch);
        Utils.writeContents(masterFile, branch_uid);
    }

    private static void checkInit() {
        if (!GITLET_DIR.isDirectory()
        || !COMMIT_DIR.isDirectory()
        || !BLOB_DIR.isDirectory()
        || !BRANCH_DIR.isDirectory()
        || !HEAD.exists()
        || !INDEX_File.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }


    /**
     * adding a file is also called staging the file for addition.
     * 1. Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * 2. If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it’s original version).
     * 3. The file will no longer be staged for removal (see gitlet rm),
     * if it was at the time of the command.
     */
    public static void add(String arg) {
        checkInit();
        File addFile = join(CWD, arg);
        if(!addFile.exists() || addFile.isDirectory()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        stagingAddFile(addFile);
    }

    private static void stagingAddFile(File addFile) {
        String fileContentUID = getContentID(addFile);
        if(readCurrentCommit().isIDexist(fileContentUID)) {
            removeInIndex(addFile, fileContentUID);
            return;
        }
        File newFileInBlob = addFileInBlob(addFile, fileContentUID);
        addtoIndex(addFile, fileContentUID);
    }

    /****************INDEX******************/
    /****
     *INDEX is the staging are of the command.
     * The content of INDEX are entries:
     * file to UID
     * or file to null to remove.
     */
    //TODO: test after finish commit()
    private static void removeInIndex(File file_CWD, String fileUID) {
            HashMap<File, String> readFile = Utils.readObject(INDEX_File, HashMap.class);
            if(readFile.containsValue(fileUID)) {
                String removeFile = readFile.remove(readFile.get(file_CWD));
                if(removeFile != null) removeFileInBlob(removeFile);
            }
            Utils.writeObject(INDEX_File, readFile);
    }

    private static void addtoIndex(File file_CWD, String fileContentUID) {
            HashMap<File, String> readFile = Utils.readObject(INDEX_File, HashMap.class);
            String RemoveFileUID = readFile.put(file_CWD, fileContentUID);
            if(RemoveFileUID != null) {
                boolean result = removeFileInBlob(RemoveFileUID);
                //TODO the file does not exist
            }
            Utils.writeObject(INDEX_File, readFile);
    }
    /****************INDEX END******************/

    private static Commit readCurrentCommit() {
        String headID = Utils.readContentsAsString(HEAD);
        File currentCommitFile = join(COMMIT_DIR, headID);
        if(!currentCommitFile.exists()) {
            return null;
        }
        return Utils.readObject(currentCommitFile, Commit.class);
    }


         static String getContentID(File fileInCWD) {
            return Utils.sha1(Utils.readContents(fileInCWD));
        }

    /****************Blob (TO Be Class)******************/
    private static boolean removeFileInBlob(String removeFileUID) {
        File removeFileInBlob = join(BLOB_DIR, removeFileUID);
            return removeFileInBlob.delete();
    }

    private static File addFileInBlob(File addFile, String fileContentUID) {
        File addFileInBlob = join(BLOB_DIR, fileContentUID);
        Utils.writeContents(addFileInBlob, Utils.readContents(addFile));
        return addFileInBlob;
    }
    /**************Blob END*****************/

    /***
     * The commit is said to be tracking the saved files.
     * 1. By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * 2. A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
     * 3. A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * 4. Finally, files tracked in the current commit may be untracked in the new commit
     * as a result being staged for removal by the rm command (below).
     * 5. The commit just made becomes the “current commit”, and the head pointer now points to it.
     * The previous head commit is this commit’s parent commit.
     * @param arg
     */
    public static void commit(String arg) {
        checkInit();
        Commit currentCommit = readCurrentCommit();
        HashMap<File, String> currentMap = currentCommit.getMap();
        if(currentMap.size() == 0) systemoutWithMessage("No changes added to the commit.");
        currentMap.putAll(Utils.readObject(INDEX_File, HashMap.class));
        String newCommitID = new Commit(arg, currentMap, currentCommit.thisID()).saveFile();

        String currentBranch = findCurrentBranch();
        //TODO to delete if find a solution
        if(currentBranch == null) {
            systemoutWithMessage("HEAD doesn't match any BRANCH files");
        }
        File curentBranch = join(BRANCH_DIR, currentBranch);
        Utils.writeContents(curentBranch, newCommitID);
        Utils.writeContents(HEAD, newCommitID);

        initIndex();
    }

    private static void systemoutWithMessage(String s) {
        System.out.println(s);
        System.exit(0);
    }

    private static String findCurrentBranch() {
        List<String> filesInBranch = Utils.plainFilenamesIn(BRANCH_DIR);
        String result = null;
        String headID = Utils.readContentsAsString(HEAD);
        for(String branchName : filesInBranch) {
            File file = join(BRANCH_DIR, branchName);
            if(headID.equals(Utils.readContentsAsString(file))) {
                result = branchName;
            }

        }
        return result;
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
