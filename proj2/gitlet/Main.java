package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            Utils.error("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs("init", args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs("add", args, 2);
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                validateNumArgs("commit", args, 2);
                Repository.commit(args[1]);
                break;
            case "checkout":
                int argLength = args.length;
                validateNumArgs("checkout", args, 2, 4);
                switch(argLength) {
                    case 2:
                        Repository.checkoutBranch(args[1]);
                        break;
                    case 3:
                        Repository.checkoutFilename(args[1], args[2]);
                        break;
                    case 4:
                        Repository.checkoutIdWithFilename(args[1], args[2], args[3]);
                        break;
                }
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            default:
                Utils.error("No command with that name exists.");
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
    /**
     * Checks the number of arguments versus two expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param nl Number of expected arguments
     * @param nh Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int nl, int nh) {
        if (args.length < nl ||  args.length > nh) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
