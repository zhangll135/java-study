package ConcTest.Part10;

public class LockOrderDeadLock {
    public void transferMony(Account from,Account to,int amount){
        synchronized (from){
            synchronized (to){
                from.debit(amount);
                to.credit(amount);
            }
        }
    }
}
class Account{
    public void debit(int amount){
        System.out.println("debit: "+amount);
    }
    public void credit(int amount){
        System.out.println("credit: "+amount);
    }
}
class LockOrderDeadLockHash {
    private static final Object tieLock = new Object();
    public void transferMony(Account from,Account to,int amount){
        class Helper{
            public void transfer(){
                from.debit(amount);
                to.credit(amount);
            }
        }
        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);

        if(fromHash<toHash){
            synchronized (from){
                synchronized (to){
                    new Helper().transfer();
                }
            }
        }else if(fromHash>toHash){
            synchronized (to){
                synchronized (from){
                    new Helper().transfer();
                }
            }
        }else {
            synchronized (tieLock){
                synchronized (from){
                    synchronized (to){
                        new Helper().transfer();
                    }
                }
            }
        }
    }
}
