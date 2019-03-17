//package JMM.Final;
//
//import static jdk.net.SocketFlow.NORMAL_PRIORITY;
//import static org.graalvm.compiler.bytecode.Bytecodes.INVOKESTATIC;
//
//public class CallSystemGC extends BytecodeScanningDetector {
//    private BugReporter bugReporter;
//    public CallSystemGC(BugReporter bugReporter) {
//        this.bugReporter = bugReporter;
//    }
//    public void sawOpcode(int seen) {
//        if (seen == INVOKESTATIC) {
//            if (getClassConstantOperand().equals("java/lang/System")
//                    && getNameConstantOperand().equals("gc")) {
//                bugReporter.reportBug(new BugInstance("SYSTEM_GC", NORMAL_PRIORITY)
//                        .addClassAndMethod(this)
//                        .addSourceLine(this));
//            }
//        }
//    }
