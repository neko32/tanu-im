package org.tanuneko.im.user;

import org.tanuneko.im.model.User;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by neko32 on 2016/12/06.
 */
@SuppressWarnings("ALL")
public class UserEntry {

    private User user;
    private AtomicInteger counter;
    private int currentStatus = CTR_ACTIVE;
    public static final int CTR_ACTIVE = 0;
    public static final int CTR_NOT_ACTIVE = 1;
    public static final int CTR_LEAVE = 3;

    public UserEntry(User user) {
        this.user = user;
        counter = new AtomicInteger();
    }

    public int increment() {
        counter.getAndIncrement();
        if(counter.get() >= CTR_LEAVE) {
            currentStatus = CTR_LEAVE;
        } else if(counter.get() >= CTR_NOT_ACTIVE) {
            currentStatus = CTR_NOT_ACTIVE;
        }
        return currentStatus;
    }

    public void reset() {
        counter.set(0);
        currentStatus = CTR_ACTIVE;
    }

    public int getCounter() {
        return counter.get();
    }
}
