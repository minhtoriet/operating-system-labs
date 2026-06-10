package lab5;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Bank {
    private final Object tieLock = new Object();
    private HashMap<Integer, Account> accounts = new HashMap<>();
    public Bank(int accountNum, int balance){
        for (int i = 0; i < accountNum; i++) {
            Account acc = new Account(i, balance);
            this.accounts.put(i,acc);
        }
    }
    private Account find(int id) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == id) return accounts.get(i);
        }
        return null;
    }
    public boolean transaction(int fromId, int toId, double amount){
        Account from = find(fromId);
        Account to = find(toId);
        if (from == null || to == null) return false;
        return transaction(from, to, amount);
    }
    public boolean transaction(Account from, Account to, double amount){

        Lock lockFrom = from.getLock();
        Lock lockTo = to.getLock();
        if (System.identityHashCode(from) > System.identityHashCode(to)){
            lockFrom.lock();
            lockTo.lock();
        } else if (System.identityHashCode(from) < System.identityHashCode(to)){
            lockTo.lock();
            lockFrom.lock();
        } else {
            synchronized(tieLock){
                lockFrom.lock();
                lockTo.lock();
            }
        }
        //apply Ostrich Algorithm in edge cases where
        //System.identityHashCode(from) == System.identityHashCode(to) 
        //and deadlock happens (ignore it)
        try {
            if (from.getBalance() < amount) return false;
            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);
            return true;
        } finally {
            lockFrom.unlock();
            lockTo.unlock();
        }
    }
}
class Account {
    private Lock lock = new ReentrantLock();
    private int id;
    private double balance;
    Account (int id, double balance){
        this.id = id; this.balance = balance;
    }
    public double getBalance(){
        return balance;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    public int getId(){return this.id;}
    public Lock getLock(){return this.lock;}
}

